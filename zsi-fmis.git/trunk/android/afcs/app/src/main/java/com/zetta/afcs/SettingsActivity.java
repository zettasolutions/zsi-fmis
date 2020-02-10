package com.zetta.afcs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zetta.afcs.api.ApiHelper;
import com.zetta.afcs.api.SettingsModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    // UI references.
    private TextView mResultTextView;
    private Button bScan;
    private Button bSaveSettings;
    private Button bSyncSettings;
    private TextView tVehicleName;
    private TextView tVehiclePlate;
    private TextView tRouteInfo;
    private TextView tRouteDetails;
    private TextView tBaseFare;
    private TextView tBaseKm;
    private TextView tSucceedingKmFare;
    private TextView tStudentDiscount;
    private TextView tSeniorDiscount;
    private TextView tPWDDiscount;

    // Global variable of the request that holds the request for the api.
    private int gRequestCode;
    private boolean hasConfiguration = false;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;
    private int CONFIG_SETTINGS_REQUEST_CODE = 2;

    private ProgressDialog progressDialog;

    private FileHelper FileHelper;
    private User User;
    private List<SettingsModel> SettingsList;
    private Configuration Configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.FileHelper = new FileHelper();
        this.SettingsList = new ArrayList<>();
        this.Configuration = new Configuration();
        this.User = new User();
        this.mResultTextView = findViewById(R.id.result_textview);
        this.bScan = findViewById(R.id.button_scan_qr);
        this.bSyncSettings = findViewById(R.id.button_sync_settings);
        this.bSaveSettings = findViewById(R.id.button_save_settings);
        this.tVehicleName = findViewById(R.id.txtVehicleName);
        this.tVehiclePlate = findViewById(R.id.txtVehiclePlate);
        this.tRouteInfo = findViewById(R.id.txtVehicleRouteInfo);
        this.tRouteDetails = findViewById(R.id.txtRouteDetails);
        this.tBaseFare = findViewById(R.id.txtBaseFare);
        this.tBaseKm = findViewById(R.id.txtBaseKm);
        this.tSucceedingKmFare = findViewById(R.id.txtSucceedingKmFare);
        this.tStudentDiscount = findViewById(R.id.txtStudentDiscount);
        this.tSeniorDiscount = findViewById(R.id.txtSeniorDiscount);
        this.tPWDDiscount = findViewById(R.id.txtPWDDiscount);

        this.bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQRCode(view);
            }
        });

        this.bSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings(view);
            }
        });

        this.bSyncSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncConfiguration(view);
            }
        });

        List<SettingsModel> settings = this.FileHelper.getSettingsList(this);
        if (settings != null && !settings.isEmpty()) {
            // Populate the UI.
            this.tVehicleName.setText(settings.get(0).getAssetCode());
            this.tVehiclePlate.setText(settings.get(0).getAssetNo());
            this.tRouteInfo.setText(settings.get(0).getRouteCode());
            this.tRouteDetails.setText(settings.get(0).getRouteDescription());
        }

        Configuration configuration = this.FileHelper.getConfiguration(this);
        if (configuration != null) {
            // Populate the UI.
            this.tBaseFare.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getBaseFare()));
            this.tBaseKm.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getBaseKm()));
            this.tSucceedingKmFare.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getSucceedingKmFare()));
            this.tStudentDiscount.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getStudentDiscountPercent()));
            this.tSeniorDiscount.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getSeniorDiscountPercent()));
            this.tPWDDiscount.setText(String.format(Locale.ENGLISH, "%.2f", configuration.getPwdDiscountPercent()));
        }
    }

    public void scanQRCode(View view) {
        this.gRequestCode = BARCODE_READER_REQUEST_CODE;
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        intent.putExtra(Common.BundleExtras.Username, "");
        intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    public void syncConfiguration(View view) {
        this.gRequestCode = CONFIG_SETTINGS_REQUEST_CODE;
        new SettingsActivity.JsonTask().execute(ApiHelper.apiURL
            , ApiHelper.SqlCodeConfigurations);
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

                    new SettingsActivity.JsonTask().execute(ApiHelper.apiURL
                            , ApiHelper.SqlCodeSettings
                            , qrResult);
                    //this.mResultTextView.setText(barcode.displayValue);
                    this.mResultTextView.setText("");
                } else {
                    mResultTextView.setText(R.string.no_barcode_captured);
                    this.resetVehicleInfo();
                }
            } else {
                this.resetVehicleInfo();
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

    /**
     * Saves the settings to the app's database.
     * @param view The view object.
     */
    public void saveSettings(View view) {
        if (this.hasConfiguration) {
            if (this.SettingsList != null && !this.SettingsList.isEmpty()) {
                // Save the settings list to the app's resource text file.
                Vehicle vehicle = this.FileHelper.updateSettingsFile(this, this.SettingsList);
                if (vehicle == null) {
                    Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                            , Message.Title.ERROR
                            , Message.ROUTE_UPDATE_FAILED);
                    dialog.show();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(Common.BundleExtras.Username, this.User.Username);
                intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
                intent.putExtra(Common.BundleExtras.FirstName, this.User.FirstName);
                intent.putExtra(Common.BundleExtras.LastName, this.User.LastName);
                intent.putExtra(Common.BundleExtras.Position, this.User.Position);
                startActivity(intent);
                finish();
            } else {
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                        , Message.Title.ERROR
                        , Message.INVALID_SETTINGS);
                dialog.show();
            }
        } else {
            Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                    , Message.Title.ERROR
                    , Message.CONFIGURATION_NEEDS_SYNC);
            dialog.show();
        }
    }

    /**
     * Saves the configuration to the app's database.
     */
    public void saveConfiguration() {
        if (this.Configuration != null) {
            // Save the configuration to the app's resource text file.
            boolean isUpdated = this.FileHelper.updateConfigurationFile(this, this.Configuration);
            if (!isUpdated) {
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                        , Message.Title.ERROR
                        , Message.CONFIGURATION_UPDATE_FAILED);
                dialog.show();
            } else {
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                        , Message.Title.INFO
                        , Message.CONFIGURATION_UPDATE_SUCCESS);
                dialog.show();

                this.hasConfiguration = true;
            }
        } else {
            this.hasConfiguration = false;
            Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                    , Message.Title.ERROR
                    , Message.INVALID_SETTINGS);
            dialog.show();
        }
    }

    /**
     * Resets the UI values for the vehicle.
     */
    private void resetVehicleInfo() {
        tVehicleName.setText("");
        tVehiclePlate.setText("");
        tRouteInfo.setText("");
        tRouteDetails.setText("");
    }

    /**
     * Resets the UI values for the configuration.
     */
    private void resetConfigurationInfo() {
        tBaseFare.setText("");
        tBaseKm.setText("");
        tSucceedingKmFare.setText("");
        tStudentDiscount.setText("");
        tSeniorDiscount.setText("");
        tPWDDiscount.setText("");
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SettingsActivity.this);
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
                if (gRequestCode == CONFIG_SETTINGS_REQUEST_CODE) {
                    // Post data.
                    jsonInputString = String.format("{ \"%s\": \"%s\" }"
                            , ApiHelper.SqlCodeKey, params[1]);
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
                    Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
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
                                    // 1. Populate the vehicle display.
                                    String vehicleName = rows.getJSONObject(0).get("asset_code").toString();
                                    String plateNo = rows.getJSONObject(0).get("asset_no").toString();
                                    String routeInfo = rows.getJSONObject(0).get("route_code").toString();
                                    String routeDescription = rows.getJSONObject(0).get("route_desc").toString();

                                    tVehicleName.setText(vehicleName);
                                    tVehiclePlate.setText(plateNo);
                                    tRouteInfo.setText(routeInfo);
                                    tRouteDetails.setText(routeDescription);

                                    // 2. Store the values in a list as reference when the settings is saved.
                                    SettingsList = getSettings(rows);
                                } else {
                                    resetVehicleInfo();
                                    Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                                            , Message.Title.ERROR
                                            , Message.VEHICLE_INACTIVE);
                                    dialog.show();
                                }
                            } else {
                                resetVehicleInfo();
                                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                                        , Message.Title.ERROR
                                        , Message.VEHICLE_NOT_FOUND);
                                dialog.show();
                            }
                        } else if (gRequestCode == CONFIG_SETTINGS_REQUEST_CODE) {
                            if (rows.length() > 0) {
                                // 1. Populate the config display.
                                Double baseFare = Double.parseDouble(rows.getJSONObject(0).get("base_fare").toString());
                                Double baseKm = Double.parseDouble(rows.getJSONObject(0).get("base_kms").toString());
                                Double succeedingKmFare = Double.parseDouble(rows.getJSONObject(0).get("succeeding_km_fare").toString());
                                Double studentDiscount = Double.parseDouble(rows.getJSONObject(0).get("student_discount").toString());
                                Double seniorDiscount = Double.parseDouble(rows.getJSONObject(0).get("senior_discount").toString());
                                Double pwdDiscount = Double.parseDouble(rows.getJSONObject(0).get("pwd_discount").toString());

                                tBaseFare.setText(String.format(Locale.ENGLISH, "%.2f", baseFare));
                                tBaseKm.setText(String.format(Locale.ENGLISH, "%.2f", baseKm));
                                tSucceedingKmFare.setText(String.format(Locale.ENGLISH, "%.2f", succeedingKmFare));
                                tStudentDiscount.setText(String.format(Locale.ENGLISH, "%.2f", studentDiscount));
                                tSeniorDiscount.setText(String.format(Locale.ENGLISH, "%.2f", seniorDiscount));
                                tPWDDiscount.setText(String.format(Locale.ENGLISH, "%.2f", pwdDiscount));

                                // 2. Save the values into an object for reference when the configurations is saved.
                                Configuration = getConfiguration(rows);
                                saveConfiguration();
                            } else {
                                resetConfigurationInfo();
                                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                                        , Message.Title.ERROR
                                        , Message.VEHICLE_NOT_FOUND);
                                dialog.show();
                            }
                        }
                    } else {
                        resetVehicleInfo();
                        resetConfigurationInfo();
                        Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                                , Message.Title.ERROR
                                , Message.ERROR_OCCURRED_API);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                resetVehicleInfo();
                resetConfigurationInfo();
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_OCCURRED_API);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------

    private List<SettingsModel> getSettings(JSONArray rows) throws JSONException {
        List<SettingsModel> settingsList = new ArrayList<>();

        if (rows != null && rows.length() > 0) {
            for (int i = 0; i < rows.length(); i++) {
                SettingsModel settingsModel = new SettingsModel();

                settingsModel.setAssetCode(rows.getJSONObject(i).get("asset_code").toString());
                settingsModel.setAssetNo(rows.getJSONObject(i).get("asset_no").toString());
                settingsModel.setRouteCode(rows.getJSONObject(i).get("route_code").toString());
                settingsModel.setRouteDescription(rows.getJSONObject(i).get("route_desc").toString());
                settingsModel.setRouteNo(Integer.parseInt(rows.getJSONObject(i).get("route_no").toString()));
                settingsModel.setLocation(rows.getJSONObject(i).get("location").toString());
                settingsModel.setDistanceKm(Float.parseFloat(rows.getJSONObject(i).get("distance_km").toString()));
                settingsModel.setSeqNo(Integer.parseInt(rows.getJSONObject(i).get("seq_no").toString()));

                settingsList.add(settingsModel);
            }
        }

        return settingsList;
    }

    private Configuration getConfiguration(JSONArray rows) throws JSONException {
        Configuration configuration = new Configuration();

        if (rows != null && rows.length() > 0) {
            configuration.setBaseFare(Double.parseDouble(rows.getJSONObject(0).get("base_fare").toString()));
            configuration.setBaseKm(Double.parseDouble(rows.getJSONObject(0).get("base_kms").toString()));
            configuration.setSucceedingKmFare(Double.parseDouble(rows.getJSONObject(0).get("succeeding_km_fare").toString()));
            configuration.setStudentDiscountPercent(Double.parseDouble(rows.getJSONObject(0).get("student_discount").toString()));
            configuration.setSeniorDiscountPercent(Double.parseDouble(rows.getJSONObject(0).get("senior_discount").toString()));
            configuration.setPwdDiscountPercent(Double.parseDouble(rows.getJSONObject(0).get("pwd_discount").toString()));
        }

        return configuration;
    }
}