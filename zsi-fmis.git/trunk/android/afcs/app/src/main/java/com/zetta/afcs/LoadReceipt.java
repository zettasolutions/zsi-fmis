package com.zetta.afcs;

public class LoadReceipt {
    private String hashKey;
    private String refTrans;

    public LoadReceipt() {}

    public void setHashKey(String hashKey) {this.hashKey = hashKey; }

    public String getHashKey() {
        return this.hashKey;
    }

    public void setRefTrans(String refTrans) {this.refTrans = refTrans; }

    public String getRefTrans() {
        return this.refTrans;
    }

}
