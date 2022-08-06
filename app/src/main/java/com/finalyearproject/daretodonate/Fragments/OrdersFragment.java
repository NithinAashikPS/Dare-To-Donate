package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalyearproject.daretodonate.Adapters.DonationRequestAdapter;
import com.finalyearproject.daretodonate.Adapters.MyRequestAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.DonationRequestModel;
import com.finalyearproject.daretodonate.Models.MyRequestModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView myRequestRecyclerview;
    private List<MyRequestModel> myRequestModelList;
    private MyRequestAdapter myRequestAdapter;

    private JSONObject header;
    private JSONObject acceptedUser;
    private JSONObject address;
    private SharedPreferences sharedPreferences;

    private JSONArray myRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        header = new JSONObject();
        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myRequestRecyclerview = root.findViewById(R.id.my_request_recyclerview);
        myRequestModelList = new ArrayList<>();
        myRequestAdapter = new MyRequestAdapter(myRequestModelList);
        myRequestRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        myRequestRecyclerview.setAdapter(myRequestAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new D2DBackend(requireActivity(), new BackendResponseListener() {
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
        }).getRequest(getString(R.string.api_myRequests), new JSONObject(), header);
    }
}