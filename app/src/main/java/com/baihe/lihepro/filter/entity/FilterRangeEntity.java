package com.baihe.lihepro.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：xubo
 * Time：2020-02-23
 * Description：
 */
public class FilterRangeEntity implements Parcelable {
    public String first;
    public String second;

    public FilterRangeEntity() {

    }

    protected FilterRangeEntity(Parcel in) {
        first = in.readString();
        second = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first);
        dest.writeString(second);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterRangeEntity> CREATOR = new Creator<FilterRangeEntity>() {
        @Override
        public FilterRangeEntity createFromParcel(Parcel in) {
            return new FilterRangeEntity(in);
        }

        @Override
        public FilterRangeEntity[] newArray(int size) {
            return new FilterRangeEntity[size];
        }
    };
}
