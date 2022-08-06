package com.finalyearproject.daretodonate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID_1 = "D2D Live Location Service";
    public static final String CHANNEL_ID_2 = "D2D Blood Request Alert";

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel1 = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_ID_1,
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationChannel serviceChannel2 = new NotificationChannel(
                    CHANNEL_ID_2,
                    CHANNEL_ID_2,
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel1);
            manager.createNotificationChannel(serviceChannel2);
        }
    }
}
