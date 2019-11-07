package me.muhammadfaisal.mycarta.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> viewPagerAdapters = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return viewPagerAdapters.get(position);
    }

    @Override
    public int getCount() {
        return viewPagerAdapters.size();
    }

    public void addFragment(Fragment fragment){
        viewPagerAdapters.add(fragment);
    }
}
