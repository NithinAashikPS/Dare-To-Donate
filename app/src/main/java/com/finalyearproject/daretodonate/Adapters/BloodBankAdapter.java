package com.finalyearproject.daretodonate.Adapters;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.BloodBankModel;
import com.finalyearproject.daretodonate.Models.BloodGroupModel;
import com.finalyearproject.daretodonate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.ViewHolder> {

    private List<BloodBankModel> bloodBankModelList;
    private List<BloodGroupModel> bloodGroupModelList;
    private BloodGroupAdapter bloodGroupAdapter;

    private Intent intent;

    public BloodBankAdapter(List<BloodBankModel> bloodBankModelList) {
        this.bloodBankModelList = bloodBankModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.blood_bank_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ArrayList<String> available;
        String uri;
        bloodGroupModelList = new ArrayList<>();
        bloodGroupAdapter = new BloodGroupAdapter(bloodGroupModelList);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(bloodGroupAdapter);
        holder.bloodBankName.setText(bloodBankModelList.get(position).getName());
        holder.address.setText(HtmlCompat.fromHtml(String.format("<b>Address :</b> %s", bloodBankModelList.get(position).getAddress()), FROM_HTML_MODE_LEGACY));
        holder.email.setText(HtmlCompat.fromHtml(String.format("<b>Email :</b> %s", !bloodBankModelList.get(position).getEmail().equals("-") ? bloodBankModelList.get(position).getEmail() : "Not Available"), FROM_HTML_MODE_LEGACY));
        holder.lastUpdate.setText(String.format("Last Updated : %s", bloodBankModelList.get(position).getLastUpdate()));
        available = bloodBankModelList.get(position).getAvailable();
        for (int i=0; i<available.size(); i++) {
            bloodGroupModelList.add(new BloodGroupModel(available.get(i).split(":")[0], available.get(i).split(":")[1]));
        }
        bloodGroupAdapter.notifyDataSetChanged();

        holder.hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.hidden) {
                    holder.hideButton.animate().rotation(-180f).setDuration(350).start();
                    holder.hide.setVisibility(View.VISIBLE);
                    holder.lastUpdate.setVisibility(View.VISIBLE);
                    holder.bloodBankName.setMaxLines(5);
                    holder.bloodBankName.setEllipsize(null);
                } else {
                    holder.hideButton.animate().rotation(-0f).setDuration(350).start();
                    holder.hide.setVisibility(View.GONE);
                    holder.lastUpdate.setVisibility(View.GONE);
                    holder.bloodBankName.setMaxLines(1);
                    holder.bloodBankName.setEllipsize(TextUtils.TruncateAt.END);
                }
                holder.hidden = !holder.hidden;
            }
        });
        uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s,%s", bloodBankModelList.get(position).getLat(), bloodBankModelList.get(position).getLon());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bloodBankModelList.get(position).getPhone()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    holder.itemView.getContext().startActivity(intent);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder blood = new StringBuilder();
                for (int i=0; i<available.size(); i++) {
                    Log.i("ASDFGJN", available.get(i));
                    blood.append(String.format("\n%s", available.get(i)));
                }
                String message = String.format("Name : %s\n\nPhone : %s\n\nEmail : %s\n\nAddress : %s\n\nDirections : %s\n\nStock Available : %s\n",
                        bloodBankModelList.get(position).getName(),
                        bloodBankModelList.get(position).getPhone(),
                        bloodBankModelList.get(position).getEmail(),
                        bloodBankModelList.get(position).getAddress(),
                        uri,
                        blood);
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
        return bloodBankModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private TextView bloodBankName;
        private TextView address;
        private TextView email;
        private TextView lastUpdate;

        private LinearLayout hide;
        private ImageView hideButton;
        private Boolean hidden = true;

        private Button call;
        private Button direction;
        private Button share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.blood_group_recycler_view);
            bloodBankName = itemView.findViewById(R.id.blood_bank_name);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.email);
            lastUpdate = itemView.findViewById(R.id.last_updated);
            hideButton = itemView.findViewById(R.id.hide_btn);
            hide = itemView.findViewById(R.id.hide);
            call = itemView.findViewById(R.id.call);
            direction = itemView.findViewById(R.id.direction);
            share = itemView.findViewById(R.id.share);
        }
    }
}
