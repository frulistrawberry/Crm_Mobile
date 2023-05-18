package com.baihe.lihepro.filter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.entity.FilterKVEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class SelectMultilevelAdapter extends RecyclerView.Adapter<SelectMultilevelAdapter.Holder> {
    private List<FilterKVEntity> allKvEntities;
    private List<FilterKVEntity> dataList;
    private List<String> defaultSelectvales;
    private LayoutInflater inflater;


    public SelectMultilevelAdapter(List<FilterKVEntity> allParentKvEntities, List<String> defaultSelectvales, Context context, boolean isMultiple) {
        if (isMultiple) {
            this.allKvEntities = getList(allParentKvEntities);
        } else {
            this.allKvEntities = getAllList(allParentKvEntities);
        }
        this.defaultSelectvales = defaultSelectvales;
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
        if (defaultSelectvales != null) {
            for (FilterKVEntity filterKVEntity : allKvEntities) {
                if (defaultSelectvales.contains(filterKVEntity.item_val)) {
                    this.dataList.add(filterKVEntity);
                }
            }
        }
    }

    public List<FilterKVEntity> getList(List<FilterKVEntity> rootList) {
        if (rootList == null) {
            return new ArrayList<>();
        }
        List<FilterKVEntity> data = new ArrayList<>();
        for (FilterKVEntity root : rootList) {
            if (root.getChildren() != null && root.getChildren().size() > 0) {
                data.addAll(getList(root.getChildren()));
            } else {
                data.add(root);
            }
        }
        return data;
    }

    public List<FilterKVEntity> getAllList(List<FilterKVEntity> rootList) {
        if (rootList == null) {
            return new ArrayList<>();
        }
        List<FilterKVEntity> data = new ArrayList<>();
        for (FilterKVEntity root : rootList) {
            data.add(root);
            if (root.getChildren() != null && root.getChildren().size() > 0) {
                data.addAll(getAllList(root.getChildren()));
            }
        }
        return data;
    }

    public void setSelect(List<String> defaultSelectvales) {
        this.defaultSelectvales = defaultSelectvales;
        this.dataList.clear();
        if (defaultSelectvales != null) {
            for (FilterKVEntity filterKVEntity : allKvEntities) {
                if (defaultSelectvales.contains(filterKVEntity.item_val)) {
                    this.dataList.add(filterKVEntity);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_filter_city_select_item, parent, false);
        SelectMultilevelAdapter.Holder holder = new SelectMultilevelAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        FilterKVEntity entity = dataList.get(position);
        holder.filter_city_select_name_tv.setText(entity.item_key);
        holder.filter_city_select_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterKVEntity filterKVEntity = dataList.get(position);
                dataList.remove(position);
                defaultSelectvales.remove(filterKVEntity.item_val);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView filter_city_select_name_tv;
        ImageView filter_city_select_delete_iv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            filter_city_select_name_tv = itemView.findViewById(R.id.filter_city_select_name_tv);
            filter_city_select_delete_iv = itemView.findViewById(R.id.filter_city_select_delete_iv);
        }
    }
}
