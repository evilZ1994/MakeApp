package com.example.lollipop.makeupapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Post;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.adapter.PostRecyclerAdapter;
import com.example.lollipop.makeupapp.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildFragment extends Fragment {

    private PostRecyclerAdapter adapter;
    private List<Post> posts;
    private String tag;
    private User currentUser;


    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static Fragment newInstance(){
        ChildFragment fragment = new ChildFragment();
        return fragment;
    }

    public ChildFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_child, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        tag = bundle.getString("tag");
        currentUser = User.getCurrentUser(User.class);

        initView();

        return view;
    }

    private void initView() {
        posts = new ArrayList<>();
        adapter = new PostRecyclerAdapter(getContext(), posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullPosts();
            }
        });
    }

    private void pullPosts() {
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        if (posts.size()>0) {
            //对比时间，查找最新动态
            Date date = DateFormatUtil.toDate(posts.get(0).getCreatedAt());
            //服务器上时间精确到毫秒，加上1000ms会丢失这个一秒内的数据，但是不会重复加载
            date.setTime(date.getTime() + 1000);
            query.addWhereGreaterThan("createdAt", new BmobDate(date));
        }
        switch (tag){
            case "share":
                query.addWhereEqualTo("author", new BmobPointer(currentUser));
                break;
            case "collect":
                query.addWhereRelatedTo("collect", new BmobPointer(currentUser));
                break;
        }
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    if (list.size()>0){
                        for (Post post : list){
                            posts.add(0, post);
                        }
                    }
                    adapter.setPosts(posts);
                }
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

}
