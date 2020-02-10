package com.zetta.afcs;

import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceHelper extends AppCompatActivity {

    public DeviceHelper() {}

    public String getSerial() {
        return Build.SERIAL;
    }
}