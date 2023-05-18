package com.baihe.lihepro.manager;

import android.text.TextUtils;

import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-08-09
 * Description：
 */
public class InputEditManager extends Observable {
    public static class INSTANCE {
        private static final InputEditManager inputEditManager = new InputEditManager();
    }

    public static InputEditManager newInstance() {
        return InputEditManager.INSTANCE.inputEditManager;
    }

    private InputEditManager() {

    }

    public void notifyObservers(String tag, String content) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        setChanged();
        String[] values = new String[2];
        values[0] = tag;
        values[1] = content;
        notifyObservers(values);
    }
}
