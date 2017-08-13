package com.example.lollipop.makeupapp.app;

import android.app.Application;

import com.example.lollipop.makeupapp.injector.component.ApplicationComponent;
import com.example.lollipop.makeupapp.injector.component.DaggerApplicationComponent;
import com.example.lollipop.makeupapp.injector.module.ApplicationModule;

import cn.bmob.v3.Bmob;

/**
 * Created by Lollipop on 2017/8/12.
 */

public class App extends Application {
    private static ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupInjector();
        bmobInit();
    }

    private void bmobInit() {
        Bmob.initialize(this, "e8fee277ee808c9586217c39a963b84a");
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }
}
