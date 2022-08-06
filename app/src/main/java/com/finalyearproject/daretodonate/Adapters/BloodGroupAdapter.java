package com.finalyearproject.daretodonate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.BloodGroupModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class BloodGroupAdapter extends RecyclerView.Adapter<BloodGroupAdapter.ViewHolder> {

    private List<BloodGroupModel> bloodGroupModelList;

    public BloodGroupAdapter(List<BloodGroupModel> bloodGroupModelList) {
        this.bloodGroupModelList = bloodGroupModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.blood_group_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bloodGroup.setText(bloodGroupModelList.get(position).getBloodGroup());
        holder.bloodUnit.setText(String.format("%s Units", bloodGroupModelList.get(position).getBloodUnits()));
    }

    @Override
    public int getItemCount() {
        return bloodGroupModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bloodGroup;
        private TextView bloodUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroup = itemView.findViewById(R.id.blood_group);
            bloodUnit = itemView.findViewById(R.id.blood_unit);
        }
    }
}
