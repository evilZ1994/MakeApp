package com.example.lollipop.makeupapp.ui.listener;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 根据点击位置设置清除图标的功能（drawableRight没有点击事件）
 * Created by Lollipop on 2017/8/11.
 */

public class InputClearOnTouchListener implements View.OnTouchListener {
    private EditText editText;

    public InputClearOnTouchListener(EditText editText){
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
}
