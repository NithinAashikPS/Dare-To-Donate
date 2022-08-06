package com.finalyearproject.daretodonate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalyearproject.daretodonate.Models.GridViewModel;
import com.finalyearproject.daretodonate.R;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridViewModel> {

    public GridViewAdapter(@NonNull Context context, ArrayList<GridViewModel> gridViewModels) {
        super(context, 0, gridViewModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item, parent, false);
        }
        TextView name = listitemView.findViewById(R.id.name);
        ImageView image = listitemView.findViewById(R.id.image);

        name.setText(getItem(position).getName());
        image.setImageResource(getItem(position).getImage());
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), getItem(position).getMoveToActivity()));
            }
        });

        return listitemView;
    }
}
