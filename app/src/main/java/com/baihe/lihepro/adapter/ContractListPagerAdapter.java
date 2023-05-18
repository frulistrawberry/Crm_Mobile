package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.fragment.ContractListFragment;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContractListPagerAdapter extends FragmentPagerAdapter {

    public ContractListPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ContractListFragment fragment = new ContractListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ContractListFragment.INTENT_TAB_VALUE, String.valueOf(position + 1));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
