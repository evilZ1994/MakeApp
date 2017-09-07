package com.example.lollipop.makeupapp.bean.bmob;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 对应Bmob数据库的_User表
 * Created by Lollipop on 2017/8/14.
 */

public class User extends BmobUser {
    private String sex;
    private String location;
    private String signature;
    private BmobFile head_icon;
    private BmobRelation like;
    private BmobRelation collect;

    public BmobRelation getLike() {
        return like;
    }

    public void setLike(BmobRelation like) {
        this.like = like;
    }

    public BmobRelation getCollect() {
        return collect;
    }

    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }

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

    /**
     * 改写equals方法，用于ArrayList的contains方法的比较
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User){
            User user = (User)obj;
            return user.getObjectId().equals(this.getObjectId());
        }
        return super.equals(obj);
    }
}
