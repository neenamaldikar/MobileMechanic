package com.mm.mobilemechanic.user;

/**
 * Created by ndw6152 on 4/15/2017.
 */

public class User {

    private String mName;
    private String mPhonenumber;
    private String mEmail;
    private String mGender;
    private String mBio;

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
}
