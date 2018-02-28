package com.mm.mobilemechanic;

import android.app.Activity;

import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mm.mobilemechanic.authorization.RestClient;

import com.mm.mobilemechanic.job.Job;
import com.mm.mobilemechanic.job.JobRequestsAdapter;

import com.mm.mobilemechanic.job.JobStatus;
import com.mm.mobilemechanic.user.Mechanic;

import com.mm.mobilemechanic.user.MechanicViewModel;

import com.mm.mobilemechanic.user.User;
import com.mm.mobilemechanic.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainScreen";
    private User mCustomer;
    private String mJWTtoken;
    private MechanicViewModel viewModel;

    private static final int FROM_USER_PROFILE_SCREEN = 123;
    private static final int FROM_NEW_JOB_SCREEN = 124;

    private List<Job> jobLists;
    private JobRequestsAdapter adapter;
    public boolean isMechanic = false;

    private String fbUserId;

    private void showToast(final String message) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }));

    }


    private void getJobs() {
        String jwtoken = getIntent().getExtras().getString("JWT");
        Log.i(TAG, jwtoken);

        Utility.showSimpleProgressDialog(MainActivity.this);

        RestClient.getUserJobs(fbUserId, jwtoken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Utility.removeSimpleProgressDialog();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());

                    LoginManager.getInstance().logOut();
                } else {
                    Log.i(TAG, response.message());
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.i(TAG, jsonArray.toString());
                        jobLists.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Type collectionType = new TypeToken<Job>() {
                            }.getType();
                            Job job = (Job) new Gson()
                                    .fromJson(jsonArray.getJSONObject(i).toString(), collectionType);
                            jobLists.add(job);

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initUI() {
        jobLists = new ArrayList<Job>();
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_main_homescreen_jobs_table);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JobRequestsAdapter(this, jobLists);
        rv.setAdapter(adapter);

    }

    private void checkMechanic() {
        viewModel = ViewModelProviders.of(this).get(MechanicViewModel.class);
        viewModel.init(fbUserId, mJWTtoken);
        viewModel.getMechanic().observe(this, new Observer<Mechanic>() {
            @Override
            public void onChanged(@Nullable Mechanic mechanic) {

                if (mechanic != null) {
                    isMechanic = true;

                } else {
                    isMechanic = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_logo, menu);
        return true;
    }


    public void handleOption1() {
        Toast.makeText(getApplicationContext(), "Stuff", Toast.LENGTH_SHORT).show();
    }

    public void handleOption2() {
        Toast.makeText(getApplicationContext(), "Action chosen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_stuff:
                handleOption1();
                return true;
            case R.id.menu_action:
                handleOption2();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        Bundle b;
        switch (item.getItemId()) {
            case R.id.nav_profile:
                intent = new Intent(this, UserProfileActivity.class);
                b = new Bundle();
                b.putString("JWT", mJWTtoken);
                intent.putExtras(b);
                startActivityForResult(intent, FROM_USER_PROFILE_SCREEN);  // starting the intent with special id that will be called back
                break;
            case R.id.nav_history:

                break;
            case R.id.nav_payment:

                break;
            case R.id.nav_sign_up_mech:
                intent = new Intent(this, MechEditActivity.class);
                b = new Bundle();
                b.putString("JWT", mJWTtoken);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.nav_sign_out:
                LoginManager.getInstance().logOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_legal_info:
                Toast.makeText(getApplicationContext(), "Legal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case (FROM_USER_PROFILE_SCREEN):
                    System.out.println("came from user profile activity");
                    break;
                case (FROM_NEW_JOB_SCREEN):

                    break;
            }
        }
    }

    @OnClick(R.id.button_create_new_job)
    public void createNewJobOnClick(View view) {
        Intent intent;
        intent = new Intent(this, JobFormActivity.class);
        Bundle b = new Bundle();
        b.putString("JWT", mJWTtoken);
        intent.putExtras(b);
        startActivityForResult(intent, FROM_NEW_JOB_SCREEN);
    }


    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.activity_main_card_actions, popup.getMenu());
        popup.show();
    }

    public void editJobOnClick(View view, Job job) {
        Intent intent;
        intent = new Intent(this, JobFormActivity.class);
        Bundle b = new Bundle();
        b.putString("JWT", mJWTtoken);
        b.putSerializable("Job", job);
        intent.putExtras(b);
        startActivity(intent);
    }


    public void sendFirebaseToken(String authToken) {
        String token = FirebaseInstanceId.getInstance().getToken();
        JsonObject token_json = new JsonObject();
        token_json.addProperty("fcmtoken", token);
        token_json.addProperty("user_id", fbUserId);  // which user the token is associated with
        JsonObject final_token_json = new JsonObject();
        final_token_json.add("tokenData", token_json);

        String userId = fbUserId;
        RestClient.createToken(userId, final_token_json.toString(), authToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
                Log.e(TAG, "Fail = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("mainActivityLog", "Received response from server ...");
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                } else {
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i(TAG, jObject.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newToken", "Token sent successfully!");
                    setResult(Activity.RESULT_OK, resultIntent);
                }
            }
        });
    }

    public void downloadImage(String jobId, String pictureId, final ImageView iv) {
        RestClient.getJobImage(fbUserId, mJWTtoken, jobId, pictureId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                    // TODO on failure what happens
                } else {

                    Log.i(TAG + "_downloadImage", response.message());
                    try {
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                byte[] data = new byte[0];
                                try {
                                    data = response.body().bytes();
                                    final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            iv.setImageBitmap(bmp);
                                        }
                                    });


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        // TODO when setting user fails
                    }
                }
            }
        });
    }

    public void deleteJob(String jobID, final int position) {
        Utility.showSimpleProgressDialog(this);
        RestClient.deleteJob(fbUserId, jobID, mJWTtoken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
                Log.e(TAG, "Fail = " + e.getMessage());
                Utility.removeSimpleProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Utility.removeSimpleProgressDialog();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                    showToast("Sorry, Please try again");
                }
                else {
                    try {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("Job Deleted");
                                jobLists.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, jobLists.size());
                                adapter.notifyDataSetChanged();

                            }
                        });

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void submitJobQuote() {
        Intent intent = new Intent(getApplicationContext(), MechSubmitQuote.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mJWTtoken = getIntent().getExtras().getString("JWT");
        fbUserId = ((MobileMechanicApplication) getApplication()).getFbUserId();
        Log.i(TAG, mJWTtoken);

        // creation of notifications
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.getActiveNotifications();
        } else {
            Log.e(TAG, "notification manager is null");
        }

        sendFirebaseToken(mJWTtoken);
        initUI();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getJobs();
        checkMechanic();
    }
}

