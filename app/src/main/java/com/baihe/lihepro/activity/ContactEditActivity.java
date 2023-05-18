package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ContactNewAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.CrmAlertDialog;
import com.baihe.lihepro.entity.ContactListEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-13
 * Description：
 */
public class ContactEditActivity extends BaseActivity {
    private static final String INTENT_CUSTOMER_ID = "INTENT_CUSTOMER_ID";

    public static void start(Activity activity, String customerId, int requestCode) {
        Intent intent = new Intent(activity, ContactEditActivity.class);
        intent.putExtra(INTENT_CUSTOMER_ID, customerId);
        activity.startActivityForResult(intent, requestCode);
    }

    private String customerId;

    private StatusLayout contact_edit_sl;
    private RecyclerView contact_edit_rv;
    private TextView contact_edit_ok_tv;

    private ContactNewAdapter contactNewAdapter;
    private List<KeyValueEntity> newConfig;
    private List<List<KeyValueEntity>> deleteContacts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMER_ID);
        setTitleText("设置联系人");
        setContentView(R.layout.activity_contact_edit);
        init();
        initData();
        listener();
        loadData();
    }

    private void init() {
        contact_edit_sl = findViewById(R.id.contact_edit_sl);
        contact_edit_rv = findViewById(R.id.contact_edit_rv);
        contact_edit_ok_tv = findViewById(R.id.contact_edit_ok_tv);
    }

    private void initData() {
        contactNewAdapter = new ContactNewAdapter(context);
        contact_edit_rv.setLayoutManager(new LinearLayoutManager(context));
        contact_edit_rv.setAdapter(contactNewAdapter);
        contact_edit_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (contactNewAdapter.getItemCount() == 1) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 16), 0, 0);
                } else {
                    if (position == 0) {
                        outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                    } else if (position == contactNewAdapter.getItemCount() - 1) {
                        outRect.set(0, CommonUtils.dp2pxForInt(context, 7), 0, CommonUtils.dp2pxForInt(context, 100));
                    } else {
                        outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                    }
                }
            }
        });
    }

    private void listener() {
        contact_edit_sl.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
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
        contact_edit_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = getCommitData();
                if (data != null) {
                    commitData(data);
                }
            }
        });
        contactNewAdapter.setOnItemListner(new ContactNewAdapter.OnItemListner() {
            @Override
            public void add() {
                List<KeyValueEntity> newData = new ArrayList<>();
                for (KeyValueEntity keyValueEntity : newConfig) {
                    if (keyValueEntity != null) {
                        newData.add(keyValueEntity.copy());
                    }
                }
                contactNewAdapter.addData(newData);
            }

            @Override
            public void delete(final List<KeyValueEntity> data) {
                //是否是本地新建
                boolean isLocalNew = false;
                //是否有name值
                String name = null;
                for (KeyValueEntity keyValueEntity : data) {
                    if ("name".equals(keyValueEntity.getEvent().getParamKey())) {
                        name = keyValueEntity.getVal();
                    }
                    if ("id".equals(keyValueEntity.getEvent().getParamKey())) {
                        String value = keyValueEntity.getDefaultVal();
                        if (TextUtils.isEmpty(value)) {
                            isLocalNew = true;
                        }
                    }
                }
                //云上数据需要记录
                if (!isLocalNew) {
                    deleteContacts.add(data);
                }
                String content;
                if (TextUtils.isEmpty(name)) {
                    content = "您确定要删除吗?";
                } else {
                    content = "您确定将(" + name + ")删除吗?";
                }
                new CrmAlertDialog.Builder(context)
                        .setContent(content)
                        .setConfirmListener(new CrmAlertDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                dialog.dismiss();
                                contactNewAdapter.getDataList().remove(data);
                                contactNewAdapter.notifyDataSetChanged();
                            }
                        }).setCancelListener(new CrmAlertDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
            }
        });
    }

    private Map<String, Object> getCommitData() {
        //提交数据集合
        List<Map<String, Object>> commitList = new ArrayList<>();
        //列表数据，新增和更新
        List<List<KeyValueEntity>> dataList = contactNewAdapter.getDataList();
        for (List<KeyValueEntity> keyValueEntities : dataList) {
            Map<String, Object> contactMap = new HashMap<>();
            for (KeyValueEntity keyValueEntity : keyValueEntities) {
                //显示的item代表可提供的数据
                if ("1".equals(keyValueEntity.getShowStatus())) {
                    //必填却没有填写，中断并toast提示
                    if ("1".equals(keyValueEntity.getOptional()) && TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        ToastUtils.toast(KeyValueEditLayout.getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
                        return null;
                    }
                    contactMap.put(keyValueEntity.getEvent().getParamKey(), keyValueEntity.getDefaultVal());
                } else {
                    //id不显示
                    //如果是id加进来
                    if ("id".equals(keyValueEntity.getEvent().getParamKey()) && !TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        contactMap.put(keyValueEntity.getEvent().getParamKey(), keyValueEntity.getDefaultVal());
                    }
                }
            }
            commitList.add(contactMap);
        }
        //删除数据
        for (List<KeyValueEntity> keyValueEntities : deleteContacts) {
            Map<String, Object> deleteContactMap = new HashMap<>();
            for (KeyValueEntity keyValueEntity : keyValueEntities) {
                //删除只需要id
                if ("id".equals(keyValueEntity.getEvent().getParamKey()) && !TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                    deleteContactMap.put(keyValueEntity.getEvent().getParamKey(), keyValueEntity.getDefaultVal());
                    break;
                }
            }
            //增加删除标记
            deleteContactMap.put("del", "del");
            commitList.add(deleteContactMap);
        }
        if (commitList.size() == 0) {
            ToastUtils.toast("请添加联系人");
            return null;
        }
        Map<String, Object> commitData = new HashMap<>();
        commitData.put("data", commitList);
        commitData.put("customerId", customerId);
        return commitData;
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.CONTACT_PARAMS_URL).putParam(jsonParam).get(new CallBack<ContactListEntity>() {
            @Override
            public ContactListEntity doInBackground(String response) {
                return JsonUtils.parse(response, ContactListEntity.class);
            }

            @Override
            public void success(ContactListEntity entity) {
                statusLayout.normalStatus();
                contactNewAdapter.updateList(entity.getShow_array());
                newConfig = entity.getNew_config();
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

    private void commitData(Map<String, Object> data) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue(data);
        HttpRequest.create(UrlConstant.CREATE_CONTACT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("更新联系人成功");
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

}
