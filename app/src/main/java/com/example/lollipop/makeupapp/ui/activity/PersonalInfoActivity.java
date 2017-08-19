package com.example.lollipop.makeupapp.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.base.BaseDialog;
import com.example.lollipop.makeupapp.ui.dialog.InfoChangeDialog;
import com.example.lollipop.makeupapp.ui.dialog.SexChangeDialog;
import com.example.lollipop.makeupapp.ui.view.PickerViewUtil;
import com.example.lollipop.makeupapp.util.Codes;
import com.example.lollipop.makeupapp.util.ImagePickerUtil;
import com.example.lollipop.makeupapp.util.SdCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {
    private User currentUser;
    private File headIconFile;
    private BmobFile bmobFile;

    private BaseDialog dialog;
    private ProgressDialog progressDialog;
    private PopupWindow popupWindow;

    private String cachePicPath;
    private String cacheIconPath;
    private String oldUrl;

    @BindView(R.id.head_img)
    AppCompatImageView headImg;
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
    @BindView(R.id.time)
    AppCompatTextView registerTimeText;

    @OnClick(R.id.back)
    void back() {
        AppManager.getInstance().finishActivity();
    }

    @OnClick(R.id.head_img)
    void headImgChange() {
        showPopupWindow();
    }

    @OnClick(R.id.nickname_layout)
    void nicknameChange() {
        String title = "昵称修改";
        UpdateListener listener = new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    currentUser = User.getCurrentUser(User.class);
                    nicknameText.setText(currentUser.getUsername());
                } else {
                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };
        dialog = new InfoChangeDialog(this, R.style.InfoChangeDialogTheme, title, listener, progressDialog);
        dialog.show();
    }

    @OnClick(R.id.sex_layout)
    void sexChange() {
        UpdateListener listener = new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    currentUser = User.getCurrentUser(User.class);
                    sexText.setText(currentUser.getSex());
                } else {
                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };
        dialog = new SexChangeDialog(this, R.style.InfoChangeDialogTheme, "性别修改", listener, progressDialog);
        dialog.show();
    }

    @OnClick(R.id.location_layout)
    void locationChange() {
        UpdateListener listener = new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    currentUser = User.getCurrentUser(User.class);
                    locationText.setText(currentUser.getLocation());
                } else {
                    Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };
        new PickerViewUtil().showAddressPickerView(this, listener, progressDialog);
    }

    @OnClick(R.id.signature_layout)
    void signatureChange() {
        Intent intent = new Intent(this, SignatureChangeActivity.class);
        startActivityForResult(intent, Codes.SIGNATURE_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);

        currentUser = User.getCurrentUser(User.class);
        cachePicPath = SdCardUtil.getCacheDirectory() + "/" + currentUser.getObjectId() + "_cache_pic.jpg";
        cacheIconPath = SdCardUtil.getCacheDirectory() + "/" + currentUser.getObjectId() + "_cache_icon.jpg";

        headIconFile = SdCardUtil.getHeadIconFile();
        if (headIconFile.exists()) {
            Bitmap headIcon = BitmapFactory.decodeFile(headIconFile.getPath());
            headImg.setImageBitmap(headIcon);
        }
        nicknameText.setText(currentUser.getUsername());
        sexText.setText(currentUser.getSex());
        locationText.setText(currentUser.getLocation());
        signatureText.setText(currentUser.getSignature());
        accountIDText.setText(currentUser.getObjectId());
        registerTimeText.setText(currentUser.getCreatedAt().split(" ")[0]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.SIGNATURE_REQUEST_CODE:
                if (resultCode == Codes.SIGNATURE_RESULT_CODE) {
                    if (data.getIntExtra("result", 0) == 1) {
                        signatureText.setText(data.getStringExtra("signature"));
                    }
                }
                break;
            case Codes.GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Intent cropIntent = ImagePickerUtil.getCropIntent(data.getData(), Uri.fromFile(new File(cacheIconPath)));
                    startActivityForResult(cropIntent, Codes.GALLERY_CROP_REQUEST_CODE);
                }
                break;
            case Codes.GALLERY_CROP_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    uploadToDatabase();
                }
                break;
            case Codes.CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    //从cache目录读取照片，裁剪后保存到cache
                    Intent cropIntent = ImagePickerUtil.getCropIntent(Uri.fromFile(new File(cachePicPath)), Uri.fromFile(new File(cacheIconPath)));
                    startActivityForResult(cropIntent, Codes.CAMERA_CROP_REQUEST_CODE);
                }
                break;
            case Codes.CAMERA_CROP_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    //上传到数据库
                    uploadToDatabase();
                }
                break;
        }
    }

    private void uploadToDatabase() {
        progressDialog.show();
        bmobFile = new BmobFile(new File(cacheIconPath));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    User newUser = new User();
                    if (currentUser.getHead_icon()!=null) {
                        oldUrl = currentUser.getHead_icon().getUrl();//用于头像更改后删除旧头像
                    }
                    newUser.setHead_icon(bmobFile);
                    newUser.update(currentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                //删除旧头像
                                if (oldUrl != null) {
                                    BmobFile oldFile = new BmobFile();
                                    oldFile.setUrl(oldUrl);
                                    oldFile.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            //必须加入listener，不然删除不了
                                        }
                                    });
                                }
                                //保存到本地
                                Bitmap photo = BitmapFactory.decodeFile(cacheIconPath);
                                try {
                                    photo.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(headIconFile));
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                                headImg.setImageBitmap(photo);
                            }else {
                                Toast.makeText(getContext(), "头像保存失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window_headicon, null);

        AppCompatTextView cameraText = (AppCompatTextView) contentView.findViewById(R.id.camera);
        AppCompatTextView galleryText = (AppCompatTextView) contentView.findViewById(R.id.gallery);
        AppCompatTextView cancelText = (AppCompatTextView) contentView.findViewById(R.id.cancel);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.4f;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
        //点击空白处隐藏
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //进入和退出动画
        popupWindow.setAnimationStyle(R.style.popupWindowAnim);
        //绑定监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复透明度
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
        galleryText.setOnClickListener(this);
        cameraText.setOnClickListener(this);
        cancelText.setOnClickListener(this);

        popupWindow.showAtLocation(LayoutInflater.from(this).inflate(R.layout.activity_personal_info, null), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                popupWindow.dismiss();
                break;
            case R.id.gallery:
                Intent galleryIntent = ImagePickerUtil.getGalleryIntent();
                startActivityForResult(galleryIntent, Codes.GALLERY_REQUEST_CODE);
                break;
            case R.id.camera:
                //拍照后的照片先保存在Cache目录下
                Intent cameraIntent = ImagePickerUtil.getCameraIntent(Uri.fromFile(new File(cachePicPath)));
                startActivityForResult(cameraIntent, Codes.CAMERA_REQUEST_CODE);
                break;
        }
    }
}
