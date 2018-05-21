package com.mm.mobilemechanic.models;

/**
 * Created by ndw6152 on 4/15/2017.
 *
 */

public class User {

    private String mName;
    private String mPhonenumber;
    private String mEmail;
    private String mGender;
    private String mBio;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZipCode;

    public User(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhonenumber() {
        return mPhonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.mPhonenumber = phonenumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        this.mBio = bio;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipcode) {
        this.mZipCode = zipcode;
    }
}
