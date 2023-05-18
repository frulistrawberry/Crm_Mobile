package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerBaseEntity {
    private List<KeyValueEntity> customerData;
    private List<KeyValueEntity> followData;
    private List<ContactEntity> contactUserData;
    private CustomerEntity customer;
    private List<ButtonTypeEntity> button_type;
    private int showFollowButton;

    public int getShowFollowButton() {
        return showFollowButton;
    }

    public void setShowFollowButton(int showFollowButton) {
        this.showFollowButton = showFollowButton;
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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<KeyValueEntity> getFollowData() {
        return followData;
    }

    public void setFollowData(List<KeyValueEntity> followData) {
        this.followData = followData;
    }

    public List<ButtonTypeEntity> getButton_type() {
        return button_type;
    }

    public void setButton_type(List<ButtonTypeEntity> button_type) {
        this.button_type = button_type;
    }
}

