package com.baihe.lihepro.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.Md5Utils;
import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ScheduleBookActivity;
import com.baihe.lihepro.activity.ScheduleReserveActivity;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.HallItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleScheduleDialog extends Dialog implements View.OnClickListener {
    TextView tv_date;
    TextView btn_cancel;
    TextView option_lunch;
    TextView option_dinner;
    TextView btn_reserve;
    TextView btn_book;
    RecyclerView rv_hall;
    private List<HallItem> mData;
    private String date;
    private HallAdapter adapter;
    private int sType = -1;
    private boolean isForReset;
    private Activity activity;


    public SingleScheduleDialog(@NonNull Activity context,String date,List<HallItem> data,boolean isForReset) {
        super(context, R.style.CommonDialog);
        this.activity = context;
        this.mData = data;
        this.date = date;
        this.isForReset = isForReset;
        setContentView(R.layout.dialog_single_schedule);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialog_style);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
        window.setAttributes(lp);



        tv_date = findViewById(R.id.tv_date);
        btn_cancel = findViewById(R.id.btn_cancel);
        option_lunch = findViewById(R.id.option_lunch);
        option_dinner = findViewById(R.id.option_dinner);
        btn_reserve = findViewById(R.id.btn_reserve);
        btn_book = findViewById(R.id.btn_book);
        rv_hall = findViewById(R.id.rv_hall);

        if (isForReset){
            btn_reserve.setVisibility(View.GONE);
            btn_book.setText("确定");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateTemp = sdf.parse(date);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            tv_date.setText(sdf1.format(dateTemp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btn_cancel.setOnClickListener(this);
        option_lunch.setOnClickListener(this);
        option_dinner.setOnClickListener(this);
        btn_reserve.setOnClickListener(this);
        btn_book.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rv_hall.setLayoutManager(manager);
        List<HallItem> temp = new ArrayList<>();
        List<HallItem> temp1 = new ArrayList<>();
        for (HallItem mDatum : mData) {
            if (mDatum.getDinnerStatus() == 0 || mDatum.getLunchStatus() == 0)
                temp.add(mDatum);
            else
                temp1.add(mDatum);
        }
        mData.clear();
        mData.addAll(temp);
        mData.addAll(temp1);
        adapter = new HallAdapter(getContext(),mData);
        rv_hall.setAdapter(adapter);
        initOptions();
    }


    /**
     *
     * @param lunchOrDinner 1 午宴 2 晚宴
     * @return 0 可用 1 不可用 -1 未设置
     */
    private int getStatus(int lunchOrDinner){
        int status = -1;
        if (lunchOrDinner == 1){
            //午宴
            for (HallItem mDatum : mData) {
                status = mDatum.getLunchStatus();
                if (mDatum.getLunchStatus() == 0){
                    break;
                }

            }
        }else if (lunchOrDinner == 2){
            for (HallItem mDatum : mData) {
                status = mDatum.getDinnerStatus();
                if (mDatum.getDinnerStatus() == 0){
                    break;
                }
            }
        }
        return status;
    }

    private void initOptions(){
        boolean singleStatus = false;
        if (getStatus(2)!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_dinner.setVisibility(View.VISIBLE);
            if (getStatus(2) == 0){
                option_dinner.setTextColor(Color.parseColor("#4A4C5C"));
                option_dinner.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_dinner.setTextColor(Color.parseColor("#AEAEBC"));
                option_dinner.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_dinner.setVisibility(View.GONE);
            singleStatus = true;
        }

        if (getStatus(1)!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_lunch.setVisibility(View.VISIBLE);
            if (getStatus(1) == 0){
                option_lunch.setTextColor(Color.parseColor("#4A4C5C"));
                option_lunch.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_lunch.setTextColor(Color.parseColor("#AEAEBC"));
                option_lunch.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_lunch.setVisibility(View.GONE);
            singleStatus = true;
        }

        if (singleStatus && getStatus(2)!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            selectOptions(option_dinner);
            adapter.setLunchOrDinner(2);
            sType = 2;
        }

        if (singleStatus && getStatus(1)!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            selectOptions(option_lunch);
            adapter.setLunchOrDinner(1);
            sType = 1;
        }

    }

    private void selectOptions(TextView v){
        v.setBackgroundResource(R.drawable.bg_option_select);
        v.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        HallItem selectItem;
        switch (view.getId()){
            case R.id.option_lunch:
                if (getStatus(1)==0){
                    initOptions();
                    selectOptions((TextView) view);
                    sType = 1;
                    adapter.setLunchOrDinner(1);
                }
                break;
            case R.id.option_dinner:
                if (getStatus(2)==0){
                    initOptions();
                    selectOptions((TextView) view);
                    sType = 2;
                    adapter.setLunchOrDinner(2);
                }
                break;
            case R.id.btn_cancel:
                sType = -1;
                dismiss();
                break;
            case R.id.btn_reserve:
                selectItem = getSelectItem();
                if (sType == -1){
                    ToastUtils.toast("请选择档期时段");
                    return;
                }
                if (selectItem == null){
                    ToastUtils.toast("请选择宴会厅");
                    return;
                }
                ScheduleReserveActivity.start( getContext(),selectItem.getId()+"",selectItem.getName(),date,new HallBookStatus(selectItem.getLunchStatus(),selectItem.getDinnerStatus()),sType);
                break;
            case R.id.btn_book:
                selectItem = getSelectItem();
                if (sType == -1){
                    ToastUtils.toast("请选择档期时段");
                    return;
                }
                if (selectItem == null){
                    ToastUtils.toast("请选择宴会厅");
                    return;
                }
                if (isForReset){
                    Intent intent = new Intent();
                    intent.putExtra("hallId",selectItem.getId()+"");
                    intent.putExtra("hallName",selectItem.getName());
                    intent.putExtra("date",date);
                    intent.putExtra("sType",sType);
                    intent.putExtra("isMulti",0);
                    activity.setResult(Activity.RESULT_OK,intent);
                    activity.finish();

                }else {
                    ScheduleBookActivity.start(getContext(),selectItem.getId()+"",selectItem.getName(),date,new HallBookStatus(selectItem.getLunchStatus(),selectItem.getDinnerStatus()),sType);
                }
                break;
        }



    }

    public HallItem getSelectItem(){
        for (HallItem mDatum : mData) {
            if (mDatum.isSelect())
                return mDatum;
        }
        return null;
    }

    public static class HallAdapter extends RecyclerView.Adapter<Holder>{
        private List<HallItem> mData;
        private int lunchOrDinner = -1;
        private Context context;

        public HallAdapter(Context context,List<HallItem> mData) {
            this.mData = mData;
            this.context = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_schedule_dialog_hall,parent,false);
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            HallItem item = mData.get(position);
            if (item.isSelect()){
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_select);
                holder.option_hall.setTextColor(Color.WHITE);
            }else if (checkOption(item)){
                holder.option_hall.setTextColor(Color.parseColor("#4A4C5C"));
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                holder.option_hall.setTextColor(Color.parseColor("#AEAEBC"));
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
            holder.option_hall.setText(item.getName());
            holder.option_hall.setOnClickListener(view -> {
                if (checkOption(item)){
                    for (HallItem mDatum : mData) {
                        mDatum.setSelect(false);
                    }
                    item.setSelect(true);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setLunchOrDinner(int lunchOrDinner){
            this.lunchOrDinner = lunchOrDinner;

            List<HallItem> temp = new ArrayList<>();
            List<HallItem> temp1 = new ArrayList<>();
            for (HallItem mDatum : mData) {
                mDatum.setSelect(false);
                if (lunchOrDinner == 1){
                    if (mDatum.getLunchStatus() == 0)
                        temp.add(mDatum);
                    else
                        temp1.add(mDatum);
                }else if (lunchOrDinner == 2){
                    if (mDatum.getDinnerStatus() == 0)
                        temp.add(mDatum);
                    else
                        temp1.add(mDatum);
                }

            }
            mData.clear();
            mData.addAll(temp);
            mData.addAll(temp1);
            notifyDataSetChanged();
        }

        private boolean checkOption(HallItem hall){
            return  (hall.getDinnerStatus()==0 && lunchOrDinner == 2) ||
                    (hall.getLunchStatus() == 0 && lunchOrDinner == 1) || (lunchOrDinner == 0 && (hall.getLunchStatus() == 0 || hall.getDinnerStatus() == 0));
        }
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public TextView option_hall;

        public Holder(@NonNull View itemView) {
            super(itemView);
            option_hall = itemView.findViewById(R.id.option_hall);
        }
    }
}
