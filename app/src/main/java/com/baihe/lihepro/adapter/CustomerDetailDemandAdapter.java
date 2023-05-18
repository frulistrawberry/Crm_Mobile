package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.view.FontTextView;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.CustomerDemandEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class CustomerDetailDemandAdapter extends KVFloadAdapter<CustomerDetailDemandAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<CustomerDemandEntity> list;

    private CustomerDetailDemandAdapter.OnItemClickListener listener;

    public CustomerDetailDemandAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<CustomerDemandEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<CustomerDemandEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<CustomerDemandEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_customer_detail_demand_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final CustomerDemandEntity customerDemandEntity = list.get(position);
        holder.customer_demand_item_name_tv.setText(customerDemandEntity.getReq_num());
        holder.customer_demand_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return customerDemandEntity.getCategory_color().size();
            }

            @Override
            public String getLabelText(int position) {
                return customerDemandEntity.getCategory_color().get(position).getCategory_name();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = customerDemandEntity.getCategory_color().get(position).getCategory_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        labelDrawable.setColor(Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return labelDrawable;
            }
        });
        holder.customer_demand_item_data_kvl.setData(customerDemandEntity.getShow_array());
        holder.customer_demand_item_data_kvl.setKVFloadAdapterListener(this, position);
        if (customerDemandEntity.getButton_type() != null && customerDemandEntity.getButton_type().size() > 0) {
            holder.customer_demand_item_edit_ll.setVisibility(View.VISIBLE);
        } else {
            holder.customer_demand_item_edit_ll.setVisibility(View.GONE);
        }
        holder.customer_demand_item_edit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.edit(customerDemandEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private FontTextView customer_demand_item_name_tv;
        private FlowLayout customer_demand_item_label_fl;
        private KeyValueLayout customer_demand_item_data_kvl;
        private LinearLayout customer_demand_item_edit_ll;

        public Holder(@NonNull View itemView) {
            super(itemView);
            customer_demand_item_name_tv = itemView.findViewById(R.id.customer_demand_item_name_tv);
            customer_demand_item_label_fl = itemView.findViewById(R.id.customer_demand_item_label_fl);
            customer_demand_item_data_kvl = itemView.findViewById(R.id.customer_demand_item_data_kvl);
            customer_demand_item_edit_ll = itemView.findViewById(R.id.customer_demand_item_edit_ll);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void edit(CustomerDemandEntity customerDemandEntity);
    }
}
