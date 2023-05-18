package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.MenuEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ApproveEntity> list;

    private OnItemClickListener listener;

    public ApproveAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<ApproveEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_home_approve_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ApproveEntity approveEntity = list.get(position);
        holder.home_approve_item_name_tv.setText(approveEntity.getTitle());
        holder.home_approve_item_time_tv.setText(approveEntity.getAudit_time());

        if ("1".equals(approveEntity.getAudit_type())){
            if (approveEntity.getAmount() != null) {
                KeyValueEntity keyValueEntity = approveEntity.getAmount();
                StringBuffer buffer = new StringBuffer();
                buffer.append(keyValueEntity.getKey());
                buffer.append(": ¥");
                buffer.append(keyValueEntity.getVal());
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2DB4E6"));
                String content = buffer.toString();
                SpannableString span = new SpannableString(content);
                span.setSpan(colorSpan, content.length() - keyValueEntity.getVal().length() - 1, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.home_approve_item_income_tv.setText(span);
            } else {
                holder.home_approve_item_income_tv.setText("");
            }
        }else {
            if (approveEntity.getSchedule()!=null){
                KeyValueEntity keyValueEntity = approveEntity.getSchedule();
                StringBuffer buffer = new StringBuffer();
                buffer.append(keyValueEntity.getKey()).append(":").append(keyValueEntity.getVal());
                holder.home_approve_item_income_tv.setText(buffer);
            }else {
                holder.home_approve_item_income_tv.setText("");
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.approve(approveEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView home_approve_item_name_tv;
        private LinearLayout home_approve_item_go_ll;
        private TextView home_approve_item_income_tv;
        private TextView home_approve_item_time_tv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            home_approve_item_name_tv = itemView.findViewById(R.id.home_approve_item_name_tv);
            home_approve_item_go_ll = itemView.findViewById(R.id.home_approve_item_go_ll);
            home_approve_item_income_tv = itemView.findViewById(R.id.home_approve_item_income_tv);
            home_approve_item_time_tv = itemView.findViewById(R.id.home_approve_item_time_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void approve(ApproveEntity approveEntity);
    }

}
