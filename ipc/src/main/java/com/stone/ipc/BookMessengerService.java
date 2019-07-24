package com.stone.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.Nullable;


/**
 * desc     : 使用 Messenger 实现 IPC
 *              它需要一个 Handler 参与构造，Handler 有个静态内部类：private final class MessengerImpl extends IMessenger.Stub
 *              该 Handler 来处理客户端消息
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/27 14 51
 */
public class BookMessengerService extends Service {
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    System.out.println("stone-> receive msg from client:" + msg.getData().getString("msg") + ", " + android.os.Process.myPid());
                    Message replyMsg = Message.obtain(null, MSG_FROM_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "yeah, i received your msg. please wait a moment." + android.os.Process.myPid());
                    replyMsg.setData(bundle);
                    try {
                        msg.replyTo.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
