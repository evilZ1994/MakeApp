package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;

import java.util.List;

/**
 * Created by R2D2 on 2017/9/8.
 */

public class ScheduleItemRecyclerAdapter extends RecyclerView.Adapter<ScheduleItemRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<ScheduleRealm> scheduleRealms;
    LayoutInflater inflater;

    public ScheduleItemRecyclerAdapter(Context context, List<ScheduleRealm> scheduleRealms){
        this.context = context;
        this.scheduleRealms = scheduleRealms;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_schedule, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item = (RelativeLayout) view.findViewById(R.id.item);
        viewHolder.titleView = (AppCompatTextView) view.findViewById(R.id.title);
        viewHolder.timeView = (AppCompatTextView) view.findViewById(R.id.time);
        viewHolder.switchCompat = (SwitchCompat) view.findViewById(R.id.item_switch);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleRealm scheduleRealm = scheduleRealms.get(position);
        String title = scheduleRealm.getTitle();
        String startTime = scheduleRealm.getStartTime();
        String endTime = scheduleRealm.getEndTime();
        boolean isOpen = scheduleRealm.isOpen;
        String time = startTime+"-"+endTime;
        holder.titleView.setText(title);
        holder.timeView.setText(time);
        holder.switchCompat.setChecked(isOpen);
        //开关状态变化监听
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        //计划项点击监听，弹出修改菜单
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleRealms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout item;
        AppCompatTextView titleView;
        AppCompatTextView timeView;
        SwitchCompat switchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
