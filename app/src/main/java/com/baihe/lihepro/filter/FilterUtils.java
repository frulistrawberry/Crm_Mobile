package com.baihe.lihepro.filter;

import android.content.Context;

import com.baihe.lihepro.filter.entity.FilterEntity;

import java.util.ArrayList;

/**
 * Author：xubo
 * Time：2020-02-22
 * Description：
 */
public class FilterUtils {
    public static void filter(Context context, ArrayList<FilterEntity> entities, FilterCallback callback) {
        FilterManager.newInstance().addObserver(callback);
        FilterActivity.start(context, entities, callback.getFilterValueMap(), callback.getTag());
    }
}
