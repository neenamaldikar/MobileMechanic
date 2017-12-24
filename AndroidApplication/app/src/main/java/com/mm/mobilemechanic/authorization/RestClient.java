package com.mm.mobilemechanic.authorization;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ndw6152 on 5/9/2017.
 */

public class RestClient {

    private static String TAG = "RestClient";
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType form_data = MediaType.parse("multipart/form-data; charset=utf-8");

    private static String URL_BASE = "http://mobilemechanic.herokuapp.com";
    private static String URI_AUTH = "/mobilemechanic/api/v1.0/auth";
    private static String URI_USER = "/mobilemechanic/api/v1.0/users/";
    private static String URI_JOB = "/jobs";
    private static String URI_MECHANIC = "/mechanic";


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
                .header("Content-Type", "application/json")
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

        String url = RestClient.URL_BASE + URI_AUTH;
        RestClient.POST(url, json.toString(), responseCallback);
    }

    public static void getUserInfo(String userId, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId;
        RestClient.GET(url, authToken, responseCallback);
    }

    public static void updateUser(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId;
        RestClient.PUT(url, authToken, json, responseCallback);
    }

    public static void createJob(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB;
        RestClient.POST(url, authToken, json, responseCallback);
    }

    public static void getUserJobs(String userId, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB;
        RestClient.GET(url, authToken, responseCallback);
    }

    public static void createMechanic(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_MECHANIC;
        RestClient.PUT(url, authToken, json, responseCallback);
    }

    public static void getMechanic(String userId,  String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_MECHANIC;
        RestClient.GET(url, authToken, responseCallback);
    }


}

