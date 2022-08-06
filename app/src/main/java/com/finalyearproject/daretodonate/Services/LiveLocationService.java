package com.finalyearproject.daretodonate.Services;

import static com.finalyearproject.daretodonate.App.CHANNEL_ID_1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.finalyearproject.daretodonate.Activities.OnBoardingActivity2;
import com.finalyearproject.daretodonate.Activities.SplashActivity;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Interfaces.LocationChanged;
import com.finalyearproject.daretodonate.Interfaces.SocketResponce;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;
import com.finalyearproject.daretodonate.Singletons.LiveLocation;
import com.finalyearproject.daretodonate.Singletons.User;
import com.finalyearproject.daretodonate.Singletons.WebSocketConnection;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;


public class LiveLocationService extends Service implements Listener, LocationData.AddressCallBack {

    private LiveLocation liveLocation;
    private Notification notification;

//    private LocationRequest locationRequest;
    private EasyWayLocation easyWayLocation;
    private NotificationManager notificationManager;
    private GetLocationDetail getLocationDetail;
    private ForegroundStatus foregroundStatus;

    private SharedPreferences sharedPreferences;
    private JSONObject data;
    private User user;

    private WebSocketConnection webSocketConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        liveLocation = LiveLocation.getInstance();
        foregroundStatus = ForegroundStatus.getInstance();
        user = User.getInstance();
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(60000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        easyWayLocation = new EasyWayLocation(this, false,false,this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getLocationDetail = new GetLocationDetail(this, this);
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        data = new JSONObject();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        webSocketConnection = WebSocketConnection.getInstance(sharedPreferences, new SocketResponce() {
            @Override
            public void response(JSONObject data) {
                user.setUserData(data);
            }
        });
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setContentTitle("Dare To Donate")
                .setContentText("Your location is monitoring for blood requests.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getColor(R.color.primary_dark))
                .setContentIntent(pendingIntent)
                .build();

        liveLocation.setLocationUpdater(new LocationChanged() {
            @Override
            public void newLocation(JSONArray location) {

                try {
                    data.put("location", location);
                    data.put("pincode", liveLocation.getPinCode());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webSocketConnection.emit("update", data);
//                notification = new NotificationCompat.Builder(LiveLocationService.this, CHANNEL_ID)
//                        .setContentTitle(String.valueOf(location.getLatitude()))
//                        .setContentText(String.valueOf(location.getLongitude()))
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentIntent(pendingIntent)
//                        .build();
//                notificationManager.notify(1, notification);
            }
        });

        startForeground(1, notification);
        easyWayLocation.startLocation();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        easyWayLocation.endUpdates();
        webSocketConnection.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {

        getLocationDetail.getAddress(location.getLatitude(), location.getLongitude(), getResources().getString(R.string.google_maps_api));
        liveLocation.setLocation(location);
    }

    @Override
    public void locationCancelled() {
    }

    @Override
    public void locationData(LocationData locationData) {
        Log.i("ASDFGH1", locationData.getPincode());
        liveLocation.setPinCode(locationData.getPincode());
    }
}
