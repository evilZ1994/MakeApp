package com.example.lollipop.makeupapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.Post;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.adapter.GridViewAdapter;
import com.example.lollipop.makeupapp.ui.adapter.PostRecyclerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.view.MyGridView;
import com.example.lollipop.makeupapp.util.Codes;
import com.example.lollipop.makeupapp.util.DateFormatUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostCheckActivity extends BaseActivity {
    private Post mPost;
    private User user;
    private Context context;
    private User currentUser;

    private List<String> images;

    private boolean collectClick = false;
    private boolean commentClick = false;
    private boolean thumbClick = false;

    private int collectNum;
    private int commentNum;
    private int thumbNum;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.head_img)
    SimpleDraweeView headImg;
    @BindView(R.id.username)
    AppCompatTextView usernameText;
    @BindView(R.id.post_time)
    AppCompatTextView postTimeText;
    @BindView(R.id.content)
    AppCompatTextView contentText;
    @BindView(R.id.grid_view)
    MyGridView gridView;
    @BindView(R.id.collected_times_text)
    AppCompatTextView collectedTimesText;
    @BindView(R.id.commented_times_text)
    AppCompatTextView commentedTimesText;
    @BindView(R.id.thumb_times_text)
    AppCompatTextView thumbTimesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_post);

        ButterKnife.bind(this);

        context = this;
        currentUser = User.getCurrentUser(User.class);

        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        Observable<Post> observable = query.getObjectObservable(Post.class, objectId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Post post) {
                        mPost = post;
                        user = mPost.getAuthor();
                        initView();
                    }
                });
        toolbar.inflateMenu(R.menu.menu_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity();
            }
        });
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder.setRoundingParams(RoundingParams.asCircle()).build();
        hierarchy.setPlaceholderImage(getDrawable(R.drawable.ic_head));
        headImg.setHierarchy(hierarchy);
    }

    private void initView() {
        //基本信息配置
        headImg.setImageURI(user.getHead_icon().getFileUrl());
        usernameText.setText(user.getUsername());
        postTimeText.setText(mPost.getCreatedAt());
        //正文内容
        contentText.setText(mPost.getContent());
        //grid view 配置
        images = mPost.getImages();
        if (images != null && images.size() > 0) {
            List<String> imagesList = new ArrayList<>();
            for (String path : images) {
                imagesList.add(path + "!/scale/20");
            }
            GridViewAdapter adapter = new GridViewAdapter(this, imagesList);
            gridView.setAdapter(adapter);
            //绑定监听
            adapter.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), ImageViewActivity.class);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) images);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
            gridView.setOnTouchInvalidPositionListener(new MyGridView.OnTouchInvalidPositionListener() {
                @Override
                public boolean onTouchInvalidPosition(int motionEvent) {
                    /*当返回false的时候代表交由父级控件处理，当return true的时候表示你已经处理了该事件并不让该事件再往上传递。为了触发listview的item点击就得返回false了*/
                    Intent intent = new Intent(context, PostCheckActivity.class);
                    context.startActivity(intent);
                    return false;
                }
            });
        } else {
            //没有图片
            List<String> images = new ArrayList<>();
            GridViewAdapter adapter = new GridViewAdapter(context, images);
            gridView.setAdapter(adapter);
        }
        //收藏
        final Post queryPost = new Post();
        queryPost.setObjectId(mPost.getObjectId());
        BmobQuery<User> collectQuery = new BmobQuery<>();
        collectQuery.addWhereRelatedTo("collect", new BmobPointer(queryPost));
        collectQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null) {
                    if (list.contains(currentUser)) {
                        collectClick = true;
                    } else {
                        collectClick = false;
                    }
                    if (collectClick) {
                        collectedTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                    } else {
                        collectedTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect1), null, null, null);
                    }
                }
            }
        });
        collectedTimesText.setOnClickListener(new MyOnClickListener("collect"));
        collectNum = mPost.getCollect_num();
        String collectNumStr = collectNum + "";
        collectedTimesText.setText(collectNumStr);
        //评论

        //点赞
        BmobQuery<User> likeQuery = new BmobQuery<>();
        likeQuery.addWhereRelatedTo("like", new BmobPointer(queryPost));
        likeQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null) {
                    if (list.contains(currentUser)) {
                        thumbClick = true;
                    } else {
                        thumbClick = false;
                    }
                    if (thumbClick) {
                        thumbTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                    } else {
                        thumbTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb1), null, null, null);
                    }
                }
            }
        });
        thumbTimesText.setOnClickListener(new MyOnClickListener("thumb"));
        thumbNum = mPost.getLiked_num();
        String thumbNumStr = thumbNum + "";
        thumbTimesText.setText(thumbNumStr);
    }

    private class MyOnClickListener implements View.OnClickListener {
        private String tag;

        public MyOnClickListener(String tag) {
            this.tag = tag;
        }

        @Override
        public void onClick(View v) {
            //当被点击后，提醒更新
            setResult(Codes.COMMUNITY_CHECK_POST_RESULT_OK);
            switch (tag) {
                case "collect":
                    collectedTimesText.setClickable(false);//先防止多次点击
                    if (collectClick) {
                        collectedTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect1), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.remove(currentUser);
                        mPost.setCollect(relation);
                        mPost.increment("collect_num", -1);
                        mPost.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                collectedTimesText.setClickable(true);//解除多次点击防止
                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.remove(mPost);
                        currentUser.setCollect(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        collectClick = false;
                        collectNum = collectNum - 1;
                        String collectNumStr = collectNum + "";
                        collectedTimesText.setText(collectNumStr);
                    } else {
                        collectedTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.add(currentUser);
                        mPost.setCollect(relation);
                        mPost.increment("collect_num", 1);
                        mPost.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                collectedTimesText.setClickable(true);//解除多次点击防止
                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.add(mPost);
                        currentUser.setCollect(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        collectClick = true;
                        collectNum = collectNum + 1;
                        String collectNumStr = collectNum + "";
                        collectedTimesText.setText(collectNumStr);
                    }
                    break;
                case "comment":

                    break;
                case "thumb":
                    thumbTimesText.setClickable(false);//先防止多次点击
                    if (thumbClick) {
                        thumbTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb1), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.remove(currentUser);
                        mPost.setLike(relation);
                        mPost.increment("liked_num", -1);
                        mPost.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                refreshPost();
                                thumbTimesText.setClickable(true);//解除多次点击防止
                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.remove(mPost);
                        currentUser.setLike(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        thumbClick = false;
                        thumbNum = thumbNum - 1;
                        String likeNumStr = thumbNum + "";
                        thumbTimesText.setText(likeNumStr);
                    } else {
                        thumbTimesText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.add(currentUser);
                        mPost.setLike(relation);
                        mPost.increment("liked_num", 1);
                        mPost.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                refreshPost();
                                thumbTimesText.setClickable(true);//解除多次点击防止
                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.add(mPost);
                        currentUser.setLike(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        thumbClick = true;
                        thumbNum = thumbNum + 1;
                        String likeNumStr = thumbNum + "";
                        thumbTimesText.setText(likeNumStr);
                    }
                    break;
            }
        }
    }

    private void refreshPost() {
       new BmobQuery<Post>().getObject(mPost.getObjectId(), new QueryListener<Post>() {
            @Override
            public void done(Post post, BmobException e) {
                mPost = post;
            }
        });
    }
}
