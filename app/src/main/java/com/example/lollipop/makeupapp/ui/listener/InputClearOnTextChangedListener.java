package com.example.lollipop.makeupapp.ui.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.lollipop.makeupapp.R;

/**
 * Created by Lollipop on 2017/8/11.
 */

public class InputClearOnTextChangedListener implements TextWatcher {
    private Context context;
    private EditText editText;

    public InputClearOnTextChangedListener(Context context, EditText editText){
        this.context = context;
        this.editText = editText;
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
}
