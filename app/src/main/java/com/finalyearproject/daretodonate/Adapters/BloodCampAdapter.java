package com.finalyearproject.daretodonate.Adapters;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.BloodCampModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class BloodCampAdapter extends RecyclerView.Adapter<BloodCampAdapter.ViewHolder> {

    private List<BloodCampModel> bloodCampModelList;

    public BloodCampAdapter(List<BloodCampModel> bloodCampModelList) {
        this.bloodCampModelList = bloodCampModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.blood_camp_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(bloodCampModelList.get(position).getName());
        holder.conductedBy.setText(HtmlCompat.fromHtml(String.format("<b>Conducted By :</b> %s", bloodCampModelList.get(position).getConductedBy()), FROM_HTML_MODE_LEGACY));
        holder.organizedBy.setText(HtmlCompat.fromHtml(String.format("<b>Organised By :</b> %s", bloodCampModelList.get(position).getOrganizedBy()), FROM_HTML_MODE_LEGACY));
        holder.address.setText(HtmlCompat.fromHtml(String.format("<b>Address :</b> %s", bloodCampModelList.get(position).getAddress()), FROM_HTML_MODE_LEGACY));
        holder.dataTime.setText(HtmlCompat.fromHtml(String.format("<b>Data | Time :</b> %s", bloodCampModelList.get(position).getDataTime()), FROM_HTML_MODE_LEGACY));

        holder.hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.hidden) {
                    holder.hideButton.animate().rotation(-180f).setDuration(350).start();
                    holder.hide.setVisibility(View.VISIBLE);
                    holder.dataTime.setVisibility(View.VISIBLE);
                    holder.name.setMaxLines(5);
                    holder.name.setEllipsize(null);
                } else {
                    holder.hideButton.animate().rotation(-0f).setDuration(350).start();
                    holder.hide.setVisibility(View.GONE);
                    holder.dataTime.setVisibility(View.GONE);
                    holder.name.setMaxLines(1);
                    holder.name.setEllipsize(TextUtils.TruncateAt.END);
                }
                holder.hidden = !holder.hidden;
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bloodCampModelList.get(position).getPhone()));
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = String.format("Name : %s\n\nPhone : %s\n\nConducted By : %s\n\nOrganised By : %s\n\nAddress : %s\n\nDate | Time : %s\n\n",
                        bloodCampModelList.get(position).getName(),
                        bloodCampModelList.get(position).getPhone(),
                        bloodCampModelList.get(position).getConductedBy(),
                        bloodCampModelList.get(position).getOrganizedBy(),
                        bloodCampModelList.get(position).getAddress(),
                        bloodCampModelList.get(position).getDataTime()
                );
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                holder.itemView.getContext().startActivity(shareIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodCampModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView conductedBy;
        private TextView organizedBy;
        private TextView address;
        private TextView dataTime;

        private LinearLayout hide;
        private ImageView hideButton;
        private Boolean hidden = true;

        private Button call;
        private Button share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.camp_name);
            conductedBy = itemView.findViewById(R.id.conducted_by);
            organizedBy = itemView.findViewById(R.id.organized_by);
            address = itemView.findViewById(R.id.address);
            dataTime = itemView.findViewById(R.id.date_time);
            hideButton = itemView.findViewById(R.id.hide_btn);
            hide = itemView.findViewById(R.id.hide);
            call = itemView.findViewById(R.id.call);
            share = itemView.findViewById(R.id.share);

        }
    }
}
