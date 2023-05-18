package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Context;
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
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.utils.InputEditUtils;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-11
 * Description：
 */
public class CustomerAddActivity extends BaseActivity {
    private static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";

    public static void start(Context context) {
        Intent intent = new Intent(context, CustomerAddActivity.class);
        context.startActivity(intent);
    }

    public static void start(Activity context) {
        Intent intent = new Intent(context, CustomerAddActivity.class);
        context.startActivityForResult(intent,103);
    }

    public static void start(Activity activity, String customerId, int requestCode) {
        Intent intent = new Intent(activity, CustomerAddActivity.class);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        activity.startActivityForResult(intent, requestCode);
    }

    private KeyValueEditLayout customer_add_base_kvel;
    private KeyValueEditLayout customer_add_category_kvel;
    private KeyValueEditLayout customer_add_category_data_kvel;
    private LinearLayout customer_add_category_ll;
    private LinearLayout customer_add_category_data_ll;
    private TextView customer_add_ok_tv;

    private CustomerEditEntity customerEditEntity = new CustomerEditEntity();
    private String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        if (TextUtils.isEmpty(customerId)) {
            setTitleText("新建客户");
        } else {
            setTitleText("编辑客户");
        }
        setContentView(R.layout.activity_customer_add);
        init();
        listener();
        loadBaseConfig();
    }

    private void init() {
        customer_add_base_kvel = findViewById(R.id.customer_add_base_kvel);
        customer_add_category_kvel = findViewById(R.id.customer_add_category_kvel);
        customer_add_category_data_kvel = findViewById(R.id.customer_add_category_data_kvel);
        customer_add_category_ll = findViewById(R.id.customer_add_category_ll);
        customer_add_category_data_ll = findViewById(R.id.customer_add_category_data_ll);
        customer_add_ok_tv = findViewById(R.id.customer_add_ok_tv);
    }

    private void listener() {
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                if (customerEditEntity.getBase() == null) {
                    loadBaseConfig();
                } else {
                    KeyValueEntity keyValueEntity = customer_add_base_kvel.findEntityByParamKey("channel");
                    if (keyValueEntity != null && !TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        loadCategoryConfig(keyValueEntity.getDefaultVal());
                    }
                }
            }

            @Override
            public void onNetFailClick() {
                if (customerEditEntity.getBase() == null) {
                    loadBaseConfig();
                } else {
                    KeyValueEntity keyValueEntity = customer_add_base_kvel.findEntityByParamKey("channel");
                    if (keyValueEntity != null && !TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        loadCategoryConfig(keyValueEntity.getDefaultVal());
                    }
                }
            }

            @Override
            public void onExpandClick() {

            }
        });
        customer_add_base_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                //微信和手机号只需要一项必填
                //其中一项填写另一项就自动非必填
                //两项都填写，两个再次变成必填
                //手机号paramkey: phone
                //微信paramkey: wechat
                if ("phone".equals(keyValueEntity.getEvent().getParamKey())) {
                    KeyValueEntity wechatKv = customer_add_base_kvel.findEntityByParamKey("wechat");
                    if (TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        if (TextUtils.isEmpty(wechatKv.getDefaultVal())) {  //两者都为空，都必填写
                            keyValueEntity.setOptional("1");
                            wechatKv.setOptional("1");
                        } else {  //微信不为空，微信必填写，手机非必填
                            keyValueEntity.setOptional("0");
                            wechatKv.setOptional("1");
                        }
                    } else {
                        if (TextUtils.isEmpty(wechatKv.getDefaultVal())) {  //手机不为空，手机必填写，微信非必填
                            keyValueEntity.setOptional("1");
                            wechatKv.setOptional("0");
                        } else {   //两者都不为空，都必填写
                            keyValueEntity.setOptional("1");
                            wechatKv.setOptional("1");
                        }
                    }
                    customer_add_base_kvel.refreshItem(keyValueEntity);
                    customer_add_base_kvel.refreshItem(wechatKv);
                } else if ("wechat".equals(keyValueEntity.getEvent().getParamKey())) {
                    KeyValueEntity phoneKv = customer_add_base_kvel.findEntityByParamKey("phone");
                    if (TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        if (TextUtils.isEmpty(phoneKv.getDefaultVal())) {   //两者都为空，都必填写
                            keyValueEntity.setOptional("1");
                            phoneKv.setOptional("1");
                        } else { //手机不为空，手机必填写，微信非必填
                            keyValueEntity.setOptional("0");
                            phoneKv.setOptional("1");
                        }
                    } else {
                        if (TextUtils.isEmpty(phoneKv.getDefaultVal())) {   //微信不为空，微信必填写，手机非必填
                            keyValueEntity.setOptional("1");
                            phoneKv.setOptional("0");
                        } else {  //两者都不为空，都必填写
                            keyValueEntity.setOptional("1");
                            phoneKv.setOptional("1");
                        }
                    }
                    customer_add_base_kvel.refreshItem(keyValueEntity);
                    customer_add_base_kvel.refreshItem(phoneKv);
                } else if ("channel".equals(keyValueEntity.getEvent().getParamKey())) {  //新建状态的客户涞源
                    if (TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        customer_add_category_ll.setVisibility(View.GONE);
                        customer_add_category_data_ll.setVisibility(View.GONE);
                    } else {
                        Map<String,Object> extra = new HashMap<>();
                        extra.put("channelId",keyValueEntity.getDefaultVal());
                        extra.put("type","1");
                        extra.put("companyId", AccountManager.newInstance().getUser().getCompany_id());
                        KeyValueEntity entity =  customer_add_base_kvel.findEntityByParamKey("recordUserId");
                        entity.setExtra(extra);
                        loadCategoryConfig(keyValueEntity.getDefaultVal());
                    }
                }
            }
        });
        customer_add_category_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                String value = keyValueEntity.getDefaultVal();
                if (customerEditEntity != null && customerEditEntity.getCategoryItem(value) != null) {
                    customer_add_category_data_kvel.setData(customerEditEntity.getCategoryItem(value));
                    customer_add_category_data_ll.setVisibility(View.VISIBLE);
                } else {
                    customer_add_category_data_kvel.clearData();
                    customer_add_category_data_ll.setVisibility(View.GONE);
                }
            }
        });
        customer_add_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private int findNumCount(String str){
        int count = 0;
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i]>='0' && array[i]<='9'){
                count++;
            }
        }
        return count;
    }

    private void commit() {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> dataBase = customer_add_base_kvel.commit();
        if (dataBase != null) {
            if (dataBase.containsKey("comment")){
                String comment = (String) dataBase.get("comment");
                if (!TextUtils.isEmpty(comment)){
                    if (findNumCount(comment)>3){
                        ToastUtils.toast("备注输入输入数字累计不允许超过3个！");
                        return;
                    }
                }
            }
            data.putAll(dataBase);
            Map<String, Object> dataCategory = customer_add_category_kvel.commit();
            if (dataCategory == null) {
                return;
            }
            if (dataCategory.containsKey("category")) {
                //选择了品类
                data.putAll(dataCategory);
                //取出具体品类录入
                Map<String, Object> dataCategoryDetail = customer_add_category_data_kvel.commit();
                if (dataCategoryDetail != null) {
                    //提交数据
                    //没有finalCategory需要把主品类值放入finalCategory
                    if (!dataCategoryDetail.containsKey("finalCategory")) {
                        dataCategoryDetail.put("finalCategory", dataCategory.get("category"));
                    }
                    //组装数据
                    Map<String, Object> childCategory = new HashMap<>();
                    childCategory.put(customerEditEntity.getCategoryItemParamKey((String) dataCategory.get("category")), dataCategoryDetail);
                    data.put("categoryReq", childCategory);
                    //提交数据
                    createOrUpdateCustomer(data);
                }
            } else {
                //品类不必填，并且未选择品类
                //提交数据
                createOrUpdateCustomer(data);
            }
        }

    }

    private void loadBaseConfig() {
        statusLayout.loadingStatus();
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.CREATE_PARAMS_URL).putParam(jsonParam).get(new CallBack<CustomerEditEntity>() {
            @Override
            public CustomerEditEntity doInBackground(String response) {
                return JsonUtils.parse(response, CustomerEditEntity.class);
            }

            @Override
            public void success(CustomerEditEntity customerEditEntity) {
                statusLayout.normalStatus();
                CustomerAddActivity.this.customerEditEntity.setBase(customerEditEntity.getBase());
                customer_add_base_kvel.setData(customerEditEntity.getBase());
                KeyValueEntity keyValueEntity = customer_add_base_kvel.findEntityByParamKey("name");
                if (keyValueEntity != null) {
                    Map<String,Object> extras = new HashMap<>();
                    extras.put("inputFilter", InputEditUtils.FILTER_CHINESE);
                    keyValueEntity.setExtra(extras);
                }

                KeyValueEntity entity = customer_add_base_kvel.findEntityByParamKey("channel");
                if (!TextUtils.isEmpty(entity.getDefaultVal())){
                    Map<String,Object> extra = new HashMap<>();
                    extra.put("channelId",entity.getDefaultVal());
                    extra.put("type","1");
                    extra.put("companyId", AccountManager.newInstance().getUser().getCompany_id());
                    KeyValueEntity entity1 =  customer_add_base_kvel.findEntityByParamKey("recordUserId");
                    entity1.setExtra(extra);
                }



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

    private void loadCategoryConfig(String channelId) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("channelId", channelId);
        HttpRequest.create(UrlConstant.GET_CATEGORY_DATA_URL).putParam(jsonParam).get(new CallBack<CustomerEditEntity>() {
            @Override
            public CustomerEditEntity doInBackground(String response) {
                return JsonUtils.parse(response, CustomerEditEntity.class);
            }

            @Override
            public void success(CustomerEditEntity entity) {
                statusLayout.normalStatus();
                customerEditEntity.setCategory(entity.getCategory());
                customerEditEntity.setCategory_data(entity.getCategory_data());

                customer_add_category_ll.setVisibility(View.VISIBLE);
                List<KeyValueEntity> categoryData = new ArrayList<>();
                categoryData.add(customerEditEntity.getCategory());
                customer_add_category_kvel.setData(categoryData);
                String value = customerEditEntity.getCategory().getDefaultVal();
                if (!TextUtils.isEmpty(value)) {
                    customer_add_category_data_kvel.setData(customerEditEntity.getCategoryItem(value));
                    customer_add_category_data_ll.setVisibility(View.VISIBLE);
                } else {
                    customer_add_category_data_ll.setVisibility(View.GONE);
                }
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
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });
    }

    private void createOrUpdateCustomer(Map<String, Object> data) {
        String url = TextUtils.isEmpty(customerId) ? UrlConstant.CREATE_LEADS_URL : UrlConstant.UPDATE_CUSTOMER_URL;
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue(data).putParamValue("customerId", customerId);
        HttpRequest.create(url).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                if (TextUtils.isEmpty(customerId)) {
                    ToastUtils.toast("客户新建成功");
                } else {
                    ToastUtils.toast("更新成功");
                }
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
