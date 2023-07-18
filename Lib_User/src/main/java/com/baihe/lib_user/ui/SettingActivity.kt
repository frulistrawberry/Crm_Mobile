package com.baihe.lib_user.ui

import android.os.Bundle
import androidx.lifecycle.viewModelScope
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
import com.baihe.lib_user.utils.UserRegexUtil
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
            if (it != null && it.version.isNotEmpty()) {
                try {
                    val versionCode =
                        UserRegexUtil.versionEncrypt(AppManager.getAppVersionName(this))
                    val versionServer = UserRegexUtil.versionEncrypt(it.version)
                    if (versionCode != null && versionServer != null && versionCode < versionServer) {
                        if (it.msgContent.isNotEmpty() && it.url.isNotEmpty()) {
                            showUpdateConfirmDialog(it.msgContent, it.url)
                        }
                    } else {
                        showToast("当前已是最新版本")
                    }
                } catch (_: Exception) {
                }
            }
        }
        //设置推送通知开关
        mViewModel.pushSwitchLiveData.observe(this) {
            if (it == 0) {
                if (mBinding.pushValueSc.isChecked) {
                    showToast("推送通知已开启")
                } else {
                    showToast("推送通知已关闭")
                }
            }
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
        LoginServiceProvider.logout(this,this)
    }

    override fun initListener() {
        super.initListener()
        mBinding.updateValueTv.click {
            //检查更新
            mViewModel.checkVersion("android")
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
            releaseRes()
        }
    }

    private fun showUpdateConfirmDialog(msg: String, url: String) {
        BaseUserTipDialog.Builder(this)
            .addOnConfirmClick(object : BaseUserTipDialog.OnConfirmClick {
                override fun onClick() {
                    //点击确认->跳转到外部浏览器下载地址
                    JumpOutSideAppUtil.jump(this@SettingActivity, url)
                }
            }).setText(R.id.tv_content, msg).setText(R.id.tv_cancel, "取消")
            .setText(R.id.tv_confirm, "确认").create().show()
    }
}