package com.zetta.afcs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zetta.afcs.api.ApiHelper;
import com.zetta.afcs.barcode.BarcodeCaptureActivity;
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

public class ReloadQRActivity extends AppCompatActivity {
    // UI References
    private Button bLoad5;
    private Button bLoad10;
    private Button bLoad20;
    private Button bLoad50;
    private Button bLoad100;
    private Button bLoad500;
    private Button bLoad1000;
    private EditText eReloadAmount;
    private Button bGO;

    private String QRHashKey = "";
    private String RefTrans = "";
    private Double loadAmount = 0.00;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;

    private ProgressDialog progressDialog;
    private DeviceHelper deviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bLoad5 = findViewById(R.id.button_reload_5);
        this.bLoad10 = findViewById(R.id.button_reload_10);
        this.bLoad20 = findViewById(R.id.button_reload_20);
        this.bLoad50 = findViewById(R.id.button_reload_50);
        this.bLoad100 = findViewById(R.id.button_reload_100);
        this.bLoad500 = findViewById(R.id.button_reload_500);
        this.bLoad1000 = findViewById(R.id.button_reload_1000);
        this.eReloadAmount = findViewById(R.id.reload_amount);
        this.bGO = findViewById(R.id.button_reload_go);

        this.deviceHelper = new DeviceHelper();

        this.bLoad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("5.00");
            }
        });
        this.bLoad10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("10.00");
            }
        });
        this.bLoad20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("20.00");
            }
        });
        this.bLoad50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("50.00");
            }
        });
        this.bLoad100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("100.00");
            }
        });
        this.bLoad500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("500.00");
            }
        });
        this.bLoad1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eReloadAmount.setError(null);
                eReloadAmount.setText("1000.00");
            }
        });

        this.bGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                eReloadAmount.setError(null);
                boolean cancel = false;
                View focusView = null;
                String eAmount = eReloadAmount.getText().toString().trim();
                Double amount = 0.00;

                if (TextUtils.isEmpty(eAmount)) {
                    eReloadAmount.setError(getString(R.string.error_field_required));
                    focusView = eReloadAmount;
                    cancel = true;
                }

                if (!TextUtils.isEmpty(eAmount)) {
                    amount = Double.parseDouble(eAmount);
                    if (amount < 5 || amount > 1000) {
                        eReloadAmount.setError(Message.INVALID_LOAD_AMOUNT);
                        focusView = eReloadAmount;
                        cancel = true;
                    }
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReloadQRActivity.this);
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
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String qrResult = barcode.displayValue;
                    String serial_no = this.deviceHelper.getSerial();

                    new ReloadQRActivity.JsonTask().execute(ApiHelper.apiURL
                            , ApiHelper.SqlCodeReloadQR
                            , serial_no
                            , qrResult);
                }
            } else {
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format)
                        , CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ReloadQRActivity.this);
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
                String jsonInputString = String.format(Locale.ENGLISH, "{ \"%s\": \"%s\", \"parameters\": { \"serial_no\": \"%s\", \"hash_key\": \"%s\", \"payment_amount\": \"%.2f\" } }"
                        , ApiHelper.SqlCodeKey, params[1], params[2], params[3], loadAmount);

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
                    Dialog dialog = CommonHelper.showDialog(ReloadQRActivity.this
                            , Message.Title.ERROR
                            , Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (rows.length() > 0) {
                            String isValid = rows.getJSONObject(0).get("is_valid").toString();
                            String msg = rows.getJSONObject(0).get("msg").toString();
                            String current_balance_amount = rows.getJSONObject(0).get("current_balance_amount").toString();

                            if (isValid.toUpperCase().equals("Y")) {
                                Dialog dialog = CommonHelper.showDialog(ReloadQRActivity.this
                                        , Message.Title.INFO
                                        , String.format(Locale.ENGLISH, "%s Current balance: %.2f", msg, Double.parseDouble(current_balance_amount)));
                                dialog.show();
                            } else {
                                Dialog dialog = CommonHelper.showDialog(ReloadQRActivity.this
                                        , Message.Title.ERROR
                                        , String.format(Locale.ENGLISH, "%s Current balance: %.2f", msg, Double.parseDouble(current_balance_amount)));
                                dialog.show();
                            }
                        } else {
                            Dialog dialog = CommonHelper.showDialog(ReloadQRActivity.this
                                    , Message.Title.ERROR
                                    , Message.INVALID_TRANSACTION);
                            dialog.show();
                        }
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(ReloadQRActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_OCCURRED_API);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------

}
