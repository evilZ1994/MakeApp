package com.example.lollipop.makeupapp.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by R2D2 on 2017/9/8.
 */

public class Schedule extends BmobObject{
    private User user;
    private String title;
    private String classification;
    private String startTime;
    private String endTime;
    private String repeatMode;
    private String remindWay;
    private boolean isOpen;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
