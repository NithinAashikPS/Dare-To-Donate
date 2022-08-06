package com.finalyearproject.daretodonate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.finalyearproject.daretodonate.BackgroundTask.ForegroundServiceStarter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Interfaces.ServiceStatus;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;
import com.finalyearproject.daretodonate.Singletons.User;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DToast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    public final String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE};
    private ForegroundServiceStarter foregroundServiceStarter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ForegroundStatus foregroundStatus;

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private LocationManager manager;
    private JSONObject header;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        foregroundStatus = ForegroundStatus.getInstance();
        foregroundStatus.setActivity(SplashActivity.this);
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        header = new JSONObject();
        user = User.getInstance();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            editor.putString("fcmRegToken", task.getResult());
                            editor.apply();
                            if (!sharedPreferences.getString("x-user-token", "").equals("")) {
                                JSONObject data = new JSONObject();
                                JSONObject header = new JSONObject();
                                try {
                                    data.put("fcmRegToken", sharedPreferences.getString("fcmRegToken", ""));
                                    data.put("available", sharedPreferences.getBoolean("available", true));
                                    header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new D2DBackend(SplashActivity.this, new BackendResponseListener() {
                                    @Override
                                    public void backendResponse(boolean isError, JSONObject response) {

                                    }
                                }).putRequest(getResources().getString(R.string.api_update), data, header);
                            }
                        }
                    }
                });
        foregroundServiceStarter = new ForegroundServiceStarter(SplashActivity.this, new ServiceStatus() {
            @Override
            public void started() {
                if (sharedPreferences.getBoolean("onBoard", false)) {
                    if (sharedPreferences.getString("x-user-token", "").equals("")) {
                        startActivity(new Intent(SplashActivity.this, OnBoardingActivity2.class));
                    } else {

                        try {
                            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new D2DBackend(SplashActivity.this, new BackendResponseListener() {
                            @Override
                            public void backendResponse(boolean isError, JSONObject response) {

                                if (!isError) {
                                    user.setUserData(response);
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(SplashActivity.this, OnBoardingActivity2.class));
                                    finish();
                                }

                            }
                        }).getRequest(getResources().getString(R.string.api_user), new JSONObject(), header);
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, OnBoardingActivity1.class));
                    finish();
                }
            }
        });
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SplashActivity.this)) {
            enableLoc();
        } else {
            startForeground();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    startForeground();
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    break;
            }
        }
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);


            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(SplashActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException ignored) {
                        }
                    }
                }
            });
        }

    }

    private void startForeground() {

        if (checkPermission()) {
            try {
                foregroundServiceStarter.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, permission, 100);
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == 0) {
            startForeground();
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
}