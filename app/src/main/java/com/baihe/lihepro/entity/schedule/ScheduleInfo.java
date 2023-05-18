/**
  * Copyright 2022 bejson.com 
  */
package com.baihe.lihepro.entity.schedule;
import java.util.Date;

/**
 * Auto-generated: 2022-02-21 14:50:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ScheduleInfo {

    private String date;
    private int sType;
    private String hallName;
    private String hallId;
    private String contract_id;
    private boolean isNow;
    private int end_type;
    private int start_type;
    private String start_date;
    private String end_date;
    private int isMulti;

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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(int isMulti) {
        this.isMulti = isMulti;
    }

    public boolean isNow() {
        return isNow;
    }

    public void setNow(boolean now) {
        isNow = now;
    }

    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setSType(int sType) {
         this.sType = sType;
     }
     public int getSType() {
         return sType;
     }


    public void setContract_id(String contract_id) {
         this.contract_id = contract_id;
     }
     public String getContract_id() {
         return contract_id;
     }


    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }
}