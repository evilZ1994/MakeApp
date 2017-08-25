package com.example.lollipop.makeupapp.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.injector.component.ApplicationComponent;
import com.example.lollipop.makeupapp.injector.component.DaggerApplicationComponent;
import com.example.lollipop.makeupapp.injector.module.ApplicationModule;
import com.example.lollipop.makeupapp.ui.ImageLoader.FrescoImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;

import cn.bmob.v3.Bmob;

/**
 * Created by Lollipop on 2017/8/12.
 */

public class App extends Application {
    private static ApplicationComponent mApplicationComponent;
    private static App application;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        context = getApplicationContext();

        setupInjector();
        bmobInit();
        SImagePickerInit();
    }

    private void SImagePickerInit() {
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new FrescoImageLoader())
                .setToolbaseColor(ContextCompat.getColor(this, R.color.colorAccent))
                .build());
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

    public static App getApplication(){
        return application;
    }

    public static Context getContext(){
        return context;
    }
}
