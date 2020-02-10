package com.zetta.afcs;

public class Configuration {
    private double baseFare;
    private double baseKm;
    private double succeedingKmFare;
    private double studentDiscountPercent;
    private double seniorDiscountPercent;
    private double pwdDiscountPercent;

    public Configuration() {}

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getBaseFare() {
        return this.baseFare;
    }

    public void setBaseKm(double baseKm) {
        this.baseKm = baseKm;
    }

    public double getBaseKm() {
        return this.baseKm;
    }

    public void setSucceedingKmFare(double succeedingKmFare) {
        this.succeedingKmFare = succeedingKmFare;
    }

    public double getSucceedingKmFare() {
        return this.succeedingKmFare;
    }

    public void setStudentDiscountPercent(double studentDiscountPercent) {
        this.studentDiscountPercent = studentDiscountPercent;
    }

    public double getStudentDiscountPercent() {
        return this.studentDiscountPercent;
    }

    public void setSeniorDiscountPercent(double seniorDiscountPercent) {
        this.seniorDiscountPercent = seniorDiscountPercent;
    }

    public double getSeniorDiscountPercent() {
        return this.seniorDiscountPercent;
    }

    public void setPwdDiscountPercent(double pwdDiscountPercent) {
        this.pwdDiscountPercent = pwdDiscountPercent;
    }

    public double getPwdDiscountPercent() {
        return this.pwdDiscountPercent;
    }
}