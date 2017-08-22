package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lollipop.makeupapp.R;

import java.util.List;

/**
 * Created by Lollipop on 2017/8/19.
 */

public class ClassificationRecycleAdapter extends RecyclerView.Adapter<ClassificationRecycleAdapter.ViewHolder> {
    private List<Integer> images;
    private List<String> titles;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public ClassificationRecycleAdapter(Context context, List<Integer> images, List<String> titles){
        this.images = images;
        this.titles = titles;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        FrameLayout itemLayout;
        AppCompatImageView imageView;
        AppCompatTextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 自定义item点击监听器
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_classification, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemLayout = (FrameLayout) view.findViewById(R.id.item);
        viewHolder.imageView = (AppCompatImageView) view.findViewById(R.id.img);
        viewHolder.titleView = (AppCompatTextView) view.findViewById(R.id.title);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setImageResource(images.get(position));
        holder.titleView.setText(titles.get(position));
        if (onItemClickListener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
