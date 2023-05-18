package com.baihe.lihepro.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ScheduleBookActivity;
import com.baihe.lihepro.activity.ScheduleMultiBookActivity;
import com.baihe.lihepro.activity.ScheduleMultiReserveActivity;
import com.baihe.lihepro.activity.ScheduleReserveActivity;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.calendarnew.ReserveHallInfo;
import com.j256.ormlite.stmt.query.In;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public  class MultiScheduleDialog extends Dialog implements View.OnClickListener{
    TextView tv_date1;
    TextView tv_date2;
    TextView btn_cancel;
    TextView option_lunch2;
    TextView option_lunch1;
    TextView option_dinner1;
    TextView option_dinner2;
    TextView btn_reserve;
    TextView btn_book;
    RecyclerView rv_hall;
    private List<ReserveHallInfo> mData;
    private String date1;
    private String date2;
    private HallAdapter adapter;
    private int startType = -1;
    private int endType = -1;
    private ReserveHallInfo selectHall;
    private boolean isForReset;
    Fragment fragment;
    Activity activity;


    public MultiScheduleDialog(@NonNull Activity context, String date1, String date2, List<ReserveHallInfo> data, Fragment fragment,boolean isForReset) {
        super(context, R.style.CommonDialog);
        this.mData = data;
        this.date1 = date1;
        this.date2 = date2;
        this.fragment = fragment;
        this.isForReset = isForReset;
        this.activity = context;
        setContentView(R.layout.dialog_multi_schedule);
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialog_style);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
        window.setAttributes(lp);

        tv_date1 = findViewById(R.id.tv_date1);
        tv_date2 = findViewById(R.id.tv_date2);
        btn_cancel = findViewById(R.id.btn_cancel);
        option_lunch1 = findViewById(R.id.option_lunch1);
        option_lunch2 = findViewById(R.id.option_lunch2);
        option_dinner1 = findViewById(R.id.option_dinner1);
        option_dinner2 = findViewById(R.id.option_dinner2);
        btn_reserve = findViewById(R.id.btn_reserve);
        btn_book = findViewById(R.id.btn_book);
        rv_hall = findViewById(R.id.rv_hall);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            tv_date1.setText(sdf.format(sdf1.parse(date1)));
            tv_date2.setText(sdf.format(sdf1.parse(date2)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btn_cancel.setOnClickListener(this);
        option_lunch1.setOnClickListener(this);
        option_lunch2.setOnClickListener(this);
        option_dinner1.setOnClickListener(this);
        option_dinner2.setOnClickListener(this);
        btn_reserve.setOnClickListener(this);
        btn_book.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rv_hall.setLayoutManager(manager);
        mData.get(0).setSelect(true);
        selectHall = mData.get(0);
        adapter = new HallAdapter(getContext(),mData);
        adapter.setListener(new HallAdapter.OnSelectListener() {
            @Override
            public void onSelect(ReserveHallInfo hall) {
                selectHall = hall;
                initStartOptions();
                initEndOptions();
                if (startType!=-1){
                    if (selectHall.getStartLunchStatus() == 0 && startType == 1){
                        selectOptions(option_lunch1);
                    }else if (selectHall.getStartDinnerStatus() == 0 && startType == 2){
                        selectOptions(option_dinner1);
                    }
                }

                if (endType!=-1){
                    if (selectHall.getEndLunchStatus() == 0 && endType == 1){
                        selectOptions(option_lunch2);
                    }else if (selectHall.getEndDinnerStatus() == 0 && endType == 2){
                        selectOptions(option_dinner2);
                    }
                }

            }
        });
        rv_hall.setAdapter(adapter);
        initStartOptions();
        initEndOptions();
        selectDef();
        if (isForReset){
            btn_reserve.setVisibility(View.GONE);
            btn_book.setText("确定");
        }

    }



    private void initStartOptions(){

        if (selectHall.getStartDinnerStatus()!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_dinner1.setVisibility(View.VISIBLE);
            if (selectHall.getStartDinnerStatus() == 0){
                option_dinner1.setTextColor(Color.parseColor("#4A4C5C"));
                option_dinner1.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_dinner1.setTextColor(Color.parseColor("#AEAEBC"));
                option_dinner1.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_dinner1.setVisibility(View.GONE);
        }

        if (selectHall.getStartLunchStatus()!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_lunch1.setVisibility(View.VISIBLE);
            if (selectHall.getStartLunchStatus() == 0){
                option_lunch1.setTextColor(Color.parseColor("#4A4C5C"));
                option_lunch1.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_lunch1.setTextColor(Color.parseColor("#AEAEBC"));
                option_lunch1.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_lunch1.setVisibility(View.GONE);
        }




    }

    private void initEndOptions(){

        if (selectHall.getEndDinnerStatus()!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_dinner2.setVisibility(View.VISIBLE);
            if (selectHall.getEndDinnerStatus() == 0){
                option_dinner2.setTextColor(Color.parseColor("#4A4C5C"));
                option_dinner2.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_dinner2.setTextColor(Color.parseColor("#AEAEBC"));
                option_dinner2.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_dinner2.setVisibility(View.GONE);
        }

        if (selectHall.getEndLunchStatus()!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_lunch2.setVisibility(View.VISIBLE);
            if (selectHall.getEndLunchStatus() == 0){
                option_lunch2.setTextColor(Color.parseColor("#4A4C5C"));
                option_lunch2.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_lunch2.setTextColor(Color.parseColor("#AEAEBC"));
                option_lunch2.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            option_lunch2.setVisibility(View.GONE);
        }




    }

    private void selectOptions(TextView v){
        v.setBackgroundResource(R.drawable.bg_option_select);
        v.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_lunch1:
                if (selectHall.getStartLunchStatus()==0){
                    initStartOptions();
                    selectOptions((TextView) view);
                    startType = 1;
                }
                break;
            case R.id.option_dinner1:
                if (selectHall.getStartDinnerStatus()==0){
                    initStartOptions();
                    selectOptions((TextView) view);
                    startType = 2;
                }
                break;
                case R.id.option_lunch2:
                if (selectHall.getEndLunchStatus()==0){
                    initEndOptions();
                    selectOptions((TextView) view);
                    endType = 1;
                }
                break;
            case R.id.option_dinner2:
                if (selectHall.getEndDinnerStatus()==0){
                    initEndOptions();
                    selectOptions((TextView) view);
                    endType = 2;
                }
                break;
            case R.id.btn_cancel:
                startType = -1;
                endType = -1;
                dismiss();
                break;
            case R.id.btn_reserve:
                if (startType == -1){
                    ToastUtils.toast("请选择档期开始日期");
                    return;
                }

                if (endType == -1){
                    ToastUtils.toast("请选择档期结束日期");
                    return;
                }

                if (selectHall == null){
                    ToastUtils.toast("请选择宴会厅");
                    return;
                }
                ScheduleMultiReserveActivity.start(fragment,selectHall.getHallId()+"",selectHall.getName(),date1,date2,startType,endType);
                break;
            case R.id.btn_book:
                    if (startType == -1){
                        ToastUtils.toast("请选择档期开始日期");
                        return;
                    }

                    if (endType == -1){
                        ToastUtils.toast("请选择档期结束日期");
                        return;
                    }

                    if (selectHall == null){
                        ToastUtils.toast("请选择宴会厅");
                        return;
                    }

                    if (isForReset){
                        Intent intent = new Intent();
                        intent.putExtra("startType",startType);
                        intent.putExtra("endType",endType);
                        intent.putExtra("hallId",selectHall.getHallId());
                        intent.putExtra("hallName",selectHall.getName());
                        intent.putExtra("startDate",date1);
                        intent.putExtra("endDate",date2);
                        intent.putExtra("isMulti",1);
                        activity.setResult(Activity.RESULT_OK,intent);
                        activity.finish();
                    }else {
                        ScheduleMultiBookActivity.start(fragment,selectHall.getHallId()+"",selectHall.getName(),date1,date2,startType,endType);
                    }

                break;
        }



    }






    public void selectDef(){
        if (selectHall.getStartLunchStatus() == 0){
            selectOptions(option_lunch1);
            startType = 1;
        }else if (selectHall.getStartDinnerStatus() == 0){
            selectOptions(option_dinner1);
            startType = 2;
        }

        if (selectHall.getEndDinnerStatus() == 0){
            selectOptions(option_dinner2);
            endType =2;
        }else if (selectHall.getEndLunchStatus() == 0){
            selectOptions(option_lunch2);
            endType = 1;
        }
    }

    public static class HallAdapter extends RecyclerView.Adapter<Holder>{
        private List<ReserveHallInfo> mData;
        private Context context;
        private OnSelectListener listener;


        public OnSelectListener getListener() {
            return listener;
        }

        public void setListener(OnSelectListener listener) {
            this.listener = listener;
        }

        public  interface OnSelectListener{
            void onSelect(ReserveHallInfo hall);
        }

        public HallAdapter(Context context,List<ReserveHallInfo> mData) {
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
            ReserveHallInfo item = mData.get(position);
            if (item.isSelect()){
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_select);
                holder.option_hall.setTextColor(Color.WHITE);
            }else if (item.isOK()){
                holder.option_hall.setTextColor(Color.parseColor("#4A4C5C"));
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                holder.option_hall.setTextColor(Color.parseColor("#AEAEBC"));
                holder.option_hall.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
            holder.option_hall.setText(item.getName());
            holder.option_hall.setOnClickListener(view -> {
                if (item.isOK()){
                    for (ReserveHallInfo mDatum : mData) {
                        mDatum.setSelect(false);
                    }
                    item.setSelect(true);
                    if (listener != null) {
                        listener.onSelect(item);
                    }

                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
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
