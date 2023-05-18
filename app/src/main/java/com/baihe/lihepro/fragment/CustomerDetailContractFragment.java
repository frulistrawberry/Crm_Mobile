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
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ContractDetailActivity;
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.activity.ProtocolDeskDetailActivity;
import com.baihe.lihepro.activity.ProtocolDetailActivity;
import com.baihe.lihepro.adapter.SalesDetailContractAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.ContactDataEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class CustomerDetailContractFragment extends BaseFragment {
    private String customerId;

    private StatusLayout sales_contract_sl;
    private RecyclerView sales_contract_rv;

    private SalesDetailContractAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sales_detail_contract;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customerId = getArguments().getString(CustomerDetailActivity.INTENT_CUSTOMER_ID);
        initView(view);
        initData();
        listener();
        loadData();
    }

    private void initView(View view) {
        sales_contract_sl = view.findViewById(R.id.sales_contract_sl);
        sales_contract_rv = view.findViewById(R.id.sales_contract_rv);
    }

    private void initData() {
        adapter = new SalesDetailContractAdapter(getContext());
        sales_contract_rv.setAdapter(adapter);
        sales_contract_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        sales_contract_sl.setExpandLayout(R.layout.activity_constact_no_data);
        sales_contract_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), 9), 0, CommonUtils.dp2pxForInt(getContext(), 3));
                } else if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, CommonUtils.dp2pxForInt(getContext(), -4), 0, CommonUtils.dp2pxForInt(getContext(), 3));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(getContext(), -4));
                }
            }
        });
    }

    private void listener() {
        sales_contract_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        adapter.setOnItemClickListener(new SalesDetailContractAdapter.OnItemClickListener() {
            @Override
            public void contractDetail(String contractId) {
                ContractDetailActivity.start(getActivity(), contractId, CustomerDetailActivity.REQUEST_CODE_EIDT_CONTRACT);
            }

            @Override
            public void agreementDetail(String contractId, String agreementNum, boolean canEdit) {
                ProtocolDetailActivity.start(getActivity(), contractId, agreementNum,canEdit, CustomerDetailActivity.REQUEST_CODE_EIDT_AGREEMENT);
            }

            @Override
            public void deskAgreementDetail(String contractId, String agreementNum, boolean canEdit) {
                ProtocolDeskDetailActivity.start(getActivity(),contractId,agreementNum,canEdit, CustomerDetailActivity.REQUEST_CODE_EIDT_AGREEMENT);
            }

        });
    }

    public void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.CONTRACT_LIST_URL).putParam(jsonParam).get(new CallBack<List<ContactDataEntity>>() {
            @Override
            public List<ContactDataEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, ContactDataEntity.class);
            }

            @Override
            public void success(List<ContactDataEntity> entities) {
                if (entities.size() > 0) {
                    sales_contract_sl.normalStatus();
                    adapter.updateData(entities);
                } else {
                    sales_contract_sl.expandStatus();
                }
            }

            @Override
            public void error() {
                sales_contract_sl.netErrorStatus();
            }

            @Override
            public void fail() {
                sales_contract_sl.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                sales_contract_sl.loadingStatus();
            }
        });
    }
}
