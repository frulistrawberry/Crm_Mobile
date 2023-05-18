package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
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
import com.baihe.lihepro.adapter.ContractListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.ContractItemEntity;
import com.baihe.lihepro.entity.ContractListEntity;
import com.blankj.utilcode.util.KeyboardUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * Author：xubo
 * Time：2020-07-28
 * Description：
 */
public class ContractSearchActivity extends BaseActivity {
    public static final int REQUEST_CODE_DETAIL_CONTRACT = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, ContractSearchActivity.class);
        context.startActivity(intent);
    }


    public static void start(Activity context,String date,String hallId,String hallName,int sType) {
        Intent intent = new Intent(context, ContractSearchActivity.class);
        intent.putExtra("mode",1);
        intent.putExtra("sType",sType);
        intent.putExtra("date",date);
        intent.putExtra("hallId",hallId);
        intent.putExtra("hallName",hallName);
        intent.putExtra("isMulti",0);
        context.startActivityForResult(intent,101);
    }

    public static void start(Activity context,String hallId,String hallName,int startType,int endType,String startDate,String endDate) {
        Intent intent = new Intent(context, ContractSearchActivity.class);
        intent.putExtra("mode",1);
        intent.putExtra("startType",startType);
        intent.putExtra("endType",endType);
        intent.putExtra("startDate",startDate);
        intent.putExtra("endDate",endDate);
        intent.putExtra("hallId",hallId);
        intent.putExtra("hallName",hallName);
        intent.putExtra("isMulti",1);
        context.startActivityForResult(intent,101);
    }

    public static final int SEARCH_PHONE_TYPE = 1;
    public static final int SEARCH_GROUPID_TYPE = 2;
    public static final int SEARCH_CONTRACT_TYPE = 3;
    public static final int SEARCH_ORDER_TYPE = 4;

    private SmartRefreshLayout requirement_search_srl;
    private RecyclerView requirement_search_rv;
    private LinearLayout search_filter_ll;
    private TextView search_filter_name_tv;
    private ImageView search_filter_arrow_iv;
    private EditText search_filter_input_et;
    private ImageView search_filter_input_delete_iv;
    private TextView search_cancel_tv;
    private LinearLayout ll_bottom;
    private TextView btn_create;

    private ContractListAdapter adapter;
    private int page = 1;

    private String hallId;
    private String hallName;
    private String date;
    private int sType;
    private int startType;
    private int endType;
    private String startDate;
    private String endDate;
    private int isMulti;

    //默认搜索类型 1手机号 2集团id 3合同号 4订单号
    private int searchType = SEARCH_CONTRACT_TYPE;

    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_requirement_search_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -6);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_requirement_search, null), params);
        statusLayout = findViewById(R.id.status_layout);
        init();
        initData();
        listener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DETAIL_CONTRACT:
                    loadData();
                    break;
                case 101:
                    setResult(102);
                    finish();
                    break;
            }
        }
    }

    private void init() {
        requirement_search_srl = findViewById(R.id.requirement_search_srl);
        requirement_search_rv = findViewById(R.id.requirement_search_rv);
        search_filter_ll = findViewById(R.id.search_filter_ll);
        search_filter_name_tv = findViewById(R.id.search_filter_name_tv);
        search_filter_arrow_iv = findViewById(R.id.search_filter_arrow_iv);
        search_filter_input_et = findViewById(R.id.search_filter_input_et);
        search_filter_input_delete_iv = findViewById(R.id.search_filter_input_delete_iv);
        search_cancel_tv = findViewById(R.id.search_cancel_tv);
        ll_bottom = findViewById(R.id.ll_bottom);
        btn_create = findViewById(R.id.btn_create);
        search_filter_name_tv.setText("合同号");
        search_filter_input_et.setHint("请输入合同号搜索");
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
        //关闭下来刷新
        requirement_search_srl.setEnableRefresh(false);
        search_filter_input_et.requestFocus();
        mode = getIntent().getIntExtra("mode",0);
        adapter = new ContractListAdapter(context, true);
        requirement_search_rv.setAdapter(adapter);
        requirement_search_rv.setLayoutManager(new LinearLayoutManager(context));

        requirement_search_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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

        if(mode == 1){
            loadData();
            ll_bottom.setVisibility(View.VISIBLE);
        }else {
            ll_bottom.setVisibility(View.GONE);
        }

    }

    private void listener() {
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContractNewActivity.start(ContractSearchActivity.this,date,hallId,hallName,sType,startType,endType,startDate,endDate,isMulti);
            }
        });
        search_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
        search_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(search_filter_input_et);
                finish();
            }
        });
        requirement_search_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        search_filter_input_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideSoftInput(search_filter_input_et);

                    String keyword = search_filter_input_et.getText().toString().trim();
                    adapter.updateSearch(keyword, searchType);
                    page = 1;
                    loadData();
                    return true;
                }
                return false;
            }
        });
        search_filter_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    search_filter_input_delete_iv.setVisibility(View.VISIBLE);
                } else {
                    search_filter_input_delete_iv.setVisibility(View.GONE);
                }
            }
        });
        search_filter_input_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_filter_input_et.setText("");
            }
        });
        adapter.setOnItemClickListener(new ContractListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContractItemEntity contractItemEntity) {
                if (mode == 0)
                    ContractDetailActivity.start(ContractSearchActivity.this, contractItemEntity.getId(), "contract", REQUEST_CODE_DETAIL_CONTRACT);
                else{
                    JsonParam param = new JsonParam("params").putParamValue("contractId",contractItemEntity.getId());
                    HttpRequest.create(UrlConstant.SCHEDULE_CHECK_ORDER).putParam(param).get(new CallBack<Boolean>() {
                        @Override
                        public Boolean doInBackground(String response) {
                            return JsonUtils.parse(response, Boolean.class);
                        }

                        @Override
                        public void success(Boolean entity) {
                            if (entity){
                                Intent intent = new Intent();
                                intent.putExtra("contractId",contractItemEntity.getId());
                                setResult(RESULT_OK,intent);
                                finish();
                            }else {
                                ToastUtils.toast("该合同已经预订了其他档期，不可重复预订");
                            }

                        }

                        @Override
                        public void error() {

                        }

                        @Override
                        public void fail() {

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

            }
        });
    }

    private void loadData() {
        String keyword = adapter.getKeyword();
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page));
        switch (searchType) {
            case SEARCH_PHONE_TYPE:
                jsonParam.putParamValue("key", keyword);
                jsonParam.putParamValue("search_type", 1);
                break;
            case SEARCH_GROUPID_TYPE:
                jsonParam.putParamValue("key", keyword);
                jsonParam.putParamValue("search_type", 2);
                break;
            case SEARCH_CONTRACT_TYPE:
                jsonParam.putParamValue("key", keyword);
                jsonParam.putParamValue("search_type", 3);
                break;
            case SEARCH_ORDER_TYPE:
                jsonParam.putParamValue("key", keyword);
                jsonParam.putParamValue("search_type", 4);
                break;
        }
        HttpRequest.create(UrlConstant.CONTRACT_LIST2_URL)
                .tag("合同搜索")
                .putParam(jsonParam)
                .get(new CallBack<ContractListEntity>() {
                    @Override
                    public ContractListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, ContractListEntity.class);
                    }

                    @Override
                    public void success(ContractListEntity entity) {
                        if (page == 1 && entity.getContractList().size() == 0) {
                            //无数据
                            statusLayout.expandStatus();
                        } else {
                            //由于进来关闭了下来刷新，有数据了直接开启
//                            requirement_search_srl.setEnableRefresh(true);
                            statusLayout.normalStatus();
                        }
                        if (page == 1) {
                            adapter.setData(entity.getContractList());
                        } else {
                            adapter.addData(entity.getContractList());
                        }
                        page++;

                        if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                            //开启加上拉加载功能
                            requirement_search_srl.setEnableLoadMore(true);
                        } else {
                            //关闭加上拉加载功能
                            requirement_search_srl.setEnableLoadMore(false);
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
                        if (requirement_search_srl.isLoading()) {
                            requirement_search_srl.finishLoadMore();
                        }
                        if (requirement_search_srl.isRefreshing()) {
                            requirement_search_srl.finishRefresh();
                        }
                    }
                });
    }

    private void showFilterDialog() {
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_contract_search_filter, CommonUtils.dp2pxForInt(context, 100), WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                FontTextView filter_phone_tv = view.findViewById(R.id.filter_phone_tv);
                FontTextView filter_groupid_tv = view.findViewById(R.id.filter_groupid_tv);
                FontTextView filter_contract_tv = view.findViewById(R.id.filter_contract_tv);
                FontTextView filter_order_tv = view.findViewById(R.id.filter_order_tv);
                switch (searchType) {
                    case SEARCH_PHONE_TYPE:
                        filter_phone_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_GROUPID_TYPE:
                        filter_groupid_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_CONTRACT_TYPE:
                        filter_contract_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
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
                        search_filter_name_tv.setText("手机号");
                        search_filter_input_et.setHint("支持手机尾号后4位搜索");
                        dialog.dismiss();
                    }
                });
                filter_groupid_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_GROUPID_TYPE;
                        search_filter_name_tv.setText("客户编号");
                        search_filter_input_et.setHint("请输入客户编号搜索");
                        dialog.dismiss();
                    }
                });
                filter_contract_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_CONTRACT_TYPE;
                        search_filter_name_tv.setText("合同号");
                        search_filter_input_et.setHint("请输入合同号搜索");
                        dialog.dismiss();
                    }
                });
                filter_order_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_ORDER_TYPE;
                        search_filter_name_tv.setText("订单号");
                        search_filter_input_et.setHint("请输入订单号搜索");
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
                search_filter_arrow_iv.startAnimation(dismissAnimation);
            }

            @Override
            public void onDetached(View contentView) {
                Animation dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.arrow_up_rotate_dialog_dismiss);
                dismissAnimation.setFillAfter(true);
                search_filter_arrow_iv.startAnimation(dismissAnimation);
            }
        }).setCancelable(true)
                .setAnimationRes(R.style.DialogSearchAnimation)
                .setAttachView(search_filter_ll)
                .setAttachOffset(CommonUtils.dp2pxForIntOffset(context, -35), CommonUtils.dp2pxForIntOffset(context, 24))
                .setGravity(Gravity.TOP | Gravity.LEFT);
        dialogBuilder.create().show();
    }


}
