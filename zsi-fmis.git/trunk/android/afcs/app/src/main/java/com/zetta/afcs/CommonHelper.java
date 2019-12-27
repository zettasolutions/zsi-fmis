package com.zetta.afcs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

public class CommonHelper extends DialogFragment {
    /*
    Shows a dialog.
     */
    public static Dialog showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });

        AlertDialog alert = builder.create();

        return alert;
    }
}

