package com.baihe.lihepro.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CustomerEntity;
import com.baihe.lihepro.entity.CustomerListEntity;
import com.baihe.lihepro.entity.PayCodeCustomerEntity;
import com.baihe.lihepro.entity.PayCodeCustomerListEntity;
import com.baihe.lihepro.entity.PayCodeEntity;
import com.baihe.lihepro.entity.PayCodeListEntity;
import com.blankj.utilcode.util.ScreenUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonSearchDialog extends Dialog {
    public PersonSearchDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    public static class Builder {
        private Context context;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private String title;
        private boolean cancelable = true;
        private int page = 1;
        private String keywords;

        private TextView bottom_select_cancel_tv;
        private TextView bottom_select_confirm_tv;
        private EditText bottom_select_search_et;
        private ImageView bottom_select_search_delete_iv;
        private TextView bottom_select_title_tv;
        private RecyclerView bottom_select_list_rv;
        private SmartRefreshLayout person_search_list_srl;
        private PersonSelectAdapter bottomSelectAdapter;
        private StatusLayout statusLayout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnConfirmClickListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public PersonSearchDialog.Builder setOnCancelClickListener(OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public PersonSelectDialog build() {
            PersonSelectDialog dialog = new PersonSelectDialog(context);
            dialog.setContentView(R.layout.dialog_person_search);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (ScreenUtils.getScreenHeight() * 0.7);
            window.setWindowAnimations(R.style.dialog_style);
            window.setGravity(Gravity.BOTTOM);
            initView(dialog);
            initData();
            listener(dialog);
            return dialog;
        }

        public PersonSelectDialog show() {
            PersonSelectDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            bottom_select_cancel_tv = dialog.findViewById(R.id.bottom_select_cancel_tv);
            bottom_select_confirm_tv = dialog.findViewById(R.id.bottom_select_confirm_tv);
            bottom_select_search_et = dialog.findViewById(R.id.bottom_select_search_et);
            bottom_select_search_delete_iv = dialog.findViewById(R.id.bottom_select_search_delete_iv);
            bottom_select_title_tv = dialog.findViewById(R.id.bottom_select_title_tv);
            bottom_select_list_rv = dialog.findViewById(R.id.bottom_select_list_rv);
            person_search_list_srl = dialog.findViewById(R.id.person_search_list_srl);
            statusLayout = dialog.findViewById(R.id.status_layout);
            bottom_select_confirm_tv.getPaint().setFakeBoldText(true);
            bottom_select_search_et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        }

        private void initData() {
            if (TextUtils.isEmpty(title)) {
                bottom_select_title_tv.setVisibility(View.GONE);
            } else {
                bottom_select_title_tv.setVisibility(View.VISIBLE);
                bottom_select_title_tv.getPaint().setFakeBoldText(true);
                bottom_select_title_tv.setText(title);
            }
            bottomSelectAdapter = new PersonSelectAdapter(context);
            bottom_select_list_rv.setAdapter(bottomSelectAdapter);
            bottom_select_list_rv.setLayoutManager(new PersonSelectDialog.MyLinearLayoutManager(context));
            final int driverMagin = CommonUtils.dp2pxForInt(context, 25.0F);
            final int driverHeight = CommonUtils.dp2pxForInt(context, 0.5F);
            final Paint driverPaint = new Paint();
            driverPaint.setColor(Color.parseColor("#ECECF0"));
            bottom_select_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                    int childSize = parent.getChildCount();
                    for (int i = 0; i < childSize - 1; i++) {
                        View child = parent.getChildAt(i);
                        float top = child.getBottom();
                        float bottom = top + driverHeight;
                        float right = child.getRight();
                        c.drawRect(driverMagin, top, right - driverMagin, bottom, driverPaint);
                    }
                }

            });
        }

        private void listener(final Dialog dialog) {
            bottom_select_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onCancel(dialog);
                    }
                }
            });

            bottom_select_confirm_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bottomSelectAdapter.getSelectId() != -1) {
                        dialog.dismiss();
                        if (confirmListener != null) {
                            confirmListener.onConfirm(dialog, bottomSelectAdapter.getSelectText(), bottomSelectAdapter.getCustomId());
                        }
                    }
                }
            });
            bottom_select_search_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String keyword = s.toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        bottom_select_search_delete_iv.setVisibility(View.GONE);
                    } else {
                        bottom_select_search_delete_iv.setVisibility(View.VISIBLE);
                    }
                }
            });
            bottom_select_search_delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottom_select_search_et.setText("");
                    keywords = "";
                }
            });
            if (person_search_list_srl != null)


            person_search_list_srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    loadData(keywords);
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    page = 1;
                    loadData(keywords);
                }
            });
            bottom_select_search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_SEARCH){
                        keywords = bottom_select_search_et.getText().toString();
                        if (!TextUtils.isEmpty(keywords) &&(keywords.length() == 4 || keywords.length() == 11)){
                            page = 1;
                            loadData(keywords);
                        }else {
                            ToastUtils.toast("请输入手机号4位或全部号码");
                        }
                    }
                    return false;
                }
            });
        }

        private void loadData(String keywords) {
            JsonParam jsonParam = JsonParam.newInstance("params");
            jsonParam.putParamValue("pageSize", "20").putParamValue("page", String.valueOf(page))
                    .putParamValue("keyword",keywords);
            HttpRequest.create(UrlConstant.PAY_CODE_SEARCH_CUSTOMER)
                    .tag("搜索客户")
                    .putParam(jsonParam)
                    .get(new CallBack<PayCodeCustomerListEntity>() {
                        @Override
                        public PayCodeCustomerListEntity doInBackground(String response) {
                            return JsonUtils.parse(response, PayCodeCustomerListEntity.class);
                        }

                        @Override
                        public void success(PayCodeCustomerListEntity entity) {
                            if (page == 1 && entity.getRows().size() == 0) {
                                //无数据
                                statusLayout.expandStatus();
                            } else {
                                statusLayout.normalStatus();
                            }
                            if (page == 1) {
                                bottomSelectAdapter.setData(entity.getRows());
                            } else {
                                bottomSelectAdapter.addData(entity.getRows());
                            }
                            page++;

                            if (entity.getTotal() > entity.getPage() * entity.getPageSize()) {
                                //开启加上拉加载功能
                                person_search_list_srl.setEnableLoadMore(true);
                            } else {
                                //关闭加上拉加载功能
                                person_search_list_srl.setEnableLoadMore(false);
                            }
                        }

                        @Override
                        public void error() {
                            if (bottomSelectAdapter.getData().size() == 0) {
                                statusLayout.netErrorStatus();
                            } else {
                                ToastUtils.toastNetError();
                            }
                        }

                        @Override
                        public void fail() {
                            if (bottomSelectAdapter.getData().size() == 0) {
                                statusLayout.netFailStatus();
                            } else {
                                ToastUtils.toastNetWorkFail();
                            }
                        }

                        @Override
                        public void before() {
                            super.before();
                            if (bottomSelectAdapter.getData().size() == 0) {
                                statusLayout.loadingStatus();
                            }
                        }

                        @Override
                        public void after() {
                            super.after();
                            if (person_search_list_srl.isLoading()) {
                                person_search_list_srl.finishLoadMore();
                            }
                            if (person_search_list_srl.isRefreshing()) {
                                person_search_list_srl.finishRefresh();
                            }
                        }
                    });
        }

    }
    public static class PersonSelectAdapter extends RecyclerView.Adapter<PersonSelectAdapter.Holder> {
        private Context context;
        private LayoutInflater inflater;
        private int selectPosition = -1;
        private String keyWord;

        private List<PayCodeCustomerEntity> mData;

        public PersonSelectAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.mData = new ArrayList<>();
        }

        public void setData(List<PayCodeCustomerEntity> entities) {
            this.mData.clear();
            this.mData.addAll(entities);
            notifyDataSetChanged();
        }

        public void addData(List<PayCodeCustomerEntity> entities) {
            this.mData.addAll(entities);
            notifyDataSetChanged();
        }

        public List<PayCodeCustomerEntity> getData() {
            return mData;
        }


        @NonNull
        @Override
        public PersonSelectAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.dialog_bottom_select_item, parent, false);
            PersonSelectAdapter.Holder holder = new PersonSelectAdapter.Holder(view);
            return holder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull PersonSelectAdapter.Holder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPosition = position;
                    notifyDataSetChanged();
                }
            });
            String customerName = mData.get(position).getCustomer_name();
            String customerPhone = mData.get(position).getPhone();

            holder.bottom_select_item_name_tv.setText(customerName+" | "+customerPhone);
            holder.bottom_select_item_name_tv.getPaint().setFakeBoldText(true);
            if (position == this.selectPosition) {
                holder.bottom_select_item_checked_cb.setChecked(true);
            } else {
                holder.bottom_select_item_checked_cb.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null?0:mData.size();
        }

        public int getSelectId() {
            return selectPosition;
        }

        public String getCustomId(){
            return String.valueOf(mData.get(selectPosition).getCustomer_id());
        }




        public String getSelectText() {
            return mData.get(selectPosition).getCustomer_name()+" | "+ mData.get(selectPosition).getPhone();
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView bottom_select_item_name_tv;
            public CheckBox bottom_select_item_checked_cb;

            public Holder(View itemView) {
                super(itemView);
                bottom_select_item_name_tv = itemView.findViewById(R.id.bottom_select_item_name_tv);
                bottom_select_item_checked_cb = itemView.findViewById(R.id.bottom_select_item_checked_cb);
            }
        }
    }


    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, String customerName, String customerId);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }

}
