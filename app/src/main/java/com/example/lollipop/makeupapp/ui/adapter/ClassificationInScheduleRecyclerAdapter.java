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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R2D2 on 2017/9/8.
 */

public class ClassificationInScheduleRecyclerAdapter extends RecyclerView.Adapter<ClassificationInScheduleRecyclerAdapter.ViewHolder> {
    private List<Integer> images;
    private List<String> titles;
    private List<Integer> numbers;
    private LayoutInflater inflater;
    private List<ViewHolder> holders;
    private ClassificationRecycleAdapter.OnItemClickListener onItemClickListener;

    private ClassificationInScheduleRecyclerAdapter.ViewHolder viewHolder;

    public ClassificationInScheduleRecyclerAdapter(Context context, List<Integer> images, List<String> titles, List<Integer> numbers) {
        this.images = images;
        this.titles = titles;
        this.numbers = numbers;
        this.inflater = LayoutInflater.from(context);
        holders = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout itemLayout;
        AppCompatImageView imageView;
        AppCompatTextView titleView;
        AppCompatTextView numView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 自定义item点击监听器
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ClassificationRecycleAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ClassificationInScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_classification_schedule, parent, false);
        ClassificationInScheduleRecyclerAdapter.ViewHolder viewHolder = new ClassificationInScheduleRecyclerAdapter.ViewHolder(view);
        viewHolder.itemLayout = (FrameLayout) view.findViewById(R.id.item);
        viewHolder.imageView = (AppCompatImageView) view.findViewById(R.id.img);
        viewHolder.titleView = (AppCompatTextView) view.findViewById(R.id.title);
        viewHolder.numView = (AppCompatTextView) view.findViewById(R.id.num);
        holders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ClassificationInScheduleRecyclerAdapter.ViewHolder holder, final int position) {
        holder.imageView.setImageResource(images.get(position));
        holder.titleView.setText(titles.get(position));
        int num = numbers.get(position);
        if (num > 0){
            holder.numView.setText(String.valueOf(num));
            holder.numView.setVisibility(View.VISIBLE);
        }else {
            holder.numView.setVisibility(View.GONE);
        }
        if (onItemClickListener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                    changeState(holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void changeState(ViewHolder holder){
        if (viewHolder != null) {
            viewHolder.itemLayout.setPressed(false);
        }
        viewHolder = holder;
        viewHolder.itemLayout.setPressed(true);
    }

    public ViewHolder getViewHolder(int position){
        return holders.get(position);
    }
}
