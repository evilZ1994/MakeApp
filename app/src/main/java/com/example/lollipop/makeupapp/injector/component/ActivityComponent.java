package com.example.lollipop.makeupapp.injector.component;

import com.example.lollipop.makeupapp.injector.module.ActivityModule;
import com.example.lollipop.makeupapp.injector.scope.PerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lollipop on 2017/8/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

}
