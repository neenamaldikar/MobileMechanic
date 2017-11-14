package com.mm.mobilemechanic.authorization;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *
 * Created by ndw6152 on 5/9/2017.
 */

public class RestClient {

    private static String TAG = "RestClient";
    private static OkHttpClient client = new OkHttpClient();
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static final MediaType form_data = MediaType.parse("multipart/form-data; charset=utf-8");

    public static String URL_ADDRESS ="mobilemechanic.herokuapp.com"; // "192.168.86.22"; // http://mobilemechanic.herokuapp.com

    public static void GET(String url, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    public static void GET(String url, String authToken, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    public static void PUT(String url, String authToken, String json, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .put(RequestBody.create(JSON, json))
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    public static void POST(final String url, String json, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    public static void POST(final String url, String authToken, String json, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .post(RequestBody.create(JSON, json))
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    //////////////////

    public static void getUserJWT(AccessToken fbToken, Callback responseCallback) {
        JsonObject json = new JsonObject();
        json.addProperty("username", fbToken.getToken());
        json.addProperty("password", "none");

        String url = "http://" + RestClient.URL_ADDRESS +"/mobilemechanic/api/v1.0/auth";
        RestClient.POST(url, json.toString(), responseCallback);
    }

    public static void getUserInfo(String userId, String authToken, Callback responseCallback) {
        String url = "http://" + RestClient.URL_ADDRESS +"/mobilemechanic/api/v1.0/users/" + userId;
        RestClient.GET(url, authToken, responseCallback);
    }

    public static void updateUser(String userId, String json, String authToken, Callback responseCallback) {
        String url = "http://" + RestClient.URL_ADDRESS +"/mobilemechanic/api/v1.0/users/" + userId;
        RestClient.PUT(url, authToken, json, responseCallback);
    }

}

