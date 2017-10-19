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
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mm.mobilemechanic.user.Job;

import java.util.ArrayList;

/**
 * Created by ndw6152 on 5/14/2017.
 */

public class JobFormActivity extends AppCompatActivity {
    private String TAG = "New Job Form";
    private final int CHOOSING_IMAGE_FROM_GALLERY = 1000;

    private boolean changesMade = false;

    private Job mJob;



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

    public void submitJobOnClick(View view) {
        Intent resultIntent = new Intent();
        TextView summaryView = (TextView) findViewById(R.id.editText_job_summary);
        mJob.setSummary("" + summaryView.getText());

        TextView descriptionView = (TextView) findViewById(R.id.editText_job_description);
        mJob.setDescription("" + descriptionView.getText());
        resultIntent.putExtra("newJob", "" + new Gson().toJson(mJob));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
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

    @Override
    public void onBackPressed() {
        launchUnsavedChangesDialogBox();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_form);

        mJob = new Job();

        initializeSwitches();

    }



}
