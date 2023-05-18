package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContactEntity {
    private String contactId;
    private String userName;
    private String identityText;
    private String identityColor;
    private List<KeyValueEntity> info;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityText() {
        return identityText;
    }

    public void setIdentityText(String identityText) {
        this.identityText = identityText;
    }

    public String getIdentityColor() {
        return identityColor;
    }

    public void setIdentityColor(String identityColor) {
        this.identityColor = identityColor;
    }

    public List<KeyValueEntity> getInfo() {
        return info;
    }

    public void setInfo(List<KeyValueEntity> info) {
        this.info = info;
    }
}

