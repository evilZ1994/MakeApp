package com.example.lollipop.makeupapp.ui.listener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.lollipop.makeupapp.R;

/**
 * 把3个监听器写在一起，并且提供一个静态方法设置监听器，减少代码量
 * Created by Lollipop on 2017/8/11.
 */

public class InputClearListener implements View.OnTouchListener, TextWatcher, View.OnFocusChangeListener {
    private Context context;
    private EditText editText;

    public InputClearListener(Context context, EditText editText){
        this.context = context;
        this.editText = editText;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //获取右侧图标
        Drawable drawableRight = editText.getCompoundDrawables()[2];
        //如果右侧没有图标，则不做处理
        if (drawableRight == null){
            return false;
        }else if (motionEvent.getAction() != MotionEvent.ACTION_UP){
            //如果不是按下事件，不做处理
            return false;
        }else if (motionEvent.getX() > editText.getWidth()-editText.getPaddingEnd()-drawableRight.getIntrinsicWidth()){
            //如果点击的位置在右侧图标处，则清空输入框
            editText.setText("");
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //当输入框有字符输入时，显示清除图标，没有字符时隐藏清除图标
        if (editable.length()>0){
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.ic_cancel), null);
            //editText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.ic_cancel), null);
        }else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            //editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        //失去焦点时隐藏图标，取得焦点且输入框中有字符时，显示图标
        if (hasFocus && editText.getText().length()>0){
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, context.getDrawable(R.drawable.ic_cancel), null);
        }else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * 给EditText对象绑定监听器
     * @param context EditText所在的activity
     * @param editText 需要绑定监听器的EditText
     */
    public static void addListener(Context context, EditText editText){
        InputClearListener listener = new InputClearListener(context, editText);
        editText.addTextChangedListener(listener);
        editText.setOnTouchListener(listener);
        editText.setOnFocusChangeListener(listener);
    }
}
