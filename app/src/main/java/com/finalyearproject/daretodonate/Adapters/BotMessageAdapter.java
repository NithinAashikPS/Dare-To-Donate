package com.finalyearproject.daretodonate.Adapters;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;
import static com.finalyearproject.daretodonate.Models.BotMessageModel.viewType.SEND_MESSAGE;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.daretodonate.Models.BotMessageModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class BotMessageAdapter extends RecyclerView.Adapter<BotMessageAdapter.ViewHolder> {

    private List<BotMessageModel> botMessageModelList;

    public BotMessageAdapter(List<BotMessageModel> botMessageModelList) {
        this.botMessageModelList = botMessageModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0)
            return new ViewHolder(inflater.inflate(R.layout.send_message, parent, false));
        else
            return new ViewHolder(inflater.inflate(R.layout.receive_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.message.setText(HtmlCompat.fromHtml(botMessageModelList.get(position).getMessage(), FROM_HTML_MODE_LEGACY));
        holder.message.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemViewType(int position) {

        if (botMessageModelList.get(position).getViewType() == SEND_MESSAGE)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return botMessageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }
    }
}
