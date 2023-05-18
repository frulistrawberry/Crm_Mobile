package com.baihe.lihepro.entity.schedule.calendarnew;

import java.io.Serializable;

public class ReserveHallInfo implements Serializable {

    private String name;
    private String hallId;
    private boolean isOK;
    private int startLunchStatus;
    private int endLunchStatus;
    private int startDinnerStatus;
    private int endDinnerStatus;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }

    public int getStartLunchStatus() {
        return startLunchStatus;
    }

    public void setStartLunchStatus(int startLunchStatus) {
        this.startLunchStatus = startLunchStatus;
    }

    public int getEndLunchStatus() {
        return endLunchStatus;
    }

    public void setEndLunchStatus(int endLunchStatus) {
        this.endLunchStatus = endLunchStatus;
    }

    public int getStartDinnerStatus() {
        return startDinnerStatus;
    }

    public void setStartDinnerStatus(int startDinnerStatus) {
        this.startDinnerStatus = startDinnerStatus;
    }

    public int getEndDinnerStatus() {
        return endDinnerStatus;
    }

    public void setEndDinnerStatus(int endDinnerStatus) {
        this.endDinnerStatus = endDinnerStatus;
    }
}
