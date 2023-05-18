package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-02
 * Description：
 */
public class ConstactDetailEntity {
    private String customer_name;
    private String customer_id;
    private String create_name;
    private String create_time;
    private String order_num;
    private String order_id;
    private String audit_status;
    private String audit_status_text;
    private String audit_color;
    private KeyValueEntity wedding_date;
    private List<KeyValueEntity> show_array;
    private List<List<KeyValueEntity>> product_info;
    private List<KeyValueEntity> customer_info;
    private List<KeyValueEntity> plan;
    private List<List<KeyValueEntity>> discount_info;
    private List<KeyValueEntity> important_data;
    private List<AuthHistoryEntity> auth_history;
    private List<ButtonTypeEntity> button_type;
    private String preferential_amount;
    private String sign_amount;
    private List<String> contract_pic;

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getAudit_status_text() {
        return audit_status_text;
    }

    public void setAudit_status_text(String audit_status_text) {
        this.audit_status_text = audit_status_text;
    }

    public String getAudit_color() {
        return audit_color;
    }

    public void setAudit_color(String audit_color) {
        this.audit_color = audit_color;
    }

    public KeyValueEntity getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(KeyValueEntity wedding_date) {
        this.wedding_date = wedding_date;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    public List<List<KeyValueEntity>> getProduct_info() {
        return product_info;
    }

    public void setProduct_info(List<List<KeyValueEntity>> product_info) {
        this.product_info = product_info;
    }

    public List<KeyValueEntity> getCustomer_info() {
        return customer_info;
    }

    public void setCustomer_info(List<KeyValueEntity> customer_info) {
        this.customer_info = customer_info;
    }

    public List<KeyValueEntity> getImportant_data() {
        return important_data;
    }

    public void setImportant_data(List<KeyValueEntity> important_data) {
        this.important_data = important_data;
    }

    public List<AuthHistoryEntity> getAuth_history() {
        return auth_history;
    }

    public void setAuth_history(List<AuthHistoryEntity> auth_history) {
        this.auth_history = auth_history;
    }

    public List<ButtonTypeEntity> getButton_type() {
        return button_type;
    }

    public void setButton_type(List<ButtonTypeEntity> button_type) {
        this.button_type = button_type;
    }

    public List<List<KeyValueEntity>> getDiscount_info() {
        return discount_info;
    }

    public void setDiscount_info(List<List<KeyValueEntity>> discount_info) {
        this.discount_info = discount_info;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPreferential_amount() {
        return preferential_amount;
    }

    public void setPreferential_amount(String preferential_amount) {
        this.preferential_amount = preferential_amount;
    }

    public String getSign_amount() {
        return sign_amount;
    }

    public void setSign_amount(String sign_amount) {
        this.sign_amount = sign_amount;
    }

    public List<String> getContract_pic() {
        return contract_pic;
    }

    public void setContract_pic(List<String> contract_pic) {
        this.contract_pic = contract_pic;
    }

    public List<KeyValueEntity> getPlan() {
        return plan;
    }

    public void setPlan(List<KeyValueEntity> plan) {
        this.plan = plan;
    }
}
