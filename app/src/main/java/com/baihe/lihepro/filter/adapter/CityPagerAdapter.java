package com.baihe.lihepro.filter.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.filter.FilterCityCityFragment;
import com.baihe.lihepro.filter.FilterCityProvinceFragment;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-24
 * Description：
 */
public class CityPagerAdapter extends FragmentPagerAdapter {
    private List<String> tabs;

    public CityPagerAdapter(@NonNull FragmentManager fm, List<String> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new FilterCityProvinceFragment();
        } else {
            fragment = new FilterCityCityFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
