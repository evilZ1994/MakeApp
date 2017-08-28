package com.example.lollipop.makeupapp.bean.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Lollipop on 2017/8/13.
 */

public class Person extends BmobObject {
    private BmobGeoPoint location;

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }
}
