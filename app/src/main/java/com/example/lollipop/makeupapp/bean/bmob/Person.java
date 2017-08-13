package com.example.lollipop.makeupapp.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lollipop on 2017/8/13.
 */

public class Person extends BmobObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
