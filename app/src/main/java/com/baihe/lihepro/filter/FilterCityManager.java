package com.baihe.lihepro.filter;

import com.baihe.lihepro.filter.entity.FilterRegionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class FilterCityManager extends Observable {
    private List<FilterRegionEntity> all = new ArrayList<>();
    private List<FilterRegionEntity> select = new ArrayList<>();
    private FilterRegionEntity tabFilterRegionEntity;

    public FilterCityManager(List<FilterRegionEntity> all, List<FilterRegionEntity> select) {
        if (all != null) {
            this.all.addAll(all);
        }
        if (select != null) {
            this.select.addAll(select);
        }
    }

    public void setTabFilterRegionEntity(FilterRegionEntity tabFilterRegionEntity) {
        this.tabFilterRegionEntity = tabFilterRegionEntity;
        setChanged();
        notifyObservers(true);
    }

    public void rest() {
        select.clear();
        tabFilterRegionEntity = null;
        setChanged();
        notifyObservers(true);
    }

    public FilterRegionEntity getTabFilterRegionEntity() {
        return tabFilterRegionEntity;
    }

    public void update() {
        setChanged();
        notifyObservers();
    }

    public List<FilterRegionEntity> getAll() {
        return all;
    }

    public List<FilterRegionEntity> getSelect() {
        return select;
    }
}

