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
import com.nmpdev.chat.databinding.ItemRowUserBinding;
import com.nmpdev.chat.model.User;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.UserViewHolder> {

    Context context;
    ArrayList<User> users;

    public ListUserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_row_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        Glide.with(context).load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_profile)
                .into(holder.binding.photo);
        holder.binding.name.setText(user.getDisplayName());

        if (position == users.size() - 1) {
            holder.binding.line.setVisibility(View.GONE);
        } else {
            holder.binding.line.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user", new Gson().toJson(user));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ItemRowUserBinding binding;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRowUserBinding.bind(itemView);
        }
    }
}
