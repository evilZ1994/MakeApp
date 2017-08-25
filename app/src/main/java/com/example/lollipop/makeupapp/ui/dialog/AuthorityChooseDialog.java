package com.example.lollipop.makeupapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.ViewGroup;
import android.view.Window;

import com.example.lollipop.makeupapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lollipop on 2017/8/25.
 */

public class AuthorityChooseDialog extends Dialog {
    private OnChooseListener listener;

    @OnClick(R.id.open)
    void open(){
        listener.onChoose(1);
    }
    @OnClick(R.id.privacy)
    void privacy(){
        listener.onChoose(-1);
    }

    public AuthorityChooseDialog(@NonNull Context context, @StyleRes int themeResId, OnChooseListener listener) {
        super(context, themeResId);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_autority_choose);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
    }

    public interface OnChooseListener {
        void onChoose(int authority);
    }
}
