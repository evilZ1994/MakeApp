package com.example.lollipop.makeupapp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.activity.ScheduleAddActivity;
import com.example.lollipop.makeupapp.ui.adapter.ClassificationInScheduleRecyclerAdapter;
import com.example.lollipop.makeupapp.ui.adapter.ClassificationRecycleAdapter;
import com.example.lollipop.makeupapp.ui.adapter.ScheduleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment implements ScheduleClassificationFragment.OnItemNumChangeListener{

    private User currentUser;
    private ClassificationInScheduleRecyclerAdapter classificationAdapter;
    private ScheduleFragmentPagerAdapter fragmentAdapter;
    private List<Integer> numbers;
    private List<Integer> images;
    private List<String> titles;
    private List<ScheduleClassificationFragment> fragments;

    private int[] imgResources = {
            R.drawable.ic_bicycle, R.drawable.ic_mask,
            R.drawable.ic_eating, R.drawable.ic_healthcare,
            R.drawable.ic_cosmetic, R.drawable.ic_menu
    };
    private String[] classificationTexts = {
            "健身", "护肤", "饮食", "保健品", "彩妆", "其他"
    };

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tag_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, view);
        currentUser = User.getCurrentUser(User.class);
        initView();

        return view;
    }

    private void initView() {
        images = new ArrayList<>();
        titles = new ArrayList<>();
        numbers = new ArrayList<>();
        for (int i = 0; i < classificationTexts.length; i++){
            images.add(imgResources[i]);
            titles.add(classificationTexts[i]);
            numbers.add(i);
        }
        classificationAdapter = new ClassificationInScheduleRecyclerAdapter(getContext(), images, titles, numbers);
        classificationAdapter.setOnItemClickListener(new ClassificationRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position, true);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(classificationAdapter);

        fragments = new ArrayList<>();
        for (int i=0; i<classificationTexts.length; i++){
            ScheduleClassificationFragment fragment = ScheduleClassificationFragment.newInstance(classificationTexts[i], i);
            fragment.setOnItemNumChangeListener(this);
            fragments.add(fragment);
        }
        fragmentAdapter = new ScheduleFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                classificationAdapter.changeState(classificationAdapter.getViewHolder(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onItemNumChange(int position, int num) {
        //修改显示的数字
    }
}
