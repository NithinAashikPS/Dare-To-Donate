package com.finalyearproject.daretodonate.Adapters;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.NotificationModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notificationModels;

    public NotificationAdapter(List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tittle.setText(HtmlCompat.fromHtml(notificationModels.get(position).getTitle(), FROM_HTML_MODE_LEGACY));
        holder.body.setText(HtmlCompat.fromHtml(notificationModels.get(position).getBody(), FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tittle;
        private TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tittle = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
        }
    }
}
