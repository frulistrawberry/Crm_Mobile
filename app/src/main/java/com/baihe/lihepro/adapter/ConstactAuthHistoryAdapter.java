package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.AuthHistoryEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-02
 * Description：
 */
public class ConstactAuthHistoryAdapter extends RecyclerView.Adapter<ConstactAuthHistoryAdapter.Holder> {
    private final static int TOP_TYPE = 1;
    private final static int MIDDLE_TYPE = 2;
    private final static int BOTTOM_TYPE = 3;
    private final static int ONLY_TYPE = 4;

    private List<AuthHistoryEntity> list;
    private LayoutInflater inflater;
    private Context context;
    private LookListener lookListener;

    public ConstactAuthHistoryAdapter(Context context, List<AuthHistoryEntity> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1) {
            return ONLY_TYPE;
        } else if (position == 0) {
            return TOP_TYPE;
        } else if (position == getItemCount() - 1) {
            return BOTTOM_TYPE;
        } else {
            return MIDDLE_TYPE;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ONLY_TYPE) {
            return new Holder(inflater.inflate(R.layout.activity_contract_auth_history_item_only, parent, false));
        } else if (viewType == TOP_TYPE) {
            return new Holder(inflater.inflate(R.layout.activity_contract_auth_history_item_top, parent, false));
        } else if (viewType == BOTTOM_TYPE) {
            return new Holder(inflater.inflate(R.layout.activity_contract_auth_history_item_bottom, parent, false));
        } else {
            return new Holder(inflater.inflate(R.layout.activity_contract_auth_history_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int itemOffset = CommonUtils.dp2pxForInt(context, 24);
        int itemMargin = CommonUtils.dp2pxForInt(context, 20);
        LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) holder.contract_auth_history_item_title_ll.getLayoutParams();
        LinearLayout.LayoutParams bottomParams = (LinearLayout.LayoutParams) holder.contract_auth_history_item_content_ll.getLayoutParams();
        if (position == 0 && position == getItemCount() - 1) {
            titleParams.topMargin = itemMargin;
            bottomParams.bottomMargin = itemMargin;
        } else if (position == 0) {
            titleParams.topMargin = itemMargin;
            bottomParams.bottomMargin = itemOffset;
        } else if (position == getItemCount() - 1) {
            bottomParams.bottomMargin = itemMargin;
        } else {
            bottomParams.bottomMargin = itemOffset;
        }
        final AuthHistoryEntity authHistoryEntity = list.get(position);
        holder.contract_auth_history_item_name_tv.setText(authHistoryEntity.getUser_name());
        holder.contract_auth_history_item_time_tv.setText(authHistoryEntity.getAudit_time());
        holder.contract_auth_history_item_content_tv.setText(authHistoryEntity.getStatus_txt());
        //没时间表示未审核
        if (TextUtils.isEmpty(authHistoryEntity.getAudit_time()) || TextUtils.isEmpty(authHistoryEntity.getAudit_time().trim())) {
            holder.contract_auth_history_item_name_tv.setTextColor(Color.parseColor("#4A4C5C"));
            holder.contract_auth_history_item_time_tv.setTextColor(Color.parseColor("#4A4C5C"));
            holder.contract_auth_history_item_content_tv.setTextColor(Color.parseColor("#4A4C5C"));
            holder.contract_auth_history_item_icon_iv.setImageResource(R.drawable.time_point);
        } else {
            holder.contract_auth_history_item_name_tv.setTextColor(Color.parseColor("#C5C5CE"));
            holder.contract_auth_history_item_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            holder.contract_auth_history_item_content_tv.setTextColor(Color.parseColor("#C5C5CE"));
            holder.contract_auth_history_item_icon_iv.setImageResource(R.drawable.time_point2);
        }
        boolean isZero = false;
        try {
            isZero = Double.parseDouble(authHistoryEntity.getPreferential_amount()) == 0.0D;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("3".equals(authHistoryEntity.getStatus()) && (!TextUtils.isEmpty(authHistoryEntity.getRemark()) || (!isZero && !TextUtils.isEmpty(authHistoryEntity.getPreferential_amount())))) {   //审核通过
            holder.contract_auth_history_item_see_ll.setVisibility(View.VISIBLE);
            holder.contract_auth_history_item_see_tv.setText("查看审批意见");
        } else if ("2".equals(authHistoryEntity.getStatus())) {   //审核驳回
            holder.contract_auth_history_item_see_ll.setVisibility(View.VISIBLE);
            holder.contract_auth_history_item_see_tv.setText("查看驳回原因");
        } else {
            holder.contract_auth_history_item_see_ll.setVisibility(View.GONE);
        }
        holder.contract_auth_history_item_see_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lookListener == null) {
                    return;
                }
                if ("3".equals(authHistoryEntity.getStatus())) {
                    lookListener.look(true, authHistoryEntity.getRemark(), authHistoryEntity.getAudit_time(), authHistoryEntity.getUser_name(), authHistoryEntity.getPreferential_amount());
                } else if ("2".equals(authHistoryEntity.getStatus())) {
                    lookListener.look(false, authHistoryEntity.getRemark(), authHistoryEntity.getAudit_time(), authHistoryEntity.getUser_name(), authHistoryEntity.getPreferential_amount());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLookListener(LookListener lookListener) {
        this.lookListener = lookListener;
    }

    /**
     * 是否审核
     *
     * @param position
     * @return
     */
    public boolean isApprove(int position) {
        AuthHistoryEntity authHistoryEntity = list.get(position);
        if (TextUtils.isEmpty(authHistoryEntity.getAudit_time()) || TextUtils.isEmpty(authHistoryEntity.getAudit_time().trim())) {
            return false;
        }
        return true;
    }

    public interface LookListener {
        void look(boolean isPass, String remark, String time, String user, String price);
    }

    public class Holder extends RecyclerView.ViewHolder {
        public LinearLayout contract_auth_history_item_title_ll;
        public ImageView contract_auth_history_item_icon_iv;
        public TextView contract_auth_history_item_name_tv;
        public TextView contract_auth_history_item_time_tv;
        public LinearLayout contract_auth_history_item_content_ll;
        public TextView contract_auth_history_item_content_tv;
        public LinearLayout contract_auth_history_item_see_ll;
        public TextView contract_auth_history_item_see_tv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            contract_auth_history_item_title_ll = itemView.findViewById(R.id.contract_auth_history_item_title_ll);
            contract_auth_history_item_icon_iv = itemView.findViewById(R.id.contract_auth_history_item_icon_iv);
            contract_auth_history_item_name_tv = itemView.findViewById(R.id.contract_auth_history_item_name_tv);
            contract_auth_history_item_time_tv = itemView.findViewById(R.id.contract_auth_history_item_time_tv);
            contract_auth_history_item_content_ll = itemView.findViewById(R.id.contract_auth_history_item_content_ll);
            contract_auth_history_item_content_tv = itemView.findViewById(R.id.contract_auth_history_item_content_tv);
            contract_auth_history_item_see_ll = itemView.findViewById(R.id.contract_auth_history_item_see_ll);
            contract_auth_history_item_see_tv = itemView.findViewById(R.id.contract_auth_history_item_see_tv);
        }
    }
}
