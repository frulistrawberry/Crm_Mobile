package com.baihe.lihepro.adapter;

import android.content.Context;
import android.text.Html;
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
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class SalesSearchAdapter extends RecyclerView.Adapter<SalesSearchAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ListItemEntity> list;

    private String keyword;
    private int searchType = SalesSearchActivity.SEARCH_PHONE_TYPE;

    private OnItemClickListener listener;

    public SalesSearchAdapter(Context context) {
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
        return new Holder(inflater.inflate(R.layout.activity_sales_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ListItemEntity listItemEntity = list.get(position);
        StringBuffer buffer = new StringBuffer();
        buffer.append(listItemEntity.getCustomer_name());
        String content;
        switch (searchType) {
            case SalesSearchActivity.SEARCH_PHONE_TYPE:
                content = listItemEntity.getEncode_phone();
                break;
            case SalesSearchActivity.SEARCH_GROUPID_TYPE:
                content = listItemEntity.getCustomer_id();
                break;
            case SalesSearchActivity.SEARCH_ORDER_TYPE:
                content = listItemEntity.getOrder_num();
                break;
            default:
                content = listItemEntity.getEncode_phone();
                break;
        }
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(keyword)) {
                String newText = "<font color='#00B6EB'>" + keyword + "</font>";
                content = content.replace(keyword, newText);
            }
            buffer.append(" | " + content);
        }
        holder.sales_search_item_name_tv.setText(Html.fromHtml(buffer.toString()));
        holder.sales_search_item_data_kvl.setData(listItemEntity.getShow_array());
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
        private TextView sales_search_item_name_tv;
        private KeyValueLayout sales_search_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            sales_search_item_name_tv = itemView.findViewById(R.id.sales_search_item_name_tv);
            sales_search_item_data_kvl = itemView.findViewById(R.id.sales_search_item_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListItemEntity listItemEntity);
    }
}
