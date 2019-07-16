    创建实体映射的 aidl，如 Book.aidl
    创建用于通信的 interface aidl， 如 IBookManager.aidl
    编译生成 接口对应的 Binder 文件， 如 IBookManager.java
    其继承了android.os.IInterface
    内部静态抽象类：public static abstract class Stub extends android.os.Binder implements com.stone.ipc.IBookManager
        asInterface(android.os.IBinder obj)
            用于将服务端的 Binder对象转成客户端需要的 AIDL的接口对象，如果在两端在同一进程，则返回服务端的 Stub，否则返回 Stub.proxy
        asBinder() 返回 Binder 对象
        onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
            该方法运行在服务端的线程池中，当客户端发起 ipc 请求时，远程请求会通过系统底层封装后交由此方法来处理。
            code 用于确定客户端所请求的方法是什么；再从 data 中取出方法参数，执行目标方法；执行完后，再写入 reply 中；
            返回值为 false 表示，客户端请求失败了

    Stub 下的静态类：private static class Proxy implements com.stone.ipc.IBookManager
        IBookManager相关方法的执行过程：
            方法在客户端调用，会先创建Parcel 类型的_输入对象data 和 输出对象 _reply，还有返回值对象；
            接着把方法的参数信息写入_data 中，再调用 transact 方法来发送 RPC(远程过程调用)请求，同时挂起当前线程；
            然后服务端的onTransact方法会被调用，直到 RPC 返回后，当前线程继续执行，
            并从_reply 中取出 RPC 过程的返回结果；最后返回_reply 中的数据
        由于 RPC 会阻塞当前线程，所以尽量不要在 UI 线程中发起请求；
        其次服务端 Binder 方法(onTransact)是运行在线程池中，所以它需要采用同步的方式去实现
    可以仿照并自实现 IBookManager.java，而不声明相应的 .aidl 文件


    Binder的两个重要方法linkToDeath、unlinkToDeath，见BookService
        即 使用AIDL 来进行 服务端 和 客户端通信
    相关线程
        客户端调用远程服务的方法，该被调用方法是运行在服务端的 Binder 线程池中的，同时客户端线程被挂起；反之亦然；
        若服务端方法较耗时，将造成客户端 UI 线程被长期阻塞；
        解决方法：不管是服务端还是客户端的方法，在其被调用端，都放在一个非 UI 线程中，这就不会造成阻塞 UI 线程

    使用 Messenger 实现轻量级的 AIDL 通信，即底层是 AIDL；见 BookMessengerService
        Messenger 是串行的，即一个一个的处理消息
      * 客户端使用 一个 Messenger 来发送 Message 到服务端，
      * 该 Message 还可以指定另一个 Messenger 来处理服务端回传的消息
      * 与服务端一样，处理消息的 Messenger 采用 结合 Handler 进行构造，以便处理


