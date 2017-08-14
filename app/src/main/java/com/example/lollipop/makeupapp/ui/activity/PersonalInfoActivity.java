package com.example.lollipop.makeupapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.base.BaseDialog;
import com.example.lollipop.makeupapp.ui.dialog.InfoChangeDialog;
import com.example.lollipop.makeupapp.ui.dialog.SexChangeDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalInfoActivity extends BaseActivity {
    private User currentUser;
    private BaseDialog dialog;

    @BindView(R.id.nickname)
    AppCompatTextView nicknameText;
    @BindView(R.id.sex)
    AppCompatTextView sexText;
    @BindView(R.id.location)
    AppCompatTextView locationText;
    @BindView(R.id.signature)
    AppCompatTextView signatureText;
    @BindView(R.id.account_id)
    AppCompatTextView accountIDText;

    @OnClick(R.id.nickname_layout)
    void nicknameChange(){
        String title = "昵称修改";

        UpdateListener listener = new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(PersonalInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    currentUser = User.getCurrentUser(User.class);
                    nicknameText.setText(currentUser.getUsername());
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }else {
                    Toast.makeText(PersonalInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        dialog = new InfoChangeDialog(this, R.style.InfoChangeDialogTheme, title, listener);
        dialog.show();
    }
    @OnClick(R.id.sex_layout)
    void sexChange(){
        dialog = new SexChangeDialog(this, R.style.InfoChangeDialogTheme, "性别修改", new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        currentUser = User.getCurrentUser(User.class);

        nicknameText.setText(currentUser.getUsername());
        sexText.setText(currentUser.getSex());
        locationText.setText(currentUser.getLocation());
        signatureText.setText(currentUser.getSignature());
        accountIDText.setText(currentUser.getObjectId());
    }
}
