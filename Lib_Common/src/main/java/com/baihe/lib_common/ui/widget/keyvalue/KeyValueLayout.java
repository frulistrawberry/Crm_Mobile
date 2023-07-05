package com.baihe.lib_common.ui.widget.keyvalue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lib_common.R;
import com.baihe.lib_common.ui.widget.keyvalue.adapter.AttachImageAdapter;
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValEventEntity;
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity;
import com.baihe.lib_common.ui.widget.font.FontStyle;
import com.baihe.lib_common.ui.widget.font.FontTextView;
import com.baihe.lib_framework.utils.DpToPx;

import java.util.List;

public class KeyValueLayout extends LinearLayout {


    private Context context;
    private int kvSpace;
    private int itemSpace;
    private float keyTextSize;
    private int keyTextColor;

    private boolean keyColon;
    private boolean keyEqualWidth;
    private float valueTextSize;
    private int valueTextColor;
    private int valueMaxLine;
    private FontStyle keyFontStyle = FontStyle.LIGHT;
    private FontStyle valueFontStyle = FontStyle.NORMAL;

    public KeyValueLayout(Context context) {
        this(context,null);
    }

    public KeyValueLayout(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public KeyValueLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    public enum ItemAction {
        CALL("call"),
        GO_LINK("goLink");
        String value;

        ItemAction(String value) {
            this.value = value;
        }

        String valueOf() {
            return value;
        }
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueLayout, defStyleAttr, 0);
        kvSpace = typedArray.getDimensionPixelOffset(R.styleable.KeyValueLayout_kv_kv_space, DpToPx.dpToPx( 4));
        itemSpace = typedArray.getDimensionPixelSize(R.styleable.KeyValueLayout_kv_item_space, DpToPx.dpToPx(16));
        keyTextSize = DpToPx.pxToSp(typedArray.getDimension(R.styleable.KeyValueLayout_kv_key_textSize, DpToPx.spToPx( 14)));
        keyTextColor = typedArray.getColor(R.styleable.KeyValueLayout_kv_key_textColor, Color.parseColor("#4A4C5C"));
        keyEqualWidth = typedArray.getBoolean(R.styleable.KeyValueLayout_kv_key_equal_width, true);
        keyColon = typedArray.getBoolean(R.styleable.KeyValueLayout_kv_key_colon, true);
        valueTextSize =  DpToPx.pxToSp( typedArray.getDimension(R.styleable.KeyValueLayout_kv_value_textSize, DpToPx.spToPx( 14)));
        valueTextColor = typedArray.getColor(R.styleable.KeyValueLayout_kv_value_textColor, Color.parseColor("#4A4C5C"));
        valueMaxLine = typedArray.getInteger(R.styleable.KeyValueLayout_kv_value_maxLine, 0);
        int keyFontStyleValue = typedArray.getInt(R.styleable.KeyValueLayout_kv_key_textStyle, FontStyle.LIGHT.valueOf());
        switch (keyFontStyleValue) {
            case FontTextView.LIGHT_VALUE:
                keyFontStyle = FontStyle.LIGHT;
                break;
            case FontTextView.NORMAL_VALUE:
                keyFontStyle = FontStyle.NORMAL;
                break;
            case FontTextView.HAFT_BOLD_VALUE:
                keyFontStyle = FontStyle.HALF_BOLD;
                break;
            case FontTextView.BOLD_VALUE:
                keyFontStyle = FontStyle.BOLD;
                break;
        }
        int valueFontStyleValue = typedArray.getInt(R.styleable.KeyValueLayout_kv_value_textStyle, FontStyle.LIGHT.valueOf());
        switch (valueFontStyleValue) {
            case FontTextView.LIGHT_VALUE:
                valueFontStyle = FontStyle.LIGHT;
                break;
            case FontTextView.NORMAL_VALUE:
                valueFontStyle = FontStyle.NORMAL;
                break;
            case FontTextView.HAFT_BOLD_VALUE:
                valueFontStyle = FontStyle.HALF_BOLD;
                break;
            case FontTextView.BOLD_VALUE:
                valueFontStyle = FontStyle.BOLD;
                break;
        }

        typedArray.recycle();
        setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (keyEqualWidth) {
            int childCount = getChildCount();
            int maxKeyWidth = 0;
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                KeyValueItemLayout kvItemLayout = (KeyValueItemLayout) childView;
                maxKeyWidth = Math.max(maxKeyWidth, kvItemLayout.getKeyMeasureWidth());
            }
            if (maxKeyWidth > 0) {
                for (int i = 0; i < childCount; i++) {
                    View childView = getChildAt(i);
                    KeyValueItemLayout kvItemLayout = (KeyValueItemLayout) childView;
                    kvItemLayout.setKeyWidth(maxKeyWidth);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setData(List<KeyValueEntity> kvList){
        if (kvList == null)
            return;
        int childCount = getChildCount();
        int dataSize = kvList.size();
        if (childCount > dataSize) {  //清除多余数据子View
            for (int i = childCount - 1; i >= dataSize; i--) {
                removeViewAt(i);
            }
        } else if (childCount < dataSize) { //数据子View不够用，添加
            for (int i = childCount; i < dataSize; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (i > 0) {
                    params.topMargin = itemSpace;
                } else {
                    params.topMargin = 0;
                }
                addView(createChildView(), i, params);
            }
        }

        childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            KeyValueEntity keyValueEntity = kvList.get(i);
            setValue(view, keyValueEntity);
        }


    }

    private View createChildView(){
        View childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_item, this, false);

        FontTextView kv_key_tv = childView.findViewById(R.id.kv_key_tv);
        kv_key_tv.setTextColor(keyTextColor);
        kv_key_tv.setTextSize(keyTextSize);
        kv_key_tv.setFontStyle(keyFontStyle);

        FontTextView kv_value_tv = childView.findViewById(R.id.kv_value_tv);
        kv_value_tv.setTextColor(valueTextColor);
        kv_value_tv.setTextSize(valueTextSize);
        if (valueMaxLine > 0) {
            kv_value_tv.setEllipsize(TextUtils.TruncateAt.END);
            kv_value_tv.setMaxLines(valueMaxLine);
        }
        kv_value_tv.setFontStyle(valueFontStyle);

        LinearLayout.LayoutParams keyLayoutParams = (LayoutParams) kv_key_tv.getLayoutParams();
        keyLayoutParams.rightMargin = kvSpace;
        RelativeLayout kv_value_rl = childView.findViewById(R.id.kv_value_rl);
        LinearLayout.LayoutParams valueLayoutParams = (LayoutParams) kv_value_rl.getLayoutParams();
        if (keyTextSize == valueTextSize) { //一样大top
            keyLayoutParams.gravity = Gravity.TOP;
            valueLayoutParams.gravity = Gravity.TOP;
        } else { //不一样居中
            keyLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            valueLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        }
        return childView;
    }

    @SuppressLint("SetTextI18n")
    private void setValue(View view, KeyValueEntity keyValueEntity){
        TextView kv_key_tv = view.findViewById(R.id.kv_key_tv);
        TextView kv_value_tv = view.findViewById(R.id.kv_value_tv);
        if (kv_key_tv != null) {
            String key = !TextUtils.isEmpty(keyValueEntity.getKey()) ? keyValueEntity.getKey() : !TextUtils.isEmpty(keyValueEntity.getKey2())?keyValueEntity.getKey2():"";
            kv_key_tv.setText(key + (keyColon ? "：" : ""));
        }
        if (kv_value_tv != null) {
            String value = !TextUtils.isEmpty(keyValueEntity.getVal()) ? keyValueEntity.getVal() : !TextUtils.isEmpty(keyValueEntity.getVal2())?keyValueEntity.getVal2():"";
            kv_value_tv.setText(value);
        }
        if (keyValueEntity.getEvent() != null) {
            setEvent(view, keyValueEntity);
        } else {
            LinearLayout kv_value_right_ll = view.findViewById(R.id.kv_value_right_ll);
            kv_value_right_ll.setVisibility(View.GONE);
        }
    }

    @SuppressLint("DiscouragedApi")
    private void setEvent(View view, KeyValueEntity keyValueEntity){
        KeyValEventEntity keyValEventEntity = keyValueEntity.getEvent();
        LinearLayout kv_value_right_ll = view.findViewById(R.id.kv_value_right_ll);
        RelativeLayout kv_value_rl = view.findViewById(R.id.kv_value_rl);
        TextView kv_value_tv = view.findViewById(R.id.kv_value_tv);
        RecyclerView kv_value_attach_rv = view.findViewById(R.id.rv_attach);
        ImageView kv_value_right_icon_iv = view.findViewById(R.id.kv_value_right_icon_iv);
        TextView kv_value_left_name_tv = view.findViewById(R.id.kv_value_left_name_tv);
        kv_value_left_name_tv.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(keyValEventEntity.getName()) || !TextUtils.isEmpty(keyValEventEntity.getIcon())||keyValEventEntity.getAttach()!=null) {
            kv_value_right_ll.setVisibility(View.VISIBLE);
        } else {
            kv_value_right_ll.setVisibility(View.GONE);
        }
        kv_value_attach_rv.setVisibility(GONE);
        if (!TextUtils.isEmpty(keyValEventEntity.getName())) {
            kv_value_right_icon_iv.setVisibility(View.VISIBLE);
            kv_value_left_name_tv.setVisibility(View.VISIBLE);
            kv_value_left_name_tv.setText(keyValEventEntity.getName());
            kv_value_right_icon_iv.setImageResource(R.mipmap.ic_arrow_right);
        } else if (!TextUtils.isEmpty(keyValEventEntity.getIcon())){
            kv_value_left_name_tv.setVisibility(View.GONE);
            kv_value_right_icon_iv.setVisibility(View.VISIBLE);
            int drawableId = context.getResources().getIdentifier(keyValEventEntity.getIcon(), "mipmap", context.getPackageName());
            if (drawableId == 0) { //未找到资源隐藏
                kv_value_right_icon_iv.setVisibility(View.GONE);
            } else {
                LinearLayout.LayoutParams valueLayoutParams = (LayoutParams) kv_value_rl.getLayoutParams();
                valueLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                kv_value_right_icon_iv.setImageResource(drawableId);
            }
        } else if (keyValEventEntity.getAttach() != null) {
            kv_value_tv.setText("");
            kv_value_right_icon_iv.setVisibility(GONE);
            kv_value_attach_rv.setVisibility(VISIBLE);
            kv_value_attach_rv.setLayoutManager(new GridLayoutManager(context,2));
            AttachImageAdapter adapter = new AttachImageAdapter();
            kv_value_attach_rv.setAdapter(adapter);
            adapter.setData(keyValEventEntity.getAttach());
        }

        setAction(view, kv_value_right_ll, keyValueEntity);
    }

    private void setAction(View itemView, LinearLayout rightView, KeyValueEntity keyValueEntity) {
        String actionName = keyValueEntity.getEvent().getAction();
        ItemAction itemAction = getItemAction(actionName);
        if (itemAction == null) {
            return;
        }
        switch (itemAction){
            case CALL:
                // TODO: 外呼
                break;
            case GO_LINK:
                // TODO: 跳转
        }
    }

    private ItemAction getItemAction(String actionName) {
        if (ItemAction.CALL.valueOf().equals(actionName)) {
            return ItemAction.CALL;
        } else if (ItemAction.GO_LINK.valueOf().equals(actionName)) {
            return ItemAction.GO_LINK;
        } else {
            return null;
        }
    }
}
