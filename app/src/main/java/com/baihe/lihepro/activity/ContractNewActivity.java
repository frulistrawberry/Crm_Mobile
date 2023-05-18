package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.UploadFileWrap;
import com.baihe.http.callback.CallBack;
import com.baihe.http.callback.UploadCallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.SimpleDialog;
import com.baihe.lihepro.entity.AttachmentEntity;
import com.baihe.lihepro.entity.ContactAddEntity;
import com.baihe.lihepro.entity.ContractResult;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.entity.ProductEntity;
import com.baihe.lihepro.entity.UploadResultEntity;
import com.baihe.lihepro.glide.transformation.RoundedCornersTransformation;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Author：xubo
 * Time：2020-09-03
 * Description：
 */
public class ContractNewActivity extends BaseActivity {
    private static final int REQUEST_CODE_SELECT_PHOTO = 100;
    private static final int REQUEST_CODE_ASSOCIATE_ORDER = 400;

    private static final int MAX_ATTACHMENT_NUM = 50;
    private static final int MAX_ATTACHMENT_SELECT_NUM = 9;

    private static final String INTENT_IS_EDIT = "INTENT_IS_EDIT";
    private static final String INTENT_ORDER_ID = "INTENT_ORDER_ID";
    private static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    private static final String INTENT_CONTRACT_TYPE = "INTENT_CONTRACT_TYPE";

    public static void start(Context context) {
        Intent intent = new Intent(context, ContractNewActivity.class);
        context.startActivity(intent);
    }

    public static void start(Activity activity,String orderId, String contractType,int requestCode) {
        Intent intent = new Intent(activity, ContractNewActivity.class);
        intent.putExtra(INTENT_ORDER_ID, orderId);
        intent.putExtra(INTENT_CONTRACT_TYPE, contractType);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ContractNewActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity,String date,String hallId,String hallName,int sType,int startType,int endType,String startDate,String endDate,int isMulti) {
        Intent intent = new Intent(activity, ContractNewActivity.class);
        intent.putExtra("date",date);
        intent.putExtra("hallId",hallId);
        intent.putExtra("hallName",hallName);
        intent.putExtra("sType",sType);
        intent.putExtra("startType",startType);
        intent.putExtra("endType",endType);
        intent.putExtra("startDate",startDate);
        intent.putExtra("endDate",endDate);
        intent.putExtra("isMulti",isMulti);
        intent.putExtra("isForSchedule",true);
        activity.startActivityForResult(intent, 101);
    }

    public static void start(Activity activity, String orderId, int requestCode) {
        Intent intent = new Intent(activity, ContractNewActivity.class);
        intent.putExtra(INTENT_ORDER_ID, orderId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startEidt(Activity activity, String contractId, int requestCode) {
        Intent intent = new Intent(activity, ContractNewActivity.class);
        intent.putExtra(INTENT_IS_EDIT, true);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        activity.startActivityForResult(intent, requestCode);
    }

    private TextView contarct_add_data_title_tv;
    private LinearLayout contarct_add_data_ll;
    private KeyValueEditLayout contarct_add_data_kvel;
    private TextView contarct_add_customer_title_tv;
    private LinearLayout contarct_add_customer_ll;
    private KeyValueEditLayout contarct_add_customer_kvel;
    private TextView contarct_add_important_title_tv;
    private LinearLayout contarct_add_important_ll;
    private KeyValueEditLayout contarct_add_important_kvel;
    private TextView contarct_add_plan_title_tv;
    private LinearLayout contarct_add_plan_ll;
    private KeyValueEditLayout contarct_add_plan_kvel;
    private RecyclerView contarct_add_attachment_rv;
    private TextView contarct_add_ok_tv;
    private TextView contarct_add_save_tv;

    private AttachmentAddAdapter attachmentAddAdapter;
    private Dialog associateOrderDialog;

    //是否编辑状态
    private boolean isEdit;
    //编辑的订单id
    private String orderId;
    //编辑的合同id
    private String contractId;
    //合同类型
    private String contractType;

    boolean hasValue = false;

    List<KeyValueEntity> planData = null;
    private String hotelId;

    private String date;
    private String hallId;
    private String hallName;
    private int sType;
    private int startType;
    private int endType;
    private String startDate;
    private String endDate;
    private int isMulti;
    private boolean isForSchedule;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEdit = getIntent().getBooleanExtra(INTENT_IS_EDIT, false);
        orderId = getIntent().getStringExtra(INTENT_ORDER_ID);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        contractType = getIntent().getStringExtra(INTENT_CONTRACT_TYPE);
        String titleWrapper = "";
        if (contractType != null){
            switch (contractType){
                case "1":
                    titleWrapper = "代收代付";
                    break;
                case "2":
                    titleWrapper = "酒店直签";
                    break;
                case "3":
                    titleWrapper = "自营签单";
                    break;
            }
        }
        if (isEdit) {
            setTitleText("编辑合同");
        } else {
            setTitleText("新建"+titleWrapper+"合同");
        }
        setContentView(R.layout.activity_contract_add);
        init();
        initData();
        listener();
        loadData();
    }

    private void init() {
        contarct_add_data_title_tv = findViewById(R.id.contarct_add_data_title_tv);
        contarct_add_data_ll = findViewById(R.id.contarct_add_data_ll);
        contarct_add_data_kvel = findViewById(R.id.contarct_add_data_kvel);
        contarct_add_customer_title_tv = findViewById(R.id.contarct_add_customer_title_tv);
        contarct_add_customer_ll = findViewById(R.id.contarct_add_customer_ll);
        contarct_add_customer_kvel = findViewById(R.id.contarct_add_customer_kvel);

        contarct_add_plan_title_tv = findViewById(R.id.contarct_add_plan_title_tv);
        contarct_add_plan_ll = findViewById(R.id.contarct_add_plan_ll);
        contarct_add_plan_kvel = findViewById(R.id.contarct_add_plan_kvel);


        contarct_add_important_title_tv = findViewById(R.id.contarct_add_important_title_tv);
        contarct_add_important_ll = findViewById(R.id.contarct_add_important_ll);
        contarct_add_important_kvel = findViewById(R.id.contarct_add_important_kvel);
        contarct_add_attachment_rv = findViewById(R.id.contarct_add_attachment_rv);



        contarct_add_ok_tv = findViewById(R.id.contarct_add_ok_tv);
        contarct_add_save_tv = findViewById(R.id.contarct_add_save_tv);
    }

    private void initData() {

        hallId = getIntent().getStringExtra("hallId");
        hallName = getIntent().getStringExtra("hallName");
        date = getIntent().getStringExtra("date");
        sType = getIntent().getIntExtra("sType",-1);
        startType = getIntent().getIntExtra("startType",-1);
        endType = getIntent().getIntExtra("endType",-1);
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        isMulti = getIntent().getIntExtra("isMulti",-1);
        isForSchedule = getIntent().getBooleanExtra("isForSchedule",false);


        //合同照片
        attachmentAddAdapter = new AttachmentAddAdapter(context);
        contarct_add_attachment_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        final int offset = CommonUtils.dp2pxForInt(context, 15);
        contarct_add_attachment_rv.setAdapter(attachmentAddAdapter);
        contarct_add_attachment_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == attachmentAddAdapter.getItemCount() - 1) {
                    outRect.set(offset, 0, offset, 0);
                } else {
                    outRect.set(offset, 0, 0, 0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
                List<String> selectPhotoPaths = data.getStringArrayListExtra(PhotoListActivity.RESULT_INTENT_SELECT_PHOTO_PATHS);
                List<AttachmentEntity> attachments = new ArrayList<>();
                for (String path : selectPhotoPaths) {
                    attachments.add(AttachmentEntity.createLocal(path));
                }
                attachmentAddAdapter.add(attachments);
            }  else if (requestCode == REQUEST_CODE_ASSOCIATE_ORDER) {
                ListItemEntity listItemEntity = data.getParcelableExtra(ReceiveOrderActivity.INTENT_ORDER_DATA);
                contractType = data.getStringExtra(ReceiveOrderActivity.INTENT_CONTRACT_TYPE);
                String titleWrapper = "";
                if (contractType != null){
                    switch (contractType){
                        case "1":
                            titleWrapper = "代收代付";
                            break;
                        case "2":
                            titleWrapper = "酒店直签";
                            break;
                        case "3":
                            titleWrapper = "自营签单";
                            break;
                    }
                }
                if (isEdit) {
                    TextView title_text_tv = titleView.findViewById(com.baihe.common.R.id.title_text_tv);
                    title_text_tv.getPaint().setFakeBoldText(true);
                    title_text_tv.setText("编辑合同");
                } else {
                    TextView title_text_tv = titleView.findViewById(com.baihe.common.R.id.title_text_tv);
                    title_text_tv.getPaint().setFakeBoldText(true);
                    title_text_tv.setText("新建"+titleWrapper+"合同");
                }
                if (listItemEntity != null) {
                    if (associateOrderDialog != null && associateOrderDialog.isShowing()) {
                        associateOrderDialog.dismiss();
                    }

                    orderId = listItemEntity.getOrder_id();
                    loadData();
                }
            }
        }
    }

    private void listener() {
        contarct_add_data_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                if ("totalAmount".equals(keyValueEntity.getEvent().getParamKey())){
                    KeyValueEntity planTypeEntity = contarct_add_plan_kvel.findEntityByParamKey("planType");
                    if (!TextUtils.isEmpty(planTypeEntity.getDefaultVal())){
                        String value = planTypeEntity.getVal();
                        if (!TextUtils.isEmpty(planTypeEntity.getDefaultVal())){
                            String[] percents = value.split("～");
                            String totalAmount = keyValueEntity.getDefaultVal();
                            if (percents.length == 3) {
                                String[] paramKeys = new String[]{"firstPlanAmount","secondPlanAmount","thirdPlanAmount"};
                                for (int i = 0; i < percents.length; i++) {
                                    String percent = percents[i].replace("%","");
                                    KeyValueEntity planAmountEntity = contarct_add_plan_kvel.findEntityByParamKey(paramKeys[i]);
                                    if (planAmountEntity != null) {
                                        if (TextUtils.isEmpty(totalAmount))
                                            totalAmount = "0";
                                        double planAmount = Double.parseDouble(percent)/100*Double.parseDouble(totalAmount);
                                        planAmountEntity.setVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.setDefaultVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.getEvent().setAction("readonly");
                                    }
                                }

                            }
                        }else {
                            String[] paramKeys = new String[]{"firstPlanAmount","secondPlanAmount","thirdPlanAmount"};
                            for (int i = 0; i < paramKeys.length; i++) {
                                KeyValueEntity planAmountEntity = contarct_add_plan_kvel.findEntityByParamKey(paramKeys[i]);
                                if (planAmountEntity != null) {
                                    planAmountEntity.setVal("");
                                    planAmountEntity.setDefaultVal("");
                                    planAmountEntity.getEvent().setAction("amount");
                                }
                            }

                        }
                        contarct_add_plan_kvel.setData(contarct_add_plan_kvel.getData());
                    }
                }


            }
        });


        contarct_add_important_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                if ("hotel".equals(keyValueEntity.getEvent().getParamKey())){
                    hotelId = keyValueEntity.getTempValue();
                }
                if ("hotelHall".equals(keyValueEntity.getEvent().getParamKey())){
                    hallId = keyValueEntity.getTempValue();
                }
            }
        });





        attachmentAddAdapter.setOnItemClickListener(new AttachmentAddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<AttachmentEntity> attachments, int postion) {
                ArrayList pics = new ArrayList();
                for (AttachmentEntity attachment : attachments) {
                    pics.add(attachment.getUrl());
                }
                PhotoBrowserActivity.start(context, pics, postion);
            }

            @Override
            public void onItemDelete(int postion) {
                attachmentAddAdapter.remove(postion);
            }

            @Override
            public void onItemAdd() {
                int dataSize = attachmentAddAdapter.getDataSize();
                int selectMaxSize = MAX_ATTACHMENT_NUM - dataSize;
                selectMaxSize = selectMaxSize > MAX_ATTACHMENT_SELECT_NUM ? MAX_ATTACHMENT_SELECT_NUM : selectMaxSize;

                PhotoListActivity.start(ContractNewActivity.this, selectMaxSize, REQUEST_CODE_SELECT_PHOTO);
            }
        });
        contarct_add_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                if (!TextUtils.isEmpty(hotelId))
                    params.put("hotelId",hotelId);
                //合同信息
                if (contarct_add_data_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_data_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }
                //回款计划
                if (contarct_add_plan_ll.getVisibility() == View.VISIBLE){
                    Map<String, Object> map = contarct_add_plan_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }

                //客户信息
                if (contarct_add_customer_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_customer_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }
                //需求信息
                if (contarct_add_important_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_important_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }

                //附件
                List<AttachmentEntity> attachments = attachmentAddAdapter.getData();
                if (attachments == null || attachments.size() == 0){
                    ToastUtils.toast("合同照片请至少上传一张");
                    return;
                }
                if (!TextUtils.isEmpty(contractType))
                    params.put("contractType",contractType);

                params.put("isCheck","1");
                double totalAmount;
                double fAmount;
                double sAmount;
                double tAmount;

                totalAmount = TextUtils.isEmpty((String)params.get("totalAmount"))?0:Double.parseDouble((String) params.get("totalAmount"));
                fAmount = TextUtils.isEmpty((String)params.get("firstPlanAmount"))?0:Double.parseDouble((String) params.get("firstPlanAmount"));
                sAmount = TextUtils.isEmpty((String)params.get("secondPlanAmount"))?0:Double.parseDouble((String) params.get("secondPlanAmount"));
                tAmount = TextUtils.isEmpty((String)params.get("thirdPlanAmount"))?0:Double.parseDouble((String) params.get("thirdPlanAmount"));
                if (totalAmount!=(tAmount+fAmount+sAmount)){
                    ToastUtils.toast("设置的分期待回款金额合计金额需等于合同金额");
                    return;
                }

                //提交数据
                showOptionLoading();
                List<String> uploadUrls = new ArrayList<>();
                uploadAttachments(attachments, params, uploadUrls);
            }
        });
        contarct_add_save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                //合同信息
                if (!TextUtils.isEmpty(hotelId))
                    params.put("hotelId",hotelId);
                if (contarct_add_data_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_data_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }
                //回款计划
                if (contarct_add_plan_ll.getVisibility() == View.VISIBLE){
                    Map<String, Object> map = contarct_add_plan_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }

                //客户信息
                if (contarct_add_customer_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_customer_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }
                //需求信息
                if (contarct_add_important_ll.getVisibility() == View.VISIBLE) {
                    Map<String, Object> map = contarct_add_important_kvel.commit();
                    if (map == null) {
                        return;
                    }
                    params.putAll(map);
                }

                //附件
                List<AttachmentEntity> attachments = attachmentAddAdapter.getData();
                if (attachments == null || attachments.size() == 0){
                    ToastUtils.toast("合同照片请至少上传一张");
                    return;
                }
                if (!TextUtils.isEmpty(contractType))
                    params.put("contractType",contractType);

                double totalAmount;
                double fAmount;
                double sAmount;
                double tAmount;

                totalAmount = TextUtils.isEmpty((String)params.get("totalAmount"))?0:Double.parseDouble((String) params.get("totalAmount"));
                fAmount = TextUtils.isEmpty((String)params.get("firstPlanAmount"))?0:Double.parseDouble((String) params.get("firstPlanAmount"));
                sAmount = TextUtils.isEmpty((String)params.get("secondPlanAmount"))?0:Double.parseDouble((String) params.get("secondPlanAmount"));
                tAmount = TextUtils.isEmpty((String)params.get("thirdPlanAmount"))?0:Double.parseDouble((String) params.get("thirdPlanAmount"));
                if (totalAmount!=(tAmount+fAmount+sAmount)){
                    ToastUtils.toast("设置的分期待回款金额合计金额需等于合同金额");
                    return;
                }

                params.put("isCheck","0");

                //提交数据
                showOptionLoading();
                List<String> uploadUrls = new ArrayList<>();
                uploadAttachments(attachments, params, uploadUrls);
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
        contarct_add_customer_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                if ("groomMobile".equals(keyValueEntity.getEvent().getParamKey())) {  //新郎手机号
                    KeyValueEntity brideKv = contarct_add_customer_kvel.findEntityByParamKey("brideMobile");
                    if (TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        if (TextUtils.isEmpty(brideKv.getDefaultVal())) {  //两者都为空，都必填写
                            keyValueEntity.setOptional("1");
                            brideKv.setOptional("1");
                        } else {  //新娘不为空，新娘必填写，新郎非必填
                            keyValueEntity.setOptional("0");
                            brideKv.setOptional("1");
                        }
                    } else {
                        if (TextUtils.isEmpty(brideKv.getDefaultVal())) {  //新郎不为空，新郎必填写，新娘非必填
                            keyValueEntity.setOptional("1");
                            brideKv.setOptional("0");
                        } else {   //两者都不为空，都必填写
                            keyValueEntity.setOptional("1");
                            brideKv.setOptional("1");
                        }
                    }
                    contarct_add_customer_kvel.refreshItem(keyValueEntity);
                    contarct_add_customer_kvel.refreshItem(brideKv);
                } else if ("brideMobile".equals(keyValueEntity.getEvent().getParamKey())) {  //新娘手机号
                    KeyValueEntity groomKv = contarct_add_customer_kvel.findEntityByParamKey("groomMobile");
                    if (TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        if (TextUtils.isEmpty(groomKv.getDefaultVal())) {  //两者都为空，都必填写
                            keyValueEntity.setOptional("1");
                            groomKv.setOptional("1");
                        } else {  //新郎不为空，新郎必填写，新娘非必填
                            keyValueEntity.setOptional("0");
                            groomKv.setOptional("1");
                        }
                    } else {
                        if (TextUtils.isEmpty(groomKv.getDefaultVal())) { //新娘不为空，新娘必填写，新郎非必填
                            keyValueEntity.setOptional("1");
                            groomKv.setOptional("0");
                        } else {   //两者都不为空，都必填写
                            keyValueEntity.setOptional("1");
                            groomKv.setOptional("1");
                        }
                    }
                    contarct_add_customer_kvel.refreshItem(keyValueEntity);
                    contarct_add_customer_kvel.refreshItem(groomKv);
                }
            }
        });

        contarct_add_plan_kvel.setOnItemActionCheckListener(new KeyValueEditLayout.OnItemActionCheckListener() {
            @Override
            public boolean onCheck(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                if ("planType".equals(keyValueEntity.getEvent().getParamKey())){
                    KeyValueEntity totalAmountEntity = contarct_add_data_kvel.findEntityByParamKey("totalAmount");
                    if (TextUtils.isEmpty(totalAmountEntity.getDefaultVal())){
                        ToastUtils.toast("请填写合同金额");
                        return false;
                    }
                }
                return true;
            }
        });
        
        contarct_add_plan_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                if ("planType".equals(keyValueEntity.getEvent().getParamKey())){
                    if (!hasValue){
                        for (KeyValueEntity planDatum : planData) {
                            if (planDatum.getEvent().getParamKey().equals("planType")){
                                planDatum = keyValueEntity;
                            }
                        }
                        contarct_add_plan_kvel.setData(planData);
                    }

                    String value = keyValueEntity.getVal();
                    if (!"362".equals(keyValueEntity.getDefaultVal())){
                        String[] percents = value.split("～");
                        String totalAmount = contarct_add_data_kvel.findEntityByParamKey("totalAmount").getDefaultVal();
                        if (percents.length == 3) {
                            String[] paramKeys = new String[]{"firstPlanAmount","secondPlanAmount","thirdPlanAmount"};
                            for (int i = 0; i < percents.length; i++) {
                                String percent = percents[i].replace("%","");
                                KeyValueEntity planAmountEntity = contarct_add_plan_kvel.findEntityByParamKey(paramKeys[i]);
                                if (planAmountEntity != null) {
                                    if (i!=2){
                                        double planAmount = Double.parseDouble(percent)/100*Double.parseDouble(totalAmount);
                                        planAmountEntity.setVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.setDefaultVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.getEvent().setAction("readonly");
                                    }else {
                                        KeyValueEntity plan1 = contarct_add_plan_kvel.findEntityByParamKey("firstPlanAmount");
                                        KeyValueEntity plan2 = contarct_add_plan_kvel.findEntityByParamKey("secondPlanAmount");
                                        double pa1 = Double.valueOf(plan1.getVal());
                                        double pa2 = Double.valueOf(plan2.getVal());
                                        double planAmount = Double.parseDouble(totalAmount) - pa1 - pa2;
                                        planAmountEntity.setVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.setDefaultVal(String.valueOf(formatDouble2(planAmount)));
                                        planAmountEntity.getEvent().setAction("readonly");
                                    }

                                }
                            }

                        }
                    }else {
                        String[] paramKeys = new String[]{"firstPlanAmount","secondPlanAmount","thirdPlanAmount"};
                        for (int i = 0; i < paramKeys.length; i++) {
                            KeyValueEntity planAmountEntity = contarct_add_plan_kvel.findEntityByParamKey(paramKeys[i]);
                            if (planAmountEntity != null) {
                                planAmountEntity.setVal("");
                                planAmountEntity.setDefaultVal("");
                                planAmountEntity.getEvent().setAction("amount");
                            }
                        }

                    }
                    contarct_add_plan_kvel.setData(contarct_add_plan_kvel.getData());
                }
            }
        });
    }

    private double formatDouble2(double d) {
        BigDecimal bigDecimal = new BigDecimal(d);
        double bg = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return bg;
    }


    private void uploadAttachments(List<AttachmentEntity> attachments, Map<String, Object> data, List<String> uploadUrls) {
        Queue<AttachmentEntity> queue = new LinkedList<>();
        for (AttachmentEntity attachment : attachments) {
            queue.offer(attachment);
        }
        uploadAttachment(queue, data, uploadUrls);
    }

    private void uploadAttachment(final Queue<AttachmentEntity> queue, final Map<String, Object> data, final List<String> uploadUrls) {
        AttachmentEntity attachment = queue.poll();
        if (attachment == null) {
            commit(data, uploadUrls);
            return;
        }
        if (attachment.getType() == AttachmentEntity.TYPE_REMOTE) {
            //远程地址自动剔除域名头
            if (attachment.getUrl().contains("://")) {
                int index = attachment.getUrl().indexOf("://") + "://".length();
                String str = attachment.getUrl().substring(index);
                if (str.contains("/")) {
                    index = str.indexOf("/");
                    uploadUrls.add(str.substring(index));
                } else {
                    uploadUrls.add(attachment.getUrl());
                }
            } else {
                uploadUrls.add(attachment.getUrl());
            }
            if (queue.peek() == null) {
                commit(data, uploadUrls);
            } else {
                uploadAttachment(queue, data, uploadUrls);
            }
            return;
        }
        HttpRequest.create(UrlConstant.UPLOAD_URL).upload(new UploadFileWrap(attachment.getUrl(), "file", "image/jpg"), new UploadCallBack<UploadResultEntity>() {
            @Override
            public void uploadProgress(long uploadSize, long totalSize, double progress) {

            }

            @Override
            public UploadResultEntity doInBackground(String response) {
                return JsonUtils.parse(response, UploadResultEntity.class);
            }

            @Override
            public void success(UploadResultEntity entity) {
                uploadUrls.add(entity.getPath());
                if (queue.peek() == null) {
                    commit(data, uploadUrls);
                } else {
                    uploadAttachment(queue, data, uploadUrls);
                }
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
            public void after(UploadResultEntity entity) {
                super.after(entity);
                if (entity == null) {
                    dismissOptionLoading();
                }
            }
        });
    }

    private void commit(Map<String, Object> data, List<String> uploadUrls) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < uploadUrls.size(); i++) {
            buffer.append(uploadUrls.get(i));
            if (i < data.size() - 1) {
                buffer.append(",");
            }
        }
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue(data)
                .putParamValue("contractPic", buffer.toString());
        String url;
        if (isEdit) {
            jsonParam.putParamValue("contractId", contractId);
            url = UrlConstant.UPDATE_CONTRACT_URL;
        } else {
            url = UrlConstant.CREATE_CONTRACT_URL;
        }
        HttpRequest.create(url).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<ContractResult>() {
            @Override
            public ContractResult doInBackground(String response) {
                return JsonUtils.parse(response,ContractResult.class);
            }

            @Override
            public void success(ContractResult result) {
                if (isEdit) {
                    ToastUtils.toast("合同编辑成功");
                } else {
                    ToastUtils.toast("合同新建成功");
                }
                if (hallName!=null){
                    BookDetailActivity.start(ContractNewActivity.this,result.getBookId());
                }
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
        });
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("type", "contract");
        if (isEdit) {
            jsonParam.putParamValue("contractId", contractId);
        } else {
            jsonParam.putParamValue("orderNum", orderId);
        }
        if (!TextUtils.isEmpty(contractType))
            jsonParam.putParamValue("contractType",contractType);
        HttpRequest.create(UrlConstant.BUILD_PARAMS_URL).putParam(jsonParam).get(new CallBack<ContactAddEntity>() {
            @Override
            public ContactAddEntity doInBackground(String response) {
                return JsonUtils.parse(response, ContactAddEntity.class);
            }

            @Override
            public void success(ContactAddEntity contactAddEntity) {
                statusLayout.normalStatus();
                initConfigData(contactAddEntity);
                //非编辑并且订单id为空显示关联订单
                if (!isEdit && orderId == null) {
                    //显示订单关联
                    TaskManager.newInstance().runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            showAssociateOrderDialog();
                        }
                    }, 500);
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


    private void initConfigData(ContactAddEntity contactAddEntity) {
        //合同信息
        if (contactAddEntity.getContract() != null) {
            contarct_add_data_kvel.setData(contactAddEntity.getContract());
            contarct_add_data_kvel.setData(contactAddEntity.getContract());
            if (date!=null){
                KeyValueEntity keyValueEntity = contarct_add_data_kvel.findEntityByParamKey("weddingDate");
                if (keyValueEntity != null) {
                    keyValueEntity.setVal(date);
                    contarct_add_data_kvel.refreshItem(keyValueEntity);
                }

            }


            if (startType!=-1 && endType !=-1 && startDate!=null && endDate!=null&&isMulti == 1){
                    KeyValueEntity keyValueEntity1 = contarct_add_data_kvel.findEntityByParamKey("dq_beginTime");
                    KeyValueEntity keyValueEntity2 = contarct_add_data_kvel.findEntityByParamKey("dq_endTime");
                    KeyValueEntity keyValueEntity3 = contarct_add_data_kvel.findEntityByParamKey("dq_type");
                    KeyValueEntity keyValueEntity4 = contarct_add_data_kvel.findEntityByParamKey("dq_one_status");
                    KeyValueEntity keyValueEntity5 = contarct_add_data_kvel.findEntityByParamKey("dq_one_time");
                    if (keyValueEntity5 != null) {
                        keyValueEntity5.setShowStatus("0");
                    }
                    if (keyValueEntity4!=null)
                        keyValueEntity4.setShowStatus("0");
                    if (keyValueEntity1!=null){
                        keyValueEntity1.setShowStatus("1");
                        if (startType == 2){
                            keyValueEntity1.setVal(startDate+" 晚宴");

                                keyValueEntity1.setDefaultVal(startDate+",2");

                        }else if (startType == 1){
                            keyValueEntity1.setVal(startDate+" 午宴");

                                keyValueEntity1.setDefaultVal(startDate+",1");

                        }
                    }

                    if (keyValueEntity2!=null){
                        keyValueEntity2.setShowStatus("1");
                        if (endType == 2){
                            keyValueEntity2.setVal(endDate+" 晚宴");
                            keyValueEntity2.setDefaultVal(endDate+",2");

                        }else if (endType == 1){
                            keyValueEntity2.setVal(endDate+" 午宴");
                            keyValueEntity2.setDefaultVal(endDate+",1");

                        }
                    }

                    if (keyValueEntity3!=null){
                        keyValueEntity3.setVal("多档期");
                        keyValueEntity3.setDefaultVal("2");
                    }
                    contarct_add_data_kvel.refresh();
                }else if (date!=null && sType != -1 && isMulti == 0){
                    KeyValueEntity keyValueEntity1 = contarct_add_data_kvel.findEntityByParamKey("dq_one_status");
                    KeyValueEntity keyValueEntity2 = contarct_add_data_kvel.findEntityByParamKey("dq_one_time");
                    KeyValueEntity keyValueEntity3 = contarct_add_data_kvel.findEntityByParamKey("dq_type");
                    KeyValueEntity keyValueEntity4 = contarct_add_data_kvel.findEntityByParamKey("dq_beginTime");
                    KeyValueEntity keyValueEntity5 = contarct_add_data_kvel.findEntityByParamKey("dq_endTime");

                    if (keyValueEntity5 != null) {
                        keyValueEntity5.setShowStatus("0");
                    }
                    if (keyValueEntity4 != null) {
                        keyValueEntity4.setShowStatus("0");
                    }

                    if (keyValueEntity1!=null){
                        keyValueEntity1.setShowStatus("1");
                        if (sType == 2){
                            keyValueEntity1.setVal("晚宴");
                            keyValueEntity1.setDefaultVal("2");
                        }else if (sType == 1){
                            keyValueEntity1.setVal("午宴");
                            keyValueEntity1.setDefaultVal("1");
                        }
                    }
                    if (keyValueEntity2!=null){
                        keyValueEntity2.setShowStatus("1");
                        keyValueEntity2.setVal(date);
                        contarct_add_data_kvel.refreshItem(keyValueEntity2);
                    }
                    if (keyValueEntity3 != null) {
                        keyValueEntity3.setVal("单档期");
                        keyValueEntity3.setDefaultVal("1");
                    }

                    contarct_add_data_kvel.refresh();



                }else if (isMulti == -1){
                KeyValueEntity keyValueEntity1 = contarct_add_data_kvel.findEntityByParamKey("dq_one_status");
                KeyValueEntity keyValueEntity2 = contarct_add_data_kvel.findEntityByParamKey("dq_one_time");
                KeyValueEntity keyValueEntity4 = contarct_add_data_kvel.findEntityByParamKey("dq_beginTime");
                KeyValueEntity keyValueEntity5 = contarct_add_data_kvel.findEntityByParamKey("dq_endTime");
                if (keyValueEntity1!=null){
                    keyValueEntity1.setShowStatus("0");
                }
                if (keyValueEntity2!=null){
                    keyValueEntity2.setShowStatus("0");
                }
                if (keyValueEntity4!=null){
                    keyValueEntity4.setShowStatus("0");
                }
                if (keyValueEntity5!=null){
                    keyValueEntity5.setShowStatus("0");
                }
                contarct_add_data_kvel.refresh();

            }

            contarct_add_data_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
                @Override
                public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                    KeyValueEntity findEntity = contarct_add_data_kvel.findEntityByParamKey("dq_type");
                    if (findEntity == null) {
                        return;
                    }
                    if ("1".equals(findEntity.getDefaultVal())){
                        contarct_add_data_kvel.findEntityByParamKey("dq_one_status").setShowStatus("1");
                        contarct_add_data_kvel.findEntityByParamKey("dq_one_time").setShowStatus("1");
                        contarct_add_data_kvel.findEntityByParamKey("dq_beginTime").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_endTime").setShowStatus("0");
                        contarct_add_data_kvel.refresh();

                    }else if ("2".equals(findEntity.getDefaultVal())){

                        contarct_add_data_kvel.findEntityByParamKey("dq_one_status").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_one_time").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_beginTime").setShowStatus("1");
                        contarct_add_data_kvel.findEntityByParamKey("dq_endTime").setShowStatus("1");
                        contarct_add_data_kvel.refresh();
                    }else {
                        contarct_add_data_kvel.findEntityByParamKey("dq_one_status").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_one_time").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_beginTime").setShowStatus("0");
                        contarct_add_data_kvel.findEntityByParamKey("dq_endTime").setShowStatus("0");
                        contarct_add_data_kvel.refresh();
                    }

                }
            });

        } else {
            contarct_add_data_title_tv.setVisibility(View.GONE);
            contarct_add_data_ll.setVisibility(View.GONE);
        }
        

        //客户信息
        if (contactAddEntity.getCustomer_info() != null) {
            contarct_add_customer_kvel.setData(contactAddEntity.getCustomer_info());
        } else {
            contarct_add_customer_title_tv.setVisibility(View.GONE);
            contarct_add_customer_ll.setVisibility(View.GONE);
        }

        //回款计划
        if (contactAddEntity.getReceivables()!=null){
            List<KeyValueEntity> data = contactAddEntity.getReceivables();
            hasValue = false;
            KeyValueEntity entity = null;
            for (KeyValueEntity datum : data) {
                if (datum.getEvent().getParamKey().equals("planType"))
                    entity = datum;
                if (!TextUtils.isEmpty(datum.getDefaultVal())){
                    hasValue = true;
                }
            }
            planData = contactAddEntity.getReceivables();
            if (hasValue)
                contarct_add_plan_kvel.setData(planData);
            else {
                List<KeyValueEntity> temp  = new ArrayList<>();
                temp.add(entity);
                contarct_add_plan_kvel.setData(temp);
            }
        }else {
            contarct_add_plan_title_tv.setVisibility(View.GONE);
            contarct_add_plan_ll.setVisibility(View.GONE);
        }


        //需求信息
        if (contactAddEntity.getReq_info() != null) {
            contarct_add_important_kvel.setData(contactAddEntity.getReq_info());

            if (sType!=-1){
                KeyValueEntity keyValueEntity = contarct_add_important_kvel.findEntityByParamKey("schedule");
                if (keyValueEntity!=null){
                    if (sType == 2){
                        keyValueEntity.setVal("晚宴");
                        keyValueEntity.setDefaultVal("2");
                    }else if (sType == 1){
                        keyValueEntity.setVal("午宴");
                        keyValueEntity.setDefaultVal("1");
                    }
                    contarct_add_important_kvel.refreshItem(keyValueEntity);
                }

            }
            if (hallId!=null && hallName !=null){
                KeyValueEntity keyValueEntity = contarct_add_important_kvel.findEntityByParamKey("hotelHall");
                if (keyValueEntity!=null){
                    keyValueEntity.setVal(hallName);
                    keyValueEntity.setDefaultVal(hallId);
                    contarct_add_important_kvel.refreshItem(keyValueEntity);
                }

            }
        } else {
            contarct_add_important_title_tv.setVisibility(View.GONE);
            contarct_add_important_ll.setVisibility(View.GONE);
        }
        //附件
        if (contactAddEntity.getContract_pic() != null && contactAddEntity.getContract_pic().size() > 0) {
            List<AttachmentEntity> attachments = new ArrayList<>();
            for (String url : contactAddEntity.getContract_pic()) {
                if (!TextUtils.isEmpty(url)) {
                    attachments.add(AttachmentEntity.createRemote(url));
                }
            }
            attachmentAddAdapter.add(attachments);
        }
    }

    /**
     * 显示订单关联dialog
     */
    private void showAssociateOrderDialog() {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(context, R.layout.dialog_associate_order, CommonUtils.getScreenWidth(context) * 325 / 375, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void dialogCreate(final Dialog dialog, View view) {
                ImageView associate_order_close_iv = view.findViewById(R.id.associate_order_close_iv);
                TextView associate_order_cancel_tv = view.findViewById(R.id.associate_order_cancel_tv);
                TextView associate_order_ok_tv = view.findViewById(R.id.associate_order_ok_tv);
                associate_order_close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
                associate_order_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
                associate_order_ok_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReceiveOrderActivity.start(ContractNewActivity.this, ReceiveOrderActivity.ORDER_TYPE_ASSOCIATE,isForSchedule, REQUEST_CODE_ASSOCIATE_ORDER);
                    }
                });
            }
        }.setCancelable(false);
        associateOrderDialog = builder.show();
    }

    public static class AttachmentAddAdapter extends RecyclerView.Adapter<AttachmentAddAdapter.Holder> {
        public static final int ITEM_NORMAL_TYPE = 0;
        public static final int ITEM_ADD_TYPE = 1;

        private Context context;
        private List<AttachmentEntity> attachments;
        private LayoutInflater inflater;

        private OnItemClickListener listener;

        public AttachmentAddAdapter(Context context) {
            this.context = context;
            this.attachments = new ArrayList<>();
            this.inflater = LayoutInflater.from(context);
        }

        public void add(AttachmentEntity attachment) {
            this.attachments.add(attachment);
            notifyDataSetChanged();
        }

        public void add(List<AttachmentEntity> attachments) {
            if (attachments != null) {
                this.attachments.addAll(attachments);
                notifyDataSetChanged();
            }
        }

        public void remove(int position) {
            this.attachments.remove(position);
            notifyDataSetChanged();
        }

        public int getDataSize() {
            return attachments.size();
        }

        public List<AttachmentEntity> getData() {
            return attachments;
        }

        public void setOnItemClickListener(AttachmentAddAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public AttachmentAddAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_ADD_TYPE) {
                return new Holder(inflater.inflate(R.layout.activity_follow_detail_attachment_add_item, parent, false), viewType);
            }
            return new Holder(inflater.inflate(R.layout.activity_follow_detail_attachment_edit_item, parent, false), viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1 && attachments.size() < MAX_ATTACHMENT_NUM) {
                return ITEM_ADD_TYPE;
            }
            return ITEM_NORMAL_TYPE;
        }

        @Override
        public void onBindViewHolder(@NonNull AttachmentAddAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {
            final int viewType = getItemViewType(position);
            if (viewType == ITEM_NORMAL_TYPE) {
                AttachmentEntity attachment = attachments.get(position);
                GlideApp.with(context).load(attachment.getUrl()).transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.follow_detail_attachment_item_iv);
                holder.follow_detail_attachment_item_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemClick(attachments, position);
                    }
                });
                holder.follow_detail_attachment_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemDelete(position);
                    }
                });
            } else if (viewType == ITEM_ADD_TYPE) {
                holder.follow_detail_attachment_item_add_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemAdd();
                    }
                });
            }
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView follow_detail_attachment_item_add_iv;

            private ImageView follow_detail_attachment_item_iv;
            private ImageView follow_detail_attachment_item_delete_iv;

            public Holder(@NonNull View itemView, int type) {
                super(itemView);
                if (type == ITEM_ADD_TYPE) {
                    follow_detail_attachment_item_add_iv = itemView.findViewById(R.id.follow_detail_attachment_item_add_iv);
                } else {
                    follow_detail_attachment_item_iv = itemView.findViewById(R.id.follow_detail_attachment_item_iv);
                    follow_detail_attachment_item_delete_iv = itemView.findViewById(R.id.follow_detail_attachment_item_delete_iv);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (attachments.size() < MAX_ATTACHMENT_NUM) {
                return attachments.size() + 1;
            }
            return attachments.size();
        }

        public interface OnItemClickListener {
            void onItemClick(List<AttachmentEntity> pics, int postion);

            void onItemDelete(int postion);

            void onItemAdd();
        }
    }

    public static class ProductDiscountAdapter extends RecyclerView.Adapter<ProductDiscountAdapter.Holder> {
        private static final int NORMAL_TYPE = 1;
        private static final int ADD_TYPE = 2;

        private static final int PRODUCT_ADAPTER_TYPE = 1;
        private static final int DISCOUNT_ADAPTER_TYPE = 2;

        private List<List<KeyValueEntity>> list;
        private LayoutInflater inflater;
        private int type;
        private OnItemClickListener listener;

        private List<ProductEntity> products;

        public ProductDiscountAdapter(Context context, int type) {
            this.inflater = LayoutInflater.from(context);
            this.type = type;
            this.list = new ArrayList<>();
            this.products = new ArrayList<>();
        }

        private void setProducts(List<ProductEntity> products) {
            this.products.clear();
            this.list.clear();
            if (products != null) {
                this.products.addAll(products);
                for (ProductEntity productEntity : products) {
                    List<KeyValueEntity> item = new ArrayList<>();
                    KeyValueEntity keyValueEntity = new KeyValueEntity();
                    keyValueEntity.setKey("产品名称");
                    keyValueEntity.setVal(productEntity.getName());
                    item.add(keyValueEntity);
                    keyValueEntity = new KeyValueEntity();
                    keyValueEntity.setKey("产品类型");
                    keyValueEntity.setVal(productEntity.getCategory());
                    item.add(keyValueEntity);
                    keyValueEntity = new KeyValueEntity();
                    keyValueEntity.setKey("产品价格");
                    keyValueEntity.setVal(productEntity.getPrice() + "元");
                    item.add(keyValueEntity);
                    this.list.add(item);
                }
            }
            notifyDataSetChanged();
        }

        private void setData(List<List<KeyValueEntity>> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }

        public List<ProductEntity> getProducts() {
            return products;
        }

        private List<List<KeyValueEntity>> getData() {
            return list;
        }

        @Override
        public int getItemViewType(int position) {
            if (list.size() == 0) {
                return ADD_TYPE;
            }
            return NORMAL_TYPE;
        }

        @NonNull
        @Override
        public ProductDiscountAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == ADD_TYPE) {
                view = inflater.inflate(R.layout.activity_contract_add_item_product_add, parent, false);
            } else {
                view = inflater.inflate(R.layout.activity_contract_add_item_product, parent, false);
            }
            return new ProductDiscountAdapter.Holder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductDiscountAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {
            int viewType = getItemViewType(position);
            if (viewType == ADD_TYPE) {
                if (type == PRODUCT_ADAPTER_TYPE) {
                    holder.contract_add_item_add_tv.setText("添加产品信息");
                } else {
                    holder.contract_add_item_add_tv.setText("添加优惠");
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemAdd();
                        }
                    }
                });
            } else {
                List<KeyValueEntity> data = list.get(position);
                holder.contract_add_item_content_kvel.setData(data);
                holder.contract_add_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == PRODUCT_ADAPTER_TYPE) {
                            products.remove(position);
                        }
                        list.remove(position);
                        if (listener != null) {
                            listener.OnItemDelete();
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size() == 0 ? 1 : list.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView contract_add_item_add_tv;
            public KeyValueEditLayout contract_add_item_content_kvel;
            public ImageView contract_add_item_delete_iv;


            public Holder(@NonNull View itemView, int viewType) {
                super(itemView);
                if (viewType == ADD_TYPE) {
                    contract_add_item_add_tv = itemView.findViewById(R.id.contract_add_item_add_tv);
                } else {
                    contract_add_item_content_kvel = itemView.findViewById(R.id.contract_add_item_content_kvel);
                    contract_add_item_delete_iv = itemView.findViewById(R.id.contract_add_item_delete_iv);
                }
            }
        }

        public void setOnItemListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public interface OnItemClickListener {
            void onItemAdd();

            void OnItemDelete();
        }
    }
}
