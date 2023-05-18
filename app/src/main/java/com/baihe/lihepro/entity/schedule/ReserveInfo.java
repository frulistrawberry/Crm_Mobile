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
public class ReserveInfo {

    private String reserveId;
    private String dateTime;
    private String submitter;
    private String endDate;
    public void setReserveId(String reserveId) {
         this.reserveId = reserveId;
     }
     public String getReserveId() {
         return reserveId;
     }

    public void setDateTime(String dateTime) {
         this.dateTime = dateTime;
     }
     public String getDateTime() {
         return dateTime;
     }

    public void setSubmitter(String submitter) {
         this.submitter = submitter;
     }
     public String getSubmitter() {
         return submitter;
     }

    public void setEndDate(String endDate) {
         this.endDate = endDate;
     }
     public String getEndDate() {
         return endDate;
     }

}