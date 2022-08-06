package com.finalyearproject.daretodonate.Models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserModel {

    private String name;
    private String phone;
    private String gender;
    private String bloodType;
    private JSONArray donated;
    private JSONArray requested;
    private boolean available;
    private boolean gotBlood;
    private JSONObject address;
    private JSONArray location;
    private String buttonText;

    public UserModel() {
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public JSONArray getLocation() {
        return location;
    }

    public void setLocation(JSONArray location) {
        this.location = location;
    }

    public boolean isGotBlood() {
        return gotBlood;
    }

    public void setGotBlood(boolean gotBlood) {
        this.gotBlood = gotBlood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public JSONArray getDonated() {
        return donated;
    }

    public void setDonated(JSONArray donated) {
        this.donated = donated;
    }

    public JSONArray getRequested() {
        return requested;
    }

    public void setRequested(JSONArray requested) {
        this.requested = requested;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public JSONObject getAddress() {
        return address;
    }

    public void setAddress(JSONObject address) {
        this.address = address;
    }
}
