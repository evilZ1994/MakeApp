package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.base.DragAdapter;
import com.example.lollipop.makeupapp.util.BitmapCompressUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lollipop on 2017/8/22.
 */

public class GridViewAdapter extends DragAdapter {
    private List<String> paths;
    private List<Bitmap> images;
    private Context context;

    public GridViewAdapter(Context context){
        paths = new ArrayList<>();
        images = new ArrayList<>();
        paths.add(null);
        this.context = context;
    }

    @Override
    public void onDataModelMove(int from, int to) {
        String path = paths.remove(from);
        Bitmap image = images.remove(from);
        paths.add(to, path);
        images.add(to, image);
    }

    private static class ViewHolder {
        public AppCompatImageView imageView;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_grid_image, null);
            viewHolder.imageView = (AppCompatImageView) view.findViewById(R.id.img);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (paths.get(position) != null){
            viewHolder.imageView.setImageBitmap(images.get(position));
        }else {
            viewHolder.imageView.setImageResource(R.drawable.ic_add_pic);
        }
        return view;
    }

    public List<String> getPaths(){
        return paths;
    }

    public void addImages(List<String> pathList){
        paths.remove(paths.size()-1);
        for (String path : pathList){
            Bitmap bitmap = BitmapCompressUtil.getFitSampleBitmap(path, 200, 200);
            images.add(bitmap);
            paths.add(path);
        }
        if (paths.size() < 9){
            paths.add(null);
        }
        this.notifyDataSetChanged();
    }

    public void removeImages(List<String> pathList){
        for (String path : pathList){
            int position = paths.indexOf(path);
            Bitmap bitmap = images.get(position);
            images.remove(position);
            if (!bitmap.isRecycled()){
                bitmap.recycle();
            }
            paths.remove(path);
        }
        if (paths.size() == 0 || paths.get(paths.size()-1) != null){
            paths.add(null);
        }
        this.notifyDataSetChanged();
    }
}
