package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.CustomerDetailPagerAdapter;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.fragment.CustomerDetailBaseFragment;
import com.baihe.lihepro.fragment.CustomerDetailContractFragment;
import com.baihe.lihepro.fragment.CustomerDetailDemandFragment;
import com.baihe.lihepro.fragment.CustomerDetailRequirementFragment;
import com.baihe.lihepro.fragment.CustomerDetailSalesFragment;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailActivity extends BaseActivity {
    public static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";
    public static final String INTENT_CUSTOMER_NAME = "INTENT_CUSTOMER_NAME";
    public static final String INTENT_CUSTOMER_TAB = "INTENT_CUSTOMER_TAB";
    public static final String INTENT_CUSTOMER_ENTRY_TYPE = "INTENT_CUSTOMER_ENTRY_TYPE";

    public static final int REQUEST_CODE_ADD_FOLLOW = 1;
    public static final int REQUEST_CODE_EDIT_CUSTOMER = 2;
    public static final int REQUEST_CODE_EDIT_CONTACT = 3;
    public static final int REQUEST_CODE_ADD_CATEGORY = 4;
    public static final int REQUEST_CODE_EIDT_CATEGORY = 5;
    public static final int REQUEST_CODE_EIDT_CONTRACT = 6;
    public static final int REQUEST_CODE_EIDT_AGREEMENT = 7;

    public static final int ENTRY_TYPE_CUSTOMER = 0;
    public static final int ENTRY_TYPE_REQUIREMENT = 1;
    public static final int ENTRY_TYPE_CUSTOMER_SERVICE = 2;

    public static void start(Context context, String customerId, String customerName, String customerTab, int entryType) {
        Intent intent = new Intent(context, CustomerDetailActivity.class);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
        intent.putExtra(INTENT_CUSTOMER_TAB, customerTab);
        intent.putExtra(INTENT_CUSTOMER_ENTRY_TYPE, entryType);
        context.startActivity(intent);
    }

    private Toolbar customer_detail_title_tb;
    private TextView customer_detail_title_name_tv;
    private HorizontalScrollView customer_detail_title_hs;
    private TextTransitionRadioGroup customer_detail_title_ttrg;
    private TextTransitionRadioButton customer_detail_title_base_ttrb;
    private TextTransitionRadioButton customer_detail_title_sales_ttrb;
    private TextTransitionRadioButton customer_detail_title_demand_ttrb;
    private TextTransitionRadioButton customer_detail_title_requirement_ttrb;
    private TextTransitionRadioButton customer_detail_title_contract_ttrb;
    private TextTransitionRadioButton customer_detail_title_team_ttrb;
    private ViewPager customer_detail_vp;
    private LinearLayout customer_detail_bottom_ll;
    private TextView customer_detail_add_category_tv;
    private TextView customer_detail_record_follow_tv;

    private FragmentManager fragmentManager;
    private CustomerDetailPagerAdapter adapter;
    private String customerId;
    private String customerName;
    private String customerTab;
    private int entryType;
    private int showFollowButton;

    private List<KeyValueEntity> followConfigData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //push消息
        Uri uri = getIntent().getData();
        Intent intent = getIntent();
        if (uri != null && intent != null) {
            String customerId = uri.getQueryParameter("customerId");
            String customerName = uri.getQueryParameter("customerName");
            String customerTab = uri.getQueryParameter("customerTab");
            String entryType = uri.getQueryParameter("entryType");
            intent.putExtra(INTENT_CUSTOMER_ID, customerId);
            intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
            intent.putExtra(INTENT_CUSTOMER_TAB, customerTab);
            intent.putExtra(INTENT_CUSTOMER_ENTRY_TYPE, entryType);
        }

        entryType = getIntent().getIntExtra(INTENT_CUSTOMER_ENTRY_TYPE, ENTRY_TYPE_CUSTOMER);
        if (entryType == ENTRY_TYPE_CUSTOMER) {
            setTitleView(R.layout.activity_customer_detail_title);
        } else if (entryType == ENTRY_TYPE_REQUIREMENT){
            setTitleView(R.layout.activity_customer_detail_title2);
        }else if(entryType == ENTRY_TYPE_CUSTOMER_SERVICE){
            setTitleView(R.layout.activity_customer_detail_title3);
        }
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_customer_detail, null), params);
        init();
        initData();
        listener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_FOLLOW: {
                    if (fragmentManager != null) {
                        Fragment fragment = getFragment(0);
                        if (fragment != null && fragment instanceof CustomerDetailBaseFragment) {
                            CustomerDetailBaseFragment customerDetailBaseFragment = (CustomerDetailBaseFragment) fragment;
                            customerDetailBaseFragment.loadData();
                        }
                        fragment = getFragment(1);
                        if (fragment != null && fragment instanceof CustomerDetailSalesFragment) {
                            CustomerDetailSalesFragment customerDetailSalesFragment = (CustomerDetailSalesFragment) fragment;
                            customerDetailSalesFragment.refresh();
                        }
                        fragment = getFragment(2);
                        if (fragment != null && fragment instanceof CustomerDetailDemandFragment) {
                            CustomerDetailDemandFragment customerDetailDemandFragment = (CustomerDetailDemandFragment) fragment;
                            customerDetailDemandFragment.loadData();
                        }
                        fragment = getFragment(3);
                        if (fragment != null && fragment instanceof CustomerDetailRequirementFragment) {
                            CustomerDetailRequirementFragment customerDetailRequirementFragment = (CustomerDetailRequirementFragment) fragment;
                            customerDetailRequirementFragment.refresh();
                        }
                    }
                }
                break;
                case REQUEST_CODE_EDIT_CUSTOMER:
                case REQUEST_CODE_EDIT_CONTACT: {
                    if (fragmentManager != null) {
                        Fragment fragment = getFragment(0);
                        if (fragment != null && fragment instanceof CustomerDetailBaseFragment) {
                            CustomerDetailBaseFragment customerDetailBaseFragment = (CustomerDetailBaseFragment) fragment;
                            customerDetailBaseFragment.loadData();
                        }
                    }
                }
                break;
                case REQUEST_CODE_ADD_CATEGORY:
                case REQUEST_CODE_EIDT_CATEGORY: {
                    if (fragmentManager != null) {
                        if (entryType == ENTRY_TYPE_CUSTOMER) {
                            Fragment fragment = getFragment(2);
                            if (fragment != null && fragment instanceof CustomerDetailDemandFragment) {
                                CustomerDetailDemandFragment customerDetailDemandFragment = (CustomerDetailDemandFragment) fragment;
                                customerDetailDemandFragment.loadData();
                            }
                        } else if (entryType == ENTRY_TYPE_REQUIREMENT) {
                            //邀约详情录完跟进要刷新baseInfo里的followData
                            Fragment fragment = getFragment(0);
                            if (fragment != null && fragment instanceof CustomerDetailBaseFragment) {
                                CustomerDetailBaseFragment customerDetailBaseFragment = (CustomerDetailBaseFragment) fragment;
                                customerDetailBaseFragment.loadData();
                            }
                            fragment = getFragment(3);
                            if (fragment != null && fragment instanceof CustomerDetailRequirementFragment) {
                                CustomerDetailRequirementFragment customerDetailRequirementFragment = (CustomerDetailRequirementFragment) fragment;
                                customerDetailRequirementFragment.refresh();
                            }
                        }else if (entryType == ENTRY_TYPE_CUSTOMER_SERVICE){
                            Fragment fragment = getFragment(0);
                            if (fragment != null && fragment instanceof CustomerDetailBaseFragment) {
                                CustomerDetailBaseFragment customerDetailBaseFragment = (CustomerDetailBaseFragment) fragment;
                                customerDetailBaseFragment.loadData();
                            }
                        }
                    }
                }
                break;
                case REQUEST_CODE_EIDT_CONTRACT:
                case REQUEST_CODE_EIDT_AGREEMENT: {
                    if (fragmentManager != null) {
                        Fragment fragment = getFragment(4);
                        if (fragment != null && fragment instanceof CustomerDetailContractFragment) {
                            CustomerDetailContractFragment customerDetailContractFragment = (CustomerDetailContractFragment) fragment;
                            customerDetailContractFragment.loadData();
                        }
                    }
                }
                break;
            }
        }
    }

    private void init() {
        customer_detail_title_tb = findViewById(R.id.customer_detail_title_tb);
        customer_detail_title_name_tv = findViewById(R.id.customer_detail_title_name_tv);
        customer_detail_title_hs = findViewById(R.id.customer_detail_title_hs);
        customer_detail_title_ttrg = findViewById(R.id.customer_detail_title_ttrg);
        customer_detail_title_base_ttrb = findViewById(R.id.customer_detail_title_base_ttrb);
        customer_detail_title_sales_ttrb = findViewById(R.id.customer_detail_title_sales_ttrb);
        customer_detail_title_demand_ttrb = findViewById(R.id.customer_detail_title_demand_ttrb);
        customer_detail_title_requirement_ttrb = findViewById(R.id.customer_detail_title_requirement_ttrb);
        customer_detail_title_contract_ttrb = findViewById(R.id.customer_detail_title_contract_ttrb);
        customer_detail_title_team_ttrb = findViewById(R.id.customer_detail_title_team_ttrb);
        customer_detail_vp = findViewById(R.id.customer_detail_vp);
        customer_detail_bottom_ll = findViewById(R.id.customer_detail_bottom_ll);
        customer_detail_add_category_tv = findViewById(R.id.customer_detail_add_category_tv);
        customer_detail_record_follow_tv = findViewById(R.id.customer_detail_record_follow_tv);
    }

    private void initData() {
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        customerName = getIntent().getStringExtra(INTENT_CUSTOMER_NAME);
        customerTab = getIntent().getStringExtra(INTENT_CUSTOMER_TAB);
        if (entryType == ENTRY_TYPE_CUSTOMER) {
            if (TextUtils.isEmpty(customerName)){
                customer_detail_title_name_tv.setText("客户详情");
            }else {
                customer_detail_title_name_tv.setText(customerName + "的客户详情");
            }
        } else if (entryType == ENTRY_TYPE_REQUIREMENT){
            if (TextUtils.isEmpty(customerName)){
                customer_detail_title_name_tv.setText("邀约详情");
            }else {
                customer_detail_title_name_tv.setText(customerName + "的邀约详情");
            }
        }else if (entryType == ENTRY_TYPE_CUSTOMER_SERVICE){

            if (TextUtils.isEmpty(customerName)){
                customer_detail_title_name_tv.setText("客服详情");
            }else {
                customer_detail_title_name_tv.setText(customerName + "的客服详情");
            }
        }

        fragmentManager = getSupportFragmentManager();

        adapter = new CustomerDetailPagerAdapter(fragmentManager, customerId, customerTab, entryType);
        customer_detail_vp.setAdapter(adapter);
        customer_detail_vp.setOffscreenPageLimit(4);
    }

    private void listener() {
        customer_detail_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customer_detail_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                customer_detail_title_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        customer_detail_title_base_ttrb.setChecked(true);
                        scrollTitle(customer_detail_title_base_ttrb, 0);
                        break;
                    case 1:
                        customer_detail_title_sales_ttrb.setChecked(true);
                        scrollTitle(customer_detail_title_sales_ttrb, 1);
                        break;
                    case 2:
                        customer_detail_title_demand_ttrb.setChecked(true);
                        scrollTitle(customer_detail_title_demand_ttrb, 2);
                        break;
                    case 3:
                        if (entryType == ENTRY_TYPE_CUSTOMER_SERVICE){
                            customer_detail_title_team_ttrb.setChecked(true);
                        }else{
                            customer_detail_title_requirement_ttrb.setChecked(true);
                        }
                        scrollTitle(customer_detail_title_requirement_ttrb, 3);

                        break;
                    case 4:
                        if (entryType == ENTRY_TYPE_CUSTOMER){
                            customer_detail_title_contract_ttrb.setChecked(true);
                            scrollTitle(customer_detail_title_contract_ttrb, 4);
                        }else {
                            customer_detail_title_team_ttrb.setChecked(true);
                            scrollTitle(customer_detail_title_team_ttrb, 4);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        customer_detail_title_ttrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.customer_detail_title_base_ttrb:
                        customer_detail_vp.setCurrentItem(0);
                        break;
                    case R.id.customer_detail_title_sales_ttrb:
                        customer_detail_vp.setCurrentItem(1);
                        break;
                    case R.id.customer_detail_title_demand_ttrb:
                        customer_detail_vp.setCurrentItem(2);
                        break;
                    case R.id.customer_detail_title_requirement_ttrb:
                        customer_detail_vp.setCurrentItem(3);
                        break;
                    case R.id.customer_detail_title_contract_ttrb:
                        customer_detail_vp.setCurrentItem(4);
                        break;
                    case R.id.customer_detail_title_team_ttrb:
                        if (entryType == ENTRY_TYPE_CUSTOMER_SERVICE)
                            customer_detail_vp.setCurrentItem(3);
                        else
                            customer_detail_vp.setCurrentItem(4);
                        break;
                }
            }
        });
        customer_detail_add_category_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryAddActivity.start(CustomerDetailActivity.this, customerId, entryType, REQUEST_CODE_ADD_CATEGORY);
            }
        });
        customer_detail_record_follow_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followConfigData != null) {
                    FollowAddActivity.start(CustomerDetailActivity.this, customerId, followConfigData,entryType == ENTRY_TYPE_CUSTOMER_SERVICE?1:2 , REQUEST_CODE_ADD_FOLLOW);
                } else {
                    ToastUtils.toast("出现未知问题");
                }
            }
        });
    }

    public void setFollowConfigData(List<KeyValueEntity> followConfigData,int showFollowButton) {
        if (entryType == ENTRY_TYPE_REQUIREMENT || entryType == ENTRY_TYPE_CUSTOMER_SERVICE) {
            customer_detail_bottom_ll.setVisibility(View.VISIBLE);
        }
        if (entryType == ENTRY_TYPE_CUSTOMER_SERVICE){
            if (showFollowButton == 1){
                customer_detail_bottom_ll.setVisibility(View.VISIBLE);
            }else {
                customer_detail_bottom_ll.setVisibility(View.GONE);
            }
        }
        this.followConfigData = followConfigData;
    }

    private Fragment getFragment(int index) {
        String tagName = "android:switcher:" + customer_detail_vp.getId() + ":" + adapter.getItemId(index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagName);
        return fragment;
    }

    private void scrollTitle(View view, int index) {
        if (entryType != ENTRY_TYPE_CUSTOMER && entryType != ENTRY_TYPE_REQUIREMENT) {
            return;
        }
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = CommonUtils.getScreenWidth(context);
        final int childWidth = right - left;
        if (index < 2) {
            customer_detail_title_hs.smoothScrollTo(0, 0);
        } else if (index > 2) {
            customer_detail_title_hs.smoothScrollTo(screenWith, 0);
        } else {
            int scrollX = left + CommonUtils.dp2pxForInt(context, 24) - (screenWith - childWidth) / 2;
            if (scrollX > 0) {
                customer_detail_title_hs.smoothScrollTo(scrollX, 0);
            }
        }
    }
}
