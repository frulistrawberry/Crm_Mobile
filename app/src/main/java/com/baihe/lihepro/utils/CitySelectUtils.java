package com.baihe.lihepro.utils;

import android.content.Context;

import com.baihe.lihepro.activity.CitySelectActivity;
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.manager.CitySelectManager;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-08-09
 * Description：
 */
public class CitySelectUtils {

    public static void select(Context context, CitySelectCallback callback) {
        CitySelectManager.newInstance().addObserver(callback);
        CitySelectActivity.start(context, callback.getTag());
    }

    public static void select(Context context, String title, CitySelectCallback callback) {
        CitySelectManager.newInstance().addObserver(callback);
        CitySelectActivity.start(context, title, callback.getTag());
    }

    public static void select(Context context, String title, String cityCode, CitySelectCallback callback) {
        CitySelectManager.newInstance().addObserver(callback);
        CitySelectActivity.start(context, title, cityCode, callback.getTag());
    }

    public static abstract class CitySelectCallback implements Observer {
        private String tag;

        public CitySelectCallback() {
            this.tag = UUID.randomUUID().toString();
        }

        public String getTag() {
            return tag;
        }

        @Override
        public void update(Observable o, Object arg) {
            //确定是自己通知
            if (arg instanceof CityEntity) {
                CityEntity cityEntity = (CityEntity) arg;
                call(cityEntity);
            }
            //一次性，使用完立即注销观察者
            o.deleteObserver(this);
        }

        /**
         * 选择的城市
         *
         * @param cityEntity
         */
        public abstract void call(CityEntity cityEntity);

    }
}
