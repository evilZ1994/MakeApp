package com.example.lollipop.makeupapp.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.adapter.MyFragmentPagerAdapter;
import com.example.lollipop.makeupapp.ui.fragment.CommunityFragment;
import com.example.lollipop.makeupapp.ui.fragment.HomeFragment;
import com.example.lollipop.makeupapp.ui.fragment.ScheduleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter fragmentPagerAdapter;
    private MenuItem menuItem;

    private CommunityFragment communityFragment;
    private HomeFragment homeFragment;
    private ScheduleFragment scheduleFragment;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        communityFragment = new CommunityFragment();
        homeFragment = new HomeFragment();
        scheduleFragment = new ScheduleFragment();
        fragmentList.add(communityFragment);
        fragmentList.add(homeFragment);
        fragmentList.add(scheduleFragment);

        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.community:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.schedule:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.me:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null){
                    menuItem.setChecked(false);
                }else {
                    menuItem = bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
