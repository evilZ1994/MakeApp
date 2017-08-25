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

public class IsDeleteDialog extends Dialog {
    private OnDeleteListener listener;

    @OnClick(R.id.yes)
    void delete(){
        if (listener != null){
            listener.onDelete();
        }
    }
    @OnClick(R.id.no)
    void no(){
        this.dismiss();
    }

    public IsDeleteDialog(@NonNull Context context, @StyleRes int themeResId, OnDeleteListener listener) {
        super(context, themeResId);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_is_delete);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
    }

    public void setOnDeleteListener(OnDeleteListener listener){
        this.listener = listener;
    }

    public interface OnDeleteListener{
        void onDelete();
    }
}
