package com.baihe.lihepro.adapter;

import android.app.Dialog;
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

import com.baihe.lihepro.R;
import com.baihe.lihepro.dialog.PersonSearchDialog;
import com.baihe.lihepro.dialog.PersonSearchDialog2;
import com.baihe.lihepro.dialog.PersonSelectDialog;
import com.baihe.lihepro.entity.RequirementEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class RequirementListAdapter extends RecyclerView.Adapter<RequirementListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<RequirementEntity> list;

    private RequirementSearchAdapter.OnItemClickListener listener;

    private  OnCustomerTransListener onCustomerTransListener;




    private boolean showDis;

    public RequirementListAdapter(Context context,boolean showDis) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
        this.showDis = showDis;
    }

    public void setOnCustomerTransListener(OnCustomerTransListener onCustomerTransListener) {
        this.onCustomerTransListener = onCustomerTransListener;
    }

    public void setData(List<RequirementEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<RequirementEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<RequirementEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_requirement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final RequirementEntity requirementEntity = list.get(position);
        holder.requirement_list_item_name_tv.setText(requirementEntity.getCustomer_name());
        holder.requirement_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return requirementEntity.getCategory_color().size();
            }

            @Override
            public String getLabelText(int position) {
                return requirementEntity.getCategory_color().get(position).getCategory_name();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = requirementEntity.getCategory_color().get(position).getCategory_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        labelDrawable.setColor(Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return labelDrawable;
            }
        });
        holder.requirement_list_item_data_kvl.setData(requirementEntity.getShow_array());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(requirementEntity);
                }
            }
        });

        if (showDis){
            holder.tv_dis.setVisibility(View.VISIBLE);
            holder.tv_dis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PersonSearchDialog2.Builder(context)
                            .setTitle("选择接收人").setOnConfirmClickListener(new PersonSearchDialog2.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, String customerName, String customerId) {
                            if (onCustomerTransListener != null) {
                                onCustomerTransListener.onTrans(requirementEntity.getCustomer_id(),customerId);
                            }

                        }
                    }).build().show();
                }
            });
        }else {
            holder.tv_dis.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView requirement_list_item_name_tv;
        private FlowLayout requirement_list_item_label_fl;
        private KeyValueLayout requirement_list_item_data_kvl;
        private TextView tv_dis;

        public Holder(@NonNull View itemView) {
            super(itemView);
            requirement_list_item_name_tv = itemView.findViewById(R.id.requirement_list_item_name_tv);
            requirement_list_item_label_fl = itemView.findViewById(R.id.requirement_list_item_label_fl);
            requirement_list_item_data_kvl = itemView.findViewById(R.id.requirement_list_item_data_kvl);
            tv_dis = itemView.findViewById(R.id.tv_dis);
        }
    }

    public void setOnItemClickListener(RequirementSearchAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(RequirementEntity requirementEntity);
    }

    public interface OnCustomerTransListener{
        void onTrans(String customerId,String ownerId);
    }
}
