package com.baihe.lihepro.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.baihe.lihepro.activity.AddTeamActivity;
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.activity.SalesDetailActivity;
import com.baihe.lihepro.adapter.TeamAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AlertDialog;
import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.entity.TeamUser;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class CustomerDetailTeamFragment extends BaseFragment {
    private String customerId;

    private StatusLayout customer_sales_sl;
    private SmartRefreshLayout customer_sales_srl;
    private RecyclerView customer_sales_rv;
    private TeamAdapter adapter;
    private String category;
    private String categoryText;

    private int type;
    private String orderId;

    public static Fragment newFragment(int type){
        Fragment fragment = new CustomerDetailTeamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newFragment(String orderId,String customerId,int type,String category,String categoryText){
        Fragment fragment = new CustomerDetailTeamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putString("category",category);
        bundle.putString("categoryText",categoryText);
        bundle.putString(SalesDetailActivity.INTENT_ORDER_ID,orderId);
        bundle.putString(CustomerDetailActivity.INTENT_CUSTOMER_ID, customerId);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_customer_detail_team;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        customerId = bundle.getString(CustomerDetailActivity.INTENT_CUSTOMER_ID);
        type = bundle.getInt("type",2);
        category = bundle.getString("category");
        categoryText = bundle.getString("categoryText");
        orderId = bundle.getString(SalesDetailActivity.INTENT_ORDER_ID);
        initView(view);
        initData();
        listener();
        customer_sales_srl.autoRefresh();
    }

    private void initView(View view) {
        customer_sales_sl = view.findViewById(R.id.customer_sales_sl);
        customer_sales_srl = view.findViewById(R.id.customer_sales_srl);
        customer_sales_rv = view.findViewById(R.id.customer_sales_rv);
    }

    private void initData() {
        adapter = new TeamAdapter(getContext(),type);
        customer_sales_rv.setAdapter(adapter);
        customer_sales_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        customer_sales_srl.setEnableLoadMore(false);
        customer_sales_sl.setExpandLayout(R.layout.empty_team);


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
                customer_sales_srl.autoRefresh();
            }

            @Override
            public void onNetFailClick() {
                customer_sales_srl.autoRefresh();
            }

            @Override
            public void onExpandClick() {
                if (type == 1 || type == 2){
                    AddTeamActivity.start(CustomerDetailTeamFragment.this,customerId,type);
                }else {
                    AddTeamActivity.start(CustomerDetailTeamFragment.this,type,customerId,category,categoryText);
                }
            }
        });
        customer_sales_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        adapter.setListener(new TeamAdapter.OnTeamOption() {
            @Override
            public void add() {
                if (type == 1 || type == 2){
                    AddTeamActivity.start(CustomerDetailTeamFragment.this,customerId,type);
                }else {
                    AddTeamActivity.start(CustomerDetailTeamFragment.this,type,customerId,category,categoryText);
                }
            }

            @Override
            public void edit(String teamUsers, String teamUserText, String category, String categoryText) {
                AddTeamActivity.start(CustomerDetailTeamFragment.this,type,customerId,category,categoryText,teamUsers,teamUserText);
            }

            @Override
            public void delete(String teamUsers, String teamUserText, String category, String categoryText) {
                new AlertDialog.Builder(getContext())
                        .setTitle("系统提示")
                        .setContent("是否确认删除该团队？")
                        .setCancelable(true)
                        .setConfirmListener("立即删除", new AlertDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                operateTeam(customerId,category,teamUsers, type+"");
                                dialog.dismiss();
                            }
                        }).setCancelListener("暂不操作", new AlertDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
            }
        });
    }

    private void getCategory(List<TeamUser> data){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.TEAM_GET_CATEGORY).putParam(jsonParam).get(new CallBack<List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, CategoryEntity.class);
            }

            @Override
            public void success(List<CategoryEntity> entities) {
                if (entities != null && entities.size()>0){
                    if (entities.size() == data.size() && type!=3){
                        adapter.setShowAdd(false);
                    }else {
                        boolean flag = true;
                        for (TeamUser datum : data) {
                            if (datum.getCategory_id().equals(category)){
                                flag = false;
                                break;
                            }
                        }
                        adapter.setShowAdd(flag);
                    }
                }else {
                    adapter.setShowAdd(false);
                }

                if ( entities.size() == 0) {
                    //无数据
                    customer_sales_sl.expandStatus();
                } else {
                    customer_sales_sl.normalStatus();
                }
                adapter.setData(data);


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

            }

            @Override
            public void after() {
                super.after();

            }
        });
    }


    private void operateTeam(String customerId, String category, String teamUserIds, String type){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId", customerId)
                .putParamValue("teamUserIds", teamUserIds)
                .putParamValue("action", "3")
                .putParamValue("type", type)
                .putParamValue("category",category);
        HttpRequest.create(UrlConstant.OPREATE_TEAM).putParam(jsonParam).get(new CallBack<Integer>() {
            @Override
            public Integer doInBackground(String response) {
                return JsonUtils.parse(response, Integer.class);
            }

            @Override
            public void success(Integer entity) {
                ToastUtils.toast("删除成功");
                customer_sales_srl.autoRefresh();
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

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("type", type)
                .putParamValue("customerId", customerId);
        if (!TextUtils.isEmpty(category)){
            jsonParam.putParamValue("category",category);
        }
        HttpRequest.create(UrlConstant.TEAM_GET_TEAM_INFO).putParam(jsonParam).get(new CallBack<List<TeamUser>>() {
            @Override
            public List<TeamUser> doInBackground(String response) {
                return JsonUtils.parseList(response, TeamUser.class);
            }

            @Override
            public void success(List<TeamUser> entities) {
                if (entities==null || entities.size()==0)
                    customer_sales_sl.expandStatus();
                else
                    getCategory(entities);

            }

            @Override
            public void error() {
                customer_sales_sl.netFailStatus();

            }

            @Override
            public void fail() {
                customer_sales_sl.netFailStatus();

            }

            @Override
            public void before() {
                super.before();

            }

            @Override
            public void after() {
                super.after();
                if (customer_sales_srl.isRefreshing()) {
                    customer_sales_srl.finishRefresh();
                }
            }
        });
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        customer_sales_srl.autoRefresh();
    }
}
