package com.baihe.lihepro.filter.entity;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class FilterRegionEntity {
    public String code;
    public String name;
    public ArrayList<FilterRegionEntity> children;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FilterRegionEntity) {
            FilterRegionEntity other = (FilterRegionEntity) obj;
            if (other.code.equals(this.code)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
