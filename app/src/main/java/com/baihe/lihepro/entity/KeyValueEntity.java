package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class KeyValueEntity implements Parcelable, Serializable {
    private String key;
    private String val;
    private String defaultVal;
    private String optional;
    private String showStatus;
    private KeyValEventEntity event;
    private String endText;

    private String count;
    private String rangeMin;
    private String rangeMax;
    private String tempValue;
    private String endCalendar;

    private Map<String,Object> extra;

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public KeyValueEntity() {

    }

    public KeyValueEntity(String key, String val) {
        this.key = key;
        this.val = val;
    }

    protected KeyValueEntity(Parcel in) {
        key = in.readString();
        val = in.readString();
        defaultVal = in.readString();
        optional = in.readString();
        showStatus = in.readString();
        event = in.readParcelable(KeyValEventEntity.class.getClassLoader());
        count = in.readString();
        rangeMin = in.readString();
        rangeMax = in.readString();
        tempValue = in.readString();
        endText = in.readString();
        endCalendar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(val);
        dest.writeString(defaultVal);
        dest.writeString(optional);
        dest.writeString(showStatus);
        dest.writeParcelable(event, flags);
        dest.writeString(count);
        dest.writeString(rangeMin);
        dest.writeString(rangeMax);
        dest.writeString(tempValue);
        dest.writeString(endText);
        dest.writeString(endCalendar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KeyValueEntity> CREATOR = new Creator<KeyValueEntity>() {
        @Override
        public KeyValueEntity createFromParcel(Parcel in) {
            return new KeyValueEntity(in);
        }

        @Override
        public KeyValueEntity[] newArray(int size) {
            return new KeyValueEntity[size];
        }
    };

    /**
     * 深拷贝
     *
     * @return
     */
    public KeyValueEntity copy() {
        KeyValueEntity keyValueEntity = new KeyValueEntity();
        keyValueEntity.setKey(key);
        keyValueEntity.setVal(val);
        keyValueEntity.setDefaultVal(defaultVal);
        keyValueEntity.setOptional(optional);
        keyValueEntity.setShowStatus(showStatus);
        keyValueEntity.setEndText(endText);
        //不为空copy KeyValEventEntity
        if (event != null) {
            keyValueEntity.setEvent(event.copy());
        }
        keyValueEntity.setCount(count);
        keyValueEntity.setRangeMin(rangeMin);
        keyValueEntity.setRangeMax(rangeMax);
        keyValueEntity.setTempValue(tempValue);
        keyValueEntity.setTempValue(endCalendar);
        return keyValueEntity;
    }

    public String getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(String endCalendar) {
        this.endCalendar = endCalendar;
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public KeyValEventEntity getEvent() {
        return event;
    }

    public void setEvent(KeyValEventEntity event) {
        this.event = event;
    }

    public String getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(String rangeMin) {
        this.rangeMin = rangeMin;
    }

    public String getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(String rangeMax) {
        this.rangeMax = rangeMax;
    }

    public String getTempValue() {
        return tempValue;
    }

    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
