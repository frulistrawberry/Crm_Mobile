package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ContractListPagerAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.filter.FilterCallback;
import com.baihe.lihepro.filter.FilterUtils;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.fragment.ContractListFragment;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-10-10
 * Description：
 */
public class ContractListActivity extends BaseActivity {
    public static final int REQUEST_CODE_NEW_CONTRACT = 1;
    public static final int REQUEST_CODE_DETAIL_CONTRACT = 2;

    public static void start(Context context) {
        Intent intent = new Intent(context, ContractListActivity.class);
        context.startActivity(intent);
    }

    private Toolbar contract_list_title_tb;
    private ImageView contract_list_title_search_iv;
    private ImageView contract_list_title_add_iv;
    private HorizontalScrollView contract_list_title_hs;
    private TextTransitionRadioGroup contract_list_title_ttrg;
    private TextTransitionRadioButton contract_list_title_my_ttrb;
    private TextTransitionRadioButton customer_detail_title_coord_ttrb;
    private LinearLayout contract_list_title_filter_ll;
    private ViewPager contract_list_vp;

    private Map<String, Object> filterParmsMap = new HashMap<>();
    private ContractListPagerAdapter adapter;

    private ArrayList<FilterEntity> filterEntities;
    private FilterCallback filterCallback = new FilterCallback() {
        @Override
        public void call(Map<String, Object> requestMap) {
            Logger.d(requestMap);
            filterParmsMap.clear();
            filterParmsMap.putAll(requestMap);
            refresh();
        }
    };

    public Map<String, Object> getFilterParmsMap() {
        return filterParmsMap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_contract_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_contract_list, null), params);
        init();
        initData();
        listener();
        loadFilterData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NEW_CONTRACT:
                case REQUEST_CODE_DETAIL_CONTRACT:
                    Fragment fragment1 = getFragment(0);
                    if (fragment1 != null && fragment1 instanceof ContractListFragment) {
                        ContractListFragment contractListFragment = (ContractListFragment) fragment1;
                        contractListFragment.loadData();
                    }
                    Fragment fragment2 = getFragment(1);
                    if (fragment2 != null && fragment2 instanceof ContractListFragment) {
                        ContractListFragment contractListFragment = (ContractListFragment) fragment2;
                        contractListFragment.loadData();
                    }
                    break;
            }
        }
    }

    private void loadFilterData() {
        HttpRequest.create(UrlConstant.FILTER_CONFIGT_URL)
                .putParam(new JsonParam("params")
                        .putParamValue("tab", "contract")).get(new CallBack<ArrayList<FilterEntity>>() {
            @Override
            public ArrayList<FilterEntity> doInBackground(String response) {
                return (ArrayList<FilterEntity>) JsonUtils.parseList(response, FilterEntity.class);
            }

            @Override
            public void success(ArrayList<FilterEntity> filterEntities) {
                ContractListActivity.this.filterEntities = filterEntities;
                contract_list_title_filter_ll.setVisibility(View.VISIBLE);
                statusLayout.normalStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }
        });
    }

    private void init() {
        contract_list_title_tb = findViewById(R.id.contract_list_title_tb);
        contract_list_title_search_iv = findViewById(R.id.contract_list_title_search_iv);
        contract_list_title_add_iv = findViewById(R.id.contract_list_title_add_iv);
        contract_list_title_hs = findViewById(R.id.contract_list_title_hs);
        contract_list_title_ttrg = findViewById(R.id.contract_list_title_ttrg);
        contract_list_title_my_ttrb = findViewById(R.id.contract_list_title_my_ttrb);
        customer_detail_title_coord_ttrb = findViewById(R.id.customer_detail_title_coord_ttrb);
        contract_list_title_filter_ll = findViewById(R.id.contract_list_title_filter_ll);
        contract_list_vp = findViewById(R.id.contract_list_vp);
    }

    private void initData() {
        adapter = new ContractListPagerAdapter(getSupportFragmentManager());
        contract_list_vp.setAdapter(adapter);
        contract_list_vp.setOffscreenPageLimit(1);
    }

    private void listener() {
        contract_list_title_ttrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.contract_list_title_my_ttrb:
                        contract_list_vp.setCurrentItem(0);
                        break;
                    case R.id.customer_detail_title_coord_ttrb:
                        contract_list_vp.setCurrentItem(1);
                        break;
                }
            }
        });
        contract_list_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                contract_list_title_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    contract_list_title_my_ttrb.setChecked(true);
                    scrollTitle(contract_list_title_my_ttrb);
                } else {
                    customer_detail_title_coord_ttrb.setChecked(true);
                    scrollTitle(customer_detail_title_coord_ttrb);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        contract_list_title_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.filter(context, filterEntities, filterCallback);
            }
        });
        contract_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadFilterData();
            }

            @Override
            public void onNetFailClick() {
                loadFilterData();
            }

            @Override
            public void onExpandClick() {

            }
        });
        contract_list_title_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractSearchActivity.start(context);
            }
        });
        contract_list_title_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractNewActivity.start(ContractListActivity.this, REQUEST_CODE_NEW_CONTRACT);
            }
        });
    }

    private void scrollTitle(View view) {
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = contract_list_title_filter_ll.getLeft() - CommonUtils.dp2pxForInt(context, 5);
        final int childWidth = right - left;
        int scrollX = left + CommonUtils.dp2pxForInt(context, 15) - (screenWith - childWidth) / 2;
        contract_list_title_hs.smoothScrollTo(scrollX, 0);
    }

    private void refresh() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            ContractListFragment contractListFragment = getFragment(i);
            if (contractListFragment != null) {
                contractListFragment.refreshData();
            }
        }
    }

    private ContractListFragment getFragment(int index) {
        String tagName = "android:switcher:" + contract_list_vp.getId() + ":" + adapter.getItemId(index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagName);
        if (fragment != null && fragment instanceof ContractListFragment) {
            return (ContractListFragment) fragment;
        }
        return null;
    }
}
