package com.baihe.lihepro.view;

import android.content.ClipboardManager;
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

import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.FontTextView;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.KVFloadAdapter;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：1005 1002
 */
public class KeyValueLayout extends LinearLayout {
    private static final String KV_CHILD_TAG = "KV_CHILD_TAG";
    private static final String KV_CHILD_FLOD_TAG = "KV_CHILD_FLOD_TAG";

    public void setOnUnlockWechatListener(OnUnlockWechatListener unlockWechatListener) {
        this.unlockWechatListener = unlockWechatListener;
    }

    public enum ItemAction {
        COPY("copy"),
        CALL("call"),
        UNLOCKPHONE("unlockPhone"),
        UNLOCKWECHAT("unlockWechat"),
        GOORDER("goOrder");
        String value;

        ItemAction(String value) {
            this.value = value;
        }

        String valueOf() {
            return value;
        }
    }

    private Context context;
    private int kvSpace;
    private int kvFlodLineNum;
    private boolean isUnFload;
    private int itemSpace;
    private float keyTextSize;
    private int keyTextColor;
    private boolean keyColon;
    private boolean keyEqualWidth;
    private float valueTextSize;
    private int valueTextColor;
    private int valueMaxLine;

    private KVFloadAdapter kvFloadAdapter;
    private int kvFloadAdapterIndex;

    private OnCallListener callListener;
    private OnUnlockMobileListener unlockMobileListener;
    private OnUnlockWechatListener unlockWechatListener;
    private OnOrderListener orderListener;

    private FontTextView.FontStyle keyFontStyle = FontTextView.FontStyle.LIGHT;
    private FontTextView.FontStyle valueFontStyle = FontTextView.FontStyle.LIGHT;

    public KeyValueLayout(Context context) {
        this(context, null);
    }

    public KeyValueLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyValueLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueLayout, defStyleAttr, 0);
        kvSpace = typedArray.getDimensionPixelSize(R.styleable.KeyValueLayout_kv_kv_space, CommonUtils.dp2pxForInt(context, 4));
        kvFlodLineNum = typedArray.getInteger(R.styleable.KeyValueLayout_kv_kv_flod_lineNum, 0);
        if (kvFlodLineNum < 0) {
            kvFlodLineNum = 0;
        }
        itemSpace = typedArray.getDimensionPixelSize(R.styleable.KeyValueLayout_kv_item_space, CommonUtils.dp2pxForInt(context, 12));
        keyTextSize = CommonUtils.pxTosp(context, typedArray.getDimension(R.styleable.KeyValueLayout_kv_key_textSize, CommonUtils.sp2px(context, 14)));
        keyTextColor = typedArray.getColor(R.styleable.KeyValueLayout_kv_key_textColor, Color.parseColor("#4A4C5C"));
        keyColon = typedArray.getBoolean(R.styleable.KeyValueLayout_kv_key_colon, true);
        keyEqualWidth = typedArray.getBoolean(R.styleable.KeyValueLayout_kv_key_equal_width, true);
        valueTextSize = CommonUtils.pxTosp(context, typedArray.getDimension(R.styleable.KeyValueLayout_kv_value_textSize, CommonUtils.sp2px(context, 14)));
        valueTextColor = typedArray.getColor(R.styleable.KeyValueLayout_kv_value_textColor, Color.parseColor("#4A4C5C"));
        valueMaxLine = typedArray.getInteger(R.styleable.KeyValueLayout_kv_value_maxLine, 0);

        int keyFontStyleValue = typedArray.getInt(R.styleable.KeyValueLayout_kv_key_textStyle, FontTextView.FontStyle.LIGHT.valueOf());
        switch (keyFontStyleValue) {
            case FontTextView.LIGHT_VALUE:
                keyFontStyle = FontTextView.FontStyle.LIGHT;
                break;
            case FontTextView.NORMAL_VALUE:
                keyFontStyle = FontTextView.FontStyle.NORMAL;
                break;
            case FontTextView.HAFT_BOLD_VALUE:
                keyFontStyle = FontTextView.FontStyle.HALF_BOLD;
                break;
            case FontTextView.BOLD_VALUE:
                keyFontStyle = FontTextView.FontStyle.BOLD;
                break;
        }
        int valueFontStyleValue = typedArray.getInt(R.styleable.KeyValueLayout_kv_value_textStyle, FontTextView.FontStyle.LIGHT.valueOf());
        switch (valueFontStyleValue) {
            case FontTextView.LIGHT_VALUE:
                valueFontStyle = FontTextView.FontStyle.LIGHT;
                break;
            case FontTextView.NORMAL_VALUE:
                valueFontStyle = FontTextView.FontStyle.NORMAL;
                break;
            case FontTextView.HAFT_BOLD_VALUE:
                valueFontStyle = FontTextView.FontStyle.HALF_BOLD;
                break;
            case FontTextView.BOLD_VALUE:
                valueFontStyle = FontTextView.FontStyle.BOLD;
                break;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (keyEqualWidth) {
            int childCount = getChildCount();
            int maxKeyWidth = 0;
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (KV_CHILD_TAG.equals(childView.getTag()) && childView instanceof KeyValueItemLayout) {
                    measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                    KeyValueItemLayout kvItemLayout = (KeyValueItemLayout) childView;
                    maxKeyWidth = maxKeyWidth < kvItemLayout.getKeyMeasureWidth() ? kvItemLayout.getKeyMeasureWidth() : maxKeyWidth;
                }
            }
            if (maxKeyWidth > 0) {
                for (int i = 0; i < childCount; i++) {
                    View childView = getChildAt(i);
                    if (KV_CHILD_TAG.equals(childView.getTag()) && childView instanceof KeyValueItemLayout) {
                        KeyValueItemLayout kvItemLayout = (KeyValueItemLayout) childView;
                        kvItemLayout.setKeyWidth(maxKeyWidth);
                    }
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 收起数据
     */
    public void flod() {
        this.isUnFload = false;
        //重制childView展开收起状态
        restFlod();
    }

    /**
     * 展开数据
     */
    public void unFlod() {
        this.isUnFload = true;
        //重制childView展开收起状态
        restFlod();
    }

    /**
     * 在adapter里使用展开收起功能，一定要设置这个方法，且adapter要继承{@link KVFloadAdapter}
     *
     * @param kvFloadAdapter
     * @param index
     */
    public void setKVFloadAdapterListener(KVFloadAdapter kvFloadAdapter, int index) {
        this.kvFloadAdapter = kvFloadAdapter;
        this.kvFloadAdapterIndex = index;
        boolean isFlod = kvFloadAdapter.getFlod(index);
        if (isFlod) {
            flod();
        } else {
            unFlod();
        }
    }

    public void setKeyTextColor(int keyTextColor) {
        this.keyTextColor = keyTextColor;
    }

    public void setValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
    }

    public void setData(List<KeyValueEntity> kvList) {
        if (kvList == null) {
            return;
        }
        int childCount = getChildCount();
        int dataSize = kvList.size();

        //尾部有折叠childView时，数据chidView的数量是childCount-1
        if (childCount > 0 && KV_CHILD_FLOD_TAG.equals(getChildAt(childCount - 1).getTag())) {
            childCount--;
        }
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
        //不需要添加折叠view时尾部却有折叠view，移除折叠view
        if ((kvFlodLineNum == 0 || kvFlodLineNum >= dataSize) && childCount > 0 && KV_CHILD_FLOD_TAG.equals(getChildAt(childCount - 1).getTag())) {
            removeViewAt(childCount - 1);
        } else if (kvFlodLineNum != 0 && kvFlodLineNum < dataSize && (childCount == 0 || !KV_CHILD_FLOD_TAG.equals(getChildAt(childCount - 1).getTag()))) {  //需要添加折叠view时尾部却没有折叠view，添加折叠view
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(createFlodChildView(), params);
        }

        childCount = getChildCount();
        //尾部有折叠childView时，数据chidView的数量是childCount-1
        if (childCount > 0 && KV_CHILD_FLOD_TAG.equals(getChildAt(childCount - 1).getTag())) {
            childCount--;
        }
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getTag().equals(KV_CHILD_TAG)) {
                KeyValueEntity keyValueEntity = kvList.get(i);
                setValue(view, keyValueEntity);
            }
        }
        restFlod();
    }

    /**
     * 重制childView展开收起状态
     */
    private void restFlod() {
        int childCount = getChildCount();
        if (kvFlodLineNum == 0) {
              for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (KV_CHILD_TAG.equals(childView.getTag())) {
                    childView.setVisibility(View.VISIBLE);
                } else if (KV_CHILD_FLOD_TAG.equals(childView.getTag())) {
                    childView.setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (KV_CHILD_TAG.equals(childView.getTag())) {
                    if (!isUnFload && i >= kvFlodLineNum) {
                        childView.setVisibility(View.GONE);
                    } else {
                        childView.setVisibility(View.VISIBLE);
                    }
                } else if (KV_CHILD_FLOD_TAG.equals(childView.getTag())) {
                    childView.setVisibility(View.VISIBLE);
                    ImageView kv_flod_iv = childView.findViewById(R.id.kv_flod_iv);
                    if (kv_flod_iv == null) {
                        continue;
                    }
                    if (isUnFload) {
                        kv_flod_iv.setImageResource(R.drawable.flod_arrow_up);
                    } else {
                        kv_flod_iv.setImageResource(R.drawable.flod_arrow_bottom);
                    }
                }
            }
        }
    }

    private View createChildView() {
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

        childView.setTag(KV_CHILD_TAG);
        return childView;
    }

    private View createFlodChildView() {
        View childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_flod_item, this, false);
        final ImageView kv_flod_iv = childView.findViewById(R.id.kv_flod_iv);
        kv_flod_iv.setPadding(itemSpace, itemSpace, itemSpace, 0);
        kv_flod_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnFload = !isUnFload;
                if (isUnFload) {
                    kv_flod_iv.setImageResource(R.drawable.flod_arrow_up);
                } else {
                    kv_flod_iv.setImageResource(R.drawable.flod_arrow_bottom);
                }
                if (kvFloadAdapter != null) {
                    kvFloadAdapter.setFlod(kvFloadAdapterIndex, !isUnFload);
                    kvFloadAdapter.notifyDataSetChanged();
                }
            }
        });
        childView.setTag(KV_CHILD_FLOD_TAG);
        return childView;
    }

    private void setValue(View view, KeyValueEntity keyValueEntity) {
        TextView kv_key_tv = view.findViewById(R.id.kv_key_tv);
        TextView kv_value_tv = view.findViewById(R.id.kv_value_tv);
        if (kv_key_tv != null) {
            kv_key_tv.setText(keyValueEntity.getKey() + (keyColon ? "：" : ""));
        }
        if (kv_value_tv != null) {
            String value = !TextUtils.isEmpty(keyValueEntity.getVal()) ? keyValueEntity.getVal() : "未填写";
            kv_value_tv.setText(value);
        }
        if (keyValueEntity.getEvent() != null) {
            setEvent(view, keyValueEntity);
        } else {
            LinearLayout kv_value_right_ll = view.findViewById(R.id.kv_value_right_ll);
            kv_value_right_ll.setVisibility(View.GONE);
        }
    }

    private void setEvent(View view, KeyValueEntity keyValueEntity) {
        KeyValEventEntity keyValEventEntity = keyValueEntity.getEvent();
        LinearLayout kv_unlock_mobile_ll = view.findViewById(R.id.kv_unlock_mobile_ll);
        TextView kv_unlock_mobile_tv = view.findViewById(R.id.kv_unlock_mobile_tv);
        LinearLayout kv_value_right_ll = view.findViewById(R.id.kv_value_right_ll);
        ImageView kv_value_right_icon_iv = view.findViewById(R.id.kv_value_right_icon_iv);
        TextView kv_value_left_name_tv = view.findViewById(R.id.kv_value_left_name_tv);
        TextView kv_value_right_name_tv = view.findViewById(R.id.kv_value_right_name_tv);
        kv_value_left_name_tv.setVisibility(View.GONE);

        if ("unlockPhone".equals(keyValEventEntity.getAction())&&keyValEventEntity.getPhoneNum() > 0 && keyValueEntity.getVal().contains("*")) {
            kv_unlock_mobile_ll.setVisibility(View.VISIBLE);
            kv_unlock_mobile_tv.setText("可看" + keyValEventEntity.getPhoneNum() + "次");
        } else if ("unlockPhone".equals(keyValEventEntity.getAction())){
            kv_unlock_mobile_ll.setVisibility(View.GONE);
        }
        if ("unlockWechat".equals(keyValEventEntity.getAction())&&keyValEventEntity.getWechatNum() > 0 && keyValueEntity.getVal().contains("*")) {
            kv_unlock_mobile_ll.setVisibility(View.VISIBLE);
            kv_unlock_mobile_tv.setText("可看" + keyValEventEntity.getWechatNum() + "次");
        } else if ("unlockWechat".equals(keyValEventEntity.getAction())){
            kv_unlock_mobile_ll.setVisibility(View.GONE);
        }
        //icon或name才显示右边
        if (keyValEventEntity != null && (!TextUtils.isEmpty(keyValEventEntity.getName()) || !TextUtils.isEmpty(keyValEventEntity.getIcon()))) {
            kv_value_right_ll.setVisibility(View.VISIBLE);
        } else {
            kv_value_right_ll.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(keyValEventEntity.getName())) {
            kv_value_right_name_tv.setVisibility(View.VISIBLE);
            kv_value_right_name_tv.setText(keyValEventEntity.getName());
        } else {
            kv_value_right_name_tv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(keyValEventEntity.getIcon())) {
            kv_value_right_icon_iv.setVisibility(View.VISIBLE);
            int drawableId = context.getResources().getIdentifier(keyValEventEntity.getIcon(), "drawable", context.getPackageName());
            if (drawableId == 0) { //未找到资源隐藏
                kv_value_right_icon_iv.setVisibility(View.GONE);
            } else {
                kv_value_right_icon_iv.setImageResource(drawableId);
            }
        } else {
            kv_value_right_icon_iv.setVisibility(View.GONE);
        }
        //处理action
        setAction(view, kv_value_right_ll, keyValueEntity);
    }

    /**
     * 处理action
     *
     * @param itemView
     * @param rightView
     * @param keyValueEntity
     */
    private void setAction(View itemView, View rightView, KeyValueEntity keyValueEntity) {
        String actionName = keyValueEntity.getEvent().getAction();
        ItemAction itemAction = getItemAction(actionName);
        if (itemAction == null) {
            return;
        }
        switch (itemAction) {
            case CALL: {
                callAction(rightView, keyValueEntity);
            }
            break;
            case COPY: {
                if (TextUtils.isEmpty(keyValueEntity.getVal())) {
                    LinearLayout kv_value_right_ll = itemView.findViewById(R.id.kv_value_right_ll);
                    kv_value_right_ll.setVisibility(View.GONE);
                } else {
                    copyAction(rightView, keyValueEntity);
                }
            }
            break;
            case UNLOCKPHONE: {
                callAction(rightView, keyValueEntity);
                if (keyValueEntity.getEvent().getPhoneNum() > 0) {
                    unlockMobileAction(itemView, keyValueEntity);
                }
            }
            break;
            case UNLOCKWECHAT:{
                if (keyValueEntity.getEvent().getWechatNum()>0){
                    unlockWechatAction(itemView,keyValueEntity);
                }
                break;
            }
            case GOORDER: {
                rightView.setVisibility(View.VISIBLE);
                TextView kv_value_left_name_tv = itemView.findViewById(R.id.kv_value_left_name_tv);
                kv_value_left_name_tv.setVisibility(View.VISIBLE);
                kv_value_left_name_tv.setTextColor(Color.parseColor("#2DB4E6"));
                kv_value_left_name_tv.setText("查看");
                orderAction(rightView, keyValueEntity);
            }
            break;
        }
    }

    private ItemAction getItemAction(String actionName) {
        if (ItemAction.COPY.valueOf().equals(actionName)) {
            return ItemAction.COPY;
        } else if (ItemAction.CALL.valueOf().equals(actionName)) {
            return ItemAction.CALL;
        } else if (ItemAction.UNLOCKPHONE.valueOf().equals(actionName)) {
            return ItemAction.UNLOCKPHONE;
        } else if (ItemAction.UNLOCKWECHAT.valueOf().equals(actionName)){
            return ItemAction.UNLOCKWECHAT;
        }
        else if (ItemAction.GOORDER.valueOf().equals(actionName)) {
            return ItemAction.GOORDER;
        } else {
            return null;
        }
    }

    private void callAction(View view, final KeyValueEntity keyValueEntity) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callListener != null) {
                    callListener.call(keyValueEntity);
                }
            }
        });
    }

    private void copyAction(View view, final KeyValueEntity keyValueEntity) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(keyValueEntity.getVal());
                ToastUtils.toast("已复制到剪切板");
            }
        });
    }

    private void unlockMobileAction(View view, final KeyValueEntity keyValueEntity) {
        final LinearLayout kv_unlock_mobile_ll = view.findViewById(R.id.kv_unlock_mobile_ll);
        final TextView kv_value_tv = view.findViewById(R.id.kv_value_tv);
        final ImageView kv_unlock_mobile_iv = view.findViewById(R.id.kv_unlock_mobile_iv);
        final TextView kv_unlock_mobile_tv = view.findViewById(R.id.kv_unlock_mobile_tv);
        kv_unlock_mobile_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unlockMobileListener != null) {
                    unlockMobileListener.unLock(kv_unlock_mobile_ll, kv_value_tv, kv_unlock_mobile_tv, kv_unlock_mobile_iv, keyValueEntity);
                }
            }
        });
    }

    private void unlockWechatAction(View view, final KeyValueEntity keyValueEntity) {
        final LinearLayout kv_unlock_mobile_ll = view.findViewById(R.id.kv_unlock_mobile_ll);
        final TextView kv_value_tv = view.findViewById(R.id.kv_value_tv);
        final ImageView kv_unlock_mobile_iv = view.findViewById(R.id.kv_unlock_mobile_iv);
        final TextView kv_unlock_mobile_tv = view.findViewById(R.id.kv_unlock_mobile_tv);
        kv_unlock_mobile_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unlockWechatListener != null) {
                    unlockWechatListener.unLock(kv_unlock_mobile_ll, kv_value_tv, kv_unlock_mobile_tv, kv_unlock_mobile_iv, keyValueEntity);
                }
            }
        });
    }

    private void orderAction(View view, final KeyValueEntity keyValueEntity) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderListener != null) {
                    orderListener.go(keyValueEntity);
                }
            }
        });
    }

    public void setOnCallListener(OnCallListener callListener) {
        this.callListener = callListener;
    }

    public void setOnUnlockMobileListener(OnUnlockMobileListener unlockMobileListener) {
        this.unlockMobileListener = unlockMobileListener;
    }

    public void setOnOrderListener(OnOrderListener orderListener) {
        this.orderListener = orderListener;
    }

    public interface OnCallListener {
        void call(KeyValueEntity keyValueEntity);
    }

    public interface OnUnlockMobileListener {
        void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity);
    }


    public interface OnUnlockWechatListener {
        void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity);
    }

    public interface OnOrderListener {
        void go(KeyValueEntity keyValueEntity);
    }

}
