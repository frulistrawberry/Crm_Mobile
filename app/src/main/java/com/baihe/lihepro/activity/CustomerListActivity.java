package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.CustomerListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerEntity;
import com.baihe.lihepro.entity.CustomerListEntity;
import com.baihe.lihepro.filter.FilterCallback;
import com.baihe.lihepro.filter.FilterUtils;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-07-24
 * Description：
 */
public class CustomerListActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, CustomerListActivity.class);
        context.startActivity(intent);
    }

    private SmartRefreshLayout customer_list_srl;
    private RecyclerView customer_list_rv;
    private Toolbar customer_list_title_tb;
    private ImageView customer_list_title_search_iv;
    private ImageView customer_list_title_add_iv;
    private LinearLayout customer_list_title_filter_ll;

    private CustomerListAdapter adapter;
    private int page = 1;
    private Map<String, Object> filterParmsMap = new HashMap<>();

    private ArrayList<FilterEntity> filterEntities;
    private FilterCallback filterCallback = new FilterCallback() {
        @Override
        public void call(Map<String, Object> requestMap) {
            Logger.d(requestMap);
            filterParmsMap.clear();
            filterParmsMap.putAll(requestMap);
            page = 1;
            adapter.getData().clear();
            loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_customer_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_customer_list, null), params);
        init();
        initData();
        loadData();
        loadFilterData();
        listener();
    }

    private void init() {
        customer_list_srl = findViewById(R.id.customer_list_srl);
        customer_list_rv = findViewById(R.id.customer_list_rv);
        customer_list_title_tb = findViewById(R.id.customer_list_title_tb);
        customer_list_title_search_iv = findViewById(R.id.customer_list_title_search_iv);
        customer_list_title_add_iv = findViewById(R.id.customer_list_title_add_iv);
        customer_list_title_filter_ll = findViewById(R.id.customer_list_title_filter_ll);
    }

    private void initData() {
        adapter = new CustomerListAdapter(context);
        customer_list_rv.setAdapter(adapter);
        customer_list_rv.setLayoutManager(new LinearLayoutManager(context));

        customer_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                } else if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, 3));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                }
            }
        });
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params");
        for (Map.Entry<String, Object> entry : filterParmsMap.entrySet()) {
            jsonParam.putParamValue(entry.getKey(), entry.getValue());
        }
        jsonParam.putParamValue("order", 3).putParamValue("pageSize", "20").putParamValue("page", page);
        HttpRequest.create(UrlConstant.CUSTOMER_LIST_URL)
                .tag("客户列表")
                .putParam(jsonParam)
                .get(new CallBack<CustomerListEntity>() {
                    @Override
                    public CustomerListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, CustomerListEntity.class);
                    }

                    @Override
                    public void success(CustomerListEntity entity) {
                        if (page == 1 && entity.getRows().size() == 0) {
                            //无数据
                            statusLayout.expandStatus();
                        } else {
                            statusLayout.normalStatus();
                        }
                        if (page == 1) {
                            adapter.setData(entity.getRows());
                        } else {
                            adapter.addData(entity.getRows());
                        }
                        page++;

                        if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                            //开启加上拉加载功能
                            customer_list_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            customer_list_srl.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void error() {
                        if (adapter.getData().size() == 0) {
                            statusLayout.netErrorStatus();
                        } else {
                            ToastUtils.toastNetError();
                        }
                    }

                    @Override
                    public void fail() {
                        if (adapter.getData().size() == 0) {
                            statusLayout.netFailStatus();
                        } else {
                            ToastUtils.toastNetWorkFail();
                        }
                    }

                    @Override
                    public void before() {
                        super.before();
                        if (adapter.getData().size() == 0) {
                            statusLayout.loadingStatus();
                        }
                    }

                    @Override
                    public void after() {
                        super.after();
                        if (customer_list_srl.isLoading()) {
                            customer_list_srl.finishLoadMore();
                        }
                        if (customer_list_srl.isRefreshing()) {
                            customer_list_srl.finishRefresh();
                        }
                    }
                });
    }

    private void loadFilterData() {
        HttpRequest.create(UrlConstant.FILTER_CONFIGT_URL)
                .putParam(new JsonParam("params")
                        .putParamValue("tab", "customer")).get(new CallBack<ArrayList<FilterEntity>>() {
            @Override
            public ArrayList<FilterEntity> doInBackground(String response) {
                return (ArrayList<FilterEntity>) JsonUtils.parseList(response, FilterEntity.class);
            }

            @Override
            public void success(ArrayList<FilterEntity> filterEntities) {
                CustomerListActivity.this.filterEntities = filterEntities;
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }
        });
    }

    private void listener() {
        customer_list_title_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.filter(context, filterEntities, filterCallback);
            }
        });
        customer_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customer_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        customer_list_title_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerSearchActivity.start(context);
            }
        });
        adapter.setOnItemClickListener(new CustomerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CustomerEntity requirementEntity) {
                CustomerDetailActivity.start(context, requirementEntity.getCustomer_id(), requirementEntity.getCustomer_name(), "2", CustomerDetailActivity.ENTRY_TYPE_CUSTOMER);
            }
        });
    }
}
