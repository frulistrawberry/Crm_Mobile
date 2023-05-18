package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.io.Serializable;
import java.util.List;

public class HallListActivity extends BaseActivity {
    private DrawerLayout filter_dl;
    private FrameLayout filter_content_fl;
    private RecyclerView recyclerView;
    private List<HallItem> hallItems;
    private TextView btn_ok;
    private HallItem selectItem;

    public static void start(Activity activity,List<HallItem> hallItems){
        Intent intent = new Intent(activity, HallListActivity.class);
        intent.putExtra("hallList", (Serializable) hallItems);
        activity.startActivityForResult(intent,105);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halls);
        StatusBarUtils.setStatusBarTransparenLight(this);
        init();
        initData();
        listener();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                filter_dl.openDrawer(filter_content_fl);
            }
        }, 50);
    }

    private void init() {
        filter_dl = findViewById(R.id.filter_dl);
        filter_content_fl = findViewById(R.id.filter_content_fl);
        recyclerView = findViewById(R.id.rv_hall);
        btn_ok = findViewById(R.id.btn_ok);
    }

    private void initData(){
        hallItems = (List<HallItem>) getIntent().getSerializableExtra("hallList");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (hallItems==null)
            return;
        recyclerView.setAdapter(new BaseQuickAdapter<HallItem,BaseViewHolder>(R.layout.item_hall_select,hallItems) {
            @Override
            protected void convert(BaseViewHolder helper, HallItem item) {
                TextView textView = helper.getView(R.id.tv_hall_name);
                if (item.isSelect()){
                    textView.setBackgroundResource(R.drawable.button_round_blue2);
                    textView.setTextColor(Color.WHITE);
                }else {
                    textView.setBackgroundResource(R.drawable.button_round_gray2);
                    textView.setTextColor(Color.parseColor("#4A4C5C"));
                }
                textView.setText(item.getName());
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (HallItem hallItem : hallItems) {
                            hallItem.setSelect(false);
                        }
                        item.setSelect(true);
                        selectItem = item;
                        notifyDataSetChanged();
                    }
                });
            }


        });
    }

    private void listener(){
        filter_dl.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectItem != null) {
                    Intent intent = new Intent();
                    intent.putExtra("hall",selectItem);
                    setResult(RESULT_OK,intent);
                    filter_dl.closeDrawer(filter_content_fl);
                }
            }
        });
    }
}
