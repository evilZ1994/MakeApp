package com.example.lollipop.makeupapp.injector.module;

import com.example.lollipop.makeupapp.api.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lollipop on 2017/8/13.
 */
@Module
public class ApiModule {
    String baseUrl = "";

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15 * 1000L, TimeUnit.MILLISECONDS)//15
                .readTimeout(20 * 1000L, TimeUnit.MILLISECONDS)//20
                .writeTimeout(30 * 1000L, TimeUnit.MILLISECONDS);//15
        return builder.build();
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }
}
