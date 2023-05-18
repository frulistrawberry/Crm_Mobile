package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-14
 * Description：
 */
public class FollowEntity implements Parcelable {
    private List<KeyValueEntity> followData;
    private List<KeyValueEntity> confirmArrivalInfo;

    public FollowEntity() {

    }

    protected FollowEntity(Parcel in) {
        followData = in.createTypedArrayList(KeyValueEntity.CREATOR);
        confirmArrivalInfo = in.createTypedArrayList(KeyValueEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(followData);
        dest.writeTypedList(confirmArrivalInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FollowEntity> CREATOR = new Creator<FollowEntity>() {
        @Override
        public FollowEntity createFromParcel(Parcel in) {
            return new FollowEntity(in);
        }

        @Override
        public FollowEntity[] newArray(int size) {
            return new FollowEntity[size];
        }
    };

    public List<KeyValueEntity> getFollowData() {
        return followData;
    }

    public void setFollowData(List<KeyValueEntity> followData) {
        this.followData = followData;
    }

    public List<KeyValueEntity> getConfirmArrivalInfo() {
        return confirmArrivalInfo;
    }

    public void setConfirmArrivalInfo(List<KeyValueEntity> confirmArrivalInfo) {
        this.confirmArrivalInfo = confirmArrivalInfo;
    }
}
