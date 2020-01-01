package com.zetta.afcs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import com.zetta.afcs.printerhelper.utils.AidlUtil;
import com.zetta.afcs.barcode.BarcodeCaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
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

    private double minimumFare = 9.50;
    private double student_discount = 0.25;
    private double senior_discount = 0.50;
    private double pwd_discount = 0.75;

    private int counterRegular = 0;
    private int counterStudent = 0;
    private int counterSenior = 0;
    private int counterPWD = 0;
    private int totalPassengers = 0;

    private String LOG_TAG = "";
    private int BARCODE_READER_REQUEST_CODE = 1;

    private FileHelper FileHelper;
    private Vehicle Vehicle;

    private List<Integer> RouteNo;
    private List<String> RouteLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.Vehicle = new Vehicle();
        this.FileHelper = new FileHelper(this);
        this.User = new User();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            this.User.setUsername(extras.getString("username"));
//            this.User.setIsAuthenticated(extras.getBoolean("isAuthenticated"));
            this.User.setUsername(extras.getString(Common.BundleExtras.Username));
            this.User.setIsAuthenticated(extras.getBoolean(Common.BundleExtras.IsAuthenticated));

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
                    if (position > 0) {
                        populateRoutes(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            this.bRegularPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counterRegular++;
                    tRegularCounter.setText(String.valueOf(counterRegular));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bRegularMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counterRegular > 0) counterRegular--;
                    tRegularCounter.setText(String.valueOf(counterRegular));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bStudentPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counterStudent++;
                    tStudentCounter.setText(String.valueOf(counterStudent));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bStudentMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counterStudent > 0) counterStudent--;
                    tStudentCounter.setText(String.valueOf(counterStudent));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bSeniorPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counterSenior++;
                    tSeniorCounter.setText(String.valueOf(counterSenior));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bSeniorMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counterSenior > 0) counterSenior--;
                    tSeniorCounter.setText(String.valueOf(counterSenior));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bPWDPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counterPWD++;
                    tPWDCounter.setText(String.valueOf(counterPWD));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bPWDMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (counterPWD > 0) counterPWD--;
                    tPWDCounter.setText(String.valueOf(counterPWD));
                    tPassengerCount.setText(String.valueOf(getTotalPassengers()));
                    double totalFare = getTotalFare();
                    tFare.setText(String.format(Locale.ENGLISH, "%.2f", totalFare));
                }
            });
            this.bPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (areValidEntries()) {
                        submitPayment();
                    }
                }
            });
            this.bPayQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (areValidEntries()) {
                        submitPaymentQR();
                    }
                }
            });

            // Initialize the printer.
            AidlUtil.getInstance().initPrinter();
        } else {
            this.User.IsAuthenticated = false;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem login_menu = menu.findItem(R.id.action_login);
        MenuItem logout_menu = menu.findItem(R.id.action_logout);
        MenuItem ticketing_menu = menu.findItem(R.id.action_ticketing);
        if (this.User.IsAuthenticated) {
            login_menu.setVisible(false);
            logout_menu.setVisible(true);
            ticketing_menu.setVisible(true);
        } else {
            login_menu.setVisible(true);
            logout_menu.setVisible(false);
            ticketing_menu.setVisible(false);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String result = barcode.displayValue;

                    // TODO
                    // 1. parse the result.
                    // 2. connect to the api and retrieve user info and user credits.
                    // 3. validate that the user has valid credits before payment is accepted.

                    // 4. if user has credits, accept payment and print receipt.
                    // 5. if user has not enough credits, prompt warning and disregard payment.
                }
            } else {
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            if (this.User.IsAuthenticated) {
                intent.putExtra("username", this.User.Username);
                intent.putExtra("isAuthenticated", true);
            } else {
                intent.putExtra("username", "");
                intent.putExtra("isAuthenticated", false);
            }
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_ticketing) {
            if (!this.User.IsAuthenticated) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("username", "");
                intent.putExtra("isAuthenticated", false);
                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public double getFare(Common.Discount discount) {
        double fare = 0;
        switch (discount) {
            case REGULAR:
                fare = this.minimumFare;
                break;
            case STUDENT:
                fare = this.minimumFare - (this.minimumFare * this.student_discount);
                break;
            case SENIOR:
                fare = this.minimumFare - (this.minimumFare * this.senior_discount);
                break;
            case PWD:
                fare = this.minimumFare - (this.minimumFare * this.pwd_discount);
                break;
        }

        return fare;
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
        double regularFare = this.counterRegular * this.minimumFare;
        double studentFare = this.counterStudent * (this.minimumFare - (this.minimumFare * this.student_discount));
        double seniorFare = this.counterSenior * (this.minimumFare - (this.minimumFare * this.senior_discount));
        double pwdFare = this.counterPWD * (this.minimumFare - (this.minimumFare * this.pwd_discount));

        return regularFare + studentFare + seniorFare + pwdFare;
    }

    @Override
    public void onBackPressed() {
        if (this.User.IsAuthenticated) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", this.User.Username);
            intent.putExtra("isAuthenticated", true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("username", "");
            intent.putExtra("isAuthenticated", false);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Clears the items in the spinner.
     */
    private void clearRoutesSpinner() {
        this.sFrom.setAdapter(null);
        this.sTo.setAdapter(null);
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
     * Determines whether the selected route is valid or not.
     * @return True or false.
     */
    private boolean isValidRoute() {
        Integer route_from = this.sFrom.getSelectedItemPosition();
        Integer route_to = this.sTo.getSelectedItemPosition();

        return route_to > route_from;
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
        Receipt receipt = new Receipt();

        receipt.setFrom(this.sFrom.getSelectedItem().toString());
        receipt.setTo(this.sTo.getSelectedItem().toString());
        receipt.setRegular(this.counterRegular);
        receipt.setStudent(this.counterStudent);
        receipt.setSenior(this.counterSenior);
        receipt.setPwd(this.counterPWD);
        receipt.setTotalAmount(this.getTotalFare());
        receipt.setPaymentType("Cash");
        receipt.setVehiclePlate(this.tPlate.getText().toString());

        // TODO: Test. Generate series number.
        receipt.setSeriesNumber("000999");

        AidlUtil.getInstance().printReceipt(receipt);
    }

    /**
     * Submits the payment through QR code.
     */
    private void submitPaymentQR() {

    }
}