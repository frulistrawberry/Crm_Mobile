package com.baihe.lihepro.filter.adapter;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.filter.FilterChannelChildFragment;
import com.baihe.lihepro.filter.entity.FilterKVEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-17
 * Description：
 */
public class ChannelPagerAdapter extends FragmentPagerAdapter {
    private List<FilterKVEntity> tabs;

    public ChannelPagerAdapter(@NonNull FragmentManager fm, List<FilterKVEntity> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FilterChannelChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FilterChannelChildFragment.INTENT_TAB_INDEX, position);
        if (position > 0) {
            bundle.putString(FilterChannelChildFragment.INTENT_CHANNEL_ID, tabs.get(position - 1).getItem_val());
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return position;
        }
        return tabs.get(position - 1).getItem_val().hashCode();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
