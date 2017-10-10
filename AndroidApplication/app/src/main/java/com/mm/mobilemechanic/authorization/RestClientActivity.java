package com.mm.mobilemechanic.authorization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.login.mm.nealio.testlogin.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by ndw6152 on 5/9/2017.

 */

public class RestClientActivity extends AppCompatActivity {
    private String TAG = "REST CLIENT TEST";

    private Callback getCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
//                Headers responseHeaders = response.headers();
//                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                    Log.i(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
                Log.i(TAG, response.message());
                Log.i(TAG, response.body().string());
            }
        }
    };


    private Callback postCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            } else {
                Log.i("POST test", response.message());
                Log.i("POST test", response.body().string());
            }
        }
    };

    public void testGetCall(String url) {
        RestClient.get(url, getCallback);
    }

    public void testPostCall(String url, String postBody) {
        RestClient.post(url, postBody, getCallback);
    }

    public void runTestPostCall() {
        testGetCall("https://api.github.com/users/codepath");
        testGetCall("http://publicobject.com/helloworld.txt");

        Log.i(TAG, "--------");
        testPostCall("https://api.github.com/markdown/raw", "" + "Releases\n" + "--------\n" + "\n" + " * _1.0_ May 6, 2013\n" + " * _1.1_ June 15, 2013\n" + " * _1.2_ August 11, 2013\n");
    }


    public void runPOST() {
        JsonObject json = new JsonObject();
        json.addProperty("username", "EAALiW35MchoBAM6nFmnICAafVbJtRRKTIrd5cXaBJZBFe6KloSoHm4qybnSgMKKUMb5oxtMK9z9lfHil6rNmbjrBzttWAseqw02gwaZBnbSVndLBV4ZBjq5pZCH2mZBGs6XA7zPVscZBa9ZBXSLmRZAWKkXBDL6aXJr1EC0lYPV9tZCTeiPutbGhkaYDfnN68924ZD");
        json.addProperty("password", "none");

        String url = "http://50.43.59.234:5000/mobilemechanic/api/v1.0/auth";
        RestClient.post(url, json.toString(), getCallback);
        Log.i(TAG, "Finish posting");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "running main");
        runPOST();









    }
}

