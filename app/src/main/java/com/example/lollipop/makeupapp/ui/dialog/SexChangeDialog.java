package com.example.lollipop.makeupapp.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lollipop on 2017/8/14.
 */

public class SexChangeDialog extends BaseDialog {

    @OnClick(R.id.cancel)
    void dialogCancel(){
        this.dismiss();
    }
    @OnClick(R.id.male)
    void changeMale(){
        changeSex("男");
    }
    @OnClick(R.id.female)
    void changeFemale(){
        changeSex("女");
    }

    private void changeSex(String sex){
        User currentUser = User.getCurrentUser(User.class);
        if (sex.equals(currentUser.getSex())){
            this.dismiss();
        }else {
            User newUser = new User();
            newUser.setSex(sex);
            newUser.update(currentUser.getObjectId(), getListener());
            //点击后显示环形进度条
            this.dismiss();
            getProgressDialog().show();
        }
    }

    public SexChangeDialog(@NonNull Context context, @StyleRes int themeResId, String title, UpdateListener listener, ProgressDialog progressDialog) {
        super(context, themeResId, title, listener, progressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sex_change);

        ButterKnife.bind(this);
    }
}
