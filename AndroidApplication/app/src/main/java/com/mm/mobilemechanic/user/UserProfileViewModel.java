package com.mm.mobilemechanic.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by ndw6152 on 10/25/2017.
 *
 */

public class UserProfileViewModel extends ViewModel {
    private String userId;
    private LiveData<User> user;

    public void init(String userId) {
        this.userId = userId;

        // TODO init the user from REST call
    }
    public LiveData<User> getUser() {
        return user;
    }
}