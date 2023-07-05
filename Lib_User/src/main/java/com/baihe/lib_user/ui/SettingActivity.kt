package com.baihe.lib_user.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.ILoginService
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_user.R
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserActivitySettingBinding
import com.baihe.lib_user.service.UserServiceImp

@Route(path = RoutePath.USER_SERVICE_SETTING)
class SettingActivity :
    BaseMvvmActivity<UserActivitySettingBinding, UserViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = getString(R.string.user_setting)
            navIcon = R.mipmap.navigation_icon
        }
        val version = "V" + AppManager.getAppVersionName(this)
        mBinding.updateValueTv.text = version
    }

    override fun initViewModel() {
        super.initViewModel()

    }

    override fun initListener() {
        super.initListener()
        mBinding.updateValueTv.click {
            //检查更新
            mViewModel.checkVersion()
        }
        mBinding.ruleValueTv.click {
            //跳转隐私政策
            UserServiceImp().readPolicy(this)
        }
        mBinding.pushValueSc.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.setPushTurnOnOrOff(isChecked)
        }
        mBinding.deleteValueTv.click {
            //注销账号
            mViewModel.deleteAccount()
        }
        mBinding.tvSetting.click {
            //退出登录
            val loginService: ILoginService =
                ARouter.getInstance().build(RoutePath.LOGIN_SERVICE_LOGIN)
                    .navigation() as ILoginService
            loginService.logout(this, this)
        }
    }
}