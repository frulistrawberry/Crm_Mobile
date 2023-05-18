package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.common.view.FontTextView;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.CustomerSalesEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class CustomerDetailSalesAdapter extends KVFloadAdapter<CustomerDetailSalesAdapter.Holder> {
    private final static int TOP_TYPE = 1;
    private final static int MIDDLE_TYPE = 2;
    private final static int BOTTOM_TYPE = 3;
    private final static int ONLY_TYPE = 4;

    private Context context;
    private LayoutInflater inflater;
    private List<CustomerSalesEntity> list;

    private CustomerDetailSalesAdapter.OnItemClickListener listener;

    public CustomerDetailSalesAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<CustomerSalesEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<CustomerSalesEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<CustomerSalesEntity> getData() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1) {
            return ONLY_TYPE;
        } else if (position == 0) {
            return TOP_TYPE;
        } else if (position == getItemCount() - 1) {
            return BOTTOM_TYPE;
        } else {
            return MIDDLE_TYPE;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ONLY_TYPE) {
            return new Holder(inflater.inflate(R.layout.fragment_customer_detail_sales_item_only, parent, false));
        } else if (viewType == TOP_TYPE) {
            return new Holder(inflater.inflate(R.layout.fragment_customer_detail_sales_item_top, parent, false));
        } else if (viewType == BOTTOM_TYPE) {
            return new Holder(inflater.inflate(R.layout.fragment_customer_detail_sales_item_bottom, parent, false));
        } else {
            return new Holder(inflater.inflate(R.layout.fragment_customer_detail_sales_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int itemOffset = CommonUtils.dp2pxForInt(context, 24);
        LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) holder.customer_sales_item_title_ll.getLayoutParams();
        LinearLayout.LayoutParams bottomParams = (LinearLayout.LayoutParams) holder.customer_sales_item_data_kvl.getLayoutParams();
        if (position == 0) {
            titleParams.topMargin = itemOffset;
            bottomParams.bottomMargin = itemOffset;
        } else if (position == getItemCount() - 1) {
            bottomParams.bottomMargin = itemOffset;
        } else {
            bottomParams.bottomMargin = itemOffset;
        }
        final CustomerSalesEntity customerSalesEntity = list.get(position);
        holder.customer_sales_item_time_tv.setText(customerSalesEntity.getFollow_time());
        holder.customer_sales_item_name_tv.setText(customerSalesEntity.getFollow_type());
        holder.customer_sales_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(customerSalesEntity.getContact_way()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return customerSalesEntity.getContact_way();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                try {
                    labelDrawable.setColor(Color.parseColor("#3300B6EB"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return labelDrawable;
            }
        });
        holder.customer_sales_item_data_kvl.setData(customerSalesEntity.getShow_array());
        if ("1".equals(customerSalesEntity.getType())) {
            holder.customer_sales_item_go_iv.setVisibility(View.INVISIBLE);
        } else {
            holder.customer_sales_item_go_iv.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"1".equals(customerSalesEntity.getType())) {
                    if (listener != null) {
                        listener.onItemClick(customerSalesEntity);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private LinearLayout customer_sales_item_title_ll;
        private FontTextView customer_sales_item_time_tv;
        private FontTextView customer_sales_item_name_tv;
        private FlowLayout customer_sales_item_label_fl;
        private ImageView customer_sales_item_go_iv;
        private KeyValueLayout customer_sales_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            customer_sales_item_title_ll = itemView.findViewById(R.id.customer_sales_item_title_ll);
            customer_sales_item_time_tv = itemView.findViewById(R.id.customer_sales_item_time_tv);
            customer_sales_item_name_tv = itemView.findViewById(R.id.customer_sales_item_name_tv);
            customer_sales_item_label_fl = itemView.findViewById(R.id.customer_sales_item_label_fl);
            customer_sales_item_go_iv = itemView.findViewById(R.id.customer_sales_item_go_iv);
            customer_sales_item_data_kvl = itemView.findViewById(R.id.customer_sales_item_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CustomerSalesEntity customerSalesEntity);
    }
}
