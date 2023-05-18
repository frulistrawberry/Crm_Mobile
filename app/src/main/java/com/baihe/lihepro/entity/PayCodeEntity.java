package com.baihe.lihepro.entity;

import java.util.List;

public class PayCodeEntity {
    private long receivables_id;
    private String system_no;
    private String user_name;
    private String create_time;
    private String order_num;
    private long order_id;
    private String receivables_status;
    private String check_status;
    private String check_status_txt;
    private String customer_name;
    private String contract_num;
    private String money;
    private String receivables_type;
    private String contract_amount;
    private String portrait_auth;
    private String receivables_plan;
    private String contract_already_receivables;
    private String contract_no_receivables;
    private String receivables_rate;
    private String payment_status;
    private List<KeyValueEntity> show_array;

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    public long getReceivables_id() {
        return receivables_id;
    }

    public void setReceivables_id(int receivables_id) {
        this.receivables_id = receivables_id;
    }

    public String getSystem_no() {
        return system_no;
    }

    public void setSystem_no(String system_no) {
        this.system_no = system_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getReceivables_status() {
        return receivables_status;
    }

    public void setReceivables_status(String receivables_status) {
        this.receivables_status = receivables_status;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public String getCheck_status_txt() {
        return check_status_txt;
    }

    public void setCheck_status_txt(String check_status_txt) {
        this.check_status_txt = check_status_txt;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getContract_num() {
        return contract_num;
    }

    public void setContract_num(String contract_num) {
        this.contract_num = contract_num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getReceivables_type() {
        return receivables_type;
    }

    public void setReceivables_type(String receivables_type) {
        this.receivables_type = receivables_type;
    }

    public String getContract_amount() {
        return contract_amount;
    }

    public void setContract_amount(String contract_amount) {
        this.contract_amount = contract_amount;
    }

    public String getPortrait_auth() {
        return portrait_auth;
    }

    public void setPortrait_auth(String portrait_auth) {
        this.portrait_auth = portrait_auth;
    }

    public String getReceivables_plan() {
        return receivables_plan;
    }

    public void setReceivables_plan(String receivables_plan) {
        this.receivables_plan = receivables_plan;
    }

    public String getContract_already_receivables() {
        return contract_already_receivables;
    }

    public void setContract_already_receivables(String contract_already_receivables) {
        this.contract_already_receivables = contract_already_receivables;
    }

    public String getContract_no_receivables() {
        return contract_no_receivables;
    }

    public void setContract_no_receivables(String contract_no_receivables) {
        this.contract_no_receivables = contract_no_receivables;
    }

    public String getReceivables_rate() {
        return receivables_rate;
    }

    public void setReceivables_rate(String receivables_rate) {
        this.receivables_rate = receivables_rate;
    }
}
