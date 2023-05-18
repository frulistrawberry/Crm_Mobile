package com.baihe.lihepro.activity;

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
import com.baihe.lihepro.adapter.RequirementSearchAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.RequirementEntity;
import com.baihe.lihepro.entity.RequirementListEntity;
import com.blankj.utilcode.util.KeyboardUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * Author：xubo
 * Time：2020-07-28
 * Description：
 */
public class RequirementSearchActivity extends BaseActivity {

    public static void start(Context context,int type) {
        Intent intent = new Intent(context, RequirementSearchActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static final int SEARCH_PHONE_TYPE = 0;
    public static final int SEARCH_GROUPID_TYPE = 1;
    public static final int SEARCH_WECHAT_TYPE = 2;

    private SmartRefreshLayout requirement_search_srl;
    private RecyclerView requirement_search_rv;
    private LinearLayout search_filter_ll;
    private TextView search_filter_name_tv;
    private ImageView search_filter_arrow_iv;
    private EditText search_filter_input_et;
    private ImageView search_filter_input_delete_iv;
    private TextView search_cancel_tv;

    private RequirementSearchAdapter adapter;
    private int page = 1;

    //默认搜索类型 0手机号 1集团id 2微信
    private int searchType = SEARCH_PHONE_TYPE;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_requirement_search_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -6);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_requirement_search, null), params);
        type = getIntent().getIntExtra("type",2);

        init();
        initData();
        listener();
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
    }

    private void initData() {
        //关闭下来刷新
        requirement_search_srl.setEnableRefresh(false);
        search_filter_input_et.requestFocus();

        adapter = new RequirementSearchAdapter(context);
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
    }

    private void listener() {
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
        adapter.setOnItemClickListener(new RequirementSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RequirementEntity requirementEntity) {
                CustomerDetailActivity.start(context, requirementEntity.getCustomer_id(), requirementEntity.getCustomer_name(), type+"", CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT);
            }
        });
    }

    private void loadData() {
        String keyword = adapter.getKeyword();
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("type",type).putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page));
        switch (searchType) {
            case SEARCH_PHONE_TYPE:
                jsonParam.putParamValue("customerPhone", keyword);
                break;
            case SEARCH_GROUPID_TYPE:
                jsonParam.putParamValue("customerId", keyword);
                break;
            case SEARCH_WECHAT_TYPE:
                jsonParam.putParamValue("customerWechat", keyword);
                break;
        }
        HttpRequest.create(UrlConstant.INVITE_LIST_URL)
                .tag("邀约搜索")
                .putParam(jsonParam)
                .get(new CallBack<RequirementListEntity>() {
                    @Override
                    public RequirementListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, RequirementListEntity.class);
                    }

                    @Override
                    public void success(RequirementListEntity entity) {
                        if (page == 1 && entity.getRows().size() == 0) {
                            //无数据
                            statusLayout.expandStatus();
                        } else {
                            //由于进来关闭了下来刷新，有数据了直接开启
//                            requirement_search_srl.setEnableRefresh(true);
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
        DialogBuilder<BaseDialog> dialogBuilder = new DialogBuilder<BaseDialog>(context, R.layout.dialog_requirement_search_filter, CommonUtils.dp2pxForInt(context, 100), WindowManager.LayoutParams.WRAP_CONTENT) {
            @Override
            public void createView(final Dialog dialog, View view) {
                FontTextView filter_phone_tv = view.findViewById(R.id.filter_phone_tv);
                FontTextView filter_groupid_tv = view.findViewById(R.id.filter_groupid_tv);
                FontTextView filter_wechat_tv = view.findViewById(R.id.filter_wechat_tv);
                switch (searchType) {
                    case SEARCH_PHONE_TYPE:
                        filter_phone_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_GROUPID_TYPE:
                        filter_groupid_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                        break;
                    case SEARCH_WECHAT_TYPE:
                        filter_wechat_tv.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
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
                filter_wechat_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchType = SEARCH_WECHAT_TYPE;
                        search_filter_name_tv.setText("微信号");
                        search_filter_input_et.setHint("请输入微信号搜索");
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
