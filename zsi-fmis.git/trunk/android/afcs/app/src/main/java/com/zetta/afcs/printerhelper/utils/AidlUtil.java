package com.zetta.afcs.printerhelper.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

//import com.zetta.afcs.printerhelper.R;
import com.zetta.afcs.LoadReceipt;
import com.zetta.afcs.R;
import com.zetta.afcs.Receipt;
import com.zetta.afcs.printerhelper.bean.TableItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

//import com.zetta.afcs.service.ICallback;
//import com.zetta.afcs.service.IWoyouService;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";


    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback){
        return new ICallback.Stub(){


            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {
                printerCallback.onReturnString(result);
            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }
        };
    }

    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPrinterInfo(PrinterCallback printerCallback) {
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            woyouService.getPrintedLength(generateCB(printerCallback));
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(printerCallback.getResult());
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if(packageInfo != null){
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode+"");
                }else{
                    info.add("");info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void initPrinter() {
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }


        try {
		    woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (woyouService == null) {
            //Toast.makeText(context, R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printBitmap(Bitmap bitmap, int orientation) {
        if (woyouService == null) {
            Toast.makeText(context,"服务已断开！",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if(orientation == 0){
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
            }else{
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printTable(LinkedList<TableItem> list) {
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            for (TableItem tableItem : list) {
                Log.i("kaltin", "printTable: "+tableItem.getText()[0]+tableItem.getText()[1]+tableItem.getText()[2]);
                woyouService.printColumnsString(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void print3Line(){
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            //Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a blank line or form feed.
     * @param line The line to print.
     */
    public void printLine(int line){
        if (woyouService == null) {
            return;
        }

        try {
            woyouService.lineWrap(line, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats the text so that it will be aligned.
     * @param label The label of the text.
     * @param value The value of the text.
     * @return A formatted text.
     */
    private String formatText(String label, String value) {
        StringBuilder sb = new StringBuilder();
        int spaceCount = 32 - (label.trim().length() + value.trim().length());
        char[] chars = new char[spaceCount];
        Arrays.fill(chars, ' ');
        sb.append(label.trim());
        sb.append(chars);
        sb.append(value.trim());

        return sb.toString();
    }

    /**
     * Prints the receipt.
     * @param receipt The receipt object.
     */
    public void printReceipt(Receipt receipt) {
        if (woyouService == null) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.sendRAWData(ESCUtil.boldOn(), null);
            woyouService.printText(String.format("%s", receipt.getCompanyName()), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(String.format("TIN: %s", receipt.getTin()), null);
            woyouService.lineWrap(2, null);
            woyouService.setAlignment(0, null);
            woyouService.printText("--------------------------------", null);
            woyouService.lineWrap(1, null);
            woyouService.sendRAWData(ESCUtil.boldOff(), null);

            woyouService.printText(formatText("From:", String.format("%s", receipt.getFrom())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("To:", String.format("%s", receipt.getTo())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("Regular:", String.format("%s", receipt.getRegular())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("Student:", String.format("%s", receipt.getStudent())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("Senior:", String.format("%s", receipt.getSenior())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("PWD:", String.format("%s", receipt.getPwd())), null);
            woyouService.lineWrap(2, null);

            woyouService.sendRAWData(ESCUtil.boldOn(), null);
            woyouService.printText(formatText("Total Passengers:", String.format("%s", receipt.getTotalPassengers())), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(formatText("Total Amount:", String.format(Locale.ENGLISH, "%.2f", receipt.getTotalAmount())), null);
            woyouService.lineWrap(2, null);
            woyouService.sendRAWData(ESCUtil.boldOff(), null);

            woyouService.printText(formatText("Payment Type:", String.format("%s", receipt.getPaymentType())), null);
            woyouService.lineWrap(2, null);

            woyouService.printText("--------------------------------", null);
            woyouService.lineWrap(1, null);
            woyouService.setAlignment(1, null);
            woyouService.printText(receipt.getDateTime(), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(String.format("Series No. %s", receipt.getSeriesNumber()), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(receipt.getVehiclePlate(), null);
            woyouService.lineWrap(2, null);
            woyouService.printText("KEEP TICKET FOR INSPECTION", null);
            woyouService.lineWrap(1, null);
            woyouService.printText("Thank you and have a safe trip!", null);

            woyouService.lineWrap(5, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the load receipt.
     * @param loadReceipt The receipt object.
     */
    public void printLoadReceipt(LoadReceipt loadReceipt) {
        if (woyouService == null) {
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.sendRAWData(ESCUtil.boldOn(), null);
            woyouService.printQRCode(loadReceipt.getHashKey(), 10, 0, null);
            //woyouService.printText(String.format("%s", loadReceipt.getHashKey()), null);
            woyouService.lineWrap(1, null);
            woyouService.printText(String.format("Trans. No.: %s", loadReceipt.getRefTrans()), null);
            woyouService.lineWrap(2, null);
            woyouService.setAlignment(0, null);
            woyouService.sendRAWData(ESCUtil.boldOff(), null);

            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
