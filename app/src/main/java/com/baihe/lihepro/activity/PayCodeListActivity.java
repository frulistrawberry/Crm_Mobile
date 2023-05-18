package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.baihe.lihepro.adapter.PayCodeListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.PayCodeEntity;
import com.baihe.lihepro.entity.PayCodeListEntity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

public class PayCodeListActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, PayCodeListActivity.class);
        context.startActivity(intent);
    }


    private SmartRefreshLayout payCodeListSrl;
    private RecyclerView payCodeListRv;
    private Toolbar payCodeListTb;
    private ImageView payCodeListAddIv;
    private PayCodeListAdapter adapter;

    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_pay_code_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
//        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_pay_code_list, null), params);
        init();
        initData();
        listener();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        adapter = new PayCodeListAdapter(context);
        payCodeListRv.setAdapter(adapter);
        payCodeListRv.setLayoutManager(new LinearLayoutManager(context));

        payCodeListRv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        jsonParam.putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page));
        HttpRequest.create(UrlConstant.PAY_CODE_LIST_URL)
                .tag("付款码列表")
                .putParam(jsonParam)
                .get(new CallBack<PayCodeListEntity>() {
                    @Override
                    public PayCodeListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, PayCodeListEntity.class);
                    }

                    @Override
                    public void success(PayCodeListEntity entity) {

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
                            payCodeListSrl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            payCodeListSrl.setEnableLoadMore(false);
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
                        if (payCodeListSrl.isLoading()) {
                            payCodeListSrl.finishLoadMore();
                        }
                        if (payCodeListSrl.isRefreshing()) {
                            payCodeListSrl.finishRefresh();
                        }
                    }
                });
    }

    private void init() {
        payCodeListAddIv = findViewById(R.id.pay_code_list_title_add_iv);
        payCodeListRv = findViewById(R.id.pay_code_list_rv);
        payCodeListSrl = findViewById(R.id.pay_code_list_srl);
        payCodeListTb = findViewById(R.id.pay_code_list_title_tb);
    }

    private void listener() {

        payCodeListTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payCodeListSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        payCodeListAddIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新建付款码
                AddPayCodeActivity.start(PayCodeListActivity.this);
            }
        });

        adapter.setOnItemClickListener(new PayCodeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PayCodeEntity payCodeEntity) {
                PayCodeDetailActivity.start(PayCodeListActivity.this,String.valueOf(payCodeEntity.getReceivables_id()));
            }
        });
    }

}
