package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContactDataEntity {
    private String contract_id;
    private String agreement_num;
    private String category;
    private ContactTitleEntity title;
    private List<KeyValueEntity> show_array;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public ContactTitleEntity getTitle() {
        return title;
    }

    public void setTitle(ContactTitleEntity title) {
        this.title = title;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    public String getAgreement_num() {
        return agreement_num;
    }

    public void setAgreement_num(String agreement_num) {
        this.agreement_num = agreement_num;
    }
}

