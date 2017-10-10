package com.mm.mobilemechanic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.login.mm.nealio.testlogin.R;

/**
 * Created by ndw6152 on 4/12/2017.
 *
 */

public class ProfileEditActivity extends AppCompatActivity {
    private String TAG = "ProfileScreen";
    private boolean textChanged = false;

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
            if( profileEditTextViews.getChildAt(i) instanceof EditText) {
                ((EditText) profileEditTextViews.getChildAt(i)).addTextChangedListener(tw);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        addOnTextChangedListenerToAllEditText(R.id.ll_profile_text_views);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting initial information from the main activitiy and setting the first edit text view content
        Intent in = getIntent();
        Bundle b = in.getExtras();
        EditText nameView = (EditText) findViewById(R.id.editText_profile_name);
        nameView.setText(b.getString("key1"));
    }
}
