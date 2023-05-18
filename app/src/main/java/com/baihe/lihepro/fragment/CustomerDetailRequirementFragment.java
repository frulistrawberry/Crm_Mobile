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
import com.baihe.common.util.ToastUtils;
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
import com.baihe.lihepro.entity.CustomerRequirementEntity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailRequirementFragment extends BaseFragment {
    private String customerId;
    private String customerTab;

    private StatusLayout customer_requirement_sl;
    private SmartRefreshLayout customer_requirement_srl;
    private RecyclerView customer_requirement_rv;

    private int page = 1;
    private CustomerDetailDemandAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer_detail_requirement;
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
        customer_requirement_sl = view.findViewById(R.id.customer_requirement_sl);
        customer_requirement_srl = view.findViewById(R.id.customer_requirement_srl);
        customer_requirement_rv = view.findViewById(R.id.customer_requirement_rv);
    }

    private void initData() {
        adapter = new CustomerDetailDemandAdapter(getContext());
        customer_requirement_rv.setAdapter(adapter);
        customer_requirement_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        customer_requirement_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        customer_requirement_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
                CategoryEditActivity.start(getActivity(), customerId, customerDemandEntity.getCategory_id(), customerDemandEntity.getCategory_name(), CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT, CustomerDetailActivity.REQUEST_CODE_EIDT_CATEGORY);
            }
        });
        customer_requirement_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                adapter.clearFLod();
                loadData();
            }
        });
    }

    public void refresh() {
        page = 1;
        adapter.clearFLod();
        loadData();
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("page", page).putParamValue("aggregation", "1");
        HttpRequest.create(UrlConstant.REQ_LIST_URL).putParam(jsonParam).get(new CallBack<CustomerRequirementEntity>() {
            @Override
            public CustomerRequirementEntity doInBackground(String response) {
                return JsonUtils.parse(response, CustomerRequirementEntity.class);
            }

            @Override
            public void success(CustomerRequirementEntity entity) {
                if (page == 1 && entity.getRows().size() == 0) {
                    //无数据
                    customer_requirement_sl.expandStatus();
                } else {
                    customer_requirement_sl.normalStatus();
                }
                if (page == 1) {
                    adapter.setData(entity.getRows());
                } else {
                    adapter.addData(entity.getRows());
                }
                page++;

                if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                    //开启加上拉加载功能
                    customer_requirement_srl.setEnableLoadMore(true);
                } else {
                    //关闭加上拉加载功能
                    customer_requirement_srl.setEnableLoadMore(false);
                }
            }

            @Override
            public void error() {
                if (adapter.getData().size() == 0) {
                    customer_requirement_sl.netErrorStatus();
                } else {
                    ToastUtils.toastNetError();
                }
            }

            @Override
            public void fail() {
                if (adapter.getData().size() == 0) {
                    customer_requirement_sl.netFailStatus();
                } else {
                    ToastUtils.toastNetWorkFail();
                }
            }

            @Override
            public void before() {
                super.before();
                if (adapter.getData().size() == 0) {
                    customer_requirement_sl.loadingStatus();
                }
            }

            @Override
            public void after() {
                super.after();
                if (customer_requirement_srl.isLoading()) {
                    customer_requirement_srl.finishLoadMore();
                }
                if (customer_requirement_srl.isRefreshing()) {
                    customer_requirement_srl.finishRefresh();
                }
            }

        });
    }
}
