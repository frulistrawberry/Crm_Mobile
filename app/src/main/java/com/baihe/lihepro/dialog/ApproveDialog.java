package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.ApproveEntity;
import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ApproveDialog extends Dialog {

    private List<ApproveEntity> approveList;
    private String hallName;
    private String date;
    private String sType;
    private String tag = "";
    private boolean isMulti;
    private String startDate;
    private String startType;
    private String endDate;
    private String endType;
    RecyclerView recyclerView;
    TextView tv_hall_name;
    TextView tv_date;
    TextView tv_type;
    ImageView iv_close;
    TextView iv_tag;
    View v_divider;


    public ApproveDialog(@NonNull Context context,List<ApproveEntity> approveList,String hallName,String date,String sType,
                         String endDate,String endType,String startDate,String startType,boolean isMulti) {
        super(context, R.style.CommonDialog);
        this.approveList = approveList;
        this.hallName = hallName;
        this.date = date;
        this.sType = sType;
        this.endDate = endDate;
        this.startDate = startDate;
        this.endType = endType;
        this.isMulti = isMulti;
        this.startType = startType;

    }

    public ApproveDialog(@NonNull Context context,List<ApproveEntity> approveList,String hallName,
                         String date,String sType,String tag,String endDate,String endType,String startDate,String startType,boolean isMulti) {
        super(context, R.style.CommonDialog);
        this.approveList = approveList;
        this.hallName = hallName;
        this.date = date;
        this.sType = sType;
        this.endDate = endDate;
        this.startDate = startDate;
        this.endType = endType;
        this.isMulti = isMulti;
        this.startType = startType;
        this.tag = tag;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_approve_list);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setWindowAnimations(R.style.dialog_style);
        window.setGravity(Gravity.BOTTOM);
        initView();
        listener();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_date = findViewById(R.id.tv_date);
        tv_type = findViewById(R.id.tv_type);
        iv_close = findViewById(R.id.btn_close);
        iv_tag = findViewById(R.id.iv_tag);
        v_divider = findViewById(R.id.v_divider);

        tv_hall_name.setText(hallName);
        if (isMulti){
            tv_date.setText(startDate.replace("年",".")
                    .replace("月",".").replace("日","")+startType+"-"+endDate.replace("年",".")
                    .replace("月",".").replace("日","")+endType);
            tv_type.setVisibility(View.GONE);
            v_divider.setVisibility(View.GONE);
        }else {
            tv_date.setText(date);
            tv_type.setText(sType);
        }

        if (TextUtils.isEmpty(tag)){
            iv_tag.setVisibility(View.GONE);
        }else {
            if ("原".equals(tag)){
                iv_tag.setBackgroundResource(R.drawable.cirlce_yellow);
                iv_tag.setTextColor(Color.parseColor("#E38E0C"));
            }else {
                iv_tag.setBackgroundResource(R.drawable.circle_blue_bg);
                iv_tag.setTextColor(Color.parseColor("#2DB4E6"));
            }
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setText(tag);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new BaseQuickAdapter<ApproveEntity,BaseViewHolder>(R.layout.item_approve_list,approveList) {

            @Override
            protected void convert(BaseViewHolder helper, ApproveEntity item) {

                helper.setVisible(R.id.v_divider1,true);
                helper.setVisible(R.id.v_divider2,true);
                helper.setVisible(R.id.v_divider3,true);
                helper.setVisible(R.id.v_divider4,true);
                helper.setVisible(R.id.iv_bg,false);
                if (item.getApproveStatus().contains("审核驳回")){
                    helper.setImageResource(R.id.iv_point,R.drawable.circle_red_stroke);
                    helper.setTextColor(R.id.tv_content, Color.parseColor("#F13C3C"));
                }else {
                    helper.setImageResource(R.id.iv_point,R.drawable.circle_blue_stroke);
                    helper.setTextColor(R.id.tv_content, Color.parseColor("#909090"));


                }
                helper.setTextColor(R.id.tv_date,Color.parseColor("#909090"));

                if (helper.getAdapterPosition() == 0){
                    helper.setVisible(R.id.v_divider1,false);
                    helper.setVisible(R.id.iv_bg,true);
                    if (item.getApproveStatus().contains("审核驳回")){
                        helper.setImageResource(R.id.iv_bg,R.drawable.circle_red_bg);
                        helper.setImageResource(R.id.iv_point,R.drawable.circle_red_solid);
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#F13C3C"));

                    }else {
                        helper.setImageResource(R.id.iv_bg,R.drawable.circle_blue_bg);
                        helper.setImageResource(R.id.iv_point,R.drawable.circle_blue_solid);
                        helper.setTextColor(R.id.tv_content, Color.parseColor("#4A4C5C"));

                    }

                    helper.setTextColor(R.id.tv_date, Color.parseColor("#4A4C5C"));



                }

                if (helper.getAdapterPosition() == approveList.size()-1){
                    helper.setVisible(R.id.v_divider2,false);
                    helper.setVisible(R.id.v_divider4,false);
                }

                helper.setText(R.id.tv_content,item.getApproveStatus());
                helper.setText(R.id.tv_date, TextUtils.isEmpty(item.getDateTime())?"":item.getDateTime());


            }
        });

    }



    private void listener() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
