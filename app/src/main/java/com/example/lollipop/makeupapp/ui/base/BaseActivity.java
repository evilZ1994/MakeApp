package com.example.lollipop.makeupapp.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.example.lollipop.makeupapp.app.App;
import com.example.lollipop.makeupapp.injector.component.ActivityComponent;
import com.example.lollipop.makeupapp.injector.component.DaggerActivityComponent;

/**
 * Created by Lollipop on 2017/8/13.
 */

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent(){
        if (activityComponent == null){
            activityComponent = DaggerActivityComponent.builder().applicationComponent(App.getApplicationComponent()).build();
        }
        return activityComponent;
    }
}
