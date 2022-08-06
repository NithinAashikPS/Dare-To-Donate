package com.finalyearproject.daretodonate.Singletons;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class ForegroundStatus {

    private static ForegroundStatus instance = null;

    private Activity activity;

    public static ForegroundStatus getInstance() {
        if (instance == null)
            instance = new ForegroundStatus();
        return instance;
    }

    public static void setInstance(ForegroundStatus instance) {
        ForegroundStatus.instance = instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isAlive(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
