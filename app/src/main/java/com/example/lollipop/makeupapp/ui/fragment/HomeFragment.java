package com.example.lollipop.makeupapp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.activity.SettingActivity;
import com.example.lollipop.makeupapp.ui.adapter.MyFragmentStatePagerAdapter;
import com.example.lollipop.makeupapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView titleView;
    private MenuItem menuItem;

    private List<Fragment> fragments;

    private static final String[] TITLES = new String[]{"分享", "收藏", "消息"};

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initView(view);
        return view;
    }

    private void initView(View view) {
        titleView = (TextView) view.findViewById(R.id.title);

        toolbar.inflateMenu(R.menu.menu_setting);
        menuItem = toolbar.getMenu().getItem(0);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getContext(), SettingActivity.class));
                return true;
            }
        });
        //绑定滑动监听，改变ToolBar上标题图标等颜色
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark));
                    toolbar.setNavigationIcon(R.drawable.ic_back_dark);
                    menuItem.setIcon(R.drawable.ic_setting_dark);
                }else {
                    titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    toolbar.setNavigationIcon(R.drawable.ic_back_white);
                    menuItem.setIcon(R.drawable.ic_setting_white);
                }
            }
        });

        //初始TabLayout和ViewPager
        fragments = new ArrayList<>();
        fragments.add(new ShareFragment());
        fragments.add(new CollectFragment());
        fragments.add(new MessageFragment());

        MyFragmentStatePagerAdapter adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fragments, Arrays.asList(TITLES));
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.setupWithViewPager(viewPager);

    }

}
