package com.baihe.lihepro.filter.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.filter.FilterSecondChildFragment;
import com.baihe.lihepro.filter.entity.FilterKVEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-24
 * Description：
 */
public class SecondPagerAdapter extends FragmentPagerAdapter {
    private List<FilterKVEntity> filterKVEntities;
    private boolean isMuitil;
    private List<String> defaultValues;
    private int viewpageId;

    public SecondPagerAdapter(@NonNull FragmentManager fm, List<FilterKVEntity> filterKVEntities, boolean isMuitil, List<String> defaultValues, int viewpageId) {
        super(fm);
        this.filterKVEntities = filterKVEntities;
        this.isMuitil = isMuitil;
        this.defaultValues = defaultValues;
        this.viewpageId = viewpageId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterSecondChildFragment.KEY_ENTITIS, (ArrayList) filterKVEntities.get(position).children);
        bundle.putBoolean(FilterSecondChildFragment.KEY_MULTIL, isMuitil);
        bundle.putSerializable(FilterSecondChildFragment.KEY_DEFAULT_VALUES, (ArrayList) defaultValues);
        bundle.putInt(FilterSecondChildFragment.KEY_VIEWPAGE_ID, viewpageId);
        bundle.putInt(FilterSecondChildFragment.KEY_CHILD_COUNT, filterKVEntities.size());
        FilterSecondChildFragment filterSecondChildFragment = new FilterSecondChildFragment();
        filterSecondChildFragment.setArguments(bundle);
        return filterSecondChildFragment;
    }

    @Override
    public int getCount() {
        return filterKVEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
