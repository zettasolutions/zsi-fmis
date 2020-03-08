package com.zetta.afcs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button bLoad5;
    private Button bLoad10;
    private Button bLoad20;
    private Button bLoad50;
    private Button bLoad100;
    private Button bLoad500;
    private Button bLoad1000;
    private Button bReprint;
    private TextView tRefTrans;
    private EditText eLoadAmount;
    private Button bGO;

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

        this.bLoad5 = findViewById(R.id.button_load_5);
        this.bLoad10 = findViewById(R.id.button_load_10);
        this.bLoad20 = findViewById(R.id.button_load_20);
        this.bLoad50 = findViewById(R.id.button_load_50);
        this.bLoad100 = findViewById(R.id.button_load_100);
        this.bLoad500 = findViewById(R.id.button_load_500);
        this.bLoad1000 = findViewById(R.id.button_load_1000);
        this.bReprint = findViewById(R.id.button_load_reprint);
        this.tRefTrans = findViewById(R.id.txtRefTrans);
        this.eLoadAmount = findViewById(R.id.load_amount);
        this.bGO = findViewById(R.id.button_load_go);

        this.deviceHelper = new DeviceHelper();

        this.bLoad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("5.00");
            }
        });
        this.bLoad10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("10.00");
            }
        });
        this.bLoad20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("20.00");
            }
        });
        this.bLoad50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("50.00");
            }
        });
        this.bLoad100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("100.00");
            }
        });
        this.bLoad500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("500.00");
            }
        });
        this.bLoad1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eLoadAmount.setError(null);
                eLoadAmount.setText("1000.00");
            }
        });
        this.bGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                eLoadAmount.setError(null);
                boolean cancel = false;
                View focusView = null;
                String eAmount = eLoadAmount.getText().toString().trim();
                Double amount = 0.00;

                if (TextUtils.isEmpty(eAmount)) {
                    eLoadAmount.setError(getString(R.string.error_field_required));
                    focusView = eLoadAmount;
                    cancel = true;
                }

                if (!TextUtils.isEmpty(eAmount)) {
                    amount = Double.parseDouble(eAmount);
                    if (amount < 5 || amount > 1000) {
                        eLoadAmount.setError(Message.INVALID_LOAD_AMOUNT);
                        focusView = eLoadAmount;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReloaderActivity.this);
                    final Double finalAmount = amount;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            load(view, finalAmount);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Stuff to do
                        }
                    });

                    builder.setMessage("Entered amount is correct?");
                    builder.setTitle("Confirm");

                    AlertDialog d = builder.create();
                    d.show();
                }
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
