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

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ApproveListActivity;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ApproveListAdapter extends RecyclerView.Adapter<ApproveListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ApproveEntity> list;
    private int sort = ApproveListActivity.SORT_AOOROVE_PENDING;

    private OnItemClickListener listener;

    public ApproveListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void setData(List<ApproveEntity> entities, int sort) {
        this.sort = sort;
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_approve_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ApproveEntity approveEntity = list.get(position);
        holder.approve_list_item_name_tv.setText(approveEntity.getTitle());
        holder.approve_list_item_dara_kvl.setData(approveEntity.getShow_array());
        if (sort == ApproveListActivity.SORT_AOOROVE_AUDITED) {
            holder.approve_list_item_label_fl.setVisibility(View.VISIBLE);
            holder.approve_list_item_go_tv.setVisibility(View.GONE);
            holder.approve_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
                @Override
                public int getSize() {
                    return TextUtils.isEmpty(approveEntity.getStatus_txt()) ? 0 : 1;
                }

                @Override
                public String getLabelText(int position) {
                    return approveEntity.getStatus_txt();
                }

                @Override
                public Drawable getLabelBackgroundDrawable(int position) {
                    GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_stroke_drawable);
                    String color = approveEntity.getAudit_color();
                    if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                        try {
                            labelDrawable.setStroke(CommonUtils.dp2pxForInt(context, 0.5F), Color.parseColor(color));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return labelDrawable;
                }

                @Override
                public int getLabelTextColor(int position) {
                    String color = approveEntity.getAudit_color();
                    if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                        try {
                            return Color.parseColor(color);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return super.getLabelTextColor(position);
                }
            });
        } else {
            holder.approve_list_item_label_fl.setVisibility(View.GONE);
            holder.approve_list_item_go_tv.setVisibility(View.VISIBLE);
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
        private TextView approve_list_item_name_tv;
        private FlowLayout approve_list_item_label_fl;
        private TextView approve_list_item_go_tv;
        private KeyValueLayout approve_list_item_dara_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            approve_list_item_name_tv = itemView.findViewById(R.id.approve_list_item_name_tv);
            approve_list_item_label_fl = itemView.findViewById(R.id.approve_list_item_label_fl);
            approve_list_item_go_tv = itemView.findViewById(R.id.approve_list_item_go_tv);
            approve_list_item_dara_kvl = itemView.findViewById(R.id.approve_list_item_dara_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void approve(ApproveEntity approveEntity);
    }

}
