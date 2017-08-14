package com.example.lollipop.makeupapp.ui.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.listener.InputClearListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FindPassActivity extends BaseActivity {

    @BindView(R.id.find_username)
    TextInputEditText usernameInput;
    @BindView(R.id.find_email)
    TextInputEditText emailInput;
    @BindView(R.id.find_submit)
    AppCompatButton submitBtn;

    @OnClick(R.id.find_submit)
    void submit(){
        String email = emailInput.getText().toString().replace(" ", "");
        User.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(FindPassActivity.this, "提交成功，我们已向您发送了一封邮件，请及时处理", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(FindPassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("email", e.getErrorCode()+":"+e.getMessage());
                }
            }
        });
    }

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
        SubmitWatcher watcher = new SubmitWatcher();
        usernameInput.addTextChangedListener(watcher);
        emailInput.addTextChangedListener(watcher);
    }

    private class SubmitWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (usernameInput.getText().length()>0 && emailInput.getText().length()>0){
                submitBtn.setEnabled(true);
            }else {
                submitBtn.setEnabled(false);
            }
        }
    }
}
