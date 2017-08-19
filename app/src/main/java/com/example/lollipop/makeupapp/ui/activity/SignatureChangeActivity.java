package com.example.lollipop.makeupapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.util.Codes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dagger.Provides;

public class SignatureChangeActivity extends BaseActivity {
    private User currentUser;
    private ProgressDialog dialog;

    @OnClick(R.id.back)
    void back(){
        setResult(Codes.SIGNATURE_RESULT_CODE, new Intent().putExtra("result", 3));
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.ok)
    void saveChange(){
        dialog.show();
        final String signature = inputText.getText().toString();
        User newUser = new User();
        newUser.setSignature(signature);
        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Intent data = new Intent();
                if (e == null){
                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    data.putExtra("result", 1);
                    data.putExtra("signature", signature);
                }else {
                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    data.putExtra("result", 0);
                }
                setResult(Codes.SIGNATURE_RESULT_CODE, data);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                AppManager.getInstance().finishActivity();
            }
        });
    }

    @BindView(R.id.ok)
    AppCompatTextView okText;
    @BindView(R.id.input)
    AppCompatEditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_change);

        ButterKnife.bind(this);
        currentUser = User.getCurrentUser(User.class);
        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        inputText.addTextChangedListener(new InputWatcher());
        inputText.setText(currentUser.getSignature());
    }

    private class InputWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (currentUser.getSignature()!=null){
                if (editable.length()>0 && !editable.toString().equals(currentUser.getSignature())){
                    okText.setTextColor(ContextCompat.getColor(SignatureChangeActivity.this, R.color.orange));
                    okText.setEnabled(true);
                }else {
                    okText.setTextColor(ContextCompat.getColor(SignatureChangeActivity.this, R.color.gray_ccc));
                    okText.setEnabled(false);
                }
            }else {
                if (editable.length()>0){
                    okText.setTextColor(ContextCompat.getColor(SignatureChangeActivity.this, R.color.orange));
                    okText.setEnabled(true);
                }else {
                    okText.setTextColor(ContextCompat.getColor(SignatureChangeActivity.this, R.color.gray_ccc));
                    okText.setEnabled(false);
                }
            }
        }
    }
}
