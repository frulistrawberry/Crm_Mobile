package com.baihe.lihepro.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.contrarywind.view.WheelView;

import java.util.Calendar;
import java.util.Date;

/**
 * Build Options
 * Created by xiaosongzeem on 2018/3/8.
 */

public class PickerOptions1 extends com.bigkoo.pickerview.configure.PickerOptions {
    public onScheduleSelectListener onScheduleSelectListener;

    public interface onScheduleSelectListener{
        void onScheduleSelect(String schedule, Date date,View v);
    }

    public void setOnScheduleSelectListener(PickerOptions1.onScheduleSelectListener onScheduleSelectListener) {
        this.onScheduleSelectListener = onScheduleSelectListener;
    }

    public PickerOptions1(int buildType) {
        super(buildType);
    }
}
