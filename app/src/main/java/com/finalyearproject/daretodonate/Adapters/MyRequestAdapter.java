package com.finalyearproject.daretodonate.Adapters;

import static com.finalyearproject.daretodonate.Adapters.DonationRequestAdapter.getBloodGroupImage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.MyRequestModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.ViewHolder> {

    private List<MyRequestModel> myRequestModelList;

    public MyRequestAdapter(List<MyRequestModel> myRequestModelList) {
        this.myRequestModelList = myRequestModelList;
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

        holder.name.setText(myRequestModelList.get(position).getName());
        holder.location.setText(myRequestModelList.get(position).getLocation());
        if (myRequestModelList.get(position).getGender().equals("Male"))
            holder.gender.setImageResource(R.drawable.ic_user_male);
        else
            holder.gender.setImageResource(R.drawable.ic_user_female);
        holder.bloodType.setImageResource(getBloodGroupImage(myRequestModelList.get(position).getBloodType()));

    }

    @Override
    public int getItemCount() {
        return myRequestModelList.size();
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
