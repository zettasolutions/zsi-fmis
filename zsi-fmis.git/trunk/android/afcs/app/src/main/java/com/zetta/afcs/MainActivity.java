package com.zetta.afcs;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FileHelper fileHelper;
    private User User;
    private AdView mAdView;
    private TextView tFullname;
    private TextView tPosition;
    private Context mContext;

    private boolean hasSettings = false;
    private boolean hasConfig = false;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;
    private DeviceHelper deviceHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fileHelper = new FileHelper(this);
        this.User = new User();
        this.tFullname = findViewById(R.id.tFullname);
        this.tPosition = findViewById(R.id.tPosition);

        this.mContext = this;
        this.deviceHelper = new DeviceHelper();

        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        // ----------- DISPLAY ADMOB ADS ------------------------------------------------------------------------------------
        //
        // NOTE:    Use the test admob when under development because Google might think of an auto-clicker for the ads
        //          every time the android app is run in debug mode.
        //
        // TODO: Update manifest.
        // ca-app-pub-2093465683432076~7329318157 -- ACTUAL ID
        // ca-app-pub-3940256099942544~3347511713 -- TEST ID

        // TODO: Update content_main.
        //  ads:adUnitId="ca-app-pub-2093465683432076/4198907727"> -- actual admob
        //  ads:adUnitId="ca-app-pub-3940256099942544/6300978111"> -- google test admob

        // TODO:
        //  TEST. Comment when building apk.
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        // TODO:
        //  ACTUAL. Uncomment when building apk.
        //MobileAds.initialize(this, "ca-app-pub-2093465683432076~7329318157");
        //
        // ----------- DISPLAY ADMOB ADS ------------------------------------------------------------------------------------

        this.mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        this.mAdView.loadAd(adRequest);
        this.mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        List<SettingsModel> settings = this.fileHelper.getSettingsList(this);
        if (settings != null && !settings.isEmpty())
            this.hasSettings = true;
        Configuration configuration = this.fileHelper.getConfiguration(this);
        if (configuration != null)
            this.hasConfig = true;

        if (!hasSettings || !hasConfig) {
            Dialog dialog = CommonHelper.showDialog(this
                    , Message.Title.ERROR
                    , Message.INCOMPLETE_SETTINGS);
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem ticketing_menu = menu.findItem(R.id.action_ticketing);

        ticketing_menu.setVisible(true);

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
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_driver_pao) {
            Intent intent = new Intent(getApplicationContext(), DriverPaoActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_ticketing) {
            Intent intent = new Intent(getApplicationContext(), TicketingActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_reloader) {
            Intent intent = new Intent(getApplicationContext(), ReloaderActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_load_qr) {
            Intent intent = new Intent(getApplicationContext(), ReloadQRActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_scan_qr_balance) {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

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
                    new MainActivity.JsonTask().execute(ApiHelper.apiURL
                            , ApiHelper.SqlCodeScanQRBalance
                            , serial_no
                            , qrResult);
                }
            } else {
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
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
                String jsonInputString = String.format("{ \"%s\": \"%s\", \"parameters\": {\"serial_no\": \"%s\", \"hash_key\": \"%s\"} }"
                            , ApiHelper.SqlCodeKey, params[1], params[2], params[3]);

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
                    Dialog dialog = CommonHelper.showDialog(MainActivity.this
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
                            String current_balance = rows.getJSONObject(0).get("current_balance").toString();
                            Double balance = Double.parseDouble(current_balance);

                            if (isValid.toUpperCase().equals("Y")) {
                                Dialog dialog = CommonHelper.showDialog(MainActivity.this
                                        , Message.Title.INFO
                                        , String.format(Locale.ENGLISH, "Current balance is %.2f.", balance));
                                dialog.show();
                            } else {
                                Dialog dialog = CommonHelper.showDialog(MainActivity.this
                                        , Message.Title.ERROR
                                        , msg);
                                dialog.show();
                            }
                        } else {
                            Dialog dialog = CommonHelper.showDialog(MainActivity.this
                                    , Message.Title.ERROR
                                    , Message.INVALID_QR);
                            dialog.show();
                        }
                    } else {
                        Dialog dialog = CommonHelper.showDialog(MainActivity.this
                                , Message.Title.ERROR
                                , Message.CONNECTION_ERROR);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(MainActivity.this
                        , Message.Title.ERROR
                        , Message.CONNECTION_ERROR);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------
}