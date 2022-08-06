package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.finalyearproject.daretodonate.Adapters.MyRequestAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.MyRequestModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyDonationActivity extends AppCompatActivity {

    private RecyclerView myRequestRecyclerview;
    private List<MyRequestModel> myRequestModelList;
    private MyRequestAdapter myRequestAdapter;

    private JSONObject header;
    private JSONObject acceptedUser;
    private JSONObject address;
    private SharedPreferences sharedPreferences;

    private JSONArray myRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation);

        header = new JSONObject();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myRequestRecyclerview = findViewById(R.id.my_request_recyclerview);
        myRequestModelList = new ArrayList<>();
        myRequestAdapter = new MyRequestAdapter(myRequestModelList);
        myRequestRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRequestRecyclerview.setAdapter(myRequestAdapter);


        new D2DBackend(MyDonationActivity.this, new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {
                try {
                    myRequests = response.getJSONArray("myRequests");
                    for (int i=0 ;i<myRequests.length(); i++) {
                        acceptedUser = myRequests.getJSONObject(i).getJSONArray("acceptedBy").getJSONObject(0);
                        address = acceptedUser.getJSONObject("address");
                        myRequestModelList.add(new MyRequestModel(
                                acceptedUser.getString("gender"),
                                acceptedUser.getString("name"),
                                String.format("%s, %s", address.getString("district"), address.getString("state")),
                                acceptedUser.getString("bloodType")
                        ));
                    }
                    myRequestAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).getRequest(getString(R.string.api_myDonations), new JSONObject(), header);

    }
}