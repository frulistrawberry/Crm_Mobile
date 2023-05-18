package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.MessageListAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.MsgListEntity;
import com.baihe.lihepro.fragment.MessageListFragment;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;

public class MessageActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    private Toolbar msg_list_title_tb;
    private HorizontalScrollView msg_list_title_hs;
    private TextTransitionRadioGroup msg_list_attr;
    private ViewPager message_list_vp;
    private List<Fragment> fragmentList;

    private MessageListAdapter adapter;
    private Map<Integer, TextTransitionRadioButton> indexForViewMap = new HashMap<>();
    private Map<Integer, Integer> idForIndexMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_message_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_message_list, null), params);
        init();
        initTab();
        listener();
        refreshBadge();
    }

    private void init() {
        msg_list_title_tb = findViewById(R.id.msg_list_title_tb);
        msg_list_title_hs = findViewById(R.id.msg_list_title_hs);
        msg_list_attr = findViewById(R.id.msg_list_attr);
        message_list_vp = findViewById(R.id.message_list_vp);

    }

    private void initTab() {
        //初始化radiobutton
        String[] tabs = {"全部","未读","已读"};
        fragmentList = new ArrayList<>();
        fragmentList.add(MessageListFragment.newFragment(3));
        fragmentList.add(MessageListFragment.newFragment(1));
        fragmentList.add(MessageListFragment.newFragment(2));
        int margin = CommonUtils.dp2pxForInt(context, 16);
        for (int i = 0; i < 3; i++) {

            TextTransitionRadioButton textTransitionRadioButton = new TextTransitionRadioButton(context);
            textTransitionRadioButton.setTransition(true);
            textTransitionRadioButton.setSelectedTextBold(true);
            textTransitionRadioButton.setSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setSelectedTextSize(CommonUtils.sp2px(context, 16));
            textTransitionRadioButton.setTextGravity(TextTransitionRadioButton.TextGravity.CENTER);
            textTransitionRadioButton.setUnSelectedTextBold(false);
            textTransitionRadioButton.setUnSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setUnSelectedTextSize(CommonUtils.sp2px(context, 14));


            int id = (int) (i + System.currentTimeMillis());
            textTransitionRadioButton.setId(id);
            textTransitionRadioButton.setText(tabs[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textTransitionRadioButton.setButtonDrawable(null);
            } else {
                textTransitionRadioButton.setButtonDrawable(new BitmapDrawable());
            }
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                textTransitionRadioButton.setChecked(true);
                params.leftMargin = 0;
                params.rightMargin = margin;
            } else if (i == 2) {
                params.leftMargin = margin;
                params.rightMargin = 0;
            } else {
                params.leftMargin = margin;
                params.rightMargin = margin;
            }

            idForIndexMap.put(id, i);
            indexForViewMap.put(i, textTransitionRadioButton);
            msg_list_attr.addView(textTransitionRadioButton, params);
        }


        adapter = new MessageListAdapter(getSupportFragmentManager(),fragmentList);
        message_list_vp.setAdapter(adapter);
        message_list_vp.setOffscreenPageLimit(2);
    }

    public void refresh(){
        for (Fragment fragment : fragmentList) {
            ((MessageListFragment)fragment).refresh();
        }
    }

    private void listener() {
        msg_list_attr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = idForIndexMap.get(checkedId);
                message_list_vp.setCurrentItem(index);
            }
        });


        message_list_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                msg_list_attr.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                TextTransitionRadioButton radioButton =  indexForViewMap.get(position);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                    scrollTitle(radioButton);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        msg_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void scrollTitle(View view) {
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = msg_list_attr.getRight() - CommonUtils.dp2pxForInt(context, 5);
        final int childWidth = right - left;
        int scrollX = left + CommonUtils.dp2pxForInt(context, 15) - (screenWith - childWidth) / 2;
        msg_list_title_hs.smoothScrollTo(scrollX, 0);
    }

    public void refreshBadge(){
        HttpRequest.create(UrlConstant.GET_UNREAD_LIST).putParam(JsonParam.newInstance("params")
                .putParamValue("push_id", AccountManager.newInstance().getUserId())
                .putParamValue("unread", "1").putParamValue("pageSize", "20").putParamValue("page", 1))
                .get(new CallBack<MsgListEntity>() {
                    @Override
                    public MsgListEntity doInBackground(String response) {
                        return JsonUtils.parse(response, MsgListEntity.class);
                    }

                    @Override
                    public void success(MsgListEntity entity) {
                        indexForViewMap.get(1).alert(entity.getTotal());
                    }

                    @Override
                    public void error() {

                    }

                    @Override
                    public void fail() {

                    }

                    @Override
                    public void before() {
                        super.before();
                        showOptionLoading();
                    }

                    @Override
                    public void after() {
                        super.after();
                        dismissOptionLoading();
                    }
                });
    }


}
