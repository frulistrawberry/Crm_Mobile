package com.baihe.lihepro.filter;

import com.baihe.lihepro.filter.entity.FilterObserverEntity;

import java.util.Map;
import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-02-22
 * Description：
 */
public class FilterManager extends Observable {

    public static class INSTANCE {
        private static final FilterManager filterManager = new FilterManager();
    }

    public static FilterManager newInstance() {
        return INSTANCE.filterManager;
    }

    private FilterManager() {

    }

    public void notifyObservers(Map<String, Object> filterValueMap, String tag) {
        setChanged();
        FilterObserverEntity filterObserverEntity = new FilterObserverEntity();
        filterObserverEntity.filterValueMap = filterValueMap;
        filterObserverEntity.tag = tag;
        notifyObservers(filterObserverEntity);
    }

}
