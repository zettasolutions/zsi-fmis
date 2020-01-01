package com.zetta.afcs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.File;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    // UI references.
    private TextView mResultTextView;
    private Button bScan;
    private Button bSaveSettings;
    private TextView tVehicleName;
    private TextView tVehiclePlate;
    private TextView tRouteInfo;
    private TextView tRouteDetails;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;

    private ProgressDialog progressDialog;

    private FileHelper FileHelper;
    private User User;
    private List<SettingsModel> SettingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.FileHelper = new FileHelper();
        this.SettingsList = new ArrayList<>();
        this.User = new User();
        this.mResultTextView = findViewById(R.id.result_textview);
        this.bScan = findViewById(R.id.button_scan_qr);
        this.bSaveSettings = findViewById(R.id.button_save_settings);
        this.tVehicleName = findViewById(R.id.txtVehicleName);
        this.tVehiclePlate = findViewById(R.id.txtVehiclePlate);
        this.tRouteInfo = findViewById(R.id.txtVehicleRouteInfo);
        this.tRouteDetails = findViewById(R.id.txtRouteDetails);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.User.setUsername(extras.getString("username"));
            this.User.setIsAuthenticated(extras.getBoolean("isAuthenticated"));
        } else {
            this.User.IsAuthenticated = false;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login_menu = menu.findItem(R.id.action_login);
        MenuItem logout_menu = menu.findItem(R.id.action_logout);
        MenuItem ticketing_menu = menu.findItem(R.id.action_ticketing);
        if (this.User.IsAuthenticated) {
            login_menu.setVisible(false);
            logout_menu.setVisible(true);
            ticketing_menu.setVisible(true);
        } else {
            login_menu.setVisible(true);
            logout_menu.setVisible(false);
            ticketing_menu.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            if (this.User.IsAuthenticated) {
//                intent.putExtra("username", this.User.Username);
//                intent.putExtra("isAuthenticated", true);
                intent.putExtra(Common.BundleExtras.Username, this.User.Username);
                intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
            } else {
//                intent.putExtra("username", "");
//                intent.putExtra("isAuthenticated", false);
                intent.putExtra(Common.BundleExtras.Username, "");
                intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
            }
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.putExtra("username", "");
//            intent.putExtra("isAuthenticated", false);
            intent.putExtra(Common.BundleExtras.Username, "");
            intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("username", "");
//            intent.putExtra("isAuthenticated", false);
            intent.putExtra(Common.BundleExtras.Username, "");
            intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_ticketing) {
            if (!this.User.IsAuthenticated) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent.putExtra("username", "");
//                intent.putExtra("isAuthenticated", false);
                intent.putExtra(Common.BundleExtras.Username, "");
                intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void scanQRCode(View view) {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
//        intent.putExtra("username", "");
//        intent.putExtra("isAuthenticated", false);
        intent.putExtra(Common.BundleExtras.Username, "");
        intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String qrResult = barcode.displayValue;

                    new SettingsActivity.JsonTask().execute(ApiHelper.apiURL, ApiHelper.SqlCodeSettings, qrResult);
                    //this.mResultTextView.setText(barcode.displayValue);
                    this.mResultTextView.setText("");
                } else {
                    mResultTextView.setText(R.string.no_barcode_captured);
                    this.resetVehicleInfo();
                }
            } else {
                this.resetVehicleInfo();
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.User.IsAuthenticated) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("username", this.User.Username);
//            intent.putExtra("isAuthenticated", true);
            intent.putExtra(Common.BundleExtras.Username, this.User.Username);
            intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.putExtra("username", "");
//            intent.putExtra("isAuthenticated", false);
            intent.putExtra(Common.BundleExtras.Username, "");
            intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Saves the settings to the app's database.
     * @param view The view object.
     */
    public void saveSettings(View view) {
        if (this.SettingsList != null && !this.SettingsList.isEmpty()) {
            // Save the settings list to the app's resource text file.
            Vehicle vehicle = this.FileHelper.updateSettingsFile(this, this.SettingsList);
            if (vehicle == null) {
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.ROUTE_UPDATE_FAILED);
                dialog.show();
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("username", this.User.Username);
//            intent.putExtra("isAuthenticated", true);
            intent.putExtra(Common.BundleExtras.Username, this.User.Username);
            intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
            startActivity(intent);
            finish();
        }
    }

    private void resetVehicleInfo() {
        tVehicleName.setText("");
        tVehiclePlate.setText("");
        tRouteInfo.setText("");
        tRouteDetails.setText("");
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

                // Post data.
                String jsonInputString = String.format("{ \"%s\": \"%s\", \"parameters\": {\"hash_key\": \"%s\"} }",
                        ApiHelper.SqlCodeKey, params[1], params[2]);

                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    System.out.println(response.toString());
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
                    Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (rows.length() > 0) {
                            String isActive = rows.getJSONObject(0).get("is_active").toString();
                            if (isActive.toUpperCase().equals("Y")) {
                                // 1. Populate the screen display.
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
                                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.VEHICLE_INACTIVE);
                                dialog.show();
                            }
                        } else {
                            resetVehicleInfo();
                            Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.VEHICLE_NOT_FOUND);
                            dialog.show();
                        }
                    } else {
                        resetVehicleInfo();
                        Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.VEHICLE_NOT_FOUND);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                resetVehicleInfo();
                Dialog dialog = CommonHelper.showDialog(SettingsActivity.this, Message.Title.ERROR, Message.ERROR_OCCURRED);
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
}