package com.mm.mobilemechanic.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

/**
 * Created by ndw6152 on 10/25/2017.
 *
 */

public class UserProfileViewModel extends ViewModel {
    private String userId;
    private LiveData<User> user;
    private UserRepository userRepo;

    public UserProfileViewModel() {
        this.userRepo = new UserRepository();
    }

    @Inject // UserRepository parameter is provided by Dagger 2
    public UserProfileViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void init(String userId, String authToken) {
        this.userId = userId;
        user = userRepo.getUser(this.userId, authToken); // TODO init the user from REST call
    }
    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(String json, String authToken) {
        userRepo.setUser(json, userId, authToken);
    }
}