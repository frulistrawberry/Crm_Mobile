package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.dialog.LoadingDialog;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.FontTextView;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ReceiveOrderAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.SimpleDialog;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.entity.SalesListEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.view.KeyValueLayout;
import com.blankj.utilcode.util.KeyboardUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-27
 * Description：
 */
public class ReceiveOrderActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ReceiveOrderActivity.class);
        context.startActivity(intent);
    }

    public static void start(Activity activity, int orderType,boolean isForSchedule, int requestCode) {
        Intent intent = new Intent(activity, ReceiveOrderActivity.class);
        intent.putExtra(INTENT_ORDER_TYPE, orderType);
        intent.putExtra("isForSchedule", isForSchedule);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String INTENT_ORDER_TYPE = "INTENT_ORDER_TYPE";
    public static final String INTENT_ORDER_DATA = "INTENT_ORDER_DATA";
    public static final String INTENT_CONTRACT_TYPE = "INTENT_CONTRACT_TYPE";

    public static final int ORDER_TYPE_RECEIVE = 1;
    public static final int ORDER_TYPE_ASSOCIATE = 2;

    public static final int SEARCH_PHONE_TYPE = 0;
    public static final int SEARCH_GROUPID_TYPE = 1;
    public static final int SEARCH_ORDER_TYPE = 2;

    private Toolbar receive_order_tb;
    private TextView receive_order_tv;
    private LinearLayout receive_order_filter_ll;
    private TextView receive_order_filter_name_tv;
    private ImageView receive_order_filter_arrow_iv;
    private EditText receive_order_filter_input_et;
    private ImageView receive_order_filter_input_delete_iv;
    private TextView receive_order_cancel_tv;

    private SmartRefreshLayout receive_order_srl;
    private RecyclerView receive_order_rv;

    private ReceiveOrderAdapter adapter;
    private int page = 1;

    //默认搜索类型 0手机号 1集团id 2订单号
    private int searchType = SEARCH_PHONE_TYPE;
    private int orderType;
    private String contractType;
    private boolean isForSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_receive_order_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_receive_order, null), params);
        orderType = getIntent().getIntExtra(INTENT_ORDER_TYPE, ORDER_TYPE_RECEIVE);
        isForSchedule = getIntent().getBooleanExtra("isForSchedule",false);
        init();
        initData();
//        loadData();
        listener();
    }

    private void init() {
        receive_order_tb = findViewById(R.id.receive_order_tb);
        receive_order_tv = findViewById(R.id.receive_order_tv);
        receive_order_filter_ll = findViewById(R.id.receive_order_filter_ll);
        receive_order_filter_name_tv = findViewById(R.id.receive_order_filter_name_tv);
        receive_order_filter_arrow_iv = findViewById(R.id.receive_order_filter_arrow_iv);
        receive_order_filter_input_et = findViewById(R.id.receive_order_filter_input_et);
        receive_order_filter_input_delete_iv = findViewById(R.id.receive_order_filter_input_delete_iv);
        receive_order_cancel_tv = findViewById(R.id.receive_order_cancel_tv);
        receive_order_srl = findViewById(R.id.receive_order_srl);
        receive_order_rv = findViewById(R.id.receive_order_rv);
    }

    private void initData() {
        if (orderType == ORDER_TYPE_ASSOCIATE) {
            receive_order_tv.setText("选择订单");
        }

        //关闭下来刷新
        receive_order_srl.setEnableRefresh(false);
        receive_order_filter_input_et.requestFocus();

        adapter = new ReceiveOrderAdapter(context);
        receive_order_rv.setAdapter(adapter);
        receive_order_rv.setLayoutManager(new LinearLayoutManager(context));

        receive_order_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        String keyword = adapter.getKeyword();
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page)).putParamValue("claim", 1);
        if (orderType == ORDER_TYPE_ASSOCIATE) {
            jsonParam.putParamValue("orderType", 1);
        }
        switch (searchType) {
            case SEARCH_PHONE_TYPE:
                jsonParam.putParamValue("customerPhone", keyword);
                break;
            case SEARCH_GROUPID_TYPE:
                jsonParam.putParamValue("customerId", keyword);
                break;
            case SEARCH_ORDER_TYPE:
                jsonParam.putParamValue("orderNum", keyword);
                break;
        }
        HttpRequest.create(UrlConstant.ORDER_DATA_LIST_URL)
                .tag("订单列表")
                .putParam(jsonParam)
                .get(new CallBack<SalesListEntity>() {
                    @Override
                    public SalesListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, SalesListEntity.class);
                    }

                    @Override
                    public void success(SalesListEntity entity) {
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
                            receive_order_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            receive_order_srl.setEnableLoadMore(false);
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
                        if (receive_order_srl.isLoading()) {
                            receive_order_srl.finishLoadMore();
                        }
                        if (receive_order_srl.isRefreshing()) {
                            receive_order_srl.finishRefresh();
                        }
                    }
                });
    }

    private void listener() {
        receive_order_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        receive_order_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        receive_order_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        receive_order_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        receive_order_filter_input_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideSoftInput(receive_order_filter_input_et);

                    String keyword = receive_order_filter_input_et.getText().toString().trim();
                    adapter.updateSearch(keyword, searchType);
                    page = 1;
                    loadData();
                    return true;
                }
                return false;
            }
        });
        receive_order_filter_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    receive_order_filter_input_delete_iv.setVisibility(View.VISIBLE);
                } else {
                    receive_order_filter_input_delete_iv.setVisibility(View.GONE);
                }
            }
        });
        receive_order_filter_input_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive_order_filter_input_et.setText("");
            }
        });
        adapter.setOnItemClickListener(new ReceiveOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListItemEntity listItemEntity) {
                int category = Integer.parseInt(listItemEntity.getCategory());
                if (category != 1 && category != 6 && category <= 7){
                    if (isForSchedule){
                        ToastUtils.toast("该品类无法预留或预定");
                        return;
                    }
                }

                if (orderType == ORDER_TYPE_ASSOCIATE) {
                    showAssociateOrderDialog(listItemEntity);
                } else {
                    showReceiveOrderDialog(listItemEntity);
                }
            }
        });
    }

    private void showAssociateOrderDialog(final ListItemEntity listItemEntity) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(context, R.layout.dialog_receive_order, CommonUtils.getScreenWidth(context) * 325 / 375, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void dialogCreate(final Dialog dialog, View view) {
                ImageView receive_order_data_close_iv = view.findViewById(R.id.receive_order_data_close_iv);
                TextView receive_order_data_title_tv = view.findViewById(R.id.receive_order_data_title_tv);
                KeyValueLayout receive_order_data_kvl = view.findViewById(R.id.receive_order_data_kvl);
                TextView receive_order_cancel_tv = view.findViewById(R.id.receive_order_cancel_tv);
                TextView receive_order_receive_tv = view.findViewById(R.id.receive_order_receive_tv);
                LinearLayout ll_contract_type = view.findViewById(R.id.ll_contract_type);
                NiceSpinner spinner = view.findViewById(R.id.spinner);
                receive_order_data_title_tv.setText("确认所选订单");
                receive_order_receive_tv.setText("确认选择");
                if ("1".equals(listItemEntity.getCategory())||"4".equals(listItemEntity.getCategory())||"6".equals(listItemEntity.getCategory())||Integer.valueOf(listItemEntity.getCategory())>7){
                    ll_contract_type.setVisibility(View.VISIBLE);
                    List<String> dataset = new LinkedList<>(Arrays.asList("请选择","代收代付","酒店直签","自营签单"));
                    if ("31".equals(AccountManager.newInstance().getUser().getCompany_id())){
                        dataset = new LinkedList<>(Arrays.asList("请选择","自营签单"));
                    }
                    else if ("20".equals(AccountManager.newInstance().getUser().getCompany_id())||"6".equals(AccountManager.newInstance().getUser().getCompany_id())){
                        dataset = new LinkedList<>(Arrays.asList("请选择","代收代付","酒店直签"));
                    }
                    spinner.attachDataSource(dataset);
                    spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                        @Override
                        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                            if (position!= 0 && "31".equals(AccountManager.newInstance().getUser().getCompany_id())){
                                contractType = "3";
                                return;
                            }
                            if (position!=0){
                                contractType = String.valueOf(position);
                            }else {
                                contractType = "";
                            }

                        }
                    });
                }
                receive_order_data_close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                receive_order_data_kvl.setData(listItemEntity.getRelation_info().getInfo());
                receive_order_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                receive_order_receive_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (("1".equals(listItemEntity.getCategory())||"4".equals(listItemEntity.getCategory())||"6".equals(listItemEntity.getCategory())||Integer.valueOf(listItemEntity.getCategory())>7)&&TextUtils.isEmpty(contractType)){
                            ToastUtils.toast("请选择合同类型");
                            return;
                        }
                        dialog.dismiss();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(INTENT_ORDER_DATA, listItemEntity);
                        if (!TextUtils.isEmpty(contractType))
                            bundle.putString(INTENT_CONTRACT_TYPE,contractType);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        };
        builder.show();
    }

    private void showReceiveOrderDialog(final ListItemEntity listItemEntity) {
        SimpleDialog.Builder builder = new SimpleDialog.Builder(context, R.layout.dialog_receive_order, CommonUtils.getScreenWidth(context) * 325 / 375, WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void dialogCreate(final Dialog dialog, View view) {
                ImageView receive_order_data_close_iv = view.findViewById(R.id.receive_order_data_close_iv);
                KeyValueLayout receive_order_data_kvl = view.findViewById(R.id.receive_order_data_kvl);
                TextView receive_order_cancel_tv = view.findViewById(R.id.receive_order_cancel_tv);
                TextView receive_order_receive_tv = view.findViewById(R.id.receive_order_receive_tv);
                receive_order_data_close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                receive_order_data_kvl.setData(listItemEntity.getShow_array());
                receive_order_cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                receive_order_receive_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        receiveOrder(listItemEntity);
                    }
                });
            }
        };
        builder.show();
    }

    private void receiveOrder(final ListItemEntity listItemEntity) {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("orderId", listItemEntity.getOrder_id());
        HttpRequest.create(UrlConstant.CLAIM_ORDER_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("领取成功");
                adapter.remove(listItemEntity);
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
                dismissOptionLoading();
            }
        });
    }

    private void showFilterDialog() {
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_sales_search_filter, CommonUtils.dp2pxForInt(context, 100), WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                FontTextView filter_phone_tv = view.findViewById(R.id.filter_phone_tv);
                FontTextView filter_groupid_tv = view.findViewById(R.id.filter_groupid_tv);
                FontTextView filter_order_tv = view.findViewById(R.id.filter_order_tv);
                switch (searchType) {
                    case SEARCH_PHONE_TYPE:
                        filter_phone_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_GROUPID_TYPE:
                        filter_groupid_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_ORDER_TYPE:
                        filter_order_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    default:
                        filter_phone_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                }
                filter_phone_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_PHONE_TYPE;
                        receive_order_filter_name_tv.setText("手机号");
                        receive_order_filter_input_et.setHint("支持手机尾号后4位搜索");
                        dialog.dismiss();
                    }
                });
                filter_groupid_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_GROUPID_TYPE;
                        receive_order_filter_name_tv.setText("客户编号");
                        receive_order_filter_input_et.setHint("请输入客户编号搜索");
                        dialog.dismiss();
                    }
                });
                filter_order_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_ORDER_TYPE;
                        receive_order_filter_name_tv.setText("订单号");
                        receive_order_filter_input_et.setHint("请输入订单号搜索");
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public LoadingDialog createDialog(Context context, int themeResId) {
                return new LoadingDialog(context, R.style.SearchTypeDialog);
            }
        }.setDialogAction(new BaseDialog.DialogAction() {
            @Override
            public void onAttached(Context context, View contentView) {
                Animation dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.arrow_up_rotate_dialog_show);
                dismissAnimation.setFillAfter(true);
                receive_order_filter_arrow_iv.startAnimation(dismissAnimation);
            }

            @Override
            public void onDetached(View contentView) {
                Animation dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.arrow_up_rotate_dialog_dismiss);
                dismissAnimation.setFillAfter(true);
                receive_order_filter_arrow_iv.startAnimation(dismissAnimation);
            }
        }).setCancelable(true)
                .setAnimationRes(R.style.DialogSearchAnimation)
                .setAttachView(receive_order_filter_ll)
                .setAttachOffset(CommonUtils.dp2pxForIntOffset(context, -35), CommonUtils.dp2pxForIntOffset(context, 24))
                .setGravity(Gravity.TOP | Gravity.LEFT);
        dialogBuilder.create().show();
    }
}
