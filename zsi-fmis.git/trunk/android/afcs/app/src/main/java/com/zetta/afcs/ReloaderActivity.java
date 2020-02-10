package com.zetta.afcs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zetta.afcs.api.ApiHelper;
import com.zetta.afcs.printerhelper.utils.AidlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ReloaderActivity extends AppCompatActivity {
    // UI References
    private Button bLoad50;
    private Button bLoad100;
    private Button bLoad200;
    private Button bLoad300;
    private Button bLoad500;
    private Button bLoad1000;
    private Button bReprint;
    private TextView tRefTrans;

    private String QRHashKey = "";
    private String RefTrans = "";
    private Double loadAmount = 0.00;

    private ProgressDialog progressDialog;
    private DeviceHelper deviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reloader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bLoad50 = findViewById(R.id.button_load_50);
        this.bLoad100 = findViewById(R.id.button_load_100);
        this.bLoad200 = findViewById(R.id.button_load_200);
        this.bLoad300 = findViewById(R.id.button_load_300);
        this.bLoad500 = findViewById(R.id.button_load_500);
        this.bLoad1000 = findViewById(R.id.button_load_1000);
        this.bReprint = findViewById(R.id.button_load_reprint);
        this.tRefTrans = findViewById(R.id.txtRefTrans);

        this.deviceHelper = new DeviceHelper();

        this.bLoad50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view, 50);
            }
        });
        this.bLoad100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view, 100);
            }
        });
        this.bLoad200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view, 200);
            }
        });
        this.bLoad300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view,300);
            }
        });
        this.bLoad500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view, 500);
            }
        });
        this.bLoad1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(view,1000);
            }
        });
        this.bReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reprint(QRHashKey, RefTrans);
            }
        });

        // Initialize the printer.
        AidlUtil.getInstance().initPrinter();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void load(View view, double amount) {
        this.loadAmount = amount;
        String serial_no = this.deviceHelper.getSerial();
        new ReloaderActivity.JsonTask().execute(ApiHelper.apiURL
                , ApiHelper.SqlCodeReLoader
                , serial_no
        );
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ReloaderActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                // Post data.
                String jsonInputString = String.format(Locale.ENGLISH, "{ \"%s\": \"%s\", \"parameters\": { \"serial_no\": \"%s\", \"amount\": \"%.2f\" } }"
                        , ApiHelper.SqlCodeKey, params[1], params[2], loadAmount);

                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    //System.out.println(response.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                if (result.trim().equals("")) {
                    Dialog dialog = CommonHelper.showDialog(ReloaderActivity.this
                            , Message.Title.ERROR
                            , Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (rows.length() > 0) {
                            // 1. Get the returned values from the api and store into the global variable to be used for printing.
                            QRHashKey = rows.getJSONObject(0).get("hash_key").toString();
                            RefTrans = rows.getJSONObject(0).get("ref_trans").toString();
                            tRefTrans.setText(String.format("Ref. No. %s", RefTrans));
                            print(QRHashKey, RefTrans);
                        } else {
                            Dialog dialog = CommonHelper.showDialog(ReloaderActivity.this
                                    , Message.Title.ERROR
                                    , Message.INVALID_TRANSACTION);
                            dialog.show();
                        }
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(ReloaderActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_OCCURRED_API);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------

    private void print(String qrHashKey, String refTrans) {
        if (!qrHashKey.trim().equals("")) {
            LoadReceipt receipt = new LoadReceipt();

            receipt.setHashKey(qrHashKey);
            receipt.setRefTrans(refTrans);

            AidlUtil.getInstance().printLoadReceipt(receipt);
        }
    }

    private void reprint(String qrHashKey, String refTrans) {
        this.print(qrHashKey, refTrans);
    }
}
