package com.example.lollipop.makeupapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lollipop.makeupapp.ui.fragment.ScheduleClassificationFragment;

import java.util.List;

/**
 * Created by R2D2 on 2017/9/8.
 */

public class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {
    List<ScheduleClassificationFragment> fragments;

    public ScheduleFragmentPagerAdapter(FragmentManager fm, List<ScheduleClassificationFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
