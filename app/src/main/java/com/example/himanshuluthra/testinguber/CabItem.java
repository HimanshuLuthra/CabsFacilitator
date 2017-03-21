package com.example.himanshuluthra.testinguber;

/**
 * Created by himanshuluthra on 07/03/17.
 */

public class CabItem {
    private CabType mCabType;
    private String mProductId;
    private String mCategory;
    private String mEstimate;
    private long mLowEstimate;
    private long mHighEstimate;
    private long mPickupTime;
    private float mSurgeMultiplier;

    public void setCabType(CabType cabType) {
        this.mCabType = cabType;
    }

    public CabType getCabType() {
        return this.mCabType;
    }

    public void setProductId(String productId) {
        this.mProductId = productId;
    }

    public String getProductId() {
        return this.mProductId;
    }

    public void setCategory(String cabSubType) {
        this.mCategory = cabSubType;
    }

    public String getCategory() {
        return this.mCategory;
    }

    public void setEstimate(String estimate) {
        this.mEstimate = estimate;
    }

    public String getEstimate() {
        return this.mEstimate;
    }

    public void setLowEstimate(long lowEstimate) {
        this.mLowEstimate = lowEstimate;
    }

    public long getLowEstimate() {
        return mLowEstimate;
    }

    public void setHighEstimate(long highEstimate) {
        this.mHighEstimate = highEstimate;
    }

    public long getHighEstimate() {
        return mHighEstimate;
    }

    public void setSurgeMultiplier(float surgeMultiplier) {
        this.mSurgeMultiplier = surgeMultiplier;
    }

    public float getSurgeMultiplier() {
        return this.mSurgeMultiplier;
    }

    public void setPickupTime(long pickupTime) {
        this.mPickupTime = pickupTime;
    }

    public long getPickupTime() {
        return this.mPickupTime/60;
    }

}
