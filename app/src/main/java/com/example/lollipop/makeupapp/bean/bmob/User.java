package com.example.lollipop.makeupapp.bean.bmob;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 对应Bmob数据库的_User表
 * Created by Lollipop on 2017/8/14.
 */

public class User extends BmobUser {
    private String sex;
    private String location;
    private String signature;
    private BmobFile head_icon;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BmobFile getHead_icon() {
        return head_icon;
    }

    public void setHead_icon(BmobFile head_icon) {
        this.head_icon = head_icon;
    }
}
