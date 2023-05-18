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
import com.baihe.lihepro.entity.ContactDataEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-03
 * Description：
 */
public class SalesDetailContractAdapter extends RecyclerView.Adapter<SalesDetailContractAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;

    private List<ContactDataEntity> contractEntites;

    private OnItemClickListener listener;

    public SalesDetailContractAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.contractEntites = new ArrayList<>();
    }

    public void updateData(List<ContactDataEntity> contractInfos) {
        this.contractEntites.clear();
        if (contractInfos != null) {
            this.contractEntites.addAll(contractInfos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_sales_detail_constract_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ContactDataEntity contactDataEntity = contractEntites.get(position);
        int labelNum = 0;
        if (contactDataEntity.getTitle() != null) {
            holder.constact_item_name_tv.setText(contactDataEntity.getTitle().getKey() + contactDataEntity.getTitle().getVal());
            if (!TextUtils.isEmpty(contactDataEntity.getTitle().getAudit_text())) {
                labelNum = 1;
            }
        } else {
            holder.constact_item_name_tv.setText("");
        }
        if ("1".equals(contactDataEntity.getTitle().getEdit_contract())){
            holder.editTagTv.setVisibility(View.VISIBLE);
        }else {
            holder.editTagTv.setVisibility(View.GONE);
        }
        final int finalLabelNum = labelNum;
        final boolean canEdit = "1".equals(contactDataEntity.getTitle().getEdit_contract());
        holder.constact_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return finalLabelNum;
            }

            @Override
            public String getLabelText(int position) {
                return contactDataEntity.getTitle().getAudit_text();
            }

            @Override
            public int getLabelTextColor(int position) {
                String color = contactDataEntity.getTitle().getAudit_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        return Color.parseColor(color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return super.getLabelTextColor(position);
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_stroke_drawable);
                String color = contactDataEntity.getTitle().getAudit_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        labelDrawable.setStroke(CommonUtils.dp2pxForInt(context, 0.5F), Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return labelDrawable;
            }
        });
        holder.constact_item_data_kvl.setData(contactDataEntity.getShow_array());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (TextUtils.isEmpty(contactDataEntity.getAgreement_num())) {
                        listener.contractDetail(contactDataEntity.getContract_id());
                    } else {

                        if ("1".equals(contactDataEntity.getCategory()))
                            listener.deskAgreementDetail(contactDataEntity.getContract_id(), contactDataEntity.getAgreement_num(),canEdit);
                        else {
                            listener.agreementDetail(contactDataEntity.getContract_id(), contactDataEntity.getAgreement_num(),canEdit);

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contractEntites.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView constact_item_name_tv;
        private FlowLayout constact_item_label_fl;
        private KeyValueLayout constact_item_data_kvl;
        private TextView editTagTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            constact_item_name_tv = itemView.findViewById(R.id.constact_item_name_tv);
            constact_item_label_fl = itemView.findViewById(R.id.constact_item_label_fl);
            constact_item_data_kvl = itemView.findViewById(R.id.constact_item_data_kvl);
            editTagTv = itemView.findViewById(R.id.tv_edit);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void contractDetail(String contractId);

        void agreementDetail(String contractId, String agreementNum,boolean canEdit);

        void deskAgreementDetail(String contractId,String agreementNum,boolean canEdit);
    }
}
