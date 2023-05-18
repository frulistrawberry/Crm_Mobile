package com.baihe.lihepro.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-22
 * Description：
 */
public class FilterEntity implements Parcelable {
    public String title;
    public String type;
    public String is_multiple;
    public String filter_key;
    public FilterRangeEntity input_tip;
    public List<FilterKVEntity> list;
    public String remark;

    public FilterEntity() {

    }

    protected FilterEntity(Parcel in) {
        title = in.readString();
        type = in.readString();
        is_multiple = in.readString();
        filter_key = in.readString();
        input_tip = in.readParcelable(FilterRangeEntity.class.getClassLoader());
        list = in.createTypedArrayList(FilterKVEntity.CREATOR);
        remark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(is_multiple);
        dest.writeString(filter_key);
        dest.writeParcelable(input_tip, flags);
        dest.writeTypedList(list);
        dest.writeString(remark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterEntity> CREATOR = new Creator<FilterEntity>() {
        @Override
        public FilterEntity createFromParcel(Parcel in) {
            return new FilterEntity(in);
        }

        @Override
        public FilterEntity[] newArray(int size) {
            return new FilterEntity[size];
        }
    };
}
