package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-23
 * Description：
 */
public class RequirementTabEntity {
    private String paramKey;
    private List<KeyValueEntity> list;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public List<KeyValueEntity> getList() {
        return list;
    }

    public void setList(List<KeyValueEntity> list) {
        this.list = list;
    }
}
