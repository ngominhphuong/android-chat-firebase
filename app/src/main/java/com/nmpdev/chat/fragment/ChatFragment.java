package com.nmpdev.chat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nmpdev.chat.adapter.ListChatAdapter;
import com.nmpdev.chat.databinding.FragmentChatBinding;
import com.nmpdev.chat.model.Message;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Message> messages;
    ListChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        messages = new ArrayList<>();
        adapter = new ListChatAdapter(getContext(), messages);
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.setAdapter(adapter);

        assert auth.getUid() != null;
        database.getReference("chats")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Message message = ds.getValue(Message.class);
                            messages.add(message);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return binding.getRoot();
    }
}
