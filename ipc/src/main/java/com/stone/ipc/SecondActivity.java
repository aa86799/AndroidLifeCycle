package com.stone.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.stone.ipc.binder.Book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/26 17 41
 */
public class SecondActivity extends AppCompatActivity {

    private IBookManager mBookManager;
    private ServiceConnection mConnection;

    private Messenger mMessenger; //发送
    private Messenger mReplyMessenger = new Messenger(new MessengerHandler()); //接收
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BookMessengerService.MSG_FROM_SERVER:
                    System.out.println("stone-> receive msg from server:" + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private IOnNewBookListener mListener = new IOnNewBookListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            System.out.println("stone-> listened a new book:" + newBook);
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {//该方法运行在 Binder 的线程池中
            System.out.println("stone-> binderDied");
            if (mBookManager == null) return;
            //取消链接
            mBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBookManager = null;

            //解绑，重新绑
            if (mConnection != null) {
                unbindService(mConnection);
                bindService(new Intent(SecondActivity.this, BookService.class),
                        mConnection, Context.BIND_AUTO_CREATE);
            }
        }
    };

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
         *
         * 安卓中IPC 的实现方式：  通过 Intent 传递数据，通过共享文件、SP，基于 Binder 的 ContentProvider、 Messenger 和 AIDL;
         *          此外，网络通信也可买实现，socket 也可以
         *      如 Intent 传递的数据需要是实现了 Serializable 或 Parcelable 接口
         *          Serializable 的序列化和反：ObjectOutputStream、ObjectInputStream； 推荐序列化到文件或通过网络传输
         *          Parcelable， 安卓推荐的，内存中序列化
         *      Binder
         *          见 aidl
         *
         */
        System.out.println("stone-> " + UserManager.userId);

        testBookService();

//        testBookMessengerService();
    }

    private void testBookService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBookManager = IBookManager.Stub.asInterface(service);
                try {
                    //链接一个死亡对象的 处理类IBinder.DeathRecipient对象
                    mBookManager.asBinder().linkToDeath(mDeathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.out.println("stone-> onServiceConnected " + Thread.currentThread().getName()); //main

                /*try {
                    mBookManager.addBook(new Book(1, "bookA"));
                    mBookManager.addBook(new Book(2, "bookB"));
                    System.out.println("stone-> " + Arrays.toString(mBookManager.getBookList().toArray()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            mBookManager.registerListener(mListener);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBookManager = null;
            }
        };
        Intent intent = new Intent(this, BookService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void testBookMessengerService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {//ui 线程
                mMessenger = new Messenger(service);
                Message msg = Message.obtain(null, BookMessengerService.MSG_FROM_CLIENT);
                Bundle data = new Bundle();
                data.putString("msg", "hello, this is client.");
                msg.setData(data);
                msg.replyTo = mReplyMessenger;
                /*
                 * Message 和 Messenger 都实现了 Parcelable，支持跨进程传输
                 * Message 中的数据载体只能是 what、arg1、arg2、Bundle 以及 replyTo;
                 *      而 object 仅支持 系统实现的 Parcelable子类
                 */
                try {
                    mMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {//ui 线程
                mBookManager = null;
                //解绑，重新绑
//                if (mConnection != null) {
//                    unbindService(mConnection);
//                    bindService(new Intent(SecondActivity.this, BookService.class),
//                            mConnection, Context.BIND_AUTO_CREATE);
//                }
            }
        };
        Intent intent = new Intent(this, BookMessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        /*
         * 客户端使用 一个 Messenger 来发送 Message 到服务端，
         * 该 Message 还可以指定另一个 Messenger 来处理服务端回传的消息
         *      与服务端一样，处理消息的 Messenger 采用 结合 Handler 进行构造，以便处理
         *
         *
         */
    }

    @Override
    protected void onDestroy() {
        if (mConnection != null) {
            unbindService(mConnection);
        }

        if (mBookManager != null && mBookManager.asBinder().isBinderAlive()) {
            try {
                mBookManager.unregisterListener(mListener);
                /*
                 * 解注册会失败，因为 Binder 在多进程中是会序列化和反序列化，由它传递的其它对象，本质上在服务端与客户端上是两个同样内容的对象；
                 * 所以造成服务端，无法删除 listener
                 * 需要使用 RemoteCallbackList
                 *      RemoteCallbackList 内部维护了一个  ArrayMap<IBinder, Callback> mCallbacks;
                 *   因为IBinder对象在多进程中是同一个，所以 可以定位到 map 中的 value
                 *      map 中 key 的 IBinder 是通过 mListener.asBinder()获取的
                 */
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();

    }
}
