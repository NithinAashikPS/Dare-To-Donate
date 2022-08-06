package com.finalyearproject.daretodonate.Models;

import org.json.JSONArray;
import org.json.JSONObject;

public class DonationRequestModel {

    private String _id;
    private JSONObject user;
    private boolean gotBlood;
    private JSONArray location;
    private JSONArray receiveFrom;

    public DonationRequestModel(String _id, JSONObject user, boolean gotBlood, JSONArray location, JSONArray receiveFrom) {
        this._id = _id;
        this.user = user;
        this.gotBlood = gotBlood;
        this.location = location;
        this.receiveFrom = receiveFrom;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public JSONObject getUser() {
        return user;
    }

    public void setUser(JSONObject user) {
        this.user = user;
    }

    public boolean isGotBlood() {
        return gotBlood;
    }

    public void setGotBlood(boolean gotBlood) {
        this.gotBlood = gotBlood;
    }

    public JSONArray getLocation() {
        return location;
    }

    public void setLocation(JSONArray location) {
        this.location = location;
    }

    public JSONArray getReceiveFrom() {
        return receiveFrom;
    }

    public void setReceiveFrom(JSONArray receiveFrom) {
        this.receiveFrom = receiveFrom;
    }
}
