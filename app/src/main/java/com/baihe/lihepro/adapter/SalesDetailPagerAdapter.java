package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.activity.SalesDetailActivity;
import com.baihe.lihepro.fragment.CustomerDetailTeamFragment;
import com.baihe.lihepro.fragment.SalesDetailContractFragment;
import com.baihe.lihepro.fragment.SalesDetailCustomerFragment;
import com.baihe.lihepro.fragment.SalesDetailOrderFragment;
import com.baihe.lihepro.fragment.SalesDetailSalesFragment;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class SalesDetailPagerAdapter extends FragmentPagerAdapter {
    private String orderId;
    private String customerId;
    private String category;
    private String categoryText;

    public SalesDetailPagerAdapter(@NonNull FragmentManager fm, String orderId, String customerId,String category,String categoryText) {
        super(fm);
        this.orderId = orderId;
        this.customerId = customerId;
        this.category = category;
        this.categoryText = categoryText;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle;
        switch (position) {
            case 4:
                fragment = CustomerDetailTeamFragment.newFragment(orderId,customerId,3,category,categoryText);
               return fragment;
            case 3:
                fragment = new SalesDetailSalesFragment();
                break;
            case 2:
                fragment = new SalesDetailContractFragment();
                break;
            case 1:
                fragment = new SalesDetailCustomerFragment();
                break;
            case 0:
            default:
                fragment = new SalesDetailOrderFragment();
                break;
        }
       bundle = new Bundle();
        bundle.putString(SalesDetailActivity.INTENT_ORDER_ID, orderId);
        bundle.putString(SalesDetailActivity.INTENT_CUSTOMER_ID, customerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
