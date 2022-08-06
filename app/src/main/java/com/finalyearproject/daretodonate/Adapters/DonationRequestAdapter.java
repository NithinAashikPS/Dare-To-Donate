package com.finalyearproject.daretodonate.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Interfaces.ClickOnBottomSheet;
import com.finalyearproject.daretodonate.Models.AppSpinnerModel;
import com.finalyearproject.daretodonate.Models.DonationRequestModel;
import com.finalyearproject.daretodonate.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DonationRequestAdapter extends RecyclerView.Adapter<DonationRequestAdapter.ViewHolder> {

    private List<DonationRequestModel> donationRequestModelList;
    private ClickOnBottomSheet clickOnBottomSheet;
    private JSONObject address;

    public DonationRequestAdapter(List<DonationRequestModel> donationRequestModelList, ClickOnBottomSheet clickOnBottomSheet) {
        this.donationRequestModelList = donationRequestModelList;
        this.clickOnBottomSheet = clickOnBottomSheet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.donation_request_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.name.setText(donationRequestModelList.get(position).getUser().getString("name"));
            address = donationRequestModelList.get(position).getUser().getJSONObject("address");
            holder.location.setText(String.format("%s, %s", address.getString("district"), address.getString("state")));
            if (donationRequestModelList.get(position).getUser().getString("gender").equals("Male"))
                holder.gender.setImageResource(R.drawable.ic_user_male);
            else
                holder.gender.setImageResource(R.drawable.ic_user_female);
            holder.bloodType.setImageResource(getBloodGroupImage(donationRequestModelList.get(position).getUser().getString("bloodType")));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickOnBottomSheet.onClick(donationRequestModelList.get(position).getUser(), donationRequestModelList.get(position).get_id());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static int getBloodGroupImage(String bloodType) {

        switch (bloodType) {
            case "A+ve":
                return R.drawable.ic_blood_a_p;
            case "A-ve":
                return R.drawable.ic_blood_a_n;
            case "B+ve":
                return R.drawable.ic_blood_b_p;
            case "B-ve":
                return R.drawable.ic_blood_b_n;
            case "O+ve":
                return R.drawable.ic_blood_o_p;
            case "O-ve":
                return R.drawable.ic_blood_o_n;
            case "AB+ve":
                return R.drawable.ic_blood_ab_p;
            case "AB-ve":
                return R.drawable.ic_blood_ab_n;
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return donationRequestModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView gender;
        private TextView name;
        private TextView location;
        private ImageView bloodType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gender = itemView.findViewById(R.id.gender);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            bloodType = itemView.findViewById(R.id.blood_type);
        }
    }
}
