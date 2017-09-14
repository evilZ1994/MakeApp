package com.example.lollipop.makeupapp.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Post;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.activity.PostActivity;
import com.example.lollipop.makeupapp.ui.adapter.ClassificationRecycleAdapter;
import com.example.lollipop.makeupapp.ui.adapter.PostRecyclerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseFragment;
import com.example.lollipop.makeupapp.util.Codes;
import com.example.lollipop.makeupapp.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends BaseFragment {
    private User currentUser;
    private List<Integer> images;
    private List<String> classifications;
    private ClassificationRecycleAdapter classificationAdapter;
    private PostRecyclerAdapter postAdapter;
    private List<Post> posts;
    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;
    private PopupWindow popupWindow;

    private Context context;
    private Activity activity;

    private int[] imgResources = {
            R.drawable.ic_bicycle, R.drawable.ic_mask,
            R.drawable.ic_eating, R.drawable.ic_healthcare,
            R.drawable.ic_cosmetic, R.drawable.ic_menu
    };
    private String[] classificationTexts = {
            "健身", "护肤", "饮食", "保健品", "彩妆", "其他"
    };
    private String classification = "none";

    @BindView(R.id.title)
    AppCompatTextView titleText;
    @BindView(R.id.recycler)
    RecyclerView classificationRecycler;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.main_recycler)
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
        initPostList();
        return view;
    }

    private void initView() {
        context = getContext();
        activity = getActivity();
        currentUser = User.getCurrentUser(User.class);
        images = new ArrayList<>();
        classifications = new ArrayList<>();
        for (int i=0; i<classificationTexts.length; i++){
            images.add(imgResources[i]);
            classifications.add(classificationTexts[i]);
        }
        classificationAdapter = new ClassificationRecycleAdapter(getContext(), images, classifications);
        classificationAdapter.setOnItemClickListener(new ClassificationListener());
        layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        classificationRecycler.setLayoutManager(layoutManager1);
        classificationRecycler.setAdapter(classificationAdapter);

        titleText.setText(currentUser.getUsername());

        //进入直接加载数据
        refreshLayout.setRefreshing(true);
        pullPosts();
    }

    private void initPostList(){
        posts = new ArrayList<>();
        postAdapter = new PostRecyclerAdapter(getContext(), posts);
        postAdapter.setOnBlankClickListener(new PostRecyclerAdapter.OnBlankClickListener() {
            @Override
            public void OnBlankClick(Intent intent) {
                startActivityForResult(intent, Codes.COMMUNITY_CHECK_POST_REQUEST_CODE);
            }
        });
        layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(postAdapter);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取动态
                pullPosts();
            }
        });

    }

    private void pullPosts() {
        //查询帖子和用户
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include("author");//将作者信息也一起查询出来
        /*if (!classification.equals("none")){
            //根据分类查找
            query.addWhereEqualTo("classification", classification);
            //移除posts里其它标签的内容
            List<Post> temp = new ArrayList<>();
            for (Post post : posts){
                if (!post.getClassification().equals(classification)){
                    //迭代过程中不能修改list中的内容，所以用了一个临时的list暂存
                    temp.add(0, post);
                }
            }
            posts.removeAll(temp);
        }
        if (posts.size() > 0){
            //对比时间，查找最新动态
            Date date = DateFormatUtil.toDate(posts.get(0).getCreatedAt());
            //服务器上时间精确到毫秒，加上1000ms会丢失这个一秒内的数据，但是不会重复加载
            date.setTime(date.getTime()+1000);
            query.addWhereGreaterThan("createdAt", new BmobDate(date));
        }
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    int oldSize = posts.size();
                    if (list.size()>0) {
                        for (Post post : list){
                            posts.add(0, post);
                        }
                    }
                    postAdapter.setPosts(posts);
                    Toast.makeText(getContext(), "更新了"+(list.size()-oldSize)+"条动态", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
                //取消刷新
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        });*/
        if (!classification.equals("none")){
            query.addWhereEqualTo("classification", classification);
        }
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    int oldSize = posts.size();
                    posts.clear();
                    for (Post post : list){
                        posts.add(0, post);
                    }
                    postAdapter.setPosts(posts);
                    Toast.makeText(getContext(), "更新了"+(list.size()-oldSize)+"条动态", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
                //取消刷新
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
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
            classification = classificationTexts[position];
            pullPosts();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Codes.COMMUNITY_POST_REQUEST_CODE && resultCode == Codes.COMMUNITY_POST_RESULT_OK){
            if (popupWindow.isShowing()){
                popupWindow.dismiss();
            }
            pullPosts();
        }else if (requestCode == Codes.COMMUNITY_CHECK_POST_REQUEST_CODE && resultCode == Codes.COMMUNITY_CHECK_POST_RESULT_OK){
            pullPosts();
        }
    }
}
