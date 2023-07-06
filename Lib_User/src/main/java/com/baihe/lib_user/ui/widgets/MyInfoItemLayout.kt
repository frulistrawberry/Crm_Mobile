package com.baihe.lib_user.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.baihe.lib_user.R

/**
 * @author xukankan
 * @date 2023/7/6 09:30
 * @email：xukankan@jiayuan.com
 * @description：个人资料页面item
 */
class MyInfoItemLayout:ConstraintLayout {
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvContent: AppCompatTextView
    private lateinit var line: View

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init()
    }


    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.user_activity_info_item, this, true)
        tvContent = findViewById(R.id.tv_content)
        tvTitle = findViewById(R.id.tv_title)
        line = findViewById(R.id.line)
    }

    public fun setTvContent(content: String) {
       tvContent.text = content
    }


    public fun setTitle(title: String) {
        tvTitle.text = title
    }


    public fun setLineHidden() {
       line.visibility = View.GONE
    }
}