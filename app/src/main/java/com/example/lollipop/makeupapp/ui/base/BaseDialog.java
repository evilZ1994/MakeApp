package com.example.lollipop.makeupapp.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.ViewGroup;
import android.view.Window;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lollipop on 2017/8/14.
 */

public class BaseDialog extends Dialog {
    private String title;
    private UpdateListener listener;

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId, String title, UpdateListener listener) {
        super(context, themeResId);
        this.title = title;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public String getTitle(){
        return title;
    }

    public UpdateListener getListener(){
        return listener;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        //设置宽高
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
