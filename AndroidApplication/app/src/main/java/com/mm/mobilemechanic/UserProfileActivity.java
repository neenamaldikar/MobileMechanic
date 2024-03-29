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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mm.mobilemechanic.models.User;
import com.mm.mobilemechanic.models.UserProfileViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ndw6152 on 4/12/2017.
 *
 */

public class UserProfileActivity extends AppCompatActivity {
    private String TAG = "ProfileScreen";
    private boolean textChanged = false;
    private UserProfileViewModel viewModel;
    @BindView(R.id.editText_profile_name) EditText mEditTextProfileName;
    @BindView(R.id.editText_profile_address) EditText mEditTextProfileAddress;
    @BindView(R.id.editText_profile_city) EditText mEditTextProfileCity;
    @BindView(R.id.editText_profile_state) EditText mEditTextProfileState;
    @BindView(R.id.editText_profile_zipcode) EditText mEditTextProfileZipCode;
    @BindView(R.id.editText_profile_additional_info) EditText mEditTextBio;


    @BindView(R.id.editText_profile_email) EditText mEditTextemail;
    @BindView(R.id.editText_profile_gender) EditText mEditTextGender;
    @BindView(R.id.editText_profile_phone_number) EditText mEditTextPhoneNumber;

    private String mJWToken;
    private String fbUserId;

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_profile_edit_actions, menu);
        return true;
    }

    public void actionOnCloseButtonPressed() {
        if(textChanged) {
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
        }
        else {
            finish();
        }
    }

    public String createJsonFromFields() {
        JsonObject updated_values = new JsonObject();
        JsonObject inner = new JsonObject();

        inner.addProperty("address_line", mEditTextProfileAddress.getText().toString());
        inner.addProperty("city", mEditTextProfileCity.getText().toString());
        inner.addProperty("state", mEditTextProfileState.getText().toString());
        inner.addProperty("zipcode", mEditTextProfileZipCode.getText().toString());
        inner.addProperty("phone_number", mEditTextPhoneNumber.getText().toString());

        updated_values.add("updated_values", inner);

        return updated_values.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.menu_save_profile:  // save button pressed
                // Saving the user information
                String updated_values = createJsonFromFields();
                viewModel.setUser(updated_values, mJWToken);

                showToast("Saving");
                textChanged = false; // set to false to prevent the dialog box from showing

                Intent resultIntent = new Intent();
                TextView nameView = (TextView) findViewById(R.id.editText_profile_name);
                resultIntent.putExtra("key1", "" + nameView.getText());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        actionOnCloseButtonPressed();
        return false;
    }


    // TODO: need to figure out a way to only pop up dialog box when a change occur and not after i set the information
    public void addOnTextChangedListenerToAllEditText(@android.support.annotation.IdRes int layoutId) {
        LinearLayout profileEditTextViews = (LinearLayout) findViewById(layoutId);
        // listener to update a flag that a text box was edited
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged = true;
            }
        };

        // loop to add the listener to all edittext box
        for( int i = 0; i < profileEditTextViews.getChildCount(); i++ ) {
            if(profileEditTextViews.getChildAt(i) instanceof EditText) {
                final EditText editText = ((EditText) profileEditTextViews.getChildAt(i));
                editText.addTextChangedListener(tw);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        Objects.requireNonNull(this.getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJWToken = getIntent().getExtras().getString("JWT");
        fbUserId = ((MobileMechanicApplication)getApplication()).getFbUserId();

        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.init(fbUserId, mJWToken);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mEditTextProfileName.setText(user.getName());
                mEditTextProfileAddress.setText(user.getAddress());
                mEditTextProfileCity.setText(user.getCity());
                mEditTextProfileState.setText(user.getState());
                mEditTextProfileZipCode.setText(user.getZipCode());

                mEditTextPhoneNumber.setText(user.getPhonenumber());
                mEditTextemail.setText(user.getEmail());
                mEditTextGender.setText(user.getGender());
                mEditTextBio.setText(user.getBio());
            }
        });


        addOnTextChangedListenerToAllEditText(R.id.ll_profile_text_views);
    }
}
