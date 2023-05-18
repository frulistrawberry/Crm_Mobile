package com.baihe.lihepro.entity;

public class PayCodeCustomerEntity {
    private long customer_id;
    private String customer_name;
    private String phone;
    private int similar_id;
    private int repeat_audit_status;
    private String wedding_date;
    private String repeat_status;

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
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

    public int getSimilar_id() {
        return similar_id;
    }

    public void setSimilar_id(int similar_id) {
        this.similar_id = similar_id;
    }

    public int getRepeat_audit_status() {
        return repeat_audit_status;
    }

    public void setRepeat_audit_status(int repeat_audit_status) {
        this.repeat_audit_status = repeat_audit_status;
    }

    public String getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(String wedding_date) {
        this.wedding_date = wedding_date;
    }

    public String getRepeat_status() {
        return repeat_status;
    }

    public void setRepeat_status(String repeat_status) {
        this.repeat_status = repeat_status;
    }
}
