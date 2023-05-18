package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-11
 * Description：
 */
public class ProtocolEntity {
    private KeyValueEntity agr_num;
    private String agreement_old_num;
    private List<KeyValueEntity> new_config;
    private List<List<KeyValueEntity>> show_array;

    public KeyValueEntity getAgr_num() {
        return agr_num;
    }

    public void setAgr_num(KeyValueEntity agr_num) {
        this.agr_num = agr_num;
    }

    public String getAgreement_old_num() {
        return agreement_old_num;
    }

    public void setAgreement_old_num(String agreement_old_num) {
        this.agreement_old_num = agreement_old_num;
    }

    public List<KeyValueEntity> getNew_config() {
        return new_config;
    }

    public void setNew_config(List<KeyValueEntity> new_config) {
        this.new_config = new_config;
    }

    public List<List<KeyValueEntity>> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<List<KeyValueEntity>> show_array) {
        this.show_array = show_array;
    }
}
