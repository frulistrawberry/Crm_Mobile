package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;

public class HallBookStatus implements Serializable {
    private int lunchStatus = -1;
    private int dinnerStatus = -1;
    private int startDinnerStatus = -1;
    private int startLunchStatus = -1;
    private int endLunchStatus = -1;
    private int endDinnerStatus = -1;

    public HallBookStatus(int startDinnerStatus, int startLunchStatus, int endLunchStatus, int endDinnerStatus) {
        this.startDinnerStatus = startDinnerStatus;
        this.startLunchStatus = startLunchStatus;
        this.endLunchStatus = endLunchStatus;
        this.endDinnerStatus = endDinnerStatus;
    }

    public HallBookStatus(int lunchStatus, int dinnerStatus) {
        this.lunchStatus = lunchStatus;
        this.dinnerStatus = dinnerStatus;
    }
    public HallBookStatus() {
    }

    public int getStartDinnerStatus() {
        return startDinnerStatus;
    }

    public void setStartDinnerStatus(int startDinnerStatus) {
        this.startDinnerStatus = startDinnerStatus;
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

    public int getEndDinnerStatus() {
        return endDinnerStatus;
    }

    public void setEndDinnerStatus(int endDinnerStatus) {
        this.endDinnerStatus = endDinnerStatus;
    }

    public int getLunchStatus() {
        return lunchStatus;
    }

    public void setLunchStatus(int lunchStatus) {
        this.lunchStatus = lunchStatus;
    }

    public int getDinnerStatus() {
        return dinnerStatus;
    }

    public void setDinnerStatus(int dinnerStatus) {
        this.dinnerStatus = dinnerStatus;
    }
}
