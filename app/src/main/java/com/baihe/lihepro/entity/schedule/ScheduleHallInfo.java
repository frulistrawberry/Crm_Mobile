package com.baihe.lihepro.entity.schedule;

import com.baihe.lihepro.entity.ButtonTypeEntity;

import java.io.Serializable;
import java.util.List;

public class ScheduleHallInfo implements Serializable {
    private List<BookActive> dynamics;
    private ScheduleRecommend recommend;
    private List<ButtonTypeEntity> buttonType;

    private HallItem thisHallInfo;


    public HallItem getThisHallInfo() {
        return thisHallInfo;
    }

    public void setThisHallInfo(HallItem thisHallInfo) {
        this.thisHallInfo = thisHallInfo;
    }

    public ScheduleHallInfo() {
    }




    public List<ButtonTypeEntity> getButtonType() {
        return buttonType;
    }

    public void setButtonType(List<ButtonTypeEntity> buttonType) {
        this.buttonType = buttonType;
    }

    public ScheduleRecommend getRecommend() {
        return recommend;
    }

    public void setRecommend(ScheduleRecommend recommend) {
        this.recommend = recommend;
    }



    public List<BookActive> getDynamics() {
        return dynamics;
    }

    public void setDynamics(List<BookActive> dynamics) {
        this.dynamics = dynamics;
    }


}
