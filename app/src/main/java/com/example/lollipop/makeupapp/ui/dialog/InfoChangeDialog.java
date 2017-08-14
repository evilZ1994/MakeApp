package com.example.lollipop.makeupapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.Window;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseDialog;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lollipop on 2017/8/14.
 */

public class InfoChangeDialog extends BaseDialog {

    @BindView(R.id.title_text)
    AppCompatTextView titleText;
    @BindView(R.id.input)
    TextInputEditText inputEditText;
    @BindView(R.id.ok)
    AppCompatTextView okText;

    public InfoChangeDialog(@NonNull Context context, @StyleRes int themeResId, String title, UpdateListener listener) {
        super(context, themeResId, title, listener);
    }

    @OnClick(R.id.cancel)
    void cancelChange(){
        this.dismiss();
    }
    @OnClick(R.id.ok)
    void ok(){
        String username = inputEditText.getText().toString();
        User newUser = new User();
        newUser.setUsername(username);
        BmobUser currentUser = User.getCurrentUser();
        newUser.update(currentUser.getObjectId(), getListener());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_change);

        ButterKnife.bind(this);

        titleText.setText(getTitle());
        inputEditText.addTextChangedListener(new InputWatcher());
        InputClearListener.addListener(getContext(), inputEditText);
    }

    private class InputWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0){
                okText.setEnabled(true);
                okText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }else {
                okText.setEnabled(false);
                okText.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_ccc));
            }
        }
    }
}
