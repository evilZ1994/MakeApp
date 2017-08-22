package com.example.lollipop.makeupapp.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.listener.ScreenListener;
import com.example.lollipop.makeupapp.util.Codes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends BaseActivity {
    private User currentUser;
    private String tag;
    private InputWatcher watcher;
    private ScreenListener screenListener;

    private static int TEXTWATCHER_LISTENER_FLAG = 0;

    @BindView(R.id.username)
    AppCompatTextView usernameText;
    @BindView(R.id.post)
    AppCompatButton postBtn;
    @BindView(R.id.input)
    AppCompatEditText inputText;

    @OnClick(R.id.back)
    void back(){
        setResult(Codes.COMMUNITY_POST_RESULT_CODE);
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.post)
    void post(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        currentUser = User.getCurrentUser(User.class);
        tag = "#"+getIntent().getStringExtra("tag")+"#";
        watcher = new InputWatcher();

        usernameText.setText(currentUser.getUsername());
        //插入话题
        inputText.append(tag);
        //绑定监听
        //监听删除按键
        inputText.setOnKeyListener(new DelOnKeyListener());
        if (TEXTWATCHER_LISTENER_FLAG == 0) {
            inputText.addTextChangedListener(watcher);
            TEXTWATCHER_LISTENER_FLAG = 1;
        }
        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                if (TEXTWATCHER_LISTENER_FLAG == 0) {
                    inputText.addTextChangedListener(watcher);
                    TEXTWATCHER_LISTENER_FLAG = 1;
                }
            }

            @Override
            public void onScreenOff() {

            }

            @Override
            public void onUserPresent() {

            }
        });
        inputText.setOnClickListener(new OnInputClickListener());
    }

    /**
     * 监听删除按键，符合条件则将话题整个删除
     */
    private class DelOnKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                int selectionStart = inputText.getSelectionStart();
                int start = 0;
                if ((start = inputText.getText().toString().indexOf(tag, start)) != -1){
                    if (selectionStart > start && selectionStart <= (start + tag.length())){
                        String oldStr = inputText.getText().toString();
                        String newStr = oldStr.substring(0, start)+oldStr.substring(start+tag.length());
                        inputText.setText(newStr);
                        inputText.setSelection(start);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 监听输入，改变话题字体颜色和发布按钮状态
     */
    private class InputWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //先移除监听再修改内容，否则editable的append方法会导致无限循环，从而GC
            inputText.removeTextChangedListener(watcher);
            TEXTWATCHER_LISTENER_FLAG = 0;

            String oldStr = editable.toString();
            int selectionStart = inputText.getSelectionStart();
            int start = oldStr.indexOf(tag, 0);
            int end = start + tag.length();
            if (oldStr.contains(tag)) {
                if (oldStr.length() > tag.length()) {
                    postBtn.setEnabled(true);
                    postBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    postBtn.setEnabled(false);
                    postBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_ccc));
                }
                SpannableStringBuilder builder = new SpannableStringBuilder(oldStr);
                builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                inputText.setText(builder);
                int oldLen = oldStr.length();
                int newLen = builder.length();
                inputText.setSelection(selectionStart + newLen - oldLen);//设置光标
            }else {
                if (oldStr.length() > 0){
                    postBtn.setEnabled(true);
                    postBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }else {
                    postBtn.setEnabled(false);
                    postBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_ccc));
                }
            }
            //重新设置监听
            inputText.addTextChangedListener(watcher);
        }
    }

    private class OnInputClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int selectionStart = inputText.getSelectionStart();
            int start = 0;
            if ((start = inputText.getText().toString().indexOf(tag, start)) != -1){
                if (selectionStart > start && selectionStart <= (start + tag.length())){
                    inputText.setSelection(start+tag.length());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenListener.unregisterListener();
    }
}
