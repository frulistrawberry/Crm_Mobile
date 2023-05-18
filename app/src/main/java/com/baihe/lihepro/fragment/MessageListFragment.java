package com.baihe.lihepro.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.baihe.lihepro.activity.MessageActivity;
import com.baihe.lihepro.adapter.MsgListAdapter;
import com.baihe.lihepro.adapter.ProductListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.entity.MsgEntity;
import com.baihe.lihepro.entity.MsgListEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.manager.ProductSelectManager;
import com.baihe.lihepro.push.PushHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

public class MessageListFragment extends BaseFragment {


    private StatusLayout product_list_sl;
    private SmartRefreshLayout product_list_srl;
    private RecyclerView product_list_rv;

    private int type;
    private MsgListAdapter adapter;
    private int page = 1;


    public static Fragment newFragment(int type){
        Fragment fragment = new MessageListFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("type",type);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = getArguments().getInt("type");
        initData();
        listener();
        loadData();
    }

    private void init(View view) {
        product_list_sl = view.findViewById(R.id.product_list_sl);
        product_list_srl = view.findViewById(R.id.product_list_srl);
        product_list_rv = view.findViewById(R.id.product_list_rv);
    }

    private void initData() {
        adapter = new MsgListAdapter(getContext());
        product_list_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        product_list_rv.setAdapter(adapter);
        product_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), 13), 0, CommonUtils.dp2pxForInt(getContext(), -4));
                } else if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), 7));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), -4));
                }
            }
        });
    }

    public void refresh() {
        page = 1;
        loadData();
    }

    private void listener() {
        product_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        product_list_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setListener(position -> {
            MsgEntity entity = adapter.getData().get(position);
            if ("1".equals(entity.getCustomer_type())){
                String lead_status = entity.getLead_status();
                int entryType = "1".equals(lead_status)?CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE:CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT;
                CustomerDetailActivity.start(getContext(),entity.getCustomer_id(),entity.getName(),lead_status,entryType);
            }

            unreadStatus(entity.getId());
            PushHelper.changeServiceUnreadCount(entity.getId());
        });
    }

    private void unreadStatus(String customerId){
        HttpRequest.create(UrlConstant.UNREAD_STATUS).putParam(JsonParam.newInstance("params").putParamValue("id",customerId).putParamValue("type",2)
        ).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String o) {
                ((MessageActivity)getActivity()).refreshBadge();
                ((MessageActivity)getActivity()).refresh();
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }
        });
    }

    private void loadData(){

                HttpRequest.create(UrlConstant.GET_UNREAD_LIST).putParam(JsonParam.newInstance("params")
                        .putParamValue("push_id",AccountManager.newInstance().getUserId())
                        .putParamValue("unread",type+"")
                        .putParamValue("page",page)
                        .putParamValue("pageSize", "20")
                )
                .get(new CallBack<MsgListEntity>() {
                    @Override
                    public MsgListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, MsgListEntity.class);
                    }

                    @Override
                    public void success(MsgListEntity entity) {
                        if (page == 1 && entity.getRows().size() == 0) {
                            //无数据
                            product_list_sl.expandStatus();
                        } else {
                            product_list_sl.normalStatus();
                        }
                        if (page == 1) {
                            adapter.update(entity.getRows());
                        } else {
                            adapter.add(entity.getRows());
                        }
                        page++;

                        if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                            //开启加上拉加载功能
                            product_list_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            product_list_srl.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void error() {
                        if (adapter.getData().size() == 0) {
                            product_list_sl.netErrorStatus();
                        } else {
                            ToastUtils.toastNetError();
                        }
                    }

                    @Override
                    public void fail() {
                        if (adapter.getData().size() == 0) {
                            product_list_sl.netFailStatus();
                        } else {
                            ToastUtils.toastNetWorkFail();
                        }
                    }

                    @Override
                    public void before() {
                        super.before();
                        if (adapter!=null && adapter.getData().size() == 0) {
                            product_list_sl.loadingStatus();
                        }
                    }

                    @Override
                    public void after() {
                        super.after();
                        if (product_list_srl.isLoading()) {
                            product_list_srl.finishLoadMore();
                        }
                        if (product_list_srl.isRefreshing()) {
                            product_list_srl.finishRefresh();
                        }
                    }
                });
    }
}
