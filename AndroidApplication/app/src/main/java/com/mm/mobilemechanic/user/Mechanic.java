package com.mm.mobilemechanic.user;

/**
 * Created by ndw6152 on 4/15/2017.
 */

public class Mechanic extends User {
    private String mResidentialAddress;
    private String mMechAddress;
    private String mExpertise;
    private String mSummary;

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

    public String getmExpertise() {
        return mExpertise;
    }

    public void setmExpertise(String mExpertise) {
        this.mExpertise = mExpertise;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
}
