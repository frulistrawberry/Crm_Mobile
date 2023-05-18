package com.baihe.lihepro.entity.schedule;

public class BookActive {
    private String content;
    private String dateTime;

    public BookActive(String content, String dateTime) {
        this.content = content;
        this.dateTime = dateTime;
    }

    public BookActive() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
