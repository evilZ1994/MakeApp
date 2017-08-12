package com.example.lollipop.makeupapp.ui.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindPassActivity extends AppCompatActivity {

    @BindView(R.id.find_username)
    TextInputEditText usernameInput;
    @BindView(R.id.find_email)
    TextInputEditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        InputClearListener.addListener(this, usernameInput);
        InputClearListener.addListener(this, emailInput);
    }
}
