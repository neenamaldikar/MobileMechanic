package com.mm.mobilemechanic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.mm.mobilemechanic.authorization.RestClient;
import com.mm.mobilemechanic.job.JobAddImageAdapter;
import com.mm.mobilemechanic.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ndw6152 on 5/14/2017.
 */

public class JobAddImagesActivity extends AppCompatActivity {
    private String TAG = "New Job Image";
    private final int CHOOSING_IMAGE_FROM_GALLERY = 1000, CHOOSE_FROM_CAMERA = 2000;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    private boolean changesMade = false;
    private String mJWToken;
    int PERMISSION_ALL = 1;
    int mPosition;
    public List<Uri> mImageList;
    String imageFilePath;
    private Uri outputUri = null;
    String jobPayload = "";
    int mCount = 0;
    ImageView imageView;
    JobAddImageAdapter jobAddImageAdapter;
    private String mJob, newJobFlag;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    private void showToast(final String message) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }));

    }

    private void initUI() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Uri uri = null;
        mImageList = new ArrayList<Uri>();
        mImageList.add(uri);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_job_images);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        jobAddImageAdapter = new JobAddImageAdapter(JobAddImagesActivity.this, mImageList);
        rv.setAdapter(jobAddImageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CHOOSING_IMAGE_FROM_GALLERY:


                    if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        File imageFile = new File(getRealPathFromURI(imageUri));
                        imageUri = Uri.fromFile(imageFile);

                        imageView.setImageURI(null);
                        imageView.setImageURI(imageUri);
                        mImageList.set(mPosition, imageUri);

                        if (mImageList.size() < 10) {
                            mImageList.add(null);
                            jobAddImageAdapter.notifyItemInserted(mPosition);
                            jobAddImageAdapter.notifyItemRangeChanged(mPosition, mImageList.size());
                            jobAddImageAdapter.notifyDataSetChanged();
                        } else {
                            //  showToast("yyyyyyyyyyyyyy");
                        }

                    } else {
                        if (data.getClipData() != null) {

                            ClipData mClipData = data.getClipData();
                            mArrayUri = new ArrayList<Uri>();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri imageUri = item.getUri();
                                File imageFile = new File(getRealPathFromURI(imageUri));
                                imageUri = Uri.fromFile(imageFile);

                                imageView.setImageURI(null);
                                imageView.setImageURI(imageUri);

                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                    break;

                case CHOOSE_FROM_CAMERA:

                    File imageFile = new File(imageFilePath);


                    Uri imageUri = Uri.fromFile(imageFile);
                    imageView.setImageURI(null);
                    imageView.setImageURI(imageUri);
                    mImageList.set(mPosition, imageUri);
                    if (mImageList.size() < 10) {
                        mImageList.add(null);
                        jobAddImageAdapter.notifyItemInserted(mPosition);
                        jobAddImageAdapter.notifyItemRangeChanged(mPosition, mImageList.size());
                        jobAddImageAdapter.notifyDataSetChanged();
                    } else {
                        //  showToast("yyyyyyyyyyyyyy");
                    }


                    break;

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

    public void choosePicturesFromGalleryOnClick() {
        changesMade = true;


        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), CHOOSING_IMAGE_FROM_GALLERY);

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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mCount++;
                    if (mCount < mImageList.size() && mImageList.get(mCount) != null) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                File mImgFile = new File(getRealPathFromURI(mImageList.get(mCount)));
                                sendJobImages(mImgFile);
                            }
                        });


                    } else {
                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("JWT", mJWToken);
                        resultIntent.putExtras(b);
                        startActivity(resultIntent);
                        finish();

                    }

                }
            }
        });
    }

    @OnClick(R.id.button_submit_job)
    public void submitJobOnClick(View view) {

        sendJob(jobPayload, Profile.getCurrentProfile().getId(), mJWToken);
       /* if (mImageList.get(0) != null) {
            File mImgFile = new File(getRealPathFromURI(mImageList.get(0)));
            sendJobImages(mImgFile);
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            Bundle b = new Bundle();
            b.putString("JWT", mJWToken);
            resultIntent.putExtras(b);
            startActivity(resultIntent);
            finish();

        }*/


    }

    @OnClick(R.id.button_skip_job)
    public void skipJobOnClick(View view) {

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle b = new Bundle();
        b.putString("JWT", mJWToken);
        resultIntent.putExtras(b);
        startActivity(resultIntent);
        finish();


    }


    @OnClick(R.id.button_cancel_job)
    public void cancelJobOnClick(View view) {

     /*   if (newJobFlag.equalsIgnoreCase("yes"))
            deleteJob();
        else {*/
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

        //   }


    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_setimages);
        ButterKnife.bind(this);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        mJob = getIntent().getExtras().getString("newJob");
        newJobFlag = getIntent().getExtras().getString("newJobFlag");
        mJWToken = getIntent().getExtras().getString("JWT");
        jobPayload= getIntent().getStringExtra("jobPayload");


        initUI();
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


    public void recyclerViewListClicked(View v, int position) {

        imageView = (ImageView) v;
        mPosition = position;
        showPictureDialog();

    }

    public void deleteImage(int position) {

        mImageList.set(position, null);

        jobAddImageAdapter.notifyItemChanged(mPosition, mImageList.size());
        jobAddImageAdapter.notifyDataSetChanged();

    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.text_choosepicture));
        String[] items = {getString(R.string.text_gallary),
                getString(R.string.text_camera)};

        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        choosePicturesFromGalleryOnClick();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;

                }
            }
        });
        dialog.show();
    }

    private void takePhotoFromCamera() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/MobileMechanic");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileMechanic/" + ts + ".jpg";
            File imageFile = new File(imageFilePath);
            outputUri = Uri.fromFile(imageFile); // convert path to Uri

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            } else {
                File file = new File(outputUri.getPath());
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CHOOSE_FROM_CAMERA);
            }
        } else {
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteJob() {
        Utility.showSimpleProgressDialog(this);
        RestClient.deleteJob(Profile.getCurrentProfile().getId(), mJob, mJWToken, new Callback() {
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

                } else {

                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i(TAG, jObject.toString());
                        showToast(jObject.getString("success"));
                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void sendJob(String json, String userId, String authToken) {
        Utility.showSimpleProgressDialog(this);
        RestClient.createJob(userId, json, authToken, new Callback() {
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
                } else {
                    String jobid = "";
                    try {
                        JSONObject jObject = new JSONObject(response.body().string());
                        Log.i(TAG, jObject.toString());
                        Log.i(TAG, jObject.getString("job_id"));
                        mJob = jObject.getString("job_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mImageList.get(0) != null) {
                        File mImgFile = new File(getRealPathFromURI(mImageList.get(0)));
                        sendJobImages(mImgFile);
                    } else {
                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("JWT", mJWToken);
                        resultIntent.putExtras(b);
                        startActivity(resultIntent);
                        finish();

                    }
                }
            }
        });
    }


}