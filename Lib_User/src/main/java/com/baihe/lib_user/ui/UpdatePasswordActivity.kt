package com.baihe.lib_user.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ext.StringExt.isPassword
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.service.ILoginService
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ResourcesExt.string
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_user.R
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserActivityResetPasswordBinding
import com.baihe.lib_user.utils.UserRegexUtil

@Route(path = RoutePath.USER_SERVICE_RESET_PASSWORD)
class UpdatePasswordActivity :
    BaseMvvmActivity<UserActivityResetPasswordBinding, UserViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = getString(R.string.user_update_pass)
            navIcon = R.mipmap.navigation_icon
        }
        UserServiceProvider.getPhoneNum()?.let {
            mBinding.phoneValueTv.text = UserRegexUtil.mobileEncrypt(it)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingDialogLiveData.observe(this) {
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }
        mViewModel.resetPasLiveData.observe(this) {
            dismissLoadingDialog()
            if (it) {
                showToast("密码修改成功，请重新登录")
                //设置密码成功-清除用户信息-跳转至登录页面
                val loginService: ILoginService =
                    ARouter.getInstance().build(RoutePath.LOGIN_SERVICE_LOGIN)
                        .navigation() as ILoginService
                loginService.logout(this, this)
                finish()
            }
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.userBtnCommit.click {
            resetPas()
        }
    }

    private fun resetPas() {
        val oldPas = mBinding.etOlderPas.text.toString().trim()
        val newPas = mBinding.etNewPas.text.toString().trim()
        val confirmPas = mBinding.etConfirmPas.text.toString().trim()
        val userId = UserServiceProvider.getUserId()
        if (oldPas.isEmpty()) {
            TipsToast.showTips("旧密码不能为空~")
            return
        }
        if (newPas.isEmpty()) {
            TipsToast.showTips("新密码不能为空~")
            return
        }
        if (!newPas.isPassword()){
            showToast(string(R.string.user_setting_tip))
            return
        }
        if (confirmPas.isEmpty()) {
            TipsToast.showTips("确认密码不能为空~")
            return
        }
        if (newPas != confirmPas) {
            TipsToast.showTips("新密码和确认密码不一致，请重新输入~")
            return
        }
        showLoadingDialog()
        mViewModel.resetPassword(userId, oldPas, newPas, confirmPas)
    }
}