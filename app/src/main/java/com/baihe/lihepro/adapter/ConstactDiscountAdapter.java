package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.AuthHistoryEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-02
 * Description：
 */
public class ConstactDiscountAdapter extends RecyclerView.Adapter<ConstactDiscountAdapter.Holder> {
    private List<List<KeyValueEntity>> list;
    private LayoutInflater inflater;

    public ConstactDiscountAdapter(Context context, List<List<KeyValueEntity>> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_contract_discount_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        List<KeyValueEntity> data = list.get(position);
        holder.contract_discount_item_kvl.setData(data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public KeyValueLayout contract_discount_item_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            contract_discount_item_kvl = itemView.findViewById(R.id.contract_discount_item_kvl);
        }
    }
}
