package com.baihe.lihepro.entity.schedule;

import android.annotation.SuppressLint;

import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.List;

public class ScheduleDate implements Serializable {
    private int level;
    private String date;
    private LocalDate localDate;
    private int status;
    private List<HomeHallItem> hallInfo;

    public ScheduleDate() {
    }

    public ScheduleDate(int level, String date, int status) {
        this.level = level;
        this.date = date;
        this.status = status;
    }

    public List<HomeHallItem> getHallInfo() {
        return hallInfo;
    }

    public void setHallInfo(List<HomeHallItem> hallInfo) {
        this.hallInfo = hallInfo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public LocalDate getLocalDate() {
        return new LocalDate(date);
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
