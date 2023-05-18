package com.baihe.lihepro.entity;

/**
 * Author：xubo
 * Time：2020-09-02
 * Description：
 */
public class AuthHistoryEntity {
    private String audit_time;
    private String remark;
    private String status_txt;
    private String status;
    private String user_name;
    private String preferential_amount;

    public String getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreferential_amount() {
        return preferential_amount;
    }

    public void setPreferential_amount(String preferential_amount) {
        this.preferential_amount = preferential_amount;
    }
}
