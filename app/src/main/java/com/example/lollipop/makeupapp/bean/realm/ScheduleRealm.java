package com.example.lollipop.makeupapp.bean.realm;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by R2D2 on 2017/9/8.
 */

public class ScheduleRealm extends RealmObject{
    private String objectId;
    private String userId;
    public String title;
    private String classification;
    private String startTime;
    private String endTime;
    private String repeatMode;
    private String remindWay;
    private boolean isOpen;
    private int requestCode;
    //是否需要更新到服务器
    private boolean needUpdate;
    private Date createTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(String repeatMode) {
        this.repeatMode = repeatMode;
    }

    public String getRemindWay() {
        return remindWay;
    }

    public void setRemindWay(String remindWay) {
        this.remindWay = remindWay;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
