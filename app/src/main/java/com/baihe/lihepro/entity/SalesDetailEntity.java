package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-01
 * Description：
 */
public class SalesDetailEntity {
    private int reserveConfirmCount;
    private String reserveId;
    private OrderEntity orderInfo;
    private List<KeyValueEntity> customerData;
    private List<ContactEntity> contactUserData;
    private List<ContactDataEntity> contractInfo;
    private List<ContactDataEntity> agreementInfo;
    private List<KeyValueEntity> followData;
    private List<ButtonTypeEntity> buttonType;
    private List<ButtonTypeEntity> customerButtonType;

    public int getReserveConfirmCount() {
        return reserveConfirmCount;
    }

    public void setReserveConfirmCount(int reserveConfirmCount) {
        this.reserveConfirmCount = reserveConfirmCount;
    }

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public OrderEntity getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderEntity orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<KeyValueEntity> getCustomerData() {
        return customerData;
    }

    public void setCustomerData(List<KeyValueEntity> customerData) {
        this.customerData = customerData;
    }

    public List<ContactEntity> getContactUserData() {
        return contactUserData;
    }

    public void setContactUserData(List<ContactEntity> contactUserData) {
        this.contactUserData = contactUserData;
    }

    public List<ContactDataEntity> getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(List<ContactDataEntity> agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public List<KeyValueEntity> getFollowData() {
        return followData;
    }

    public void setFollowData(List<KeyValueEntity> followData) {
        this.followData = followData;
    }

    public List<ContactDataEntity> getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(List<ContactDataEntity> contractInfo) {
        this.contractInfo = contractInfo;
    }

    public List<ButtonTypeEntity> getButtonType() {
        return buttonType;
    }

    public void setButtonType(List<ButtonTypeEntity> buttonType) {
        this.buttonType = buttonType;
    }

    public List<ButtonTypeEntity> getCustomerButtonType() {
        return customerButtonType;
    }

    public void setCustomerButtonType(List<ButtonTypeEntity> customerButtonType) {
        this.customerButtonType = customerButtonType;
    }
}
