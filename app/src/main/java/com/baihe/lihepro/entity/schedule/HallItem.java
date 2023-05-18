package com.baihe.lihepro.entity.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HallItem implements Serializable,Comparable<HallItem> {
    private int id;
    private String name;
    private HallBookStatus hallBookStatus;
    private int lunchStatus = -1;
    private int dinnerStatus = -1;
    private boolean isSelect;
    private String hallId;
    private int startLunchStatus;
    private int endLunchStatus;
    private int startDinnerStatus;
    private int endDinnerStatus;
    private boolean isOk;
    private boolean isEmpty;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
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

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public HallItem(String hallName, HallBookStatus hallBookStatus) {
        this.name = hallName;
        this.hallBookStatus = hallBookStatus;
    }

    public HallItem() {
    }

    public HallItem(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HallBookStatus getHallBookStatus() {
        return hallBookStatus;
    }

    public void setHallBookStatus(HallBookStatus hallBookStatus) {
        this.hallBookStatus = hallBookStatus;
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

    @Override
    public int compareTo(HallItem hallItem) {
        return 0;
    }
}
