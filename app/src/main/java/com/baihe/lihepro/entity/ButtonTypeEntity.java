package com.baihe.lihepro.entity;

/**
 * Author：xubo
 * Time：2020-08-03
 * Description：
 */
public class ButtonTypeEntity {
    private int type;
    private String name;
    //1不可用，0可用
    private String disabled;

    public ButtonTypeEntity(int type, String name, String disabled) {
        this.type = type;
        this.name = name;
        this.disabled = disabled;
    }

    public ButtonTypeEntity() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
}
