package com.nmpdev.chat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nmpdev.chat.fragment.ChatFragment;
import com.nmpdev.chat.fragment.GroupFragment;
import com.nmpdev.chat.fragment.PeopleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new GroupFragment();
            default:
                return new PeopleFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
