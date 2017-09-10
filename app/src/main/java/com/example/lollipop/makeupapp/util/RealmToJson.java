package com.example.lollipop.makeupapp.util;

import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.google.gson.JsonObject;

/**
 * Created by R2D2 on 2017/9/10.
 */

public class RealmToJson {

    public static String scheduleToJson1(ScheduleRealm schedule){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("objectId", schedule.getObjectId());
        jsonObject.addProperty("userId", schedule.getUserId());
        jsonObject.addProperty("createTime", DateFormatUtil.toStr(schedule.getCreateTime()));

        return jsonObject.toString();
    }
}
