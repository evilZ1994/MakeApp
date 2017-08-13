package com.example.lollipop.makeupapp.injector.module;

import android.content.Context;

import com.example.lollipop.makeupapp.ui.base.BaseFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lollipop on 2017/8/13.
 */
@Module
public class FragmentModule {
    private BaseFragment baseFragment;

    public FragmentModule(BaseFragment baseFragment){
        this.baseFragment = baseFragment;
    }

    @Provides
    BaseFragment provideFragment(){
        return baseFragment;
    }

    @Provides
    Context provideContext(){
        return baseFragment.getContext();
    }
}
