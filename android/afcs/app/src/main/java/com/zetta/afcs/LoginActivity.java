package com.zetta.afcs;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText eUserName;
    private EditText ePassword;
    private Button bLogin;

    private String STATUS_MESSAGE = "";
    private User User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.bLogin = findViewById(R.id.bLogin);
        this.eUserName = findViewById(R.id.eUsername);
        this.ePassword = findViewById(R.id.ePassword);

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
                intent.putExtra("username", this.User.Username);
                intent.putExtra("isAuthenticated", true);
                startActivity(intent);
                finish();
            } else {
                Dialog dialog = CommonHelper.showDialog(this, "Error", Message.USER_NOT_FOUND);
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

}

