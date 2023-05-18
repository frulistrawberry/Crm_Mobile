package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ConstactAuthHistoryAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.AuthHistoryEntity;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.ConstactDetailEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.glide.transformation.RoundedCornersTransformation;
import com.baihe.lihepro.manager.HomeRefreshManager;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-02
 * Description：
 */
public class ContractDetailActivity extends BaseActivity {
    public static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    public static final String INTENT_CONTRACT_FROM = "INTENT_CONTRACT_FROM";
    public static final String INTENT_APPROVE_ID = "INTENT_APPROVE_ID";
    public static final String INTENT_CONTRACT_TYPE = "INTENT_CONTRACT_TYPE";

    public static final int REQUEST_CODE_EDIT_CONTRACT = 100;

    public static final int CONTRACT_TYPE = 1;
    public static final int APPROVE_TYPE = 2;

    public static void start(Activity activity, String contractId, int requestCode) {
        Intent intent = new Intent(activity, ContractDetailActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_CONTRACT_TYPE, CONTRACT_TYPE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, String contractId, String contractFrom, int requestCode) {
        Intent intent = new Intent(activity, ContractDetailActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_CONTRACT_FROM, contractFrom);
        intent.putExtra(INTENT_CONTRACT_TYPE, CONTRACT_TYPE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForApprove(Activity activity, String aId, int requestCode) {
        Intent intent = new Intent(activity, ContractDetailActivity.class);
        intent.putExtra(INTENT_APPROVE_ID, aId);
        intent.putExtra(INTENT_CONTRACT_TYPE, APPROVE_TYPE);
        activity.startActivityForResult(intent, requestCode);
    }

    private String contractId;
    private String approveId;
    private ConstactDetailEntity constactDetailEntity;
    private int pageType;

    private TextView contarct_detail_name_tv;
    private FlowLayout contarct_detail_label_fl;
    private TextView contarct_detail_wedding_time_tv;
    private KeyValueLayout contarct_detail_data_kvl;
    private TextView contarct_detail_customer_title_tv;
    private LinearLayout contarct_detail_customer_ll;
    private KeyValueLayout contarct_detail_customer_kvl;

    private TextView contarct_detail_plan_title_tv;
    private LinearLayout contarct_detail_plan_ll;
    private KeyValueLayout contarct_detail_plan_kvl;

    private TextView contarct_detail_important_title_tv;
    private LinearLayout contarct_detail_important_ll;
    private KeyValueLayout contarct_detail_important_kvl;
    private TextView contarct_detail_history_title_tv;
    private RecyclerView contarct_detail_history_rv;
    private RecyclerView contarct_detail_attachment_rv;
    private LinearLayout contract_detail_bottom_ll;
    private TextView contract_detail_botton1_tv;
    private TextView contract_detail_botton2_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        approveId = getIntent().getStringExtra(INTENT_APPROVE_ID);
        pageType = getIntent().getIntExtra(INTENT_CONTRACT_TYPE, CONTRACT_TYPE);
        setTitleText("合同");
        setContentView(R.layout.activity_contract_detail);
        init();
        listener();
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_CONTRACT: {
                    loadData();
                    setResult(RESULT_OK);
                }
                break;
            }
        }
    }

    private void init() {
        contarct_detail_name_tv = findViewById(R.id.contarct_detail_name_tv);
        contarct_detail_label_fl = findViewById(R.id.contarct_detail_label_fl);
        contarct_detail_wedding_time_tv = findViewById(R.id.contarct_detail_wedding_time_tv);
        contarct_detail_data_kvl = findViewById(R.id.contarct_detail_data_kvl);
        contarct_detail_customer_title_tv = findViewById(R.id.contarct_detail_customer_title_tv);
        contarct_detail_customer_ll = findViewById(R.id.contarct_detail_customer_ll);
        contarct_detail_customer_kvl = findViewById(R.id.contarct_detail_customer_kvl);
        contarct_detail_plan_title_tv = findViewById(R.id.contarct_detail_plan_title_tv);
        contarct_detail_plan_ll = findViewById(R.id.contarct_detail_plan_ll);
        contarct_detail_plan_kvl = findViewById(R.id.contarct_detail_plan_kvl);
        contarct_detail_important_title_tv = findViewById(R.id.contarct_detail_important_title_tv);
        contarct_detail_important_ll = findViewById(R.id.contarct_detail_important_ll);
        contarct_detail_important_kvl = findViewById(R.id.contarct_detail_important_kvl);
        contarct_detail_history_title_tv = findViewById(R.id.contarct_detail_history_title_tv);
        contarct_detail_history_rv = findViewById(R.id.contarct_detail_history_rv);
        contarct_detail_attachment_rv = findViewById(R.id.contarct_detail_attachment_rv);
        contract_detail_bottom_ll = findViewById(R.id.contract_detail_bottom_ll);
        contract_detail_botton1_tv = findViewById(R.id.contract_detail_botton1_tv);
        contract_detail_botton2_tv = findViewById(R.id.contract_detail_botton2_tv);
    }

    private void initData(final ConstactDetailEntity entity) {
        constactDetailEntity = entity;
        TextView title_text_tv = findViewById(R.id.title_text_tv);
        title_text_tv.setText(entity.getCustomer_name() + "的合同");
        contarct_detail_name_tv.setText(entity.getCustomer_name());
        contarct_detail_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(entity.getAudit_status_text()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return entity.getAudit_status_text();
            }

            @Override
            public int getLabelTextColor(int position) {
                String color = entity.getAudit_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        return Color.parseColor(color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return super.getLabelTextColor(position);
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_stroke_drawable);
                String color = entity.getAudit_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        labelDrawable.setStroke(CommonUtils.dp2pxForInt(context, 0.5F), Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return labelDrawable;
            }
        });
        contarct_detail_wedding_time_tv.setText(entity.getWedding_date().getKey() + "：" + (TextUtils.isEmpty(entity.getWedding_date().getVal()) ? "未填写" : entity.getWedding_date().getVal()));
        contarct_detail_data_kvl.setData(entity.getShow_array());
        //附件信息
        setAttachment(entity.getContract_pic());

        //客户信息
        List<KeyValueEntity> customer = entity.getCustomer_info();
        if (customer != null && customer.size() > 0) {
            contarct_detail_customer_kvl.setData(customer);
        } else {
            contarct_detail_customer_title_tv.setVisibility(View.GONE);
            contarct_detail_customer_ll.setVisibility(View.GONE);
        }

        //回款计划
        List<KeyValueEntity> plan = entity.getPlan();
        if (plan != null && plan.size() > 0) {
            contarct_detail_plan_kvl.setData(plan);
        } else {
            contarct_detail_plan_title_tv.setVisibility(View.GONE);
            contarct_detail_plan_ll.setVisibility(View.GONE);
        }
        //客户需求
        List<KeyValueEntity> important = entity.getImportant_data();
        if (important != null && important.size() > 0) {
            contarct_detail_important_kvl.setData(important);
        } else {
            contarct_detail_important_title_tv.setVisibility(View.GONE);
            contarct_detail_important_ll.setVisibility(View.GONE);
        }
        //审批历史
        List<AuthHistoryEntity> history = entity.getAuth_history();
        if (history != null && history.size() > 0) {
            setHistory(history);
        } else {
            contarct_detail_history_title_tv.setVisibility(View.GONE);
            contarct_detail_history_rv.setVisibility(View.GONE);
        }
        //底部按钮
        List<ButtonTypeEntity> buttonTypeEntities = entity.getButton_type();
        Utils.bottomButtons(contract_detail_bottom_ll, contract_detail_botton1_tv, contract_detail_botton2_tv, buttonTypeEntities, new Utils.BottomCallback() {
            @Override
            public void click(int type) {
                bottomButtonClick(type);
            }
        });
    }

    public void bottomButtonClick(int type) {
        if (type == 2) {  //驳回申请-合同详情页-审批
            if (pageType == APPROVE_TYPE) {
                showRejectDialog();
            }
        } else if (type == 3) { //审批通过-合同详情页-审批
            if (pageType == APPROVE_TYPE) {
                showApprovedDialog();
            }
        } else if (type == 102) { //提交审批-合同详情页-合同详情
            if (pageType == CONTRACT_TYPE) {
                submitApprove();
            }
        } else if (type == 103) { //合同重新编辑-合同详情页-合同详情
            if (pageType == CONTRACT_TYPE) {
                ContractNewActivity.startEidt(this, contractId, REQUEST_CODE_EDIT_CONTRACT);
            }
        }
    }

    private void setApproveSale(LinearLayout approve_price_ll, TextView approve_price_tv, LinearLayout approve_sale_ll, TextView approve_sale_tv, String preferentialStr, double sign) {
        if (TextUtils.isEmpty(preferentialStr)) {
            approve_price_ll.setVisibility(View.GONE);
            approve_sale_ll.setVisibility(View.GONE);
        } else {
            double preferential = Double.parseDouble(preferentialStr);
            if (preferential <= 0 || preferential > sign) {
                approve_price_ll.setVisibility(View.GONE);
                approve_sale_ll.setVisibility(View.GONE);
            } else {
                approve_price_ll.setVisibility(View.VISIBLE);
                approve_sale_ll.setVisibility(View.VISIBLE);
                approve_price_tv.setText(Utils.formatFloat(sign - preferential, 2) + "元");
                double saleValue = (sign - preferential) / sign * 10d;
                if (saleValue < 0d) {
                    saleValue = 0d;
                }
                approve_sale_tv.setText(Utils.formatFloat(saleValue, 1) + "折");
            }
        }

    }

    private void showApprovedDialog() {
        if (constactDetailEntity == null) {
            return;
        }
        double signAmount = 0;
        try {
            signAmount = Double.parseDouble(constactDetailEntity.getSign_amount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dialogWidth = CommonUtils.getScreenWidth(context) * 325 / 375;
        final double finalSignAmount = signAmount;
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_approve, dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                final EditText approve_price_input_et = view.findViewById(R.id.approve_price_input_et);
                final LinearLayout approve_price_ll = view.findViewById(R.id.approve_price_ll);
                final TextView approve_price_tv = view.findViewById(R.id.approve_price_tv);
                final LinearLayout approve_sale_ll = view.findViewById(R.id.approve_sale_ll);
                final TextView approve_sale_tv = view.findViewById(R.id.approve_sale_tv);
                final EditText approve_ok_input_et = view.findViewById(R.id.approve_ok_input_et);
                TextView approve_cancel_tv = view.findViewById(R.id.approve_cancel_tv);
                TextView approve_ok_tv = view.findViewById(R.id.approve_ok_tv);

                if (finalSignAmount <= 0) {
                    approve_price_input_et.setEnabled(false);
                    approve_price_ll.setVisibility(View.GONE);
                    approve_price_ll.setVisibility(View.GONE);
                } else {
                    try {
                        double preferentialAmount = Double.parseDouble(constactDetailEntity.getPreferential_amount());
                        if (preferentialAmount > 0) {
                            approve_price_input_et.setText(preferentialAmount + "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setApproveSale(approve_price_ll, approve_price_tv, approve_sale_ll, approve_sale_tv, approve_price_input_et.getText().toString().trim(), finalSignAmount);
                }
                approve_price_input_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setApproveSale(approve_price_ll, approve_price_tv, approve_sale_ll, approve_sale_tv, s.toString().trim(), finalSignAmount);
                    }
                });
                approve_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                approve_ok_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String preferentialStr = approve_price_input_et.getText().toString().trim();
                        if (TextUtils.isEmpty(preferentialStr)) {
                            dialog.dismiss();
                            approved(null, approve_ok_input_et.getText().toString().trim());
                        } else {
                            try {
                                float preferential = Float.parseFloat(preferentialStr);
                                if (preferential > finalSignAmount) {
                                    ToastUtils.toast("优惠金额不得大于合同金额");
                                    return;
                                } else if (preferential <= 0) {
                                    ToastUtils.toast("优惠金额不得输入0");
                                    return;
                                }
                                dialog.dismiss();
                                approved(preferentialStr, approve_ok_input_et.getText().toString().trim());
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.toast("数据异常");
                            }
                        }
                    }
                });
            }

            @Override
            public BaseDialog createDialog(Context context, int themeResId) {
                return new BaseDialog(context, R.style.CommonDialog);
            }
        }.setCancelable(true);
        dialogBuilder.create().show();
    }

    private void showRejectDialog() {
        int dialogWidth = CommonUtils.getScreenWidth(context) * 325 / 375;
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_contract_reject, dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                final EditText reject_input_et = view.findViewById(R.id.reject_input_et);
                TextView reject_cancel_tv = view.findViewById(R.id.reject_cancel_tv);
                TextView reject_ok_tv = view.findViewById(R.id.reject_ok_tv);
                reject_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                reject_ok_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = reject_input_et.getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            ToastUtils.toast("请输入驳回理由");
                            return;
                        }
                        reject(content);
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public BaseDialog createDialog(Context context, int themeResId) {
                return new BaseDialog(context, R.style.CommonDialog);
            }
        }.setCancelable(true);
        dialogBuilder.create().show();
    }

    private void reject(String content) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("aId", approveId).putParamValue("status", 2).putParamValue("remark", content);
        HttpRequest.create(UrlConstant.DO_AUDIT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("驳回成功");
                setResult(RESULT_OK);
                HomeRefreshManager.newInstance().refresh();
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

    private void approved(String preferentialAmount, String remark) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("aId", approveId).putParamValue("status", 3).putParamValue("preferentialAmount", preferentialAmount).putParamValue("remark", remark);
        HttpRequest.create(UrlConstant.DO_AUDIT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("审批通过成功");
                setResult(RESULT_OK);
                HomeRefreshManager.newInstance().refresh();
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

    private void submitApprove() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("contractId", contractId);
        HttpRequest.create(UrlConstant.SUBMIT_AUDIT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("提交审核成功");
                setResult(RESULT_OK);
                HomeRefreshManager.newInstance().refresh();
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

    /**
     * 设置附件信息
     *
     * @param attachments
     */
    private void setAttachment(List<String> attachments) {
        final AttachmentAdapter adapter = new AttachmentAdapter(context, attachments);
        contarct_detail_attachment_rv.setAdapter(adapter);
        contarct_detail_attachment_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        final int offset = CommonUtils.dp2pxForInt(context, 15);
        contarct_detail_attachment_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == adapter.getItemCount() - 1) {
                    outRect.set(offset, 0, offset, 0);
                } else {
                    outRect.set(offset, 0, 0, 0);
                }
            }
        });
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<String> pics, int position) {
                PhotoBrowserActivity.start(context, (ArrayList<String>) pics, position);
            }
        });
    }




    /**
     * 设置审批历史
     *
     * @param history
     */
    private void setHistory(List<AuthHistoryEntity> history) {
        final ConstactAuthHistoryAdapter adapter = new ConstactAuthHistoryAdapter(context, history);
        contarct_detail_history_rv.setAdapter(adapter);
        contarct_detail_history_rv.setLayoutManager(new LinearLayoutManager(context));
        final Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ECECF0"));
        final float offsetFor9 = CommonUtils.dp2px(context, 16f);
        final float padding = CommonUtils.dp2px(context, 20f);
        final float iconffset = CommonUtils.dp2px(context, 2f);
        final float pointWidth = CommonUtils.dp2px(context, 11f);
        final float lineWidth = CommonUtils.dp2px(context, 0.5f);
        final float lineMagin = CommonUtils.dp2px(context, 34f) + (pointWidth - lineWidth) / 2;
        contarct_detail_history_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == adapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -3));
                }
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                int childSize = parent.getChildCount();
                if (childSize <= 1) {
                    return;
                }
                RectF rectF = new RectF();
                for (int i = 0; i < childSize; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (position >= adapter.getItemCount() - 1) {
                        continue;
                    }
                    ImageView icon = child.findViewById(R.id.contract_auth_history_item_icon_iv);
                    if (icon == null) {
                        continue;
                    }
                    float top = child.getTop() + icon.getBottom() - (adapter.isApprove(position) ? iconffset : 0);
                    if (i == 0) {
                        top += padding + offsetFor9;
                    }
                    View nextChild = parent.getChildAt(i + 1);
                    float bottom = nextChild.getTop() + icon.getTop() + (adapter.isApprove(position + 1) ? iconffset : 0);
                    rectF.set(lineMagin + child.getLeft(), top, lineMagin + child.getLeft() + lineWidth, bottom);
                    c.drawRect(rectF, paint);
                }
            }
        });
        adapter.setLookListener(new ConstactAuthHistoryAdapter.LookListener() {
            @Override
            public void look(boolean isPass, String remark, String time, String user, String price) {
                showLookDialog(isPass, remark, time, user, price);
            }
        });
    }

    private void showLookDialog(final boolean isPass, final String remark, final String time, final String userName, final String price) {
        int dialogWidth = CommonUtils.getScreenWidth(context) * 325 / 375;
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_contract_approve, dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                final TextView contract_approve_title_tv = view.findViewById(R.id.contract_approve_title_tv);
                final TextView contract_approve_content_tv = view.findViewById(R.id.contract_approve_content_tv);
                final LinearLayout contract_approve_price_ll = view.findViewById(R.id.contract_approve_price_ll);
                final TextView contract_approve_price_tv = view.findViewById(R.id.contract_approve_price_tv);
                final TextView contract_approve_user_tv = view.findViewById(R.id.contract_approve_user_tv);
                final TextView contract_approve_time_tv = view.findViewById(R.id.contract_approve_time_tv);
                TextView contract_approve_close_tv = view.findViewById(R.id.contract_approve_close_tv);

                if (isPass) {
                    contract_approve_title_tv.setText("审批意见");
                    contract_approve_price_ll.setVisibility(View.VISIBLE);
                    boolean isZero = false;
                    try {
                        isZero = Double.parseDouble(price) == 0.0D;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contract_approve_price_tv.setText((TextUtils.isEmpty(price) || isZero) ? "无" : price);
                } else {
                    contract_approve_title_tv.setText("驳回原因");
                    contract_approve_price_ll.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(remark)) {
                    contract_approve_content_tv.setVisibility(View.GONE);
                } else {
                    contract_approve_content_tv.setText(remark);
                }
                contract_approve_user_tv.setText(TextUtils.isEmpty(userName) ? "无" : userName);
                contract_approve_time_tv.setText(TextUtils.isEmpty(time) ? "无" : time);
                contract_approve_close_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public BaseDialog createDialog(Context context, int themeResId) {
                return new BaseDialog(context, R.style.CommonDialog);
            }
        }.setCancelable(true);
        dialogBuilder.create().show();
    }

    private void listener() {
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
        contarct_detail_data_kvl.setOnOrderListener(new KeyValueLayout.OnOrderListener() {
            @Override
            public void go(KeyValueEntity keyValueEntity) {
                SalesDetailActivity.start(context, constactDetailEntity.getOrder_id(), constactDetailEntity.getCustomer_id(), constactDetailEntity.getCustomer_name(), true);
            }
        });
    }

    private void loadData() {
        String url;
        JsonParam jsonParam = JsonParam.newInstance("params");
        if (pageType == CONTRACT_TYPE) {
            url = UrlConstant.CONTRACT_DETAIL_URL;
            String fromType = getIntent().getStringExtra(INTENT_CONTRACT_FROM);
            jsonParam.putParamValue("contractId", contractId).putParamValue("type", fromType);
        } else {
            url = UrlConstant.AUDIT_DETAIL_URL;
            jsonParam.putParamValue("aId", approveId);
        }
        HttpRequest.create(url).putParam(jsonParam).get(new CallBack<ConstactDetailEntity>() {
            @Override
            public ConstactDetailEntity doInBackground(String response) {
                if ("[]".equals(response)) {
                    return new ConstactDetailEntity();
                } else {
                    return JsonUtils.parse(response, ConstactDetailEntity.class);
                }
            }

            @Override
            public void success(ConstactDetailEntity entity) {
                if (TextUtils.isEmpty(entity.getCustomer_name())) {
                    statusLayout.expandStatus();
                } else {
                    statusLayout.normalStatus();
                    initData(entity);
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

    public static class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.Holder> {
        public static final int ITEM_NORMAL_TYPE = 0;
        public static final int ITEM_NO_TYPE = 1;

        private Context context;
        private List<String> attachments;
        private LayoutInflater inflater;

        private AttachmentAdapter.OnItemClickListener listener;

        public AttachmentAdapter(Context context, List<String> attachments) {
            this.context = context;
            this.attachments = new ArrayList<>();
            this.inflater = LayoutInflater.from(context);
            if (attachments != null) {
                this.attachments.addAll(attachments);
            }
        }

        public void setOnItemClickListener(AttachmentAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public AttachmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_NORMAL_TYPE) {
                return new AttachmentAdapter.Holder(inflater.inflate(R.layout.activity_contract_detail_attachment_item, parent, false), viewType);
            }
            return new AttachmentAdapter.Holder(inflater.inflate(R.layout.activity_contract_detail_no_attachment_item, parent, false), viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (attachments.size() == 0) {
                return ITEM_NO_TYPE;
            }
            return ITEM_NORMAL_TYPE;
        }

        @Override
        public void onBindViewHolder(@NonNull AttachmentAdapter.Holder holder, final int position) {
            final int viewType = getItemViewType(position);
            if (viewType == ITEM_NORMAL_TYPE) {
                String attachment = attachments.get(position);
                GlideApp.with(context).load(attachment).transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.contract_detail_attachment_item_iv);
                holder.contract_detail_attachment_item_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemClick(attachments, position);
                    }
                });
            } else if (viewType == ITEM_NO_TYPE) {
                GlideApp.with(context).load("").transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.contract_detail_attachment_item_iv);
                holder.contract_detail_attachment_item_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView contract_detail_attachment_item_iv;

            public Holder(@NonNull View itemView, int type) {
                super(itemView);
                contract_detail_attachment_item_iv = itemView.findViewById(R.id.contract_detail_attachment_item_iv);
            }
        }

        @Override
        public int getItemCount() {
            if (attachments.size() == 0) {
                return 1;
            }
            return attachments.size();
        }

        public interface OnItemClickListener {
            void onItemClick(List<String> pics, int position);
        }
    }
}
