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
    public static String ERROR_OCCURRED_API = "An error occurred while connecting to the api. Please try again.";
    public static String INVALID_SETTINGS = "Invalid settings found.";
    public static String CONFIGURATION_UPDATE_FAILED = "Failed to update the configuration. Kindly check your internet connection and try again.";
    public static String CONFIGURATION_UPDATE_SUCCESS = "Configuration updated successfully.";
    public static String CONFIGURATION_NEEDS_SYNC = "Configuration needs to be synced first.";
    public static String NO_DRIVER_PAO_REGISTERED = "No Driver or PAO registered in the application.";
    public static String ERROR_SAVE_DRIVER_PAO = "An error occurred when saving the driver / PAO.";
    public static String INCOMPLETE_SETTINGS = "Incomplete settings found. Make sure you have setup the settings.";
    public static String INVALID_DRIVER = "Invalid driver scanned.";
    public static String INVALID_PAO = "Invalid PAO scanned.";
    public static String ERROR_LOAD = "An error occurred when generating Load QR. Please try again.";
    public static String SUCCESS_LOAD = "Load QR generated successfully.";
    public static String INVALID_TRANSACTION = "Invalid transaction. Please try again.";
    public static String INVALID_QR = "Invalid QR Code. Please try again.";


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