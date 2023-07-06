package com.baihe.lib_user.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_user.ui.widgets.MineItemLayout
import com.baihe.lib_user.R
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserFragmentMineBinding
import com.baihe.lib_user.service.UserServiceImp
import com.bumptech.glide.Glide

/**
 * @author xukankan
 * @date 2023/7/5 16:34
 * @email：xukankan@jiayuan.com
 * @description：
 */
class MineFragment : BaseMvvmFragment<UserFragmentMineBinding, UserViewModel>() {
    private val userServiceImp = UserServiceImp()
    private val titleItems = arrayListOf("个人资料", "修改密码", "设置")
    private val titleIconItems = arrayListOf(
        R.drawable.user_info_icon,
        R.drawable.user_pwd_icon,
        R.drawable.user_setting_icon
    )

    private val titlePaths = arrayListOf(
        RoutePath.USER_SERVICE_INFO,
        RoutePath.USER_SERVICE_RESET_PASSWORD,
        RoutePath.USER_SERVICE_SETTING
    )


    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.tvCompany?.text = userServiceImp.getCompanyName()
        mBinding?.tvName?.text = userServiceImp.getUserInfo()?.name
        mBinding?.tvBoss?.text = userServiceImp.getUserInfo()?.company_tag
        for (i in titleItems.indices) {
            activity?.let {
                val mineItemLayout = MineItemLayout(it)
                mineItemLayout.setOnClickListener {
                    ARouter.getInstance().build(titlePaths[i]).navigation();
                }
                mineItemLayout.setTitle(titleItems[i])
                mineItemLayout.setIvIcon(titleIconItems[i])
                if (i == titleIconItems.size - 1) {
                    mineItemLayout.setLineHidden()
                }
                mBinding?.llSetting?.addView(mineItemLayout)
            }
        }
    }

}