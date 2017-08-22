package com.example.lollipop.makeupapp.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.activity.PostActivity;
import com.example.lollipop.makeupapp.ui.adapter.ClassificationRecycleAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseFragment;
import com.example.lollipop.makeupapp.util.Codes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends BaseFragment {
    private User currentUser;
    private List<Integer> images;
    private List<String> titles;
    private ClassificationRecycleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private PopupWindow popupWindow;

    private Context context;
    private Activity activity;

    private int[] imgResources = {
            R.drawable.ic_bicycle, R.drawable.ic_mask,
            R.drawable.ic_eating, R.drawable.ic_healthcare,
            R.drawable.ic_cosmetic, R.drawable.ic_menu
    };
    private String[] titleTexts = {
            "健身", "护肤", "饮食", "保健品", "彩妆", "其他"
    };

    @BindView(R.id.title)
    AppCompatTextView titleText;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @OnClick(R.id.post)
    void post(){
        showPopupWindow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        context = getContext();
        activity = getActivity();
        currentUser = User.getCurrentUser(User.class);
        images = new ArrayList<>();
        titles = new ArrayList<>();
        for (int i=0; i<titleTexts.length; i++){
            images.add(imgResources[i]);
            titles.add(titleTexts[i]);
        }
        adapter = new ClassificationRecycleAdapter(getContext(), images, titles);
        adapter.setOnItemClickListener(new ClassificationListener());
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        titleText.setText(currentUser.getUsername());
    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_window_classification, null);

        AppCompatImageView item1 = (AppCompatImageView) contentView.findViewById(R.id.item1);
        AppCompatImageView item2 = (AppCompatImageView) contentView.findViewById(R.id.item2);
        AppCompatImageView item3 = (AppCompatImageView) contentView.findViewById(R.id.item3);
        AppCompatImageView item4 = (AppCompatImageView) contentView.findViewById(R.id.item4);
        AppCompatImageView item5 = (AppCompatImageView) contentView.findViewById(R.id.item5);
        AppCompatImageView item6 = (AppCompatImageView) contentView.findViewById(R.id.item6);
        AppCompatImageView cancel = (AppCompatImageView) contentView.findViewById(R.id.cancel);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        //点击空白处隐藏
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //进入和退出动画
        popupWindow.setAnimationStyle(R.style.popupWindowAnim);
        //绑定监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复透明度
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
        PostClassificationListener listener = new PostClassificationListener();
        item1.setOnClickListener(listener);
        item2.setOnClickListener(listener);
        item3.setOnClickListener(listener);
        item4.setOnClickListener(listener);
        item5.setOnClickListener(listener);
        item6.setOnClickListener(listener);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(LayoutInflater.from(context).inflate(R.layout.fragment_community, null), Gravity.BOTTOM, 0, 0);
    }

    private class ClassificationListener implements ClassificationRecycleAdapter.OnItemClickListener{
        @Override
        public void onItemClick(View view, int position) {
            //do something
            Toast.makeText(getContext(), titles.get(position), Toast.LENGTH_SHORT).show();
        }
    }

    private class PostClassificationListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String tag = null;
            switch (view.getId()){
                case R.id.item1:
                    tag = "健身";
                    break;
                case R.id.item2:
                    tag = "护肤";
                    break;
                case R.id.item3:
                    tag = "饮食";
                    break;
                case R.id.item4:
                    tag = "保健品";
                    break;
                case R.id.item5:
                    tag = "彩妆";
                    break;
                case R.id.item6:
                    tag = "其他";
                    break;
            }
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra("tag", tag);
            startActivityForResult(intent, Codes.COMMUNITY_POST_REQUEST_CODE);
        }
    }
}
