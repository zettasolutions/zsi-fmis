package com.zetta.afcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Message {
    public static String ERROR_OCCURRED = "An error occurred. Please try again.";
    public static String MISSING_SETTINGS = "Missing settings file.";
    public static String STORAGE_NOT_AVAILABLE = "External storage is not available.";
    public static String USER_NOT_FOUND = "User not found.";

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

}
