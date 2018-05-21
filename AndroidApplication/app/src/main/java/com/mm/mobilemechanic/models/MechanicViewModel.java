package com.mm.mobilemechanic.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

/**
 * Created by ndw6152 on 10/25/2017.
 */

public class MechanicViewModel extends ViewModel {
    private String userId;
    private LiveData<Mechanic> mechanic;
    private MechanicRepository mechRepo;

    public MechanicViewModel() {
        this.mechRepo = new MechanicRepository();
    }

    @Inject // UserRepository parameter is provided by Dagger 2
    public MechanicViewModel(UserRepository userRepo) {
        this.mechRepo = mechRepo;
    }

    public void init(String userId, String authToken) {
        this.userId = userId;
        mechanic = mechRepo.getMechanic(this.userId, authToken); // TODO init the user from REST call
    }

    public LiveData<Mechanic> getMechanic() {
        return mechanic;
    }

    public void setMechanic(String json, String authToken) {
        mechRepo.setMechanic(json, userId, authToken);
    }
}