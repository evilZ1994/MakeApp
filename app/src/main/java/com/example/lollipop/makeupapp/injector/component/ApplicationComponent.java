package com.example.lollipop.makeupapp.injector.component;

import com.example.lollipop.makeupapp.app.App;
import com.example.lollipop.makeupapp.injector.module.ApplicationModule;
import com.example.lollipop.makeupapp.mvp.model.DataManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lollipop on 2017/8/13.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    App application();

    DataManager datamanager();
}
