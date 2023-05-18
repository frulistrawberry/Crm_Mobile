package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerEditEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-14
 * Description：
 */
public class CategoryAddActivity extends BaseActivity {
    private static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";
    private static final String INTENT_TYPE = "INTENT_TYPE";

    public static void start(Activity activity, String customerId, int entryType, int requestCode) {
        Intent intent = new Intent(activity, CategoryAddActivity.class);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        intent.putExtra(INTENT_TYPE, entryType);
        activity.startActivityForResult(intent, requestCode);
    }

    private KeyValueEditLayout category_add_name_kvel;
    private LinearLayout category_add_data_ll;
    private KeyValueEditLayout category_add_data_kvel;
    private TextView category_add_ok_tv;

    private int entryType;
    private String customerId;

    private CustomerEditEntity customerEditEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entryType = getIntent().getIntExtra(INTENT_TYPE, CustomerDetailActivity.ENTRY_TYPE_CUSTOMER);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        setTitleText("添加品类单");
        setContentView(R.layout.activity_category_add);
        init();
        listener();
        loadConfig();
    }

    private void init() {
        category_add_name_kvel = findViewById(R.id.category_add_name_kvel);
        category_add_data_ll = findViewById(R.id.category_add_data_ll);
        category_add_data_kvel = findViewById(R.id.category_add_data_kvel);
        category_add_ok_tv = findViewById(R.id.category_add_ok_tv);
    }

    private void listener() {
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadConfig();
            }

            @Override
            public void onNetFailClick() {
                loadConfig();
            }

            @Override
            public void onExpandClick() {

            }
        });
        category_add_name_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                String value = keyValueEntity.getDefaultVal();
                if (customerEditEntity != null && customerEditEntity.getCategoryItem(value) != null) {
                    category_add_data_kvel.setData(customerEditEntity.getCategoryItem(value));
                    category_add_data_ll.setVisibility(View.VISIBLE);
                } else {
                    category_add_data_kvel.clearData();
                    category_add_data_ll.setVisibility(View.GONE);
                }
            }
        });
        category_add_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void loadConfig() {
        HttpRequest.create(UrlConstant.GET_CATEGORY_DATA_URL).get(new CallBack<CustomerEditEntity>() {
            @Override
            public CustomerEditEntity doInBackground(String response) {
                return JsonUtils.parse(response, CustomerEditEntity.class);
            }

            @Override
            public void success(CustomerEditEntity entity) {
                statusLayout.normalStatus();
                initData(entity);
            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
    }

    private void initData(CustomerEditEntity customerEditEntity) {
        this.customerEditEntity = customerEditEntity;
        List<KeyValueEntity> categoryData = new ArrayList<>();
        categoryData.add(customerEditEntity.getCategory());
        category_add_name_kvel.setData(categoryData);
        String value = customerEditEntity.getCategory().getDefaultVal();
        if (!TextUtils.isEmpty(value)) {
            category_add_data_kvel.setData(customerEditEntity.getCategoryItem(value));
            category_add_data_ll.setVisibility(View.VISIBLE);
        }
    }

    private void commit() {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> dataCategory = category_add_name_kvel.commit();
        if (dataCategory == null) {
            return;
        }
        //选择了品类
        data.putAll(dataCategory);
        //取出具体品类录入
        Map<String, Object> dataCategoryDetail = category_add_data_kvel.commit();
        if (dataCategoryDetail != null) {
            //提交数据
            //没有finalCategory需要把主品类值放入finalCategory
            if (!dataCategoryDetail.containsKey("finalCategory")) {
                dataCategoryDetail.put("finalCategory", dataCategory.get("category"));
            }
            //组装数据
            Map<String, Object> childCategory = new HashMap<>();
            childCategory.put(customerEditEntity.getCategoryItemParamKey((String) dataCategory.get("category")), dataCategoryDetail);
            if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER || entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE) {
                data.put("req", childCategory);
            } else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT ) {
                data.put("categoryReq", childCategory);
            } else {
                return;
            }
            data.put("customer_id", customerId);
            data.put("customerId", customerId);
            //提交数据
            creatCategory(entryType, data);
        }
    }

    private void creatCategory(int entryType, Map<String, Object> data) {
        String url;
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER || entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE) {
            url = UrlConstant.INSERT_LEADS_URL;
        } else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT ) {
            url = UrlConstant.CREATE_REQ_URL;
        } else {
            return;
        }
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue(data);
        HttpRequest.create(url).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("品类单添加成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void before() {
                super.before();
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });
    }
}
