package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.structure.MemberEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-07
 * Description：
 */
public class StructureSearchAdapter extends RecyclerView.Adapter<StructureSearchAdapter.Holder> {
    private List<MemberEntity> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public OnItemClickListener listener;

    public StructureSearchAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void updateData(List<MemberEntity> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_customer_structure_list_item_user, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final MemberEntity memberEntity = dataList.get(position);
        if (memberEntity.isSelect()) {
            holder.customer_structure_list_item_user_select_iv.setImageResource(R.drawable.check_icon);
        } else {
            holder.customer_structure_list_item_user_select_iv.setImageResource(R.drawable.unchecked_icon);
        }
        holder.customer_structure_list_item_user_name_tv.setText(memberEntity.getName());
        holder.customer_structure_list_item_user_data_kvl.setData(memberEntity.getOther());

        //选择逻辑
        holder.customer_structure_list_item_user_select_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberEntity.isSelect()) {
                    memberEntity.setSelect(false);
                } else {
                    memberEntity.setSelect(true);
                }
                notifyDataSetChanged();
                if (listener != null) {
                    listener.notifySelectText();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView customer_structure_list_item_user_select_iv;
        public TextView customer_structure_list_item_user_name_tv;
        public KeyValueLayout customer_structure_list_item_user_data_kvl;


        public Holder(@NonNull View itemView, int itemType) {
            super(itemView);
            customer_structure_list_item_user_select_iv = itemView.findViewById(R.id.customer_structure_list_item_user_select_iv);
            customer_structure_list_item_user_name_tv = itemView.findViewById(R.id.customer_structure_list_item_user_name_tv);
            customer_structure_list_item_user_data_kvl = itemView.findViewById(R.id.customer_structure_list_item_user_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void notifySelectText();
    }
}
