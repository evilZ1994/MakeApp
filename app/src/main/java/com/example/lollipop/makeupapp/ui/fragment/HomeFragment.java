package com.example.lollipop.makeupapp.ui.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Post;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.activity.SettingActivity;
import com.example.lollipop.makeupapp.ui.adapter.MyFragmentStatePagerAdapter;
import com.example.lollipop.makeupapp.util.SdCardUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //private AppCompatTextView titleView;

    private User currentUser;
    private List<Fragment> fragments;
    private File headIconFile;

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
    @BindView(R.id.head_img)
    RoundedImageView headImg;
    @BindView(R.id.nickname)
    AppCompatTextView nickNameText;
    @BindView(R.id.location)
    AppCompatTextView locationText;
    @BindView(R.id.signature)
    AppCompatTextView signatureText;
    @BindView(R.id.shared_times)
    AppCompatTextView sharedTimesText;
    @BindView(R.id.thumb_times)
    AppCompatTextView thumbTimesText;
    @BindView(R.id.commented_times)
    AppCompatTextView commentedTimesText;
    @BindView(R.id.collected_times)
    AppCompatTextView collectedTimesText;
    @BindView(R.id.title)
    AppCompatTextView titleView;
    @BindView(R.id.setting)
    AppCompatImageView setting;

    @OnClick(R.id.setting)
    void setting(){
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

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
        titleView = (AppCompatTextView) view.findViewById(R.id.title);

        //绑定滑动监听，改变ToolBar上标题图标等颜色
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark));
                    setting.setImageResource(R.drawable.ic_setting_dark);
                }else {
                    titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    setting.setImageResource(R.drawable.ic_setting_white);
                }
            }
        });

        //初始TabLayout和ViewPager
        fragments = new ArrayList<>();
        ChildFragment childFragment1 = (ChildFragment) ChildFragment.newInstance();
        Bundle bundle1 = new Bundle();
        bundle1.putString("tag", "share");
        childFragment1.setArguments(bundle1);
        fragments.add(childFragment1);
        ChildFragment childFragment2 = (ChildFragment) ChildFragment.newInstance();
        Bundle bundle2 = new Bundle();
        bundle2.putString("tag", "collect");
        childFragment2.setArguments(bundle2);
        fragments.add(childFragment2);
        MessageFragment messageFragment = (MessageFragment) MessageFragment.newInstance();
        fragments.add(messageFragment);

        MyFragmentStatePagerAdapter adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fragments, Arrays.asList(TITLES));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        tabLayout.setupWithViewPager(viewPager);

        initInfoPanel();
    }

    private void initInfoPanel(){
        currentUser = User.getCurrentUser(User.class);
        //设置个人信息面板
        headIconFile = SdCardUtil.getHeadIconFile();
        if (headIconFile.exists()){
            headImg.setImageBitmap(BitmapFactory.decodeFile(headIconFile.getPath()));
        }
        nickNameText.setText(currentUser.getUsername());
        locationText.setText(currentUser.getLocation());
        signatureText.setText(currentUser.getSignature());
        //设置分享、被赞、被评论、被收藏数量
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("author", currentUser);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    //分享次数
                    sharedTimesText.setText(String.valueOf(list.size()));
                    if (list.size() > 0) {
                        int thumbTimes = 0;
                        int commentedTimes = 0;
                        int collectedTimes = 0;
                        for (Post post : list) {
                            thumbTimes += post.getLiked_num();
                            commentedTimes += post.getCommented_num();
                            collectedTimes += post.getCollect_num();
                        }
                        thumbTimesText.setText(String.valueOf(thumbTimes));
                        commentedTimesText.setText(String.valueOf(commentedTimes));
                        collectedTimesText.setText(String.valueOf(collectedTimes));
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initInfoPanel();
    }
}
