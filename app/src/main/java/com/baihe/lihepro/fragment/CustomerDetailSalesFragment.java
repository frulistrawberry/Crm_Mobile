package com.baihe.lihepro.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.activity.FollowDetailActivity;
import com.baihe.lihepro.adapter.CustomerDetailSalesAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerSalesEntity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailSalesFragment extends BaseFragment {
    private String customerId;

    private StatusLayout customer_sales_sl;
    private SmartRefreshLayout customer_sales_srl;
    private RecyclerView customer_sales_rv;

    private int page = 1;
    private CustomerDetailSalesAdapter adapter;
    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer_detail_sales;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerId = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_ID);
        type = getArguments().getInt("type",2);
        initView(view);
        initData();
        listener();
        loadData();
    }

    private void initView(View view) {
        customer_sales_sl = view.findViewById(R.id.customer_sales_sl);
        customer_sales_srl = view.findViewById(R.id.customer_sales_srl);
        customer_sales_rv = view.findViewById(R.id.customer_sales_rv);
    }

    private void initData() {
        adapter = new CustomerDetailSalesAdapter(getContext());
        customer_sales_rv.setAdapter(adapter);
        customer_sales_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        final Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ECECF0"));
        final float nightOffset = CommonUtils.dp2px(getContext(), 19);
        final float itemOffset = CommonUtils.dp2px(getContext(), 24);
        final float pointWidth = CommonUtils.dp2px(getContext(), 11f);
        final float lineWidth = CommonUtils.dp2px(getContext(), 0.5f);
        final float lineMagin = CommonUtils.dp2px(getContext(), 34f) + (pointWidth - lineWidth) / 2;
        customer_sales_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                int childSize = parent.getChildCount();
                RectF rectF = new RectF();
                for (int i = 0; i < childSize; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (position == adapter.getItemCount() - 1) {
                        continue;
                    }

                    float top = child.getTop() + pointWidth;
                    if (i == 0) {
                        top = top + nightOffset + itemOffset;
                    }
                    rectF.set(lineMagin + child.getLeft(), top, lineMagin + child.getLeft() + lineWidth, child.getBottom());
                    c.drawRect(rectF, paint);
                }
            }
        });
    }

    private void listener() {
        customer_sales_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new CustomerDetailSalesAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(CustomerSalesEntity customerSalesEntity) {
                FollowDetailActivity.start(getContext(), customerSalesEntity.getId(), customerSalesEntity.getFollow_type());
            }
        });
        customer_sales_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData();
            }
        });
    }

    public void refresh(){
        customer_sales_sl.loadingStatus();
        page = 1;
        loadData();
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("type", type)
                .putParamValue("customerId", customerId)
                .putParamValue("pageSize", 20)
                .putParamValue("page", page);
        HttpRequest.create(UrlConstant.FOLLOW_LIST_URL).putParam(jsonParam).get(new CallBack<List<CustomerSalesEntity>>() {
            @Override
            public List<CustomerSalesEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, CustomerSalesEntity.class);
            }

            @Override
            public void success(List<CustomerSalesEntity> entities) {
                if (page == 1 && entities.size() == 0) {
                    //无数据
                    customer_sales_sl.expandStatus();
                } else {
                    customer_sales_sl.normalStatus();
                }
                if (page == 1) {
                    adapter.setData(entities);
                } else {
                    adapter.addData(entities);
                }
                page++;

                if (entities.size() == 20) {
                    //开启加上拉加载功能
                    customer_sales_srl.setEnableLoadMore(true);
                } else {
                    //关闭加上拉加载功能
                    customer_sales_srl.setEnableLoadMore(false);
                }
            }

            @Override
            public void error() {
                if (adapter.getData().size() == 0) {
                    customer_sales_sl.netErrorStatus();
                } else {
                    ToastUtils.toastNetError();
                }
            }

            @Override
            public void fail() {
                if (adapter.getData().size() == 0) {
                    customer_sales_sl.netFailStatus();
                } else {
                    ToastUtils.toastNetWorkFail();
                }
            }

            @Override
            public void before() {
                super.before();
                if (adapter.getData().size() == 0) {
                    customer_sales_sl.loadingStatus();
                }
            }

            @Override
            public void after() {
                super.after();
                if (customer_sales_srl.isLoading()) {
                    customer_sales_srl.finishLoadMore();
                }
                if (customer_sales_srl.isRefreshing()) {
                    customer_sales_srl.finishRefresh();
                }
            }
        });
    }
}
