package com.zetta.afcs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zetta.afcs.api.ApiHelper;
import com.zetta.afcs.barcode.BarcodeCaptureActivity;

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
import java.util.List;

public class DriverPaoActivity extends AppCompatActivity {
    // UI references.
    private Button bScanDriver;
    private Button bScanPao;
    private Button bSaveDriverPao;
    private ImageView imgDriver;
    private ImageView imgPao;
    private TextView tDriverName;
    private TextView tPaoName;

    private FileHelper FileHelper;
    private User User;
    private String DriverId = "";
    private String DriverName = "";
    private String PaoId = "";
    private String PaoName = "";
    private boolean isDriver = false;
    private boolean isPao = false;

    private ProgressDialog progressDialog;

    // Global variable of the request that holds the request for the api.
    private int gRequestCode;
    private int BARCODE_READER_REQUEST_CODE = 1;
    private String LOG_TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.FileHelper = new FileHelper();
        this.User = new User();
        this.bScanDriver = findViewById(R.id.bScanDriver);
        this.bScanPao = findViewById(R.id.bScanPao);
        this.bSaveDriverPao = findViewById(R.id.bSaveDriverPao);
        this.imgDriver = findViewById(R.id.imgDriver);
        this.imgPao = findViewById(R.id.imgPao);
        this.tDriverName = findViewById(R.id.lblDriverName);
        this.tPaoName = findViewById(R.id.lblPaoName);

        List<String> driverPao = this.FileHelper.getDriverPao(this);
        if (driverPao != null && !driverPao.isEmpty()) {
            if (driverPao.size() == 1) {
                this.DriverName = driverPao.get(0);
                this.PaoName = "";
            } else {
                this.DriverName = driverPao.get(0);
                this.PaoName = driverPao.get(1);
            }

            this.tDriverName.setText(this.DriverName);
            this.tPaoName.setText(this.PaoName);
        }

        this.bScanDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDriver = true;
                isPao = false;
                scanDriver(view);
            }
        });

        this.bScanPao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDriver = false;
                isPao = true;
                scanPao(view);
            }
        });

        this.bSaveDriverPao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDriverPao(view);
            }
        });
    }

    public void scanDriver(View view) {
        this.gRequestCode = BARCODE_READER_REQUEST_CODE;
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        intent.putExtra(Common.BundleExtras.Username, "");
        intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    public void scanPao(View view) {
        this.gRequestCode = BARCODE_READER_REQUEST_CODE;
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        intent.putExtra(Common.BundleExtras.Username, "");
        intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    public void saveDriverPao(View view) {
        if (!this.DriverName.isEmpty() && !this.PaoName.isEmpty()) {
            // Save the values to the app's resource text file.
            boolean isSaved = this.FileHelper.updateDriverPaoFile(this, this.DriverName, this.PaoName, this.DriverId, this.PaoId);
            if (!isSaved) {
                Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_SAVE_DRIVER_PAO);
                dialog.show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                    , Message.Title.ERROR
                    , Message.NO_DRIVER_PAO_REGISTERED);
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            this.gRequestCode = BARCODE_READER_REQUEST_CODE;
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String qrResult = barcode.displayValue;

                    new DriverPaoActivity.JsonTask().execute(ApiHelper.apiURL
                            , ApiHelper.SqlCodeUsers
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DriverPaoActivity.this);
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

                String jsonInputString = "";
                if (gRequestCode == BARCODE_READER_REQUEST_CODE) {
                    // Post data.
                    jsonInputString = String.format("{ \"%s\": \"%s\", \"parameters\": {\"hash_key\": \"%s\"} }"
                            , ApiHelper.SqlCodeKey, params[1], params[2]);
                }

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
                    Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                            , Message.Title.ERROR
                            , Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (gRequestCode == BARCODE_READER_REQUEST_CODE) {
                            if (rows.length() > 0) {
                                String isActive = rows.getJSONObject(0).get("is_active").toString();
                                if (isActive.toUpperCase().equals("Y")) {
                                    // 1. Populate the image display.
                                    String full_name = rows.getJSONObject(0).get("full_name").toString();
                                    String position = rows.getJSONObject(0).get("position").toString();
                                    String user_id = rows.getJSONObject(0).get("user_id").toString();
                                    if (isDriver) {
                                        if (position.toUpperCase().trim().equals("DRIVER")) {
                                            //imgDriver = rows.getJSONObject(0).get("asset_code").toString();
                                            DriverName = full_name;
                                            tDriverName.setText(full_name);
                                            DriverId = user_id;
                                        } else {
                                            Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                                                    , Message.Title.ERROR
                                                    , Message.INVALID_DRIVER);
                                            dialog.show();
                                        }
                                    }
                                    if (isPao) {
                                        if (!position.toUpperCase().trim().equals("DRIVER")) {
                                            //imgPao = rows.getJSONObject(0).get("asset_code").toString();
                                            PaoName = full_name;
                                            tPaoName.setText(full_name);
                                            PaoId = user_id;
                                        } else {
                                            Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                                                    , Message.Title.ERROR
                                                    , Message.INVALID_PAO);
                                            dialog.show();
                                        }
                                    }
                                } else {
                                    Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                                            , Message.Title.ERROR
                                            , Message.NO_DRIVER_PAO_REGISTERED);
                                    dialog.show();
                                }
                            } else {
                                Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                                        , Message.Title.ERROR
                                        , Message.NO_DRIVER_PAO_REGISTERED);
                                dialog.show();
                            }
                        }
                    } else {
                        Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                                , Message.Title.ERROR
                                , Message.ERROR_OCCURRED_API);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(DriverPaoActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_OCCURRED_API);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------
}