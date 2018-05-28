package com.mm.mobilemechanic.models.jobQuote;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.mobilemechanic.R;

import java.util.ArrayList;

/**
 * Created by ndw6152 on 5/26/2018.
 * following wiki from https://github.com/codepath/android_guides/wiki/Using-a-BaseAdapter-with-ListView
 */
public class ItemCostAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<ListViewItem> itemList;

    public ItemCostAdapter(Context context, ArrayList<ListViewItem> arr) {
        this.context = context;
        itemList = arr;

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeItem(int position) {
        Log.i("removing", "" + position);
        itemList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.activity_jobquote_item_cost, viewGroup, false);
        }

        TextView name = view.findViewById(R.id.textView_item_name);
        TextView cost = view.findViewById(R.id.textView_item_cost);
        Button deleteButton = view.findViewById(R.id.button_item_remove);

        name.setText(itemList.get(i).itemName);
        double value = itemList.get(i).itemCost;
        cost.setText("$" + value + "");

        final String str = name.getText() + "";
        final int position = i;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                removeItem(position);
            }
        });

        return view;
    }
}
