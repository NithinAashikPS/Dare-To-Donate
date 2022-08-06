package com.finalyearproject.daretodonate.Receivers;

import static android.content.Context.MODE_PRIVATE;
import static com.finalyearproject.daretodonate.App.CHANNEL_ID_2;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.finalyearproject.daretodonate.Activities.MainActivity;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DToast;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAlertReceiver extends BroadcastReceiver {

    private JSONObject header;
    private JSONObject acceptData;
    private SharedPreferences sharedPreferences;
    private ForegroundStatus foregroundStatus;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("HGJHG",intent.getExtras().getString("PATIENT_ID"));
        header = new JSONObject();
        acceptData = new JSONObject();
        foregroundStatus = ForegroundStatus.getInstance();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (intent.getAction()) {
            case "accept":
                try {
                    acceptData.put("patient", intent.getExtras().getString("PATIENT_ID"));
                    new D2DBackend(foregroundStatus.getActivity(), new BackendResponseListener() {
                        @Override
                        public void backendResponse(boolean isError, JSONObject response) {
                            if (!isError) {
                                Intent home = new Intent(context, MainActivity.class);
                                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(home);
                            } else {
                                try {
                                    new D2DToast().makeText((Activity) context, response.getString("error"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).putRequest(context.getString(R.string.api_acceptRequest), acceptData, header);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "reject":
                break;
        }
    }
}
