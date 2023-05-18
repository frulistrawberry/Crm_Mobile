package com.baihe.lihepro.entity.schedule.calendarnew;

import com.baihe.lihepro.entity.schedule.BookInfo;

import java.io.Serializable;
import java.util.Objects;

public class HomeHallItem implements Serializable {
    private String hallName;
    private HomeReserveInfo lunch;
    private HomeReserveInfo dinner;

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    private boolean isEmpty;

    public HomeHallItem() {
    }

    public HomeHallItem(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public HomeReserveInfo getLunch() {
        return lunch;
    }

    public void setLunch(HomeReserveInfo lunch) {
        this.lunch = lunch;
    }

    public HomeReserveInfo getDinner() {
        return dinner;
    }

    public void setDinner(HomeReserveInfo dinner) {
        this.dinner = dinner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeHallItem that = (HomeHallItem) o;
        return isEmpty == that.isEmpty && Objects.equals(hallName, that.hallName) && Objects.equals(lunch, that.lunch) && Objects.equals(dinner, that.dinner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hallName, lunch, dinner, isEmpty);
    }
}
