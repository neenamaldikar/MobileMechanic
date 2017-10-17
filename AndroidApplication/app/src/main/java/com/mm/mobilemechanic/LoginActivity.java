package com.mm.mobilemechanic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.view.Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 4/9/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginScreen";
    private CallbackManager mFBcallbackManager;
    private Callback getUserTokenCallback;

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
                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, "LoginActivity Response " + response.toString());

                                try {
                                    String name = object.getString("name");
                                    Log.d(TAG, "Name = " + name);
                                    String gender = object.getString("gender");
                                    Log.d(TAG, "Gender = " + gender);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, gender, work");
                request.setParameters(parameters);
                request.executeAsync();

                //// RestClient.getUserJWToken(token, "", getUserTokenCallback);  //TODO uncomment when python service is running
                Bundle bundle = new Bundle();
                bundle.putString("token", "FakeToken");
                onSuccessLaunchMainScreen(bundle);
                ////
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

    public void onSuccessLaunchMainScreen(Bundle bundle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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

        AccessToken token = AccessToken.getCurrentAccessToken();

        getUserTokenCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                    showToast("Error " + response.code());
                    LoginManager.getInstance().logOut();
                }
                else {
                    Log.i(TAG, response.message());
                    Bundle bundle = new Bundle();

                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        String jwtToken = jObject.getString("access_token");

                        bundle.putString("token", jwtToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    onSuccessLaunchMainScreen(bundle);
                }
            }
        };

        if (token != null) {  // get the JWT directly and open the next screen
            Toast.makeText(getApplicationContext(), "Token still valid", Toast.LENGTH_SHORT).show();

            //// RestClient.getUserJWToken(token, "", getUserTokenCallback);  //TODO uncomment when python service is running
            Bundle bundle = new Bundle();
            bundle.putString("token", "FakeToken");
            onSuccessLaunchMainScreen(bundle);
            ////
        }


    }
}

