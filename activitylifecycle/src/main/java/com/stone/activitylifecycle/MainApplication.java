package com.stone.activitylifecycle;

import android.app.Application;

import com.stone.activitylifecycle.lifecycle.GlobalActivityLifecycle;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/14 10 22
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //注册全局 activity 的生命周期监听回调
        registerActivityLifecycleCallbacks(new GlobalActivityLifecycle());
    }
}
