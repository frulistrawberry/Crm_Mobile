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
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.activity.CustomerServiceListActivity;
import com.baihe.lihepro.adapter.RequirementListAdapter;
import com.baihe.lihepro.adapter.RequirementSearchAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.RequirementEntity;
import com.baihe.lihepro.entity.RequirementListEntity;
import com.blankj.utilcode.util.SPUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-09-23
 * Description：
 */
public class CustomerServiceListFragment extends BaseFragment {
    public static final String INTENT_PARAM_KEY = "INTENT_PARAM_KEY";
    public static final String INTENT_TAB_VALUE = "INTENT_TAB_VALUE";

    private StatusLayout requirement_list_sl;
    private SmartRefreshLayout requirement_list_srl;
    private RecyclerView requirement_list_rv;
    private TextView requirement_list_num_tv;

    private RequirementListAdapter adapter;
    private int page = 1;
    private String paramKey;
    private String paramValue;

    private boolean showDis;
    private boolean isTeam;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        requirement_list_sl = view.findViewById(R.id.requirement_list_sl);
        requirement_list_srl = view.findViewById(R.id.requirement_list_srl);
        requirement_list_rv = view.findViewById(R.id.requirement_list_rv);
        requirement_list_num_tv = view.findViewById(R.id.requirement_list_num_tv);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        paramKey = getArguments().getString(INTENT_PARAM_KEY);
        paramValue = getArguments().getString(INTENT_TAB_VALUE);
        isTeam = getArguments().getBoolean("isTeam",false);
        showDis = SPUtils.getInstance().getBoolean("transfercustomerleads",false);
        initData();
        loadData();
        listener();
    }

    private void initData() {
        adapter = new RequirementListAdapter(getContext(),showDis);
        requirement_list_rv.setAdapter(adapter);
        requirement_list_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        requirement_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    public void refreshData(boolean isTeam) {
        this.isTeam = isTeam;
        requirement_list_srl.autoRefresh();
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params");
        if (getActivity() != null && getActivity() instanceof CustomerServiceListActivity) {
            CustomerServiceListActivity requirementListActivity = (CustomerServiceListActivity) getActivity();
            Map<String, Object> filterParmsMap = requirementListActivity.getFilterParmsMap();
            Object object = filterParmsMap.get("filter");
            if (object != null && object instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) object;
                map.put(paramKey, paramValue);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put(paramKey, paramValue);
                filterParmsMap.put("filter", map);
            }
            for (Map.Entry<String, Object> entry : filterParmsMap.entrySet()) {
                jsonParam.putParamValue(entry.getKey(), entry.getValue());
            }
        }
        jsonParam.putParamValue("type", "1").putParamValue("team",isTeam).putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page));
        HttpRequest.create(UrlConstant.INVITE_LIST_URL)
                .tag("邀约列表")
                .putParam(jsonParam)
                .get(new CallBack<RequirementListEntity>() {
                    @Override
                    public RequirementListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, RequirementListEntity.class);
                        }

                    @Override
                    public void success(RequirementListEntity entity) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("总数量：");
                        int length1 = buffer.length();
                        buffer.append(entity.getTotal());
                        int length2 = buffer.length();
                        buffer.append(" 条");
                        String content = buffer.toString();
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2DB4E6"));

                        SpannableString span = new SpannableString(content);
                        span.setSpan(colorSpan, length1, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        span.setSpan(new StyleSpan(Typeface.BOLD), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        span.setSpan(new AbsoluteSizeSpan(24, true), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        requirement_list_num_tv.setText(span);

                        if (page == 1 && entity.getRows().size() == 0) {
                            //无数据
                            requirement_list_sl.expandStatus();
                        } else {
                            requirement_list_sl.normalStatus();
                        }
                        if (page == 1) {
                            adapter.setData(entity.getRows());
                        } else {
                            adapter.addData(entity.getRows());
                        }
                        page++;

                        if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                            //开启加上拉加载功能
                            requirement_list_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            requirement_list_srl.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void error() {
                        if (adapter.getData().size() == 0) {
                            requirement_list_sl.netErrorStatus();
                        } else {
                            ToastUtils.toastNetError();
                        }
                    }

                    @Override
                    public void fail() {
                        if (adapter.getData().size() == 0) {
                            requirement_list_sl.netFailStatus();
                        } else {
                            ToastUtils.toastNetWorkFail();
                        }
                    }

                    @Override
                    public void before() {
                        super.before();
                        if (adapter.getData().size() == 0) {
                            requirement_list_sl.loadingStatus();
                        }
                    }

                    @Override
                    public void after() {
                        super.after();
                        if (requirement_list_srl.isLoading()) {
                            requirement_list_srl.finishLoadMore();
                        }
                        if (requirement_list_srl.isRefreshing()) {
                            requirement_list_srl.finishRefresh();
                        }
                    }
                });
    }

    private void listener() {
        requirement_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        requirement_list_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new RequirementSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RequirementEntity requirementEntity) {
                CustomerDetailActivity.start(getContext(), requirementEntity.getCustomer_id(), requirementEntity.getCustomer_name(), "1", CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE);
            }
        });

        adapter.setOnCustomerTransListener(new RequirementListAdapter.OnCustomerTransListener() {
            @Override
            public void onTrans(String customerId, String ownerId) {
                trans(customerId,ownerId);
            }
        });
    }

    private void trans(String customerId,String ownerId){
        JsonParam jsonParam = JsonParam.newInstance("params");
        List<String> customerIds = new ArrayList<>();
        customerIds.add(customerId);
        jsonParam.putParamValue("customerIds",customerIds).putParamValue("ownerId",ownerId);


        HttpRequest.create(UrlConstant.TRANS_CUSTOMER_LEADS)
                .putParam(jsonParam)
                .get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String entity) {
                try {
                    ((CustomerServiceListActivity)getActivity()).refresh();
                }catch (Exception e){
                    refreshData(isTeam);
                }
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

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
