package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.AgreementInfoEntity;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtocolDeskActivity extends BaseActivity {
    public static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    public static final String INTENT_AGREEMENT_NUM = "INTENT_AGREEMENT_DATA";

    private TextView protocol_ok_tv;
    private KeyValueEditLayout contract_add_item_content_kvel;

    private String contractId;
    private String agreementNum;

    public static void start(Activity activity, String contractId, int requestCode) {
        Intent intent = new Intent(activity, ProtocolDeskActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, String contractId, String agreementNum, int requestCode) {
        Intent intent = new Intent(activity, ProtocolDeskActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_AGREEMENT_NUM, agreementNum);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        agreementNum = getIntent().getStringExtra(INTENT_AGREEMENT_NUM);
        setTitleText("加减桌");
        setContentView(R.layout.activity_protocol_desk);
        init();
        listener();
        loadData();
    }

    private void loadData() {
        if (TextUtils.isEmpty(agreementNum)){
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity keyValueEntity1 = new KeyValueEntity();
            keyValueEntity1.setKey("加减类型");
            keyValueEntity1.setOptional("1");
            KeyValEventEntity eventEntity1 = new KeyValEventEntity();
            eventEntity1.setAction("select");
            eventEntity1.setParamKey("type");
            List<KeyValueEntity> options = new ArrayList<>();
            KeyValueEntity option1 = new KeyValueEntity();
            option1.setKey("加桌");
            option1.setVal("1");
            options.add(option1);
            KeyValueEntity option2 = new KeyValueEntity();
            option2.setKey("减桌");
            option2.setVal("2");
            options.add(option2);
            eventEntity1.setOptions(options);
            keyValueEntity1.setEvent(eventEntity1);

            KeyValueEntity keyValueEntity2 = new KeyValueEntity();
            keyValueEntity2.setKey("桌数");
            keyValueEntity2.setEndText("桌");
            keyValueEntity2.setOptional("1");
            KeyValEventEntity eventEntity2 = new KeyValEventEntity();
            eventEntity2.setAction("amount");
            eventEntity2.setParamKey("desk_table");
            keyValueEntity2.setEvent(eventEntity2);

            KeyValueEntity keyValueEntity3 = new KeyValueEntity();
            keyValueEntity3.setKey("每桌价格");
            keyValueEntity3.setEndText("元");
            keyValueEntity3.setOptional("1");
            KeyValEventEntity eventEntity3 = new KeyValEventEntity();
            eventEntity3.setAction("amount");
            eventEntity3.setParamKey("desk");
            keyValueEntity3.setEvent(eventEntity3);

            keyValueEntity1.setShowStatus("1");
            keyValueEntity2.setShowStatus("1");
            keyValueEntity3.setShowStatus("1");
            data.add(keyValueEntity1);
            data.add(keyValueEntity2);
            data.add(keyValueEntity3);
            contract_add_item_content_kvel.setData(data);
        }else {
            JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("agreementNum", agreementNum);
            HttpRequest.create(UrlConstant.GET_ARGUMENT_INFO).putParam(jsonParam).get(new CallBack<List<AgreementInfoEntity>>() {

                @Override
                public List<AgreementInfoEntity> doInBackground(String response) {
                    return JsonUtils.parseList(response,AgreementInfoEntity.class);
                }

                @Override
                public void success(List<AgreementInfoEntity> agreementInfoEntities) {
                    if (agreementInfoEntities!=null && agreementInfoEntities.size()>0){
                        AgreementInfoEntity infoEntity = agreementInfoEntities.get(0);
                        List<KeyValueEntity> data = new ArrayList<>();
                        KeyValueEntity keyValueEntity1 = new KeyValueEntity();
                        keyValueEntity1.setKey("加减类型");
                        keyValueEntity1.setOptional("1");
                        KeyValEventEntity eventEntity1 = new KeyValEventEntity();
                        eventEntity1.setAction("select");
                        keyValueEntity1.setVal("1".equals(infoEntity.getType())?"加桌":"减桌");
                        keyValueEntity1.setDefaultVal(infoEntity.getType());
                        eventEntity1.setParamKey("type");
                        List<KeyValueEntity> options = new ArrayList<>();
                        KeyValueEntity option1 = new KeyValueEntity();
                        option1.setKey("加桌");
                        option1.setVal("1");
                        options.add(option1);
                        KeyValueEntity option2 = new KeyValueEntity();
                        option1.setKey("减桌");
                        option1.setVal("2");
                        options.add(option2);
                        keyValueEntity1.setEvent(eventEntity1);

                        KeyValueEntity keyValueEntity2 = new KeyValueEntity();
                        keyValueEntity2.setKey("桌数");
                        keyValueEntity2.setEndText("桌");
                        KeyValEventEntity eventEntity2 = new KeyValEventEntity();
                        keyValueEntity2.setVal(infoEntity.getDesk_table());
                        keyValueEntity2.setDefaultVal(infoEntity.getDesk_table());
                        eventEntity2.setAction("amount");
                        keyValueEntity2.setOptional("1");
                        eventEntity2.setParamKey("desk_table");
                        keyValueEntity2.setEvent(eventEntity2);

                        KeyValueEntity keyValueEntity3 = new KeyValueEntity();
                        keyValueEntity3.setKey("每桌价格");
                        keyValueEntity3.setEndText("元");
                        KeyValEventEntity eventEntity3 = new KeyValEventEntity();
                        eventEntity3.setAction("amount");
                        keyValueEntity3.setOptional("1");
                        eventEntity3.setParamKey("desk");
                        keyValueEntity3.setEvent(eventEntity3);
                        keyValueEntity3.setVal(infoEntity.getDesk());
                        keyValueEntity3.setDefaultVal(infoEntity.getDesk());
                        keyValueEntity1.setShowStatus("1");
                        keyValueEntity2.setShowStatus("1");
                        keyValueEntity3.setShowStatus("1");
                        data.add(keyValueEntity1);
                        data.add(keyValueEntity2);
                        data.add(keyValueEntity3);
                        contract_add_item_content_kvel.setData(data);
                    }

                }

                @Override
                public void error() {

                }

                @Override
                public void fail() {

                }
            });

        }

    }

    private void listener() {
        protocol_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = contract_add_item_content_kvel.commit();
                if (paramsMap != null) {
                    List<Map<String, Object>> agreementInfo = new ArrayList<>();
                    agreementInfo.add(paramsMap);
                    Map<String,Object> params = new HashMap<>();
                    params.put("agreementInfo",agreementInfo);
                    commitData(params);
                }
            }
        });
    }

    private void commitData(Map<String, Object> paramsMap) {

        String url;
        agreementNum = getIntent().getStringExtra(INTENT_AGREEMENT_NUM);
        if (TextUtils.isEmpty(agreementNum)){
            url = UrlConstant.ADD_AGREEMENT_URL;
            agreementNum = System.currentTimeMillis()+"";
        }
        else
            url = UrlConstant.UPDATE_AGREEMENT_URL;

        paramsMap.put("agreementNum",agreementNum);
        paramsMap.put("contractId", contractId);
        paramsMap.put("auditStatus","3");
        HttpRequest.create(url).putParam(JsonParam.newInstance("params").putParamValue(paramsMap)).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }

            @Override
            public void before() {
                super.before();
                showOptionLoading();
            }
        });
    }

    private void init() {
        contract_add_item_content_kvel = findViewById(R.id.contract_add_item_content_kvel);
        protocol_ok_tv = findViewById(R.id.protocol_ok_tv);
    }


}
