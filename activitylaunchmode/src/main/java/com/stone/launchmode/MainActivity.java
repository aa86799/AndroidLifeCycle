package com.stone.launchmode;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/13 17 27
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "stone->MainActivity";

    /*
     * 默认情况下，多次启动同一 Activity ，会创建多个实例，并压入任务栈(task stack，后进先出)中。
     * 当点击back键，会一一回退。
     *
     * 有四种启动模式，来改变上述的行为。
     *  - standard (默认)
     *  - singleTop 栈顶复用。
     *      若新 Activity 位于栈顶，则直接复用，调用 onNewIntent()
     *      若新 Activity 已存在且不位于栈顶，仍会重新创建该 Activity
     *  - singleTask 栈内复用，栈内单实例
     *      若栈不存在，会先创建栈，再创建实例，压入栈中
     *      若实例不存在，会创建实例，压入栈中
     *      若新 Activity 的实例存在(即栈也存在)，则移到栈顶，调用 onNewIntent()
     *         且默认具有 clearTop 的效果，会把栈内该实例之上的，都清除出栈
     *  - singleInstance 独栈单例
     *      除具有 singleTask的所有特性外，增加了一点：单独存在于一个任务栈中，即该栈中不能有其它实例
     *
     *  关于任务栈：
     *      每个应用一般有各自的任务，任务中可以有多个栈
     *      一个栈中可以有多个 activity 实例；每个实例也可以属于不同的栈
     *      以非 activity 的 Context 来启动 Activity，会报错，因为它们没有栈
     *
     *  设置 Activity 的任务栈:
     *      默认情况下，activity 的栈名 就是 应用的包名
     *      android:taskAffinity 可以指定任务栈的名字，但单独使用是没有意义的。
     *          结合 singleTask，则新实例会运行在相同栈名的栈中
     *          结合 android:allowTaskReparenting="true"，
     *              是否允许该activity可以更换从属task栈，用于实现把一个应用程序的Activity移到另一个应用程序的Task中
     *              如，A 应用启动 B 应用中有该配置的 Activity，则会从 A 栈中移动到属于应用 B 的栈中
     *
     *  启动模式的设置有两种方式：1：通过在 manifest 中设置 android:launchMode 2: 在代码中通过 Intent 的 FLAG_Activity_ 来设置
     *      第2种方式，比第1种灵活，可以设置更多的启动标志；若两种方式同时存在，以第2种为准
     *
     *  关于启动标志 Intent.FLAG_Activity_:
     *      - Intent.FLAG_ACTIVITY_NEW_TASK
     *          同 launchMode="singleTask"
     *      - Intent.FLAG_ACTIVITY_SINGLE_TOP
     *          同 launchMode="singleTop"
     *      - Intent.FLAG_ACTIVITY_CLEAR_TOP
     *          如果正在启动的 Activity 已在当前任务中运行，则会销毁当前任务顶部的所有 Activity，
     *          并通过 onNewIntent() 将此 Intent 传递给 Activity 已恢复的实例（现在位于顶部），而不是启动该 Activity 的新实例。
     *          产生这种行为的 launchMode 属性没有值。
     *          FLAG_ACTIVITY_CLEAR_TOP 通常与 FLAG_ACTIVITY_NEW_TASK 结合使用。
     *          一起使用时，通过这些标志，可以找到其他任务中的现有 Activity，并将其放入可从中响应 Intent 的位置。
     *      - Intent.FLAG_ACTIVITY_FORWARD_RESULT
     *          若有 Activity A，B，C； A 启动 B 使用了startActivityForResult(),
     *              而 B 又启动了 C，且启动的 Intent设置了该标记，且 B 不调用 setResult，则是交由 C 来 setResult
     *          真机测试后，发现在华为某平板上无效，慎用
     *      - Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
     *          即不出现在历史Activity列表中，等同于 xml 中设置：android:excludeFromRecents="true"
     *      - FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
     *      - FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
     *      - FLAG_ACTIVITY_NEW_DOCUMENT=FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
     *      - FLAG_ACTIVITY_NO_USER_ACTION
     *      - FLAG_ACTIVITY_REORDER_TO_FRONT
     *      - FLAG_ACTIVITY_CLEAR_TASK
     *      - FLAG_ACTIVITY_TASK_ON_HOME
     *      - FLAG_ACTIVITY_RETAIN_IN_RECENTS
     *      - FLAG_ACTIVITY_LAUNCH_ADJACENT
     *      还有些标志，是被系统使用的，不要人为使用。
     *
     *   task 相关的实用函数:
     *      boolean isTaskRoot()
     *          判断该Activity是否为任务栈中的根Activity，即启动应用的第一个Activity
     *      boolean moveTaskToBack (boolean nonRoot)
     *          用于将activity退到后台，不是finish
     *          从生命周期来说，会执行onPause、onStop，但不会执行onDestroy
     *          恢复的时候也一样，会执行onRestart、onStart、onResume，但不会执行onCreate
     *          参数nonRoot表示的含义是此方法对非根activity是否有效
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "taskId=" + getTaskId());

        Intent intent = new Intent();
        intent.setClass(this, SecondActivity.class);
        startActivityForResult(intent, 1000);

        //测试 moveTaskToBack()
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                moveTaskToBack(true);
//            }
//        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "" + requestCode + ",," + resultCode);
        if (resultCode == 1001) {
            if (requestCode == 1000) {
                Log.i(TAG, "receive msg");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
