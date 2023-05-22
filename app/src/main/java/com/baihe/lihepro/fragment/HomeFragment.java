package com.baihe.lihepro.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ApproveListActivity;
import com.baihe.lihepro.activity.ContractDetailActivity;
import com.baihe.lihepro.activity.ContractListActivity;
import com.baihe.lihepro.activity.CustomerListActivity;
import com.baihe.lihepro.activity.CustomerServiceListActivity;
import com.baihe.lihepro.activity.MainActivity;
import com.baihe.lihepro.activity.MessageActivity;
import com.baihe.lihepro.activity.MultiScheduleHomeActivity;
import com.baihe.lihepro.activity.PayCodeListActivity;
import com.baihe.lihepro.activity.RequirementListActivity;
import com.baihe.lihepro.activity.SalesListActivity;
import com.baihe.lihepro.activity.ScheduleHomeActivity;
import com.baihe.lihepro.activity.WebActivity;
import com.baihe.lihepro.adapter.AppMenuAdapter;
import com.baihe.lihepro.adapter.ApproveAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.MsgDialog;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.entity.MenuEntity;
import com.baihe.lihepro.entity.MsgListEntity;
import com.baihe.lihepro.entity.UserEntity;
import com.baihe.lihepro.entity.UserPermission;
import com.baihe.lihepro.entity.schedule.CompanyLevel;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.manager.HomeRefreshManager;
import com.baihe.lihepro.manager.HttpRequestManager;
import com.baihe.lihepro.manager.HttpTask;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * Author：xubo
 * Time：2020-07-22
 * Description：
 */
public class HomeFragment extends BaseFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private LinearLayout home_top_ll;
    private TextView home_name_tv;
    private StatusLayout home_content_sl;
    private SmartRefreshLayout home_content_srl;
    private TextView home_apps_tv;
    private RecyclerView home_apps_rv;
    private LinearLayout home_approve_ll;
    private TextView home_approve_tv;
    private ImageView btn_msg;
    private RecyclerView home_approve_rv;
    private LinearLayout msg_ll;

    private AppMenuAdapter appMenuAdapter;
    private ApproveAdapter approveAdapter;
    private int jumpTo = 1;
    private QBadgeView badgeView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initView();
        initData();
        listener();
        loadData();
    }

    @Subscribe
    public void onEvent(String action){
        switch (action){
            case "refresh_home_unread_count":
                getUnreadMsgNum();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(View view) {
        home_top_ll = view.findViewById(R.id.home_top_ll);
        home_name_tv = view.findViewById(R.id.home_name_tv);
        home_content_sl = view.findViewById(R.id.home_content_sl);
        home_content_srl = view.findViewById(R.id.home_content_srl);
        home_apps_tv = view.findViewById(R.id.home_apps_tv);
        home_apps_rv = view.findViewById(R.id.home_apps_rv);
        home_approve_ll = view.findViewById(R.id.home_approve_ll);
        home_approve_tv = view.findViewById(R.id.home_approve_tv);
        home_approve_rv = view.findViewById(R.id.home_approve_rv);
        btn_msg = view.findViewById(R.id.btn_msg);
        msg_ll = view.findViewById(R.id.msg_ll);
    }

    private void initView() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getContext().getResources(), R.drawable.home_top_bg, options);
        int topHeight = (int) (ScreenUtils.getScreenWidth() * ((float) options.outHeight / (float) options.outWidth));
        home_top_ll.getLayoutParams().height = topHeight;
        badgeView = new QBadgeView(getContext());
        badgeView.setBadgeTextSize(10f,true);
        badgeView.setBadgeGravity(Gravity.TOP|Gravity.END);
        badgeView.setBadgePadding(5f,true);
        badgeView.bindTarget(msg_ll);
    }

    private void initData() {
        home_name_tv.setText("您好，" + AccountManager.newInstance().getUser().getName());
        //临时添加测试入口
        home_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                ARouter.getInstance().build("/demo/activity").navigation();
            }
        });
        appMenuAdapter = new AppMenuAdapter(getContext());
        home_apps_rv.setAdapter(appMenuAdapter);
        home_apps_rv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        final int sapce = (CommonUtils.getScreenWidth(getContext()) - CommonUtils.dp2pxForInt(getContext(), 40) - 4 * CommonUtils.dp2pxForInt(getContext(), 52)) / (4 * 3);
        home_apps_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 4 == 0) {
                    outRect.left = 0;
                } else if (position % 4 == 1) {
                    outRect.left = sapce;
                } else if (position % 4 == 2) {
                    outRect.left = sapce * 2;
                } else {
                    outRect.left = sapce * 3;
                }
                if (position / 4 == 0) {
                    outRect.top = 0;
                } else {
                    outRect.top = CommonUtils.dp2pxForInt(getContext(), 21);
                }
            }
        });

        approveAdapter = new ApproveAdapter(getContext());
        home_approve_rv.setAdapter(approveAdapter);
        home_approve_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        home_approve_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), 9), 0, CommonUtils.dp2pxForInt(getContext(), -4));
                } else if (position == approveAdapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), 78));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), -4));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnreadMsgNum();
    }

    private void getCompanyLevel(){

        HttpRequest.create(UrlConstant.SCHEDULE_GET_COMPANY_LEVEL).get(new CallBack<CompanyLevel>() {
            @Override
            public CompanyLevel doInBackground(String response) {
                return JsonUtils.parse(response, CompanyLevel.class);
            }

            @Override
            public void success(CompanyLevel entity) {
                SPUtils.getInstance().put("companyLevel",entity.getLevelLimit());
                if ("34".equals(AccountManager.newInstance().getUser().getCompany_id()))
                    MultiScheduleHomeActivity.start(getActivity(),entity.getLevelLimit(),null,false);
                else
                    startActivity(new Intent(getContext(),ScheduleHomeActivity.class));

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

    private void getUnreadMsgNum(){
        HttpRequest.create(UrlConstant.GET_UNREAD_LIST).putParam(JsonParam.newInstance("params")
                .putParamValue("push_id", AccountManager.newInstance().getUserId())
                .putParamValue("unread", "1").putParamValue("pageSize", "20").putParamValue("page", 1))
                .get(new CallBack<MsgListEntity>() {
                    @Override
                    public MsgListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, MsgListEntity.class);
                    }

                    @Override
                    public void success(MsgListEntity entity) {
                        if (entity.getTotal() > 0) {
                            badgeView.setVisibility(View.VISIBLE);
                            badgeView.setBadgeNumber(entity.getTotal());
                        } else {
                            badgeView.setVisibility(View.GONE);
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
                    }

                    @Override
                    public void after() {
                        super.after();

                    }
                });
    }
    private void getUserPermissionList(){

        HttpRequest.create(UrlConstant.GET_USER_PERMISSION_LIST)

                .get(new CallBack<UserPermission>() {
            @Override
            public UserPermission doInBackground(String response) {
                return JsonUtils.parse(response, UserPermission.class);
            }

            @Override
            public void success(UserPermission entity) {
                SPUtils.getInstance().put("transfercustomerreq",entity.isTransfercustomerreq());
                SPUtils.getInstance().put("transfercustomerleads",entity.isTransfercustomerleads());
                if (jumpTo == 2)
                RequirementListActivity.start(getContext());
                else if (jumpTo == 3){
                    CustomerServiceListActivity.start(getContext());
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

    private void listener() {
        appMenuAdapter.setOnItemClickListener(new AppMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String menuCode) {
                if (AppMenuAdapter.MENU_CUSTOMER_CODE.equals(menuCode)) {
                    CustomerListActivity.start(getContext());
                } else if (AppMenuAdapter.MENU_REQUIREMENT_CODE.equals(menuCode)) {
                    jumpTo = 2;
                    getUserPermissionList();
                } else if (AppMenuAdapter.MENU_SALES_CODE.equals(menuCode)) {
                    SalesListActivity.start(getContext());
                } else if (AppMenuAdapter.MENU_APPROVE_CODE.equals(menuCode)) {
                    ApproveListActivity.start(getContext());
                } else if (AppMenuAdapter.MENU_CONTRACT_CODE.equals(menuCode)) {
                    ContractListActivity.start(getContext());
                }else if (AppMenuAdapter.MENU_SCHEDULE_CODE.equals(menuCode)) {
                    WebActivity.start(getContext(), "http://lihe.hunli.baihe.com/");
                }else if (AppMenuAdapter.MENU_ANLIKU_CODE == menuCode){
                    HashMap<String,String> params = new HashMap<>();
                    UserEntity userEntity = AccountManager.newInstance().getUser();
                    params.put("account", userEntity.getAccount());
                    params.put("company_id",userEntity.getCompany_id() );
                    params.put("id", userEntity.getId());
                    params.put("name", userEntity.getName());
                    params.put("company_name", userEntity.getCompany_name());
                    params.put("avatar", userEntity.getAvatar());
                    params.put("device","android");
                    WebActivity.start(getContext(),buildUrl(UrlConstant.ANLIKU_LOGIN,params));
                }else if (AppMenuAdapter.MENU_ANLIKU_CODE == menuCode){
                    HashMap<String,String> params = new HashMap<>();
                    UserEntity userEntity = AccountManager.newInstance().getUser();
                    params.put("account", userEntity.getAccount());
                    params.put("company_id",userEntity.getCompany_id() );
                    params.put("id", userEntity.getId());
                    params.put("name", userEntity.getName());
                    params.put("company_name", userEntity.getCompany_name());
                    params.put("avatar", userEntity.getAvatar());
                    params.put("device","android");
                    WebActivity.start(getContext(),buildUrl(UrlConstant.ANLIKU_LOGIN,params));
                }else if (AppMenuAdapter.MENU_PAY_CODE.equals(menuCode)){
                    PayCodeListActivity.start(getContext());
                }else if (AppMenuAdapter.MENU_SCHEDULE_CODE_NEW.equals(menuCode)){
                    getCompanyLevel();
                }else if (AppMenuAdapter.MENU_KEFU.equals(menuCode)){
                    jumpTo = 3;
                    getUserPermissionList();
                }
            }
        });
        home_approve_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveListActivity.start(getContext());
            }
        });
        home_content_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        home_content_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        approveAdapter.setOnItemClickListener(new ApproveAdapter.OnItemClickListener() {
            @Override
            public void approve(ApproveEntity approveEntity) {
                ContractDetailActivity.startForApprove(getActivity(), approveEntity.getaId(), MainActivity.REQUEST_CODE_APPROVE);
            }
        });
        HomeRefreshManager.newInstance().subscribe(new HomeRefreshManager.RefreshCallback() {
            @Override
            public void refresh() {
                if (home_content_sl.getStatus() == StatusLayout.Status.NORMAL) {
                    refreshData();
                }
            }
        });

        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageActivity.start(getContext());

            }
        });
    }

    public void refreshData() {
        home_content_srl.autoRefresh();
    }

    private void loadData() {
        HttpTask taskMenu = HttpTask.create(HttpRequest.create(UrlConstant.APP_MENU_LIST_URL));
        HttpTask taskAudit = HttpTask.create(HttpRequest.create(UrlConstant.AUDIT_LIST_PAGE_URL));
        HttpRequestManager.newInstance().get(taskMenu, new HttpRequestManager.TaskCallBack<List<MenuEntity>>() {

            @Override
            public List<MenuEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, MenuEntity.class);
            }

            @Override
            public void success(List<MenuEntity> menuEntities) {
                List<MenuEntity> menus = appMenuAdapter.filterData(menuEntities);
                //无应用展示
                if (menus == null || menus.size() == 0) {
                    home_apps_tv.setVisibility(View.GONE);
                    home_apps_rv.setVisibility(View.GONE);
                } else {
                    appMenuAdapter.setData(menus);
                }
            }
        }).get(taskAudit, new HttpRequestManager.TaskCallBack<List<ApproveEntity>>() {
            @Override
            public List<ApproveEntity> doInBackground(String response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    List<ApproveEntity> data = jsonArray.toJavaList(ApproveEntity.class);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            public void success(List<ApproveEntity> approveEntities) {
                if (approveEntities == null || approveEntities.size() == 0) {
                    home_approve_ll.setVisibility(View.GONE);
                    home_approve_rv.setVisibility(View.GONE);
                } else {
                    home_approve_ll.setVisibility(View.VISIBLE);
                    home_approve_rv.setVisibility(View.VISIBLE);
                    approveAdapter.setData(approveEntities);
                }
            }
        }).execute(new HttpRequestManager.CallBack() {
            @Override
            public void before() {
                if (appMenuAdapter.getItemCount() == 0 && approveAdapter.getItemCount() == 0) {
                    home_content_sl.loadingStatus();
                }
            }

            @Override
            public void success() {
                home_content_sl.normalStatus();
            }

            @Override
            public void netError() {
                if (home_content_sl.getStatus() != StatusLayout.Status.NORMAL) {
                    home_content_sl.netErrorStatus();
                }
            }

            @Override
            public void netFail() {
                if (home_content_sl.getStatus() != StatusLayout.Status.NORMAL) {
                    home_content_sl.netFailStatus();
                }
            }

            @Override
            public void after() {
                if (home_content_srl.isRefreshing()) {
                    home_content_srl.finishRefresh();
                }
            }
        });

    }

    private String buildUrl(String url, HashMap<String,String> params){
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }

}
