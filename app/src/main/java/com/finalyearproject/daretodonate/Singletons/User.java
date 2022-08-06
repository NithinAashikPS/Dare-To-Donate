package com.finalyearproject.daretodonate.Singletons;

import com.finalyearproject.daretodonate.Interfaces.UserDetailsUpdate;
import com.finalyearproject.daretodonate.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private static User user = null;
    private static UserModel userModel = null;

    private UserDetailsUpdate userDetailsUpdate;

    public static User getInstance() {

        if (user == null) {
            user = new User();
            userModel = new UserModel();
        }
        return user;
    }

    public User setUserDetailsUpdate(UserDetailsUpdate userDetailsUpdate) {
        this.userDetailsUpdate = userDetailsUpdate;
        if (userModel.getName() != null)
            userDetailsUpdate.newData(userModel);
        return this;
    }

    public void setUserData(JSONObject userData) {
        try {
            userModel.setName(userData.getString("name"));
            userModel.setPhone(userData.getString("phone"));
            userModel.setAddress(userData.getJSONObject("address"));
            userModel.setAvailable(userData.getBoolean("available"));
            userModel.setBloodType(userData.getString("bloodType").replace("ve", ""));
            userModel.setGender(userData.getString("gender"));
            userModel.setButtonText(userData.getString("buttonText"));
            userModel.setDonated(userData.getJSONArray("donated"));
            userModel.setRequested(userData.getJSONArray("requested"));
            userModel.setGotBlood(userData.getBoolean("gotBlood"));
            userModel.setLocation(userData.getJSONArray("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (userDetailsUpdate != null)
            userDetailsUpdate.newData(userModel);
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
