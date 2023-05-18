package com.baihe.lihepro.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.OrderEditActivity;
import com.baihe.lihepro.activity.SalesDetailActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.OrderEntity;
import com.baihe.lihepro.entity.SalesDetailEntity;
import com.baihe.lihepro.view.KeyValueLayout;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class SalesDetailOrderFragment extends BaseFragment {
    private String orderId;

    private StatusLayout sales_order_sl;
    private ImageView sales_order_head_iv;
    private TextView sales_order_name_tv;
    private LinearLayout sales_order_edit_ll;
    private TextView sales_order_wedding_time_tv;
    private KeyValueLayout csales_order_order_data_kvl;
    private KeyValueLayout csales_order_important_data_kvl;
    private TextView csales_order_important_tv;
    private LinearLayout csales_order_important_data_ll;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sales_detail_order;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderId = getArguments().getString(SalesDetailActivity.INTENT_ORDER_ID);
        initView(view);
        listener();
        loadData();
    }

    private void initView(View view) {
        sales_order_sl = view.findViewById(R.id.sales_order_sl);
        sales_order_head_iv = view.findViewById(R.id.sales_order_head_iv);
        sales_order_name_tv = view.findViewById(R.id.sales_order_name_tv);
        sales_order_edit_ll = view.findViewById(R.id.sales_order_edit_ll);
        sales_order_wedding_time_tv = view.findViewById(R.id.sales_order_wedding_time_tv);
        csales_order_order_data_kvl = view.findViewById(R.id.csales_order_order_data_kvl);
        csales_order_important_tv = view.findViewById(R.id.csales_order_important_tv);
        csales_order_important_data_ll = view.findViewById(R.id.csales_order_important_data_ll);
        csales_order_important_data_kvl = view.findViewById(R.id.csales_order_important_data_kvl);
    }

    private void listener() {
        sales_order_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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

    public void loadData() {
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
                sales_order_sl.normalStatus();
                initData(entities);
            }

            @Override
            public void error() {
                sales_order_sl.netErrorStatus();
            }

            @Override
            public void fail() {
                sales_order_sl.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                sales_order_sl.loadingStatus();
            }
        });
    }

    private void initData(SalesDetailEntity salesDetailEntity) {
        final OrderEntity orderEntity = salesDetailEntity.getOrderInfo();
        if (orderEntity != null) {
            if (getActivity() != null && getActivity() instanceof SalesDetailActivity) {
                SalesDetailActivity salesDetailActivity = (SalesDetailActivity) getActivity();
                salesDetailActivity.setButtonTypeData(salesDetailEntity);
            }

            sales_order_name_tv.setText(orderEntity.getCustomer_name());
            sales_order_wedding_time_tv.setText(orderEntity.getWedding_date().getKey() + "：" + (TextUtils.isEmpty(orderEntity.getWedding_date().getVal()) ? "未填写" : orderEntity.getWedding_date().getVal()));
            csales_order_order_data_kvl.setData(orderEntity.getShow_data());
            if (orderEntity.getImportant_data() != null && orderEntity.getImportant_data().size() > 0) {
                csales_order_important_data_kvl.setData(orderEntity.getImportant_data());
            } else {
                csales_order_important_tv.setVisibility(View.GONE);
                csales_order_important_data_ll.setVisibility(View.GONE);
            }
            if (orderEntity.getButton_type() != null && orderEntity.getButton_type().size() > 0) {
                sales_order_edit_ll.setVisibility(View.VISIBLE);
                sales_order_edit_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderEditActivity.start(getActivity(), orderEntity.getOrder_id(), SalesDetailActivity.REQUEST_CODE_EDIT_ORDER);
                    }
                });
            } else {
                sales_order_edit_ll.setVisibility(View.GONE);
            }
        } else {
            sales_order_sl.expandStatus();
        }
    }

}
