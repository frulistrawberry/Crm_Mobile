package com.baihe.lib_common.widget.keyvalue.entity;

import java.io.Serializable;

public class KeyValueEntity implements Serializable {
    private String key;
    private String val;
    private KeyValEventEntity event;


    public KeyValEventEntity getEvent() {
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
}
