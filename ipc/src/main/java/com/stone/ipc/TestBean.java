package com.stone.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/27 10 29
 */
public class TestBean implements Parcelable {

    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override //序列化
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeString(this.name);
    }

    public TestBean() {
    }

    //具体的反序列化构造方法
    protected TestBean(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
    }

    //反序列化：如何创建序列化对象和数组
    public static final Parcelable.Creator<TestBean> CREATOR = new Parcelable.Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel source) {
            return new TestBean(source);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };
}
