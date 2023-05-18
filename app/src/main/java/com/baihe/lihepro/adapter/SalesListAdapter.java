package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class SalesListAdapter extends RecyclerView.Adapter<SalesListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ListItemEntity> list;

    private OnItemClickListener listener;

    public SalesListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<ListItemEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<ListItemEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<ListItemEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_sales_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ListItemEntity listitemEntity = list.get(position);
        holder.sales_list_item_name_tv.setText(listitemEntity.getCustomer_name());
        holder.sales_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(listitemEntity.getCategory_text()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return listitemEntity.getCategory_text();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = listitemEntity.getCategory_color();
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
        holder.sales_list_item_data_kvl.setData(listitemEntity.getShow_array());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(listitemEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView sales_list_item_name_tv;
        private FlowLayout sales_list_item_label_fl;
        private KeyValueLayout sales_list_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            sales_list_item_name_tv = itemView.findViewById(R.id.sales_list_item_name_tv);
            sales_list_item_label_fl = itemView.findViewById(R.id.sales_list_item_label_fl);
            sales_list_item_data_kvl = itemView.findViewById(R.id.sales_list_item_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListItemEntity listitemEntity);
    }
}
