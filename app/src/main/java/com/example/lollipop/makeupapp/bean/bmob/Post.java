package com.example.lollipop.makeupapp.bean.bmob;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Lollipop on 2017/8/26.
 */

public class Post extends BmobObject {
    private String classification;
    private User author;
    private int authority;
    private String content;
    private List<String> images;
    private String location;
    private int scan_num;
    private int commented_num;
    private int collect_num;
    private int liked_num;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getScan_num() {
        return scan_num;
    }

    public void setScan_num(int scan_num) {
        this.scan_num = scan_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getCommented_num() {
        return commented_num;
    }

    public void setCommented_num(int commented_num) {
        this.commented_num = commented_num;
    }

    public int getLiked_num() {
        return liked_num;
    }

    public void setLiked_num(int liked_num) {
        this.liked_num = liked_num;
    }
}
