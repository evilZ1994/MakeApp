package com.example.lollipop.makeupapp.injector.module;

import android.content.Context;

import com.example.lollipop.makeupapp.ui.base.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lollipop on 2017/8/13.
 */
@Module
public class ActivityModule {
    private BaseActivity baseActivity;

    public ActivityModule(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
    }

    @Provides
    BaseActivity provideActivity(){
        return baseActivity;
    }

    @Provides
    Context provideContext(){
        return baseActivity;
    }
}
