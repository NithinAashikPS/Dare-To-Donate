package com.finalyearproject.daretodonate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalyearproject.daretodonate.Models.AppSpinnerModel;
import com.finalyearproject.daretodonate.R;

import java.util.ArrayList;

public class AppSpinnerAdapter extends ArrayAdapter<AppSpinnerModel> {

    public AppSpinnerAdapter(Context context, ArrayList<AppSpinnerModel> appSpinnerModelArrayList) {
        super(context, 0, appSpinnerModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_spinner, parent, false);

        ImageView imageViewFlag = convertView.findViewById(R.id.spinner_icon_field);
        TextView textViewName = convertView.findViewById(R.id.spinner_text_field);

        if (getItem(position) != null) {
            imageViewFlag.setImageDrawable(getItem(position).getSpinnerIcon());
            textViewName.setText(getItem(position).getSpinnerText());
        }

        return convertView;
    }
}
