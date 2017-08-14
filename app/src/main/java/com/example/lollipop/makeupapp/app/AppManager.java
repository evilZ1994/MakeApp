package com.example.lollipop.makeupapp.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by Lollipop on 2017/8/14.
 */

public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    /**
     * 单一实例
     */
    public static AppManager getInstance(){
        if (instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定Activity
     */
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack){
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity(){
        for (Activity activity : activityStack){
            if (activity != null){
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context){
        finishAllActivity();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(context.getPackageName());
        System.exit(0);
    }
}
