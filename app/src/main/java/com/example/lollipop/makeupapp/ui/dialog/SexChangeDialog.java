package com.example.lollipop.makeupapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.base.BaseDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lollipop on 2017/8/14.
 */

public class SexChangeDialog extends BaseDialog {


    public SexChangeDialog(@NonNull Context context, @StyleRes int themeResId, String title, UpdateListener listener) {
        super(context, themeResId, title, listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sex_change);

        ButterKnife.bind(this);
    }
}
