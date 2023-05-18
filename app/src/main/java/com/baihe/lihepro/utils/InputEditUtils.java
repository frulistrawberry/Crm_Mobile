package com.baihe.lihepro.utils;

import android.content.Context;
import android.text.InputFilter;

import com.baihe.common.util.StringUtils;
import com.baihe.lihepro.activity.InputEditActivity;
import com.baihe.lihepro.manager.InputEditManager;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-08-09
 * Description：
 */
public class InputEditUtils {
    public static final String FILTER_CHINESE = "FILTER_CHINESE";

    public static void input(Context context, String title, InputEditCallback callback) {
        InputEditManager.newInstance().addObserver(callback);
        InputEditActivity.start(context, title, callback.getTag());
    }

    public static void input(Context context, String title, String content, String inputFilter,InputEditCallback callback) {
        InputEditManager.newInstance().addObserver(callback);
        InputEditActivity.start(context, title,null, content,callback.getTag(), inputFilter);
    }

    public static void input(Context context, String title, String hint, String content,String inputFilter, InputEditCallback callback) {
        InputEditManager.newInstance().addObserver(callback);
        InputEditActivity.start(context, title, hint, content, callback.getTag(),inputFilter);
    }

    public static abstract class InputEditCallback implements Observer {
        private String tag;

        public InputEditCallback() {
            this.tag = UUID.randomUUID().toString();
        }

        public String getTag() {
            return tag;
        }

        @Override
        public void update(Observable o, Object arg) {
            //确定是自己通知
            if (arg instanceof String[]) {
                String[] values = (String[]) arg;
                if (values.length == 2 && tag.equals(values[0])) {
                    call(values[1]);
                }
            }
            //一次性，使用完立即注销观察者
            o.deleteObserver(this);
        }

        /**
         * 修改后的内容
         *
         * @param content
         */
        public abstract void call(String content);

    }


    public static InputFilter getInputFilter(String filter){
        InputFilter inputFilter = null;

        switch (filter){
            case FILTER_CHINESE:
                inputFilter = (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        if (!StringUtils.isChinese(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                };
                break;
        }
        return inputFilter;
    }
}
