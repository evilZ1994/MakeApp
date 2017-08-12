package com.example.lollipop.makeupapp.ui.listener;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.example.lollipop.makeupapp.R;

/**
 * Created by Lollipop on 2017/8/11.
 */

public class InputClearOnFocusChangeListener implements View.OnFocusChangeListener {
    private Context context;
    private EditText editText;

    public InputClearOnFocusChangeListener(Context context, EditText editText){
        this.context = context;
        this.editText = editText;
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
}
