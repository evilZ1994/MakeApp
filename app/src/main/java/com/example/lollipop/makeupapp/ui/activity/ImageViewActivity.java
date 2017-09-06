package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.ui.adapter.ImagePreviewPagerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewActivity extends BaseActivity {
    private ImagePreviewPagerAdapter adapter;
    private List<String> paths;
    private int mPosition;

    private boolean isHide = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_count)
    AppCompatTextView imgCountText;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        paths = intent.getStringArrayListExtra("paths");
        mPosition = intent.getIntExtra("position", 0);

        initView();
    }

    private void initView() {
        toolbar.inflateMenu(R.menu.menu_menu);
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //弹出选项框，保存\分享等
                return false;
            }
        });
        imgCountText.setText((mPosition+1)+"/"+paths.size());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity();
            }
        });

        adapter = new ImagePreviewPagerAdapter(this, paths, ImagePreviewPagerAdapter.TAG_NET);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHide){
                    toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                    isHide = false;
                }else {
                    toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                    isHide = true;
                }
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgCountText.setText((position+1)+"/"+paths.size());
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mPosition);
    }
}
