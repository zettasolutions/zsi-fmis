package com.zetta.afcs.api;

public class SettingsModel {
    public SettingsModel() {}

    private String assetCode;
    public String getAssetCode() {
        return assetCode;
    }
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    private String assetNo;
    public String getAssetNo() {
        return assetNo;
    }
    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    private String routeCode;
    public String getRouteCode() {
        return routeCode;
    }
    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    private String routeDescription;
    public String getRouteDescription() {
        return routeDescription;
    }
    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    private Integer routeNo;
    public Integer getRouteNo() {
        return routeNo;
    }
    public void setRouteNo(Integer routeNo) {
        this.routeNo = routeNo;
    }

    private String location;
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    private Float distanceKm;
    public Float getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(Float distanceKm) {
        this.distanceKm = distanceKm;
    }

    private Integer seqNo;
    public Integer getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
