package com.baihe.common.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.baihe.common.base.BaseApplication;
import com.baihe.common.util.CommonUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-03-01
 * Description：
 */
public class BackgroundManager {
    private static class BackgroundManagerHolder {
        private static BackgroundManager backgroundManager = new BackgroundManager();
    }

    private Context context;
    private boolean isBackground;
    private List<BackgroundObserver> observerList = new ArrayList<>();

    private BackgroundManager() {
        this.isBackground = false;
        this.context = BaseApplication.getInstance();
    }

    public static BackgroundManager newInstance() {
        return BackgroundManagerHolder.backgroundManager;
    }

    public void activityStart() {
        boolean isBackground = !CommonUtils.isAppOnForeground(context);
        if (this.isBackground != isBackground) {
            this.isBackground = isBackground;
            observer();
        }
    }

    public void activityStop() {
        boolean isBackground = !CommonUtils.isAppOnForeground(context);
        if (this.isBackground != isBackground) {
            this.isBackground = isBackground;
            observer();
        }
    }

    private void observer() {
        for (int i = 0; i < observerList.size(); i++) {
            final BackgroundObserver backgroundObserver = observerList.get(i);
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    backgroundObserver.change(isBackground);
                }
            });
        }
    }

    /**
     * 订阅后台通知
     *
     * @param observer
     */
    public void subscribe(BackgroundObserver observer) {
        observerList.add(observer);
    }

    public void unBind(BackgroundObserver observer){
        observerList.remove(observer);
    }

    /**
     * app是否在后台
     *
     * @return
     */
    public boolean isBackground() {
        return isBackground;
    }

    /**
     * 后台改变接收者
     */
    public interface BackgroundObserver {
        void change(boolean isBackground);
    }

    public static Activity getCurrentActivity () {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
