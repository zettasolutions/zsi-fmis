package com.zetta.afcs;

public class RouteInfo {
    public int routeNo;
    public String location;
    public double distanceKm;
    public int seqNo;

    public RouteInfo() {}

    public void setRouteNo(int routeNo) {this.routeNo = routeNo; }

    public int getRouteNo() {
        return this.routeNo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setDistanceKm(double distanceKm) {this.distanceKm = distanceKm; }

    public double getDistanceKm() {
        return this.distanceKm;
    }

    public void setSeqNo(int seqNo) {this.seqNo = seqNo; }

    public int getSeqNo() { return this.seqNo; }
}
