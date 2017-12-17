package com.mm.mobilemechanic.job;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ndw6152 on 10/17/2017.
 */

public enum JobStatus {

    @SerializedName("Quotes_requested")
    QUOTES_REQUESTED("Quotes_requested"),
    @SerializedName("Quotes_available")
    QUOTES_AVAILABLE("Quotes_available"),
    @SerializedName("Job_in_progress")
    JOB_IN_PROGRESS("Job_in_progress"),
    @SerializedName("Job_done")
    JOB_DONE("Job_done"),
    @SerializedName("Job_request_closed")
    CLOSED("Job_request_closed"),
    @SerializedName("Submitted")
    SUBMITTED("Submitted");


    private final String name;

    JobStatus(final String name) {
        this.name = name;
    }


    public String toString() {
        return this.name;
    }
}
