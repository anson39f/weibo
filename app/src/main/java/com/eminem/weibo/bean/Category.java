package com.eminem.weibo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @todo 分类
 */
public class Category implements Parcelable {
    public String id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
