package com.zetta.afcs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TicketingActivity extends AppCompatActivity {
    private User User;

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
    private TextView tPassengerCount;
    private TextView tFare;
    private TextView tRegularCounter;
    private TextView tStudentCounter;
    private TextView tSeniorCounter;
    private TextView tPWDCounter;

    private double minimumFare = 9.50;
    private double student_discount = 0.25;
    private double senior_discount = 0.50;
    private double pwd_discount = 0.75;

    private int counterRegular = 0;
    private int counterStudent = 0;
    private int counterSenior = 0;
    private int counterPWD = 0;
    private int totalPassengers = 0;

    private FileHelper fileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.fileHelper = new FileHelper(this);
        this.User = new User();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.User.setUsername(extras.getString("username"));
            this.User.setIsAuthenticated(extras.getBoolean("isAuthenticated"));
        } else {
            this.User.IsAuthenticated = false;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

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
        this.tPassengerCount = findViewById(R.id.lbl_passenger_count);
        this.tFare = findViewById(R.id.lbl_fare);
        this.tRegularCounter = findViewById(R.id.lbl_reg_counter);
        this.tStudentCounter = findViewById(R.id.lbl_student_counter);
        this.tSeniorCounter = findViewById(R.id.lbl_sen_counter);
        this.tPWDCounter = findViewById(R.id.lbl_pwd_counter);

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

    private int getTotalPassengers() {
        return this.counterRegular + this.counterStudent + this.counterSenior + this.counterPWD;
    }

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
}
