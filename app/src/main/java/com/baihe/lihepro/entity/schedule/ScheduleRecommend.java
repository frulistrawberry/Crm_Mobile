package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;
import java.util.List;

public class ScheduleRecommend implements Serializable {
    private List<String> date;
    private List<HallItem> halls;

    public ScheduleRecommend(List<String> dates, List<HallItem> halls) {
        this.date = dates;
        this.halls = halls;
    }

    public ScheduleRecommend() {
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> dates) {
        this.date = dates;
    }

    public List<HallItem> getHalls() {
        return halls;
    }

    public void setHalls(List<HallItem> halls) {
        this.halls = halls;
    }
}
