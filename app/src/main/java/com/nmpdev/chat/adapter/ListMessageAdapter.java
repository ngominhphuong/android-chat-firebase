package com.nmpdev.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.nmpdev.chat.R;
import com.nmpdev.chat.databinding.ItemRowMessageReceivedBinding;
import com.nmpdev.chat.databinding.ItemRowMessageSentBinding;
import com.nmpdev.chat.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVED = 2;

    public ListMessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String uid = FirebaseAuth.getInstance().getUid();
        if (!message.getUser().getUid().equals(uid)) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_row_message_sent, parent, false);
            return new SentViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_row_message_received, parent, false);
            return new ReceivedViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.text.setText(message.getText());
            viewHolder.binding.time.setText(dateFormat.format(message.getTimestamp()));

            if (message.getText().equals("Just sent a picture") && message.getImageUrl() != null) {
                Glide.with(context).load(message.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder)
                        .into(viewHolder.binding.image);
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.text.setVisibility(View.GONE);
            }
        } else {
            ReceivedViewHolder viewHolder = (ReceivedViewHolder) holder;
            viewHolder.binding.text.setText(message.getText());
            viewHolder.binding.time.setText(dateFormat.format(message.getTimestamp()));

            if (message.getText().equals("Just sent a picture") && message.getImageUrl() != null) {
                Glide.with(context).load(message.getImageUrl())
                        .placeholder(R.drawable.ic_placeholder)
                        .into(viewHolder.binding.image);
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.text.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {

        ItemRowMessageSentBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRowMessageSentBinding.bind(itemView);
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {

        ItemRowMessageReceivedBinding binding;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRowMessageReceivedBinding.bind(itemView);
        }
    }
}
