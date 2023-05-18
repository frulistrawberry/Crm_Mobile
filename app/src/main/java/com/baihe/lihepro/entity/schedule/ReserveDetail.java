package com.baihe.lihepro.entity.schedule;

import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.entity.ButtonTypeEntity;

import java.io.Serializable;
import java.util.List;

public class ReserveDetail implements Serializable {
    private StatusText statusText;
    private ScheduleInfo scheduleInfo;
    private ScheduleInfo newScheduleInfo;
    private CustomerInfo customerInfo;
    private PayInfo payInfo;
    private ReserveInfo reserveInfo;
    private List<ApproveEntity> approveInfo;
    private List<ButtonTypeEntity> buttonType;
    private ContractInfo contarctInfo;
    private BookInfo bookInfo;

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public ContractInfo getContarctInfo() {
        return contarctInfo;
    }

    public void setContarctInfo(ContractInfo contarctInfo) {
        this.contarctInfo = contarctInfo;
    }

    public StatusText getStatusText() {
        return statusText;
    }

    public void setStatusText(StatusText statusText) {
        this.statusText = statusText;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public ReserveInfo getReserveInfo() {
        return reserveInfo;
    }

    public void setReserveInfo(ReserveInfo reserveInfo) {
        this.reserveInfo = reserveInfo;
    }

    public List<ApproveEntity> getApproveInfo() {
        return approveInfo;
    }

    public void setApproveInfo(List<ApproveEntity> approveInfo) {
        this.approveInfo = approveInfo;
    }

    public List<ButtonTypeEntity> getButtonType() {
        return buttonType;
    }

    public void setButtonType(List<ButtonTypeEntity> buttonType) {
        this.buttonType = buttonType;
    }

    public ScheduleInfo getNewScheduleInfo() {
        return newScheduleInfo;
    }

    public void setNewScheduleInfo(ScheduleInfo newScheduleInfo) {
        this.newScheduleInfo = newScheduleInfo;
    }
}
