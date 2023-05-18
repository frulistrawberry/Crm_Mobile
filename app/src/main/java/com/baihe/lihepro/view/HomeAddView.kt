package com.baihe.lihepro.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.baihe.lihepro.R
import kotlinx.android.synthetic.main.layout_home_add.view.*

class HomeAddView:FrameLayout {

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }

    private lateinit var contentView: View
    private var listener: OnHomeAddViewClickListener? = null
    private var isAnimating = false

    private val DELAY_TIME = 50L
    private val ANIMATOR_TIME = 100L

    private fun initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_home_add, this, true)
        contentView.setOnClickListener { hide() }
        contentView.home_add_customer.visibility = View.INVISIBLE
        contentView.home_add_contract.visibility = View.INVISIBLE

        contentView.home_add_customer.setOnClickListener {
            listener?.onNewCustomerClick()
            hide()
        }
        contentView.home_add_contract.setOnClickListener {
            listener?.onNewContractClick()
            hide()
        }
        contentView.home_add_more.setOnClickListener {
            listener?.onCloseClick()
            hide()
        }
    }

    fun show(){
        if(isAnimating) return
        if(context != null && context is Activity){
            isAnimating = true
            val activity = context as Activity
            val parentView = activity.findViewById<ViewGroup>(android.R.id.content)
            parentView.addView(this,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

            animator(contentView.home_add_customer,true,false,null)
            animator(contentView.home_add_contract,true,true){
                isAnimating = false
            }
        }
    }

    fun hide(){
        if(isAnimating) return
        if(context != null && context is Activity){
            isAnimating = true
            animator(contentView.home_add_contract,false,false,null)
            animator(contentView.home_add_customer,false,true){
                val activity = context as Activity
                val parentView = activity.findViewById<ViewGroup>(android.R.id.content)
                parentView.removeView(this)
                isAnimating = false
            }
        }
    }

    private fun animator(view:View, isIn:Boolean,isDelay:Boolean, endAnim: (() -> Unit)?){
        view.measure(0,0)
        val pointY = view.measuredHeight
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = ANIMATOR_TIME
        if(isDelay){
            valueAnimator.startDelay = DELAY_TIME
        }
        valueAnimator.addUpdateListener {
            val factor = it.animatedValue as Float
            if(isIn){
                view.alpha = factor
                view.y = (1 - factor) * pointY
            }else{
                view.alpha = (1 - factor)
                view.y = factor * pointY
            }
            view.requestLayout()
        }
        valueAnimator.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                if (endAnim != null) {
                    endAnim()
                }
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {
                view.visibility = View.VISIBLE
            }
        })
        valueAnimator.start()
    }

    fun setListener(l: OnHomeAddViewClickListener){
        this.listener = l
    }

    interface OnHomeAddViewClickListener{
        fun onNewCustomerClick()
        fun onNewContractClick()
        fun onCloseClick()
    }
}