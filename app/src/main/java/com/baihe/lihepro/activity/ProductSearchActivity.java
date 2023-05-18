package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ProductPagerAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.fragment.ProductListFragment;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.manager.ProductSelectManager;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductSearchActivity extends BaseActivity {
    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ProductSearchActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    private EditText product_title_search_et;
    private ImageView product_title_search_delete_iv;
    private TextView product_title_cancel_tv;
    private HorizontalScrollView product_title_hs;
    private TextTransitionRadioGroup product_title_ttrg;
    private ViewPager product_vp;
    private TextView product_ok_tv;

    private List<CategoryEntity> categorys;
    private ProductPagerAdapter productPagerAdapter;
    private Map<Integer, Integer> idForIndexMap = new HashMap<>();
    private Map<Integer, TextTransitionRadioButton> indexForViewMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_product_search_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_product, null), params);
        init();
        listener();
        loadData();
    }

    private void init() {
        product_title_search_et = findViewById(R.id.product_title_search_et);
        product_title_search_delete_iv = findViewById(R.id.product_title_search_delete_iv);
        product_title_cancel_tv = findViewById(R.id.product_title_cancel_tv);
        product_title_hs = findViewById(R.id.product_title_hs);
        product_title_ttrg = findViewById(R.id.product_title_ttrg);
        product_vp = findViewById(R.id.product_vp);
        product_ok_tv = findViewById(R.id.product_ok_tv);
    }

    private void initData() {
        //初始化radiobutton
        int count = categorys.size();
        int margin = CommonUtils.dp2pxForInt(context, 16);
        for (int i = 0; i < count; i++) {
            CategoryEntity categoryEntity = categorys.get(i);

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
            textTransitionRadioButton.setText(categoryEntity.getName());
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
            } else if (i == count - 1) {
                params.leftMargin = margin;
                params.rightMargin = 0;
            } else {
                params.leftMargin = margin;
                params.rightMargin = margin;
            }
            idForIndexMap.put(id, i);
            indexForViewMap.put(i, textTransitionRadioButton);

            product_title_ttrg.addView(textTransitionRadioButton, params);
        }

        productPagerAdapter = new ProductPagerAdapter(getSupportFragmentManager(), categorys);
        product_vp.setAdapter(productPagerAdapter);
        product_vp.setOffscreenPageLimit(categorys.size() - 1);
    }

    private void listener() {
        product_title_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadKeyword(s.toString().trim());
            }
        });
        product_title_search_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_title_search_et.setText("");
            }
        });
        product_title_cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        product_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductSelectManager.newInstance().getSelectProduct() != null && ProductSelectManager.newInstance().getSelectProduct().size() > 0) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        product_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                product_title_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                TextTransitionRadioButton radioButton = indexForViewMap.get(position);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                    scrollTitle(radioButton);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        product_title_ttrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = idForIndexMap.get(checkedId);
                if (categorys != null && index >= 0 && index < categorys.size()) {
                    product_vp.setCurrentItem(index);
                }
            }
        });
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadData();
            }

            @Override
            public void onNetFailClick() {
                loadData();
            }

            @Override
            public void onExpandClick() {

            }
        });
    }

    private void loadKeyword(String keyword) {
        int count = productPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            ProductListFragment productListFragment = getFragment(i);
            if (productListFragment != null) {
                productListFragment.restKeyWord(keyword);
            }
        }
    }

    private ProductListFragment getFragment(int index) {
        String tagName = "android:switcher:" + product_vp.getId() + ":" + productPagerAdapter.getItemId(index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagName);
        if (fragment != null && fragment instanceof ProductListFragment) {
            return (ProductListFragment) fragment;
        }
        return null;
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("companyId", AccountManager.newInstance().getUser().getCompany_id());
        HttpRequest.create(UrlConstant.COMPANY_CATEGORY_URL).putParam(jsonParam).get(new CallBack<List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, CategoryEntity.class);
            }

            @Override
            public void success(List<CategoryEntity> entities) {
                statusLayout.normalStatus();
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId("0");
                categoryEntity.setName("全部");
                entities.add(0, categoryEntity);
                ProductSearchActivity.this.categorys = entities;
                initData();
            }

            @Override
            public void error() {
                statusLayout.netFailStatus();
            }

            @Override
            public void fail() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
    }

    private void scrollTitle(View view) {
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = CommonUtils.getScreenWidth(context);
        final int childWidth = right - left;
        int scrollX = left + CommonUtils.dp2pxForInt(context, 15) - (screenWith - childWidth) / 2;
        product_title_hs.smoothScrollTo(scrollX, 0);
    }

}
