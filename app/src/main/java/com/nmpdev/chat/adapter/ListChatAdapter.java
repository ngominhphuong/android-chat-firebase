package com.nmpdev.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nmpdev.chat.R;
import com.nmpdev.chat.activity.ChatActivity;
import com.nmpdev.chat.databinding.ItemRowChatBinding;
import com.nmpdev.chat.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.ChatViewHolder> {

    Context context;
    ArrayList<Message> messages;

    public ListChatAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_row_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Glide.with(context).load(message.getUser().getPhotoUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.binding.photo);
        holder.binding.name.setText(message.getUser().getDisplayName());
        holder.binding.text.setText(message.getText());
        holder.binding.time.setText(dateFormat.format(message.getTimestamp()));

        if (position == messages.size() - 1) {
            holder.binding.line.setVisibility(View.GONE);
        } else {
            holder.binding.line.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user", new Gson().toJson(message.getUser()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        ItemRowChatBinding binding;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRowChatBinding.bind(itemView);
        }
    }
}
