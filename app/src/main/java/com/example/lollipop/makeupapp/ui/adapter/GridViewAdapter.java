package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lollipop.makeupapp.R;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Lollipop on 2017/8/28.
 */

public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private OnItemClickListener listener;
    private List<String> images;

    public GridViewAdapter(Context context, List<String> images){
        this.context = context;
        this.images = images;
    }

    public static class ViewHolder {
        SimpleDraweeView draweeView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_image, null);
            viewHolder.draweeView = (SimpleDraweeView) view.findViewById(R.id.drawee_view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.draweeView.setImageURI(images.get(position));
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP).build();
        hierarchy.setPlaceholderImage(context.getDrawable(R.drawable.cat));
        viewHolder.draweeView.setHierarchy(hierarchy);
        if (listener != null){
            viewHolder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }

        return view;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
