package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.fragment.CustomerDetailBaseFragment;
import com.baihe.lihepro.fragment.CustomerDetailContractFragment;
import com.baihe.lihepro.fragment.CustomerDetailDemandFragment;
import com.baihe.lihepro.fragment.CustomerDetailRequirementFragment;
import com.baihe.lihepro.fragment.CustomerDetailSalesFragment;
import com.baihe.lihepro.fragment.CustomerDetailTeamFragment;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailPagerAdapter extends FragmentPagerAdapter {
    private String customerId;
    private String customerTab;
    private int entryType;

    public CustomerDetailPagerAdapter(@NonNull FragmentManager fm, String customerId, String customerTab, int entryType) {
        super(fm);
        this.customerId = customerId;
        this.customerTab = customerTab;
        this.entryType = entryType;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE){
            switch (position) {
                case 3:
                    fragment = CustomerDetailTeamFragment.newFragment(1);
                    break;
                case 2:
                    fragment = new CustomerDetailDemandFragment();
                    break;
                case 1:
                    fragment = new CustomerDetailSalesFragment();
                    break;
                case 0:
                default:
                    fragment = new CustomerDetailBaseFragment();
                    break;
            }
        }else if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER){
            switch (position) {
                case 4:
                    fragment = new CustomerDetailContractFragment();
                    break;
                case 3:
                    fragment = new CustomerDetailRequirementFragment();
                    break;
                case 2:
                    fragment = new CustomerDetailDemandFragment();
                    break;
                case 1:
                    fragment = new CustomerDetailSalesFragment();
                    break;
                case 0:
                default:
                    fragment = new CustomerDetailBaseFragment();
                    break;
            }
        }else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT){
            switch (position) {
                case 4:
                    fragment = CustomerDetailTeamFragment.newFragment(2);
                    break;
                case 3:
                    fragment = new CustomerDetailRequirementFragment();
                    break;
                case 2:
                    fragment = new CustomerDetailDemandFragment();
                    break;
                case 1:
                    fragment = new CustomerDetailSalesFragment();
                    break;
                case 0:
                default:
                    fragment = new CustomerDetailBaseFragment();
                    break;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(CustomerDetailActivity.INTENT_CUSTOMER_ID, customerId);
        bundle.putString(CustomerDetailActivity.INTENT_CUSTOMER_TAB, customerTab);
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE){
            bundle.putInt("type", 1);
        }else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT){
            bundle.putInt("type", 2);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER) {
            return 5;
        }
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE)
            return 4;
        return  5;
    }
}
