package com.zetta.afcs;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.zetta.afcs.barcode.BarcodeCaptureActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    // UI references.
    private TextView mResultTextView;
    private Button bScan;
    private Button bSaveSettings;

    private User User;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.User = new User();
        this.mResultTextView = findViewById(R.id.result_textview);
        this.bScan = findViewById(R.id.button_scan_qr);
        this.bSaveSettings = findViewById(R.id.button_save_settings);

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
                intent.putExtra("username", this.User.Username);
                intent.putExtra("isAuthenticated", true);
            } else {
                intent.putExtra("username", "");
                intent.putExtra("isAuthenticated", false);
            }
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_ticketing) {
            if (!this.User.IsAuthenticated) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("username", "");
                intent.putExtra("isAuthenticated", false);
                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void scanQRCode(View view) {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        intent.putExtra("username", "");
        intent.putExtra("isAuthenticated", false);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    this.mResultTextView.setText(barcode.displayValue);
                } else {
                    mResultTextView.setText(R.string.no_barcode_captured);
                }
            } else {
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
            intent.putExtra("username", this.User.Username);
            intent.putExtra("isAuthenticated", true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }
    }

    public void saveSettings(View view) {
        // TODO:
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", this.User.Username);
        intent.putExtra("isAuthenticated", true);
        startActivity(intent);
        finish();
    }
}
