package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-03
 * Description：
 */
public class CustomerDemandEntity {
    private String id;
    private List<ButtonTypeEntity> button_type;
    private String req_num;
    private List<CategoryLabelEntity> category_color;
    private List<KeyValueEntity> show_array;
    private String category_id;
    private String category_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CategoryLabelEntity> getCategory_color() {
        return category_color;
    }

    public void setCategory_color(List<CategoryLabelEntity> category_color) {
        this.category_color = category_color;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    public String getReq_num() {
        return req_num;
    }

    public void setReq_num(String req_num) {
        this.req_num = req_num;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<ButtonTypeEntity> getButton_type() {
        return button_type;
    }

    public void setButton_type(List<ButtonTypeEntity> button_type) {
        this.button_type = button_type;
    }
}
