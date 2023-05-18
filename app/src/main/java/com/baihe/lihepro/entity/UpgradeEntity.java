package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：xubo
 * Time：2020-08-18
 * Description：
 */
public class UpgradeEntity implements Parcelable {
    private String version;
    private int forced;
    private String url;
    private String channel;
    private String msgContent;

    public UpgradeEntity() {

    }

    protected UpgradeEntity(Parcel in) {
        version = in.readString();
        forced = in.readInt();
        url = in.readString();
        channel = in.readString();
        msgContent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeInt(forced);
        dest.writeString(url);
        dest.writeString(channel);
        dest.writeString(msgContent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpgradeEntity> CREATOR = new Creator<UpgradeEntity>() {
        @Override
        public UpgradeEntity createFromParcel(Parcel in) {
            return new UpgradeEntity(in);
        }

        @Override
        public UpgradeEntity[] newArray(int size) {
            return new UpgradeEntity[size];
        }
    };

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getForced() {
        return forced;
    }

    public void setForced(int forced) {
        this.forced = forced;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
