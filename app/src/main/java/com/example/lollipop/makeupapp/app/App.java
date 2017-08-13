package com.example.lollipop.makeupapp.app;

import android.app.Application;

import com.example.lollipop.makeupapp.injector.component.ApplicationComponent;
import com.example.lollipop.makeupapp.injector.component.DaggerApplicationComponent;
import com.example.lollipop.makeupapp.injector.module.ApplicationModule;

/**
 * Created by Lollipop on 2017/8/12.
 */

public class App extends Application {
    private static ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupInjector();
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }
}
