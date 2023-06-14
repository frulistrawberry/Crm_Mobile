package com.baihe.lib_login.login.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import com.baihe.lib_common.ext.dismissLoadingDialog
import com.baihe.lib_common.ext.isPhone
import com.baihe.lib_common.ext.printBackStack
import com.baihe.lib_common.ext.showLoadingDialog
import com.baihe.lib_common.provider.HomeServiceProvider
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.gone
import com.baihe.lib_framework.ext.onClick
import com.baihe.lib_framework.ext.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_login.BuildConfig
import com.baihe.lib_login.R
import com.baihe.lib_login.constant.KeyConstant
import com.baihe.lib_login.databinding.LoginFragmentVerifyLoginBinding
import com.baihe.lib_login.login.viewmodel.LoginViewModel
import com.dylanc.loadingstateview.NavBtnType

class VerifyLoginFragment :BaseMvvmFragment<LoginFragmentVerifyLoginBinding,LoginViewModel>() {


    private val mobile:String by lazy {
        arguments?.getString(KeyConstant.KEY_MOBILE)!!
    }

    private val mController by lazy {
        findNavController()
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.verifyCodeLiveData.observe(this){
            dismissLoadingDialog()
            showVerifyState(it)
        }

        mViewModel.loginLiveData.observe(this){
            dismissLoadingDialog()
            if (it){
                showToast("登录成功")
                if (BuildConfig.IS_BUILD_MODULE){
                    if ("com.baihe.lib_home" == AppManager.getApplicationId(requireContext())){
                        HomeServiceProvider.toHome(requireContext())
                    }else if ("com.baihe.lib_user" == AppManager.getApplicationId(requireContext())){
                        // TODO: 跳转用户
                    }
                }else{
                    // TODO: 跳转至主页面
                }
                activity?.finish()
            }
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setToolbar(navBtnType = NavBtnType.ICON){
            navIcon(R.mipmap.navigation_icon){
                if (mobile.isNotEmpty()&&mobile.isPhone())
                    mController.previousBackStackEntry?.savedStateHandle?.set(KeyConstant.KEY_MOBILE,mobile)
                mController.previousBackStackEntry?.savedStateHandle?.set(KeyConstant.KET_CHECK,true)

                mController.popBackStack()
            }
        }
    }

    override fun initListener() {
        super.initListener()

        mBinding?.btnSendVerifyCode?.onClick {
            showLoadingDialog()
            mViewModel.sendVerifyCode(mobile)
        }

        mBinding?.vvCode?.finish = {
            login(it)
        }

    }



    override fun initData() {
        super.initData()
        val sendVerifySuccess = arguments?.getBoolean(KeyConstant.KET_SEND_VERIFY_SUCCESS)
        showVerifyState(sendVerifySuccess)


    }

    private fun login(it: String) {
        showLoadingDialog()
        mViewModel.verifyCodeLogin(mobile,it)
    }

    @SuppressLint("SetTextI18n")
    private fun showVerifyState(it: Boolean?) {
        if (it == true){
            mBinding?.tvMobile?.text = "验证码已发送至 +86${mobile}"
            mBinding?.tvTimer?.visible()
            mBinding?.btnSendVerifyCode?.gone()
            showToast("短信验证码已发送，请查收")
            showCountDownTimer()
        }else{
            mBinding?.tvMobile?.text = "验证码发送失败，请点击重新获取验证码"
            mBinding?.tvTimer?.gone()
            mBinding?.btnSendVerifyCode?.visible()
        }
    }

    @SuppressLint("SetTextI18n")
    fun showCountDownTimer(){
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mBinding?.tvTimer?.text = (millisUntilFinished / 1000).toString() + " 秒后重新获取验证码"
            }

            override fun onFinish() {
                mBinding?.tvTimer?.visibility = View.GONE
                mBinding?.btnSendVerifyCode?.visibility = View.VISIBLE
            }
        }.start()
    }


}