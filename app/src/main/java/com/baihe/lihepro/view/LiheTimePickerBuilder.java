package com.baihe.lihepro.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.CrmApp;
import com.baihe.lihepro.R;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;

import java.util.Calendar;

/**
 * Created by xiaosongzeem on 2018/3/20.
 */

public class LiheTimePickerBuilder {

    private PickerOptions mPickerOptions;
    private TimePickerView timePickerView;
    private com.baihe.lihepro.view.TimePickerView timePickerView1;
    //Required
    public LiheTimePickerBuilder(Context context, OnTimeSelectListener listener) {
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.context = context;
        mPickerOptions.timeSelectListener = listener;
    }

    public LiheTimePickerBuilder(Context context, PickerOptions1.onScheduleSelectListener listener) {
        mPickerOptions = new PickerOptions1(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.context = context;
        ((PickerOptions1)mPickerOptions).onScheduleSelectListener = listener;
    }

    //Option
    public LiheTimePickerBuilder setGravity(int gravity) {
        mPickerOptions.textGravity = gravity;
        return this;
    }

    public LiheTimePickerBuilder addOnCancelClickListener(View.OnClickListener cancelListener) {
        mPickerOptions.cancelListener = cancelListener;
        return this;
    }

    /**
     * new boolean[]{true, true, true, false, false, false}
     * control the "year","month","day","hours","minutes","seconds " display or hide.
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
     *
     * @param type 布尔型数组，长度需要设置为6。
     * @return TimePickerBuilder
     */
    public LiheTimePickerBuilder setType(boolean[] type) {
        mPickerOptions.type = type;
        return this;
    }

    public LiheTimePickerBuilder setSubmitText(String textContentConfirm) {
        mPickerOptions.textContentConfirm = textContentConfirm;
        return this;
    }

    public LiheTimePickerBuilder isDialog(boolean isDialog) {
        mPickerOptions.isDialog = isDialog;
        return this;
    }

    public LiheTimePickerBuilder setCancelText(String textContentCancel) {
        mPickerOptions.textContentCancel = textContentCancel;
        return this;
    }

    public LiheTimePickerBuilder setListener(PickerOptions1.onScheduleSelectListener listener) {
        ((PickerOptions1)mPickerOptions).onScheduleSelectListener = listener;
        return this;
    }

    public LiheTimePickerBuilder setTitleText(String textContentTitle) {
        mPickerOptions.textContentTitle = textContentTitle;
        return this;
    }

    public LiheTimePickerBuilder setSubmitColor(int textColorConfirm) {
        mPickerOptions.textColorConfirm = textColorConfirm;
        return this;
    }

    public LiheTimePickerBuilder setCancelColor(int textColorCancel) {
        mPickerOptions.textColorCancel = textColorCancel;
        return this;
    }

    /**
     * ViewGroup 类型的容器
     *
     * @param decorView 选择器会被添加到此容器中
     * @return TimePickerBuilder
     */
    public LiheTimePickerBuilder setDecorView(ViewGroup decorView) {
        mPickerOptions.decorView = decorView;
        return this;
    }

    public LiheTimePickerBuilder setBgColor(int bgColorWheel) {
        mPickerOptions.bgColorWheel = bgColorWheel;
        return this;
    }

    public LiheTimePickerBuilder setTitleBgColor(int bgColorTitle) {
        mPickerOptions.bgColorTitle = bgColorTitle;
        return this;
    }

    public LiheTimePickerBuilder setTitleColor(int textColorTitle) {
        mPickerOptions.textColorTitle = textColorTitle;
        return this;
    }

    public LiheTimePickerBuilder setSubCalSize(int textSizeSubmitCancel) {
        mPickerOptions.textSizeSubmitCancel = textSizeSubmitCancel;
        return this;
    }

    public LiheTimePickerBuilder setTitleSize(int textSizeTitle) {
        mPickerOptions.textSizeTitle = textSizeTitle;
        return this;
    }

    public LiheTimePickerBuilder setContentTextSize(int textSizeContent) {
        mPickerOptions.textSizeContent = textSizeContent;
        return this;
    }

    /**
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     *
     * @param date
     * @return TimePickerBuilder
     */
    public LiheTimePickerBuilder setDate(Calendar date) {
        mPickerOptions.date = date;
        return this;
    }

    public LiheTimePickerBuilder setLayoutRes(int res, CustomListener customListener) {
        mPickerOptions.layoutRes = res;
        mPickerOptions.customListener = customListener;
        return this;
    }


    /**
     * 设置起始时间
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     */

    public LiheTimePickerBuilder setRangDate(Calendar startDate, Calendar endDate) {
        mPickerOptions.startDate = startDate;
        mPickerOptions.endDate = endDate;
        return this;
    }


    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public LiheTimePickerBuilder setLineSpacingMultiplier(float lineSpacingMultiplier) {
        mPickerOptions.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */

    public LiheTimePickerBuilder setDividerColor(@ColorInt int dividerColor) {
        mPickerOptions.dividerColor = dividerColor;
        return this;
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public LiheTimePickerBuilder setDividerType(WheelView.DividerType dividerType) {
        mPickerOptions.dividerType = dividerType;
        return this;
    }

    /**
     * {@link #setOutSideColor} instead.
     *
     * @param backgroundId color resId.
     */
    @Deprecated
    public LiheTimePickerBuilder setBackgroundId(int backgroundId) {
        mPickerOptions.outSideColor = backgroundId;
        return this;
    }

    /**
     * 显示时的外部背景色颜色,默认是灰色
     *
     * @param outSideColor
     */
    public LiheTimePickerBuilder setOutSideColor(@ColorInt int outSideColor) {
        mPickerOptions.outSideColor = outSideColor;
        return this;
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public LiheTimePickerBuilder setTextColorCenter(@ColorInt int textColorCenter) {
        mPickerOptions.textColorCenter = textColorCenter;
        return this;
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public LiheTimePickerBuilder setTextColorOut(@ColorInt int textColorOut) {
        mPickerOptions.textColorOut = textColorOut;
        return this;
    }

    public LiheTimePickerBuilder isCyclic(boolean cyclic) {
        mPickerOptions.cyclic = cyclic;
        return this;
    }

    public LiheTimePickerBuilder setOutSideCancelable(boolean cancelable) {
        mPickerOptions.cancelable = cancelable;
        return this;
    }

    public LiheTimePickerBuilder setLunarCalendar(boolean lunarCalendar) {
        mPickerOptions.isLunarCalendar = lunarCalendar;
        return this;
    }


    public LiheTimePickerBuilder setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        mPickerOptions.label_year = label_year;
        mPickerOptions.label_month = label_month;
        mPickerOptions.label_day = label_day;
        mPickerOptions.label_hours = label_hours;
        mPickerOptions.label_minutes = label_mins;
        mPickerOptions.label_seconds = label_seconds;
        return this;
    }

    /**
     * 设置X轴倾斜角度[ -90 , 90°]
     *
     * @param x_offset_year    年
     * @param x_offset_month   月
     * @param x_offset_day     日
     * @param x_offset_hours   时
     * @param x_offset_minutes 分
     * @param x_offset_seconds 秒
     * @return
     */
    public LiheTimePickerBuilder setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
                                                int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {
        mPickerOptions.x_offset_year = x_offset_year;
        mPickerOptions.x_offset_month = x_offset_month;
        mPickerOptions.x_offset_day = x_offset_day;
        mPickerOptions.x_offset_hours = x_offset_hours;
        mPickerOptions.x_offset_minutes = x_offset_minutes;
        mPickerOptions.x_offset_seconds = x_offset_seconds;
        return this;
    }

    public LiheTimePickerBuilder isCenterLabel(boolean isCenterLabel) {
        mPickerOptions.isCenterLabel = isCenterLabel;
        return this;
    }

    /**
     * @param listener 切换item项滚动停止时，实时回调监听。
     * @return
     */
    public LiheTimePickerBuilder setTimeSelectChangeListener(OnTimeSelectChangeListener listener) {
        mPickerOptions.timeSelectChangeListener = listener;
        return this;
    }

    public TimePickerView build() {
        timePickerView = new TimePickerView(mPickerOptions){
            @Override
            public void show() {
                super.show();
                setBackgroundResId();
            }
        };
        return timePickerView;
    }

    public com.baihe.lihepro.view.TimePickerView build1() {
        timePickerView1 = new com.baihe.lihepro.view.TimePickerView(mPickerOptions){
            @Override
            public void show() {
                super.show();
                setBackgroundResId1();
            }
        };
        return timePickerView1;
    }

    private void setBackgroundResId(){
        if(timePickerView.getDialogContainerLayout() != null){
            View view = timePickerView.getDialogContainerLayout().findViewById(com.bigkoo.pickerview.R.id.rv_topbar);
            if(view != null){
                view.setBackgroundResource(R.drawable.time_picker_white_bg);
                view.setPadding(0, CommonUtils.dp2pxForInt(CrmApp.getInstance(),15f),0,CommonUtils.dp2pxForInt(CrmApp.getInstance(),5f));
            }
        }
    }
    private void setBackgroundResId1(){
        if(timePickerView1.getDialogContainerLayout() != null){
            View view = timePickerView1.getDialogContainerLayout().findViewById(com.bigkoo.pickerview.R.id.rv_topbar);
            if(view != null){
                view.setBackgroundResource(R.drawable.time_picker_white_bg);
                view.setPadding(0, CommonUtils.dp2pxForInt(CrmApp.getInstance(),15f),0,CommonUtils.dp2pxForInt(CrmApp.getInstance(),5f));
            }
        }
    }
}
