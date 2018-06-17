package com.mm.mobilemechanic.job;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ndw6152 on 10/17/2017.
 *
 */

public enum JobStatus {
    @SerializedName("submitted")
    SUBMITTED("submitted"),
    @SerializedName("quotes_available")
    QUOTES_AVAILABLE("quotes_available"),
    @SerializedName("quote_accepted")
    QUOTE_ACCEPTED("quote_accepted"),
    @SerializedName("job_in_progress")
    JOB_IN_PROGRESS("job_in_progress"),
    @SerializedName("job_completed")
    JOB_DONE("job_completed"),
    @SerializedName("job_request_closed")
    CLOSED("job_request_closed");


    private final String name;

    JobStatus(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
