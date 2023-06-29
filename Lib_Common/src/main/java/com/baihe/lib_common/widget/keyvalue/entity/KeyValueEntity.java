package com.baihe.lib_common.widget.keyvalue.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class KeyValueEntity implements Serializable {

    /**
     * 标题
     */
    @SerializedName("name")
    private String key;
    /**
     * 内容（用于显示）
     */
    @SerializedName("value")
    private String val;

    private String type;

    private List<KeyValueEntity> option;

    /**
     * 用于参数提交
     */
    @SerializedName("defaultValue")
    private String defaultVal;
    /**
     * 是否必填
     */
    @SerializedName("is_true")
    private String optional;
    /**
     * 是否显示
     */
    @SerializedName("is_open")
    private String showStatus;

    /**
     * 是否可编辑
     */
    @SerializedName("is_channge")
    private String editable;


    /**
     * 本地标记手机号
     */
    private String phone;
    /**
     * 本地标记微信号
     */
    private String wechat;

    /**
     * 本地标记渠道id
     */
    private String channelId;


    /**
     * 本地标记rangeMin
     */
    private String rangeMin;
    /**
     * 本地标记rangeMax
     */
    private String rangeMax;

    /**
     * 行为交互
     */
    private KeyValEventEntity event;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
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

    public boolean isReadOnly() {
        return !"2".equals(editable);
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

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public KeyValEventEntity getEvent() {
        if (event == null) {
            event = convertToKeyValEvent();
        }
        return event;
    }

    public void setEvent(KeyValEventEntity event) {
        this.event = event;
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

    private KeyValEventEntity convertToKeyValEvent(){
        if (TextUtils.isEmpty(type))
            return null;
        KeyValEventEntity keyValEventEntity = new KeyValEventEntity();
        keyValEventEntity.setAction(type);
        keyValEventEntity.setOptions(option);
        return keyValEventEntity;
    }
}
