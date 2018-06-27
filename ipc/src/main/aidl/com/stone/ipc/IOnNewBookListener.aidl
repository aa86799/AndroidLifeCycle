package com.stone.ipc;

// Declare any non-default types here with import statements
import com.stone.ipc.binder.Book;

interface IOnNewBookListener {
    void onNewBookArrived(in Book newBook);
}
