package com.example.lollipop.makeupapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.adapter.GridViewAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.dialog.AuthorityChooseDialog;
import com.example.lollipop.makeupapp.ui.listener.ScreenListener;
import com.example.lollipop.makeupapp.ui.view.DragSortGridView;
import com.example.lollipop.makeupapp.util.Codes;
import com.example.lollipop.makeupapp.util.SingleFileLimitInterceptor;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends BaseActivity {
    private User currentUser;
    private String tag;
    private InputWatcher watcher;
    private ScreenListener screenListener;
    private GridViewAdapter adapter;
    private AuthorityChooseDialog dialog;
    private int imageCountLimit = 9;

    private int mAuthority = 1;

    private static int TEXTWATCHER_LISTENER_FLAG = 0;

    @BindView(R.id.username)
    AppCompatTextView usernameText;
    @BindView(R.id.post)
    AppCompatButton postBtn;
    @BindView(R.id.input)
    AppCompatEditText inputText;
    @BindView(R.id.grid_view)
    DragSortGridView gridView;
    @BindView(R.id.authority_text)
    AppCompatTextView authorityText;

    @OnClick(R.id.back)
    void back(){
        setResult(Codes.COMMUNITY_POST_RESULT_CODE);
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.post)
    void post(){

    }
    @OnClick(R.id.authority)
    void whoCanSee(){
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        dialog = new AuthorityChooseDialog(this, R.style.InfoChangeDialogTheme, new AuthorityChooseDialog.OnChooseListener() {
            @Override
            public void onChoose(int authority) {
                mAuthority = authority;
                if (mAuthority == 1){
                    authorityText.setText("公开");
                }else if (mAuthority == -1){
                    authorityText.setText("私密");
                }
                dialog.dismiss();
            }
        });
        currentUser = User.getCurrentUser(User.class);
        tag = "#"+getIntent().getStringExtra("tag")+"#";
        watcher = new InputWatcher();
        adapter = new GridViewAdapter(this);

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

        gridView.setFootNoPositionChangeItemCount(1);//最后一个不能被拖动
        gridView.setDragModel(DragSortGridView.DRAG_BY_LONG_CLICK);
        gridView.setNumColumns(4);//4列
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapter.getPaths().get(position) != null){
                    Intent intent = new Intent(getContext(), PostImageViewActivity.class);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) adapter.getPaths());
                    intent.putExtra("position", position);
                    startActivityForResult(intent, Codes.POST_IMAGE_PREVIEW_REQUEST_CODE);
                }else {
                    SImagePicker
                            .from(getActivity())
                            .maxCount(imageCountLimit)
                            .rowCount(3)
                            .showCamera(true)
                            .pickMode(SImagePicker.MODE_IMAGE)
                            .forResult(Codes.IMAGE_REQUEST_CODE);
                }
            }
        });
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
            //TEXTWATCHER_LISTENER_FLAG = 1;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Codes.IMAGE_REQUEST_CODE){
            ArrayList<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            adapter.addImages(pathList);
            imageCountLimit = imageCountLimit - pathList.size();
            if (imageCountLimit == 0){
                gridView.setFootNoPositionChangeItemCount(0);
            }
        }else if (resultCode == Codes.POST_IMAGE_PREVIEW_RESULT_CODE && requestCode == Codes.POST_IMAGE_PREVIEW_REQUEST_CODE){
            List<String> deletePaths = data.getStringArrayListExtra("deletePaths");
            if (deletePaths != null && deletePaths.size() > 0){
                adapter.removeImages(deletePaths);
            }
            imageCountLimit = imageCountLimit + deletePaths.size();
        }
    }
}
