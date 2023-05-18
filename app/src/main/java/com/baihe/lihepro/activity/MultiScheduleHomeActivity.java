package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.MyScheduleListPagerAdapter;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.fragment.MultiScheduleFragment;
import com.baihe.lihepro.fragment.ScheduleListFragment;
import com.baihe.lihepro.fragment.SingleScheduleFragment;
import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.baihe.lihepro.view.indicator.ScaleTransitionPagerTitleView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;
import com.j256.ormlite.stmt.query.In;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MultiScheduleHomeActivity extends BaseActivity implements OnTimeSelectListener, View.OnClickListener {

    private TextView tv_date;
    private ImageView iv_arrow;
    private ImageView iv_halls;
    private ImageView iv_my;
    private Toolbar contract_list_title_tb;
    private MagicIndicator tabIndicator;
    private ViewPager viewPager;
    private MyScheduleListPagerAdapter adapter;
    private TimePickerView timePickerView;
    private int index;
    private String chooseDate;

    private int companyLevel;

    public static void start(Activity context, int companyLevel, String chooseDate,boolean isMulti){
        Intent intent = new Intent(context,MultiScheduleHomeActivity.class);
        intent.putExtra("companyLevel",companyLevel);
        intent.putExtra("isMulti",isMulti);
        intent.putExtra("chooseDate",chooseDate);
        context.startActivityForResult(intent,105);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_schedule_home_title);
        setContentView(R.layout.activity_schedule_home_new);
        companyLevel = getIntent().getIntExtra("companyLevel",4);
        index = getIntent().getBooleanExtra("isMulti",false)?1:0;
        chooseDate = getIntent().getStringExtra("chooseDate");
        findView();
        initTab();
        initView();
    }

    private void initTab() {
        String[] tabs = {"单档期预订","多档期预订"};
        List<Fragment> fragments = new ArrayList<>();
        Fragment singleFragment = new SingleScheduleFragment();
        Bundle singleArgument = new Bundle();
        singleArgument.putInt("companyLevel",companyLevel);
        Fragment multiFragment = new MultiScheduleFragment();
        Bundle multiArgument = new Bundle();
        multiArgument.putInt("companyLevel",companyLevel);
        if (chooseDate!=null){
            singleArgument.putString("chooseDate",chooseDate);
            multiArgument.putString("chooseDate",chooseDate);

        }
        multiFragment.setArguments(singleArgument);
        singleFragment.setArguments(singleArgument);

        fragments.add(singleFragment);
        fragments.add(multiFragment);
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
                titleView.setText(tabs[index]);
                titleView.setNormalColor(Color.parseColor("#4A4C5C"));
                titleView.setSelectedColor(Color.parseColor("#4A4C5C"));
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                        MultiScheduleHomeActivity.this.index = index;
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setXOffset(UIUtil.dip2px(context, 20));
                indicator.setColors(Color.parseColor("#00B6EB"));
                return indicator;
            }
        });

        tabIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(tabIndicator,viewPager);
        adapter = new MyScheduleListPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        if (chooseDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
            try {
                tv_date.setText(sdf1.format(sdf.parse(chooseDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            tv_date.setText(LocalDate.now().toString("yyyy年MM月"));
        }
        viewPager.setCurrentItem(index);


    }

    private void initView() {
        contract_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_halls.setVisibility(View.GONE);
        LiheTimePickerBuilder pickerBuilder = DateDialogUtils.createPickerViewBuilder(this,this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 2);
        pickerBuilder.setRangDate(Calendar.getInstance(),calendar);
        boolean[] type = {true,true,false,false,false,false};
        pickerBuilder.setType(type);
        pickerBuilder.addOnCancelClickListener(view -> iv_arrow.setImageResource(R.drawable.schedule_arrow_down));
        timePickerView = pickerBuilder.build();
        timePickerView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                iv_arrow.setImageResource(R.drawable.schedule_arrow_down);
            }
        });
        tv_date.setOnClickListener(this);
        iv_my.setOnClickListener(this);
    }

    private void findView() {
        tv_date = findViewById(R.id.tv_date);
        contract_list_title_tb = findViewById(R.id.contract_list_title_tb);
        iv_arrow = findViewById(R.id.iv_arrow);
        iv_halls = findViewById(R.id.iv_halls);
        iv_my = findViewById(R.id.iv_my);
        tabIndicator = findViewById(R.id.tab_indicator);
        viewPager = findViewById(R.id.calendar_vp);



    }

    @Override
    public void onTimeSelect(Date date, View v) {
        iv_arrow.setImageResource(R.drawable.schedule_arrow_down);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月");
        String time = format.format(date);
        tv_date.setText(format1.format(date));
        ((SingleScheduleFragment)adapter.getItem(0)).jump(time);
        ((MultiScheduleFragment)adapter.getItem(1)).jump(time);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_date:
                iv_arrow.setImageResource(R.drawable.schedule_arrow_up);
                timePickerView.show();
                break;
            case R.id.iv_my:
                MyScheduleListActivity.start(this);
                break;
        }
    }
}
