package com.example.lollipop.makeupapp.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_email)
    TextInputEditText emailInput;
    @BindView(R.id.register_username)
    TextInputEditText usernameInput;
    @BindView(R.id.register_pass)
    TextInputEditText passInput;
    @BindView(R.id.register_repeat)
    TextInputEditText repeatInput;
    @BindView(R.id.repeat_hint)
    AppCompatTextView repeatHintText;
    @BindView(R.id.register)
    AppCompatButton registerBtn;

    @OnClick(R.id.register_back)
    void back(){
        AppManager.getInstance().finishActivity();
    }

    @OnClick(R.id.register)
    void register(){
        String email = emailInput.getText().toString().replaceAll(" ", "");
        String username = usernameInput.getText().toString().replaceAll(" ", "");
        String password = passInput.getText().toString().replaceAll(" ", "");
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    AppManager.getInstance().finishAllActivity();
                }else {
                    String message = "注册失败";
                    switch (e.getErrorCode()){
                        case 202:
                            message += ",用户名已存在";
                            break;
                        case 203:
                            message += ",邮箱已存在";
                            break;
                    }
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        InputClearListener.addListener(this, emailInput);
        InputClearListener.addListener(this, usernameInput);
        InputClearListener.addListener(this, passInput);
        InputClearListener.addListener(this, repeatInput);
        RegisterInputWatcher watcher = new RegisterInputWatcher();
        emailInput.addTextChangedListener(watcher);
        usernameInput.addTextChangedListener(watcher);
        passInput.addTextChangedListener(watcher);
        repeatInput.addTextChangedListener(watcher);
    }

    private class RegisterInputWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (emailInput.getText().length()>0 && usernameInput.getText().length()>0 && passInput.getText().length()>0 && repeatInput.getText().length()>0){
                if (passInput.getText().toString().equals(repeatInput.getText().toString())){
                    repeatHintText.setVisibility(View.INVISIBLE);
                    registerBtn.setEnabled(true);
                }else {
                    repeatHintText.setVisibility(View.VISIBLE);
                    registerBtn.setEnabled(false);
                }
            }else {
                registerBtn.setEnabled(false);
            }
        }
    }
}
