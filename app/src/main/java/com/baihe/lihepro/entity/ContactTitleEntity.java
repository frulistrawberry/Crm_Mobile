package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContactTitleEntity implements Parcelable {
    private String key;
    private String val;
    private String audit_status;
    private String audit_text;
    private String audit_color;
    private String edit_contract;

    public void setEdit_contract(String edit_contract) {
        this.edit_contract = edit_contract;
    }

    public String getEdit_contract() {
        return edit_contract;
    }

    public ContactTitleEntity() {

    }

    protected ContactTitleEntity(Parcel in) {
        key = in.readString();
        val = in.readString();
        audit_status = in.readString();
        audit_text = in.readString();
        audit_color = in.readString();
        edit_contract = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(val);
        dest.writeString(audit_status);
        dest.writeString(audit_text);
        dest.writeString(audit_color);
        dest.writeString(edit_contract);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactTitleEntity> CREATOR = new Creator<ContactTitleEntity>() {
        @Override
        public ContactTitleEntity createFromParcel(Parcel in) {
            return new ContactTitleEntity(in);
        }

        @Override
        public ContactTitleEntity[] newArray(int size) {
            return new ContactTitleEntity[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getAudit_text() {
        return audit_text;
    }

    public void setAudit_text(String audit_text) {
        this.audit_text = audit_text;
    }

    public String getAudit_color() {
        return audit_color;
    }

    public void setAudit_color(String audit_color) {
        this.audit_color = audit_color;
    }
}

