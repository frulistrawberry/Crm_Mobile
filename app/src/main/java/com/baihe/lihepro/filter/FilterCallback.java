package com.baihe.lihepro.filter;

import com.baihe.lihepro.filter.entity.FilterObserverEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-02-22
 * Description：
 */
public abstract class FilterCallback implements Observer {
    private String tag;
    private HashMap<String, Object> filterValueMap;
    private Map<String, Object> requestMap;

    public FilterCallback() {
        this.tag = UUID.randomUUID().toString();
        this.filterValueMap = new HashMap<>();
        this.requestMap = new HashMap<>();
        this.requestMap.put("filter", this.filterValueMap);
    }

    public String getTag() {
        return tag;
    }

    public HashMap<String, Object> getFilterValueMap() {
        return filterValueMap;
    }

    @Override
    public void update(Observable o, Object arg) {
        //确定是自己通知
        if (arg instanceof FilterObserverEntity) {
            FilterObserverEntity filterObserverEntity = (FilterObserverEntity) arg;
            if (tag.equals(filterObserverEntity.tag)) {
                this.filterValueMap.clear();
                this.filterValueMap.putAll(filterObserverEntity.filterValueMap);
                call(requestMap);
            }
        }
        //一次性，使用完立即注销观察者
        o.deleteObserver(this);
    }

    /**
     * 筛选回调
     *
     * @param filterValueMap
     */
    public abstract void call(Map<String, Object> filterValueMap);

}
