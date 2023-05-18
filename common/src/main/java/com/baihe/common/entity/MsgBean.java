package com.baihe.common.entity;

import java.io.Serializable;

public class MsgBean implements Serializable {
    public String customer_id;
    public String message_id;
    public String lead_status;

    public MsgBean() {
    }

    public MsgBean(String customer_id, String message_id, String lead_status) {
        this.customer_id = customer_id;
        this.message_id = message_id;
        this.lead_status = lead_status;
    }
}
