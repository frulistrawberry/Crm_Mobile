package com.baihe.lihepro.adapter;

import android.graphics.Color;
import android.view.View;

import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.BookDetailActivity;
import com.baihe.lihepro.activity.ReserveDetailActivity;
import com.baihe.lihepro.entity.schedule.MyScheduleItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class ScheduleListAdapter extends BaseQuickAdapter<MyScheduleItem, BaseViewHolder> {

    private int type;
    public ScheduleListAdapter() {
        super(R.layout.fragment_my_schedule_list_item);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyScheduleItem item) {
        if (item.getIsMulti() ==1){
            helper.setText(R.id.tv_date,item.getStart_date()+(item.getStart_type()==1?" 午宴":" 晚宴")+" - "+item.getEnd_date()+(item.getEnd_type()==1?" 午宴":" 晚宴"));
        }else {
            helper.setText(R.id.tv_date,item.getDate()+(item.getsType()==1?" 午宴":" 晚宴"));
        }
        helper.setText(R.id.tv_hall_name,item.getHallName());
        helper.setText(R.id.tv_customer,item.getCustomerName()!=null?item.getCustomerName():"未填写");
        helper.setText(R.id.tv_status_name,type == 1?"预留状态：":"预订状态：");
        helper.setText(R.id.tv_date_name,type == 1?"预留至：":"预订时间：");
        helper.setText(R.id.tv_status,item.getStatus());
        if (item.getTextInfo()!=null)
            helper.setTextColor(R.id.tv_status, Color.parseColor(item.getTextInfo().getTextColor()));
        if (type == 1){
            helper.setText(R.id.tv_end_date,item.getEndTime());
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReserveDetailActivity.start(mContext,item.getId());
                }
            });
        }else {
            helper.setText(R.id.tv_end_date,item.getBookTime());
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.start(mContext,item.getId());
                }
            });
        }

    }
}
