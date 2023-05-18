package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ContractItemEntity {
    private String id;
    private String title;
    private List<KeyValueEntity> other;
    private List<CategoryLabelEntity> category_color;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<KeyValueEntity> getOther() {
        return other;
    }

    public void setOther(List<KeyValueEntity> other) {
        this.other = other;
    }

    public List<CategoryLabelEntity> getCategory_color() {
        return category_color;
    }

    public void setCategory_color(List<CategoryLabelEntity> category_color) {
        this.category_color = category_color;
    }
}
