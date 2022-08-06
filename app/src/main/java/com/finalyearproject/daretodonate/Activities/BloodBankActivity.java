package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.finalyearproject.daretodonate.Adapters.BloodBankAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.BloodBankModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BloodBankActivity extends AppCompatActivity {

    private RecyclerView bloodBankRecyclerView;
    private BloodBankAdapter bloodBankAdapter;
    private List<BloodBankModel> bloodBankModelList;

    private SharedPreferences sharedPreferences;
    private JSONObject header;

    private View hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        bloodBankRecyclerView = findViewById(R.id.blood_bank_recycler_view);
        hide = findViewById(R.id.hide);
        bloodBankModelList = new ArrayList<>();
        bloodBankAdapter = new BloodBankAdapter(bloodBankModelList);
        bloodBankRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bloodBankRecyclerView.setAdapter(bloodBankAdapter);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        header = new JSONObject();
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new D2DBackend(BloodBankActivity.this, new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {

                try {
                    JSONArray data = response.getJSONArray("data");
                    if (data.length() != 0) {
                        hide.setVisibility(View.GONE);
                        for (int i = 0; i < data.length(); i++) {
                            bloodBankModelList.add(new BloodBankModel(
                                    data.getJSONObject(i).getString("name"),
                                    data.getJSONObject(i).getString("add"),
                                    data.getJSONObject(i).getString("ph"),
                                    data.getJSONObject(i).getString("email"),
                                    data.getJSONObject(i).getString("lastUpdate"),
                                    data.getJSONObject(i).getString("lat"),
                                    data.getJSONObject(i).getString("long"),
                                    new ArrayList(Arrays.asList(data.getJSONObject(i).getString("available_WithQty").split(",")))
                            ));
                        }
                    } else {
                        hide.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bloodBankAdapter.notifyDataSetChanged();

            }
        }).getRequest(getResources().getString(R.string.api_bloodBank), new JSONObject(), header);

    }
}