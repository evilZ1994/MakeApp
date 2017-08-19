package com.example.lollipop.makeupapp.util;

import com.example.lollipop.makeupapp.bean.bmob.User;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Lollipop on 2017/8/19.
 */

public class FileUtil {
    private User currentUser;
    private File headIconFile;
    private static FileUtil instance;

    public FileUtil(){
        currentUser = User.getCurrentUser(User.class);
        headIconFile = SdCardUtil.getHeadIconFile();
    }

    public static FileUtil getInstance(){
        if (instance == null){
            instance = new FileUtil();
        }
        return instance;
    }

    public void checkHeadIcon(){
        headIconFile = SdCardUtil.getHeadIconFile();
        currentUser = User.getCurrentUser(User.class);
        if (!headIconFile.exists()){
            //从数据库下载头像
            BmobFile file = currentUser.getHead_icon();
            if (file!=null) {
                file.download(headIconFile, new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }
    }
}
