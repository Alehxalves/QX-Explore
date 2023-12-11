package com.ufc.explorequixada.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ufc.explorequixada.Fragment.FollowingFragment;
import com.ufc.explorequixada.Fragment.FriendListFragment;

public class MyViewPagerAdapter  extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FriendListFragment();
            case 1:
                return new FollowingFragment();
            default:
                return new FriendListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
