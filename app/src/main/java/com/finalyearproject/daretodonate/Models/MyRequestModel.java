package com.finalyearproject.daretodonate.Models;

public class MyRequestModel {

    private String gender;
    private String name;
    private String location;
    private String bloodType;

    public MyRequestModel() {
    }

    public MyRequestModel(String gender, String name, String location, String bloodType) {
        this.gender = gender;
        this.name = name;
        this.location = location;
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
