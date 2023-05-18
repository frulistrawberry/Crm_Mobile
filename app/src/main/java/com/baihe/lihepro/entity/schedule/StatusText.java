package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;

public class StatusText implements Serializable {
    private String text;
    private String textColor;
    private String bgColor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
