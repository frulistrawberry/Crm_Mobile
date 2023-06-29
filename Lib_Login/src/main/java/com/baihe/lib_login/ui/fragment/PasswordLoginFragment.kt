package com.baihe.lib_login.ui.fragment

import android.app.Activity.RESULT_OK
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.ext.StringExt.isPhone
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.*
import com.baihe.lib_framework.ext.EditTextExt.textChangeFlow
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ResourcesExt.string
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.onClick
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_login.R
import com.baihe.lib_login.constant.KeyConstant.KET_CHECK
import com.baihe.lib_login.constant.KeyConstant.KEY_MOBILE
import com.baihe.lib_login.databinding.LoginFragmentPasswordLoginBinding
import com.baihe.lib_login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PasswordLoginFragment: BaseMvvmFragment<LoginFragmentPasswordLoginBinding, LoginViewModel>() {
    private var isShowPassword = false
    private val mController by lazy {
        findNavController()
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loginLiveData.observe(this){ success ->
            dismissLoadingDialog()
            if (success){
                showToast("登录成功")
            }
            activity?.setResult(RESULT_OK)
            activity?.finish()
        }
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        initAgreement()
        mBinding?.etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    override fun initListener() {
        super.initListener()
        mBinding?.ivMobileDel?.onClick {
            mBinding?.etMobile?.setText("")
        }
        mBinding?.ivPasswordDel?.onClick {
            mBinding?.etPassword?.setText("")
        }
        mBinding?.ivPasswordEye?.onClick {
            setPasswordHide()
        }
        mBinding?.btnVerifyLogin?.onClick {
            verifyLogin()
        }
        mBinding?.btnLogin?.onClick {
            login()
        }


        mBinding?.etMobile?.textChangeFlow()
            ?.flowOn(Dispatchers.IO)
            ?.onEach {
                val mobile = mBinding?.etMobile?.text.toString()
                val delVisible = mobile.isNotEmpty()
                if (delVisible){
                    mBinding?.ivMobileDel?.visible()
                }else{
                    mBinding?.ivMobileDel?.gone()
                }
            }
            ?.launchIn(lifecycleScope)

        mBinding?.etPassword?.textChangeFlow()
            ?.flowOn(Dispatchers.IO)
            ?.onEach {
                val password = mBinding?.etPassword?.text.toString()
                val delVisible = password.isNotEmpty()
                if (delVisible){
                    mBinding?.ivPasswordDel?.visible()
                    mBinding?.ivPasswordEye?.visible()
                }else{
                    mBinding?.ivPasswordDel?.gone()
                    mBinding?.ivPasswordEye?.gone()
                }
            }
            ?.launchIn(lifecycleScope)
    }

    override fun initData() {
        super.initData()
        val mobile = arguments?.getString(KEY_MOBILE)
        mBinding?.etMobile?.setText(mobile)
        mBinding?.etMobile?.setSelection(mBinding?.etMobile?.length()?:0)

        val check = arguments?.getBoolean(KET_CHECK)
        mBinding?.cbAgreement?.isChecked = check?:false
    }

    private fun initAgreement() {
        val agreement = string(R.string.login_agreement_desc)
        try {
            mBinding?.tvAgreement?.movementMethod = LinkMovementMethod.getInstance()
            val spaBuilder = SpannableStringBuilder(agreement)
            val privacySpan = string(R.string.login_privacy_agreement)
            val serviceSpan = string(R.string.login_user_agreement)
            spaBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        //  跳转隐私政策
                        UserServiceProvider.readPolicy(requireContext())
                        (widget as TextView).highlightColor = color(R.color.COLOR_00000000)
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = color(R.color.COLOR_6C8EFF)
                        ds.isUnderlineText = false
                        ds.clearShadowLayer()
                    }
                },
                spaBuilder.indexOf(privacySpan),
                spaBuilder.indexOf(privacySpan) + privacySpan.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spaBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        // 跳转用户协议
                        UserServiceProvider.readAgreement(requireContext())
                        (widget as TextView).highlightColor = color(R.color.COLOR_00000000)
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = color(R.color.COLOR_6C8EFF)
                        ds.isUnderlineText = false
                        ds.clearShadowLayer()
                    }
                },
                spaBuilder.indexOf(serviceSpan),
                spaBuilder.indexOf(serviceSpan) + serviceSpan.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mBinding?.tvAgreement?.setText(spaBuilder, TextView.BufferType.SPANNABLE)
        } catch (e: Exception) {
            LogUtil.e(e)
            mBinding?.tvAgreement?.text = agreement
        }
    }

    private fun setPasswordHide() {
        if (isShowPassword) {
            mBinding?.ivPasswordEye?.setImageResource(R.mipmap.login_ic_eye_open)
            mBinding?.etPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            mBinding?.etPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
            mBinding?.ivPasswordEye?.setImageResource(R.mipmap.login_ic_eye_close)
        }
        isShowPassword = !isShowPassword
        mBinding?.etPassword?.setSelection(mBinding?.etPassword?.length()?:0)
    }

    private fun verifyLogin(){
        val mobile = mBinding?.etMobile?.text.toString().trim()
        val isCheck = mBinding?.cbAgreement?.isChecked
        if (mobile.isNotEmpty()&&mobile.isPhone())
            mController.previousBackStackEntry?.savedStateHandle?.set(KEY_MOBILE,mobile)
        mController.previousBackStackEntry?.savedStateHandle?.set(KET_CHECK,isCheck)

        mController.popBackStack()
    }

    private fun login(){
        val mobile = mBinding?.etMobile?.text.toString().trim()
        val password = mBinding?.etPassword?.text.toString().trim()
        if(mobile.isEmpty()){
            TipsToast.showTips("请输入手机号")
            return
        }
        if (!mobile.isPhone()){
            TipsToast.showTips("手机号输入格式不正确")
            return
        }
        if (password.isEmpty()){
            TipsToast.showTips("请输入密码")
            return
        }
        if (mBinding?.cbAgreement?.isChecked?:return){
            showLoadingDialog()
            mViewModel.passWordLogin(mobile,password)
        }else{
            TipsToast.showTips("请同意并勾选用户协议及隐私政策")
            return
        }

    }



}