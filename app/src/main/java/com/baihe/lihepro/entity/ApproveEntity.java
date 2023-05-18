package com.baihe.lihepro.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-28
 * Description：
 */
public class ApproveEntity implements Comparable<ApproveEntity>{
    private String customer_id;
    private String status;
    private String audit_time;
    private String aId;
    private String status_txt;
    private String audit_color;
    private String title;
    private KeyValueEntity amount;
    private String dateTime;
    private String content;
    private String approveStatus;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    private KeyValueEntity schedule;
    private List<KeyValueEntity> show_array;
    private String audit_type;//1 合同 2 预留 3 预定

    public String getAudit_type() {
        return audit_type;
    }

    public void setAudit_type(String audit_type) {
        this.audit_type = audit_type;
    }


    public KeyValueEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(KeyValueEntity schedule) {
        this.schedule = schedule;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public String getAudit_color() {
        return audit_color;
    }

    public void setAudit_color(String audit_color) {
        this.audit_color = audit_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public KeyValueEntity getAmount() {
        return amount;
    }

    public void setAmount(KeyValueEntity amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    @Override
    public int compareTo(ApproveEntity approveEntity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateTime);
            Date date1 = sdf.parse(approveEntity.getDateTime());
            return date.compareTo(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
