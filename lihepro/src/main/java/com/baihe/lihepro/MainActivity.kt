package com.baihe.lihepro

import android.os.Bundle
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.baihe.lib_common.provider.HomeServiceProvider
import com.baihe.lib_common.provider.MessageServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_home.ui.fragment.HomeFragment
import com.baihe.lib_message.ui.fragment.MessageFragment
import com.baihe.lib_user.ui.MineFragment
import com.baihe.lihepro.databinding.ActivityMainBinding

class MainActivity : BaseViewBindActivity<ActivityMainBinding>() {
    companion object{
        const val TAG_HOME = "TAG_HOME"
        const val TAG_MESSAGE = "TAG_MESSAGE"
        const val TAG_MY = "TAG_MY"
    }


    override fun initView(savedInstanceState: Bundle?) {
       setContent(TAG_HOME)
    }

    override fun initListener() {
        super.initListener()
        mBinding.mainRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.main_home_rb -> setContent(TAG_HOME)
                R.id.main_my_rb -> setContent(TAG_MY)
                R.id.main_message_rb -> setContent(TAG_MESSAGE)
            }
        }
    }


    private fun setContent(fragmentTag: String) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragments: List<Fragment> = supportFragmentManager.fragments
        hideFragments(fragments, fragmentTransaction, fragmentTag)
        if (TAG_HOME == fragmentTag) {
            var fragment =
                supportFragmentManager.findFragmentByTag(TAG_HOME)
            if (fragment == null) {
                fragment = HomeServiceProvider.getHomeFragment()
                fragmentTransaction.add(R.id.main_fl, fragment, fragmentTag)
            } else {
                fragmentTransaction.show(fragment)
            }
        } else if (TAG_MY == fragmentTag) {
            var fragment =
                supportFragmentManager.findFragmentByTag(TAG_MY)
            if (fragment == null) {
                fragment = UserServiceProvider.getMineFragment()
                fragmentTransaction.add(R.id.main_fl, fragment, fragmentTag)
            } else {
                fragmentTransaction.show(fragment)
            }
        }
        else if (TAG_MESSAGE == fragmentTag) {
            var fragment =
                supportFragmentManager.findFragmentByTag(TAG_MESSAGE)
            if (fragment == null) {
                fragment = MessageServiceProvider.getMessageFragment()
                fragmentTransaction.add(R.id.main_fl, fragment, fragmentTag)
            } else {
                fragmentTransaction.show(fragment)
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun hideFragments(
        fragments: List<Fragment>?,
        fragmentTransaction: FragmentTransaction,
        showTag: String
    ) {
        if (fragments == null || fragments.isEmpty()) {
            return
        }
        for (fragment in fragments) {
            val tag = fragment.tag
            if (showTag != tag) {
                fragmentTransaction.hide(fragment)
            }
        }
    }


}