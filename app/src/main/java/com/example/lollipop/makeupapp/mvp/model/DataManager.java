package com.example.lollipop.makeupapp.mvp.model;

import com.example.lollipop.makeupapp.api.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 封装网络数据的各种操作
 * Created by Lollipop on 2017/8/13.
 */
@Singleton
public class DataManager {
    private ApiService apiService;

    @Inject
    public DataManager(ApiService apiService){
        this.apiService = apiService;
    }
}
