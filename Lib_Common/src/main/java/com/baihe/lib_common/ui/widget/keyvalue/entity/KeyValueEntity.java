package com.baihe.lib_common.ui.widget.keyvalue.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class KeyValueEntity implements Serializable {

    /**
     * 标题
     */
    private String name;
    /**
     *用于参数提交
     */
    private String value;
    private String label;

    private String pid;

    private List<KeyValueEntity> children;
    private String type;

    private List<KeyValueEntity> option;

    /**
     * 内容（用于显示）
     */
    private String defaultValue;
    /**
     * 是否必填
     */
    private String is_true;
    /**
     * 是否显示
     */
    private String is_open;

    /**
     * 是否可编辑
     */
    private String is_channge;

    private String paramKey;

    private String subParamKey;

    private String subValue;

    private String subDefaultValue;

    private String dateFormatter;

    public String getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(String dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public String getSubParamKey() {
        return subParamKey;
    }

    public void setSubParamKey(String subParamKey) {
        this.subParamKey = subParamKey;
    }

    public String getSubValue() {
        return subValue;
    }

    public void setSubValue(String subValue) {
        this.subValue = subValue;
    }

    public String getSubDefaultValue() {
        return subDefaultValue;
    }

    public void setSubDefaultValue(String subDefaultValue) {
        this.subDefaultValue = subDefaultValue;
    }

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

    private String order_id;

    //---------------------------------KeyValueLayout-------------------//

    private String key;

    private String val;

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

    /**
     * 点击事件
     */
    private String action;
    /**
     * 文字按钮（用于KeyValueLayout）
     */
    private String text;
    /**
     * 图标按钮（用于KeyValueLayout）
     */
    private String icon;

    /**
     * 附件
     */
    private List<String> attach;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getAttach() {
        return attach;
    }

    public void setAttach(List<String> attach) {
        this.attach = attach;
    }

    //---------------------------------KeyValueLayout-------------------//


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_true() {
        return is_true;
    }

    public void setIs_true(String is_true) {
        this.is_true = is_true;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getIs_channge() {
        return is_channge;
    }

    public void setIs_channge(String is_channge) {
        this.is_channge = is_channge;
    }

    public List<KeyValueEntity> getOption() {
        return option;
    }

    public void setOption(List<KeyValueEntity> option) {
        this.option = option;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<KeyValueEntity> getChildren() {
        return children;
    }

    public void setChildren(List<KeyValueEntity> children) {
        this.children = children;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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




}
