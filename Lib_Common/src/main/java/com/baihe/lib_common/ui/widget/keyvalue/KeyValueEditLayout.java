package com.baihe.lib_common.ui.widget.keyvalue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lib_common.R;
import com.baihe.lib_common.ext.StringExt;
import com.baihe.lib_common.provider.CustomerServiceProvider;
import com.baihe.lib_common.ui.dialog.SelectChannelDialog;
import com.baihe.lib_common.ui.dialog.SelectCompanyUserDialog;
import com.baihe.lib_common.ui.dialog.BottomSelectDialog;
import com.baihe.lib_common.ui.dialog.BottomSelectMultiDialog;
import com.baihe.lib_common.ui.dialog.SelectRecordUserDialog;
import com.baihe.lib_common.ui.dialog.DateDialogUtils;
import com.baihe.lib_common.ui.dialog.SelectCityDialog;
import com.baihe.lib_common.ui.widget.FlowLayout;
import com.baihe.lib_common.ui.widget.keyvalue.adapter.AttachImageAdapter;
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity;
import com.baihe.lib_framework.toast.TipsToast;
import com.baihe.lib_framework.utils.DpToPx;
import com.baihe.lib_framework.utils.ViewUtils;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


public class KeyValueEditLayout extends LinearLayout {
    private static final String KV_CHILD_TAG = "KV_CHILD_TAG";
    private static final String SEPARATOR = ",";
    private List<KeyValueEntity> kvList;
    private OnItemActionListener listener;

    public enum ItemType {
        INPUT("input"),
        SINGLE_SELECT("select"),
        PLAN_TYPE("planType"),
        MULTI_SELECT("multipleSelect"),

        AMOUNT("amount"),
        NUMBER("number"),
        PHONE("phone"),

        COLLECTION("collection"),
        COLLECTION_MULTIPLE("collectionMultiple"),
        COLLECTION_MULTIPLE_LEVEL("collectionMultipleLevel"),
        READ_ONLY("readonly"),
        CONTACT("contact"),
        DATE_TIME("datetime"),
        DATE_TIME2("dateTime"),
        DATE_TIME_RANGE("dateTimeRange"),
        RECORD_USER("recordUser"),
        COMPANY_USER("userlist"),
        CITY("city"),
        CUSTOMER_SELECT("customerSelect"),
        UPLOAD("upload"),
        FOLLOW_RESULT("followResult"),
        CHANNEL_FILTER("channelFilter"),
        DATE_TIME_RANGE_FILTER("dateTimeRangeFilter"),
        CHANNEL("channe");




        final String value;

        ItemType(String value) {
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

    public List<KeyValueEntity> getData(){
        return kvList;
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
     * @param keyValueEntity itemValue
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
     * 清空
     */
    public void clearData() {
        this.kvList = null;
        removeAllViews();
    }


    /**
     * 通过name找对象
     *
     * @param name
     * @return
     */
    public KeyValueEntity findEntityByName(String name) {
        if (kvList != null && name != null) {
            for (KeyValueEntity keyValueEntity : kvList) {
                if (name.equals(keyValueEntity.getName())) {
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
                if (paramKey.equals(keyValueEntity.getParamKey())) {
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
    @SuppressLint("SimpleDateFormat")
    @Nullable
    private LinkedHashMap<String, Object> getCommitMap(List<KeyValueEntity> kvList) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        if (kvList == null) {
            return data;
        }
        for (KeyValueEntity keyValueEntity : kvList) {
            //可提交的数据
            if ("1".equals(keyValueEntity.getIs_open())) {
                String value = keyValueEntity.getValue();
                ItemType itemType = getItemType(keyValueEntity);
                switch (itemType){
                    case DATE_TIME_RANGE:
                        String startTime = keyValueEntity.getRangeMin();
                        String endTime = keyValueEntity.getRangeMax();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        if (TextUtils.isEmpty(startTime) && "1".equals(keyValueEntity.getIs_true())) {
                            TipsToast.INSTANCE.showTips(getAlertPrefix(keyValueEntity) + "起始时间");
                            return null;
                        }
                        if (TextUtils.isEmpty(endTime) && "1".equals(keyValueEntity.getIs_true())) {
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

                        if ("weddingDate".equals(keyValueEntity.getParamKey())){
                            if (!TextUtils.isEmpty(keyValueEntity.getRangeMin())) {
                                data.put("wedding_date_from", keyValueEntity.getRangeMin());
                            }
                            if (!TextUtils.isEmpty(keyValueEntity.getRangeMax())) {
                                data.put("wedding_date_end", keyValueEntity.getRangeMax());
                            }
                        }
                        break;
                    case CONTACT:
                        String wechat = keyValueEntity.getWechat();

                        String phone = keyValueEntity.getSeePhone();

                        if (TextUtils.isEmpty(wechat)&&TextUtils.isEmpty(phone) && "1".equals(keyValueEntity.getIs_true())) {
                            TipsToast.INSTANCE.showTips("联系方式至少填一项");
                            return null;
                        }
                        if (!TextUtils.isEmpty(keyValueEntity.getPhone())) {
                            if (StringExt.INSTANCE.isPhone(keyValueEntity.getPhone())){
                                data.put("phone", keyValueEntity.getPhone());
                            }
                            else {
                                TipsToast.INSTANCE.showTips("手机号格式不正确");
                                return null;
                            }
                        }
                        if (!TextUtils.isEmpty(keyValueEntity.getWechat())) {
                            data.put("wechat", keyValueEntity.getWechat());
                        }
                        break;
                    case COLLECTION_MULTIPLE_LEVEL:
                        if (!TextUtils.isEmpty(keyValueEntity.getSubValue())) {
                            value = keyValueEntity.getSubValue();
                        }
                        if (TextUtils.isEmpty(value) && "1".equals(keyValueEntity.getIs_true()) && itemType != ItemType.READ_ONLY) {
                            String prefix = TextUtils.isEmpty(getAlertPrefix(keyValueEntity)) ? "请输入" : getAlertPrefix(keyValueEntity);
                            TipsToast.INSTANCE.showTips(prefix + keyValueEntity.getName());
                            return null;
                        }
                        if (!TextUtils.isEmpty(value)) {
                            data.put(keyValueEntity.getParamKey(), value);
                        }
                        break;
                    default:
                        if (TextUtils.isEmpty(value) && "1".equals(keyValueEntity.getIs_true()) && itemType != ItemType.READ_ONLY) {
                            String prefix = TextUtils.isEmpty(getAlertPrefix(keyValueEntity)) ? "请输入" : getAlertPrefix(keyValueEntity);
                            TipsToast.INSTANCE.showTips(prefix + keyValueEntity.getName());
                            return null;
                        }
                        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(keyValueEntity.getParamKey())) {
                            data.put(keyValueEntity.getParamKey(), value);
                        }
                        if (!TextUtils.isEmpty(keyValueEntity.getSubParamKey())&& !TextUtils.isEmpty(keyValueEntity.getSubValue())){
                            data.put(keyValueEntity.getSubParamKey(), keyValueEntity.getSubValue());
                        }
                        break;
                }
            }
        }
        return data;
    }

    public List<String> getAttachment(){
        List<String> attachment = new ArrayList<>();
        if (kvList == null)
            return null;
        for (KeyValueEntity valueEntity : kvList) {
            if (getItemType(valueEntity) == ItemType.UPLOAD){
                return valueEntity.getAttach();
            }
        }
        return attachment;

    }






    /**
     * 查找itemView的分割线view
     *
     * @param keyValueEntity
     * @return
     */
    private View findItemViewLine(KeyValueEntity keyValueEntity) {
        return findViewById(generateItemLineId(keyValueEntity));
    }

    /**
     * 刷新所有item分割线
     * <p>
     * 触发：刷新所有item或单个item都需要调用该方法刷新，因为可能存在联动显示
     * <p>
     * 规则：显示的last item隐藏分割线，剩余显示item显示分割线
     */
    private void refreshItemLines() {
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
        ItemType itemType = getItemType(keyValueEntity);
        switch (itemType){
            case INPUT:
            case AMOUNT:
            case NUMBER:
            case PHONE:
            case CUSTOMER_SELECT:
                setItemValueForInput(itemView, keyValueEntity);
                break;
            case CONTACT:
                setItemValueForContact(itemView, keyValueEntity);
                break;
            case DATE_TIME_RANGE:
            case DATE_TIME_RANGE_FILTER:
                setItemValueForRangeTime(itemView, keyValueEntity);
                break;
            case COLLECTION:
            case COLLECTION_MULTIPLE:
                setItemValueForFlowButtons(itemView, keyValueEntity);
                break;
            case COLLECTION_MULTIPLE_LEVEL:
                setItemValueForFlowButtonsWithChild(itemView,keyValueEntity);
                break;
            case FOLLOW_RESULT:
                setItemValueForFollowResult(itemView, keyValueEntity);
                break;
            case UPLOAD:
                setItemValueForUpload(itemView,keyValueEntity);
                break;
            default:
                setItemValueForNormal(itemView, keyValueEntity);
                break;
        }
        setItemEvent(keyValueEntity);
    }

    private void setItemValueForUpload(View itemView, KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        RecyclerView kv_edit_value_image_rv = itemView.findViewById(R.id.rv_image);
        kv_edit_key_tv.setText(keyValueEntity.getName());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        kv_edit_value_image_rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        AttachImageAdapter adapter = new AttachImageAdapter(AttachImageAdapter.MODE_EDIT);
        View addImageView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_item_attach,kv_edit_value_image_rv,false);
        ImageView imageView = addImageView.findViewById(R.id.imageview);
        imageView.setImageResource(R.mipmap.ic_photo_add_dark);
        ViewUtils.INSTANCE.setClipViewCornerRadius(imageView, DpToPx.dpToPx(14));
        adapter.addFootView(addImageView,-1);
        adapter.setSpanCount(2);
        kv_edit_value_image_rv.setAdapter(adapter);
        if (!TextUtils.isEmpty(keyValueEntity.getValue())){
            String[] images = keyValueEntity.getValue().split(",");
            adapter.setData(Arrays.asList(images));
        }else {
            adapter.setData(null);
        }
    }


    /**
     * FlowButton选择器赋值
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForFlowButtons(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);

        kv_edit_key_tv.setText(keyValueEntity.getName());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        ItemType action = getItemType(keyValueEntity);
        final List<String> values = new ArrayList<>();
        if (ItemType.COLLECTION_MULTIPLE == action) {
            kv_edit_key_buttons_fl.setLabelSelect(FlowLayout.LabelSelect.MULTI);
            if (!TextUtils.isEmpty(keyValueEntity.getValue())) {
                if (keyValueEntity.getValue().contains(SEPARATOR)) {
                    String[] valuesArray = keyValueEntity.getValue().split(SEPARATOR);
                    if (valuesArray.length > 0) {
                        values.addAll(Arrays.asList(valuesArray));
                    }
                } else {
                    values.add(keyValueEntity.getValue());
                }
            }
        } else {
            kv_edit_key_buttons_fl.setLabelSelect(FlowLayout.LabelSelect.SINGLE);
            if (!TextUtils.isEmpty(keyValueEntity.getValue())) {
                values.add(keyValueEntity.getValue());
            }
        }
        kv_edit_key_buttons_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {


                return keyValueEntity.getOption().size();
            }

            @Override
            public String getLabelText(int position) {
                return keyValueEntity.getOption().get(position).getName();
            }

            @Override
            public boolean isSelect(int position) {
                String defValue = keyValueEntity.getOption().get(position).getValue();
                if (values.contains(defValue)) {
                    return true;
                }
                return false;
            }

        });
    }
    private void setItemValueForFlowButtonsWithChild(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);
        FlowLayout kv_edit_key_buttons_fl_child = itemView.findViewById(R.id.kv_edit_key_buttons_fl_child);

        kv_edit_key_tv.setText(keyValueEntity.getName());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        kv_edit_key_buttons_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return keyValueEntity.getOption().size();
            }

            @Override
            public String getLabelText(int position) {
                return keyValueEntity.getOption().get(position).getLabel();
            }

            @Override
            public boolean isSelect(int position) {
                return keyValueEntity.getOption().get(position).getValue().equals(keyValueEntity.getValue());
            }
        });

        int selectPosition = -1;
        for (KeyValueEntity valueEntity : keyValueEntity.getOption()) {
            if (valueEntity.getValue().equals(keyValueEntity.getValue())){
                selectPosition = keyValueEntity.getOption().indexOf(valueEntity);
                if (valueEntity.getChildren()!=null && valueEntity.getChildren().size()>0){
                    kv_edit_key_buttons_fl_child.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
                        @Override
                        public int getSize() {
                            return valueEntity.getChildren() == null?0:valueEntity.getChildren().size();
                        }

                        @Override
                        public String getLabelText(int position) {
                            return valueEntity.getChildren().get(position).getLabel();
                        }

                        @Override
                        public boolean isSelect(int position) {
                            return valueEntity.getChildren().get(position).getValue().equals(keyValueEntity.getSubValue());
                        }
                    });
                }
                break;
            }
        }
        if (selectPosition!=-1&&keyValueEntity.getOption().get(selectPosition).getChildren()!=null&&keyValueEntity.getOption().get(selectPosition).getChildren().size()>0){
            kv_edit_key_buttons_fl_child.setVisibility(VISIBLE);
        }else {
            kv_edit_key_buttons_fl_child.setVisibility(GONE);
        }
    }

    /**
     * 跟进结果视图赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForFollowResult(View itemView, final KeyValueEntity keyValueEntity) {
        FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_result_fl);
        TextView kv_edit_value_text_et = itemView.findViewById(R.id.kv_edit_value_text_et);
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
        final List<String> values = new ArrayList<>();
        if (!TextUtils.isEmpty(keyValueEntity.getValue())) {
            values.add(keyValueEntity.getValue());
        }
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
                keyValueEntity.setSubValue(value);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
        kv_edit_key_buttons_fl.setLabelSelect(FlowLayout.LabelSelect.SINGLE);
        kv_edit_key_buttons_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {


                return keyValueEntity.getOption().size();
            }

            @Override
            public String getLabelText(int position) {
                return keyValueEntity.getOption().get(position).getName();
            }

            @Override
            public boolean isSelect(int position) {
                String defValue = keyValueEntity.getOption().get(position).getValue();
                if (values.contains(defValue)) {
                    return true;
                }
                return false;
            }

        });
    }

    /**
     * 文本输入item赋值
     * @param itemView 文本输入框
     * @param keyValueEntity 数据
     */
    @SuppressLint("DiscouragedPrivateApi")
    private void setItemValueForInput(View itemView, KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_text_et = itemView.findViewById(R.id.kv_edit_value_text_et);
        final TextView kv_edit_choose_tv = itemView.findViewById(R.id.kv_edit_choose_tv);
        kv_edit_key_tv.setText(keyValueEntity.getName());
        switch (getItemType(keyValueEntity)){
            case NUMBER:
                kv_edit_value_text_et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case PHONE:
                kv_edit_value_text_et.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case AMOUNT:
                kv_edit_value_text_et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;

        }
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
        String value = keyValueEntity.getDefaultValue();
        kv_edit_value_text_et.setHint(getAlertPrefix(keyValueEntity) + keyValueEntity.getName());
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
                keyValueEntity.setDefaultValue(value);
                keyValueEntity.setValue(value);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
        if (getItemType(keyValueEntity) == ItemType.CUSTOMER_SELECT){
            kv_edit_choose_tv.setVisibility(VISIBLE);
        }
    }

    /**
     * 联系方式item赋值
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

        kv_edit_value_phone_et.setOnFocusChangeListener((view, b) -> {
            if (b){
                kv_edit_value_phone_et.setText("");
            }
        });
        kv_edit_key_tv.setText(keyValueEntity.getName());
        kv_edit_value_phone_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        kv_edit_value_wechat_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        String defaultValue = keyValueEntity.getDefaultValue();
        String value = keyValueEntity.getValue();
        String phone = "";
        String wechat = "";
        String seePhone = "";

        if (!TextUtils.isEmpty(defaultValue) && defaultValue.contains(SEPARATOR) && defaultValue.split(SEPARATOR).length > 0 && defaultValue.split(SEPARATOR).length <= 2) {
            if (defaultValue.split(SEPARATOR).length == 1) {
                phone = defaultValue.split(SEPARATOR)[0];
                wechat = "";
            } else {
                phone = defaultValue.split(SEPARATOR)[0];
                wechat = defaultValue.split(SEPARATOR)[1];
            }
        }
        if (!TextUtils.isEmpty(value) && value.contains(SEPARATOR) && value.split(SEPARATOR).length > 0 && value.split(SEPARATOR).length <= 2) {
            if (value.split(SEPARATOR).length == 1) {
                seePhone = value.split(SEPARATOR)[0];
                wechat = "";
            } else {
                seePhone = value.split(SEPARATOR)[0];
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
        keyValueEntity.setSeePhone(seePhone);
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
                keyValueEntity.setValue(phone + SEPARATOR + keyValueEntity.getWechat());
                keyValueEntity.setDefaultValue(keyValueEntity.getValue());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
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
                keyValueEntity.setValue(keyValueEntity.getPhone() + SEPARATOR + wechat);
                keyValueEntity.setDefaultValue(keyValueEntity.getValue());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });

    }

    /**
     * 范围时间item赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForRangeTime(View itemView, KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        TextView kv_edit_value_start_time_tv = itemView.findViewById(R.id.kv_edit_value_start_time_tv);
        TextView kv_edit_value_end_time_tv = itemView.findViewById(R.id.kv_edit_value_end_time_tv);
        ImageView kv_edit_value_start_time_arrow = itemView.findViewById(R.id.kv_edit_value_start_time_arrow);
        ImageView kv_edit_value_end_time_arrow = itemView.findViewById(R.id.kv_edit_value_end_time_arrow);

        kv_edit_key_tv.setText(keyValueEntity.getName());
        if (isItemOption(keyValueEntity))
            kv_edit_key_tv.setTextColor(Color.parseColor("#FF4A4C5C"));
        else
            kv_edit_key_tv.setTextColor(Color.parseColor("#FFC5C5CE"));
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        String timeRange = keyValueEntity.getDefaultValue();
        String startTime = "";
        String endTime = "";
        if (!TextUtils.isEmpty(timeRange) && timeRange.contains(SEPARATOR) && timeRange.split(SEPARATOR).length > 0 && timeRange.split(SEPARATOR).length <= 2) {
            if (timeRange.split(SEPARATOR).length == 1) {
                startTime = timeRange.split(SEPARATOR)[0];
                endTime = "";
            } else {
                startTime = timeRange.split(SEPARATOR)[0];
                endTime = timeRange.split(SEPARATOR)[1];
            }
        }
        if (!TextUtils.isEmpty(startTime)) {
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#8B8B99"));
            kv_edit_value_start_time_tv.setText(startTime);
        } else {
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_start_time_tv.setText(getAlertPrefix(keyValueEntity) + "起始时间");
        }
        if (!TextUtils.isEmpty(endTime)) {
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#8B8B99"));
            kv_edit_value_end_time_tv.setText(endTime);
        } else {
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_end_time_tv.setText(getAlertPrefix(keyValueEntity) + "结束时间");
        }

        if (isItemOption(keyValueEntity)){
            kv_edit_key_tv.setTextColor(Color.parseColor("#FF4A4C5C"));
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#8B8B99"));
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#8B8B99"));
            kv_edit_value_end_time_arrow.setVisibility(VISIBLE);
            kv_edit_value_start_time_arrow.setVisibility(VISIBLE);
        } else{
            kv_edit_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_end_time_arrow.setVisibility(GONE);
            kv_edit_value_start_time_arrow.setVisibility(GONE);

        }
        //本地标记minRange和maxRange
        keyValueEntity.setRangeMin(startTime);
        keyValueEntity.setRangeMax(endTime);
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
        kv_edit_key_tv.setText(keyValueEntity.getName());

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
        if ("channe".equals(keyValueEntity.getParamKey())||"channel".equals(keyValueEntity.getParamKey())){
            KeyValueEntity recordEntity = findEntityByParamKey("record_user_id");
            if (recordEntity != null) {
                recordEntity.setChannelId(keyValueEntity.getValue());
            }else {
                recordEntity = findEntityByParamKey("ownerId");
                if (recordEntity!=null)
                    recordEntity.setChannelId(keyValueEntity.getValue());
            }
        }
        if (!isItemOption(keyValueEntity)) {
            kv_edit_value_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_key_tv.setTextColor(Color.parseColor("#C5C5CE"));
            if (TextUtils.isEmpty(keyValueEntity.getDefaultValue())) {
                kv_edit_value_tv.setText("未填写");
            } else {
                kv_edit_value_tv.setText(keyValueEntity.getDefaultValue());
            }
        } else {
            kv_edit_key_tv.setTextColor(Color.parseColor("#4A4C5C"));
            if (TextUtils.isEmpty(keyValueEntity.getDefaultValue())) {
                kv_edit_value_tv.setTextColor(Color.parseColor("#C5C5CE"));
                kv_edit_value_tv.setText(getAlertPrefix(keyValueEntity) + keyValueEntity.getName());
            } else {
                kv_edit_value_tv.setTextColor(Color.parseColor("#8B8B99"));
                kv_edit_value_tv.setText(keyValueEntity.getDefaultValue());
            }
        }
    }

    /**
     * 设置item事件
     *
     * @param keyValueEntity
     */
    private void setItemEvent(KeyValueEntity keyValueEntity) {
        ItemType itemType = getItemType(keyValueEntity);
        if (ItemType.SINGLE_SELECT == itemType || ItemType.PLAN_TYPE == itemType) {  //单选
            singleSelectAction(keyValueEntity);
        }else if (ItemType.MULTI_SELECT == itemType){
            multiSelectAction(keyValueEntity);
        } else if (ItemType.CHANNEL == itemType || ItemType.CHANNEL_FILTER == itemType){//渠道选择
            channelAction(keyValueEntity);
        }else if (ItemType.RECORD_USER == itemType){
            recordUserAction(keyValueEntity);
        }else if (ItemType.COMPANY_USER == itemType){
            companyUserAction(keyValueEntity);
        } else if (ItemType.DATE_TIME_RANGE == itemType || ItemType.DATE_TIME_RANGE_FILTER == itemType){
            timeRangeAction(keyValueEntity);
        }else if (ItemType.COLLECTION_MULTIPLE == itemType) {  //多选
            multipleCollectionAction(keyValueEntity);
        } else if (ItemType.COLLECTION == itemType) {  //单选
            collectionAction(keyValueEntity);
        }else if (ItemType.COLLECTION_MULTIPLE_LEVEL == itemType) {  //多级单选
            collectionWithChildAction(keyValueEntity);
        }
        else if (ItemType.DATE_TIME == itemType || ItemType.DATE_TIME2 == itemType){
            timeAction(keyValueEntity);
        }else if (ItemType.FOLLOW_RESULT == itemType) {
            followResultAction(keyValueEntity);
        }else if (ItemType.CUSTOMER_SELECT == itemType) {
            customerSelectAction(keyValueEntity);
        }else if (ItemType.CITY == itemType) {
            citySelect(keyValueEntity);
        }else if (ItemType.UPLOAD == itemType){
            uploadAction(keyValueEntity);
        }
    }

    private void uploadAction(KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        if (!isItemOption(keyValueEntity)) {
            return;
        }
        RecyclerView rvImages = itemView.findViewById(R.id.rv_image);
        AttachImageAdapter adapter = (AttachImageAdapter) rvImages.getAdapter();
        assert adapter != null;
        View addImageView = Objects.requireNonNull(adapter.getFooterBinding()).findViewById(R.id.imageview);
        addImageView.setOnClickListener(view -> {
            if (listener!=null){
                listener.onEvent(keyValueEntity,getItemType(keyValueEntity));
            }
        });
    }

    /**
     * 城市选择
     * @param keyValueEntity
     */
    private void citySelect(KeyValueEntity keyValueEntity) {
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
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new SelectCityDialog
                        .Builder(getContext())
                        .setOnConfirmListener(cityEntity -> {
                            keyValueEntity.setValue(cityEntity.getCode());
                            keyValueEntity.setDefaultValue(cityEntity.getName());
                            refreshItem(keyValueEntity);
                            return null;
                        })
                        .create().show();
            }
        });
    }


    /**
     * 选择客户
     * @param keyValueEntity
     */
    private void customerSelectAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        TextView kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_choose_tv);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType())) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerServiceProvider.chooseCustomer(getContext());
            }
        });
    }

    /**
     * 时间选择器
     * @param keyValueEntity
     */
    private void timeAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType())) {
            return;
        }

        //时间处理
        String dateFormatter = keyValueEntity.getDateFormatter();
        if (TextUtils.isEmpty(dateFormatter))
            dateFormatter = "yyyy-MM-dd HH:mm:ss";
        dateFormatter.replace("ii","ss");
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat format = new SimpleDateFormat(dateFormatter);
        String finalDateFormatter = dateFormatter;
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectCalendar = Calendar.getInstance();
                if (!TextUtils.isEmpty(keyValueEntity.getDefaultValue())) {
                    try {
                        Date date = format.parse(keyValueEntity.getDefaultValue());
                        selectCalendar.setTime(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                DateDialogUtils.createDateTimePickerView(getContext(), "请选择"+keyValueEntity.getName(),(date, v1) -> {
                    String selectTime = format.format(date);
                    //更新item
                    keyValueEntity.setDefaultValue(selectTime);
                    keyValueEntity.setValue(selectTime);
                    refreshItem(keyValueEntity);
                    if (listener != null) {
                        listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                    }
                }, finalDateFormatter).show();
            }
        });
    }

    /**
     * FlowButton选择器：单选
     * @param keyValueEntity
     */
    private void collectionAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);
        if (kv_edit_key_buttons_fl == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType()) || keyValueEntity.getOption() == null) {
            return;
        }
        kv_edit_key_buttons_fl.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0 && values.get(0) < keyValueEntity.getOption().size()) {
                    final KeyValueEntity option = keyValueEntity.getOption().get(values.get(0));
                    keyValueEntity.setValue(option.getValue());
                } else {
                    keyValueEntity.setValue("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
    }

    private void collectionWithChildAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);
        final FlowLayout kv_edit_key_buttons_fl_child = itemView.findViewById(R.id.kv_edit_key_buttons_fl_child);
        if (kv_edit_key_buttons_fl == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType()) || keyValueEntity.getOption() == null) {
            return;
        }
        kv_edit_key_buttons_fl.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0 && values.get(0) < keyValueEntity.getOption().size()) {
                    final KeyValueEntity option = keyValueEntity.getOption().get(values.get(0));
                    keyValueEntity.setValue(option.getValue());
                } else {
                    keyValueEntity.setValue("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
        kv_edit_key_buttons_fl_child.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0 && values.get(0) < keyValueEntity.getOption().size()) {
                    final KeyValueEntity valueEntity = keyValueEntity.getOption().get(values.get(0));
                    List<KeyValueEntity> children = valueEntity.getChildren();
                    List<Integer> childValues = kv_edit_key_buttons_fl_child.getSelectLabelsIndex();
                    if (childValues.size() > 0 && childValues.get(0) < children.size()){
                        KeyValueEntity child = children.get(childValues.get(0));
                        keyValueEntity.setSubValue(child.getValue());
                    }
                } else {
                    keyValueEntity.setSubValue("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
    }

    /**
     * 跟进结果操作
     *
     * @param keyValueEntity
     */
    private void followResultAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_result_fl);
        if (kv_edit_key_buttons_fl == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType()) || keyValueEntity.getOption() == null) {
            return;
        }
        kv_edit_key_buttons_fl.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0 && values.get(0) < keyValueEntity.getOption().size()) {
                    final KeyValueEntity option = keyValueEntity.getOption().get(values.get(0));
                    keyValueEntity.setValue(option.getValue());
                } else {
                    keyValueEntity.setValue("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
    }

    /**
     * 按钮多选操作
     *
     * @param keyValueEntity
     */
    private void multipleCollectionAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        final FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);
        if (kv_edit_key_buttons_fl == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType()) || keyValueEntity.getOption() == null) {
            return;
        }
        kv_edit_key_buttons_fl.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < values.size(); i++) {
                        int pos = values.get(i);
                        if (pos < keyValueEntity.getOption().size()) {
                            final KeyValueEntity option = keyValueEntity.getOption().get(pos);
                            if (i == values.size() - 1) {
                                buffer.append(option.getValue());
                            } else {
                                buffer.append(option.getValue() + SEPARATOR);
                            }
                        }
                    }
                    keyValueEntity.setValue(buffer.toString());
                } else {
                    keyValueEntity.setValue("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                }
            }
        });
    }

    /**
     * 时间区间操作
     *
     * @param keyValueEntity
     */
    private void timeRangeAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        TextView kv_edit_value_start_time_tv = itemView.findViewById(R.id.kv_edit_value_start_time_tv);
        TextView kv_edit_value_end_time_tv = itemView.findViewById(R.id.kv_edit_value_end_time_tv);
        if (kv_edit_value_start_time_tv == null || kv_edit_value_end_time_tv == null) {
            return;
        }
        if (TextUtils.isEmpty(keyValueEntity.getType())) {
            return;
        }
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        kv_edit_value_start_time_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar startCalendar = Calendar.getInstance();
                String startTime = keyValueEntity.getRangeMin();
                try {
                    startCalendar.setTime(format.parse(startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DateDialogUtils.INSTANCE.createDatePickerView(getContext(), "请选择开始日期",new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String selectTime = format.format(date);
                        keyValueEntity.setRangeMin(selectTime);
                        //更新item
                        String timeRange = selectTime + SEPARATOR + keyValueEntity.getRangeMax();
                        keyValueEntity.setDefaultValue(timeRange);
                        keyValueEntity.setValue(timeRange);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                        }
                    }
                }).show();
            }
        });
        kv_edit_value_end_time_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endCalendar = Calendar.getInstance();
                String endTime = keyValueEntity.getRangeMax();
                try {
                    endCalendar.setTime(format.parse(endTime));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateDialogUtils.INSTANCE.createDatePickerView(getContext(), "请选择结束日期",new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String selectTime = format.format(date);
                        keyValueEntity.setRangeMax(selectTime);
                        String timeRange = keyValueEntity.getRangeMin() + SEPARATOR + selectTime;
                        //更新item
                        keyValueEntity.setDefaultValue(timeRange);
                        keyValueEntity.setValue(timeRange);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                        }
                    }
                }).show();
            }
        });
    }


    /**
     * 获取公司人员
     * @param keyValueEntity
     */
    private void companyUserAction(KeyValueEntity keyValueEntity){
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
            String defaultVal = keyValueEntity.getValue();
            new SelectCompanyUserDialog.Builder(getContext())
                    .setOnConfirmClickListener((dialog, value, name) -> {
                        keyValueEntity.setValue(value);
                        keyValueEntity.setDefaultValue(name);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                        }
                        dialog.dismiss();
                        return null;
                    })
                    .setDefaultValue(defaultVal)
                    .create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });
    }

    /**
     * 提供人
     * @param keyValueEntity
     */
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
            String defaultVal = keyValueEntity.getValue();
            String channelId = keyValueEntity.getChannelId();
            if (TextUtils.isEmpty(channelId)){
                TipsToast.INSTANCE.showTips("请选择渠道来源");
                return;
            }
            new SelectRecordUserDialog.Builder(getContext(),channelId)
                    .setOnConfirmClickListener((dialog, value, name) -> {
                        keyValueEntity.setValue(value);
                        keyValueEntity.setDefaultValue(name);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                        }
                        dialog.dismiss();
                        return null;
                    })
                    .setDefaultValue(defaultVal)
                    .create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });
    }

    /**
     * 渠道来源
     * @param keyValueEntity
     */
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
            String defaultVal = keyValueEntity.getValue();
            new SelectChannelDialog.Builder(getContext())
                    .setOnConfirmClickListener((dialog, id, name) -> {
                        keyValueEntity.setValue(String.valueOf(id));
                        keyValueEntity.setDefaultValue(name);
                        KeyValueEntity recordEntity = findEntityByParamKey("record_user_id");
                        if (recordEntity != null) {
                            recordEntity.setChannelId(String.valueOf(id));
                        }else {
                            recordEntity = findEntityByParamKey("ownerId");
                            if (recordEntity!=null)
                                recordEntity.setChannelId(String.valueOf(id));
                        }
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemType(keyValueEntity));
                        }
                        dialog.dismiss();
                        return null;
                    })
                    .setDefaultValue(defaultVal)
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
            final List<KeyValueEntity> options = keyValueEntity.getOption();
            String defaultVal = keyValueEntity.getValue();
            new BottomSelectDialog.Builder(getContext())
                    .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getName())
                    .setData(options,defaultVal)
                    .setOnConfirmClickListener((dialog, value, name) -> {
                        keyValueEntity.setValue(value);
                        keyValueEntity.setDefaultValue(name);
                        refreshItem(keyValueEntity);
                        return null;
                    })
                    .create().show();
            kv_edit_value_ll.setFocusableInTouchMode(false);

        });
    }

    /**
     * 二级选择
     * @param keyValueEntity
     */
    private void multiSelectAction(final KeyValueEntity keyValueEntity) {
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
            final List<KeyValueEntity> options = keyValueEntity.getOption();
            String defaultVal = keyValueEntity.getValue();
            new BottomSelectMultiDialog.Builder(getContext())
                    .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getName())
                    .setData(options,defaultVal)
                    .setOnConfirmClickListener((dialog, value, label) -> {
                        keyValueEntity.setValue(value);
                        keyValueEntity.setDefaultValue(label);
                        refreshItem(keyValueEntity);
                        return null;
                    })
                    .create().show();
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
        if (keyValueEntity.getIs_open() == null || "1".equals(keyValueEntity.getIs_open())) {
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
        if ("1".equals(keyValueEntity.getIs_true())) {
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
        if (TextUtils.isEmpty(keyValueEntity.getType())) {
            return false;
        }
        String action = keyValueEntity.getType();
        if (ItemType.READ_ONLY.valueOf().equals(action)) {
            return false;
        } else {
            return "2".equals(keyValueEntity.getIs_channge());
        }

    }


    /**
     * 获取提示前缀
     * @param keyValueEntity
     * @return
     */
    private   String getAlertPrefix(KeyValueEntity keyValueEntity) {
        ItemType itemAction = getItemType(keyValueEntity);
        String prefix;
        switch (itemAction) {
            case INPUT:
            case CONTACT:
            case AMOUNT:
                prefix = "请输入";
                break;
            default:
                prefix = "请选择";

        }
        return prefix;
    }

    /**
     * 获取item操作动作
     *
     * @param keyValueEntity
     * @return
     */
    private   ItemType getItemType(KeyValueEntity keyValueEntity) {
        String type = keyValueEntity.getType();
        if (ItemType.INPUT.valueOf().equals(type)) {
            return ItemType.INPUT;
        } else if (ItemType.CONTACT.valueOf().equals(type)) {
            return ItemType.CONTACT;
        } else if (ItemType.SINGLE_SELECT.valueOf().equals(type)||ItemType.PLAN_TYPE.valueOf().equals(type)) {
            return ItemType.SINGLE_SELECT;
        } else if (ItemType.CHANNEL.valueOf().equals(type)) {
            return ItemType.CHANNEL;
        }else if (ItemType.RECORD_USER.valueOf().equals(type)) {
            return ItemType.RECORD_USER;
        } else if (ItemType.DATE_TIME_RANGE.valueOf().equals(type)) {
            return ItemType.DATE_TIME_RANGE;
        }else if (ItemType.DATE_TIME.valueOf().equals(type)) {
            return ItemType.DATE_TIME;

        }else if (ItemType.DATE_TIME2.valueOf().equals(type)) {
            return ItemType.DATE_TIME2;
        } else if (ItemType.COLLECTION.valueOf().equals(type)) {
            return ItemType.COLLECTION;
        }else if (ItemType.COLLECTION_MULTIPLE.valueOf().equals(type)) {
            return ItemType.COLLECTION_MULTIPLE;
        }else if (ItemType.MULTI_SELECT.valueOf().equals(type)) {
            return ItemType.MULTI_SELECT;
        } else if (ItemType.AMOUNT.valueOf().equals(type)) {
            return ItemType.AMOUNT;
        }else if (ItemType.COMPANY_USER.valueOf().equals(type)) {
            return ItemType.COMPANY_USER;
        }else if (ItemType.FOLLOW_RESULT.valueOf().equals(type)) {
            return ItemType.FOLLOW_RESULT;
        }else if (ItemType.CUSTOMER_SELECT.valueOf().equals(type)) {
            return ItemType.CUSTOMER_SELECT;
        }else if (ItemType.CITY.valueOf().equals(type)) {
            return ItemType.CITY;
        }else if (ItemType.COLLECTION_MULTIPLE_LEVEL.valueOf().equals(type)) {
            return ItemType.COLLECTION_MULTIPLE_LEVEL;
        }else if (ItemType.CHANNEL_FILTER.valueOf().equals(type)) {
            return ItemType.CHANNEL_FILTER;
        }else if (ItemType.DATE_TIME_RANGE_FILTER.valueOf().equals(type)) {
            return ItemType.DATE_TIME_RANGE_FILTER;
        }else if (ItemType.UPLOAD.valueOf().equals(type)) {
            return ItemType.UPLOAD;
        }else if (ItemType.NUMBER.valueOf().equals(type)) {
            return ItemType.NUMBER;
        }else if (ItemType.PHONE.valueOf().equals(type)) {
            return ItemType.PHONE;
        }
        else {
            return ItemType.READ_ONLY;
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
        ItemType itemType = getItemType(keyValueEntity);
        View childView;
        switch (itemType){
            case INPUT:
            case NUMBER:
            case PHONE:
            case AMOUNT:
            case CUSTOMER_SELECT:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_input_item, this, false);
                break;
            case CONTACT:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_contact_item, this, false);
                break;
            case DATE_TIME_RANGE:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_date_range,this,false);
                break;
            case DATE_TIME_RANGE_FILTER:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_date_range_filter,this,false);
                break;
            case COLLECTION:
            case COLLECTION_MULTIPLE:
            case COLLECTION_MULTIPLE_LEVEL:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_eidt_flowbutton_item, this, false);
                break;
            case FOLLOW_RESULT:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_eidt_follow_result_item, this, false);
                break;
            case CHANNEL_FILTER:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_eidt_channel_filter_item, this, false);
                break;
            case UPLOAD:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_eidt_upload_item,this,false);
                break;
            default:
                childView = LayoutInflater.from(getContext()).inflate(R.layout.layout_keyvalue_edit_select_item, this, false);
                break;
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
         * @param itemType
         */
        public abstract void onEvent(KeyValueEntity keyValueEntity, ItemType itemType);
    }

}
