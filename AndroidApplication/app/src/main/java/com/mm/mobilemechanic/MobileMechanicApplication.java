package com.mm.mobilemechanic;

import android.app.Application;
import android.util.Log;

/**
 * Created by ndw6152 on 2/28/2018.
 */

public class MobileMechanicApplication extends Application {
    private String fbId;

    public String getFbUserId() {
        return this.fbId;
    }

    public void setFbUserId(String fbId) {
        this.fbId = fbId;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
