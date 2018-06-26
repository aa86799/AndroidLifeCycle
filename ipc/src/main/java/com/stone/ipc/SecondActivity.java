package com.stone.ipc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/26 17 41
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * SecondActivity 运行在一个独立进程中，它也有一个 UserManager 的类，所以其中的静态变量还是原始值
         * 所以，如果运行在不同进程中，且要通过内存来共享数据，都会共享失败
         *
         * 一般来说，多进程的影响：
         *      静态成员和单例模式完全失效
         *      线程同步机制完全失效
         *      SharedPreferences 的可靠性下降(底层 xml 文件实现，并发读写会引发数据丢失问题)
         *      Application 会多次创建
         *          相当于两个不同的应用间采用了 (shared)
         */
        System.out.println("stone-> " + UserManager.userId);
    }
}
