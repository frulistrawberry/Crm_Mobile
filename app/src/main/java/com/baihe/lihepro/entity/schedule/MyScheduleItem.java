package com.baihe.lihepro.entity.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyScheduleItem implements Serializable {

    private String hallName;
    private String date;
    private String id;
    private int sType;
    private String customerName;
    private String status;
    private String endTime;
    private String bookTime;
    private StatusText textInfo;
    private String end_date;
    private String start_date;
    private int end_type;
    private int start_type;
    private int isMulti;

    public StatusText getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(StatusText textInfo) {
        this.textInfo = textInfo;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getEnd_type() {
        return end_type;
    }

    public void setEnd_type(int end_type) {
        this.end_type = end_type;
    }

    public int getStart_type() {
        return start_type;
    }

    public void setStart_type(int start_type) {
        this.start_type = start_type;
    }

    public int getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(int isMulti) {
        this.isMulti = isMulti;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }
}
