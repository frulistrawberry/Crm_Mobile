package com.baihe.lihepro.entity;

import java.io.Serializable;

public class PayCodeCreateEntity implements Serializable {
    private String qrCode;
    private String name;
    private Double plan_money;
    private Object order_id;
    private String family;
    private String wedding_date;
    private String hotel;
    private String is_finsh_txt;
    private String is_finsh;
    private String logo_url;
    private String company_name;
    private String category_name;
    private String create_time;
    private String icon_url;
    private String short_url;
    private String receivables_id;

    public String getReceivables_id() {
        return receivables_id;
    }

    public void setReceivables_id(String receivables_id) {
        this.receivables_id = receivables_id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPlan_money() {
        return plan_money;
    }

    public void setPlan_money(Double plan_money) {
        this.plan_money = plan_money;
    }

    public Object getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Object order_id) {
        this.order_id = order_id;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(String wedding_date) {
        this.wedding_date = wedding_date;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getIs_finsh_txt() {
        return is_finsh_txt;
    }

    public void setIs_finsh_txt(String is_finsh_txt) {
        this.is_finsh_txt = is_finsh_txt;
    }

    public String getIs_finsh() {
        return is_finsh;
    }

    public void setIs_finsh(String is_finsh) {
        this.is_finsh = is_finsh;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }
}
