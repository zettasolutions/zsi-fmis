package com.zetta.afcs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectionHelper extends AppCompatActivity {

    /**
     * Determines if a network is available.
     * @return True or false.
     */
    public boolean IsNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * Determines if the device is connected to the internet.
     * @return True or false.
     */
    public boolean IsInternetConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}