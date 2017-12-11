package com.mm.mobilemechanic.job;

/**
 * Created by ndw6152 on 10/17/2017.
 *
 */

public enum JobStatus {
    QUOTES_REQUESTED("Quotes_requested"),
    QUOTES_AVAILABLE("Quotes_available"),
    JOB_IN_PROGRESS("Job_in_progress"),
    JOB_DONE("Job_done"),
    CLOSED("Job_request_closed");

    private final String name;
    JobStatus(final String name) { this.name = name; }



    public String toString() {
        return this.name;
    }
}
