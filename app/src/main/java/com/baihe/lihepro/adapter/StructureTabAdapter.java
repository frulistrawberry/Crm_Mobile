package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.structure.RoleEntity;
import com.baihe.lihepro.entity.structure.StructureBaseEntity;
import com.baihe.lihepro.entity.structure.StructureEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-07
 * Description：
 */
public class StructureTabAdapter extends RecyclerView.Adapter<StructureTabAdapter.Holder> {
    private List<StructureBaseEntity> tabList = new ArrayList<>();
    private LayoutInflater inflater;
    private String companyName;

    public OnItemClickListener listener;

    public StructureTabAdapter(Context context, String companyName) {
        this.companyName = companyName;
        this.inflater = LayoutInflater.from(context);
    }

    public void add(StructureBaseEntity structureBaseEntity) {
        tabList.add(structureBaseEntity);
        notifyDataSetChanged();
    }

    public StructureBaseEntity getCurrentTab() {
        if (getItemCount() == 1) {
            return null;
        } else {
            return tabList.get(getItemCount() - 2);
        }
    }

    public void selectPosition(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            int removeSize = getItemCount() - 1 - position;
            for (int i = 0; i < removeSize; i++) {
                int lastIndex = tabList.size() - 1;
                tabList.remove(lastIndex);
            }
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_customer_structure_list_tab_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        if (position == 0) {
            holder.customer_structure_list_tab_item_name_tv.setText(companyName);
        } else {
            StructureBaseEntity structureBaseEntity = tabList.get(position - 1);
            if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
                StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
                holder.customer_structure_list_tab_item_name_tv.setText(structureEntity.getName());
            } else if (structureBaseEntity.getType() == StructureBaseEntity.Type.ROLE) {
                RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
                holder.customer_structure_list_tab_item_name_tv.setText(roleEntity.getName());
            }
        }
        if (position == getItemCount() - 1) {
            holder.customer_structure_list_tab_item_right_iv.setVisibility(View.GONE);
            holder.customer_structure_list_tab_item_name_tv.setTextColor(Color.parseColor("#8B8B99"));
            holder.customer_structure_list_tab_item_name_tv.setOnClickListener(null);
        } else {
            holder.customer_structure_list_tab_item_right_iv.setVisibility(View.VISIBLE);
            holder.customer_structure_list_tab_item_name_tv.setTextColor(Color.parseColor("#2DB4E6"));
            holder.customer_structure_list_tab_item_name_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) {
                        return;
                    }
                    if (position == 0) {
                        listener.onItemClick(position, null);
                    } else {
                        listener.onItemClick(position, tabList.get(position - 1));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tabList.size() + 1;
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView customer_structure_list_tab_item_name_tv;
        public ImageView customer_structure_list_tab_item_right_iv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            customer_structure_list_tab_item_name_tv = itemView.findViewById(R.id.customer_structure_list_tab_item_name_tv);
            customer_structure_list_tab_item_right_iv = itemView.findViewById(R.id.customer_structure_list_tab_item_right_iv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, StructureBaseEntity structureBaseEntity);
    }
}
