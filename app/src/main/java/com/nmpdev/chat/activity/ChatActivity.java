package com.nmpdev.chat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.nmpdev.chat.adapter.ListMessageAdapter;
import com.nmpdev.chat.databinding.ActivityChatBinding;
import com.nmpdev.chat.model.Message;
import com.nmpdev.chat.model.User;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<Message> messages;
    ListMessageAdapter adapter;
    User sender, receiver;
    String senderUid, receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        receiver = new Gson().fromJson(getIntent().getStringExtra("user"), User.class);

        senderUid = auth.getUid();
        receiverUid = receiver.getUid();

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(receiver.getDisplayName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messages = new ArrayList<>();
        adapter = new ListMessageAdapter(this, messages);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        database.getReference("messages")
                .child(senderUid)
                .child(receiverUid)
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

        binding.sendButton.setOnClickListener(v -> {
            assert binding.textInput.getText() != null;
            String text = binding.textInput.getText().toString();
            String key = database.getReference().push().getKey();
            assert key != null;

            binding.textInput.setText("");

            Message message = new Message(text, receiver);

            database.getReference("messages")
                    .child(senderUid)
                    .child(receiverUid)
                    .child(key)
                    .setValue(message);
            database.getReference("messages")
                    .child(receiverUid)
                    .child(senderUid)
                    .child(key)
                    .setValue(message);

            database.getReference("chats")
                    .child(senderUid)
                    .child(receiverUid)
                    .setValue(message);
            database.getReference("users")
                    .child(senderUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            sender = snapshot.getValue(User.class);

                            message.setUser(sender);

                            database.getReference("chats")
                                    .child(receiverUid)
                                    .child(senderUid)
                                    .setValue(message);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });

        binding.attachButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 25);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 25 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String key = database.getReference().push().getKey();
            assert key != null;
            StorageReference ref = storage.getReference("images").child(new Date().getTime() + "");

            ref.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                Message message = new Message("Just sent a picture", receiver);
                message.setImageUrl(uri.toString());

                database.getReference("messages")
                        .child(senderUid)
                        .child(receiverUid)
                        .child(key)
                        .setValue(message);
                database.getReference("messages")
                        .child(receiverUid)
                        .child(senderUid)
                        .child(key)
                        .setValue(message);

                database.getReference("chats")
                        .child(senderUid)
                        .child(receiverUid)
                        .setValue(message);
                database.getReference("users")
                        .child(senderUid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                sender = snapshot.getValue(User.class);

                                message.setUser(sender);

                                database.getReference("chats")
                                        .child(receiverUid)
                                        .child(senderUid)
                                        .setValue(message);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}