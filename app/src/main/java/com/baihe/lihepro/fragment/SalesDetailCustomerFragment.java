package com.baihe.lihepro.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ContactEditActivity;
import com.baihe.lihepro.activity.SalesDetailActivity;
import com.baihe.lihepro.adapter.SalesDetailCustomerAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.OrderEntity;
import com.baihe.lihepro.entity.SalesDetailEntity;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class SalesDetailCustomerFragment extends BaseFragment {
    private String orderId;

    private StatusLayout sales_customer_sl;
    private RecyclerView sales_customer_rv;

    private SalesDetailCustomerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sales_detail_customer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderId = getArguments().getString(SalesDetailActivity.INTENT_ORDER_ID);
        initView(view);
        initData();
        listener();
        loadData();
    }

    private void initView(View view) {
        sales_customer_sl = view.findViewById(R.id.sales_customer_sl);
        sales_customer_rv = view.findViewById(R.id.sales_customer_rv);
    }

    private void initData() {
        adapter = new SalesDetailCustomerAdapter(getContext());
        adapter.setBaseFragment(this);
        sales_customer_rv.setAdapter(adapter);
        sales_customer_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        sales_customer_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), 9), 0, CommonUtils.dp2pxForInt(getContext(), 3));
                } else if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), -4), 0, CommonUtils.dp2pxForInt(getContext(), 3));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), -4));
                }
            }
        });
    }

    private void listener() {
        sales_customer_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new SalesDetailCustomerAdapter.OnItemClickListener() {
            @Override
            public void addContact(String customerId) {
                ContactEditActivity.start(getActivity(), customerId, SalesDetailActivity.REQUEST_CODE_EDIT_CONTACT);
            }

            @Override
            public void editContact(String customerId) {
                ContactEditActivity.start(getActivity(), customerId, SalesDetailActivity.REQUEST_CODE_EDIT_CONTACT);
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
            public void success(SalesDetailEntity entity) {
                OrderEntity orderEntity = entity.getOrderInfo();
                if (orderEntity != null) {
                    sales_customer_sl.normalStatus();
                    if (getActivity() != null && getActivity() instanceof SalesDetailActivity) {
                        SalesDetailActivity salesDetailActivity = (SalesDetailActivity) getActivity();
                        salesDetailActivity.setButtonTypeData(entity);
                    }
                    adapter.setData(entity.getContactUserData(), entity.getCustomerData(), orderEntity, entity.getCustomerButtonType());
                } else {
                    sales_customer_sl.expandStatus();
                }
            }

            @Override
            public void error() {
                sales_customer_sl.netErrorStatus();
            }

            @Override
            public void fail() {
                sales_customer_sl.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                sales_customer_sl.loadingStatus();
            }
        });
    }
}
