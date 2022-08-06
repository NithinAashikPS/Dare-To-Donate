package com.finalyearproject.daretodonate.Interfaces;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

public interface LocationChanged {
    void newLocation(JSONArray location);
}
