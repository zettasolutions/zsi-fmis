package com.zetta.afcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Message {
    public static String ERROR_OCCURRED = "An error occurred. Please try again.";
    public static String MISSING_SETTINGS = "Missing settings file.";
    public static String STORAGE_NOT_AVAILABLE = "External storage is not available.";
    public static String USER_NOT_FOUND = "User not found.";
    public static String VEHICLE_NOT_FOUND = "Vehicle not found.";
    public static String VEHICLE_INACTIVE = "Vehicle is currently inactive.";
    public static String CONNECTION_ERROR = "Connection error. Kindly check your internet connection and try again.";
    public static String ROUTE_UPDATE_FAILED = "Failed to update the route info settings. Kindly check your internet connection and try again.";
    public static String INVALID_ROUTE = "Invalid route selected.";
    public static String INVALID_PASSENGER_COUNT = "Invalid passenger count.";

    public static void show(String msg, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return ;
            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    public static class Title {
        public static String INFO = "Info";
        public static String WARN = "Warn";
        public static String ERROR = "Error";
    }
}