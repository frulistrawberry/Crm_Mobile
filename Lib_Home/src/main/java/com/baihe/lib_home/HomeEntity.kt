package com.baihe.lib_home

import android.graphics.Color
import android.graphics.Typeface
import com.baihe.lib_common.entity.StatusText
import com.baihe.lib_common.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.utils.ResUtils
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.gson.annotations.SerializedName


data class DataEntity(
    val cancelOrder: Int,
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
    val signSum: Int,
    val successInvitation: Int,
    val waitInvitation: Int
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
        val colors = mutableListOf<Int>()
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_6C8EFF))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_C5C5CE))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_C2C5DB))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_33FFFFFF))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_F6F7FC))
        val pieEntityList = mutableListOf<PieEntry>()
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        val pieDataSet = PieDataSet(pieEntityList,"")
        pieDataSet.sliceSpace = 0f; //设置饼状Item之间的间隙
        pieDataSet.selectionShift = 10f; //设置饼状Item被选中时变化的距离
        pieDataSet.colors = colors; //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true) //设置是否显示数据实体(百分比，true:以下属性才有意义)

        pieData.setValueTextColor(Color.BLUE) //设置所有DataSet内数据实体（百分比）的文本颜色

        pieData.setValueTextSize(12f) //设置所有DataSet内数据实体（百分比）的文本字体大小

        pieData.setValueTypeface(Typeface.DEFAULT) //设置所有DataSet内数据实体（百分比）的文本字体样式

        pieData.setValueFormatter(PercentFormatter()) //设置所有DataSet内数据实体（百分比）的文本字体格式
        return pieData


    }

    fun generateOrderData(): PieData {
        val colors = mutableListOf<Int>()
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_6C8EFF))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_C5C5CE))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_C2C5DB))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_33FFFFFF))
        colors.add(ResUtils.getColorFromResource(R.color.COLOR_F6F7FC))
        val pieEntityList = mutableListOf<PieEntry>()
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        pieEntityList.add(PieEntry(1f,""))
        val pieDataSet = PieDataSet(pieEntityList,"")
        pieDataSet.sliceSpace = 3f; //设置饼状Item之间的间隙
        pieDataSet.selectionShift = 10f; //设置饼状Item被选中时变化的距离
        pieDataSet.colors = colors; //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true) //设置是否显示数据实体(百分比，true:以下属性才有意义)

        pieData.setValueTextColor(Color.BLUE) //设置所有DataSet内数据实体（百分比）的文本颜色

        pieData.setValueTextSize(12f) //设置所有DataSet内数据实体（百分比）的文本字体大小

        pieData.setValueTypeface(Typeface.DEFAULT) //设置所有DataSet内数据实体（百分比）的文本字体样式

        pieData.setValueFormatter(PercentFormatter()) //设置所有DataSet内数据实体（百分比）的文本字体格式
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
    @SerializedName("nextContactTime")
    val next_contact_time: String,
    val phone: String,
    val type: Int,
    @SerializedName("type_txt")
    val typeTxt: String
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
        showArray.add(KeyValueEntity().apply {
            key = "计划到店时间"
            `val` = arrivalTime
        })
        showArray.add(KeyValueEntity().apply {
            key = "最新沟通记录"
            `val` = followTxt
        })
        return showArray
    }
}

data class HomeEntity(
    val waitingEntity:List<WaitingEntity>?,
    val dataEntity: DataEntity?
    )