// Book.aidl
package com.stone.ipc;

// Declare any non-default types here with import statements
import com.stone.ipc.binder.Book;
import com.stone.ipc.IOnNewBookListener;
import java.util.List;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookListener listener);
    void unregisterListener(IOnNewBookListener listener);

    /*
     * in(输入), out(输出), inout(输入输出);   基本数据类型时，可以省略
     */
}
