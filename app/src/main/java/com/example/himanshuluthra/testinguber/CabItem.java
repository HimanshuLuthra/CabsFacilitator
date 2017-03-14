package com.example.himanshuluthra.testinguber;

/**
 * Created by himanshuluthra on 07/03/17.
 */

public class CabItem {
    private CabType mCabType;
    private String mCabSubType;
    private long mPrice;
    private long mPickupTime;

    public void setCabType(CabType cabType) {
        this.mCabType = cabType;
    }

    public CabType getCabType() {
        return this.mCabType;
    }

    public void setCabSubType(String cabSubType) {
        this.mCabSubType = cabSubType;
    }

    public String getCabSubType() {
        return this.mCabSubType;
    }

    public void setPrice() {
        this.mPrice = mPrice;
    }

    public long getPrice() {
        return this.mPrice;
    }

    public void setPickupTime(long pickupTime) {
        this.mPickupTime = pickupTime;
    }

    public long getPickupTime() {
        return this.mPickupTime;
    }

}
