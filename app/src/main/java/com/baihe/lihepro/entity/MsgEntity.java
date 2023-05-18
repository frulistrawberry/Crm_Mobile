package com.baihe.lihepro.entity;

import java.io.Serializable;

public class MsgEntity implements Serializable {

    private String id;
    private String unread;
    private String name;
    private String channel_name;
    private String text;
    private String push_time;
    private String customer_type;
    private String customer_id;
    private String lead_status;
    private String type_txt;

    public String getType_txt() {
        return type_txt;
    }

    public void setType_txt(String type_txt) {
        this.type_txt = type_txt;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getLead_status() {
        return lead_status;
    }

    public void setLead_status(String lead_status) {
        this.lead_status = lead_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }
}
