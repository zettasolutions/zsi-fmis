package com.zetta.afcs;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.ArrayMap;

import java.io.File;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class FileHelper extends AppCompatActivity {
    private Activity Activity;

    // File references.
    public String Filepath = "afcs";
    public String DatabaseFile = "users.db";

    //private static String UserFile = "user.zta";

    public FileHelper() { }

    public FileHelper(Activity activity) {
        this.Activity = activity;
    }

    /**
     * Determines whether the external storage is available.
     * @return true or false.
     */
    public boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    /**
     * Determines whether the external storage is read only.
     * @return true or false.
     */
    public boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    /**
     * Determines whether the device has a file.
     * @return true or false.
     * @throws Exception, The exception.
     */
    public boolean hasFile(File file) {
        return file.exists();
    }

    /**
     * Finds a key in an array map.
     * @param arrayMap, The array map object.
     * @param keyToFind, The key to find in the array map.
     * @return true or false.
     */
    public boolean findKeyInArrayMap(ArrayMap arrayMap, String keyToFind) {
        for (int i = 0; i < arrayMap.size(); i++) {
            String key = arrayMap.keyAt(i).toString();
            if (Objects.equals(key.trim().toUpperCase(), keyToFind.trim().toUpperCase()))
                return true;
        }

        return false;
    }

    public void createFolder(String folderName) {
        //File file = new File(Environment.getExternalStorageDirectory(), folderName);
        //File userFile = new File(getExternalFilesDir(this.FileHelper.Filepath), this.FileHelper.UserFile);

        //File file = new File(this.getExternalFilesDir(this.Filepath), folderName);
//        File folder = new File(this.getExternalFilesDir(this.Filepath), folderName);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
    }


}
