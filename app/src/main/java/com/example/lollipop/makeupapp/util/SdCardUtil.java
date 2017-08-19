package com.example.lollipop.makeupapp.util;

import android.os.Environment;

import com.example.lollipop.makeupapp.bean.bmob.User;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lollipop on 2017/8/18.
 */

public class SdCardUtil {
    // 项目文件根目录
    public static final String FILEDIR = "/MakeUpApp";

    // 应用程序图片存放
    public static final String FILEIMAGE = "/images";
    //头像
    public static final String IMAGE_HEADICON = "/head_icon";
    //图片下载
    public static final String IMAGE_DOWNLOAD = "/download";

    // 应用程序缓存
    public static final String FILECACHE = "/cache";

    // 用户信息目录
    public static final String FILEUSER = "/users";

   /**
    * 检查sd卡是否可用
    * getExternalStorageState 获取状态
    * Environment.MEDIA_MOUNTED 直译  环境媒体登上  表示，当前sd可用
    */
    public static boolean checkSdCard() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            //sd卡可用
            return true;
        else
            //当前sd卡不可用
            return false;
    }

   /**
    * 获取sd卡的文件路径
    * getExternalStorageDirectory 获取路径
    */
    public static String getSdPath(){
        return Environment.getExternalStorageDirectory()+"/";
    }

   /**
    * 创建一个文件夹
    */
    public static  void  createFileDir(String fileDir){
        String path = getSdPath()+fileDir;
        File path1 = new File(path);
        if(!path1.exists())
        {
            path1.mkdirs();
        }
    }

    /**
     * 初始化应用文件目录（在启动页调用）
     */
    public static void initDirectory(){
        //创建文件根目录
        createFileDir(FILEDIR);
        //创建根目录下其他目录
        createFileDir(FILEDIR+FILEUSER);
        createFileDir(FILEDIR+FILEIMAGE);
        createFileDir(FILEDIR+FILECACHE);
        //创建Image文件夹下其他目录
        createFileDir(FILEDIR+FILEIMAGE+IMAGE_HEADICON);
        createFileDir(FILEDIR+FILEIMAGE+IMAGE_DOWNLOAD);
    }

    /**
     * 创建头像文件
     */
    public static File getHeadIconFile(){
        String path = getSdPath()+FILEDIR+FILEIMAGE+IMAGE_HEADICON+"/"+ User.getCurrentUser().getObjectId()+".jpg";
        File file = new File(path);

        return file;
    }

    /**
     * 获取缓存目录
     */
    public static String getCacheDirectory(){
        return getSdPath()+FILEDIR+FILECACHE;
    }
}
