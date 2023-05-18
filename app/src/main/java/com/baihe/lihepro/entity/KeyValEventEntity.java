package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.baihe.lihepro.filter.entity.FilterKVEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-31
 * Description：
 */
public class KeyValEventEntity implements Parcelable {
    private String action;
    private String name;
    private String icon;
    private String paramKey;
    private String format;
    private int phoneNum;
    private int wechatNum;
    private boolean isUnlock;
    private List<KeyValueEntity> options;
    private List<FilterKVEntity> channelList;

    public KeyValEventEntity() {

    }

    protected KeyValEventEntity(Parcel in) {
        action = in.readString();
        name = in.readString();
        icon = in.readString();
        paramKey = in.readString();
        format = in.readString();
        phoneNum = in.readInt();
        isUnlock = in.readByte() != 0;
        options = in.createTypedArrayList(KeyValueEntity.CREATOR);
        channelList = in.createTypedArrayList(FilterKVEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(paramKey);
        dest.writeString(format);
        dest.writeInt(phoneNum);
        dest.writeByte((byte) (isUnlock ? 1 : 0));
        dest.writeTypedList(options);
        dest.writeTypedList(channelList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KeyValEventEntity> CREATOR = new Creator<KeyValEventEntity>() {
        @Override
        public KeyValEventEntity createFromParcel(Parcel in) {
            return new KeyValEventEntity(in);
        }

        @Override
        public KeyValEventEntity[] newArray(int size) {
            return new KeyValEventEntity[size];
        }
    };

    /**
     * 深拷贝
     *
     * @return
     */
    public KeyValEventEntity copy() {
        KeyValEventEntity keyValEventEntity = new KeyValEventEntity();
        keyValEventEntity.setAction(action);
        keyValEventEntity.setName(name);
        keyValEventEntity.setIcon(icon);
        keyValEventEntity.setParamKey(paramKey);
        keyValEventEntity.setFormat(format);
        keyValEventEntity.setPhoneNum(phoneNum);
        keyValEventEntity.setUnlock(isUnlock);
        if (options != null) {
            List<KeyValueEntity> copyOptions = new ArrayList<>();
            for (KeyValueEntity keyValueEntity : options) {
                copyOptions.add(keyValueEntity.copy());
            }
            keyValEventEntity.setOptions(copyOptions);
        }
        if (channelList != null) {
            List<FilterKVEntity> copyChannels = new ArrayList<>();
            for (FilterKVEntity filterKVEntity : channelList) {
                copyChannels.add(filterKVEntity.copy());
            }
            keyValEventEntity.setChannelList(copyChannels);
        }
        return keyValEventEntity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public List<KeyValueEntity> getOptions() {
        return options;
    }

    public void setOptions(List<KeyValueEntity> options) {
        this.options = options;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isUnlock() {
        return isUnlock;
    }

    public void setUnlock(boolean unlock) {
        isUnlock = unlock;
    }

    public List<FilterKVEntity> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<FilterKVEntity> channelList) {
        this.channelList = channelList;
    }

    public int getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(int wechatNum) {
        this.wechatNum = wechatNum;
    }
}
