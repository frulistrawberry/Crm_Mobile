package com.baihe.lihepro.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.SalesDetailPagerAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.BottomSelectDialog;
import com.baihe.lihepro.entity.BanquetUserEntity;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.entity.SalesDetailEntity;

import com.baihe.lihepro.fragment.SalesDetailContractFragment;
import com.baihe.lihepro.fragment.SalesDetailCustomerFragment;
import com.baihe.lihepro.fragment.SalesDetailOrderFragment;
import com.baihe.lihepro.fragment.SalesDetailSalesFragment;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-31
 * Description：
 */
public class SalesDetailActivity extends BaseActivity {
    public static final String INTENT_ORDER_ID = "INTENT_ORDER_ID";
    public static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";
    public static final String INTENT_CUSTOMER_NAME = "INTENT_CUSTOMER_NAME";
    public static final String INTENT_HIDDE_BOTTOM = "INTENT_HIDDE_BOTTOM";

    public static final int REQUEST_CODE_ADD_FOLLOW = 1;
    public static final int REQUEST_CODE_EDIT_CONTACT = 2;
    public static final int REQUEST_CODE_EIDT_CONTRACT = 3;
    public static final int REQUEST_CODE_NEW_CONTRACT = 4;
    public static final int REQUEST_CODE_NEW_AGREEMENT = 5;
    public static final int REQUEST_CODE_EDIT_AGREEMENT = 6;
    public static final int REQUEST_CODE_EDIT_ORDER = 7;
    public static final int REQUEST_CODE_EDIT_CUSTOMER = 8;

    public static void start(Context context, String orderId, String customerId, String customerName) {
        Intent intent = new Intent(context, SalesDetailActivity.class);
        intent.putExtra(INTENT_ORDER_ID, orderId);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
        context.startActivity(intent);
    }

    public static void start(Context context, String orderId, String customerId, String customerName, boolean isHideBottom) {
        Intent intent = new Intent(context, SalesDetailActivity.class);
        intent.putExtra(INTENT_ORDER_ID, orderId);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
        intent.putExtra(INTENT_HIDDE_BOTTOM, isHideBottom);
        context.startActivity(intent);
    }

    private Toolbar sales_detail_title_tb;
    private ImageView sales_detail_title_chenge;
    private TextView sales_detail_title_name_tv;
    private TextTransitionRadioGroup sales_detail_title_ttrg;
    private TextTransitionRadioButton sales_detail_title_order_ttrb;
    private TextTransitionRadioButton sales_detail_title_customer_ttrb;
    private TextTransitionRadioButton sales_detail_title_contract_ttrb;
    private TextTransitionRadioButton sales_detail_title_sales_ttrb;
    private TextTransitionRadioButton sales_detail_title_team_ttrb;
    private HorizontalScrollView customer_detail_title_hs;

    private ViewPager sales_detail_vp;
    private LinearLayout sales_detail_bottom_ll;
    private TextView sales_detail_botton1_tv;
    private TextView sales_detail_botton2_tv;


    private FragmentManager fragmentManager;
    private SalesDetailPagerAdapter adapter;
    private String orderId;
    private String customerId;
    private String customerName;
    private boolean isHideBottom;
    private List<BanquetUserEntity> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //push消息
        Uri uri = getIntent().getData();
        Intent intent = getIntent();
        if (uri != null && intent != null) {
            String orderId = uri.getQueryParameter("orderId");
            String customerId = uri.getQueryParameter("customerId");
            String customerName = uri.getQueryParameter("customerName");
            intent.putExtra(INTENT_ORDER_ID, orderId);
            intent.putExtra(INTENT_CUSTOMER_ID, customerId);
            intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
        }

        setTitleView(R.layout.activity_sales_detail_title);

        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_sales_detail, null), params);
        init();
        initData();
        listener();
        loadData();
    }

    private void scrollTitle(View view, int index) {
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

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params");
        HttpRequest.create(UrlConstant.BANQUET_USER_LIST).putParam(jsonParam).get(new CallBack<List<BanquetUserEntity>>() {
            @Override
            public List<BanquetUserEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, BanquetUserEntity.class);
            }

            @Override
            public void success(List<BanquetUserEntity> entity) {
                userList = entity;
            }

            @Override
            public void error() {
            }

            @Override
            public void fail() {
            }

            @Override
            public void before() {
                super.before();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_FOLLOW: {
                    sales_detail_bottom_ll.setVisibility(View.GONE);
                    if (fragmentManager != null) {
                        Fragment fragment1 = getFragment(0);
                        if (fragment1 != null && fragment1 instanceof SalesDetailOrderFragment) {
                            SalesDetailOrderFragment salesDetailOrderFragment = (SalesDetailOrderFragment) fragment1;
                            salesDetailOrderFragment.loadData();
                        }
                        Fragment fragment2 = getFragment(1);
                        if (fragment2 != null && fragment2 instanceof SalesDetailCustomerFragment) {
                            SalesDetailCustomerFragment salesDetailCustomerFragment = (SalesDetailCustomerFragment) fragment2;
                            salesDetailCustomerFragment.loadData();
                        }
                        Fragment fragment3 = getFragment(2);
                        if (fragment3 != null && fragment3 instanceof SalesDetailContractFragment) {
                            SalesDetailContractFragment salesDetailContractFragment = (SalesDetailContractFragment) fragment3;
                            salesDetailContractFragment.loadData();
                        }
                        Fragment fragment = getFragment(3);
                        if (fragment != null && fragment instanceof SalesDetailSalesFragment) {
                            SalesDetailSalesFragment salesDetailSalesFragment = (SalesDetailSalesFragment) fragment;
                            salesDetailSalesFragment.refresh();
                        }
                    }
                }
                break;
                case REQUEST_CODE_EDIT_CONTACT: {
                    if (fragmentManager != null) {
                        Fragment fragment = getFragment(1);
                        if (fragment != null && fragment instanceof SalesDetailCustomerFragment) {
                            SalesDetailCustomerFragment salesDetailCustomerFragment = (SalesDetailCustomerFragment) fragment;
                            salesDetailCustomerFragment.loadData();
                        }
                    }
                }
                break;
                case REQUEST_CODE_EIDT_CONTRACT:
                case REQUEST_CODE_NEW_CONTRACT:
                case REQUEST_CODE_NEW_AGREEMENT:
                case REQUEST_CODE_EDIT_AGREEMENT:
                case REQUEST_CODE_EDIT_ORDER:
                case REQUEST_CODE_EDIT_CUSTOMER: {
                    sales_detail_bottom_ll.setVisibility(View.GONE);
                    if (fragmentManager != null) {
                        Fragment fragment1 = getFragment(0);
                        if (fragment1 != null && fragment1 instanceof SalesDetailOrderFragment) {
                            SalesDetailOrderFragment salesDetailOrderFragment = (SalesDetailOrderFragment) fragment1;
                            salesDetailOrderFragment.loadData();
                        }
                        Fragment fragment2 = getFragment(1);
                        if (fragment2 != null && fragment2 instanceof SalesDetailCustomerFragment) {
                            SalesDetailCustomerFragment salesDetailCustomerFragment = (SalesDetailCustomerFragment) fragment2;
                            salesDetailCustomerFragment.loadData();
                        }
                        Fragment fragment3 = getFragment(2);
                        if (fragment3 != null && fragment3 instanceof SalesDetailContractFragment) {
                            SalesDetailContractFragment salesDetailContractFragment = (SalesDetailContractFragment) fragment3;
                            salesDetailContractFragment.loadData();
                        }
                    }
                }
                break;
            }
        }
    }

    private void init() {
        sales_detail_title_tb = findViewById(R.id.sales_detail_title_tb);
        sales_detail_title_chenge = findViewById(R.id.sales_detail_title_change);
        sales_detail_title_name_tv = findViewById(R.id.sales_detail_title_name_tv);
        sales_detail_title_ttrg = findViewById(R.id.sales_detail_title_ttrg);
        sales_detail_title_order_ttrb = findViewById(R.id.sales_detail_title_order_ttrb);
        sales_detail_title_customer_ttrb = findViewById(R.id.sales_detail_title_customer_ttrb);
        sales_detail_title_contract_ttrb = findViewById(R.id.sales_detail_title_contract_ttrb);
        sales_detail_title_sales_ttrb = findViewById(R.id.sales_detail_title_sales_ttrb);
        sales_detail_title_team_ttrb = findViewById(R.id.sales_detail_title_team_ttrb);
        customer_detail_title_hs = findViewById(R.id.customer_detail_title_hs);

        sales_detail_vp = findViewById(R.id.sales_detail_vp);
        sales_detail_bottom_ll = findViewById(R.id.sales_detail_bottom_ll);
        sales_detail_botton1_tv = findViewById(R.id.sales_detail_botton1_tv);
        sales_detail_botton2_tv = findViewById(R.id.sales_detail_botton2_tv);

    }

    private void initData() {
        orderId = getIntent().getStringExtra(INTENT_ORDER_ID);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        customerName = getIntent().getStringExtra(INTENT_CUSTOMER_NAME);
        sales_detail_title_name_tv.setText(customerName + "的销售详情");

        fragmentManager = getSupportFragmentManager();
        loadData2();


    }

    public void loadData2() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("orderId", orderId);
        HttpRequest.create(UrlConstant.ORDER_DETAIL_URL).putParam(jsonParam).get(new CallBack<SalesDetailEntity>() {
            @Override
            public SalesDetailEntity doInBackground(String response) {
                SalesDetailEntity salesDetailEntity;
                if ("[]".equals(response)) {
                    salesDetailEntity = new SalesDetailEntity();
                } else {
                    salesDetailEntity = JsonUtils.parse(response, SalesDetailEntity.class);
                }
                return salesDetailEntity;
            }

            @Override
            public void success(SalesDetailEntity entities) {
                adapter = new SalesDetailPagerAdapter(fragmentManager, orderId, customerId,entities.getOrderInfo().getCategory(),entities.getOrderInfo().getCategory_text());
                sales_detail_vp.setAdapter(adapter);
                sales_detail_vp.setOffscreenPageLimit(3);
            }

            @Override
            public void error() {
            }

            @Override
            public void fail() {
            }

            @Override
            public void before() {
                super.before();
            }
        });
    }


    private void listener() {
        sales_detail_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sales_detail_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                sales_detail_title_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sales_detail_title_order_ttrb.setChecked(true);
                        scrollTitle(sales_detail_title_order_ttrb,0);
                        break;
                    case 1:
                        sales_detail_title_customer_ttrb.setChecked(true);
                        scrollTitle(sales_detail_title_customer_ttrb,1);
                        break;
                    case 2:
                        sales_detail_title_contract_ttrb.setChecked(true);
                        scrollTitle(sales_detail_title_contract_ttrb,2);
                        break;
                    case 3:
                        sales_detail_title_sales_ttrb.setChecked(true);
                        scrollTitle(sales_detail_title_sales_ttrb,3);
                        break;
                    case 4:
                        sales_detail_title_team_ttrb.setChecked(true);
                        scrollTitle(sales_detail_title_team_ttrb,4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sales_detail_title_ttrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sales_detail_title_order_ttrb:
                        sales_detail_vp.setCurrentItem(0);
                        break;
                    case R.id.sales_detail_title_customer_ttrb:
                        sales_detail_vp.setCurrentItem(1);
                        break;
                    case R.id.sales_detail_title_contract_ttrb:
                        sales_detail_vp.setCurrentItem(2);
                        break;
                    case R.id.sales_detail_title_sales_ttrb:
                        sales_detail_vp.setCurrentItem(3);
                        break;
                    case R.id.sales_detail_title_team_ttrb:
                        sales_detail_vp.setCurrentItem(4);
                        break;
                }
            }
        });
        sales_detail_title_chenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userList!=null){
                    new BottomSelectDialog.Builder(SalesDetailActivity.this)
                            .setTitle("选择转单接收人")
                            .setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirm(Dialog dialog, int position) {
                                    JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId",customerId).putParamValue("associatesId",userList.get(position).getUser_id());
                                    HttpRequest.create(UrlConstant.A_KEY_TO_BANQUET).putParam(jsonParam).get(new CallBack<String>() {
                                        @Override
                                        public String doInBackground(String response) {
                                            return response;
                                        }

                                        @Override
                                        public void success(String entity) {
                                            ToastUtils.toast(entity);
                                            if (fragmentManager != null) {
                                                Fragment fragment1 = getFragment(0);
                                                if (fragment1 != null && fragment1 instanceof SalesDetailOrderFragment) {
                                                    SalesDetailOrderFragment salesDetailOrderFragment = (SalesDetailOrderFragment) fragment1;
                                                    salesDetailOrderFragment.loadData();
                                                }
                                                Fragment fragment2 = getFragment(1);
                                                if (fragment2 != null && fragment2 instanceof SalesDetailCustomerFragment) {
                                                    SalesDetailCustomerFragment salesDetailCustomerFragment = (SalesDetailCustomerFragment) fragment2;
                                                    salesDetailCustomerFragment.loadData();
                                                }
                                                Fragment fragment3 = getFragment(2);
                                                if (fragment3 != null && fragment3 instanceof SalesDetailContractFragment) {
                                                    SalesDetailContractFragment salesDetailContractFragment = (SalesDetailContractFragment) fragment3;
                                                    salesDetailContractFragment.loadData();
                                                }
                                                Fragment fragment = getFragment(3);
                                                if (fragment != null && fragment instanceof SalesDetailSalesFragment) {
                                                    SalesDetailSalesFragment salesDetailSalesFragment = (SalesDetailSalesFragment) fragment;
                                                    salesDetailSalesFragment.refresh();
                                                }
                                            }
                                        }

                                        @Override
                                        public void error() {
                                            ToastUtils.toastNetError();
                                        }

                                        @Override
                                        public void fail() {
                                            ToastUtils.toastNetWorkFail();
                                        }

                                        @Override
                                        public void before() {
                                            super.before();
                                        }
                                    });

                                }
                            }).setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
                        @Override
                        public int getCount() {
                            return userList.size();
                        }

                        @Override
                        public String getText(int dataPostion) {
                            return userList.get(dataPostion).getName();
                        }
                    }).build().show();
                }
            }
        });

    }

    private Fragment getFragment(int index) {
        String tagName = "android:switcher:" + sales_detail_vp.getId() + ":" + adapter.getItemId(index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagName);
        return fragment;
    }


    public void setButtonTypeData(final SalesDetailEntity salesDetail) {
        if (salesDetail.getOrderInfo().getCategory().equals("2")){
            sales_detail_title_chenge.setVisibility(View.VISIBLE);
        }else {
            sales_detail_title_chenge.setVisibility(View.GONE);
        }
        isHideBottom = getIntent().getBooleanExtra(INTENT_HIDDE_BOTTOM, false);
        if (isHideBottom) {
            sales_detail_bottom_ll.setVisibility(View.GONE);
        } else {
            Utils.bottomButtons(sales_detail_bottom_ll, sales_detail_botton1_tv, sales_detail_botton2_tv, salesDetail.getButtonType(), new Utils.BottomCallback() {
                @Override
                public void click(int type) {
                    bottomButtonClick(salesDetail, type);
                }
            });
        }
    }

    public void bottomButtonClick(SalesDetailEntity salesDetail, int type) {
        if (type == 104) { //录跟进-销售详情页
            SalesFollowAddActivity.start(SalesDetailActivity.this, orderId, customerId, salesDetail.getFollowData(), salesDetail.getReserveId(), salesDetail.getReserveConfirmCount(), REQUEST_CODE_ADD_FOLLOW);
        } else if (type == 105) {  //签订协议-销售详情页
            if (salesDetail.getContractInfo() != null && salesDetail.getContractInfo().size() > 0) {
                if (salesDetail.getOrderInfo().getCategory().equals("1")||salesDetail.getOrderInfo().getCategory().equals("6")){
                    ProtocolDeskActivity.start(this,salesDetail.getContractInfo().get(0).getContract_id(),REQUEST_CODE_NEW_AGREEMENT);
                }else {
                    ProtocolActivity.start(this, salesDetail.getContractInfo().get(0).getContract_id(), REQUEST_CODE_NEW_AGREEMENT);
                }
            }
        } else if (type == 106) {  //新建合同-销售详情页
            final ListItemEntity listItemEntity = new ListItemEntity();
            listItemEntity.setCustomer_name(salesDetail.getOrderInfo().getCustomer_name());
            listItemEntity.setCustomer_id(salesDetail.getOrderInfo().getCustomer_id());
            listItemEntity.setOrder_num(salesDetail.getOrderInfo().getOrder_num());
            listItemEntity.setOrder_id(salesDetail.getOrderInfo().getOrder_id());
            listItemEntity.setCategory_text(salesDetail.getOrderInfo().getCategory_text());
            listItemEntity.setCategory(salesDetail.getOrderInfo().getCategory());
            if ("1".equals(listItemEntity.getCategory())||"6".equals(listItemEntity.getCategory())||"4".equals(listItemEntity.getCategory())||Integer.valueOf(listItemEntity.getCategory())>7){
                String companyId = AccountManager.newInstance().getUser().getCompany_id();
                String[] items = new String[]{"代收代付","酒店直签","自营签单"};
                String[] contractType = new String[]{"1","2","3"};
                if ("31".equals(AccountManager.newInstance().getUser().getCompany_id())){
                    items = new String[]{"自营签单"};
                    contractType = new String[]{"3"};
                }else if ("20".equals(companyId)||"6".equals(companyId)){
                    items = new String[]{"代收代付","酒店直签"};
                    contractType = new String[]{"1","2"};
                }
                String[] finalItems = items;
                String[] finalContractType = contractType;
                new BottomSelectDialog.Builder(context)
                        .setTitle("请选择合同类型")
                        .setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
                            @Override
                            public int getCount() {
                                return finalItems.length;
                            }

                            @Override
                            public String getText(int dataPostion) {
                                return finalItems[dataPostion];
                            }

                            @Override
                            public int initSelectDataPosition() {
                                return -1;
                            }
                        }).setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, int position) {
                        if (-1 != position) {
                            ContractNewActivity.start(SalesDetailActivity.this,listItemEntity.getOrder_id(), finalContractType[position],REQUEST_CODE_NEW_CONTRACT);
                        }else {
                            ToastUtils.toast("请选择合同类型");
                        }
                    }
                }).setOnCancelClickListener(new BottomSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();

            } else {
                ContractNewActivity.start(SalesDetailActivity.this, listItemEntity.getOrder_id(), REQUEST_CODE_NEW_CONTRACT);
            }
        }
    }

}
