package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.baihe.lihepro.entity.CategoryEditEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-16
 * Description：
 */
public class CategoryEditActivity extends BaseActivity {
    private static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";
    private static final String INTENT_CATEGORY_ID = "INTENT_CATEGORY_ID";
    private static final String INTENT_CATEGORY_NAME = "INTENT_CATEGORY_NAME";
    private static final String INTENT_TYPE = "INTENT_TYPE";

    public static void start(Activity activity, String customerId, String categoryId, String categoryName, int entryType, int requestCode) {
        Intent intent = new Intent(activity, CategoryEditActivity.class);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        intent.putExtra(INTENT_CATEGORY_ID, categoryId);
        intent.putExtra(INTENT_CATEGORY_NAME, categoryName);
        intent.putExtra(INTENT_TYPE, entryType);
        activity.startActivityForResult(intent, requestCode);
    }

    private int entryType;
    private String customerId;
    private String categoryId;
    private String categoryName;
    private CategoryEditEntity categoryEditEntity;

    private KeyValueEditLayout category_edit_main_kvel;
    private KeyValueEditLayout category_edit_other_kvel;
    private TextView category_eidt_ok_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entryType = getIntent().getIntExtra(INTENT_TYPE, CustomerDetailActivity.ENTRY_TYPE_CUSTOMER);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        categoryId = getIntent().getStringExtra(INTENT_CATEGORY_ID);
        categoryName = getIntent().getStringExtra(INTENT_CATEGORY_NAME);
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER) {
            setTitleText("编辑" + categoryName + "客服单");
        } else {
            setTitleText("编辑" + categoryName + "邀约单");
        }
        setContentView(R.layout.activity_category_edit);
        init();
        listener();
        loadData();
    }

    private void init() {
        category_edit_main_kvel = findViewById(R.id.category_edit_main_kvel);
        category_edit_other_kvel = findViewById(R.id.category_edit_other_kvel);
        category_eidt_ok_tv = findViewById(R.id.category_eidt_ok_tv);
    }

    private void listener() {
        category_edit_main_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {

            }
        });
        category_eidt_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadData();
            }

            @Override
            public void onNetFailClick() {
                loadData();
            }

            @Override
            public void onExpandClick() {

            }
        });
    }

    private void loadData() {
        String url;
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER) {
            url = UrlConstant.CUSTOMER_LEADS_URL;
        } else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT) {
            url = UrlConstant.REQ_LIST_URL;
        } else {
            return;
        }
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("aggregation", "1").putParamValue("category", categoryId);
        HttpRequest.create(url).putParam(jsonParam).get(new CallBack<CategoryEditEntity>() {
            @Override
            public CategoryEditEntity doInBackground(String response) {
                return JsonUtils.parse(response, CategoryEditEntity.class);
            }

            @Override
            public void success(CategoryEditEntity categoryEditEntity) {
                statusLayout.normalStatus();
                CategoryEditActivity.this.categoryEditEntity = categoryEditEntity;
                category_edit_main_kvel.setData(categoryEditEntity.getCategory_data());
                category_edit_other_kvel.setData(categoryEditEntity.getOther_info());
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

    private void commit() {
        Map<String, Object> data = new HashMap<>();
        //取出具体品类录入
        Map<String, Object> dataCategoryDetail = category_edit_main_kvel.commit();
        if (dataCategoryDetail != null) {
            //提交数据
            //没有finalCategory需要把主品类值放入finalCategory
            if (!dataCategoryDetail.containsKey("finalCategory")) {
                dataCategoryDetail.put("finalCategory", categoryId);
            }
            //组装数据
            Map<String, Object> childCategory = new HashMap<>();
            childCategory.put(categoryEditEntity.getParamKey(), dataCategoryDetail);
            if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER) {
                data.put("req", childCategory);
                data.put("leads_id", categoryEditEntity.getId());
            } else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT) {
                data.put("categoryReq", childCategory);
                data.put("reqId", categoryEditEntity.getId());
            } else {
                return;
            }
            data.put("customer_id", customerId);
            data.put("customerId", customerId);
            //提交数据
            updatCategory(entryType, data);
        }
    }

    private void updatCategory(int entryType, Map<String, Object> data) {
        String url;
        if (entryType == CustomerDetailActivity.ENTRY_TYPE_CUSTOMER) {
            url = UrlConstant.UPDATE_LEADS_URL;
        } else if (entryType == CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT) {
            url = UrlConstant.UPDATE_REQ_URL;
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
                ToastUtils.toast("更新成功");
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
