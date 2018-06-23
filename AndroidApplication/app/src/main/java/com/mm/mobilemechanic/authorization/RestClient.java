package com.mm.mobilemechanic.authorization;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ndw6152 on 5/9/2017.
 *
 */

public class RestClient {

    private static String TAG = "RestClient";
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType form_data = MediaType.parse("multipart/form-data; charset=utf-8");
    //private static String URL_BASE ="http://mobilemechanic.herokuapp.com";
    private static String URL_BASE = "http://192.168.86.49:5000";
    private static String URI_AUTH= "/mobilemechanic/api/v1.0/auth";
    private static String URI_USER = "/mobilemechanic/api/v1.0/users/";
    private static String URI_JOB = "/jobs";
    private static String URI_TOKEN = "/token";
    private static String URI_QUOTE = "/quotes";


    private static String URI_PICTURE = "/picture";
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

    public static void GETMULTIPART(String url, String authToken, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .header("Content-Type", "multipart/form-data")
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
        Log.d("mainActivityLog", "POST call is now made ...");

    }


    public static void POSTMULTIPART(final String url, String authToken, File imgFile, Callback responseCallback) {

         MediaType MEDIA_TYPE =
                MediaType.parse("image/png") ;

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

                builder.addFormDataPart("imagefile",imgFile.getName(),RequestBody.create(MEDIA_TYPE,imgFile));


        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .header("Content-Type", "multipart/form-data")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(responseCallback);

    }

    public static void DELETE(final String url, String authToken, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authToken)
                .delete()
                .build();
        client.newCall(request).enqueue(responseCallback);
        Log.d("mainActivityLog", "DELETE call is now made ...");
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

    public static void getUserJobs(String userId, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB;
        RestClient.GET(url, authToken, responseCallback);
    }
    ////////////////////

    public static void createMechanic(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_MECHANIC;
        RestClient.PUT(url, authToken, json, responseCallback);
    }

    public static void getMechanic(String userId, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_MECHANIC;
        RestClient.GET(url, authToken, responseCallback);
    }

    ////////////////////
    public static void createJob(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB;
        RestClient.POST(url, authToken, json, responseCallback);
    }

    public static void addJobImage(String userId, String jobId, File imgFile, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB + "/" + jobId + URI_PICTURE;
        RestClient.POSTMULTIPART(url, authToken, imgFile, responseCallback);
    }

    public static void getJobImage(String userId, String authToken, String jobId, String pictureId , Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB + "/"+jobId + URI_PICTURE + "?picture_id=" +pictureId;
        RestClient.GETMULTIPART(url, authToken, responseCallback);
    }

    public static void updateJob(String userId,String jobId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB+"?job_id="+jobId;
        RestClient.PUT(url, authToken, json, responseCallback);
    }

    public static void deleteJob(String userId, String jobId, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_JOB+"?job_id="+jobId;
        RestClient.DELETE(url, authToken, responseCallback);
    }
    ////////////////////
    public static void createToken(String userId, String json, String authToken, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + userId + URI_TOKEN;
        Log.d("mainActivityLog", "Create token function is called ... " + url);
        RestClient.POST(url, authToken, json, responseCallback);
    }
    ////////////////////
    public static void createQuote(String authToken, String customerId, String jobId, String json, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + customerId + URI_JOB + "/" + jobId + URI_QUOTE;
        Log.w("mainActivityLog", "Create new quote called " + url);
        RestClient.POST(url, authToken, json, responseCallback);
    }

    public static void getAllQuotes(String authToken, String customerId, String jobId, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + customerId + URI_JOB + "/" + jobId + URI_QUOTE;
        Log.w("mainActivityLog", "Get all quotes" + url);
        RestClient.GET(url, authToken, responseCallback);
    }

    public static void getSingleQuote(String authToken, String customerId, String jobId, String quoteId, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + customerId + URI_JOB + "/" + jobId + URI_QUOTE + "?picture_id=" + quoteId;
        Log.w("mainActivityLog", "Get single quote " + url);
        RestClient.GET(url, authToken, responseCallback);
    }

    public static void deletetAllQuotes(String authToken, String customerId, String jobId, Callback responseCallback) {
        String url = RestClient.URL_BASE + URI_USER + customerId + URI_JOB + "/" + jobId + URI_QUOTE;
        Log.w("mainActivityLog", "Get all quotes" + url);
        RestClient.DELETE(url, authToken, responseCallback);
    }


}

