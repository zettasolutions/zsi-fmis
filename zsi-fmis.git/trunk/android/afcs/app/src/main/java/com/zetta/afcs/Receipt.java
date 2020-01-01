package com.zetta.afcs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Receipt {
    private String from;
    private String to;
    private int regular;
    private int student;
    private int senior;
    private int pwd;
    private String paymentType;
    private double totalAmount;
    private String seriesNumber;
    private String vehiclePlate;

    public Receipt() {}

    public void setFrom(String from) {this.from = from; }

    public String getFrom() {
        return this.from;
    }

    public void setTo(String to) {this.to = to; }

    public String getTo() {
        return this.to;
    }

    public void setRegular(int regular) {this.regular = regular; }

    public int getRegular() {
        return this.regular;
    }

    public void setStudent(int student) {this.student = student; }

    public int getStudent() {
        return this.student;
    }

    public void setSenior(int senior) {this.senior = senior; }

    public int getSenior() {
        return this.senior;
    }

    public void setPwd(int pwd) {this.pwd = pwd; }

    public int getPwd() {
        return this.pwd;
    }

    public void setPaymentType(String paymentType) {this.paymentType = paymentType; }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setTotalAmount(double totalAmount) {this.totalAmount = totalAmount; }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setVehiclePlate(String vehiclePlate) {this.vehiclePlate = vehiclePlate; }

    public String getSeriesNumber() {
        return this.seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {this.seriesNumber = seriesNumber; }

    public String getVehiclePlate() {
        return this.vehiclePlate;
    }

    public int getTotalPassengers() {
        return this.regular + this.student + this.senior + this.pwd;
    }

    public String getDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();

        return formatter.format(date);
    }


}
