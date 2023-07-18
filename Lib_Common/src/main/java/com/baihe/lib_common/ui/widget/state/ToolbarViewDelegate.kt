package com.baihe.lib_common.ui.widget.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout.LayoutParams
import androidx.core.widget.doOnTextChanged
import com.baihe.lib_common.databinding.CommonTitleViewBinding
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.onSearchListener
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.searchHint
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearchBehind
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.utils.KeyboardUtils
import com.baihe.lib_framework.utils.StatusBarSettingHelper
import com.baihe.lib_framework.widget.state.ktx.BaseToolbarViewDelegate
import com.baihe.lib_framework.widget.state.ktx.NavBtnType
import com.baihe.lib_framework.widget.state.ktx.ToolbarConfig

class ToolbarViewDelegate: BaseToolbarViewDelegate() {
    lateinit var mBinding: CommonTitleViewBinding

    override fun onCreateToolbar(inflater: LayoutInflater, parent: ViewGroup): View {
        mBinding = CommonTitleViewBinding.inflate(inflater,parent,false)
        mBinding.vStatusBar.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
            StatusBarSettingHelper.
            getStatusBarHeight(AppHelper.
            getApplication()))
        return mBinding.root
    }
    override fun onBindToolbar(config: ToolbarConfig) {
        mBinding.apply {
            tvTitle.text = config.title?:""
            when(config.navBtnType){
                NavBtnType.ICON -> {
                    config.navIcon?.let { ivLeft.setImageResource(it) }
                    ivLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.GONE
                    ivLeft.visibility = View.VISIBLE
                }
                NavBtnType.TEXT -> {
                    tvLeft.text = config.navText
                    tvLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.VISIBLE
                    ivLeft.visibility = View.GONE
                }
                NavBtnType.ICON_TEXT -> {
                    config.navIcon?.let { ivLeft.setImageResource(it) }
                    tvLeft.text = config.navText
                    ivLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.VISIBLE
                    ivLeft.visibility = View.VISIBLE
                }
                NavBtnType.NONE -> {
                    ivLeft.visibility = View.GONE
                    tvLeft.visibility = View.GONE
                }

            }

            if (config.rightText != null) {
                tvRight.text = config.rightText
                tvRight.setOnClickListener(config.onRightClickListener)
                tvRight.visibility = View.VISIBLE
            } else {
                tvRight.visibility = View.GONE
            }

            if (config.rightIcon != null) {
                ivRight.setImageResource(config.rightIcon!!)
                ivRight.setOnClickListener(config.onRightClickListener)
                ivRight.visibility = View.VISIBLE
            } else {
                ivRight.visibility = View.GONE
            }
            if (config.showSearch == true){
                if (config.showSearchBehind == true){
                    cvSearch.gone()
                    etSearch.setOnEditorActionListener { textView, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH){
                            val keywords = textView.text.toString().trim()
                            config.onSearchListener?.invoke(keywords)
                            etSearch.clearFocus()
                            KeyboardUtils.hideInputMethod(etSearch)

                            return@setOnEditorActionListener true
                        }

                        return@setOnEditorActionListener false
                    }
                    etSearch.doOnTextChanged{ text, _, _, _ ->
                        if (text?.isNotEmpty() == true){
                            btnCancel.visible()
                        }else{
                            btnCancel.gone()
                        }
                    }

                    btnCancel.click {
                        etSearch.setText("")
                        config.onSearchListener?.invoke("")
                        etSearch.clearFocus()
                        KeyboardUtils.hideInputMethod(etSearch)
                    }
                    llSearch.visible()
                }else{
                    llSearch.gone()
                    etSearch2.hint = config.searchHint?:"请输入机会名称/手机号/客户姓名"
                    etSearch2.setOnEditorActionListener { textView, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH){
                            val keywords = textView.text.toString().trim()
                            config.onSearchListener?.invoke(keywords)

                            etSearch.clearFocus()
                            KeyboardUtils.hideInputMethod(etSearch)

                            return@setOnEditorActionListener true
                        }

                        return@setOnEditorActionListener false
                    }
                    etSearch2.doOnTextChanged{ text, _, _, _ ->
                        if (text?.isNotEmpty() == true){
                            btnCancel2.visible()
                        }else{
                            btnCancel2.gone()
                        }
                    }
                    btnCancel2.click {
                        etSearch2.setText("")
                        config.onSearchListener?.invoke("")
                        etSearch2.clearFocus()
                        KeyboardUtils.hideInputMethod(etSearch2)
                    }
                    cvSearch.visible()
                }

            }else{
                llSearch.gone()
                cvSearch.gone()
            }
        }
    }

}