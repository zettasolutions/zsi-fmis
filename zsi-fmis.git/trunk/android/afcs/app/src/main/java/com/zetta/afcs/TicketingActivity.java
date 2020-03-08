package com.zetta.afcs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.zetta.afcs.api.ApiHelper;
import com.zetta.afcs.printerhelper.utils.AidlUtil;
import com.zetta.afcs.barcode.BarcodeCaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TicketingActivity extends AppCompatActivity {
    private User User;

    private Spinner sRouteNo;
    private Spinner sFrom;
    private Spinner sTo;
    private Button bRegularPlus;
    private Button bRegularMinus;
    private Button bStudentPlus;
    private Button bStudentMinus;
    private Button bSeniorPlus;
    private Button bSeniorMinus;
    private Button bPWDPlus;
    private Button bPWDMinus;
    private Button bPay;
    private Button bPayQR;
    private TextView tPassengerCount;
    private TextView tFare;
    private TextView tRegularCounter;
    private TextView tStudentCounter;
    private TextView tSeniorCounter;
    private TextView tPWDCounter;
    private TextView tPlate;

    private String paymentType = "Cash";
    private String g_qrCode = "";

    private int counterRegular = 0;
    private int counterStudent = 0;
    private int counterSenior = 0;
    private int counterPWD = 0;
    private String companyName = "LAMADO TRANSPORTATION"; // TODO: Get from api.
    private String tin = "123-456-789"; // TODO: Get from api.

    private int processCode = 0;
    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;
    private int QR_PAYMENT_CODE = 2;
    private int SAVE_PAYMENT_CODE = 3;

    private FileHelper FileHelper;
    private Vehicle Vehicle;
    private List<Integer> RouteNo;
    private List<String> RouteLocation;
    private ProgressDialog progressDialog;
    private DeviceHelper deviceHelper;
    private ConnectionHelper connectionHelper;
    private Receipt receipt = new Receipt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.Vehicle = new Vehicle();
        this.FileHelper = new FileHelper(this);
        this.User = new User();
        this.deviceHelper = new DeviceHelper();
        this.connectionHelper = new ConnectionHelper();

        //Bundle extras = getIntent().getExtras();

        this.sRouteNo = findViewById(R.id.spinner_route_no);
        this.sFrom = findViewById(R.id.spinner_from);
        this.sTo = findViewById(R.id.spinner_to);
        this.bRegularPlus = findViewById(R.id.btn_reg_plus);
        this.bRegularMinus = findViewById(R.id.btn_reg_minus);
        this.bStudentPlus = findViewById(R.id.btn_student_plus);
        this.bStudentMinus = findViewById(R.id.btn_student_minus);
        this.bSeniorPlus = findViewById(R.id.btn_sen_plus);
        this.bSeniorMinus = findViewById(R.id.btn_sen_minus);
        this.bPWDPlus = findViewById(R.id.btn_pwd_plus);
        this.bPWDMinus = findViewById(R.id.btn_pwd_minus);
        this.bPay = findViewById(R.id.btn_pay);
        this.bPayQR = findViewById(R.id.btn_pay_qr);
        this.tPassengerCount = findViewById(R.id.lbl_passenger_count);
        this.tFare = findViewById(R.id.lbl_fare);
        this.tRegularCounter = findViewById(R.id.lbl_reg_counter);
        this.tStudentCounter = findViewById(R.id.lbl_student_counter);
        this.tSeniorCounter = findViewById(R.id.lbl_sen_counter);
        this.tPWDCounter = findViewById(R.id.lbl_pwd_counter);
        this.tPlate = findViewById(R.id.lbl_plate);

        this.RouteNo = new ArrayList<>();
        this.RouteLocation = new ArrayList<>();

        // Get the details of the vehicle and route info.
        this.Vehicle = this.FileHelper.getVehicleInfo(this);
        if (this.Vehicle != null) {
            this.tPlate.setText(String.format("VEHICLE: %s", this.Vehicle.getAssetNo().toUpperCase()));
        }

        this.sRouteNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearRoutesSpinner();
                resetDetails();
                if (position > 0) {
                    populateRoutes(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        this.sFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                resetDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        this.sTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                resetDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        this.bRegularPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    counterRegular++;
                    tRegularCounter.setText(String.valueOf(counterRegular));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bRegularMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    if (counterRegular > 0) counterRegular--;
                    tRegularCounter.setText(String.valueOf(counterRegular));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bStudentPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    counterStudent++;
                    tStudentCounter.setText(String.valueOf(counterStudent));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bStudentMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    if (counterStudent > 0) counterStudent--;
                    tStudentCounter.setText(String.valueOf(counterStudent));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bSeniorPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    counterSenior++;
                    tSeniorCounter.setText(String.valueOf(counterSenior));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bSeniorMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    if (counterSenior > 0) counterSenior--;
                    tSeniorCounter.setText(String.valueOf(counterSenior));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bPWDPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    counterPWD++;
                    tPWDCounter.setText(String.valueOf(counterPWD));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bPWDMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidRouteNo()) {
                    if (counterPWD > 0) counterPWD--;
                    tPWDCounter.setText(String.valueOf(counterPWD));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            }
        });
        this.bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areValidEntries()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TicketingActivity.this);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            submitPayment();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Stuff to do
                        }
                    });

                    builder.setMessage("All entries correct?");
                    builder.setTitle("Confirm");

                    AlertDialog d = builder.create();
                    d.show();
                }
            }
        });
        this.bPayQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areValidEntries()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TicketingActivity.this);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            submitPaymentQR();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Stuff to do
                        }
                    });

                    builder.setMessage("All entries correct?");
                    builder.setTitle("Confirm");

                    AlertDialog d = builder.create();
                    d.show();
                }
            }
        });

        // Initialize the printer.
        AidlUtil.getInstance().initPrinter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the total passengers entered.
     * @return The total passengers entered.
     */
    private int getTotalPassengers() {
        return this.counterRegular + this.counterStudent + this.counterSenior + this.counterPWD;
    }

    /**
     * Gets the total fare.
     * @return The total amount for the fare.
     */
    private double getTotalFare() {
        FareHelper fareHelper = new FareHelper(
            this.Vehicle
            , this.sRouteNo.getSelectedItem().toString()
            , this.sFrom.getSelectedItem().toString()
            , this.sTo.getSelectedItem().toString()
            , this.counterRegular
            , this.counterStudent
            , this.counterSenior
            , this.counterPWD);

        return fareHelper.getTotalFare();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Clears the items in the spinner.
     */
    private void clearRoutesSpinner() {
        this.sFrom.setAdapter(null);
        this.sTo.setAdapter(null);
    }

    /**
     * Resets the counters and fare.
     */
    private void resetDetails() {
        this.counterRegular = 0;
        this.counterStudent = 0;
        this.counterSenior = 0;
        this.counterPWD = 0;

        this.tRegularCounter.setText(String.valueOf(this.counterRegular));
        this.tStudentCounter.setText(String.valueOf(this.counterStudent));
        this.tSeniorCounter.setText(String.valueOf(this.counterSenior));
        this.tPWDCounter.setText(String.valueOf(this.counterPWD));
        this.tPassengerCount.setText("0");
        double totalFare = 0;
        tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
    }

    /**
     * Populates the items in the spinner based on the selected route number.
     * @param routeNumber The route number.
     */
    private void populateRoutes(int routeNumber) {
        this.RouteNo = new ArrayList<>();
        this.RouteLocation = new ArrayList<>();

        if (routeNumber == 1) {
            for (RouteInfo e : this.Vehicle.getRouteInfoOne()) {
                this.RouteNo.add(e.getSeqNo());
                this.RouteLocation.add(e.getLocation());
            }
        }

        if (routeNumber == 2) {
            for (RouteInfo e : this.Vehicle.getRouteInfoTwo()) {
                this.RouteNo.add(e.getSeqNo());
                this.RouteLocation.add(e.getLocation());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.RouteLocation);
        this.sFrom.setAdapter(adapter);
        this.sTo.setAdapter(adapter);
    }

    /**
     * Determines whether the selected route number is valid or not.
     * @return True or false.
     */
    private boolean isValidRouteNo() {
        return this.sRouteNo.getSelectedItemPosition() > 0;
    }

    /**
     * Determines whether the selected route is valid or not.
     * @return True or false.
     */
    private boolean isValidRoute() {
        Integer route_from = this.sFrom.getSelectedItemPosition();
        Integer route_to = this.sTo.getSelectedItemPosition();

        return route_to >= route_from;
    }

    /**
     * Determines whether the passenger count is valid or not.
     * @return True or false.
     */
    private boolean isValidPassengerCount() {
        return this.getTotalPassengers() > 0;
    }

    /**
     * Determines whether the entries are valid or not.
     * @return True or false.
     */
    private boolean areValidEntries() {
        try {
            if (!this.isValidRoute()) {
                Dialog dialog = CommonHelper.showDialog(TicketingActivity.this, Message.Title.ERROR, Message.INVALID_ROUTE);
                dialog.show();

                return false;
            }

            if (!this.isValidPassengerCount()) {
                Dialog dialog = CommonHelper.showDialog(TicketingActivity.this, Message.Title.ERROR, Message.INVALID_PASSENGER_COUNT);
                dialog.show();

                return false;
            }
        } catch (Exception ex) {
            Dialog dialog = CommonHelper.showDialog(TicketingActivity.this, Message.Title.ERROR, Message.ERROR_OCCURRED);
            dialog.show();

            return false;
        }

        return true;
    }

    /**
     * Submits the payment through cash.
     */
    private void submitPayment() {
        this.paymentType = "Cash";
        this.g_qrCode = "";
        if (this.connectionHelper.IsInternetConnected()) {
            this.printReceipt();
        } else {
            Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                    , Message.Title.ERROR
                    , Message.CONNECTION_ERROR);
            dialog.show();
        }
    }

    /**
     * Submits the payment through QR code.
     */
    private void submitPaymentQR() {
        this.processCode = BARCODE_READER_REQUEST_CODE;
        this.paymentType = "QR Code";
        if (this.connectionHelper.IsInternetConnected()) {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        } else {
            Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                    , Message.Title.ERROR
                    , Message.CONNECTION_ERROR);
            dialog.show();
        }
    }

    private void printReceipt() {
        // Instantiate the global receipt variable.
        this.receipt = new Receipt();

        this.receipt.setFrom(this.sFrom.getSelectedItem().toString());
        this.receipt.setTo(this.sTo.getSelectedItem().toString());
        this.receipt.setRegular(this.counterRegular);
        this.receipt.setStudent(this.counterStudent);
        this.receipt.setSenior(this.counterSenior);
        this.receipt.setPwd(this.counterPWD);
        this.receipt.setTotalAmount(this.getTotalFare());
        this.receipt.setPaymentType(this.paymentType);
        this.receipt.setVehiclePlate(this.tPlate.getText().toString());
        this.receipt.setCompanyName(this.companyName);
        this.receipt.setTin(this.tin);

        // TODO: Test. Generate series number.
        this.receipt.setSeriesNumber("000999");

        // Saves the payments to the database first and return the payment_key
        // to be included om the receipt as qr code.
        this.savePayments(this.receipt);
    }

    /**
     * Saves the payments to the database.
     * @param receipt The receipt object.
     */
    private void savePayments(Receipt receipt) {
        if (receipt != null) {
            this.processCode = SAVE_PAYMENT_CODE;
            String serial_no = this.deviceHelper.getSerial();
            //String driver = "";
            //String pao = "";
            String driverId = "";
            String paoId = "";
            List<String> driverPao = this.FileHelper.getDriverPao(this);
            if (driverPao != null && !driverPao.isEmpty()) {
                //driver = driverPao.get(0);
                //pao = driverPao.get(1);
                driverId = driverPao.get(2);
                paoId = driverPao.get(3);
            }

            FareHelper fareHelper = new FareHelper(
                    this.Vehicle
                    , this.sRouteNo.getSelectedItem().toString()
                    , this.sFrom.getSelectedItem().toString()
                    , this.sTo.getSelectedItem().toString()
                    , this.counterRegular
                    , this.counterStudent
                    , this.counterSenior
                    , this.counterPWD);

            new TicketingActivity.JsonTask().execute(ApiHelper.apiURL
                    , ApiHelper.SqlCodeSavePayment
                    , serial_no
                    , this.Vehicle.getAssetNo()
                    , driverId
                    , paoId
                    , String.format(Locale.ENGLISH, "%s", this.sRouteNo.getSelectedItemId())
                    , this.sFrom.getSelectedItem().toString()
                    , this.sTo.getSelectedItem().toString()
                    , String.format(Locale.ENGLISH, "%.2f", fareHelper.getTravelDistance())
                    , String.format(Locale.ENGLISH, "%s", this.counterRegular)
                    , String.format(Locale.ENGLISH, "%s", this.counterStudent)
                    , String.format(Locale.ENGLISH, "%s", this.counterSenior)
                    , String.format(Locale.ENGLISH, "%s", this.counterPWD)
                    , String.format(Locale.ENGLISH, "%.2f", fareHelper.getTotalRegularFare())
                    , String.format(Locale.ENGLISH, "%.2f", fareHelper.getTotalStudentFare())
                    , String.format(Locale.ENGLISH, "%.2f", fareHelper.getTotalSeniorFare())
                    , String.format(Locale.ENGLISH, "%.2f", fareHelper.getTotalPwdFare())
                    , this.g_qrCode
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String qrResult = barcode.displayValue;

                    this.g_qrCode = qrResult;
                    this.processCode = QR_PAYMENT_CODE;
                    String serial_no = this.deviceHelper.getSerial();
                    String total_fare = String.format(Locale.ENGLISH, "%.2f", this.getTotalFare());
                    new TicketingActivity.JsonTask().execute(ApiHelper.apiURL
                            , ApiHelper.SqlCodeQRPayment
                            , serial_no
                            , qrResult
                            , total_fare);
                }
            } else {
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // --------------------- API calls -------------------------------------------------
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TicketingActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                String jsonInputString = "";
                if (processCode == QR_PAYMENT_CODE) {
                    // Post data.
                    jsonInputString = String.format("{ \"%s\": \"%s\", \"parameters\": {\"serial_no\": \"%s\", \"hash_key\": \"%s\", \"payment_amount\": \"%s\"} }"
                            , ApiHelper.SqlCodeKey, params[1], params[2], params[3], params[4]);
                }
                if (processCode == SAVE_PAYMENT_CODE) {
                    // Post data.
                    jsonInputString = String.format("{ \"%s\": \"%s\", \"parameters\": {" +
                                    "\"serial_no\": \"%s\", " +
                                    "\"vehicle_plate\": \"%s\", " +
                                    "\"driver_id\": \"%s\", " +
                                    "\"pao_id\": \"%s\", " +
                                    "\"route_id\": \"%s\", " +
                                    "\"from_location\": \"%s\", " +
                                    "\"to_location\": \"%s\", " +
                                    "\"travel_distance\": \"%s\", " +
                                    "\"count_regular\": \"%s\", " +
                                    "\"count_student\": \"%s\", " +
                                    "\"count_senior\": \"%s\", " +
                                    "\"count_pwd\": \"%s\", " +
                                    "\"total_regular_fare\": \"%s\", " +
                                    "\"total_student_fare\": \"%s\", " +
                                    "\"total_senior_fare\": \"%s\", " +
                                    "\"total_pwd_fare\": \"%s\", " +
                                    "\"qr_code\": \"%s\"" +
                                    "} }"
                            , ApiHelper.SqlCodeKey, params[1], params[2], params[3], params[4], params[5], params[6], params[7]
                            , params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18]);
                }

                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    //System.out.println(response.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                if (result.trim().equals("")) {
                    Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                            , Message.Title.ERROR
                            , Message.CONNECTION_ERROR);
                    dialog.show();
                } else {
                    JSONObject jObj = new JSONObject(result);
                    String isSuccess = jObj.get("isSuccess").toString();
                    if (isSuccess.equals("true")) {
                        JSONArray rows = jObj.getJSONArray("rows");
                        if (rows.length() > 0) {
                            if (processCode == QR_PAYMENT_CODE) {
                                String isValid = rows.getJSONObject(0).get("is_valid").toString();
                                String msg = rows.getJSONObject(0).get("msg").toString();
                                String current_balance_amount = rows.getJSONObject(0).get("current_balance_amount").toString();

                                if (isValid.toUpperCase().equals("Y")) {
                                    // Print the receipt.
                                    printReceipt();
                                } else {
                                    Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                                            , Message.Title.ERROR
                                            , String.format(Locale.ENGLISH, "%s Current balance: %.2f", msg, Double.parseDouble(current_balance_amount)));
                                    dialog.show();
                                }
                            } else if (processCode == SAVE_PAYMENT_CODE) {
                                String isValid = rows.getJSONObject(0).get("is_valid").toString();
                                String msg = rows.getJSONObject(0).get("msg").toString();
                                String payment_key = rows.getJSONObject(0).get("payment_key").toString();

                                if (isValid.toUpperCase().equals("Y")) {
                                    // Print the receipt.
                                    receipt.setQrCode(payment_key);
                                    AidlUtil.getInstance().printReceipt(receipt);
                                } else {
                                    Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                                            , Message.Title.ERROR
                                            , msg);
                                    dialog.show();
                                }
                            }
                        } else {
                            Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                                    , Message.Title.ERROR
                                    , Message.INVALID_QR);
                            dialog.show();
                        }
                    } else {
                        Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                                , Message.Title.ERROR
                                , Message.ERROR_OCCURRED_API);
                        dialog.show();
                    }
                }
            } catch (JSONException e) {
                Dialog dialog = CommonHelper.showDialog(TicketingActivity.this
                        , Message.Title.ERROR
                        , Message.ERROR_OCCURRED_API);
                dialog.show();
            }
        }
    }
    // --------------------- API calls -------------------------------------------------
}