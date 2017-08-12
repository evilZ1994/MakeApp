package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;
import com.example.lollipop.makeupapp.ui.listener.InputClearOnFocusChangeListener;
import com.example.lollipop.makeupapp.ui.listener.InputClearOnTextChangedListener;
import com.example.lollipop.makeupapp.ui.listener.InputClearOnTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.editable;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_username)
    TextInputEditText usernameInput;
    @BindView(R.id.login_pass)
    TextInputEditText passInput;

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

        initView();

    }

    private void initView() {
        /*//当输入框有字符输入时，显示清除图标，没有字符时隐藏清除图标
        usernameInput.addTextChangedListener(new InputClearOnTextChangedListener(this, usernameInput));
        //根据点击位置设置清除图标的功能（drawableRight没有点击事件）
        usernameInput.setOnTouchListener(new InputClearOnTouchListener(usernameInput));
        //失去焦点时隐藏图标，取得焦点且输入框中有字符时，显示图标
        usernameInput.setOnFocusChangeListener(new InputClearOnFocusChangeListener(this, usernameInput));
        passInput.addTextChangedListener(new InputClearOnTextChangedListener(this, passInput));
        passInput.setOnTouchListener(new InputClearOnTouchListener(passInput));
        passInput.setOnFocusChangeListener(new InputClearOnFocusChangeListener(this, passInput));*/
        InputClearListener.addListener(this, usernameInput);
        InputClearListener.addListener(this, passInput);
    }


}
