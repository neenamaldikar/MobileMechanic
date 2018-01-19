package com.mm.mobilemechanic;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mm.mobilemechanic.authorization.RestClient;
import com.mm.mobilemechanic.job.Job;
import com.mm.mobilemechanic.util.Utility;
import com.tuanchauict.intentchooser.ImageChooserMaker;
import com.tuanchauict.intentchooser.selectphoto.ImageChooser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 5/14/2017.
 */

public class JobAddImageActivity extends AppCompatActivity {
    private String TAG = "New Job Image";
    private final int CHOOSING_IMAGE_FROM_GALLERY = 1000;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    private boolean changesMade = false;
    private String mJWToken;
    int PERMISSION_ALL = 1;
    private String mJob;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    private void showToast(final String message) {
        runOnUiThread(new Thread(new Runnable() {
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
                Toast.makeText(getApplicationContext(), v.getWindowId() + "", Toast.LENGTH_SHORT).show();
            }
        });
        return imgView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
              /*  case CHOOSING_IMAGE_FROM_GALLERY:
                    // When an Image is picked
                    // Get the Image from data
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    ArrayList<String> imagesEncodedList = new ArrayList<String>();


                    LinearLayout linearLayoutImages = (LinearLayout) findViewById(R.id.ll_images_from_gallery);


                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri imageUri = item.getUri();
                            File imageFile = new File(getRealPathFromURI(imageUri));
                            imageUri = Uri.fromFile(imageFile);
                            ImageView imgView = createImageViewsWhenChosen(imageUri);
                            linearLayoutImages.addView(imgView);
                            mArrayUri.add(imageUri);
                        }
                    } else if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        File imageFile = new File(getRealPathFromURI(imageUri));
                        imageUri = Uri.fromFile(imageFile);
                        ImageView imgView = createImageViewsWhenChosen(imageUri);
                        linearLayoutImages.addView(imgView);
                        mArrayUri.add(imageUri);
                    }


                  *//*  if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        File imageFile = new File(getRealPathFromURI(imageUri));
                        imageUri = Uri.fromFile(imageFile);
                        ImageView imgView = createImageViewsWhenChosen(imageUri);
                        linearLayoutImages.addView(imgView);
                        mArrayUri.add(imageUri);

                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            mArrayUri = new ArrayList<Uri>();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri imageUri = item.getUri();
                                File imageFile = new File(getRealPathFromURI(imageUri));
                                imageUri = Uri.fromFile(imageFile);
                                ImageView imgView = createImageViewsWhenChosen(imageUri);
                                linearLayoutImages.addView(imgView);
                                mArrayUri.add(imageUri);
                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }*//*
                  break;;*/
                case CHOOSING_IMAGE_FROM_GALLERY: {
                    mArrayUri.clear();
                    LinearLayout linearLayoutImages = (LinearLayout) findViewById(R.id.ll_images_from_gallery);


                    List<Uri> imageUris = ImageChooserMaker.getPickMultipleImageResultUris(this, data);
                    for (int i = 0; i < imageUris.size(); i++) {
                        ImageView imgView = createImageViewsWhenChosen(imageUris.get(i));
                        linearLayoutImages.addView(imgView);
                        mArrayUri.add(imageUris.get(i));
                    }

                }
            }
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ChoosePicturesFromGalleryOnClick(View view) {
        changesMade = true;
      /*  Intent imageIntent = new Intent();
        imageIntent.setType("image*//*");
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), CHOOSING_IMAGE_FROM_GALLERY);
*/

     /*   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*"); //allows any image file type. Change * to specific extension to limit it
/*//**These following line is the important one!
         intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSING_IMAGE_FROM_GALLERY);
         */
        Intent intent = ImageChooserMaker.newChooser(JobAddImageActivity.this)
                .add(new ImageChooser(true))
                .create("Select Image");
        startActivityForResult(intent, CHOOSING_IMAGE_FROM_GALLERY);


        LinearLayout imageLayout = (LinearLayout) findViewById(R.id.ll_images_from_gallery);
        if ((imageLayout).getChildCount() > 0)
            imageLayout.removeAllViews();

    }


    public void sendJobImages(File imgFile) {
        Utility.showSimpleProgressDialog(this);
        RestClient.addJobImage(Profile.getCurrentProfile().getId(), mJob, imgFile, mJWToken, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO on failure what happens
                Utility.removeSimpleProgressDialog();
                Log.e(TAG, "Fail = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Utility.removeSimpleProgressDialog();
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
        File mImgFile = new File(getRealPathFromURI(mArrayUri.get(0)));
        sendJobImages(mImgFile);
    }


    public void cancelJobOnClick(View view) {

    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_addimage);
        ButterKnife.bind(this);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        mJob = getIntent().getExtras().getString("newJob");
        mJWToken = getIntent().getExtras().getString("JWT");


    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}
