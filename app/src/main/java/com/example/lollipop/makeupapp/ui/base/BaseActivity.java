package com.example.lollipop.makeupapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lollipop.makeupapp.app.App;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.injector.component.ActivityComponent;
import com.example.lollipop.makeupapp.injector.component.DaggerActivityComponent;

/**
 * Created by Lollipop on 2017/8/13.
 */

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化时将Activity压入管理栈
        AppManager.getInstance().addActivity(this);
    }

    public ActivityComponent getActivityComponent(){
        if (activityComponent == null){
            activityComponent = DaggerActivityComponent.builder().applicationComponent(App.getApplicationComponent()).build();
        }
        return activityComponent;
    }
}
