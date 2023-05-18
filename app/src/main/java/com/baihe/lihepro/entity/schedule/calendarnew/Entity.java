package com.baihe.lihepro.entity.schedule.calendarnew;

import java.io.Serializable;
import java.util.List;

public class Entity implements Serializable {


    List<ReserveHallInfo> hallinfo;
    boolean isOk;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public List<ReserveHallInfo> getHallinfo() {
        return hallinfo;
    }

    public void setHallinfo(List<ReserveHallInfo> hallinfo) {
        this.hallinfo = hallinfo;
    }
}
