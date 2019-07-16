package com.stone.activitylifecycle.lifecycle;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.stone.activitylifecycle.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/13 11 39
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "stone->MainActivity";

    /**
     * Activity正在被创建，可以做些初始化工作，如设置布局，加载数据等
     *
     * @param savedInstanceState 保存的状态；正常启动其=null，异常重启=onSaveInstanceState(Bundle outState)中的 outState
     *          当在异常情况时， 也可以在onRestoreInstanceState(Bundle savedInstanceState) 处理savedInstanceState中保存的数据
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_main);

        Log.i(TAG, "onCreate1");


        startActivity(new Intent(this, SecondActivity.class));

        //ThirdActivity是一个非全屏的 activity，由 theme控制
//        startActivity(new Intent(this, ThirdActivity.class));
    }

    /**
     * 5.0(api21)后新增的方法。
     * 在manifest中 配置属性 <activity android:persistableMode="persistAcrossReboots" />; 表示设备重启时持久化
     * https://blog.csdn.net/jjwwmlp456/article/details/78035906
     * @param savedInstanceState
     * @param persistentState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.i(TAG, "onCreate2");


    }

    /**
     * 当 activity 从完全不可见，变为可见时，会被调用。
     * 按 home 键，或 打开一个新的 activity 后，再后退回到当前 activity，会触发。
     *
     * 若 配置了<activity android:noHistory="true"../>  表示无历史，一旦完全不可见，就会销毁掉
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    /**
     * activity 在系统中已加载，但还无法和用户交互。 即 activity 在后台
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    /**
     *  activity 在前台，可以交互了
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    /**
     * 当 activity 部分或全 不可见时
     * 若打开一个新 activity，新的生命周期 需要等 之前的 onPause()执行完毕后 才执行
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    /**
     * 当activity全不可见时
     * 若打开一个新 activity，需要等新的生命周期执行完毕后，才执行
     *
     * 若新 activity 是透明主题，则不会触发
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    /**
     *  activity 即将被销毁。
     */
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        super.onDestroy();
    }

    /**
     * 异常情况下(配置改变，内存不足)，保存数据
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState1");
    }

    /**
     * 持久化数据
     * https://blog.csdn.net/jjwwmlp456/article/details/78035906
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onSaveInstanceState2");
    }

    /**
     * 恢复异常情况下，保存的数据
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState1");
    }

    /**
     * 恢复持久化的数据
     * https://blog.csdn.net/jjwwmlp456/article/details/78035906
     * @param savedInstanceState
     * @param persistentState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.i(TAG, "onRestoreInstanceState2");
    }

    /**
     * 默认情况下，当系统配置发生改变，activity 会被销毁并重建；
     *      这时会执行 onPause() -> onSaveInstanceState(outState) -> onStop;
     *      再执行 onCreate() -> onStart() -> onRestoreInstanceState(savedInstanceState) -> onResume
     * 若在 manifest中配置 <activity android:configChanges=".."/>
     * 则不会发生 activity 会被销毁并重建，即不会走 onCreate，而是直接执行 onConfigurationChanged()
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged");
    }
}
