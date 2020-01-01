package com.zetta.afcs;

import android.app.Activity;
import android.content.Context;
import android.media.audiofx.Equalizer;
import android.os.Environment;
import android.util.ArrayMap;

import com.zetta.afcs.api.SettingsModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class FileHelper extends AppCompatActivity {
    private Activity Activity;

    // File references.
    public String Filepath = "resources";
    public String RouteInfoFile = "route_info.txt";


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

    /**
     * Updates the text file for the route info.
     * @param context The context.
     * @param settingsList The list of settings retrieved from the api.
     * @return Count of items in list to determine the success of the update.
     */
    public Vehicle updateSettingsFile(Context context, List<SettingsModel> settingsList) {
        Vehicle vehicle = null;
        String FILE_NAME = "route_info.txt";
        File file = new File(context.getFilesDir(), "resources");

        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File route_file = new File(file, FILE_NAME);

            if (settingsList != null && settingsList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(route_file, false));

                for (int i = 0; i < settingsList.size(); i++) {
                    if (i == 0) {
                        vehicle = new Vehicle();
                        vehicle.setAssetCode(settingsList.get(i).getAssetCode());
                        vehicle.setAssetNo(settingsList.get(i).getAssetNo());
                        vehicle.setRouteCode(settingsList.get(i).getRouteCode());
                        vehicle.setRouteDescription(settingsList.get(i).getRouteDescription());
                    }

                    sb.append(String.format("%s, %s, %s, %s, %s, %s, %s, %s\r",
                            settingsList.get(i).getAssetCode()
                            , settingsList.get(i).getAssetNo()
                            , settingsList.get(i).getRouteCode()
                            , settingsList.get(i).getRouteDescription()
                            , settingsList.get(i).getRouteNo()
                            , settingsList.get(i).getLocation()
                            , settingsList.get(i).getDistanceKm()
                            , settingsList.get(i).getSeqNo())
                    );
                }

                bufferedWriter.write(sb.toString());
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicle;
    }

    /**
     * Gets the information of the vehicle or asset.
     * @param context The context.
     * @return A vehicle or asset object.
     */
    public Vehicle getVehicleInfo(Context context) {
        Vehicle vehicle = null;
        List<RouteInfo> routeInfoOne = null;
        List<RouteInfo> routeInfoTwo = null;
        String file = context.getFilesDir() + "/" + this.Filepath + "/" + this.RouteInfoFile;

        try {
            vehicle = new Vehicle();
            String assetCode = "";
            String assetNo = "";
            String routeCode = "";
            String routeDescription = "";
            boolean hasVehicleInfo = false;

            FileInputStream fis = new FileInputStream(new File(file));
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;
                routeInfoOne = new ArrayList<>();
                routeInfoTwo = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    RouteInfo route1 = new RouteInfo();
                    RouteInfo route2 = new RouteInfo();
                    String[] data = line.split(",");

                    if (!hasVehicleInfo) {
                        assetCode = data[0].trim();
                        assetNo = data[1].trim();
                        routeCode = data[2].trim();
                        routeDescription = data[3].trim();

                        hasVehicleInfo = true;
                    }

                    int route_no = Integer.parseInt(data[4].trim());
                    if (route_no == 1) {
                        route1.setRouteNo(route_no);
                        route1.setLocation(data[5].trim());
                        route1.setDistanceKm(Double.parseDouble(data[6].trim()));
                        route1.setSeqNo(Integer.parseInt(data[7].trim()));

                        routeInfoOne.add(route1);
                    }
                    if (route_no == 2) {
                        route2.setRouteNo(route_no);
                        route2.setLocation(data[5].trim());
                        route2.setDistanceKm(Double.parseDouble(data[6].trim()));
                        route2.setSeqNo(Integer.parseInt(data[7].trim()));

                        routeInfoTwo.add(route2);
                    }
                }

                vehicle.setAssetCode(assetCode);
                vehicle.setAssetNo(assetNo);
                vehicle.setRouteCode(routeCode);
                vehicle.setRouteDescription(routeDescription);
                vehicle.setRouteInfoOne(routeInfoOne);
                vehicle.setRouteInfoTwo(routeInfoTwo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicle;
    }
}