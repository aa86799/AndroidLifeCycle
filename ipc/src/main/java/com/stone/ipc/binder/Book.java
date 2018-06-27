package com.stone.ipc.binder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/27 10 51
 */
public class Book implements Parcelable {

    public int bookId;
    public String bookName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.bookName);
    }

    public void readFromParcel(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public Book() {

    }

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
