package com.mm.mobilemechanic.models.jobQuote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Hashtable;

/*
  Created by ndw6152 on 5/27/2018.

 "quote": {
     "labor_cost":{
        "breaks":100, "oil change":40
     },
     "part_cost":{
        "break pads":100, "oil":10
     },
     "onsite_service_charges": 0,
     "comments":"part cost may change by 10%"
 }
 */

public class JobQuote {
    @SerializedName("quote_id")
    private String quoteId;

    @SerializedName("job_id")
    private String jobId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("comments")
    private String comments;

    @SerializedName("labor_cost")
    private Hashtable<String, Double> laborCostMap;

    @SerializedName("part_cost")
    private Hashtable<String, Double> partsCostMap;

    @SerializedName("onsite_service_charges")
    double onSiteServiceCharge;

    public JobQuote(ArrayList<ListViewItem> laborCostArray, ArrayList<ListViewItem> partsCostArray, double onSiteServiceCharge, String comments) {
        laborCostMap = new Hashtable<>();
        partsCostMap = new Hashtable<>();

        this.onSiteServiceCharge = onSiteServiceCharge;
        this.comments = comments;
        initMaps(laborCostArray, partsCostArray);
    }

    private void initMaps(ArrayList<ListViewItem> laborCostArray, ArrayList<ListViewItem> partsCostArray) {
        ListViewItem temp;
        for(int i = 0; i < laborCostArray.size(); i++) {
            temp = laborCostArray.get(i);
            laborCostMap.put(temp.itemName, temp.itemCost);
        }

        for(int i = 0; i < partsCostArray.size(); i++) {
            temp = partsCostArray.get(i);
            partsCostMap.put(temp.itemName, temp.itemCost);
        }
    }


    public String getQuoteId() {
        return quoteId;
    }




}