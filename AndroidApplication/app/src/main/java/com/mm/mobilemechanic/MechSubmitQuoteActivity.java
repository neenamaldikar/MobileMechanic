package com.mm.mobilemechanic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.mobilemechanic.models.jobQuote.ItemCostAdapter;
import com.mm.mobilemechanic.models.jobQuote.JobQuote;
import com.mm.mobilemechanic.models.jobQuote.ListViewItem;

import java.util.ArrayList;
import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MechSubmitQuoteActivity extends AppCompatActivity {

    private String TAG = "SubmitQuote";

    private final ArrayList<ListViewItem> laborCostArray = new ArrayList<>();
    private final ArrayList<ListViewItem> partsCostArray = new ArrayList<>();
    private double onSiteServiceCharge = 0.0;
    private String comments = "";

    private ItemCostAdapter laborCostAdapter;
    private ItemCostAdapter partsCostAdapter;

    public String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(this)
                .setMessage("Cancel quote?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return false;
    }

    public void submitQuote() {

    }

    public JobQuote createJobQuote() {
        return new JobQuote(laborCostArray, partsCostArray, onSiteServiceCharge, comments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.job_quote_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_save_profile)
        {
            JobQuote quote = createJobQuote();
            String str = toJson(quote);
            Log.i(TAG, str);

            submitQuote();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAddServiceChargeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_jobquote_onsitecharge_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText editText_onSiteCost = dialogView.findViewById(R.id.editText_onSiteCost);

        final TextView textView_onSiteCost = findViewById(R.id.textView_onSiteCost);

        dialogBuilder.setMessage("Enter on-site service charge:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Double value = 0.0;
                try {
                    value = Double.parseDouble(editText_onSiteCost.getText().toString());
                }
                catch (NullPointerException ignored) {}
                onSiteServiceCharge = value;
                String charge = "$" + editText_onSiteCost.getText().toString();
                textView_onSiteCost.setText(charge);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showAddItemDialog(final ArrayList<ListViewItem> array, final ItemCostAdapter adapter) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_jobquote_listview_item_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText editTextName = dialogView.findViewById(R.id.editText_item_name);
        final EditText editTextCost = dialogView.findViewById(R.id.editText_item_cost);


        dialogBuilder.setMessage("Enter on-site service charge:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Double value = 0.0;
                try {
                    value = Double.parseDouble(editTextCost.getText().toString());
                }
                catch (NullPointerException ignored) {}

                array.add(new ListViewItem(editTextName.getText().toString(), value));
                adapter.notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showAddCommentsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_jobquote_comments_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText editText = dialogView.findViewById(R.id.editText_comments);
        final TextView textView_comments = findViewById(R.id.textView_comments);
        dialogBuilder.setMessage("Enter on-site service charge:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                comments = editText.getText().toString();
                textView_comments.setText(comments);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void initQuoteListViews() {
        laborCostAdapter = new ItemCostAdapter(MechSubmitQuoteActivity.this, laborCostArray);
        ListView view = findViewById(R.id.listView_labor_cost);
        view.setAdapter(laborCostAdapter);


        partsCostAdapter = new ItemCostAdapter(MechSubmitQuoteActivity.this, partsCostArray);
        ListView view2 = findViewById(R.id.listView_parts_cost);
        view2.setAdapter(partsCostAdapter);
    }


    public void initFabActions() {
        FabSpeedDial fabSpeedDial = findViewById(R.id.fabSpeedDial_quote_actions);
        // fab actions and listeners
        // https://github.com/yavski/fab-speed-dial
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
        });


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_add_comments:
                        showAddCommentsDialog();
                        break;

                    case R.id.action_add_labor_cost:
                        showAddItemDialog(laborCostArray, laborCostAdapter);
                        break;

                    case R.id.action_add_parts_cost:
                        showAddItemDialog(partsCostArray, partsCostAdapter);
                        break;

                    case R.id.action_add_service_cost:
                        showAddServiceChargeDialog();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobquote);

        Objects.requireNonNull(this.getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initQuoteListViews();
        initFabActions();
    }
}
