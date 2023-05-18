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
import com.baihe.lihepro.activity.CustomerAddActivity;
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.adapter.CustomerDetailBaseAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerBaseEntity;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailBaseFragment extends BaseFragment {
    private String customerId;
    private String customerTab;

    private StatusLayout customer_base_sl;
    private RecyclerView ccustomer_base_rv;

    private CustomerDetailBaseAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer_detail_base;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerId = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_ID);
        customerTab = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_TAB);
        initView(view);
        initData();
        listener();
        loadData();
    }

    private void initView(View view) {
        customer_base_sl = view.findViewById(R.id.customer_base_sl);
        ccustomer_base_rv = view.findViewById(R.id.ccustomer_base_rv);
    }

    private void initData() {
        adapter = new CustomerDetailBaseAdapter(getContext(), customerId);
        adapter.setBaseFragment(this);
        ccustomer_base_rv.setAdapter(adapter);
        ccustomer_base_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        ccustomer_base_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        customer_base_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new CustomerDetailBaseAdapter.OnItemClickListener() {
            @Override
            public void addContact() {
                ContactEditActivity.start(getActivity(), customerId, CustomerDetailActivity.REQUEST_CODE_EDIT_CONTACT);
            }

            @Override
            public void editContact() {
                ContactEditActivity.start(getActivity(), customerId, CustomerDetailActivity.REQUEST_CODE_EDIT_CONTACT);
            }

            @Override
            public void editCustomer() {
                CustomerAddActivity.start(getActivity(), customerId, CustomerDetailActivity.REQUEST_CODE_EDIT_CUSTOMER);
            }
        });
    }

    public void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("type", customerTab);
        HttpRequest.create(UrlConstant.CUSTOMER_DETAIL_URL).putParam(jsonParam).get(new CallBack<CustomerBaseEntity>() {
            @Override
            public CustomerBaseEntity doInBackground(String response) {
                return JsonUtils.parse(response, CustomerBaseEntity.class);
            }

            @Override
            public void success(CustomerBaseEntity entity) {
                customer_base_sl.normalStatus();
                adapter.setData(entity.getContactUserData(), entity.getCustomerData(), entity.getCustomer(), entity.getButton_type());

                if (getActivity() != null && getActivity() instanceof CustomerDetailActivity) {
                    CustomerDetailActivity customerDetailActivity = (CustomerDetailActivity) getActivity();
                    customerDetailActivity.setFollowConfigData(entity.getFollowData(),entity.getShowFollowButton());
                }
            }

            @Override
            public void error() {
                customer_base_sl.netErrorStatus();
            }

            @Override
            public void fail() {
                customer_base_sl.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                customer_base_sl.loadingStatus();
            }
        });
    }
}
