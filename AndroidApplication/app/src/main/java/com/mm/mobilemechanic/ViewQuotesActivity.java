package com.mm.mobilemechanic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mm.mobilemechanic.authorization.RestClient;
import com.mm.mobilemechanic.job.Job;
import com.mm.mobilemechanic.models.jobQuote.JobQuote;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 6/22/2018.
 */
public class ViewQuotesActivity extends AppCompatActivity {
    private String TAG = "ViewQuptes";
    private String mJWToken;
    private String fbUserId;
    private String customerId;
    private String jobId;
    private List<JobQuote> quotesLists;

    private void showToast(final String message) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_logo, menu);
        return true;
    }


    public void populateView() {
        LinearLayout ll_available_quotes=(LinearLayout) findViewById(R.id.ll_available_quotes);
        LayoutInflater ll_inflater = LayoutInflater.from(this);
        for (int i = 0; i < quotesLists.size(); i++) {
            final Button button_viewQuote = new Button(this);
            button_viewQuote.setText("quote " + i);
            final int index = i;
            button_viewQuote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JobQuote quote = quotesLists.get(index);
                    showToast(quote.getQuoteId());
                }
            });

            ll_available_quotes.addView(button_viewQuote);
        }
    }



    public void getQuotes() {
        RestClient.getAllQuotes(mJWToken, customerId, jobId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("Failed to get available quotes");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                } else {
                    Log.i(TAG, response.message());
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.i(TAG, jsonArray.toString());
                        quotesLists.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Type collectionType = new TypeToken<JobQuote>() { }.getType();
                            JobQuote quote = (JobQuote) new Gson()
                                    .fromJson(jsonArray.getJSONObject(i).toString(), collectionType);
                            quotesLists.add(quote);

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateView();
                            }
                        });

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewquotes);
        mJWToken = getIntent().getExtras().getString("JWT");
        fbUserId = ((MobileMechanicApplication)getApplication()).getFbUserId();
        customerId = getIntent().getExtras().getString("customerId");
        jobId = getIntent().getExtras().getString("jobId");


        quotesLists = new ArrayList<JobQuote>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getQuotes();
    }
}
