package com.mm.mobilemechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mm.mobilemechanic.authorization.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 4/9/2017.
 *
 */

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginScreen";
    private CallbackManager mFBcallbackManager;
    private Callback getUserTokenCallback;
    private AccessToken mFbToken;

    private ProgressDialog mProgress;

    private void showToast(final String message) {
        runOnUiThread (new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "running the on activity");
        mFBcallbackManager.onActivityResult(requestCode, resultCode, data); // sending information to fb sdk
    }


    private void initFacebookLoginButton() {

        // using default fb button for login
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_login);
        loginButton.setReadPermissions();
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "user_birthday", "user_likes"));

        // Callback registration
        loginButton.registerCallback(mFBcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFbToken = loginResult.getAccessToken();
                mProgress.show();
                RestClient.getUserJWT(mFbToken, getUserTokenCallback);  //TODO uncomment when python service is running
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "on Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "FB error" + exception.toString());
                showToast("FB " + exception.getMessage());
            }
        });
    }

    public void onFailureJWT(final String errMessage) {
        mProgress.dismiss();
        LoginManager.getInstance().logOut();
        runOnUiThread (new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_LONG).show();
            }
        }));
    }

    public void onSuccessLaunchMainScreen(Bundle bundle) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        mProgress.dismiss();
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_logo, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFBcallbackManager = CallbackManager.Factory.create();
        initFacebookLoginButton();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching user info");
        mProgress.setIndeterminate(true);


        getUserTokenCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO
                Log.e(TAG +"1", e.getMessage());
                onFailureJWT("Error retrieving user information, please try again later");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG + "2", "Code = " + response.code() + " " + response.message());
                    onFailureJWT("Error" + response.code());
                }
                else {
                    Log.i(TAG, response.message());
                    Bundle bundle = new Bundle();

                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i(TAG, "Json object after successful login is " + jObject.toString());
                        String jwtToken = jObject.getString("access_token");
                        bundle.putString("JWT", "JWT " + jwtToken);
                        onSuccessLaunchMainScreen(bundle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mFbToken =  AccessToken.getCurrentAccessToken();
        if (mFbToken != null) {  // get the JWT directly and open the next screen
            mProgress.show();
            RestClient.getUserJWT(mFbToken, getUserTokenCallback);  //TODO uncomment when python service is running
        }


    }
}

