package com.mm.mobilemechanic.user;

/**
 *
 * Created by ndw6152 on 5/22/2017.
 */

public class Job {

    private String summary;
    private String description;

    private boolean onSiteDiagnostic = false;
    private boolean carInWorkingCondition = false;
    private boolean repairCanBeDoneOnSite = false;
    private boolean carPickUpAndDropOff = false;
    private boolean parkingAvailable = false;

    public Job() {

    }

    public Job (String summary, String description, boolean onSiteDiagnostic, boolean carInWorkingCondition, boolean repairCanBeDoneOnSite, boolean carPickUpAndDropOff) {
        this.summary = summary;
        this.description = description;
        this.onSiteDiagnostic = onSiteDiagnostic;
        this.carInWorkingCondition = carInWorkingCondition;
        this.repairCanBeDoneOnSite = repairCanBeDoneOnSite;
        this.carPickUpAndDropOff = carPickUpAndDropOff;

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
}
