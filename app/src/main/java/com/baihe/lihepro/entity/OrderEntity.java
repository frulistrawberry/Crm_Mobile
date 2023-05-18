package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-01
 * Description：
 */
public class OrderEntity {
    private String customer_id;
    private String order_id;
    private String order_num;
    private String category;
    private String category_text;
    private String owner_id;
    private String sale_name;
    private String kefu_name;
    private String customer_name;
    private String arrival_status;
    private String req_id;
    private List<ButtonTypeEntity> button_type;
    private KeyValueEntity wedding_date;
    private List<KeyValueEntity> show_data;
    private List<KeyValueEntity> important_data;
    private List<KeyValueEntity> update_data;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
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

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getSale_name() {
        return sale_name;
    }

    public void setSale_name(String sale_name) {
        this.sale_name = sale_name;
    }

    public String getKefu_name() {
        return kefu_name;
    }

    public void setKefu_name(String kefu_name) {
        this.kefu_name = kefu_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getArrival_status() {
        return arrival_status;
    }

    public void setArrival_status(String arrival_status) {
        this.arrival_status = arrival_status;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public List<ButtonTypeEntity> getButton_type() {
        return button_type;
    }

    public void setButton_type(List<ButtonTypeEntity> button_type) {
        this.button_type = button_type;
    }

    public KeyValueEntity getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(KeyValueEntity wedding_date) {
        this.wedding_date = wedding_date;
    }

    public List<KeyValueEntity> getShow_data() {
        return show_data;
    }

    public void setShow_data(List<KeyValueEntity> show_data) {
        this.show_data = show_data;
    }

    public List<KeyValueEntity> getImportant_data() {
        return important_data;
    }

    public void setImportant_data(List<KeyValueEntity> important_data) {
        this.important_data = important_data;
    }

    public List<KeyValueEntity> getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(List<KeyValueEntity> update_data) {
        this.update_data = update_data;
    }
}
