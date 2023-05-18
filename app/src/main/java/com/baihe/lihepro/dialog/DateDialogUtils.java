package com.baihe.lihepro.dialog;

import android.content.Context;

import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.baihe.lihepro.view.PickerOptions1;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.util.Calendar;


public class DateDialogUtils {

    public static LiheTimePickerBuilder createPickerViewBuilder(Context context, OnTimeSelectListener onTimeSelectListener) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR,  endCalendar.get(Calendar.YEAR)+5);
        LiheTimePickerBuilder pvTime;
        pvTime = new LiheTimePickerBuilder(context, onTimeSelectListener)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(0xff030303)//标题文字颜色
                .setSubmitColor(0xff2db4e6)//确定按钮文字颜色
                .setCancelColor(0xffc5c5ce)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setRangDate(Calendar.getInstance(),endCalendar)
                .setType(new boolean[]{true, true, true, false, false, false});
        return pvTime;
    }

    public static LiheTimePickerBuilder createPickerViewBuilder1(Context context, PickerOptions1.onScheduleSelectListener listener) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR,  endCalendar.get(Calendar.YEAR)+5);
        LiheTimePickerBuilder pvTime;
        pvTime = new LiheTimePickerBuilder(context, listener)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)
                .setListener(listener)
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(0xff030303)//标题文字颜色
                .setSubmitColor(0xff2db4e6)//确定按钮文字颜色
                .setCancelColor(0xffc5c5ce)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setRangDate(Calendar.getInstance(),endCalendar)
                .setType(new boolean[]{true, true, true, false, false, false});
        return pvTime;
    }



    public static LiheTimePickerBuilder createPickerViewBuilder(Context context,Calendar endCalendar, OnTimeSelectListener onTimeSelectListener) {
        LiheTimePickerBuilder pvTime;
        pvTime = new LiheTimePickerBuilder(context, onTimeSelectListener)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(0xff030303)//标题文字颜色
                .setSubmitColor(0xff2db4e6)//确定按钮文字颜色
                .setCancelColor(0xffc5c5ce)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setRangDate(Calendar.getInstance(),endCalendar)
                .setType(new boolean[]{true, true, true, false, false, false});
        return pvTime;
    }


    public static LiheTimePickerBuilder createPickerViewBuilderNoRangDate(Context context, OnTimeSelectListener onTimeSelectListener) {
        LiheTimePickerBuilder pvTime;
        pvTime = new LiheTimePickerBuilder(context, onTimeSelectListener)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(0xff030303)//标题文字颜色
                .setSubmitColor(0xfff096b4)//确定按钮文字颜色
                .setCancelColor(0xffc5c5ce)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setType(new boolean[]{true, true, true, false, false, false});
        return pvTime;
    }
}
