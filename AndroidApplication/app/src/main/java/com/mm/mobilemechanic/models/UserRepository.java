package com.mm.mobilemechanic.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

    private String mGender;
    private User mUser;     // TODO need to optimize the number of calls

    public LiveData<User> getUser(String userId, String authToken) { // 10152521620162653
        // This is not an optimal implementation, we'll fix it below
        Log.i(TAG, userId + "  " + authToken);
        final MutableLiveData<User> data = new MutableLiveData<>();


        // Facebook request to get user info
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "LoginActivity Response " + response.toString());

                        try {
                            if(mUser != null && object != null) {
                                mGender = object.getString("gender");
                                mUser.setGender(mGender);
                                data.postValue(mUser);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "gender");
        request.setParameters(parameters);
        request.executeAsync();

        RestClient.getUserInfo(userId, authToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                    // TODO on failure what happens
                } else {
                    Log.i(TAG, response.message());
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        String name = jObject.getString("first_name") + " " + jObject.getString("last_name");
                        if(mUser == null) {
                            mUser = new User(name);
                        }

                        mUser.setName(name);
                        mUser.setAddress(jObject.getString("address_line"));
                        mUser.setCity(jObject.getString("city"));
                        mUser.setState(jObject.getString("state"));
                        mUser.setZipCode(jObject.getString("zipcode"));
                        mUser.setEmail(jObject.getString("email"));
                        mUser.setPhonenumber(jObject.getString("phone_number"));
                        mUser.setGender(mGender);
                        data.postValue(mUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // TODO when setting user fails
                    }
                }
            }
        });
        return data;
    }

    public void setUser(String json, String userId, String authToken) {
        RestClient.updateUser(userId, json, authToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                } else {
                    Log.i(TAG, response.message());
                }
            }
        });
    }
}
