package com.stone.ipc;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.stone.ipc.binder.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.Nullable;


/**
 * desc     : 示例了在 Service 中使用 IBinder，也属于 AIDL。 当前 service 在一个新进程
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/27 12 32
 */
public class BookService extends Service {

    private CopyOnWriteArrayList<Book> mList;//多进程，需要是线程安全

//    private CopyOnWriteArrayList<IOnNewBookListener> mListeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookListener> mListeners = new RemoteCallbackList<>();

    private IBookManager mBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            System.out.println("stone-> addBook-thread: " + Thread.currentThread().getName());
            getBookList().add(book);
        }

        @Override
        public void registerListener(IOnNewBookListener listener) throws RemoteException {
//            if (!mListeners.contains(listener)) {
//                mListeners.add(listener);
//            }

            mListeners.register(listener);
            System.out.println("stone-> register-thread: " + Thread.currentThread().getName());
        }

        @Override
        public void unregisterListener(IOnNewBookListener listener) throws RemoteException {
//            if (mListeners.contains(listener)) {
//                mListeners.remove(listener);
//            }

            mListeners.unregister(listener);
        }
    };

    private AtomicInteger mAtomicInteger = new AtomicInteger(0);
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Book book = new Book(1, "book" + mAtomicInteger.addAndGet(1));
//                    mList.add(book);
                    try {
                        onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    System.out.println("stone-> handleMsg-thread: " + Thread.currentThread().getName());
                    if (mAtomicInteger.get() < 3) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mList = new CopyOnWriteArrayList<>();
        mHandler.sendEmptyMessage(0);
    }

    @Nullable
    @Override //当前 service 用AIDL时，需要有返回值，其它仅做后台服务时，可返回 null
    public IBinder onBind(Intent intent) {
        //做权限验证
        int check = checkCallingOrSelfPermission("com.stone.ipc.share.permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBookManager.asBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 新 book 的 观察者、监听器
     * @param book
     * @throws RemoteException
     */
    private void onNewBookArrived(final Book book) throws RemoteException {
        mBookManager.addBook(book);
        System.out.println("stone-> " + mBookManager.getBookList().size());
//        for (int i = 0, len = mListeners.size(); i < len; i++) {
//            IOnNewBookListener listener = mListeners.get(i);
//            listener.onNewBookArrived(book);
//        }

        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0, len = mListeners.beginBroadcast(); i < len; i++) {
                        IOnNewBookListener listener = mListeners.getBroadcastItem(i);
                        listener.onNewBookArrived(book);
                    }
                    mListeners.finishBroadcast();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}
