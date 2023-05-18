package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-03
 * Description：
 */
public class CustomerSalesEntity {
    private String id;
    private String follow_time;
    private String contact_way;
    private String follow_type;
    private String type;
    private List<KeyValueEntity> show_array;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollow_time() {
        return follow_time;
    }

    public void setFollow_time(String follow_time) {
        this.follow_time = follow_time;
    }

    public String getContact_way() {
        return contact_way;
    }

    public void setContact_way(String contact_way) {
        this.contact_way = contact_way;
    }

    public String getFollow_type() {
        return follow_type;
    }

    public void setFollow_type(String follow_type) {
        this.follow_type = follow_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

}
