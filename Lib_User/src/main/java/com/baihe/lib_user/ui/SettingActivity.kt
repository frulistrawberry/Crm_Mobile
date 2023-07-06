package com.baihe.lib_user.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ActivityScenario.launch
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.http.cookie.CookieJarImpl
import com.baihe.lib_common.http.exception.ApiException
import com.baihe.lib_common.provider.LoginServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.push.PushHelper
import com.baihe.lib_common.service.ILoginService
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_user.R
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserActivitySettingBinding
import com.baihe.lib_user.dialog.BaseUserTipDialog
import com.baihe.lib_user.service.UserServiceImp
import com.baihe.lib_user.utils.JumpOutSideAppUtil
import kotlinx.coroutines.launch

@Route(path = RoutePath.USER_SERVICE_SETTING)
class SettingActivity : BaseMvvmActivity<UserActivitySettingBinding, UserViewModel>() {

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
        mViewModel.loadingDialogLiveData.observe(this) {
            if (it) showLoadingDialog()
            else dismissLoadingDialog()
        }
        //更新版本处理
        mViewModel.versionLiveData.observe(this) {
            if (it != null && it.androidURL.isNotEmpty()) {
                showUpdateConfirmDialog(it.androidURL)
            }
        }
        //设置推送通知开关
        mViewModel.pushSwitchLiveData.observe(this) {
            mBinding.pushValueSc.isChecked = it
        }
        //注销账号
        mViewModel.deleteAccountLiveData.observe(this) {
            if (it) {
                releaseRes()
            }
        }
    }

    /**注销账号之后释放资源*/
    private fun releaseRes() {
        mViewModel.viewModelScope.launch {
            try {
                PushHelper.logoutAccount()
                UserServiceProvider.clearUserInfo()
                CookieJarImpl.getInstance().cookieStore.removeAll()
                LoginServiceProvider.login(this@SettingActivity)
            } catch (ex: ApiException) {
                LogUtil.e(ex)
            }
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.updateValueTv.click {
            //检查更新
            mViewModel.checkVersion(AppManager.getAppVersionName(this))
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
            BaseUserTipDialog.Builder(this)
                .addOnConfirmClick(object : BaseUserTipDialog.OnConfirmClick {
                    override fun onClick() {
                        //点击确认
                        mViewModel.deleteAccount()
                    }
                }).setText(R.id.tv_content, "是否确定注销当前账号？").setText(R.id.tv_cancel, "取消")
                .setText(R.id.tv_confirm, "确认").create().show()
        }
        mBinding.tvSetting.click {
            //退出登录
            val loginService: ILoginService =
                ARouter.getInstance().build(RoutePath.LOGIN_SERVICE_LOGIN)
                    .navigation() as ILoginService
            loginService.logout(this, this)
        }
    }

    private fun showUpdateConfirmDialog(url: String) {
        BaseUserTipDialog.Builder(this)
            .addOnConfirmClick(object : BaseUserTipDialog.OnConfirmClick {
                override fun onClick() {
                    //点击确认->跳转到外部浏览器下载地址
                    JumpOutSideAppUtil.jump(this@SettingActivity, url)
                }
            }).setText(R.id.tv_content, "检查到新内容，是否更新？")
            .setText(R.id.tv_cancel, "取消")
            .setText(R.id.tv_confirm, "确认").create().show()
    }
}