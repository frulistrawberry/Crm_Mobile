package com.baihe.lib_common.widget.keyvalue.entity;

import java.io.Serializable;

public class KeyValEventEntity implements Serializable {
    private String action;
    private String name;
    private String icon;

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
