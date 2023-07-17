package com.baihe.lib_common.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalPhotoEntity implements Parcelable {
    private String photoId;
    private String photoPath;
    private String photoThumbnailPath;

    public LocalPhotoEntity() {

    }
    public LocalPhotoEntity(String photoId, String photoPath) {
        this.photoId = photoId;
        this.photoPath = photoPath;
    }

    protected LocalPhotoEntity(Parcel in) {
        photoId = in.readString();
        photoPath = in.readString();
        photoThumbnailPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoId);
        dest.writeString(photoPath);
        dest.writeString(photoThumbnailPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocalPhotoEntity> CREATOR = new Creator<LocalPhotoEntity>() {
        @Override
        public LocalPhotoEntity createFromParcel(Parcel in) {
            return new LocalPhotoEntity(in);
        }

        @Override
        public LocalPhotoEntity[] newArray(int size) {
            return new LocalPhotoEntity[size];
        }
    };

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoThumbnailPath() {
        return photoThumbnailPath;
    }

    public void setPhotoThumbnailPath(String photoThumbnailPath) {
        this.photoThumbnailPath = photoThumbnailPath;
    }
}
