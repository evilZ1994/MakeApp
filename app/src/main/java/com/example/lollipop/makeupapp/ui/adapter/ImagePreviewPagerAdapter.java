package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

/**
 * Created by Lollipop on 2017/8/24.
 */

public class ImagePreviewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> paths;
    private View.OnClickListener listener;

    public ImagePreviewPagerAdapter(Context context, List<String> paths){
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SimpleDraweeView image = new SimpleDraweeView(context);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER).build();
        image.setImageURI(Uri.fromFile(new File(paths.get(position))));
        image.setHierarchy(hierarchy);
        container.addView(image);
        if (listener != null){
            image.setOnClickListener(listener);
        }
        return image;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public String deleteItem(int position){
        String path = paths.get(position);
        paths.remove(position);
        this.notifyDataSetChanged();

        return path;
    }
}
