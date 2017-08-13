package com.example.lollipop.makeupapp.injector.module;

import android.content.Context;

import com.example.lollipop.makeupapp.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private App mApplication;

    public ApplicationModule(App application){
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public App provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return mApplication;
    }
}
