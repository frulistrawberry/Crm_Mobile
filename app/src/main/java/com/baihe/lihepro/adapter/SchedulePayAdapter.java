package com.baihe.lihepro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.PayItem;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

public class SchedulePayAdapter extends RecyclerView.Adapter<SchedulePayAdapter.Holder>{
    private LayoutInflater inflater;
    private Context context;
    private List<PayItem> data;


    public SchedulePayAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public SchedulePayAdapter(Context context, List<PayItem> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<PayItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new Holder(inflater.inflate(R.layout.item_schedule_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PayItem item = data.get(position);
        List<KeyValueEntity> keyValueEntityList = new ArrayList<>();
        if (item.getAmount()!=null){
            KeyValueEntity entity1 = new KeyValueEntity("付款金额",item.getAmount()+"元");
            keyValueEntityList.add(entity1);
        }
        if (item.getPayType()!=null){
            KeyValueEntity entity2 = new KeyValueEntity("付款类型",item.getPayType());
            keyValueEntityList.add(entity2);
        }
        if (item.getPayMode()!=null){
            KeyValueEntity entity3 = new KeyValueEntity("付款方式",item.getPayMode());
            keyValueEntityList.add(entity3);
        }
        if (item.getPayStatus()!=null){
            KeyValueEntity entity4 = new KeyValueEntity("支付状态",item.getPayStatus());
            keyValueEntityList.add(entity4);
        }
        if (item.getDateTime()!=null){
            KeyValueEntity entity5 = new KeyValueEntity("支付时间",item.getDateTime());
            keyValueEntityList.add(entity5);
        }
        holder.keyValueLayout.setData(keyValueEntityList);

    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public KeyValueLayout keyValueLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            keyValueLayout = itemView.findViewById(R.id.kv_pay);

        }
    }
}
