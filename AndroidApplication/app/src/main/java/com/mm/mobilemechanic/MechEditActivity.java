package com.mm.mobilemechanic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;

/**
 * Created by ndw6152 on 4/15/2017.
 *
 */

public class MechEditActivity extends AppCompatActivity {
    private String TAG = "MechincScreen";
    private boolean textChanged = false;

    @BindView(R.id.editText_profile_name) EditText mEditTextProfileName;
    @BindView(R.id.editText_profile_address) EditText mEditTextProfileAddress;
    @BindView(R.id.editText_profile_city) EditText mEditTextProfileCity;
    @BindView(R.id.editText_profile_state) EditText mEditTextProfileState;
    @BindView(R.id.editText_profile_zipcode) EditText mEditTextProfileZipCode;
    @BindView(R.id.editText_profile_additional_info) EditText mEditTextBio;


    @BindView(R.id.editText_profile_email) EditText mEditTextemail;
    @BindView(R.id.editText_profile_gender) EditText mEditTextGender;
    @BindView(R.id.editText_profile_phone_number) EditText mEditTextPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mech_profile_edit);


        addOnTextChangedListenerToAllEditText(R.id.ll_mech_profile_text_views);


        // adding the done button at the top left
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.menu_save_profile:
                showToast("Saving");
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
        for( int i = 0; i < mechEditTextViews.getChildCount(); i++ ) {
            if( mechEditTextViews.getChildAt(i) instanceof EditText) {
                ((EditText) mechEditTextViews.getChildAt(i)).addTextChangedListener(tw);
            }
        }
    }



}
