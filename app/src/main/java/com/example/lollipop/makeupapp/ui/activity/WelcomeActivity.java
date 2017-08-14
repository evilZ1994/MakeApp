package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lollipop.makeupapp.bean.bmob.User;

import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);

        //判断用户是否已经登陆
        BmobUser user = User.getCurrentUser();
        if (user != null){
            //用户已经登陆，直接进入主界面
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }else {
            //用户未登陆或者登陆过期，进入登陆界面
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
