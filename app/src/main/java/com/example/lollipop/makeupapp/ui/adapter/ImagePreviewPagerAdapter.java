package com.example.lollipop.makeupapp.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lollipop.makeupapp.R;
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
    //图片类别，本地（"local"）或者网络（"net"）图片
    private String tag;

    public static String TAG_LOCAL = "local";
    public static String TAG_NET = "net";

    public ImagePreviewPagerAdapter(Context context, List<String> paths, String tag){
        this.context = context;
        this.paths = paths;
        this.tag = tag;
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
        hierarchy.setPlaceholderImage(context.getDrawable(R.drawable.ic_placeholder), ScalingUtils.ScaleType.FIT_CENTER);
        Uri uri = null;
        if (tag.equals(TAG_LOCAL)){
            uri = Uri.fromFile(new File(paths.get(position)));
        }else if (tag.equals(TAG_NET)){
            uri = Uri.parse(paths.get(position));
        }
        image.setImageURI(uri);
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
