package com.mm.mobilemechanic.job;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ndw6152 on 5/22/2017.
 */

public class Job implements Serializable {
    @SerializedName("job_id")
    private String job_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("summary")
    private String summary;

    @SerializedName("description")
    private String description;

    @SerializedName("make")
    private String make;


    @SerializedName("model")
    private String model;

    @SerializedName("year")
    private String year;

    @SerializedName("address_line")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zipcode")
    private String zipCode;

    @SerializedName("images")
    private String[] images;


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
    public Job(String summary, String description, boolean onSiteDiagnostic, boolean carInWorkingCondition, boolean repairCanBeDoneOnSite, boolean carPickUpAndDropOff, JobStatus status) {

        this.summary = summary;
        this.description = description;
        this.jobOptions = jobOptions;
        this.status = status;
    }
    public String getJob_id() {
        return job_id;
    }
    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
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
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public String[] getImages() {
        return images;
    }
    public void setImages(String[] images) {
        this.images = images;
    }

    public JobOptions getJobOptions() {
        return jobOptions;
    }
    public void setJobOptions(JobOptions jobOptions) {
        this.jobOptions = jobOptions;
    }

    public class JobOptions implements Serializable {


        @SerializedName("onsite_diagnostic")
        private boolean onSiteDiagnostic = false;
        @SerializedName("working")
        private boolean carInWorkingCondition = false;
        @SerializedName("onsite_repair")
        private boolean repairCanBeDoneOnSite = false;
        @SerializedName("pickup_dropoff")
        private boolean carPickUpAndDropOff = false;
        @SerializedName("parking_available")
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
}