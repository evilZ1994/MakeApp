package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Post;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.activity.ImageViewActivity;
import com.example.lollipop.makeupapp.ui.view.MyGridView;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Lollipop on 2017/8/28.
 */

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private LayoutInflater inflater;
    private User currentUser;

    public PostRecyclerAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        inflater = LayoutInflater.from(context);
        currentUser = User.getCurrentUser(User.class);
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView headImg;
        AppCompatTextView usernameText;
        AppCompatTextView postTimeText;
        AppCompatTextView contentText;
        MyGridView gridView;
        LinearLayoutCompat collectedTimes;
        AppCompatTextView collectedText;
        LinearLayoutCompat commentedTimes;
        AppCompatTextView commentedText;
        LinearLayoutCompat thumbTimes;
        AppCompatTextView thumbText;

        boolean collectClick = false;
        boolean commentClick = false;
        boolean thumbClick = false;

        public ViewHolder(View itemView){
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.headImg = (SimpleDraweeView) view.findViewById(R.id.head_img);
        viewHolder.usernameText = (AppCompatTextView) view.findViewById(R.id.username);
        viewHolder.postTimeText = (AppCompatTextView) view.findViewById(R.id.post_time);
        viewHolder.contentText = (AppCompatTextView) view.findViewById(R.id.content);
        viewHolder.gridView = (MyGridView) view.findViewById(R.id.grid_view);
        viewHolder.collectedTimes = (LinearLayoutCompat) view.findViewById(R.id.collected_times);
        viewHolder.collectedText = (AppCompatTextView) view.findViewById(R.id.collected_times_text);
        viewHolder.commentedTimes = (LinearLayoutCompat) view.findViewById(R.id.commented_times);
        viewHolder.commentedText = (AppCompatTextView) view.findViewById(R.id.commented_times_text);
        viewHolder.thumbTimes = (LinearLayoutCompat) view.findViewById(R.id.thumb_times);
        viewHolder.thumbText = (AppCompatTextView) view.findViewById(R.id.thumb_times_text);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Post post = posts.get(position);
        User user = post.getAuthor();
        //基本信息配置
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.setRoundingParams(RoundingParams.asCircle()).build();
        holder.headImg.setImageURI(user.getHead_icon().getFileUrl());
        holder.headImg.setHierarchy(hierarchy);
        holder.usernameText.setText(user.getUsername());
        holder.postTimeText.setText(post.getCreatedAt());
        //正文内容
        holder.contentText.setText(post.getContent());
        //grid view 配置
        final List<String> imageList = post.getImages();
        if (imageList != null && imageList.size() > 0){
            List<String> images = new ArrayList<>();
            for (String path : imageList){
                images.add(path+"!/scale/20");
            }
            GridViewAdapter adapter = new GridViewAdapter(context, images);
            holder.gridView.setAdapter(adapter);
            //绑定监听
            adapter.setOnItemClickListener(new GridViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>)imageList);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }else {
            //没有图片
            List<String> images = new ArrayList<>();
            GridViewAdapter adapter = new GridViewAdapter(context, images);
            holder.gridView.setAdapter(adapter);
        }
        //收藏
        final Post queryPost = new Post();
        queryPost.setObjectId(post.getObjectId());
        BmobQuery<User> collectQuery = new BmobQuery<>();
        collectQuery.addWhereRelatedTo("collect", new BmobPointer(queryPost));
        collectQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null) {
                    if (list.contains(currentUser)) {
                        holder.collectClick = true;
                    } else {
                        holder.collectClick = false;
                    }
                    if (holder.collectClick) {
                        holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                    } else {
                        holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect1), null, null, null);
                    }
                }
            }
        });
        holder.collectedTimes.setOnClickListener(new OnItemClickListener(holder, "collect", post));
        String collectNum = post.getCollect_num()+"";
        holder.collectedText.setText(collectNum);
        //评论

        //点赞
        BmobQuery<User> likeQuery = new BmobQuery<>();
        likeQuery.addWhereRelatedTo("like", new BmobPointer(queryPost));
        likeQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list != null) {
                    if (list.contains(currentUser)) {
                        holder.thumbClick = true;
                    } else {
                        holder.thumbClick = false;
                    }
                    if (holder.thumbClick) {
                        holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                    } else {
                        holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb1), null, null, null);
                    }
                }
            }
        });
        holder.thumbTimes.setOnClickListener(new OnItemClickListener(holder, "thumb", post));
        String thumbNum = post.getLiked_num()+"";
        holder.thumbText.setText(thumbNum);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class OnItemClickListener implements View.OnClickListener{
        private ViewHolder holder;
        private String tag;
        private Post post;

        public OnItemClickListener(ViewHolder holder, String tag, Post post){
            this.holder = holder;
            this.tag = tag;
            this.post = post;
        }

        @Override
        public void onClick(View view) {
            switch (tag){
                case "collect":
                    holder.collectedTimes.setClickable(false);//先防止多次点击
                    if (holder.collectClick){
                        holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect1), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.remove(currentUser);
                        post.setCollect(relation);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.remove(post);
                        currentUser.setCollect(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        holder.collectClick = false;
                        post.setCollect_num(post.getCollect_num()-1);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                String collectNum = post.getCollect_num()+"";
                                holder.collectedText.setText(collectNum);
                                holder.collectedTimes.setClickable(true);//解除多次点击防止
                            }
                        });
                    }else {
                        holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.add(currentUser);
                        post.setCollect(relation);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.add(post);
                        currentUser.setCollect(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        holder.collectClick = true;
                        post.setCollect_num(post.getCollect_num()+1);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                String collectNum = post.getCollect_num()+"";
                                holder.collectedText.setText(collectNum);
                                holder.collectedTimes.setClickable(true);//解除多次点击防止
                            }
                        });
                    }
                    break;
                case "comment":

                    break;
                case "thumb":
                    holder.thumbTimes.setClickable(false);//先防止多次点击
                    if (holder.thumbClick){
                        holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb1), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.remove(currentUser);
                        post.setLike(relation);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.remove(post);
                        currentUser.setLike(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        holder.thumbClick = false;
                        post.setLiked_num(post.getLiked_num()-1);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                String likeNum = post.getLiked_num()+"";
                                holder.thumbText.setText(likeNum);
                                holder.thumbTimes.setClickable(true);//解除多次点击防止
                            }
                        });
                    }else {
                        holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                        BmobRelation relation = new BmobRelation();
                        relation.add(currentUser);
                        post.setLike(relation);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        BmobRelation relation2 = new BmobRelation();
                        relation2.add(post);
                        currentUser.setLike(relation2);
                        currentUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                        holder.thumbClick = true;
                        post.setLiked_num(post.getLiked_num()+1);
                        post.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                String likeNum = post.getLiked_num()+"";
                                holder.thumbText.setText(likeNum);
                                holder.thumbTimes.setClickable(true);//解除多次点击防止
                            }
                        });
                    }
                    break;
            }
        }
    }
}
