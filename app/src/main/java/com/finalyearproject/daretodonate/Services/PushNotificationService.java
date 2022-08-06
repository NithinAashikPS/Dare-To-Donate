package com.finalyearproject.daretodonate.Services;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;
import static com.finalyearproject.daretodonate.App.CHANNEL_ID_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.text.HtmlCompat;

import com.finalyearproject.daretodonate.Activities.MainActivity;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {

    private ForegroundStatus foregroundStatus;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Notification notification;
    private Intent acceptRequestIntent;
    private PendingIntent acceptRequestPendingIntent;
    private NotificationManager notificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        foregroundStatus = ForegroundStatus.getInstance();
        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getString("x-user-token", "").equals("")) {
            JSONObject data = new JSONObject();
            JSONObject header = new JSONObject();
            editor.putString("fcmRegToken", token);
            editor.apply();
            try {
                data.put("fcmRegToken", sharedPreferences.getString("fcmRegToken", ""));
                header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new D2DBackend(foregroundStatus.getActivity(), new BackendResponseListener() {
                @Override
                public void backendResponse(boolean isError, JSONObject response) {

                }
            }).putRequest(getResources().getString(R.string.api_update), data, header);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> data = message.getData();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        acceptRequestIntent = new Intent(this, MainActivity.class);
        acceptRequestIntent.setAction("Notification");
        acceptRequestPendingIntent = PendingIntent.getActivity(this, 0, acceptRequestIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setContentTitle(data.get("title"))
                .setContentText(HtmlCompat.fromHtml(data.get("body"), FROM_HTML_MODE_LEGACY))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(acceptRequestPendingIntent)
                .setColor(getColor(R.color.primary_dark))
                .build();

        notificationManager.notify(2, notification);

    }
}
