package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.util.FileUtil;
import com.example.lollipop.makeupapp.util.SdCardUtil;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class WelcomeActivity extends AppCompatActivity {
    private User currentUser;
    private File headIconFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);

        //初始化应用文件目录
        SdCardUtil.initDirectory();

        //判断用户是否已经登陆
        currentUser = User.getCurrentUser(User.class);
        if (currentUser != null){
            //用户已经登陆
            //检查头像
            FileUtil.getInstance().checkHeadIcon();
            //进入主界面
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
