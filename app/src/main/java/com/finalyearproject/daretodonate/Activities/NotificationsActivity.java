package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.finalyearproject.daretodonate.Adapters.DonationRequestAdapter;
import com.finalyearproject.daretodonate.Adapters.NotificationAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.DonationRequestModel;
import com.finalyearproject.daretodonate.Models.NotificationModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView notificationRecyclerview;
    private List<NotificationModel> notificationModels;
    private NotificationAdapter notificationAdapter;

    private JSONObject header;
    private SharedPreferences sharedPreferences;

    private JSONArray data;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        header = new JSONObject();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        notificationRecyclerview = findViewById(R.id.notifications_recyclerview);
        backBtn = findViewById(R.id.back_btn);
        notificationModels = new ArrayList<>();
        notificationRecyclerview.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(notificationModels);
        notificationRecyclerview.setAdapter(notificationAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new D2DBackend(NotificationsActivity.this, new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {
                try {
                    data = response.getJSONArray("data");
                    for (int i=0; i< data.length(); i++) {
                        notificationModels.add(new NotificationModel(data.getJSONObject(i).getString("title"),
                                data.getJSONObject(i).getString("body")));
                    }
                    notificationAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).getRequest(getString(R.string.api_notification), new JSONObject(), header);
    }
}