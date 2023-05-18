package com.baihe.lihepro.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.fragment.ProductListFragment;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ProductPagerAdapter extends FragmentPagerAdapter {
    private List<CategoryEntity> categorys;
    private String keyword;

    public ProductPagerAdapter(@NonNull FragmentManager fm, List<CategoryEntity> categorys) {
        super(fm);
        this.categorys = categorys;
    }

    public ProductPagerAdapter(@NonNull FragmentManager fm, List<CategoryEntity> categorys, String keyword) {
        super(fm);
        this.categorys = categorys;
        this.keyword = keyword;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductListFragment.INTENT_PRODUCT_CATEGOTY, categorys.get(position));
        bundle.putString(ProductListFragment.INTENT_PRODUCT_KEYWORD, keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return categorys.size();
    }
}
