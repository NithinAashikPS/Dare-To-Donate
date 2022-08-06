package com.finalyearproject.daretodonate.Models;

public class BloodGroupModel {

    private String bloodGroup;
    private String bloodUnits;

    public BloodGroupModel() {
    }

    public BloodGroupModel(String bloodGroup, String bloodUnits) {
        this.bloodGroup = bloodGroup;
        this.bloodUnits = bloodUnits;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodUnits() {
        return bloodUnits;
    }

    public void setBloodUnits(String bloodUnits) {
        this.bloodUnits = bloodUnits;
    }
}
