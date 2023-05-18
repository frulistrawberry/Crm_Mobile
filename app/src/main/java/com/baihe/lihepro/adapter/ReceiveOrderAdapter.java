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
import com.baihe.lihepro.activity.SalesSearchActivity;
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
public class ReceiveOrderAdapter extends RecyclerView.Adapter<ReceiveOrderAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ListItemEntity> list;

    private String keyword;
    private int searchType = SalesSearchActivity.SEARCH_PHONE_TYPE;

    private OnItemClickListener listener;

    public ReceiveOrderAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void updateSearch(String keyword, int searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
        this.list.clear();
        notifyDataSetChanged();
    }

    public void remove(ListItemEntity listItemEntity) {
        if (list.contains(listItemEntity)) {
            list.remove(listItemEntity);
        }
        notifyDataSetChanged();
    }

    public String getKeyword() {
        return keyword;
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
        return new Holder(inflater.inflate(R.layout.activity_receive_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ListItemEntity listItemEntity = list.get(position);
        holder.receive_order_item_name_tv.setText(listItemEntity.getCustomer_name());
        holder.receive_order_item_data_kvl.setData(listItemEntity.getShow_array());
        holder.receive_order_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(listItemEntity.getCategory_text()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return listItemEntity.getCategory_text();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = listItemEntity.getCategory_color();
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(listItemEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView receive_order_item_name_tv;
        private FlowLayout receive_order_item_label_fl;
        private KeyValueLayout receive_order_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            receive_order_item_name_tv = itemView.findViewById(R.id.receive_order_item_name_tv);
            receive_order_item_label_fl = itemView.findViewById(R.id.receive_order_item_label_fl);
            receive_order_item_data_kvl = itemView.findViewById(R.id.receive_order_item_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListItemEntity listItemEntity);
    }
}
