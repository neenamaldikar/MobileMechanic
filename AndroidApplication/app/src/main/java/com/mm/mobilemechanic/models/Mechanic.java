package com.mm.mobilemechanic.models;

/**
 * Created by ndw6152 on 4/15/2017.
 */

public class Mechanic extends User {
    private String mResidentialAddress;
    private String mMechAddress;
    private String mRate;

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    private String mRating;

    public Mechanic(String name) {
        super(name);
    }


    public String getmResidentialAddress() {
        return mResidentialAddress;
    }

    public void setmResidentialAddress(String mResidentialAddress) {
        this.mResidentialAddress = mResidentialAddress;
    }

    public String getmMechAddress() {
        return mMechAddress;
    }

    public void setmMechAddress(String mMechAddress) {
        this.mMechAddress = mMechAddress;
    }

    public String getmRate() {
        return mRate;
    }

    public void setmRate(String mRate) {
        this.mRate = mRate;
    }

}
