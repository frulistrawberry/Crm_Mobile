package com.baihe.lib_common.ui.widget.keyvalue.entity;

import java.io.Serializable;
import java.util.List;

public class KeyValEventEntity implements Serializable {
    /**
     * 动作id
     */
    private String action;

    private String paramKey;

    private List<KeyValueEntity> options;

    /**
     * 文字按钮（用于KeyValueLayout）
     */
    private String name;
    /**
     * 图标按钮（用于KeyValueLayout）
     */
    private String icon;

    private List<String> attach;

    private String format = "yyyy-MM-dd";

    public List<String> getAttach() {
        return attach;
    }

    public void setAttach(List<String> attach) {
        this.attach = attach;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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
}
