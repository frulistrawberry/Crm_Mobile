package com.baihe.lib_common.widget.keyvalue;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.lib_common.R;
import com.baihe.lib_common.dialog.BottomSelectChannelDialog;
import com.baihe.lib_common.dialog.BottomSelectDialog;
import com.baihe.lib_common.dialog.BottomSelectRecordUserDialog;
import com.baihe.lib_common.dialog.adapter.SelectDataAdapter;
import com.baihe.lib_common.widget.keyvalue.entity.KeyValueEntity;
import com.baihe.lib_framework.toast.TipsToast;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;


public class KeyValueEditLayout extends LinearLayout {
    private static final String KV_CHILD_TAG = "KV_CHILD_TAG";
    private static final String SEPARATOR = ",";
    private static final int ITEM_VIEW_TYPE_NORMAL = 1;
    private static final int ITEM_VIEW_TYPE_INPUT = 2;
    private static final int ITEM_VIEW_TYPE_CONTACT = 3;
    private List<KeyValueEntity> kvList;

    private OnItemActionListener listener;

    public enum ItemAction {
        INPUT("input"),
        SINGLE_SELECT("select"),

        MULTIPLE_SELECT("multipleSelect"),
        COLLECTION("collection"),
        COLLECTION_MULTIPLE("collectionMultiple"),
        READ_ONLY("readOnly"),
        CONTACT("contact"),
        DATE_TIME("datetime"),
        DATE_TIME_RANGE("dateTimeRange"),
        RECORD_USER("recordUser"),
        CHANNEL("channe");


        final String value;

        ItemAction(String value) {
            this.value = value;
        }

        String valueOf() {
            return value;
        }
    }

    public KeyValueEditLayout(Context context) {
        this(context,null);
    }

    public KeyValueEditLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KeyValueEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        setOrientation(VERTICAL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int maxKeyWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (KV_CHILD_TAG.equals(childView.getTag()) && childView instanceof KeyValueItemLayout) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                KeyValueItemLayout kvItemLayout = (KeyValueItemLayout) childView;
                maxKeyWidth = Math.max(maxKeyWidth, kvItemLayout.getKeyMeasureWidth());
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    /**
     * 设置数据
     *
     * @param kvList
     */
    public void setData(List<KeyValueEntity> kvList) {
        this.kvList = kvList;
        removeAllViews();
        if (kvList == null) {
            return;
        }
        int dataSize = kvList.size();
        for (int i = 0; i < dataSize; i++) {
            KeyValueEntity keyValueEntity = kvList.get(i);
            addView(createItemView(keyValueEntity));
            addView(createItemViewLine(keyValueEntity));
        }
        refresh();
    }

    /**
     * 清空
     */
    public void clearData() {
        this.kvList = null;
        removeAllViews();
    }

    /**
     * 查找索引
     *
     * @param keyValueEntity
     * @return
     */
    public int findItemIndex(KeyValueEntity keyValueEntity) {
        int size = kvList.size();
        for (int index = 0; index < size; index++) {
            if (keyValueEntity == kvList.get(index)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 通过key找对象
     *
     * @param key
     * @return
     */
    public KeyValueEntity findEntityByKey(String key) {
        if (kvList != null && key != null) {
            for (KeyValueEntity keyValueEntity : kvList) {
                if (key.equals(keyValueEntity.getKey())) {
                    return keyValueEntity;
                }
            }
        }
        return null;
    }

    /**
     * 通过paramKey找对象
     *
     * @param paramKey
     * @return
     */
    public KeyValueEntity findEntityByParamKey(String paramKey) {
        if (kvList != null && paramKey != null) {
            for (KeyValueEntity keyValueEntity : kvList) {
                if (keyValueEntity.getEvent() != null && paramKey.equals(keyValueEntity.getEvent().getParamKey())) {
                    return keyValueEntity;
                }
            }
        }
        return null;
    }

    /**
     * 提交数据，如果提交数据失败（必填项未填写或未设置数据），直接返回null，并自动Toast
     *
     * @return
     */
    @Nullable
    public LinkedHashMap<String, Object> commit() {
        return getCommitMap(kvList);
    }

    /**
     * 获取提交数据
     *
     * @param kvList
     * @return
     */
    @Nullable
    private LinkedHashMap<String, Object> getCommitMap(List<KeyValueEntity> kvList) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if (kvList == null) {
            return data;
        }
        for (KeyValueEntity keyValueEntity : kvList) {
            //可提交的数据
            if ("1".equals(keyValueEntity.getShowStatus())) {
                String value = keyValueEntity.getDefaultVal();
                ItemAction itemAction = getItemAction(keyValueEntity);
                 if (itemAction == ItemAction.DATE_TIME_RANGE) {
                    //范围时间取标记值
                    String startTime = keyValueEntity.getRangeMin();
                    String endTime = keyValueEntity.getRangeMax();
                    String timeFormat = getTimeFormat(keyValueEntity.getEvent().getFormat());

                    SimpleDateFormat format = new SimpleDateFormat(timeFormat);
                    if (TextUtils.isEmpty(startTime) && "1".equals(keyValueEntity.getOptional())) {
                        TipsToast.INSTANCE.showTips(getAlertPrefix(keyValueEntity) + "起始时间");
                        return null;
                    }
                    if (TextUtils.isEmpty(endTime) && "1".equals(keyValueEntity.getOptional())) {
                        TipsToast.INSTANCE.showTips(getAlertPrefix(keyValueEntity) + "结束时间");
                        return null;
                    }
                    try {
                        if (format.parse(startTime).getTime() > format.parse(endTime).getTime()) {
                            TipsToast.INSTANCE.showTips("起始时间不能大于结束时间");
                            return null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] paramsKeys = keyValueEntity.getEvent().getParamKey().split(SEPARATOR);
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMin())) {
                        data.put(paramsKeys[0], keyValueEntity.getRangeMin());
                    }
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMax())) {
                        data.put(paramsKeys[1], keyValueEntity.getRangeMax());
                    }
                } else if (itemAction == ItemAction.CONTACT){
                     //范围时间取标记值
                     String phone = keyValueEntity.getPhone();
                     String wechat = keyValueEntity.getWechat();

                     if (TextUtils.isEmpty(wechat)&&TextUtils.isEmpty(phone) && "1".equals(keyValueEntity.getOptional())) {
                         TipsToast.INSTANCE.showTips("联系方式至少填一项");
                         return null;
                     }
                     String[] paramsKeys = keyValueEntity.getEvent().getParamKey().split(SEPARATOR);
                     if (!TextUtils.isEmpty(keyValueEntity.getPhone())) {
                         data.put(paramsKeys[0], keyValueEntity.getPhone());
                     }
                     if (!TextUtils.isEmpty(keyValueEntity.getWechat())) {
                         data.put(paramsKeys[1], keyValueEntity.getWechat());
                     }
                 }
                 else {
                    if (TextUtils.isEmpty(value) && "1".equals(keyValueEntity.getOptional()) && itemAction != ItemAction.READ_ONLY) {
                        String prefix = TextUtils.isEmpty(getAlertPrefix(keyValueEntity)) ? "请输入" : getAlertPrefix(keyValueEntity);
                        TipsToast.INSTANCE.showTips(prefix + keyValueEntity.getKey());
                        return null;
                    }
                    if (!TextUtils.isEmpty(value)) {
                        data.put(keyValueEntity.getEvent().getParamKey(), value);
                    }
                }
            }
        }
        return data;
    }

    /**
     * 获取时间格式
     *
     * @param format
     * @return
     */
    public static String getTimeFormat(String format) {
        if (format == null) {
            return "yyyy-MM-dd";
        }
        return format;
    }


    /**
     * 刷新整个页面
     */
    public void refresh() {
        if (kvList != null) {
            int dataSize = kvList.size();
            for (int i = 0; i < dataSize; i++) {
                KeyValueEntity keyValueEntity = kvList.get(i);
                //刷新item
                setItemValue(keyValueEntity);
                if (isItemShow(keyValueEntity)) {
                    setItemVisible(keyValueEntity);
                } else {
                    setItemGone(keyValueEntity);
                }
            }
            refreshItemLines();
        }
    }

    /**
     * 刷新item
     *
     * @param keyValueEntity
     */
    public void refreshItem(KeyValueEntity keyValueEntity) {
        if (keyValueEntity == null) {
            return;
        }
        setItemValue(keyValueEntity);
        if (isItemShow(keyValueEntity)) {
            setItemVisible(keyValueEntity);
        } else {
            setItemGone(keyValueEntity);
        }
        refreshItemLines();
    }

    /**
     * 查找itemView的分割线view
     *
     * @param keyValueEntity
     * @return
     */
    public View findItemViewLine(KeyValueEntity keyValueEntity) {
        return findViewById(generateItemLineId(keyValueEntity));
    }

    /**
     * 刷新所有item分割线
     * <p>
     * 触发：刷新所有item或单个item都需要调用该方法刷新，因为可能存在联动显示
     * <p>
     * 规则：显示的last item隐藏分割线，剩余显示item显示分割线
     */
    public void refreshItemLines() {
        if (kvList != null) {
            int dataSize = kvList.size();
            //最后一条显示分割线的索引
            int lastLineIndex = -1;
            for (int i = 0; i < dataSize; i++) {
                KeyValueEntity keyValueEntity = kvList.get(i);
                View itemViewLine = findItemViewLine(keyValueEntity);
                if (isItemShow(keyValueEntity)) {
                    if (itemViewLine != null) {
                        itemViewLine.setVisibility(View.VISIBLE);
                        lastLineIndex = Math.max(lastLineIndex, i);
                    }
                } else {
                    if (itemViewLine != null) {
                        itemViewLine.setVisibility(View.GONE);
                    }
                }
            }
            if (lastLineIndex != -1) {
                KeyValueEntity keyValueEntity = kvList.get(lastLineIndex);
                View itemViewLine = findItemViewLine(keyValueEntity);
                if (itemViewLine != null) {
                    itemViewLine.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * item赋值
     *
     * @param keyValueEntity
     */
    private void setItemValue(KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView != null) {
            int viewType = getItemViewType(keyValueEntity);
            if (viewType == ITEM_VIEW_TYPE_INPUT) {
                setItemValueForInput(itemView, keyValueEntity);
            }  else if (viewType == ITEM_VIEW_TYPE_CONTACT) {
                setItemValueForContact(itemView, keyValueEntity);
            } else {
                setItemValueForNormal(itemView, keyValueEntity);
            }
            setItemEvent(keyValueEntity);
        }
    }

    /**
     * 文本输入
     * @param itemView 文本输入框
     * @param keyValueEntity 数据
     */
    private void setItemValueForInput(View itemView, KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_text_et = itemView.findViewById(R.id.kv_edit_value_text_et);
        kv_edit_key_tv.setText(keyValueEntity.getKey());
        //清除EditText绑定
        try {
            Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_text_et);
            if (listeners != null) {
                for (TextWatcher listener : listeners) {
                    kv_edit_value_text_et.removeTextChangedListener(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = keyValueEntity.getVal();
        kv_edit_value_text_et.setHint(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
        if (isItemOption(keyValueEntity)){
            kv_edit_value_text_et.setFocusable(true);
            kv_edit_value_text_et.setCursorVisible(true);
            kv_edit_key_tv.setTextColor(Color.parseColor("#4A4C5C"));
            kv_edit_value_text_et.setTextColor(Color.parseColor("#8B8B99"));
            if (!TextUtils.isEmpty(value)) {
                kv_edit_value_text_et.setText(value);
                kv_edit_value_text_et.setSelection(value.length());
            }
        }else {
            kv_edit_value_text_et.setFocusable(false);
            kv_edit_value_text_et.setCursorVisible(false);
            kv_edit_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_text_et.setTextColor(Color.parseColor("#C5C5CE"));
            if (!TextUtils.isEmpty(value)) {
                kv_edit_value_text_et.setText(value);
            }else {
                kv_edit_value_text_et.setText("未填写");
            }
        }

        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        kv_edit_value_text_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                keyValueEntity.setDefaultVal(value);
                keyValueEntity.setVal(value);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });
    }

    /**
     * 联系方式
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForContact(View itemView, KeyValueEntity keyValueEntity){
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_phone_et = itemView.findViewById(R.id.kv_edit_value_phone_et);
        final EditText kv_edit_value_wechat_et = itemView.findViewById(R.id.kv_edit_value_wechat_et);
        final TextView kv_edit_phone_key_tv = itemView.findViewById(R.id.kv_edit_phone_key_tv);
        final TextView kv_edit_wechat_key_tv = itemView.findViewById(R.id.kv_edit_wechat_key_tv);
        final TextView kv_edit_contact_tip_tv = itemView.findViewById(R.id.kv_edit_contact_tip_tv);
        //清除EditText绑定
        try {
             Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            try {
                List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_phone_et);
                if (listeners != null) {
                    for (TextWatcher listener : listeners) {
                        kv_edit_value_phone_et.removeTextChangedListener(listener);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_wechat_et);
                if (listeners != null) {
                    for (TextWatcher listener : listeners) {
                        kv_edit_value_wechat_et.removeTextChangedListener(listener);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        kv_edit_key_tv.setText(keyValueEntity.getKey());
        kv_edit_value_phone_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        kv_edit_value_wechat_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        String value = keyValueEntity.getDefaultVal();
        String phone = "";
        String wechat = "";

        if (!TextUtils.isEmpty(value) && value.contains(SEPARATOR) && value.split(SEPARATOR).length > 0 && value.split(SEPARATOR).length <= 2) {
            if (value.split(SEPARATOR).length == 1) {
                phone = value.split(SEPARATOR)[0];
                wechat = "";
            } else {
                phone = value.split(SEPARATOR)[0];
                wechat = value.split(SEPARATOR)[1];
            }
        }
        if (isItemOption(keyValueEntity)){
            kv_edit_key_tv.setTextColor(Color.parseColor("#4A4C5C"));
            kv_edit_value_phone_et.setTextColor(Color.parseColor("#FF8B8B99"));
            kv_edit_value_wechat_et.setTextColor(Color.parseColor("#FF8B8B99"));
            kv_edit_phone_key_tv.setTextColor(Color.parseColor("#FF8B8B99"));
            kv_edit_wechat_key_tv.setTextColor(Color.parseColor("#FF8B8B99"));
            kv_edit_contact_tip_tv.setTextColor(Color.parseColor("#A8A9B1"));
            kv_edit_value_phone_et.setText(phone);
            kv_edit_value_wechat_et.setText(wechat);
            kv_edit_value_phone_et.setFocusable(true);
            kv_edit_value_wechat_et.setFocusable(true);
            kv_edit_value_phone_et.setCursorVisible(true);
            kv_edit_value_wechat_et.setCursorVisible(true);
        }else {
            kv_edit_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_phone_et.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_wechat_et.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_phone_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_wechat_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_contact_tip_tv.setTextColor(Color.parseColor("#C5C5CE"));
            if (TextUtils.isEmpty(phone))
                kv_edit_value_phone_et.setText("未填写");
            else
                kv_edit_value_phone_et.setText(phone);
            if (TextUtils.isEmpty(wechat)){
                kv_edit_value_wechat_et.setText("未填写");
            }else {
                kv_edit_value_wechat_et.setText(wechat);
            }
            kv_edit_value_phone_et.setFocusable(false);
            kv_edit_value_wechat_et.setFocusable(false);
            kv_edit_value_phone_et.setCursorVisible(false);
            kv_edit_value_wechat_et.setCursorVisible(false);

        }
        keyValueEntity.setPhone(phone);
        keyValueEntity.setWechat(wechat);

        kv_edit_value_phone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                keyValueEntity.setPhone(phone);
                keyValueEntity.setDefaultVal(phone + SEPARATOR + keyValueEntity.getWechat());
                keyValueEntity.setVal(keyValueEntity.getDefaultVal());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });
        kv_edit_value_wechat_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String wechat = s.toString().trim();
                //更新本地标记的maxRange
                keyValueEntity.setWechat(wechat);
                keyValueEntity.setDefaultVal(keyValueEntity.getPhone() + SEPARATOR + wechat);
                keyValueEntity.setVal(keyValueEntity.getDefaultVal());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });

    }


    /**
     * 选择、只读
     * @param itemView
     * @param keyValueEntity
     */
    @SuppressLint("SetTextI18n")
    private void setItemValueForNormal(View itemView, KeyValueEntity keyValueEntity){
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        TextView kv_edit_value_tv = itemView.findViewById(R.id.kv_edit_value_tv);
        ImageView kv_edit_value_right_icon_iv = itemView.findViewById(R.id.kv_edit_value_right_icon_iv);
        kv_edit_key_tv.setText(keyValueEntity.getKey());

        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //是否可操作
        if (isItemOption(keyValueEntity)) {
            kv_edit_value_right_icon_iv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_value_right_icon_iv.setVisibility(View.INVISIBLE);
        }
        //是否有填写值，只读颜色不一样
        if (!isItemOption(keyValueEntity)) {
            kv_edit_value_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            if (TextUtils.isEmpty(keyValueEntity.getVal())) {
                kv_edit_value_tv.setText("未填写");
            } else {
                kv_edit_value_tv.setText(keyValueEntity.getVal());
            }
        } else {
            kv_edit_key_tv.setTextColor(Color.parseColor("#4A4C5C"));
            if (TextUtils.isEmpty(keyValueEntity.getVal())) {
                kv_edit_value_tv.setTextColor(Color.parseColor("#C5C5CE"));
                kv_edit_value_tv.setText(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
            } else {
                kv_edit_value_tv.setTextColor(Color.parseColor("#8B8B99"));
                kv_edit_value_tv.setText(keyValueEntity.getVal());
            }
        }
    }

    /**
     * 设置item事件
     *
     * @param keyValueEntity
     */
    private void setItemEvent(KeyValueEntity keyValueEntity) {
        ItemAction itemAction = getItemAction(keyValueEntity);
        if (ItemAction.SINGLE_SELECT == itemAction) {  //单选
            singleSelectAction(keyValueEntity);
        }else if (ItemAction.CHANNEL == itemAction){//渠道选择
            channelAction(keyValueEntity);
        }else if (ItemAction.RECORD_USER == itemAction){
            recordUserAction(keyValueEntity);
        }
    }

    private void recordUserAction(KeyValueEntity keyValueEntity){
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (!isItemOption(keyValueEntity)) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(v -> {
            kv_edit_value_ll.setFocusable(true);
            kv_edit_value_ll.setFocusableInTouchMode(true);
            kv_edit_value_ll.requestFocus();
            String defaultVal = keyValueEntity.getDefaultVal();
            String channelId = keyValueEntity.getChannelId();
            if (TextUtils.isEmpty(channelId)){
                TipsToast.INSTANCE.showTips("请选择渠道来源");
                return;
            }
            new BottomSelectRecordUserDialog.Builder(getContext(),channelId)
                    .setOnConfirmClickListener((dialog, id, name) -> {
                        keyValueEntity.setDefaultVal(String.valueOf(id));
                        keyValueEntity.setVal(name);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                        dialog.dismiss();
                        return null;
                    })
                    .setDefValue(!TextUtils.isEmpty(defaultVal) ? Integer.parseInt(defaultVal) : -1)
                    .create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });
    }

    private void channelAction(KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (!isItemOption(keyValueEntity)) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(v -> {
            kv_edit_value_ll.setFocusable(true);
            kv_edit_value_ll.setFocusableInTouchMode(true);
            kv_edit_value_ll.requestFocus();
            String defaultVal = keyValueEntity.getDefaultVal();
            new BottomSelectChannelDialog.Builder(getContext())
                    .setOnConfirmClickListener((dialog, id, name) -> {
                        keyValueEntity.setDefaultVal(String.valueOf(id));
                        keyValueEntity.setVal(name);
                        KeyValueEntity recordEntity = findEntityByParamKey("record_user_id");
                        if (recordEntity != null) {
                            recordEntity.setChannelId(String.valueOf(id));
                        }
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                        dialog.dismiss();
                        return null;
                    })
                    .setDefValue(!TextUtils.isEmpty(defaultVal) ? Integer.parseInt(defaultVal) : -1)
                    .create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });

    }

    /**
     * 单选操作
     *
     * @param keyValueEntity
     */
    private void singleSelectAction(final KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (!isItemOption(keyValueEntity)) {
            return;
        }


        kv_edit_value_ll.setOnClickListener(v -> {
            kv_edit_value_ll.setFocusable(true);
            kv_edit_value_ll.setFocusableInTouchMode(true);
            kv_edit_value_ll.requestFocus();
            final List<KeyValueEntity> options = keyValueEntity.getEvent().getOptions();
            String defaultVal = keyValueEntity.getDefaultVal();
            int initSelectDataPosition = -1;
            for (int i = 0; i < options.size(); i++) {
                KeyValueEntity option = options.get(i);
                if (!TextUtils.isEmpty(option.getVal())  && option.getVal().equals(defaultVal)) {
                    initSelectDataPosition = i;
                }
            }
            int finalInitSelectDataPosition = initSelectDataPosition;
            new BottomSelectDialog.Builder(getContext())
                    .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                    .setSelectDataAdapter(new SelectDataAdapter() {
                        @Override
                        public int getCount() {
                            return options.size();
                        }

                        @Override
                        public String getText(int dataPostion) {
                            return options.get(dataPostion).getKey();
                        }

                        @Override
                        public int initSelectDataPosition() {
                            return finalInitSelectDataPosition;
                        }
                    }).setOnConfirmClickListener((dialog, position) -> {
                        if (finalInitSelectDataPosition != position) {
                            final KeyValueEntity option = options.get(position);
                            //更新item
                            keyValueEntity.setDefaultVal(option.getVal());
                            keyValueEntity.setVal(option.getKey());
                            refreshItem(keyValueEntity);
                            if (listener != null) {
                                listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                            }
                            dialog.dismiss();
                        }
                        return null;
                    }).create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });
    }

    /**
     * item显示
     *
     * @param keyValueEntity
     */
    private void setItemVisible(KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView != null) {
            itemView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * item隐藏
     *
     * @param keyValueEntity
     */
    private void setItemGone(KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView != null) {
            itemView.setVisibility(View.GONE);
        }
    }

    /**
     * item是否显示
     *
     * @return
     */
    private boolean isItemShow(KeyValueEntity keyValueEntity) {
        if (keyValueEntity.getShowStatus() == null || "1".equals(keyValueEntity.getShowStatus())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * item是否必填
     *
     * @return
     */
    private boolean isItemRequired(KeyValueEntity keyValueEntity) {
        if ("1".equals(keyValueEntity.getOptional())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * item是否可操作
     *
     * @param keyValueEntity
     */
    private boolean isItemOption(KeyValueEntity keyValueEntity) {
        if (keyValueEntity.getEvent() == null) {
            return false;
        }
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.READ_ONLY.valueOf().equals(action)) {
            return false;
        } else {
            return !keyValueEntity.isReadOnly();
        }

    }



    public static String getAlertPrefix(KeyValueEntity keyValueEntity) {
        ItemAction itemAction = getItemAction(keyValueEntity);
        String prefix = "";
        switch (itemAction) {
            case INPUT:
            case CONTACT:
                prefix = "请输入";
                break;
            case SINGLE_SELECT:
            case CHANNEL:
            case RECORD_USER:
                prefix = "请选择";
                break;

        }
        return prefix;
    }

    /**
     * 获取item操作动作
     *
     * @param keyValueEntity
     * @return
     */
    public static ItemAction getItemAction(KeyValueEntity keyValueEntity) {
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.INPUT.valueOf().equals(action)) {
            return ItemAction.INPUT;
        } else if (ItemAction.CONTACT.valueOf().equals(action)) {
            return ItemAction.CONTACT;
        } else if (ItemAction.SINGLE_SELECT.valueOf().equals(action)) {
            return ItemAction.SINGLE_SELECT;
        } else if (ItemAction.CHANNEL.valueOf().equals(action)) {
            return ItemAction.CHANNEL;
        }else if (ItemAction.RECORD_USER.valueOf().equals(action)) {
            return ItemAction.RECORD_USER;
        } else {
            return ItemAction.READ_ONLY;
        }
    }

    /**
     * 查找itemView
     *
     * @param keyValueEntity
     * @return
     */
    public View findItemView(KeyValueEntity keyValueEntity) {
        return findViewById(generateItemId(keyValueEntity));
    }


    /**
     * 创建itemView
     *
     * @param keyValueEntity
     * @return
     */
    private View createItemView(final KeyValueEntity keyValueEntity) {
        int viewType = getItemViewType(keyValueEntity);
        View childView;
        if (viewType == ITEM_VIEW_TYPE_INPUT) {
            childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_input_item, this, false);
        } else if (viewType == ITEM_VIEW_TYPE_CONTACT) {
            childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_contact_item, this, false);
        } else if (viewType == ITEM_VIEW_TYPE_NORMAL) {
            childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_select_item, this, false);
        } else {
            childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_select_item, this, false);
        }
        childView.setTag(KV_CHILD_TAG);
        setItemViewId(childView, keyValueEntity);
        return childView;
    }

    /**
     * itemView设置id
     *
     * @param childView
     * @param keyValueEntity
     */
    private void setItemViewId(View childView, KeyValueEntity keyValueEntity) {
        childView.setId(generateItemId(keyValueEntity));
    }

    /**
     * 对象item唯一id
     *
     * @param keyValueEntity
     * @return
     */
    private int generateItemId(KeyValueEntity keyValueEntity) {
        String identify = "keyValueItem_" + keyValueEntity.toString();
        byte[] identifyBytes;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(identify.getBytes(StandardCharsets.UTF_8));
            identifyBytes = md5.digest();
        } catch (Exception e) {
            e.printStackTrace();
            identifyBytes = identify.getBytes();
        }
        BigInteger bigInt = new BigInteger(1, identifyBytes);
        return bigInt.intValue();
    }


    /**
     * 创建itemView分割线
     *
     * @param keyValueEntity
     * @return
     */
    private View createItemViewLine(KeyValueEntity keyValueEntity) {
        View childViewLine = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_line_item, this, false);
        setItemViewLineId(childViewLine, keyValueEntity);
        return childViewLine;
    }

    /**
     * itemView设置id
     *
     * @param childView
     * @param keyValueEntity
     */
    private void setItemViewLineId(View childView, KeyValueEntity keyValueEntity) {
        childView.setId(generateItemLineId(keyValueEntity));
    }

    /**
     * 生成item line唯一id
     *
     * @param keyValueEntity
     * @return
     */
    private int generateItemLineId(KeyValueEntity keyValueEntity) {
        String identify = "keyValueItem_Line_" + keyValueEntity.toString();
        byte[] identifyBytes;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(identify.getBytes(StandardCharsets.UTF_8));
            identifyBytes = md5.digest();
        } catch (Exception e) {
            e.printStackTrace();
            identifyBytes = identify.getBytes();
        }
        BigInteger bigInt = new BigInteger(1, identifyBytes);
        return bigInt.intValue();
    }

    /**
     * 获取item的view类型
     *
     * @param keyValueEntity
     * @return
     */
    private int getItemViewType(KeyValueEntity keyValueEntity) {
        if (keyValueEntity.getEvent() == null) {
            return ITEM_VIEW_TYPE_NORMAL;
        }
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.INPUT.valueOf().equals(action)) {
            return ITEM_VIEW_TYPE_INPUT;
        } else if (ItemAction.CONTACT.valueOf().equals(action)) {
            return ITEM_VIEW_TYPE_CONTACT;
        } else {
            return ITEM_VIEW_TYPE_NORMAL;
        }
    }


    /**
     * 设置Item Action监听
     *
     * @param listener
     */
    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    /**
     * action监听
     */
    public static abstract class OnItemActionListener {
        /**
         * 事件响应
         *
         * @param keyValueEntity
         * @param itemAction
         */
        public abstract void onEvent(KeyValueEntity keyValueEntity, ItemAction itemAction);
    }


}
