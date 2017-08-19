package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @OnClick(R.id.back)
    void back(){
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.personal_info)
    void personalInfo(){
        startActivity(new Intent(this, PersonalInfoActivity.class));
    }
    @OnClick(R.id.logout)
    void logout(){
        User.logOut();
        AppManager.getInstance().finishAllActivity();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }
}
