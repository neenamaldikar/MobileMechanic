package com.mm.mobilemechanic.models.mechanic;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
public class MechanicRepository {
    private String TAG = "MechRepository";


    private Mechanic mMechanic;     // TODO need to optimize the number of calls

    public LiveData<Mechanic> getMechanic(String userId, String authToken) { // 10152521620162653
        // This is not an optimal implementation, we'll fix it below
        Log.i(TAG, userId + "  " + authToken);
        final MutableLiveData<Mechanic> data = new MutableLiveData<>();

        RestClient.getMechanic(userId, authToken, new Callback() {
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
                        Log.i(TAG,jObject.toString());
                        if(mMechanic == null) {
                            mMechanic = new Mechanic("");
                        }


                        mMechanic.setAddress(jObject.getString("address_line"));
                        mMechanic.setCity(jObject.getString("city"));
                        mMechanic.setState(jObject.getString("state"));
                        mMechanic.setZipCode(jObject.getString("zipcode"));
                        mMechanic.setmRate(jObject.getString("rate"));
                        mMechanic.setmRating(jObject.getString("rating"));
                        mMechanic.setPhonenumber(jObject.getString("phone_number"));

                        data.postValue(mMechanic);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // TODO when setting user fails
                    }
                }
            }
        });
        return data;
    }

    public void setMechanic(String json, String userId, String authToken) {
        RestClient.createMechanic(userId, json, authToken, new Callback() {
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
                    Log.i(TAG,response.body().toString());
                }
            }
        });
    }
}
