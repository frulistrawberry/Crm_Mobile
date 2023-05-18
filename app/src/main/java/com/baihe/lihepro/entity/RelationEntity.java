package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class RelationEntity implements Parcelable {
    private List<KeyValueEntity> info;

    public RelationEntity() {

    }

    protected RelationEntity(Parcel in) {
        info = in.createTypedArrayList(KeyValueEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RelationEntity> CREATOR = new Creator<RelationEntity>() {
        @Override
        public RelationEntity createFromParcel(Parcel in) {
            return new RelationEntity(in);
        }

        @Override
        public RelationEntity[] newArray(int size) {
            return new RelationEntity[size];
        }
    };

    public List<KeyValueEntity> getInfo() {
        return info;
    }

    public void setInfo(List<KeyValueEntity> info) {
        this.info = info;
    }
}
