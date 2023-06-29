package com.baihe.lib_home

import android.graphics.Typeface
import com.baihe.lib_common.entity.StatusText
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.utils.ResUtils
import com.baihe.lib_home.ui.widget.chart.PercentValueFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.model.GradientColor
import com.google.gson.annotations.SerializedName


data class DataEntity(
    val cancelOrder: Int,
    @SerializedName("customeCount")
    val customerCount: Int,
    @SerializedName("customeUnValid")
    val customerUnValid: Int,
    @SerializedName("customeValid")
    val customerValid: Int,
    @SerializedName("customeWait")
    val customerWait: Int,
    val exitOrder: Int,
    @SerializedName("finshOrder")
    val finishOrder: Int,
    val openInvitation: Int,
    val oppoCount: Int,
    val order: Int,
    val orderCount: Int,
    val orderWait: Int,
    @SerializedName("signCustomeCount")
    val signCustomerCount: Int,
    val signSum: String,
    val successInvitation: Int,
    val waitInvitation: Int,
){
    fun isOppoDataNotEmpty():Boolean{
        return waitInvitation+successInvitation+
                openInvitation+customerValid+customerUnValid+
                customerWait>0
    }

    fun isOrderDataNotEmpty():Boolean{
        return orderWait+order+cancelOrder+exitOrder+finishOrder>0
    }

    fun generateOpportunityData(): PieData {
        val colors = mutableListOf<Int>().apply {
            add(ResUtils.getColorFromResource(R.color.home_pie_line_1))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_2))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_3))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_4))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_5))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_6))
        }

        val gradientColor = mutableListOf<GradientColor>().apply {
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_1),
                ResUtils.getColorFromResource(R.color.home_pie_end_1)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_2),
                ResUtils.getColorFromResource(R.color.home_pie_end_2)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_3),
                ResUtils.getColorFromResource(R.color.home_pie_end_3)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_4),
                ResUtils.getColorFromResource(R.color.home_pie_end_4)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_5),
                ResUtils.getColorFromResource(R.color.home_pie_end_5)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_6),
                ResUtils.getColorFromResource(R.color.home_pie_end_6)))
        }

        val pieEntityList = mutableListOf<PieEntry>().apply {
            add(PieEntry(successInvitation.toFloat(),"邀约成功"))
            add(PieEntry(customerWait.toFloat(),"客户待定"))
            add(PieEntry(customerUnValid.toFloat(),"客户无效"))
            add(PieEntry(waitInvitation.toFloat(),"待邀约"))
            add(PieEntry(customerValid.toFloat(),"客户有效"))
            add(PieEntry(openInvitation.toFloat(),"已到店"))
        }

        val pieDataSet = PieDataSet(pieEntityList,"").apply {
            sliceSpace = 0f; //设置饼状Item之间的间隙
            selectionShift = 0f; //设置饼状Item被选中时变化的距离
            this.colors = colors; //为DataSet中的数据匹配上颜色集(饼图Item颜色)
            gradientColors = gradientColor
            valueFormatter =
                PercentValueFormatter()
            valueLinePart1Length = 0.47f
            valueLinePart2Length = 0.7f
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            isValueLineVariableLength = true
            valueLinePart1OffsetPercentage = 50f
            isUsingSliceColorAsValueLineColor = true
        }
        val pieData = PieData(pieDataSet).apply {
            setDrawValues(true) //设置是否显示数据实体(百分比，true:以下属性才有意义)
            setValueTextColor(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C)) //设置所有DataSet内数据实体（百分比）的文本颜色
            setValueTextSize(16f) //设置所有DataSet内数据实体（百分比）的文本字体大小
            setValueTypeface(Typeface.DEFAULT) //设置所有DataSet内数据实体（百分比）的文本字体样式
        }

        return pieData


    }

    fun generateOrderData(): PieData {
        val colors = mutableListOf<Int>().apply {
            add(ResUtils.getColorFromResource(R.color.home_pie_line_1))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_2))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_7))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_5))
            add(ResUtils.getColorFromResource(R.color.home_pie_line_4))
        }

        val gradientColor = mutableListOf<GradientColor>().apply {
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_1),
                ResUtils.getColorFromResource(R.color.home_pie_end_1)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_2),
                ResUtils.getColorFromResource(R.color.home_pie_end_2)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_7),
                ResUtils.getColorFromResource(R.color.home_pie_end_7)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_5),
                ResUtils.getColorFromResource(R.color.home_pie_end_5)))
            add(GradientColor(ResUtils.getColorFromResource(R.color.home_pie_start_4),
                ResUtils.getColorFromResource(R.color.home_pie_end_4)))
        }

        val pieEntityList = mutableListOf<PieEntry>().apply {
            add(PieEntry(order.toFloat(),"已签约"))
            add(PieEntry(finishOrder.toFloat(),"已完成"))
            add(PieEntry(exitOrder.toFloat(),"已退单"))
            add(PieEntry(cancelOrder.toFloat(),"已取消"))
            add(PieEntry(orderWait.toFloat(),"待签约"))
        }

        val pieDataSet = PieDataSet(pieEntityList,"").apply {
            sliceSpace = 0f; //设置饼状Item之间的间隙
            selectionShift = 0f; //设置饼状Item被选中时变化的距离
            this.colors = colors; //为DataSet中的数据匹配上颜色集(饼图Item颜色)
            gradientColors = gradientColor
            valueFormatter =
                PercentValueFormatter()
            valueLinePart1Length = 0.47f
            valueLinePart2Length = 0.7f
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            isValueLineVariableLength = true
            valueLinePart1OffsetPercentage = 50f
            isUsingSliceColorAsValueLineColor = true
            isAutomaticallyDisableSliceSpacingEnabled
        }

        val pieData = PieData(pieDataSet).apply {
            setDrawValues(true) //设置是否显示数据实体(百分比，true:以下属性才有意义)
            setValueTextColor(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C)) //设置所有DataSet内数据实体（百分比）的文本颜色
            setValueTextSize(16f) //设置所有DataSet内数据实体（百分比）的文本字体大小
            setValueTypeface(Typeface.DEFAULT) //设置所有DataSet内数据实体（百分比）的文本字体样式
        }

        return pieData
    }
}

data class WaitingEntity(
    @SerializedName("arrival_time")
    val arrivalTime: String,
    val category: String,
    @SerializedName("category_txt")
    val categoryTxt: String,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("follow_txt")
    val followTxt: String,
    val id: Int,
    val name: String,
    @SerializedName("next_contact_time")
    val nextContactTime: String,
    val phone: String,
    val type: Int,
    val tag:String

){
    fun toStatusText():StatusText{
        val statusText = StatusText()
        when(type){
            //新机会
            1 ->{
                statusText.text = "新机会"
                statusText.textColor = "#6CB643"
                statusText.bgColor = "#298AFF00"
            }
            //待跟进
            2->{
                statusText.text = "待跟进"
                statusText.textColor = "#E08B01"
                statusText.bgColor = "#1FFFB600"
            }
            //新订单
            3->{
                statusText.text = "新订单"
                statusText.textColor = "#677EFF"
                statusText.bgColor = "#1A6984FF"
            }
            //已逾期
            4->{
                statusText.text = "已逾期"
                statusText.textColor = "#F11E1E"
                statusText.bgColor = "#14FF2C2C"
            }
        }
        return statusText
    }

    fun showArray():MutableList<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.add(KeyValueEntity().apply {
            key = "联系电话"
            `val` = phone
        })
        if ("2"==tag || "4"==tag){
            showArray.add(KeyValueEntity().apply {
                key = "下次回访时间"
                `val` = nextContactTime
            })
        }
        if ("3"==tag || "5"==tag){
            showArray.add(KeyValueEntity().apply {
                key = "计划到店时间"
                `val` = arrivalTime
            })
        }
        if ("1"!=tag){
            showArray.add(KeyValueEntity().apply {
                key = "最新沟通记录"
                `val` = followTxt
            })
        }
        return showArray
    }
}

data class HomeEntity(
    val waitingEntity:List<WaitingEntity>?,
    val dataEntity: DataEntity?
    )