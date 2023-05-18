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
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-03
 * Description：
 */
public class DiscountActivity extends BaseActivity {
    public static void start(Activity activity, ArrayList<List<KeyValueEntity>> data, int requestCode) {
        Intent intent = new Intent(activity, DiscountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_DISCOUNT_DATA, data);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String INTENT_DISCOUNT_DATA = "INTENT_DISCOUNT_DATA";

    private RecyclerView discount_rv;
    private TextView discount_ok_tv;

    private List<List<KeyValueEntity>> data;
    private List<KeyValueEntity> configData;
    private DiscountAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (List<List<KeyValueEntity>>) getIntent().getSerializableExtra(INTENT_DISCOUNT_DATA);
        setTitleText("优惠信息");
        setContentView(R.layout.activity_discount);
        init();
        initData();
        listener();
        loadData();
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("type", "discount");
        HttpRequest.create(UrlConstant.BUILD_PARAMS_URL).putParam(jsonParam).get(new CallBack<List<KeyValueEntity>>() {
            @Override
            public List<KeyValueEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, KeyValueEntity.class);
            }

            @Override
            public void success(List<KeyValueEntity> list) {
                statusLayout.normalStatus();
                configData = list;
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

    private void init() {
        discount_rv = findViewById(R.id.discount_rv);
        discount_ok_tv = findViewById(R.id.discount_ok_tv);
    }

    private void initData() {
        adapter = new DiscountAdapter(context);
        adapter.setData(data);
        discount_rv.setLayoutManager(new LinearLayoutManager(context));
        discount_rv.setAdapter(adapter);
        discount_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (adapter.getItemCount() == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 6), 0, CommonUtils.dp2pxForInt(context, 30));
                } else {
                    if (position == 0) {
                        outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                    } else if (position == adapter.getItemCount() - 1) {
                        outRect.set(0, CommonUtils.dp2pxForInt(context, -7), 0, CommonUtils.dp2pxForInt(context, 30));
                    } else {
                        outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                    }
                }
            }
        });
    }

    private void listener() {
        adapter.setOnItemListener(new DiscountAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd() {
                List<KeyValueEntity> newData = new ArrayList<>();
                for (KeyValueEntity keyValueEntity : configData) {
                    if (keyValueEntity != null) {
                        newData.add(keyValueEntity.copy());
                    }
                }
                adapter.addData(newData);
            }
        });
        discount_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<List<KeyValueEntity>> dataList = getData();
                if (dataList != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(INTENT_DISCOUNT_DATA, (ArrayList) dataList);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private List<List<KeyValueEntity>> getData() {
        List<List<KeyValueEntity>> dataList = new ArrayList<>();
        for (List<KeyValueEntity> keyValueEntities : adapter.getData()) {
            for (KeyValueEntity keyValueEntity : keyValueEntities) {
                //显示的item代表可提供的数据
                if ("1".equals(keyValueEntity.getShowStatus())) {
                    //必填却没有填写，中断并toast提示
                    if ("1".equals(keyValueEntity.getOptional()) && TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                        ToastUtils.toast(KeyValueEditLayout.getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
                        return null;
                    }
                }
            }
            dataList.add(keyValueEntities);
        }
        return dataList;
    }

    public static class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.Holder> {
        private static final int NORMAL_TYPE = 1;
        private static final int ADD_TYPE = 2;

        private List<List<KeyValueEntity>> list;
        private LayoutInflater inflater;
        private OnItemClickListener listener;

        public DiscountAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.list = new ArrayList<>();
        }

        public void setData(List<List<KeyValueEntity>> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }

        public void addData(List<KeyValueEntity> data) {
            this.list.add(data);
            notifyDataSetChanged();
        }

        private List<List<KeyValueEntity>> getData() {
            return list;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() - 1 == position) {
                return ADD_TYPE;
            }
            return NORMAL_TYPE;
        }

        @NonNull
        @Override
        public DiscountAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == ADD_TYPE) {
                view = inflater.inflate(R.layout.activity_contract_add_item_product_add, parent, false);
            } else {
                view = inflater.inflate(R.layout.activity_contract_item_product, parent, false);
            }
            return new DiscountAdapter.Holder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull DiscountAdapter.Holder holder, final int position) {
            int viewType = getItemViewType(position);
            if (viewType == ADD_TYPE) {
                holder.contract_add_item_add_tv.setText("添加优惠");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemAdd();
                        }
                    }
                });
            } else {
                holder.contract_add_item_name_tv.setText("优惠信息" + Utils.formatNumText(position + 1));
                List<KeyValueEntity> data = list.get(position);
                holder.contract_add_item_content_kvel.setData(data);
                holder.contract_add_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView contract_add_item_add_tv;
            public TextView contract_add_item_name_tv;
            public KeyValueEditLayout contract_add_item_content_kvel;
            public ImageView contract_add_item_delete_iv;


            public Holder(@NonNull View itemView, int viewType) {
                super(itemView);
                if (viewType == ADD_TYPE) {
                    contract_add_item_add_tv = itemView.findViewById(R.id.contract_add_item_add_tv);
                } else {
                    contract_add_item_name_tv = itemView.findViewById(R.id.contract_add_item_name_tv);
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
        }
    }

}
