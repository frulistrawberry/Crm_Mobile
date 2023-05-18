package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class RequirementEntity {
    private String customer_id;
    private String customer_name;
    private String phone;
    private String encode_phone;
    private String category;
    private String category_text;
    private List<CategoryLabelEntity> category_color;
    private String wechat;
    private String group_customer_id;
    private List<KeyValueEntity> show_array;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEncode_phone() {
        return encode_phone;
    }

    public void setEncode_phone(String encode_phone) {
        this.encode_phone = encode_phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_text() {
        return category_text;
    }

    public void setCategory_text(String category_text) {
        this.category_text = category_text;
    }

    public List<CategoryLabelEntity> getCategory_color() {
        return category_color;
    }

    public void setCategory_color(List<CategoryLabelEntity> category_color) {
        this.category_color = category_color;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getGroup_customer_id() {
        return group_customer_id;
    }

    public void setGroup_customer_id(String group_customer_id) {
        this.group_customer_id = group_customer_id;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }
}
