package com.finalyearproject.daretodonate.Models;

public class BloodCampModel {

    private String name;
    private String conductedBy;
    private String phone;
    private String organizedBy;
    private String address;
    private String dataTime;

    public BloodCampModel() {
    }

    public BloodCampModel(String name, String conductedBy, String phone, String organizedBy, String address, String dataTime) {
        this.name = name;
        this.conductedBy = conductedBy;
        this.phone = phone;
        this.organizedBy = organizedBy;
        this.address = address;
        this.dataTime = dataTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConductedBy() {
        return conductedBy;
    }

    public void setConductedBy(String conductedBy) {
        this.conductedBy = conductedBy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganizedBy() {
        return organizedBy;
    }

    public void setOrganizedBy(String organizedBy) {
        this.organizedBy = organizedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
