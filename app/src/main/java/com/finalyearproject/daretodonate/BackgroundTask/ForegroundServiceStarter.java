package com.finalyearproject.daretodonate.BackgroundTask;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.core.content.ContextCompat;

import com.finalyearproject.daretodonate.Interfaces.ServiceStatus;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Services.LiveLocationService;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;

public class ForegroundServiceStarter extends AsyncTask<Void, Void, Void> {

    private Intent liveLocationServiceIntent;
    private ForegroundStatus foregroundStatus;
    private ServiceStatus serviceStatus;
    private SharedPreferences sharedPreferences;
    private boolean available;

    public ForegroundServiceStarter(Activity activity, ServiceStatus serviceStatus) {
        this.foregroundStatus = ForegroundStatus.getInstance();
        this.foregroundStatus.setActivity(activity);
        this.serviceStatus = serviceStatus;
        this.sharedPreferences = activity.getSharedPreferences(activity.getResources().getString(R.string.app_name), MODE_PRIVATE);
        this.available = sharedPreferences.getBoolean("available", true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            if (!foregroundStatus.isAlive(LiveLocationService.class) && available) {
                liveLocationServiceIntent = new Intent(foregroundStatus.getActivity(), LiveLocationService.class);
                ContextCompat.startForegroundService(foregroundStatus.getActivity(), liveLocationServiceIntent);
            } else {
                SystemClock.sleep(2000);
                serviceStatus.started();
                break;
            }
            SystemClock.sleep(1000);
        }
        return null;
    }

}
