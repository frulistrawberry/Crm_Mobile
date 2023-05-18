package com.baihe.lihepro.fragment;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

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
import com.baihe.lihepro.activity.ContractDetailActivity;
import com.baihe.lihepro.activity.ContractListActivity;
import com.baihe.lihepro.adapter.ContractListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.ContractItemEntity;
import com.baihe.lihepro.entity.ContractListEntity;
import com.baihe.lihepro.utils.Utils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Map;

/**
 * Author：xubo
 * Time：2020-09-23
 * Description：
 */
public class ContractListFragment extends BaseFragment {
    public static final String INTENT_TAB_VALUE = "INTENT_TAB_VALUE";

    private StatusLayout contrcat_list_sl;
    private SmartRefreshLayout contrcat_list_srl;
    private RecyclerView contrcat_list_rv;
    private TextView contrcat_list_num_tv;
    private TextView contrcat_list_amount_tv;

    private ContractListAdapter adapter;
    private int page = 1;
    private String paramValue;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contract_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        contrcat_list_sl = view.findViewById(R.id.contrcat_list_sl);
        contrcat_list_srl = view.findViewById(R.id.contrcat_list_srl);
        contrcat_list_rv = view.findViewById(R.id.contrcat_list_rv);
        contrcat_list_num_tv = view.findViewById(R.id.contrcat_list_num_tv);
        contrcat_list_amount_tv = view.findViewById(R.id.contrcat_list_amount_tv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        paramValue = getArguments().getString(INTENT_TAB_VALUE);
        initData();
        loadData();
        listener();
    }

    private void initData() {
        adapter = new ContractListAdapter(getContext(), false);
        contrcat_list_rv.setAdapter(adapter);
        contrcat_list_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        contrcat_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    public void refreshData() {
        contrcat_list_srl.autoRefresh();
    }

    public void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("isMy", paramValue);
        if (getActivity() != null && getActivity() instanceof ContractListActivity) {
            ContractListActivity contractListActivity = (ContractListActivity) getActivity();
            Map<String, Object> filterParmsMap = contractListActivity.getFilterParmsMap();
            for (Map.Entry<String, Object> entry : filterParmsMap.entrySet()) {
                jsonParam.putParamValue(entry.getKey(), entry.getValue());
            }
        }
        jsonParam.putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page));
        HttpRequest.create(UrlConstant.CONTRACT_LIST2_URL)
                .tag("合同列表")
                .putParam(jsonParam)
                .get(new CallBack<ContractListEntity>() {
                    @Override
                    public ContractListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, ContractListEntity.class);
                    }

                    @Override
                    public void success(ContractListEntity entity) {
                        contrcat_list_num_tv.setText("总数量：" + entity.getTotal() + "条");

                        String[] values = Utils.formatPrice(entity.getAmount_total());
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("合同金额：");
                        int length1 = buffer.length();
                        buffer.append(values[0]);
                        int length2 = buffer.length();
                        buffer.append(" " + values[1]);
                        String content = buffer.toString();
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2DB4E6"));

                        SpannableString span = new SpannableString(content);
                        span.setSpan(colorSpan, length1, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        span.setSpan(new StyleSpan(Typeface.BOLD), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        span.setSpan(new AbsoluteSizeSpan(24, true), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        contrcat_list_amount_tv.setText(span);

                        if (page == 1 && entity.getContractList().size() == 0) {
                            //无数据
                            contrcat_list_sl.expandStatus();
                        } else {
                            contrcat_list_sl.normalStatus();
                        }
                        if (page == 1) {
                            adapter.setData(entity.getContractList());
                        } else {
                            adapter.addData(entity.getContractList());
                        }
                        page++;

                        if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                            //开启加上拉加载功能
                            contrcat_list_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            contrcat_list_srl.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void error() {
                        if (adapter.getData().size() == 0) {
                            contrcat_list_sl.netErrorStatus();
                        } else {
                            ToastUtils.toastNetError();
                        }
                    }

                    @Override
                    public void fail() {
                        if (adapter.getData().size() == 0) {
                            contrcat_list_sl.netFailStatus();
                        } else {
                            ToastUtils.toastNetWorkFail();
                        }
                    }

                    @Override
                    public void before() {
                        super.before();
                        if (adapter.getData().size() == 0) {
                            contrcat_list_sl.loadingStatus();
                        }
                    }

                    @Override
                    public void after() {
                        super.after();
                        if (contrcat_list_srl.isLoading()) {
                            contrcat_list_srl.finishLoadMore();
                        }
                        if (contrcat_list_srl.isRefreshing()) {
                            contrcat_list_srl.finishRefresh();
                        }
                    }
                });
    }

    private void listener() {
        contrcat_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        contrcat_list_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new ContractListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContractItemEntity contractItemEntity) {
                ContractDetailActivity.start(getActivity(), contractItemEntity.getId(), "contract", ContractListActivity.REQUEST_CODE_DETAIL_CONTRACT);
            }
        });
    }
}
