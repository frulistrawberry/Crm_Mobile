package com.baihe.lihepro.entity.photo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-10
 * Description：
 */
public class PhotoBrowserEntity implements Parcelable {
    private int browserStartIndex;
    private Map<Integer, Integer> selectIndexMap;

    public PhotoBrowserEntity() {

    }

    protected PhotoBrowserEntity(Parcel in) {
        browserStartIndex = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(browserStartIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoBrowserEntity> CREATOR = new Creator<PhotoBrowserEntity>() {
        @Override
        public PhotoBrowserEntity createFromParcel(Parcel in) {
            return new PhotoBrowserEntity(in);
        }

        @Override
        public PhotoBrowserEntity[] newArray(int size) {
            return new PhotoBrowserEntity[size];
        }
    };

    public int getBrowserStartIndex() {
        return browserStartIndex;
    }

    public void setBrowserStartIndex(int browserStartIndex) {
        this.browserStartIndex = browserStartIndex;
    }

    public Map<Integer, Integer> getSelectIndexMap() {
        return selectIndexMap;
    }

    public void setSelectIndexMap(Map<Integer, Integer> selectIndexMap) {
        this.selectIndexMap = selectIndexMap;
    }
}
