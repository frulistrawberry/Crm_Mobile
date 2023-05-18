package com.baihe.lihepro.fragment;

import android.graphics.Rect;
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
import com.baihe.lihepro.adapter.ProductListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.entity.ProductListEntity;
import com.baihe.lihepro.manager.ProductSelectManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductListFragment extends BaseFragment {
    public static final String INTENT_PRODUCT_CATEGOTY = "INTENT_PRODUCT_CATEGOTY";
    public static final String INTENT_PRODUCT_KEYWORD = "INTENT_PRODUCT_KEYWORD";

    private StatusLayout product_list_sl;
    private SmartRefreshLayout product_list_srl;
    private RecyclerView product_list_rv;

    private CategoryEntity category;
    private String keyWord;
    private ProductListAdapter adapter;
    private int page = 1;

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
        category = getArguments().getParcelable(INTENT_PRODUCT_CATEGOTY);
        keyWord = getArguments().getString(INTENT_PRODUCT_KEYWORD);
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
        adapter = new ProductListAdapter(getContext(), ProductSelectManager.newInstance());
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
    }

    public void restKeyWord(String keyWord) {
        this.keyWord = keyWord;
        adapter.rest();
        page = 1;
        loadData();
    }

    public void refresh() {
        page = 1;
        loadData();
    }

    public void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("category", category.getId()).putParamValue("name", keyWord).putParamValue("pageSize", "20").putParamValue("page", page);
        HttpRequest.create(UrlConstant.PRODUCT_LIST_URL).putParam(jsonParam).get(new CallBack<ProductListEntity>() {
            @Override
            public ProductListEntity doInBackground(String response) {
                return JsonUtils.parse(response, ProductListEntity.class);
            }

            @Override
            public void success(ProductListEntity productListEntity) {
                if (page == 1 && productListEntity.getRows().size() == 0) {
                    //无数据
                    product_list_sl.expandStatus();
                } else {
                    product_list_sl.normalStatus();
                }
                if (page == 1) {
                    adapter.update(productListEntity.getRows());
                } else {
                    adapter.add(productListEntity.getRows());
                }
                page++;

                if (productListEntity.getTotal() > productListEntity.getPage() * productListEntity.getPageSize()) {
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
                if (adapter.getData().size() == 0) {
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
