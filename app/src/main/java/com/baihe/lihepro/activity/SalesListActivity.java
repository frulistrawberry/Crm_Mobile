package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.baihe.common.view.FontTextView;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.SalesListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.BottomOneOptionDialog;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.entity.SalesListEntity;
import com.baihe.lihepro.filter.FilterCallback;
import com.baihe.lihepro.filter.FilterUtils;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.utils.Utils;
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
public class SalesListActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SalesListActivity.class);
        context.startActivity(intent);
    }

    private SmartRefreshLayout sales_list_srl;
    private RecyclerView sales_list_rv;
    private Toolbar sales_list_title_tb;
    private ImageView sales_list_title_search_iv;
    private ImageView sales_list_title_add_iv;
    private TextView sales_list_num_tv;
    private TextView sales_list_income_tv;
    private LinearLayout sales_list_title_filter_ll;

    private LinearLayout btnTab1;
    private LinearLayout btnTab2;
    private FontTextView tab1;
    private FontTextView tab2;
    private View line1;
    private View line2;

    private boolean isTeam;

    private SalesListAdapter adapter;
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
        setTitleView(R.layout.activity_sales_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_sales_list, null), params);
        init();
        init2Tab();
        initData();
        loadData();
        loadFilterData();
        listener();
    }

    private void init() {
        sales_list_srl = findViewById(R.id.sales_list_srl);
        sales_list_rv = findViewById(R.id.sales_list_rv);
        sales_list_title_tb = findViewById(R.id.sales_list_title_tb);
        sales_list_title_search_iv = findViewById(R.id.sales_list_title_search_iv);
        sales_list_title_add_iv = findViewById(R.id.sales_list_title_add_iv);
        sales_list_num_tv = findViewById(R.id.sales_list_num_tv);
        sales_list_income_tv = findViewById(R.id.sales_list_income_tv);
        sales_list_title_filter_ll = findViewById(R.id.sales_list_title_filter_ll);
        btnTab1 = findViewById(R.id.btn_tab1);
        btnTab2 = findViewById(R.id.btn_tab2);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
    }

    private void init2Tab() {
        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTeam = false;
                tab1.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                tab2.setFontStyle(FontTextView.FontStyle.NORMAL);
                tab1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                tab2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                tab1.invalidate();
                tab2.invalidate();
                sales_list_srl.autoRefresh();
            }
        });
        btnTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTeam = true;
                tab2.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                tab1.setFontStyle(FontTextView.FontStyle.NORMAL);
                tab1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                tab2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                line2.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
                tab1.invalidate();
                tab2.invalidate();
                sales_list_srl.autoRefresh();
            }
        });
    }

    private void initData() {
        adapter = new SalesListAdapter(context);
        sales_list_rv.setAdapter(adapter);
        sales_list_rv.setLayoutManager(new LinearLayoutManager(context));

        sales_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        jsonParam.putParamValue("pageSize", "20").putParamValue("team",isTeam).putParamValue("page", String.valueOf(page));
        HttpRequest.create(UrlConstant.ORDER_DATA_LIST_URL)
                .tag("销售列表")
                .putParam(jsonParam)
                .get(new CallBack<SalesListEntity>() {
                    @Override
                    public SalesListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, SalesListEntity.class);
                    }

                    @Override
                    public void success(SalesListEntity entity) {
                        sales_list_num_tv.setText("总数量：" + entity.getTotal() + "条");
                        String[] values = Utils.formatPrice(entity.getAmount_total());
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("签单金额：");
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
                        sales_list_income_tv.setText(span);

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
                            sales_list_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            sales_list_srl.setEnableLoadMore(false);
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
                        if (sales_list_srl.isLoading()) {
                            sales_list_srl.finishLoadMore();
                        }
                        if (sales_list_srl.isRefreshing()) {
                            sales_list_srl.finishRefresh();
                        }
                    }
                });
    }

    private void loadFilterData() {
        HttpRequest.create(UrlConstant.FILTER_CONFIGT_URL)
                .putParam(new JsonParam("params")
                        .putParamValue("tab", "order")).get(new CallBack<ArrayList<FilterEntity>>() {
            @Override
            public ArrayList<FilterEntity> doInBackground(String response) {
                return (ArrayList<FilterEntity>) JsonUtils.parseList(response, FilterEntity.class);
            }

            @Override
            public void success(ArrayList<FilterEntity> filterEntities) {
                SalesListActivity.this.filterEntities = filterEntities;
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
        sales_list_title_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.filter(context, filterEntities, filterCallback);
            }
        });
        sales_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sales_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        sales_list_title_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesSearchActivity.start(context);
            }
        });
        sales_list_title_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomOneOptionDialog bottomOneOptionDialog = new BottomOneOptionDialog(SalesListActivity.this, "领取订单", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReceiveOrderActivity.start(context);
                    }
                });
                bottomOneOptionDialog.show();
            }
        });
        adapter.setOnItemClickListener(new SalesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListItemEntity listitemEntity) {
                SalesDetailActivity.start(context, listitemEntity.getOrder_id(), listitemEntity.getCustomer_id(), listitemEntity.getCustomer_name());
            }
        });
    }
}
