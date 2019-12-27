package com.zetta.afcs;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private FileHelper fileHelper;
    private User User;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fileHelper = new FileHelper(this);
        this.User = new User();

        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        // Display ads.
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

        // TODO: Update manifest.
        // Actual ID: ca-app-pub-2093465683432076~7329318157
        // Test ID: ca-app-pub-3940256099942544~3347511713
        // TODO: Update content_main
        //  ads:adUnitId="ca-app-pub-2093465683432076/4198907727"> -- actual admob
        //  ads:adUnitId="ca-app-pub-3940256099942544/6300978111"> -- google test admob

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        //MobileAds.initialize(this, "ca-app-pub-2093465683432076~7329318157");

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login_menu = menu.findItem(R.id.action_login);
        MenuItem logout_menu = menu.findItem(R.id.action_logout);
        MenuItem checklist_menu = menu.findItem(R.id.action_ticketing);
        if (this.User.IsAuthenticated) {
            login_menu.setVisible(false);
            logout_menu.setVisible(true);
            checklist_menu.setVisible(true);
        } else {
            login_menu.setVisible(true);
            logout_menu.setVisible(false);
            checklist_menu.setVisible(false);
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
            if (this.User.IsAuthenticated) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
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
            if (this.User.IsAuthenticated) {
                Intent intent = new Intent(getApplicationContext(), TicketingActivity.class);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this.User.IsAuthenticated) {

        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }
    }
}
