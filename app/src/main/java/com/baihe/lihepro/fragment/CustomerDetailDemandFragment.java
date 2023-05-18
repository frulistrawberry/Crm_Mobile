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
import com.baihe.lihepro.activity.CategoryEditActivity;
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.adapter.CustomerDetailDemandAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerDemandEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailDemandFragment extends BaseFragment {
    private String customerId;
    private String customerTab;

    private StatusLayout customer_demand_sl;
    private RecyclerView customer_demand_rv;

    private CustomerDetailDemandAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer_detail_demand;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customerId = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_ID);
        customerTab = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_TAB);
        initView(view);
        initData();
        listener();
        loadData();
    }

    private void initView(View view) {
        customer_demand_sl = view.findViewById(R.id.customer_demand_sl);
        customer_demand_rv = view.findViewById(R.id.customer_demand_rv);
    }

    private void initData() {
        adapter = new CustomerDetailDemandAdapter(getContext());
        customer_demand_rv.setAdapter(adapter);
        customer_demand_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        customer_demand_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), 9), 0, CommonUtils.dp2pxForInt(getContext(), -4));
                } else if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), 3));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), -4));
                }
            }
        });
    }

    private void listener() {
        customer_demand_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new CustomerDetailDemandAdapter.OnItemClickListener() {
            @Override
            public void edit(CustomerDemandEntity customerDemandEntity) {
                CategoryEditActivity.start(getActivity(), customerId, customerDemandEntity.getCategory_id(), customerDemandEntity.getCategory_name(), CustomerDetailActivity.ENTRY_TYPE_CUSTOMER, CustomerDetailActivity.REQUEST_CODE_EIDT_CATEGORY);
            }
        });
    }

    public void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.CUSTOMER_LEADS_URL).putParam(jsonParam).get(new CallBack<List<CustomerDemandEntity>>() {
            @Override
            public List<CustomerDemandEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, CustomerDemandEntity.class);
            }

            @Override
            public void success(List<CustomerDemandEntity> entities) {
                if (entities.size() > 0) {
                    customer_demand_sl.normalStatus();
                    adapter.setData(entities);
                } else {
                    customer_demand_sl.expandStatus();
                }
            }

            @Override
            public void error() {
                customer_demand_sl.netErrorStatus();
            }

            @Override
            public void fail() {
                customer_demand_sl.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                customer_demand_sl.loadingStatus();
            }

        });
    }
}
