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
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.filter.entity.FilterRegionEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class SelectCity2Adapter extends RecyclerView.Adapter<SelectCity2Adapter.Holder> {
    private List<CityEntity> all;
    private List<CityEntity> select;
    private LayoutInflater inflater;

    public SelectCity2Adapter(List<CityEntity> all, List<CityEntity> select, Context context) {
        this.all = all;
        this.select = select;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelect(List<CityEntity> select) {
        this.select = select;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_filter_city_select_item, parent, false);
        SelectCity2Adapter.Holder holder = new SelectCity2Adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        CityEntity entity = select.get(position);
        holder.filter_city_select_name_tv.setText(entity.getName());
        holder.filter_city_select_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return select == null ? 0 : select.size();
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
