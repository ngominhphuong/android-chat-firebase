package com.nmpdev.chat.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nmpdev.chat.databinding.ActivitySignUpBinding;
import com.nmpdev.chat.model.User;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assert getSupportActionBar() != null;
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.nameInputWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.emailInputWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.passwordInputWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.confirmPasswordInputWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.signUpButton.setOnClickListener(v -> {
            assert  binding.nameInput.getText() != null;
            assert  binding.emailInput.getText() != null;
            assert  binding.passwordInput.getText() != null;
            assert  binding.confirmPasswordInput.getText() != null;

            String name = binding.nameInput.getText().toString();
            String email = binding.emailInput.getText().toString();
            String password = binding.passwordInput.getText().toString();
            String confirmPassword = binding.confirmPasswordInput.getText().toString();

            if (TextUtils.isEmpty(name)) {
                binding.nameInputWrapper.setError("Enter your name");
            } else if (TextUtils.isEmpty(email)) {
                binding.emailInputWrapper.setError("Enter your email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInputWrapper.setError("Email is invalid");
            } else if (TextUtils.isEmpty(password)) {
                binding.passwordInputWrapper.setError("Enter your password");
            } else if (password.length() < 6) {
                binding.passwordInputWrapper.setError("Password must have at least 6 characters");
            } else if (!password.equals(confirmPassword)) {
                binding.confirmPasswordInputWrapper.setError("Password does not match");
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getUid();
                        assert uid != null;
                        StorageReference ref = storage.getReference("photos").child(uid);
                        if (selectedImage != null) {
                            ref.putFile(selectedImage).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        User user = new User(uid, email, name, uri.toString());
                                        database.getReference("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(this, MainActivity.class));
                                                    finish();
                                                });
                                    }));
                        } else {
                            User user = new User(uid, email, name, "");
                            database.getReference("users")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.backButton.setOnClickListener(v -> finish());

        binding.photo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 17);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            binding.photo.setImageURI(selectedImage);
        }
    }
}