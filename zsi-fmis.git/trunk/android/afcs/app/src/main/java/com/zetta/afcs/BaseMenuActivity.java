package com.zetta.afcs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseMenuActivity extends AppCompatActivity {
    private User User;
    //ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setIcon(R.drawable.ic_social_share);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.app_bar, null);
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setCustomView(v);

        this.User = new User();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            this.User.setUsername(extras.getString("username"));
//            this.User.setIsAuthenticated(extras.getBoolean("isAuthenticated"));
            this.User.setUsername(extras.getString(Common.BundleExtras.Username));
            this.User.setIsAuthenticated(extras.getBoolean(Common.BundleExtras.IsAuthenticated));
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

//        MenuItem login_menu = menu.findItem(R.id.action_login);
//        MenuItem logout_menu = menu.findItem(R.id.action_logout);
//        if (this.User.IsAuthenticated) {
//            login_menu.setVisible(false);
//            logout_menu.setVisible(true);
//        } else {
//            login_menu.setVisible(true);
//            logout_menu.setVisible(false);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

//        if (id == R.id.action_login) {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.putExtra("username", "");
//            intent.putExtra("isAuthenticated", false);
//            startActivity(intent);
//            finish();
//        }
//
//        if (id == R.id.action_logout) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("username", "");
//            intent.putExtra("isAuthenticated", false);
//            startActivity(intent);
//            finish();
//        }

//        if (this.User.IsAuthenticated) {
//            Intent intent = new Intent(getApplicationContext(), TicketingActivity.class);
//            intent.putExtra("username", this.User.Username);
//            intent.putExtra("isAuthenticated", true);
//            startActivity(intent);
//            finish();
//        }

        return super.onOptionsItemSelected(item);
    }
}