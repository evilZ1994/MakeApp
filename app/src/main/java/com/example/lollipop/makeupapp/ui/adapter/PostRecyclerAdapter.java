package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Collect;
import com.example.lollipop.makeupapp.bean.bmob.Like;
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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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

        Collect collect;
        Like like;
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
        Post queryPost = new Post();
        queryPost.setObjectId(post.getObjectId());
        BmobQuery<Collect> collectQuery = new BmobQuery<>();
        collectQuery.addWhereEqualTo("post", new BmobPointer(queryPost));
        collectQuery.addWhereEqualTo("user", new BmobPointer(currentUser));
        collectQuery.findObjects(new FindListener<Collect>() {
            @Override
            public void done(List<Collect> list, BmobException e) {
                if (list!=null && list.size()>0){
                    //holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                    holder.collectClick = true;
                    holder.collect = list.get(0);
                }else {
                    holder.collectClick = false;
                    holder.collect = null;
                }
                if (holder.collectClick){
                    holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                }else {
                    holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect1), null, null, null);
                }
            }
        });
        holder.collectedTimes.setOnClickListener(new OnItemClickListener(holder, "collect", post));
        String collectNum = post.getCollect_num()+"";
        holder.collectedText.setText(collectNum);
        //评论

        //点赞
        BmobQuery<Like> likeQuery = new BmobQuery<>();
        likeQuery.addWhereEqualTo("post", new BmobPointer(queryPost));
        likeQuery.addWhereEqualTo("user", new BmobPointer(currentUser));
        likeQuery.findObjects(new FindListener<Like>() {
            @Override
            public void done(List<Like> list, BmobException e) {
                if (list!=null && list.size()>0){
                    //holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                    holder.thumbClick = true;
                    holder.like = list.get(0);
                }else {
                    holder.thumbClick = false;
                    holder.like = null;
                }
                if (holder.thumbClick){
                    holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                }else {
                    holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb1), null, null, null);
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
                        holder.collect.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
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
                            }
                        });
                    }else {
                        holder.collectedText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_collect2), null, null, null);
                        final Collect collect = new Collect();
                        collect.setPost(post);
                        collect.setUser(currentUser);
                        collect.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
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
                                    holder.collect = collect;
                                }
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
                        holder.like.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                holder.thumbClick = false;
                                post.setLiked_num(post.getLiked_num()-1);
                                post.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        String thumbNum = post.getLiked_num()+"";
                                        holder.thumbText.setText(thumbNum);
                                        holder.thumbTimes.setClickable(true);//解除防止多次点击
                                    }
                                });
                            }
                        });
                    }else {
                        holder.thumbText.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_thumb2), null, null, null);
                        final Like like = new Like();
                        like.setPost(post);
                        like.setUser(currentUser);
                        like.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    holder.thumbClick = true;
                                    post.setLiked_num(post.getLiked_num()+1);
                                    post.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            String thumbNum = post.getLiked_num()+"";
                                            holder.thumbText.setText(thumbNum);
                                            holder.thumbTimes.setClickable(true);//解除防止多次点击
                                        }
                                    });
                                    holder.like = like;
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }
}
