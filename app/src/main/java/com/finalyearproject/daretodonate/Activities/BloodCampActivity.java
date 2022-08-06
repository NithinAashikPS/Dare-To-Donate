package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.finalyearproject.daretodonate.Adapters.BloodCampAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.BloodCampModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BloodCampActivity extends AppCompatActivity {

    private RecyclerView bloodCampRecyclerView;
    private BloodCampAdapter bloodCampAdapter;
    private List<BloodCampModel> bloodCampModelList;

    private SharedPreferences sharedPreferences;
    private JSONObject header;

    private View hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_camp);

        bloodCampRecyclerView = findViewById(R.id.blood_camp_recycler_view);
        hide = findViewById(R.id.hide);
        bloodCampModelList = new ArrayList<>();
        bloodCampAdapter = new BloodCampAdapter(bloodCampModelList);
        bloodCampRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bloodCampRecyclerView.setAdapter(bloodCampAdapter);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        header = new JSONObject();
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new D2DBackend(BloodCampActivity.this, new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");
                    if (data.length() != 0) {
                        for (int i = 0; i < data.length(); i++) {
                            bloodCampModelList.add(new BloodCampModel(
                                    data.getJSONObject(i).getString("name"),
                                    data.getJSONObject(i).getString("conducted_by"),
                                    data.getJSONObject(i).getString("ph"),
                                    data.getJSONObject(i).getString("organized_by"),
                                    data.getJSONObject(i).getString("venue"),
                                    data.getJSONObject(i).getString("dt") + " | " + data.getJSONObject(i).getString("time")
                            ));
                        }
                    } else {
                        hide.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bloodCampAdapter.notifyDataSetChanged();
                Log.i("AAAAAAAA", String.valueOf(isError));
            }
        }).getRequest(getResources().getString(R.string.api_bloodCamp), new JSONObject(), header);
    }
}