package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-16
 * Description：
 */
public class CategoryEditEntity {
    private String id;
    private List<KeyValueEntity> category_data;
    private List<KeyValueEntity> other_info;
    private String paramKey;

    public List<KeyValueEntity> getCategory_data() {
        return category_data;
    }

    public void setCategory_data(List<KeyValueEntity> category_data) {
        this.category_data = category_data;
    }

    public List<KeyValueEntity> getOther_info() {
        return other_info;
    }

    public void setOther_info(List<KeyValueEntity> other_info) {
        this.other_info = other_info;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
