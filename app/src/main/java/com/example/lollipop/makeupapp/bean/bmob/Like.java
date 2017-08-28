package com.example.lollipop.makeupapp.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lollipop on 2017/8/28.
 */

public class Like extends BmobObject {
    private User user;
    private Post post;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
