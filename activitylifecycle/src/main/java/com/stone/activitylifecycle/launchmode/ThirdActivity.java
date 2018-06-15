package com.stone.activitylifecycle.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/13 17 43
 */
public class ThirdActivity extends AppCompatActivity {
    private static final String TAG = "stone->ThirdActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "taskId=" + getTaskId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("value", 8888);
                setResult(1001, intent);
                finish();
            }
        }, 2000);
    }
}
