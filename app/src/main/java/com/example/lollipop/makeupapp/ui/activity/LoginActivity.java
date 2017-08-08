package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lollipop.makeupapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @OnClick(R.id.login_register)
    void register(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    @OnClick(R.id.login_forget)
    void forget(){
        startActivity(new Intent(this, FindPassActivity.class));
    }
    @OnClick(R.id.login)
    void login(){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }
}
