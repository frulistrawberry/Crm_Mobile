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
 * @date 2023/7/5 18:32
 * @email：xukankan@jiayuan.com
 * @description：我的页面item
 */
class MineItemLayout : ConstraintLayout {
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var ivIcon: AppCompatImageView
    private lateinit var line: View

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.user_fragment_setting_item, this, true)
        ivIcon = findViewById(R.id.iv_icon)
        tvTitle = findViewById(R.id.tv_title)
        line = findViewById(R.id.line)
    }

    public fun setIvIcon(resId: Int) {
        ivIcon.setBackgroundResource(resId)
    }


    public fun setTitle(title: String) {
        tvTitle.text = title
    }


    public fun setLineHidden() {
        line.visibility = View.GONE
    }

}