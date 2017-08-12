package com.example.lollipop.makeupapp.ui.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_email)
    TextInputEditText emailInput;
    @BindView(R.id.register_username)
    TextInputEditText usernameInput;
    @BindView(R.id.register_pass)
    TextInputEditText passInput;
    @BindView(R.id.register_repeat)
    TextInputEditText repeatInput;

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
    }
}
