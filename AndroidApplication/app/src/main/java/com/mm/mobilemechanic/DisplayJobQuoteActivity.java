package com.mm.mobilemechanic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.mobilemechanic.models.jobQuote.JobQuote;

import java.util.Hashtable;

/**
 * Created by ndw6152 on 7/6/2018.
 */
public class DisplayJobQuoteActivity extends AppCompatActivity {


    private void addItemsToLinearLayout(LinearLayout layout, String quoteName, String quotePrice) {
        LinearLayout ll_laborCost_item = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_display_jobquote_item, layout);//child.xml
        TextView textView_itemName = ll_laborCost_item.findViewById(R.id.textView_display_item_name);
        textView_itemName.setText(quoteName);
        TextView textView_itemCost = ll_laborCost_item.findViewById(R.id.textView_display_item_cost);
        textView_itemCost.setText("$"+ quotePrice);
    }

    // function is used to loop through the quote and then inflate a view showing the name and price
    // the inflated layout is then added to the activity's layout
    private void displayCostItems(LinearLayout layout, JobQuote quote, Hashtable<String, Double> table) {
        for(String key : table.keySet()) {
            addItemsToLinearLayout(layout, key, table.get(key)+"");
        }
    }

    private void populateView(JobQuote quote) {

        displayCostItems((LinearLayout)findViewById(R.id.ll_display_labor_cost), quote, quote.getLaborCostMap());
        displayCostItems((LinearLayout)findViewById(R.id.ll_display_parts_cost), quote, quote.getPartsCostMap());

        TextView textView_onSiteCharge = findViewById(R.id.textView_display_onsite_charge);
        textView_onSiteCharge.setText("On-site charge: $" + quote.getOnSiteServiceCharge());
        TextView textView_notes = findViewById(R.id.textView_display_comments);
        textView_notes.setText(quote.getComments());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_jobquote);
    }


    @Override
    protected void onResume() {
        super.onResume();

        String str = getIntent().getExtras().getString("quote");
        Gson gson = new Gson();
        JobQuote quote = gson.fromJson(str, JobQuote.class);
        populateView(quote);

    }
}
