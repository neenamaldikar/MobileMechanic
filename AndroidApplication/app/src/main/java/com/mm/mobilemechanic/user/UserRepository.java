package com.mm.mobilemechanic.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.util.Log;

import com.mm.mobilemechanic.authorization.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 10/31/2017.
 *
 */
@Singleton  // informs Dagger that this class should be constructed once
public class UserRepository {
    private String TAG = "UserRepository";

    public LiveData<User> getUser(String userId, String authToken) { // 10152521620162653
        // This is not an optimal implementation, we'll fix it below
        Log.i(TAG, userId + "  " + authToken);
        final MutableLiveData<User> data = new MutableLiveData<>();

        RestClient.getUserInfo("", userId, authToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                } else {
                    Log.i(TAG, response.message());
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        String name = jObject.getString("first_name") + " " + jObject.getString("last_name");
                        User x = new User(name);
                        x.setEmail(jObject.getString("email"));
                        data.postValue(x);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        User x = new User("TEST");
        data.setValue(x);
        return data;
    }
}
