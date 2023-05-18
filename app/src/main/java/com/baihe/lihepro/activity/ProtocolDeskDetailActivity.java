package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.AgreementInfoEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

public class ProtocolDeskDetailActivity extends BaseActivity {
    public static final String INTENT_AGREEMENT_NUM = "INTENT_AGREEMENT_DATA";
    public static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    public static final String INTENT_CONTRACT_CAN_EDIT = "INTENT_CONTRACT_CAN_EDIT";

    private String agreementNum;
    private String contractId;
    private boolean canEdit;

    private KeyValueLayout protocol_add_kvl;
    private TextView btn_edit;
    private TextView tv_type;

    public static void start(Activity activity, String contractId, String agreementNum, boolean canEdit, int requestCode) {
        Intent intent = new Intent(activity, ProtocolDeskDetailActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_AGREEMENT_NUM, agreementNum);
        intent.putExtra(INTENT_CONTRACT_CAN_EDIT, canEdit);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        agreementNum = getIntent().getStringExtra(INTENT_AGREEMENT_NUM);
        canEdit = getIntent().getBooleanExtra(INTENT_CONTRACT_CAN_EDIT,false);
        setTitleText("查看协议");
        setContentView(R.layout.activity_protocol_desk_detail);
        init();
        listener();
        loadData();
    }

    private void init(){
        btn_edit = findViewById(R.id.btn_edit);
        protocol_add_kvl = findViewById(R.id.protocol_add_kvl);
        tv_type = findViewById(R.id.tv_type);
        if (canEdit)
            btn_edit.setVisibility(View.VISIBLE);
        else
            btn_edit.setVisibility(View.GONE);
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("agreementNum", agreementNum).putParamValue("contractId",contractId);
        HttpRequest.create(UrlConstant.GET_ARGUMENT_INFO).putParam(jsonParam).get(new CallBack<List<AgreementInfoEntity>>() {
            @Override
            public List<AgreementInfoEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, AgreementInfoEntity.class);
            }

            @Override
            public void success(List<AgreementInfoEntity> protocolEntity) {
                List<List<KeyValueEntity>> showData = new ArrayList<>();
                for (AgreementInfoEntity agreementInfoEntity : protocolEntity) {
                    showData.add(agreementInfoEntity.covertToKeyValListForDesk());
                }
                tv_type.setText("1".equals(protocolEntity.get(0).getType())?"加桌协议":"减桌协议");
                if (showData!=null && showData.size()>0){
                    protocol_add_kvl.setData(showData.get(0));
                }
                statusLayout.normalStatus();


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

    private void listener() {
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProtocolDeskActivity.start(ProtocolDeskDetailActivity.this,contractId,agreementNum,101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            loadData();
    }

}
