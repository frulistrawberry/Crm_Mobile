package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.CustomerEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<CustomerEntity> list;

    private CustomerListAdapter.OnItemClickListener listener;

    public CustomerListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<CustomerEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<CustomerEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<CustomerEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_requirement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final CustomerEntity customerEntity = list.get(position);
        holder.requirement_list_item_name_tv.setText(customerEntity.getCustomer_name());
        holder.requirement_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(customerEntity.getCustomer_level()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return customerEntity.getCustomer_level() + "类";
            }

            @Override
            public int getLabelTextColor(int position) {
                return Color.parseColor("#00B6EB");
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.round_level_label_drawable);
                return drawable;
            }
        });
        holder.requirement_list_item_data_kvl.setData(customerEntity.getShow_array());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(customerEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView requirement_list_item_name_tv;
        private FlowLayout requirement_list_item_label_fl;
        private KeyValueLayout requirement_list_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            requirement_list_item_name_tv = itemView.findViewById(R.id.requirement_list_item_name_tv);
            requirement_list_item_label_fl = itemView.findViewById(R.id.requirement_list_item_label_fl);
            requirement_list_item_data_kvl = itemView.findViewById(R.id.requirement_list_item_data_kvl);
        }
    }

    public void setOnItemClickListener(CustomerListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CustomerEntity customerEntity);
    }
}
