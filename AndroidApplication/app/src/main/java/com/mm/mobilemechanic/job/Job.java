package com.mm.mobilemechanic.job;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by ndw6152 on 5/22/2017.
 */

public class Job {
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

    @SerializedName("address_line")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zipcode")
    private int zipCode;

    @SerializedName("options")
    private JobOptions jobOptions;

    @SerializedName("status")
    private JobStatus status;


    public Job() {
        this.jobOptions = new JobOptions();
    }

    public Job (String summary, String description, JobOptions jobOptions, JobStatus status) {
        this.summary = summary;
        this.description = description;
        this.jobOptions = jobOptions;
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

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
    public int getZipCode() {
        return zipCode;
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

        private boolean parkingAvailable = false;  // TODO will add field


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