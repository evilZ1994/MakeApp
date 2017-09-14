package com.example.lollipop.makeupapp.bean.realm;

import io.realm.RealmObject;

/**
 * Created by R2D2 on 2017/9/14.
 */

public class ScheduleDelete extends RealmObject {
    private String objectId;
    private String userId;

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
}
