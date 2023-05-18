package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-11
 * Description：
 */
public class CustomerEditEntity {
    private List<KeyValueEntity> base;
    private KeyValueEntity category;
    private List<CategoryConfigEntity> category_data;

    public List<KeyValueEntity> getBase() {
        return base;
    }

    public void setBase(List<KeyValueEntity> base) {
        this.base = base;
    }

    public KeyValueEntity getCategory() {
        return category;
    }

    public void setCategory(KeyValueEntity category) {
        this.category = category;
    }

    public List<CategoryConfigEntity> getCategory_data() {
        return category_data;
    }

    public void setCategory_data(List<CategoryConfigEntity> category_data) {
        this.category_data = category_data;
    }

    public List<KeyValueEntity> getCategoryItem(String val) {
        if (category_data != null && val != null) {
            for (CategoryConfigEntity categoryConfigEntity : category_data) {
                if (val.equals(categoryConfigEntity.getVal())) {
                    return categoryConfigEntity.getCategory_item();
                }
            }
        }
        return null;
    }

    public String getCategoryItemParamKey(String val) {
        if (category_data != null && val != null) {
            for (CategoryConfigEntity categoryConfigEntity : category_data) {
                if (val.equals(categoryConfigEntity.getVal())) {
                    return categoryConfigEntity.getParamKey();
                }
            }
        }
        return null;
    }
}
