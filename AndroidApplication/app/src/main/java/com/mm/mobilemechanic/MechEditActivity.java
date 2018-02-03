package com.mm.mobilemechanic;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mm.mobilemechanic.user.Mechanic;
import com.mm.mobilemechanic.user.MechanicViewModel;
import com.mm.mobilemechanic.user.User;
import com.mm.mobilemechanic.user.UserProfileViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ndw6152 on 4/15/2017.
 */

public class MechEditActivity extends AppCompatActivity {
    private String TAG = "MechincScreen";
    private boolean textChanged = false;

    @BindView(R.id.editText_mech_phone)
    EditText mEditTextMechPhone;
    @BindView(R.id.editText_mech_work_address)
    EditText mEditTextMechAddress;
    @BindView(R.id.editText_mech_rate)
    EditText mEditTextMechRate;
    @BindView(R.id.editText_mech_zipcode)
    EditText mEditTextMechZipcode;
    @BindView(R.id.editText_mech_work_city)
    EditText mEditTextMechCity;
    @BindView(R.id.editText_mech_work_state)
    EditText mEditTextMechState;

    @BindView(R.id.rating_mech_rating)
    RatingBar mRatingMechRating;


    private MechanicViewModel viewModel;
    private String mJWToken;
    private GoogleApiClient mGoogleApiClient;

    private int fieldsCount = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_profile_edit);
        ButterKnife.bind(this);


        // adding the done button at the top left
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();
        addListenerToEditTexts();
        addOnTextChangedListenerToAllEditText(R.id.ll_mech_profile_text_views);


    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_profile_edit_actions, menu);
        return true;
    }

    public String createJsonFromFields() {
        JsonObject updated_values = new JsonObject();
        JsonObject inner = new JsonObject();

        inner.addProperty("phone_number", mEditTextMechPhone.getText().toString());
        inner.addProperty("address_line", mEditTextMechAddress.getText().toString());

        inner.addProperty("city", mEditTextMechCity.getText().toString());
        inner.addProperty("state", mEditTextMechState.getText().toString());
        inner.addProperty("zipcode", mEditTextMechZipcode.getText().toString());
        inner.addProperty("rate", mEditTextMechRate.getText().toString());

        inner.addProperty("rating", "0");
        JsonArray mJsonReviews = new JsonArray();
        mJsonReviews.add("Great mechanic");
        mJsonReviews.add("Gets the job done!");

        inner.add("reviews", mJsonReviews);

        JsonArray mJsonZipcodes = new JsonArray();
        mJsonZipcodes.add("76013");
        mJsonZipcodes.add("76017");

        inner.add("serving_zipcodes", mJsonZipcodes);


        updated_values.add("updated_values", inner);

        return updated_values.toString();
    }


    public void actionOnCloseButtonPressed() {
        if (textChanged) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("Are you sure you want to discard these changes?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("Closing everything");
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showToast("cancel and stay on screen");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_save_profile:

                if (fieldsCount == 0) {
                    String updated_values = createJsonFromFields();
                    viewModel.setMechanic(updated_values, mJWToken);
                    showToast("Saving");
                    textChanged = false; // set to false to prevent the dialog box from showing
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    showToast("Missing required fields");
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        actionOnCloseButtonPressed();
        return false;
    }


    public void addOnTextChangedListenerToAllEditText(@android.support.annotation.IdRes int layoutId) {
        LinearLayout mechEditTextViews = (LinearLayout) findViewById(layoutId);

        // listener to update a flag that a text box was edited
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        // loop to add the listener to all edittext box
        for (int i = 0; i < mechEditTextViews.getChildCount(); i++) {
            if (mechEditTextViews.getChildAt(i) instanceof EditText) {
                ((EditText) mechEditTextViews.getChildAt(i)).addTextChangedListener(tw);
            }
        }
    }

    private void initUI() {

        mJWToken = getIntent().getExtras().getString("JWT");

        viewModel = ViewModelProviders.of(this).get(MechanicViewModel.class);
        viewModel.init(Profile.getCurrentProfile().getId(), mJWToken);
        viewModel.getMechanic().observe(this, new Observer<Mechanic>() {
            @Override
            public void onChanged(@Nullable Mechanic mechanic) {

                if (mechanic != null) {
                    mEditTextMechPhone.setText(mechanic.getPhonenumber());
                    mEditTextMechAddress.setText(mechanic.getAddress());
                    mEditTextMechZipcode.setText(mechanic.getZipCode());
                    mEditTextMechRate.setText(mechanic.getmRate());
                    mEditTextMechState.setText(mechanic.getState());
                    mEditTextMechCity.setText(mechanic.getCity());
                    //mRatingMechRating.setRating(Integer.parseInt(mechanic.getmRating()));
                  //  mRatingMechRating.setRating(3);

                }

            }
        });


    }

    public void addListenerToEditTexts() {
        LinearLayout mechEditTextViews = (LinearLayout) findViewById(R.id.ll_mech_profile_text_views);
        for (int i = 0; i < mechEditTextViews.getChildCount(); i++) {
            if (mechEditTextViews.getChildAt(i) instanceof EditText) {
                final EditText editText = ((EditText) mechEditTextViews.getChildAt(i));
                //   editText.setError("Field cannot be empty");
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
                        if (editText.getText().toString().isEmpty()) {
                            editText.setError("Field cannot be empty");
                            fieldsCount++;
                        } else {
                            if (prevSize == 0) {
                                fieldsCount--;
                            }
                        }
                    }
                });
            }
        }
    }


}
