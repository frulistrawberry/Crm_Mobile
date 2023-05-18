package com.baihe.lihepro.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ApproveListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.SortSelectDialog;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.filter.FilterCallback;
import com.baihe.lihepro.filter.FilterUtils;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-09-07
 * Description：
 */
public class ApproveListActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ApproveListActivity.class);
        context.startActivity(intent);
    }

    public static final int SORT_AOOROVE_AUDITED = 1;
    public static final int SORT_AOOROVE_PENDING = 2;

    private RelativeLayout approve_list_title_rl;
    private Toolbar approve_list_title_tb;
    private LinearLayout approve_list_title_sort_ll;
    private RadioButton approve_list_title_sort_rb;
    private LinearLayout approve_list_title_filter_ll;
    private RecyclerView approve_list_rv;
    private ApproveListAdapter approveAdapter;

    private int sort = SORT_AOOROVE_PENDING;
    private Map<String, Object> filterParmsMap = new HashMap<>();

    private ArrayList<FilterEntity> filterEntities;
    private FilterCallback filterCallback = new FilterCallback() {
        @Override
        public void call(Map<String, Object> requestMap) {
            Logger.d(requestMap);
            filterParmsMap.clear();
            filterParmsMap.putAll(requestMap);
            loadData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_approve_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_approve_list, null), params);
        init();
        initData();
        listener();
        loadFilterData();
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MainActivity.REQUEST_CODE_APPROVE: {
                    loadData();
                }
                break;
            }
        }
    }

    private void init() {
        approve_list_title_rl = findViewById(R.id.approve_list_title_rl);
        approve_list_title_tb = findViewById(R.id.approve_list_title_tb);
        approve_list_title_sort_ll = findViewById(R.id.approve_list_title_sort_ll);
        approve_list_title_sort_rb = findViewById(R.id.approve_list_title_sort_rb);
        approve_list_title_filter_ll = findViewById(R.id.approve_list_title_filter_ll);
        approve_list_rv = findViewById(R.id.approve_list_rv);
    }

    private void initData() {
        approveAdapter = new ApproveListAdapter(context);
        approve_list_rv.setAdapter(approveAdapter);
        approve_list_rv.setLayoutManager(new LinearLayoutManager(context));
        approve_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                } else if (position == approveAdapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, 3));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                }
            }
        });
    }

    private void listener() {
        approveAdapter.setOnItemClickListener(new ApproveListAdapter.OnItemClickListener() {
            @Override
            public void approve(ApproveEntity approveEntity) {
//                if ("1".equals(approveEntity.getAudit_type()))
//                else if ("2".equals(approveEntity.getAudit_type())){
//                    // TODO: 预留单详情
//                }else if ("3".equals(approveEntity.getAudit_type())){
//                    // TODO: 预订单详情
//                }
                ContractDetailActivity.startForApprove(ApproveListActivity.this, approveEntity.getaId(), MainActivity.REQUEST_CODE_APPROVE);

            }
        });
        approve_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        approve_list_title_sort_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
        approve_list_title_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.filter(context, filterEntities, filterCallback);
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
    }


    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("status", sort).putParamValue(filterParmsMap);
        HttpRequest.create(UrlConstant.AUDIT_LIST_URL).putParam(jsonParam).get(new CallBack<List<ApproveEntity>>() {
            @Override
            public List<ApproveEntity> doInBackground(String response) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                List<ApproveEntity> data = JsonUtils.parseList(jsonObject.getString("rows"), ApproveEntity.class);
                return data;
            }

            @Override
            public void success(List<ApproveEntity> list) {
                if (list.size() == 0) {
                    statusLayout.expandStatus();
                } else {
                    statusLayout.normalStatus();
                    approveAdapter.setData(list, sort);
                }

            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
    }


    private void loadFilterData() {
        HttpRequest.create(UrlConstant.FILTER_CONFIGT_URL)
                .putParam(new JsonParam("params")
                        .putParamValue("tab", "audit")).get(new CallBack<ArrayList<FilterEntity>>() {
            @Override
            public ArrayList<FilterEntity> doInBackground(String response) {
                return (ArrayList<FilterEntity>) JsonUtils.parseList(response, FilterEntity.class);
            }

            @Override
            public void success(ArrayList<FilterEntity> filterEntities) {
                ApproveListActivity.this.filterEntities = filterEntities;
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }
        });
    }

    private void showSortDialog() {
        new SortSelectDialog.Builder(context)
                .setSelectDataAdapter(new SortSelectDialog.SelectDataAdapter() {
                    @Override
                    public int getCount() {
                        return 2;
                    }

                    @Override
                    public String getText(int dataPostion) {
                        if (dataPostion == 0) {
                            return "待审批";
                        }
                        return "已审批";
                    }

                    @Override
                    public int initSelectDataPosition() {
                        if (sort == SORT_AOOROVE_AUDITED) {
                            return 1;
                        }
                        return 0;
                    }
                }).setOnSelectListener(new SortSelectDialog.OnSelectListener() {
            @Override
            public void onSelect(Dialog dialog, int position) {
                if (position == 1) {
                    sort = SORT_AOOROVE_AUDITED;
                    approve_list_title_sort_rb.setText("已审批");
                } else {
                    sort = SORT_AOOROVE_PENDING;
                    approve_list_title_sort_rb.setText("待审批");
                }
                dialog.dismiss();
                loadData();
            }
        }).setAttachView(approve_list_title_rl).build().show();
    }
}
