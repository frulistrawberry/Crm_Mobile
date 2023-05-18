package com.baihe.lihepro.manager;

import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-09-09
 * Description：
 */
public class HomeRefreshManager extends Observable {
    public static class INSTANCE {
        private static final HomeRefreshManager homeRefreshManager = new HomeRefreshManager();
    }

    public static HomeRefreshManager newInstance() {
        return HomeRefreshManager.INSTANCE.homeRefreshManager;
    }

    private HomeRefreshManager() {

    }

    public void refresh() {
        setChanged();
        notifyObservers(true);
    }

    public void subscribe(RefreshCallback callback){
        addObserver(callback);
    }

    public static abstract class RefreshCallback implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (arg != null && arg instanceof Boolean) {
                boolean isRefresh = (boolean) arg;
                if (isRefresh) {
                    refresh();
                }
            }
        }

        public abstract void refresh();

    }

}
