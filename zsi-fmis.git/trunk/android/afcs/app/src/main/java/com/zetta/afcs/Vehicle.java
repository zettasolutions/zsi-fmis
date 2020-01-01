package com.zetta.afcs;

import java.util.List;

public class Vehicle {
    private String assetCode;
    private String assetNo;
    private String routeCode;
    private String routeDescription;
    private List<RouteInfo> routeInfoOne;
    private List<RouteInfo> routeInfoTwo;

    public Vehicle() {}

    public void setAssetCode(String assetCode) {this.assetCode = assetCode; }

    public String getAssetCode() {
        return this.assetCode;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getAssetNo() { return this.assetNo; }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteCode() {
        return this.routeCode;
    }

    public void setRouteDescription(String routeDescription) { this.routeDescription = routeDescription; }

    public String getRouteDescription() {
        return this.routeDescription;
    }

    public void setRouteInfoOne(List<RouteInfo> routeInfoOne) {this.routeInfoOne = routeInfoOne; }

    public List<RouteInfo> getRouteInfoOne() { return this.routeInfoOne; }

    public void setRouteInfoTwo(List<RouteInfo> routeInfoTwo) {this.routeInfoTwo = routeInfoTwo; }

    public List<RouteInfo> getRouteInfoTwo() {
        return this.routeInfoTwo;
    }
}