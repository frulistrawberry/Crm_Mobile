package com.baihe.lib_common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    private var fragmentList = mutableListOf<Fragment>()





    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList.get(position)

    fun addFragment(fragment: Fragment):ViewPager2Adapter{
        fragmentList.add(fragment)
        return this
    }


}
