package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.ProtocolEntity;
import com.baihe.lihepro.entity.SumEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-09-07
 * Description：
 */
public class ProtocolActivity extends BaseActivity {
    public static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    public static final String INTENT_AGREEMENT_NUM = "INTENT_AGREEMENT_DATA";

    public static void start(Activity activity, String contractId, int requestCode) {
        Intent intent = new Intent(activity, ProtocolActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, String contractId, String agreementNum, int requestCode) {
        Intent intent = new Intent(activity, ProtocolActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_AGREEMENT_NUM, agreementNum);
        activity.startActivityForResult(intent, requestCode);
    }

    private RecyclerView protocol_rv;
    private TextView protocol_ok_tv;
    private TextView product_save_tv;

    private ProtocolEntity protocol;
    private String contractId;
    private String agreementNum;
    private ProtocolAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        agreementNum = getIntent().getStringExtra(INTENT_AGREEMENT_NUM);
        if (TextUtils.isEmpty(agreementNum)) {
            setTitleText("新建协议");
        } else {
            setTitleText("编辑协议");
        }
        setContentView(R.layout.activity_protocol);
        init();
        initData();
        listener();
        loadData();
    }

    private void init() {
        protocol_rv = findViewById(R.id.protocol_rv);
        product_save_tv = findViewById(R.id.protocol_save_tv);
        protocol_ok_tv = findViewById(R.id.protocol_ok_tv);
    }

    private void initData() {
        adapter = new ProtocolAdapter(context);
        protocol_rv.setLayoutManager(new LinearLayoutManager(context));
        protocol_rv.setAdapter(adapter);
        protocol_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (adapter.getItemCount() == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 6), 0, CommonUtils.dp2pxForInt(context, 30));
                } else {
                    if (position == 0) {
                        outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                    } else {
                        outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                    }
                }
            }
        });
    }

    private void listener() {
        protocol_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPrice()==null || Double.valueOf(getPrice().getSumAmount()) == 0){
                    ToastUtils.toast("本次协议金额不得为0");
                    return;
                }


                Map<String, Object> paramsMap = getData();
                if (paramsMap != null) {
                    paramsMap.put("auditStatus","3");
                    commitData(paramsMap);
                }
            }
        });

        product_save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPrice()==null || Double.valueOf(getPrice().getSumAmount()) == 0){
                    ToastUtils.toast("本次协议金额不得为0");
                    return;
                }
                Map<String, Object> paramsMap = getData();
                if (paramsMap != null) {
                    paramsMap.put("auditStatus","0");
                    commitData(paramsMap);
                }
            }
        });

        adapter.setOnPriceListener(new ProtocolAdapter.OnPriceChangeListener() {
            @Override
            public void change() {
                updatePrice();
            }
        });
    }

    private void commitData(Map<String, Object> paramsMap) {
        String url;
        if (TextUtils.isEmpty(agreementNum))
            url = UrlConstant.ADD_AGREEMENT_URL;
        else
            url = UrlConstant.UPDATE_AGREEMENT_URL;


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

    private Map<String, Object> getData() {
        Map<String, Object> paramsMap = new HashMap<>();

        List<KeyValueEntity> headData = adapter.getHead();
        Map<String, Object> headMap = KeyValueEditLayout.getCommitMap(headData);
        if (headData == null) {
            return null;
        }
        List<List<KeyValueEntity>> dataList = adapter.getData();
        List<Map<String, Object>> agreementInfo = new ArrayList<>();
        for (List<KeyValueEntity> data : dataList) {
            Map<String, Object> dataMap = KeyValueEditLayout.getCommitMap(data);
            if (dataMap == null) {
                return null;
            }
            agreementInfo.add(dataMap);
        }
        paramsMap.putAll(headMap);
        paramsMap.put("agreementInfo", agreementInfo);
        paramsMap.put("agreementOldNum", protocol.getAgreement_old_num());
        paramsMap.put("contractId", contractId);
        return paramsMap;
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("type", "agreement").putParamValue("contractId", contractId).putParamValue("agreementNum", agreementNum);
        HttpRequest.create(UrlConstant.BUILD_PARAMS_URL).putParam(jsonParam).get(new CallBack<ProtocolEntity>() {
            @Override
            public ProtocolEntity doInBackground(String response) {
                return JsonUtils.parse(response, ProtocolEntity.class);
            }

            @Override
            public void success(ProtocolEntity protocolEntity) {
                statusLayout.normalStatus();
                protocol = protocolEntity;
                adapter.setData(protocol);
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

    private void updatePrice() {
        SumEntity price = getPrice();
        adapter.setSumEntity(price);
        adapter.notifyItemChanged(adapter.getItemCount()-1);
    }

    private SumEntity getPrice() {
        double price = 0D;
        SumEntity sumEntity = new SumEntity();
        List<List<KeyValueEntity>> dataList = adapter.getData();
        for (List<KeyValueEntity> data : dataList) {
            //增项1，减项2
            String agreementType = "";
            double agreementAmount = 0.0;
            for (KeyValueEntity keyValueEntity : data) {
                if ("type".equals(keyValueEntity.getEvent().getParamKey())) {
                    agreementType = keyValueEntity.getDefaultVal();
                }
                if ("amount".equals(keyValueEntity.getEvent().getAction())) {
                    agreementAmount+= Double.parseDouble(TextUtils.isEmpty(keyValueEntity.getVal())?"0.0":keyValueEntity.getVal());
                }
            }
            if (!TextUtils.isEmpty(agreementType)) {
                try {
                    if ("1".equals(agreementType)) {
                        price += agreementAmount;
                        sumEntity.setAddAmount(String.valueOf(agreementAmount));
                    } else if ("2".equals(agreementType)) {
                        sumEntity.setDelAmount(String.valueOf(agreementAmount));
                        price -= agreementAmount;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        sumEntity.setSumAmount(String.valueOf(price));
        return sumEntity;
    }

    public static class ProtocolAdapter extends RecyclerView.Adapter<ProtocolAdapter.Holder> {
        private static final int NORMAL_TYPE = 1;
        private static final int ADD_TYPE = 2;
        private static final int HEADER_TYPE = 3;
        private static final int FOOTER_TYPE = 4;

        private List<List<KeyValueEntity>> dataList;
        private List<KeyValueEntity> headList;
        private List<KeyValueEntity> config;
        private SumEntity sumEntity;
        private LayoutInflater inflater;
        private OnPriceChangeListener priceListener;

        public ProtocolAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.dataList = new ArrayList<>();
            this.headList = new ArrayList<>();
        }

        public void setData(ProtocolEntity protocol) {
            this.dataList.clear();
            this.headList.clear();
            if (protocol != null) {
                this.headList.add(protocol.getAgr_num());
                this.config = protocol.getNew_config();
                if (protocol.getShow_array() != null) {
                    this.dataList.addAll(protocol.getShow_array());
                }
                this.sumEntity = new SumEntity();
                if (dataList.size() == 0)
                    add();
            }
            notifyDataSetChanged();
        }

        public void add() {
            if (config == null) {
                return;
            }
            List<KeyValueEntity> data = new ArrayList();
            if(dataList.size() == 0){
                KeyValueEntity keyValueEntity = new KeyValueEntity();
                KeyValEventEntity eventEntity = new KeyValEventEntity();
                eventEntity.setAction("readonly");
                eventEntity.setParamKey("type");
                keyValueEntity.setEvent(eventEntity);
                keyValueEntity.setVal("增项");
                keyValueEntity.setOptional("1");
                keyValueEntity.setShowStatus("1");
                keyValueEntity.setDefaultVal("1");
                keyValueEntity.setKey("项目增减");
                data.add(keyValueEntity);
            }else {
                KeyValueEntity keyValueEntity = new KeyValueEntity();
                KeyValEventEntity eventEntity = new KeyValEventEntity();
                eventEntity.setAction("readonly");
                eventEntity.setParamKey("type");
                keyValueEntity.setEvent(eventEntity);
                keyValueEntity.setVal("减项");
                keyValueEntity.setOptional("1");
                keyValueEntity.setShowStatus("1");
                keyValueEntity.setDefaultVal("2");
                keyValueEntity.setKey("项目增减");
                data.add(keyValueEntity);
            }
            for (KeyValueEntity keyValueEntity : config) {
                KeyValueEntity keyValueEntity1 = keyValueEntity.copy();
                String key = keyValueEntity1.getKey();
                if (dataList.size()==0){
                    keyValueEntity1.setKey(key+"升级");
                }else {
                    keyValueEntity1.setKey(key+"减项");
                }

                data.add(keyValueEntity1);
            }
            this.dataList.add(data);
            notifyDataSetChanged();
        }

        private List<KeyValueEntity> getHead() {
            return headList;
        }

        private List<List<KeyValueEntity>> getData() {
            return dataList;
        }

        public void setOnPriceListener(OnPriceChangeListener priceListener) {
            this.priceListener = priceListener;
        }

        @Override
        public int getItemViewType(int position) {
            if (config != null && getItemCount() - 2 == position && dataList.size()<2) {
                return ADD_TYPE;
            }
            if (headList.size() > 0 && position == 0) {
                return HEADER_TYPE;
            }
            if (getItemCount()-1 == position)
                return FOOTER_TYPE;
            return NORMAL_TYPE;
        }

        @NonNull
        @Override
        public ProtocolAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == ADD_TYPE) {
                view = inflater.inflate(R.layout.activity_contract_add_item_product_add, parent, false);
            } else if (viewType == HEADER_TYPE) {
                view = inflater.inflate(R.layout.activity_protocol_item_head, parent, false);
            } else if (viewType == FOOTER_TYPE){
                view = inflater.inflate(R.layout.activity_protocol_item_foot,parent,false);
            } else {
                view = inflater.inflate(R.layout.activity_contract_item_product, parent, false);
            }
            return new ProtocolAdapter.Holder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ProtocolAdapter.Holder holder, final int position) {
            int viewType = getItemViewType(position);
            if (viewType == ADD_TYPE) {
                holder.contract_add_item_add_tv.setText("添加减项");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add();
                    }
                });
            } else if (viewType == HEADER_TYPE) {
                holder.protocol_item_header_kvl.setData(headList);
            } else if (viewType == FOOTER_TYPE){
                if (sumEntity != null) {
                    holder.sumAmountTv.setText("￥"+sumEntity.getSumAmount());
                    holder.addAmountTv.setText("￥"+sumEntity.getAddAmount());
                    holder.delAmount.setText("￥"+sumEntity.getDelAmount());
                }
            } else {
                final int index = position - (headList.size() > 0 ? 1 : 0);
                List<KeyValueEntity> data = dataList.get(index);
                holder.contract_add_item_content_kvel.setData(data);
                KeyValueEntity agreementTypeEntity = holder.contract_add_item_content_kvel.findEntityByParamKey("type");
                if ("1".equals(agreementTypeEntity.getDefaultVal())){
                    holder.contract_add_item_name_tv.setText("增项协议");
                    holder.contract_add_item_delete_iv.setVisibility(View.GONE);
                }else {
                    holder.contract_add_item_name_tv.setText("减项协议");
                    holder.contract_add_item_delete_iv.setVisibility(View.VISIBLE);
                }
                holder.contract_add_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataList.remove(index);
                        if (priceListener != null) {
                            priceListener.change();
                        }
                        notifyDataSetChanged();
                    }
                });
                holder.contract_add_item_content_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
                    @Override
                    public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                        if ("amount".equals(keyValueEntity.getEvent().getAction())) {
                            if (priceListener != null) {
                                priceListener.change();
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            int count = headList.size() > 0 ? 1 : 0;
            count += dataList.size();
            if ( config != null && dataList.size() == 1) {
                count++;
            }
                count++;
            return count;
        }

        public void setSumEntity(SumEntity sumEntity) {
            this.sumEntity = sumEntity;
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView contract_add_item_add_tv;

            public TextView contract_add_item_name_tv;
            public KeyValueEditLayout contract_add_item_content_kvel;
            public ImageView contract_add_item_delete_iv;

            public KeyValueEditLayout protocol_item_header_kvl;

            private TextView addAmountTv;
            private TextView delAmount;
            private TextView sumAmountTv;


            public Holder(@NonNull View itemView, int viewType) {
                super(itemView);
                if (viewType == ADD_TYPE) {
                    contract_add_item_add_tv = itemView.findViewById(R.id.contract_add_item_add_tv);
                } else if (viewType == HEADER_TYPE) {
                    protocol_item_header_kvl = itemView.findViewById(R.id.protocol_item_header_kvl);
                }else if (viewType == FOOTER_TYPE){
                    addAmountTv = itemView.findViewById(R.id.tv_add_price);
                    delAmount = itemView.findViewById(R.id.tv_del_price);
                    sumAmountTv = itemView.findViewById(R.id.tv_sum_price);

                } else {
                    contract_add_item_name_tv = itemView.findViewById(R.id.contract_add_item_name_tv);
                    contract_add_item_content_kvel = itemView.findViewById(R.id.contract_add_item_content_kvel);
                    contract_add_item_delete_iv = itemView.findViewById(R.id.contract_add_item_delete_iv);
                }
            }
        }

        public interface OnPriceChangeListener {
            void change();
        }
    }

}
