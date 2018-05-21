package com.mm.mobilemechanic.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ndw6152 on 5/20/2018.
 */
public class JobQuote {



    @SerializedName("labor_cost")
    private Labor labor;

    @SerializedName("parts")
    private Parts parts;

    @SerializedName("onsite_service")
    private OnSiteService onSiteService;

    public JobQuote(Labor labor, Parts parts, OnSiteService onsiteService) {
        this.labor = labor;
        this.parts = parts;
        this.onSiteService = onsiteService;
    }


    protected class Labor implements Serializable {
        @SerializedName("description")
        private String description;
        @SerializedName("cost")
        private int cost;

        public Labor(String desc, int cost) {
            this.description = desc;
            this.cost = cost;
        }
    }

    protected class Parts implements Serializable {
        @SerializedName("description")
        private String description;
        @SerializedName("cost")
        private int cost;

        public Parts(String desc, int cost) {
            this.description = desc;
            this.cost = cost;
        }
    }

    protected class OnSiteService implements Serializable {
        @SerializedName("description")
        private String description;
        @SerializedName("cost")
        private int cost;

        public OnSiteService(String desc, int cost) {
            this.description = desc;
            this.cost = cost;
        }
    }
}
