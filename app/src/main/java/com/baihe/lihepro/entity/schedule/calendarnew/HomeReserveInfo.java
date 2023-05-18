package com.baihe.lihepro.entity.schedule.calendarnew;

import java.io.Serializable;
import java.util.Objects;

public class HomeReserveInfo implements Serializable {
    private int isMulti;
    private String reserve_num;
    private String saleName;
    private String end_date;
    private String start_date;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(int isMulti) {
        this.isMulti = isMulti;
    }

    public String getReserve_num() {
        return reserve_num;
    }

    public void setReserve_num(String reserve_num) {
        this.reserve_num = reserve_num;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeReserveInfo that = (HomeReserveInfo) o;
        return isMulti == that.isMulti && Objects.equals(reserve_num, that.reserve_num) && Objects.equals(saleName, that.saleName) && Objects.equals(end_date, that.end_date) && Objects.equals(start_date, that.start_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isMulti, reserve_num, saleName, end_date, start_date);
    }
}
