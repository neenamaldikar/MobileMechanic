package com.mm.mobilemechanic.job;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by ndw6152 on 5/22/2017.
 */

public class Job {

    @SerializedName("status")
    private JobStatus status;

    @SerializedName("summary")
    private String summary;

    @SerializedName("description")
    private String description;

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("year")
    private int year;


    @SerializedName("options")
    private JobOptions jobOptions;


    private boolean onSiteDiagnostic = false;
    private boolean carInWorkingCondition = false;
    private boolean repairCanBeDoneOnSite = false;
    private boolean carPickUpAndDropOff = false;
    private boolean parkingAvailable = false;

    public Job() {

    }

    public Job (String summary, String description, boolean onSiteDiagnostic, boolean carInWorkingCondition, boolean repairCanBeDoneOnSite, boolean carPickUpAndDropOff, JobStatus status) {
        this.summary = summary;
        this.description = description;
        this.onSiteDiagnostic = onSiteDiagnostic;
        this.carInWorkingCondition = carInWorkingCondition;
        this.repairCanBeDoneOnSite = repairCanBeDoneOnSite;
        this.carPickUpAndDropOff = carPickUpAndDropOff;
        this.status = status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
    public JobStatus getStatus() {
        return this.status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOnSiteDiagnostic() {
        return onSiteDiagnostic;
    }

    public void setOnSiteDiagnostic(boolean onSiteDiagnostic) {
        this.onSiteDiagnostic = onSiteDiagnostic;
    }

    public boolean isCarInWorkingCondition() {
        return carInWorkingCondition;
    }

    public void setCarInWorkingCondition(boolean carInWorkingCondition) {
        this.carInWorkingCondition = carInWorkingCondition;
    }

    public boolean isRepairDoneOnSite() {
        return repairCanBeDoneOnSite;
    }

    public void setRepairCanBeDoneOnSite(boolean repairCanBeDoneOnSite) {
        this.repairCanBeDoneOnSite = repairCanBeDoneOnSite;
    }

    public boolean isCarPickUpAndDropOff() {
        return carPickUpAndDropOff;
    }

    public void setCarPickUpAndDropOff(boolean carPickUpAndDropOff) {
        this.carPickUpAndDropOff = carPickUpAndDropOff;
    }

    public boolean isParkingAvailable() {
        return parkingAvailable;
    }
    public void setParkingAvailable(boolean parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public JobOptions getJobOptions() {
        return jobOptions;
    }

    public void setJobOptions(JobOptions jobOptions) {
        this.jobOptions = jobOptions;
    }


    public class JobOptions{

        @SerializedName("onsite_diagnostic")
        private boolean onSiteDiagnostic = false;

        @SerializedName("working")
        private boolean carInWorkingCondition = false;

        @SerializedName("onsite_repair")
        private boolean repairCanBeDoneOnSite = false;

        @SerializedName("pickup_dropoff")
        private boolean carPickUpAndDropOff = false;

        private boolean parkingAvailable = false;


        public boolean isOnSiteDiagnostic() {
            return onSiteDiagnostic;
        }

        public void setOnSiteDiagnostic(boolean onSiteDiagnostic) {
            this.onSiteDiagnostic = onSiteDiagnostic;
        }

        public boolean isCarInWorkingCondition() {
            return carInWorkingCondition;
        }

        public void setCarInWorkingCondition(boolean carInWorkingCondition) {
            this.carInWorkingCondition = carInWorkingCondition;
        }

        public boolean isRepairCanBeDoneOnSite() {
            return repairCanBeDoneOnSite;
        }

        public void setRepairCanBeDoneOnSite(boolean repairCanBeDoneOnSite) {
            this.repairCanBeDoneOnSite = repairCanBeDoneOnSite;
        }

        public boolean isCarPickUpAndDropOff() {
            return carPickUpAndDropOff;
        }

        public void setCarPickUpAndDropOff(boolean carPickUpAndDropOff) {
            this.carPickUpAndDropOff = carPickUpAndDropOff;
        }

        public boolean isParkingAvailable() {
            return parkingAvailable;
        }

        public void setParkingAvailable(boolean parkingAvailable) {
            this.parkingAvailable = parkingAvailable;
        }



    }


    /*{
        "description": "tube",
            "images": [],
        "job_id": "7889b9e7-c5d8-4767-97f5-0a71d3821c30",
            "make": "ford",
            "model": "falcon",
            "options": {
        "onsite_diagnostic": true,
                "onsite_repair": false,
                "pickup_dropoff": false,
                "working": true
    },
        "status": "Submitted",
            "summary": "summary xyz",
            "user_id": "790175691186546",
            "year": 2014
    }*/
}
