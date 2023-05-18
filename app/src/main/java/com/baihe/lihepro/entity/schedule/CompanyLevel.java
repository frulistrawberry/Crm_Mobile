package com.baihe.lihepro.entity.schedule;

import java.io.Serializable;

public class CompanyLevel implements Serializable {
    private int levelLimit;

    public int getLevelLimit() {
        return levelLimit;
    }

    public void setLevelLimit(int levelLimit) {
        this.levelLimit = levelLimit;
    }
}
