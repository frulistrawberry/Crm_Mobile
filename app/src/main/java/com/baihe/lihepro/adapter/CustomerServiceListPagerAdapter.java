package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.entity.RequirementTabEntity;
import com.baihe.lihepro.fragment.CustomerServiceListFragment;
import com.baihe.lihepro.fragment.RequirementListFragment;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerServiceListPagerAdapter extends FragmentPagerAdapter {
    private RequirementTabEntity tabEntity;

    public CustomerServiceListPagerAdapter(@NonNull FragmentManager fm, RequirementTabEntity tabEntity) {
        super(fm);
        this.tabEntity = tabEntity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        CustomerServiceListFragment fragment = new CustomerServiceListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CustomerServiceListFragment.INTENT_PARAM_KEY, tabEntity.getParamKey());
        bundle.putString(CustomerServiceListFragment.INTENT_TAB_VALUE, tabEntity.getList().get(position).getVal());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabEntity.getList().size();
    }
}
