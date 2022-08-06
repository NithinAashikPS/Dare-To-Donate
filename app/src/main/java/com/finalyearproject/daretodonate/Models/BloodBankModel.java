package com.finalyearproject.daretodonate.Models;

import java.util.ArrayList;

public class BloodBankModel {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String lastUpdate;
    private String lat;
    private String lon;
    private ArrayList<String> available;

    public BloodBankModel() {
    }

    public BloodBankModel(String name, String address, String phone, String email, String lastUpdate, String lat, String lon, ArrayList<String> available) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.lastUpdate = lastUpdate;
        this.lat = lat;
        this.lon = lon;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public ArrayList<String> getAvailable() {
        return available;
    }

    public void setAvailable(ArrayList<String> available) {
        this.available = available;
    }
}
