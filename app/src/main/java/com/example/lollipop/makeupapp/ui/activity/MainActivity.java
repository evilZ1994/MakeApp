package com.example.lollipop.makeupapp.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.ui.adapter.MyFragmentPagerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.fragment.CommunityFragment;
import com.example.lollipop.makeupapp.ui.fragment.HomeFragment;
import com.example.lollipop.makeupapp.ui.fragment.ScheduleFragment;
import com.example.lollipop.makeupapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter fragmentPagerAdapter;
    private MenuItem menuItem;

    private CommunityFragment communityFragment;
    private HomeFragment homeFragment;
    private ScheduleFragment scheduleFragment;

    //点击返回键时的时间，用于控制双击退出
    private long mPressedTime = 0;

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
        fragmentList.add(scheduleFragment);
        fragmentList.add(homeFragment);

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

    /**
     * 重写onBackPressed()方法，设置双击退出，时间差500毫秒
     */
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();
        if (mNowTime - mPressedTime > 500){
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            //做一些退出操作
            AppManager.getInstance().AppExit(this);
        }
    }
}
