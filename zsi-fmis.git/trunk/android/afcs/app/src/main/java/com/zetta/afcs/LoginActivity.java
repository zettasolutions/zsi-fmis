package com.zetta.afcs;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText eUserName;
    private EditText ePassword;
    private Button bLogin;
    private Button bQRLogin;

    private String LOG_TAG = "";
    private String STATUS_MESSAGE = "";
    private User User;
    private int BARCODE_READER_REQUEST_CODE = 1;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.eUserName = findViewById(R.id.eUsername);
        this.ePassword = findViewById(R.id.ePassword);
        this.bLogin = findViewById(R.id.bLogin);
        this.bQRLogin = findViewById(R.id.bQRLogin);

        this.User = new User();

        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        this.ePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        this.bLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        this.bQRLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptQRLogin(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String qrResult = barcode.displayValue;

                    //String user_hash = "95561D62-0BED-4E1D-B208-25721997F5BA"; gtabinas
                    new LoginActivity.JsonTask().execute(ApiHelper.apiURL, ApiHelper.SqlCodeUsers, qrResult);
                }
            } else {
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        this.eUserName.setError(null);
        this.ePassword.setError(null);

        // Store values at the time of the login attempt.
        this.User.Username = this.eUserName.getText().toString().trim();
        this.User.Password = this.ePassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(this.User.Username)) {
            this.eUserName.setError(getString(R.string.error_field_required));
            focusView = this.eUserName;
            cancel = true;
        }

        // Check for a blank password.
        if (TextUtils.isEmpty(this.User.Password)) {
            this.ePassword.setError(getString(R.string.error_field_required));
            focusView = this.ePassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(this.User.Password) && !isValidPassword(this.User.Password)) {
            this.ePassword.setError(getString(R.string.error_invalid_password));
            focusView = this.ePassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (login()) {
                // Store user info for global variable.
                // Login the user and redirect to the main.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("username", this.User.Username);
//                intent.putExtra("isAuthenticated", true);
                intent.putExtra(Common.BundleExtras.Username, this.User.Username);
                intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
                startActivity(intent);
                finish();
            } else {
                Dialog dialog = CommonHelper.showDialog(this, Message.Title.ERROR, Message.USER_NOT_FOUND);
                dialog.show();
            }
        }
    }

    private boolean login() {
        Boolean isAuthenticated = this.AuthenticateUser(this.User);
        if (isAuthenticated) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the state of the button.
     */
    private void updateButtonState(Button button, boolean state) {
        button.setEnabled(state);
    }

    private boolean isValidPassword(String password) {
        return password.length() > 4;
    }

    public Boolean AuthenticateUser(User user) {
        if (user != null) {
            try {
                String username = "";
                String password = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("user.zta")));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    username = data[0];
                    password = data[1];
                }
                if (user.getUsername().equals(username) && user.getPassword().equals(password))
                    return true;
                else
                    return false;
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
    Attempt login using the QR code.
     */
    private void attemptQRLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
//        intent.putExtra("username", "");
//        intent.putExtra("isAuthenticated", false);
        intent.putExtra(Common.BundleExtras.Username, "");
        intent.putExtra(Common.BundleExtras.IsAuthenticated, false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE );
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
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
                    Dialog dialog = CommonHelper.showDialog(LoginActivity.this, Message.Title.ERROR, Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (rows.length() > 0) {
                            // Store user info for global variable.
                            User.Username = rows.getJSONObject(0).get("logon").toString();
                            User.FirstName = rows.getJSONObject(0).get("first_name").toString();
                            User.LastName = rows.getJSONObject(0).get("last_name").toString();

                            // Login the user and redirect to the main.
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.putExtra("username", User.Username);
//                            intent.putExtra("firstName", User.FirstName);
//                            intent.putExtra("lastName", User.LastName);
//                            intent.putExtra("isAuthenticated", true);
                            intent.putExtra(Common.BundleExtras.Username, User.Username);
                            intent.putExtra(Common.BundleExtras.FirstName, User.FirstName);
                            intent.putExtra(Common.BundleExtras.LastName, User.LastName);
                            intent.putExtra(Common.BundleExtras.IsAuthenticated, true);
                            startActivity(intent);
                            finish();
                        } else {
                            Dialog dialog = CommonHelper.showDialog(LoginActivity.this, Message.Title.ERROR, Message.USER_NOT_FOUND);
                            dialog.show();
                        }
                    } else {
                        Dialog dialog = CommonHelper.showDialog(LoginActivity.this, Message.Title.ERROR, Message.USER_NOT_FOUND);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(LoginActivity.this, Message.Title.ERROR, Message.ERROR_OCCURRED);
                dialog.show();
            }

        }
        // --------------------- API calls -------------------------------------------------
    }
}