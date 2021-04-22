package com.nmpdev.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.nmpdev.chat.R;
import com.nmpdev.chat.adapter.ViewPagerAdapter;
import com.nmpdev.chat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        adapter = new ViewPagerAdapter(this);
        binding.pager.setAdapter(adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(binding.tab, binding.pager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("CHAT");
                    tab.setIcon(R.drawable.ic_chat);
                    break;
                case 1:
                    tab.setText("GROUP");
                    tab.setIcon(R.drawable.ic_group);
                    break;
                case 2:
                    tab.setText("PEOPLE");
                    tab.setIcon(R.drawable.ic_people);
                    break;
            }
        });
        mediator.attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}