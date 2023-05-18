package com.baihe.lihepro.manager;

import android.text.TextUtils;

import com.baihe.lihepro.entity.CityEntity;

import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-08-09
 * Description：
 */
public class CitySelectManager extends Observable {
    public static class INSTANCE {
        private static final CitySelectManager citySelectManager = new CitySelectManager();
    }

    public static CitySelectManager newInstance() {
        return CitySelectManager.INSTANCE.citySelectManager;
    }

    private CitySelectManager() {

    }

    public void notifyObservers(String tag, CityEntity cityEntity) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        setChanged();
        cityEntity.setTag(tag);
        notifyObservers(cityEntity);
    }
}
