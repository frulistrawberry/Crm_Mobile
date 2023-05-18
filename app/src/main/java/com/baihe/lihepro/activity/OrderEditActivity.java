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
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-11
 * Description：
 */
public class OrderEditActivity extends BaseActivity {
    private static final String INTENT_ORDER_ID = "INTENT_ORDER_ID";

    public static void start(Activity activity, String orderId, int requestCode) {
        Intent intent = new Intent(activity, OrderEditActivity.class);
        intent.putExtra(INTENT_ORDER_ID, orderId);
        activity.startActivityForResult(intent, requestCode);
    }

    private KeyValueEditLayout order_edit_data_kvel;
    private TextView order_edit_ok_tv;

    private String orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra(INTENT_ORDER_ID);
        setTitleText("编辑订单信息");
        setContentView(R.layout.activity_order_edit);
        init();
        listener();
        loadConfig();
    }

    private void init() {
        order_edit_data_kvel = findViewById(R.id.order_edit_data_kvel);
        order_edit_ok_tv = findViewById(R.id.order_edit_ok_tv);
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
        order_edit_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void commit() {
        Map<String, Object> data = order_edit_data_kvel.commit();
        if (data != null) {
            updateOrder(data);
        }
    }

    private void loadConfig() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("orderId", orderId).putParamValue("type", "order");
        HttpRequest.create(UrlConstant.BUILD_PARAMS_URL).putParam(jsonParam).get(new CallBack<List<KeyValueEntity>>() {
            @Override
            public List<KeyValueEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, KeyValueEntity.class);
            }

            @Override
            public void success(List<KeyValueEntity> data) {
                statusLayout.normalStatus();
                order_edit_data_kvel.setData(data);
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

    private void updateOrder(Map<String, Object> data) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue(data).putParamValue("orderId", orderId);
        HttpRequest.create(UrlConstant.UPDATE_ORDER_URL).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<String>() {
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
