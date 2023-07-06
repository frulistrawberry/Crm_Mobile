package com.baihe.lib_user.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_user.R
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserActivityInfoBinding
import com.baihe.lib_user.service.UserServiceImp
import com.baihe.lib_user.ui.widgets.MyInfoItemLayout

/**
 * @author xukankan
 * @date 2023/7/5 19:09
 * @email：xukankan@jiayuan.com
 * @description：
 */
@Route(path = RoutePath.USER_SERVICE_INFO)
class MyInfoActivity : BaseMvvmActivity<UserActivityInfoBinding, UserViewModel>() {

    private val titleItems = arrayListOf("登录账号", "手机账号", "所属公司", "所属部门", "角色", "姓名")
    private val userService = UserServiceImp()
    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = getString(R.string.user_info)
            navIcon = R.mipmap.navigation_icon
        }
        for (i in titleItems.indices) {
            val mineInfoItemLayout = MyInfoItemLayout(this)
            mineInfoItemLayout.setTitle(titleItems[i])
            if (i == 0) {
                userService.getUserInfo()?.let { mineInfoItemLayout.setTvContent(it.name) }
            }

            if (i == 1) {
                userService.getPhoneNum()?.let { mineInfoItemLayout.setTvContent(it) }
            }

            if (i == 2) {
                userService.getUserInfo()?.let { mineInfoItemLayout.setTvContent(it.company_name) }
            }


            if (i == 3) {
                userService.getUserInfo()?.let { mineInfoItemLayout.setTvContent(it.company_name) }
            }

            if (i == 4) {
                userService.getUserInfo()?.let { mineInfoItemLayout.setTvContent(it.company_tag) }
            }

            if (i == 5) {
                userService.getUserInfo()?.let { mineInfoItemLayout.setTvContent(it.name) }
            }

            if (i == titleItems.size - 1) {
                mineInfoItemLayout.setLineHidden()
            }
            mBinding.llContainer.addView(mineInfoItemLayout)
        }
    }

}