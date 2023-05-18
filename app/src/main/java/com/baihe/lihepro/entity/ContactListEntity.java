package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContactListEntity {
    private List<List<KeyValueEntity>> show_array;
    private List<KeyValueEntity> new_config;

    public List<List<KeyValueEntity>> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<List<KeyValueEntity>> show_array) {
        this.show_array = show_array;
    }

    public List<KeyValueEntity> getNew_config() {
        return new_config;
    }

    public void setNew_config(List<KeyValueEntity> new_config) {
        this.new_config = new_config;
    }
}

