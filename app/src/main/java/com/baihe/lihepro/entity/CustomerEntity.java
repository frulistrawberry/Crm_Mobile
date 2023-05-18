package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-31
 * Description：
 */
public class CustomerEntity {
    private String name;
    private KeyValueEntity wedding_date;
    private String customer_id;
    private String customer_name;
    private String customer_level;
    private String customer_gender;
    private String phone;
    private String encode_phone;
    private String category;
    private String category_text;
    private String wechat;
    private String group_customer_id;
    private String customerId;
    private List<KeyValueEntity> show_array;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyValueEntity getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(KeyValueEntity wedding_date) {
        this.wedding_date = wedding_date;
    }

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

    public String getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(String customer_level) {
        this.customer_level = customer_level;
    }

    public String getCustomer_gender() {
        return customer_gender;
    }

    public void setCustomer_gender(String customer_gender) {
        this.customer_gender = customer_gender;
    }
}
