package com.baihe.lib_common.ui.dialog

import android.content.Context
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import com.baihe.lib_common.R
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ViewExt.click
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import java.util.*

object DateDialogUtils {
    fun createDatePickerView(context: Context,title:String="请选择日期",onTimeSelectListener: OnTimeSelectListener):TimePickerView{
        var timePickerView:TimePickerView? = null
        val builder = TimePickerBuilder(context,onTimeSelectListener).apply {
            setContentTextSize(18)
            setOutSideCancelable(true)
            isCyclic(false)
            setBgColor(context.color(R.color.COLOR_FFFFFF))
            setLabel("年","月","日","时","分","秒")
            setType(booleanArrayOf(true,true,true,false,false,false))
            isCenterLabel(false)
            setLayoutRes(R.layout.common_pickerview_time){
                val titleTv = it.findViewById<TextView>(R.id.tv_title)
                val cancelBtn = it.findViewById<ImageView>(R.id.btn_cancel)
                val submitBtn = it.findViewById<TextView>(R.id.btn_submit)
                titleTv.text = title
                cancelBtn.click {
                    timePickerView?.dismiss()
                }
                submitBtn.click {
                    timePickerView?.returnData()
                    timePickerView?.dismiss()
                }
            }
        }
        timePickerView = builder.build()

        return timePickerView
    }

    fun createDateTimePickerView(context: Context,title:String="请选择时间",onTimeSelectListener: OnTimeSelectListener):TimePickerView{
        var timePickerView:TimePickerView? = null
        val builder = TimePickerBuilder(context,onTimeSelectListener).apply {
            setContentTextSize(18)
            setOutSideCancelable(true)
            isCyclic(false)
            setBgColor(context.color(R.color.COLOR_FFFFFF))
            setLabel("年","月","日","时","分","秒")
            setType(booleanArrayOf(true,true,true,true,true,true))
            isCenterLabel(false)
            setLayoutRes(R.layout.common_pickerview_time){
                val titleTv = it.findViewById<TextView>(R.id.tv_title)
                val cancelBtn = it.findViewById<ImageView>(R.id.btn_cancel)
                val submitBtn = it.findViewById<TextView>(R.id.btn_submit)
                titleTv.text = title
                cancelBtn.click {
                    timePickerView?.dismiss()
                }
                submitBtn.click {
                    timePickerView?.returnData()
                    timePickerView?.dismiss()
                }
            }
        }
        timePickerView = builder.build()

        return timePickerView
    }
}



