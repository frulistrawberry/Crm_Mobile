package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;
import java.util.List;

public class PayInfo implements Serializable {

    private int showType;
    private List<PayItem> payInfo;

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public List<PayItem> getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(List<PayItem> payInfo) {
        this.payInfo = payInfo;
    }
}
