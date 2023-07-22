package com.baihe.lib_common.ui.dialog

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.baihe.lib_common.R
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ViewExt.click
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView

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

    @JvmStatic
    @JvmOverloads
    fun createDateTimePickerView(context: Context,title:String="请选择时间",onTimeSelectListener: OnTimeSelectListener,dateFormatter:String?=null):TimePickerView{
        var timePickerView:TimePickerView? = null
        val builder = TimePickerBuilder(context,onTimeSelectListener).apply {
            setContentTextSize(18)
            setOutSideCancelable(true)
            isCyclic(false)
            setBgColor(context.color(R.color.COLOR_FFFFFF))
            setLabel("年","月","日","时","分","秒")
            if (dateFormatter.isNullOrEmpty())
                setType(booleanArrayOf(true,true,true,true,true,true))
            else
                setType(getTimeTypeArray(dateFormatter))
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

     fun getTimeTypeArray(format: String?): BooleanArray {
        if (format == null) {
            return booleanArrayOf(true, true, true, false, false, false)
        }
        return if (format.contains("ss")) {  //年月日时分秒
            booleanArrayOf(true, true, true, true, true, true)
        } else if (format.contains("mm")) { //年月日时分
            booleanArrayOf(true, true, true, true, true, false)
        } else if (format.contains("HH") || format.contains("hh")) { //年月日时
            booleanArrayOf(true, true, true, true, false, false)
        } else if (format.contains("dd")) { //年月日
            booleanArrayOf(true, true, true, false, false, false)
        } else { //年月日
            booleanArrayOf(true, true, true, false, false, false)
        }
    }

}



