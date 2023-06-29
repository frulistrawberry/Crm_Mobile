package com.baihe.lib_login.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.StringExt.isPhone
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.EditTextExt.textChangeFlow
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ResourcesExt.string
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.onClick
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_login.R
import com.baihe.lib_login.constant.KeyConstant
import com.baihe.lib_login.databinding.LoginFragmentSendVerifyCodeBinding
import com.baihe.lib_login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SendVerifyCodeFragment:
    BaseMvvmFragment<LoginFragmentSendVerifyCodeBinding, LoginViewModel>() {
    private val  mController by lazy {
        findNavController()
    }


    override fun initViewModel() {
        super.initViewModel()
        mViewModel.verifyCodeLiveData.observe(this){ success ->
            dismissLoadingDialog()
            toVerifyLogin(success)

        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initAgreement()
    }


    override fun initListener() {
        super.initListener()
        mBinding?.btnSendVerifyCode?.onClick {
            sendVerifyCode()
        }
        mBinding?.btnPasswordLogin?.onClick {
            toPasswordLogin()
        }
        mBinding?.ivDel?.onClick {
            mBinding?.etMobile?.setText("")
        }

        mBinding?.etMobile?.textChangeFlow()
            ?.flowOn(Dispatchers.IO)
            ?.onEach {
                val mobile = mBinding?.etMobile?.text.toString()
                val delVisible = mobile.isNotEmpty()
                if (delVisible){
                    mBinding?.ivDel?.visible()
                }else{
                    mBinding?.ivDel?.gone()
                }
            }
            ?.launchIn(lifecycleScope)
    }
    override fun initData() {
        super.initData()
        val mobile:String? = UserServiceProvider.getPhoneNum()
        mBinding?.etMobile?.setText(mobile)
        mBinding?.etMobile?.length()?.let { mBinding?.etMobile?.setSelection(it) }
        mController
            .currentBackStackEntry?.
            savedStateHandle?.getLiveData<Boolean>(KeyConstant.KET_CHECK)?.observe(this){
                mBinding?.
                cbAgreement?.
                isChecked = it
            }

        mController
            .currentBackStackEntry?.
            savedStateHandle?.getLiveData<String>(KeyConstant.KEY_MOBILE)?.observe(this){it ->
                if (it.isNotEmpty()&&it.isPhone())
                    mBinding?.etMobile?.setText(it)

            }


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

    private fun sendVerifyCode(){
        val mobile = mBinding?.etMobile?.text.toString().trim()
        if(mobile.isEmpty()){
            TipsToast.showTips("请输入手机号")
            return
        }
        if (!mobile.isPhone()){
            TipsToast.showTips("手机号输入格式不正确")
            return
        }
        if (mBinding?.cbAgreement?.isChecked?:return){
            showLoadingDialog()
            mViewModel.sendVerifyCode(mobile)
        }else{
            TipsToast.showTips("请同意并勾选用户协议及隐私政策")
            return
        }
    }

    private fun toPasswordLogin(){
        val mobile = mBinding?.etMobile?.text.toString().trim()
        val isCheck = mBinding?.cbAgreement?.isChecked
        val argument = Bundle()
        if (mobile.isNotEmpty()&&mobile.isPhone())
            argument.putString(KeyConstant.KEY_MOBILE,mobile)
        argument.putBoolean(KeyConstant.KET_CHECK, isCheck?:false)
        mController.navigate(R.id.login_action_login_send_verify_code_fragment_to_login_password_login_fragment,argument)
    }

    private fun toVerifyLogin(success:Boolean){
        val mobile = mBinding?.etMobile?.text.toString().trim()
        val argument = Bundle()
        if (mobile.isNotEmpty()&&mobile.isPhone())
            argument.putString(KeyConstant.KEY_MOBILE,mobile)
        argument.putBoolean(KeyConstant.KET_SEND_VERIFY_SUCCESS,success)
        mController
            .navigate(R.id.login_action_login_send_verify_code_fragment_to_login_verify_login_fragment,argument)

    }





}