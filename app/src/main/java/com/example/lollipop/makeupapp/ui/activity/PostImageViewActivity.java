package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.ui.adapter.ImagePreviewPagerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.dialog.IsDeleteDialog;
import com.example.lollipop.makeupapp.util.Codes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostImageViewActivity extends BaseActivity {
    private ImagePreviewPagerAdapter adapter;
    private List<String> paths;
    private List<String> deletePaths;
    private IsDeleteDialog dialog;
    private int mPosition;
    private int num;

    private Intent resultIntent;

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
        setContentView(R.layout.activity_post_image_view);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        paths = intent.getStringArrayListExtra("paths");
        if (paths.get(paths.size()-1) == null) {
            paths.remove(paths.size() - 1);//最后一个空白的移除
        }
        mPosition = intent.getIntExtra("position", 0);
        num = mPosition+1;

        resultIntent = new Intent();

        initView();
    }

    private void initView() {
        deletePaths = new ArrayList<>();
        dialog = new IsDeleteDialog(this, R.style.InfoChangeDialogTheme, new IsDeleteDialog.OnDeleteListener() {
            @Override
            public void onDelete() {
                //删除操作
                String path = adapter.deleteItem(mPosition);
                deletePaths.add(path);
                imgCountText.setText((viewPager.getCurrentItem()+1)+"/"+adapter.getCount());
                //回传删除数据
                resultIntent.putStringArrayListExtra("deletePaths", (ArrayList<String>) deletePaths);
                setResult(Codes.POST_IMAGE_PREVIEW_RESULT_CODE, resultIntent);
                //如果删除最后一张，则直接返回
                if (paths.size() == 0){
                    AppManager.getInstance().finishActivity();
                }
                dialog.dismiss();
            }
        });

        toolbar.inflateMenu(R.menu.menu_delete);
        imgCountText.setText(num+"/"+paths.size());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManager.getInstance().finishActivity();
            }
        });
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                dialog.show();
                return false;
            }
        });

        adapter = new ImagePreviewPagerAdapter(this, paths);
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
                num = position+1;
                imgCountText.setText(num+"/"+paths.size());
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
