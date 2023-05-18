package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.ListItemEntity;
import com.baihe.lihepro.entity.PayCodeEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class PayCodeListAdapter extends RecyclerView.Adapter<PayCodeListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<PayCodeEntity> list;

    private OnItemClickListener listener;

    public PayCodeListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<PayCodeEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<PayCodeEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<PayCodeEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_pay_code_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final PayCodeEntity listItemEntity = list.get(position);
        if ("3".equals(listItemEntity.getPayment_status())){
            holder.labelLayout.setText("已作废");
            holder.labelLayout.setTextColor(Color.parseColor("#8B8B99"));
            holder.labelLayout.setBackgroundResource(R.drawable.bg_destroy);
            holder.nameTv.setTextColor(Color.parseColor("#8B8B99"));
            holder.headIv.setAlpha(0.5f);
//            holder.listItemLayout.setKeyTextColor(Color.parseColor("#C5C5CE"));
//            holder.listItemLayout.setValueTextColor(Color.parseColor("#C5C5CE"));
            holder.listItemLayout.setAlpha(0.5f);

        }else if ("1".equals(listItemEntity.getPayment_status())){
            holder.labelLayout.setText("已支付");
            holder.labelLayout.setTextColor(Color.parseColor("#2DB4E6"));
            holder.labelLayout.setBackgroundResource(R.drawable.bg_not_pay);
            holder.nameTv.setTextColor(Color.parseColor("#4A4C5C"));
            holder.headIv.setAlpha(1f);
            holder.listItemLayout.setAlpha(1f);

//            holder.listItemLayout.setKeyTextColor(Color.parseColor("#4A4C5C"));
//            holder.listItemLayout.setValueTextColor(Color.parseColor("#4A4C5C"));
        }else {
            holder.labelLayout.setText("待支付");
            holder.labelLayout.setTextColor(Color.parseColor("#F096B3"));
            holder.labelLayout.setBackgroundResource(R.drawable.bg_pay);
            holder.nameTv.setTextColor(Color.parseColor("#4A4C5C"));
            holder.headIv.setAlpha(1f);
//            holder.listItemLayout.setKeyTextColor(Color.parseColor("#4A4C5C"));
//            holder.listItemLayout.setValueTextColor(Color.parseColor("#4A4C5C"));
            holder.listItemLayout.setAlpha(1f);
        }

        holder.nameTv.setText(TextUtils.isEmpty(listItemEntity.getCustomer_name())?"客户":listItemEntity.getCustomer_name());
        holder.listItemLayout.setData(listItemEntity.getShow_array());
        holder.listItemLayout.invalidate();
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
        private TextView nameTv;
        private KeyValueLayout listItemLayout;
        private TextView labelLayout;
        private ImageView headIv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.pay_code_list_item_name_tv);
            listItemLayout = itemView.findViewById(R.id.pay_code_list_item_data_kvl);
            labelLayout = itemView.findViewById(R.id.tv_tag);
            headIv = itemView.findViewById(R.id.pay_code_list_item_head_iv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(PayCodeEntity listitemEntity);
    }
}
