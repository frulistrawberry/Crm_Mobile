package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.RequirementTabEntity;
import com.baihe.lihepro.fragment.ProductListFragment;
import com.baihe.lihepro.fragment.RequirementListFragment;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class RequirementListPagerAdapter extends FragmentPagerAdapter {
    private RequirementTabEntity tabEntity;

    public RequirementListPagerAdapter(@NonNull FragmentManager fm, RequirementTabEntity tabEntity) {
        super(fm);
        this.tabEntity = tabEntity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        RequirementListFragment fragment = new RequirementListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RequirementListFragment.INTENT_PARAM_KEY, tabEntity.getParamKey());
        bundle.putString(RequirementListFragment.INTENT_TAB_VALUE, tabEntity.getList().get(position).getVal());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabEntity.getList().size();
    }
}
