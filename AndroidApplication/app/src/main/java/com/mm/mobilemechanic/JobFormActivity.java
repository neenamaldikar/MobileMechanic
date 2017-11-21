package com.mm.mobilemechanic;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mm.mobilemechanic.authorization.RestClient;
import com.mm.mobilemechanic.job.Job;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 5/14/2017.
 */

public class JobFormActivity extends AppCompatActivity {
    private String TAG = "New Job Form";
    private final int CHOOSING_IMAGE_FROM_GALLERY = 1000;

    private boolean changesMade = false;
    private String mJWToken;

    private Job mJob;
    @BindView(R.id.editText_job_summary) EditText mEditTextSummary;
    @BindView(R.id.editText_job_description) EditText mEditTextDescription;
    @BindView(R.id.editText_car_make) EditText mEditTextCarMake;
    @BindView(R.id.editText_car_model) EditText mEditTextCarModel;
    @BindView(R.id.editText_car_year) EditText mEditTextCarYear;

    private int fieldsCount = 5;


    private void showToast(final String message) {
        runOnUiThread (new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }));

    }
    private ImageView createImageViewsWhenChosen(Uri imageUri) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(layoutParams);
        imgView.setImageURI(null);
        imgView.setImageURI(imageUri);
        imgView.setClickable(true);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), v.getWindowId()+"", Toast.LENGTH_SHORT).show();
            }
        });
        return imgView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case CHOOSING_IMAGE_FROM_GALLERY:
                    // When an Image is picked
                    // Get the Image from data
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    ArrayList<String> imagesEncodedList = new ArrayList<String>();

                    LinearLayout linearLayoutImages = (LinearLayout)findViewById(R.id.ll_images_from_gallery);

                    if(data.getData()!= null){
                        Uri imageUri = data.getData();
                        ImageView imgView = createImageViewsWhenChosen(imageUri);
                        linearLayoutImages.addView(imgView);

                    }
                    else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri imageUri = item.getUri();

                                ImageView imgView = createImageViewsWhenChosen(imageUri);
                                linearLayoutImages.addView(imgView);

                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
            }
        }
    }

    public void ChoosePicturesFromGalleryOnClick(View view) {
        changesMade = true;
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), CHOOSING_IMAGE_FROM_GALLERY);
        LinearLayout imageLayout = (LinearLayout)findViewById(R.id.ll_images_from_gallery);
        if((imageLayout).getChildCount() > 0)
            imageLayout.removeAllViews();

    }

    public String createJsonFromFields(Job job) {
        JsonObject updated_values = new JsonObject();
        JsonObject inner = new JsonObject();
        inner.addProperty("make", job.getMake());
        inner.addProperty("model", job.getModel());
        inner.addProperty("year", job.getYear());
        inner.addProperty("summary", job.getSummary());
        inner.addProperty("description", job.getDescription());

        JsonObject options = new JsonObject();
        options.addProperty("onsite_diagnostic", job.isOnSiteDiagnostic());
        options.addProperty("working", job.isCarInWorkingCondition());
        options.addProperty("onsite_repair", job.isRepairDoneOnSite());
        options.addProperty("pickup_dropoff", job.isCarPickUpAndDropOff());

        inner.addProperty("status", "Submitted");
        inner.add("options", options);
        updated_values.add("job", inner);
        return updated_values.toString();
    }

    public void sendJob(String json, String userId, String authToken) {

        RestClient.createJob(userId, json, authToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
                Log.e(TAG, "Fail = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code = " + response.code() + " " + response.message());
                } else {

                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i(TAG, jObject.toString());
                        Log.i(TAG, jObject.getString("job_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newJob", "" + new Gson().toJson(mJob));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.button_submit_job)
    public void submitJobOnClick(View view) {

        if(fieldsCount == 0) {
            mJob.setSummary(mEditTextSummary.getText().toString());
            mJob.setDescription(mEditTextDescription.getText().toString());
            mJob.setMake(mEditTextCarMake.getText().toString());
            mJob.setModel(mEditTextCarModel.getText().toString());
            if(!mEditTextCarYear.getText().toString().equals("")) {
                mJob.setYear(Integer.parseInt(mEditTextCarYear.getText().toString()));
            }
            String jobPayload = createJsonFromFields(mJob);
            sendJob(jobPayload, Profile.getCurrentProfile().getId(), mJWToken);
        }
        else {
            showToast("Missing required fields");
        }


    }


    private void launchUnsavedChangesDialogBox() {
        if(changesMade) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("Are you sure you want to discard these changes?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            finish();
        }

    }

    public void cancelJobOnClick(View view) {
        launchUnsavedChangesDialogBox();
    }



    private void initializeSwitches() {
        Switch switchOnSiteDiagnostic = (Switch) findViewById(R.id.switch_on_site_diagnostic);
        switchOnSiteDiagnostic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setOnSiteDiagnostic(isChecked);
                changesMade = true;
            }
        });

        Switch switchCarInWorkingCondition = (Switch) findViewById(R.id.switch_car_in_working_condition);
        switchCarInWorkingCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setCarInWorkingCondition(isChecked);
                changesMade = true;
            }
        });

        Switch switchRepairCanBeDoneOnSite = (Switch) findViewById(R.id.switch_repair_done_on_site);
        switchRepairCanBeDoneOnSite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setRepairCanBeDoneOnSite(isChecked);
                changesMade = true;
            }
        });

        Switch switchCarPickUpDropOff = (Switch) findViewById(R.id.switch_car_pick_up_drop_off);
        final Switch switchParkingAvailable = (Switch) findViewById(R.id.switch_parking_available);
        switchCarPickUpDropOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setCarPickUpAndDropOff(isChecked);
                changesMade = true;
                if(isChecked) {
                    switchParkingAvailable.setVisibility(View.VISIBLE);

                }
                else {
                    switchParkingAvailable.setVisibility(View.INVISIBLE);
                    switchParkingAvailable.setChecked(false);
                    mJob.setParkingAvailable(false);
                }
            }
        });

        switchParkingAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setParkingAvailable(isChecked);
                changesMade = true;
            }
        });
    }


    public void addListenerToEditTexts() {
        LinearLayout jobEditTextViews = (LinearLayout) findViewById(R.id.ll_create_job);
        for( int i = 0; i < jobEditTextViews.getChildCount(); i++ ) {
            if(jobEditTextViews.getChildAt(i) instanceof EditText) {
                final EditText editText = ((EditText) jobEditTextViews.getChildAt(i));
                editText.setError("Field cannot be empty");
                editText.addTextChangedListener(new TextWatcher() {
                    int prevSize;
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        prevSize = editText.getText().toString().length();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(editText.getText().toString().isEmpty()) {
                            editText.setError("Field cannot be empty");
                            fieldsCount++;
                        }
                        else {
                            if(prevSize == 0) {
                                fieldsCount--;
                            }
                        }
                    }
                });
            }
        }
    }
    @Override
    public void onBackPressed() {
        launchUnsavedChangesDialogBox();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_form);
        ButterKnife.bind(this);
        mJob = new Job();

        mJWToken = getIntent().getExtras().getString("JWT");

        initializeSwitches();
        addListenerToEditTexts();

    }



}
