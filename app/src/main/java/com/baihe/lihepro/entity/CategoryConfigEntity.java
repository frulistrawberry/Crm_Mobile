package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-11
 * Description：
 */
public class CategoryConfigEntity {
    private String val;
    private String paramKey;
    private List<KeyValueEntity> category_item;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public List<KeyValueEntity> getCategory_item() {
        return category_item;
    }

    public void setCategory_item(List<KeyValueEntity> category_item) {
        this.category_item = category_item;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
}
