package com.baihe.lihepro.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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

import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.dialog.AdjustDialog;
import com.baihe.lihepro.dialog.BottomMultipleSelectDialog;
import com.baihe.lihepro.dialog.BottomSelectDialog;
import com.baihe.lihepro.dialog.ChannelDialog;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.dialog.HallSelectDialog;
import com.baihe.lihepro.dialog.HotelSelectDialog;
import com.baihe.lihepro.dialog.PersonSelectDialog;
import com.baihe.lihepro.dialog.RecordUserSelectDialog;
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.utils.CitySelectUtils;
import com.baihe.lihepro.utils.InputEditUtils;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：1005 1002
 */
public class KeyValueEditLayout extends LinearLayout {
    private static final String KV_CHILD_TAG = "KV_CHILD_TAG";
    private static final String RANGE_SEPARATOR = "&&";
    private static final String MULTIPLE_SELECT_SEPARATOR = ",";

    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_RANGE = 2;
    private static final int ITEM_TYPE_RANGE_TIME = 3;
    private static final int ITEM_TYPE_MOBILE = 4;
    private static final int ITEM_TYPE_FLOWBUTTON = 5;
    private static final int ITEM_TYPE_PRICE = 6;

    public void setOnItemActionCheckListener(OnItemActionCheckListener onItemActionCheckListener) {
        this.onItemActionCheckListener = onItemActionCheckListener;
    }

    public enum ItemAction {
        //输入
        INPUT("input"),
        //输入
        TEXT("text"),
        //手机号
        MOBILE("mobile"),
        //金额
        PRICE("amount"),
        //金额
        CHANNEL("channel"),
        //日期时间选择
        DATETIME("datetime"),
        //日期时间选择范围
        DATETIMERANGE("datetimeRange"),
        //文字项范围
        RANGE("range"),
        //多选
        MULTIPLESELECT("multipleSelect"),
        //单选
        SELECT("select"),
        //按钮多选
        COLLECTIONMULTIPLE("collectionMultiple"),
        //按钮单选
        COLLECTION("collection"),
        //城市选择
        CITY("city"),
        //人员选择
        SELECTPERSONNEL("selectPersonnel"),
        //酒店选择
        SELECTHOTEL("hotelType"),
        SELECTHALL("hotelHall"),
        SELECTSCHEDULE("newselect"),
        //内部提供人
        SELECTRECORDUSER("recordUserList"),
        //调节器
        ADJUST("adjust"),
        //不可编辑
        READONLY("readonly");

        String value;

        ItemAction(String value) {
            this.value = value;
        }

        public String valueOf() {
            return value;
        }
    }

    private Context context;
    private List<KeyValueEntity> kvList;
    private OnItemActionListener listener;
    private OnItemActionCheckListener onItemActionCheckListener;

    public KeyValueEditLayout(Context context) {
        this(context, null);
    }

    public KeyValueEditLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyValueEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
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
     * 查找itemView
     *
     * @param keyValueEntity
     * @return
     */
    public View findItemView(KeyValueEntity keyValueEntity) {
        return findViewById(generateItemId(keyValueEntity));
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
     * 通过key找对象
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
    public Map<String, Object> commit() {
        return getCommitMap(kvList);
    }

    /**
     * 获取提交数据
     *
     * @param kvList
     * @return
     */
    public static Map<String, Object> getCommitMap(List<KeyValueEntity> kvList) {
        Map<String, Object> data = new HashMap<>();
        if (kvList == null) {
            return data;
        }
        for (KeyValueEntity keyValueEntity : kvList) {
            //可提交的数据
            if ("1".equals(keyValueEntity.getShowStatus())) {
                String value = keyValueEntity.getDefaultVal();
                ItemAction itemAction = getItemAction(keyValueEntity);
                if (itemAction == ItemAction.RANGE) {   //范围输入取标记值
                    if (TextUtils.isEmpty(keyValueEntity.getRangeMin()) && "1".equals(keyValueEntity.getOptional())) {
                        ToastUtils.toast(getAlertPrefix(keyValueEntity) + "最小" + keyValueEntity.getKey());
                        return null;
                    } else if (TextUtils.isEmpty(keyValueEntity.getRangeMax()) && "1".equals(keyValueEntity.getOptional())) {
                        ToastUtils.toast(getAlertPrefix(keyValueEntity) + "最大" + keyValueEntity.getKey());
                        return null;
                    }
                    String[] paramsKeys = keyValueEntity.getEvent().getParamKey().split(RANGE_SEPARATOR);
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMin())) {
                        data.put(paramsKeys[0], keyValueEntity.getRangeMin());
                    }
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMax())) {
                        data.put(paramsKeys[1], keyValueEntity.getRangeMax());
                    }
                } else if (itemAction == ItemAction.DATETIMERANGE) {
                    //范围时间取标记值
                    String startTime = keyValueEntity.getRangeMin();
                    String endTime = keyValueEntity.getRangeMax();
                    String timeFormat = getTimeFormat(keyValueEntity.getEvent().getFormat());

                    SimpleDateFormat format = new SimpleDateFormat(timeFormat);
                    if (TextUtils.isEmpty(startTime) && "1".equals(keyValueEntity.getOptional())) {
                        ToastUtils.toast(getAlertPrefix(keyValueEntity) + "起始时间");
                        return null;
                    }
                    if (TextUtils.isEmpty(endTime) && "1".equals(keyValueEntity.getOptional())) {
                        ToastUtils.toast(getAlertPrefix(keyValueEntity) + "结束时间");
                        return null;
                    }
                    try {
                        if (format.parse(startTime).getTime() > format.parse(endTime).getTime()) {
                            ToastUtils.toast("起始时间不能大于结束时间");
                            return null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] paramsKeys = keyValueEntity.getEvent().getParamKey().split(RANGE_SEPARATOR);
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMin())) {
                        data.put(paramsKeys[0], keyValueEntity.getRangeMin());
                    }
                    if (!TextUtils.isEmpty(keyValueEntity.getRangeMax())) {
                        data.put(paramsKeys[1], keyValueEntity.getRangeMax());
                    }
                } else if (itemAction == ItemAction.ADJUST) {
                    //服务器目前就定死为car，如若优化再改
                    List<Map<String, String>> carList = new ArrayList();
                    //更新item
                    int optionSize = keyValueEntity.getEvent().getOptions() != null ? keyValueEntity.getEvent().getOptions().size() : 0;
                    for (int i = 0; i < optionSize; i++) {
                        KeyValueEntity option = keyValueEntity.getEvent().getOptions().get(i);
                        int num = 0;
                        try {
                            num = TextUtils.isEmpty(option.getCount()) ? 0 : Integer.parseInt(option.getCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (num > 0) {
                            Map<String, String> carMap = new HashMap<>();
                            carMap.put("carBrand", option.getVal());
                            carMap.put("carBrandName", option.getKey());
                            carMap.put("carNum", num + "");
                            carList.add(carMap);
                        }
                    }
                    if ("1".equals(keyValueEntity.getOptional()) && carList.size() == 0) {
                        ToastUtils.toast(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
                        return null;
                    }
                    if (carList.size() > 0) {
                        data.put(keyValueEntity.getEvent().getParamKey(), carList);
                    }
                } else {
                    if (TextUtils.isEmpty(value) && "1".equals(keyValueEntity.getOptional()) && itemAction != ItemAction.READONLY) {
                        String prefix = TextUtils.isEmpty(getAlertPrefix(keyValueEntity)) ? "请输入" : getAlertPrefix(keyValueEntity);
                        ToastUtils.toast(prefix + keyValueEntity.getKey());
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
     * 创建itemView
     *
     * @param keyValueEntity
     * @return
     */
    private View createItemView(final KeyValueEntity keyValueEntity) {
        int viewType = getItemViewType(keyValueEntity);
        View childView;
        if (viewType == ITEM_TYPE_RANGE) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_range_item, this, false);
        } else if (viewType == ITEM_TYPE_RANGE_TIME) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_range_time_item, this, false);
        } else if (viewType == ITEM_TYPE_MOBILE) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_phone_item, this, false);
        } else if (viewType == ITEM_TYPE_FLOWBUTTON) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_flowbutton_item, this, false);
        } else if (viewType == ITEM_TYPE_PRICE) {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_price_item, this, false);
        } else {
            childView = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_item, this, false);
        }
        childView.setTag(KV_CHILD_TAG);
        setItemViewId(childView, keyValueEntity);
        return childView;
    }

    /**
     * 创建itemView分割线
     *
     * @param keyValueEntity
     * @return
     */
    private View createItemViewLine(KeyValueEntity keyValueEntity) {
        View childViewLine = LayoutInflater.from(context).inflate(R.layout.layout_keyvalue_eidt_line_item, this, false);
        setItemViewLineId(childViewLine, keyValueEntity);
        return childViewLine;
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
     * itemView设置id
     *
     * @param childView
     * @param keyValueEntity
     */
    private void setItemViewLineId(View childView, KeyValueEntity keyValueEntity) {
        childView.setId(generateItemLineId(keyValueEntity));
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
            md5.update(identify.getBytes("utf-8"));
            identifyBytes = md5.digest();
        } catch (Exception e) {
            e.printStackTrace();
            identifyBytes = identify.getBytes();
        }
        BigInteger bigInt = new BigInteger(1, identifyBytes);
        return bigInt.intValue();
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
            md5.update(identify.getBytes("utf-8"));
            identifyBytes = md5.digest();
        } catch (Exception e) {
            e.printStackTrace();
            identifyBytes = identify.getBytes();
        }
        BigInteger bigInt = new BigInteger(1, identifyBytes);
        return bigInt.intValue();
    }

    /**
     * item赋值
     *
     * @param keyValueEntity
     */
    private void setItemValue(KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView != null) {
            //当defaultValue为空时表示展示的value却不为空
            //ItemAction为INPUT、TEXT、MOBILE、PRICE、DATETIME、DATETIME、RANGE的对象
            //value需要赋值给defaultValue
            if (TextUtils.isEmpty(keyValueEntity.getDefaultVal()) && !TextUtils.isEmpty(keyValueEntity.getVal())) {
                ItemAction itemAction = getItemAction(keyValueEntity);
                if (itemAction == ItemAction.INPUT || itemAction == ItemAction.TEXT || itemAction == ItemAction.MOBILE || itemAction == ItemAction.PRICE || itemAction == ItemAction.DATETIME || itemAction == ItemAction.DATETIMERANGE || itemAction == ItemAction.RANGE) {
                    keyValueEntity.setDefaultVal(keyValueEntity.getVal());
                }
            }

            int viewType = getItemViewType(keyValueEntity);
            if (viewType == ITEM_TYPE_RANGE) {
                setItemValueForRange(itemView, keyValueEntity);
            } else if (viewType == ITEM_TYPE_RANGE_TIME) {
                setItemValueForRangeTime(itemView, keyValueEntity);
            } else if (viewType == ITEM_TYPE_MOBILE) {
                setItemValueForMobile(itemView, keyValueEntity);
            } else if (viewType == ITEM_TYPE_FLOWBUTTON) {
                setItemValueForFlowButtons(itemView, keyValueEntity);
            } else if (viewType == ITEM_TYPE_PRICE) {
                setItemValueForPrice(itemView, keyValueEntity);
            } else {
                setItemValueForNormal(itemView, keyValueEntity);
            }
            setItemEvent(keyValueEntity);
        }
    }

    /**
     * 普通item赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForNormal(View itemView, KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        TextView kv_edit_value_tv = itemView.findViewById(R.id.kv_edit_value_tv);
        ImageView kv_edit_value_right_icon_iv = itemView.findViewById(R.id.kv_edit_value_right_icon_iv);

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        //是否可操作
        if (isItemOption(keyValueEntity)) {
            kv_edit_value_right_icon_iv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_value_right_icon_iv.setVisibility(View.INVISIBLE);
        }
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //是否有填写值，只读颜色不一样
        if (!isItemOption(keyValueEntity)) {
            kv_edit_value_tv.setTextColor(Color.parseColor("#8B8B99"));
            if (TextUtils.isEmpty(keyValueEntity.getVal())) {
                kv_edit_value_tv.setText("未填写");
            } else {
                kv_edit_value_tv.setText(keyValueEntity.getVal());
            }
        } else {
            if (TextUtils.isEmpty(keyValueEntity.getVal())) {
                kv_edit_value_tv.setTextColor(Color.parseColor("#C5C5CE"));
                kv_edit_value_tv.setText(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
            } else {
                kv_edit_value_tv.setTextColor(Color.parseColor("#4A4C5C"));
                kv_edit_value_tv.setText(keyValueEntity.getVal());
            }
        }
    }

    /**
     * buttons赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForFlowButtons(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        FlowLayout kv_edit_key_buttons_fl = itemView.findViewById(R.id.kv_edit_key_buttons_fl);

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        ItemAction action = getItemAction(keyValueEntity);
        final List<String> values = new ArrayList<>();
        if (ItemAction.COLLECTIONMULTIPLE == action) {
            kv_edit_key_buttons_fl.setLabelSelect(FlowLayout.LabelSelect.MULTI);
            if (!TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                if (keyValueEntity.getDefaultVal().contains(MULTIPLE_SELECT_SEPARATOR)) {
                    String[] valuesArray = keyValueEntity.getDefaultVal().split(MULTIPLE_SELECT_SEPARATOR);
                    if (valuesArray.length > 0) {
                        values.addAll(Arrays.asList(valuesArray));
                    }
                } else {
                    values.add(keyValueEntity.getDefaultVal());
                }
            }
        } else {
            kv_edit_key_buttons_fl.setLabelSelect(FlowLayout.LabelSelect.SINGLE);
            if (!TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                values.add(keyValueEntity.getDefaultVal());
            }
        }
        kv_edit_key_buttons_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {


                return keyValueEntity.getEvent().getOptions().size();
            }

            @Override
            public String getLabelText(int position) {
                return keyValueEntity.getEvent().getOptions().get(position).getKey();
            }

            @Override
            public boolean isSelect(int position) {
                String defValue = keyValueEntity.getEvent().getOptions().get(position).getVal();
                if (values.contains(defValue)) {
                    return true;
                }
                return false;
            }

        });
    }

    /**
     * 金额item赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForPrice(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_price_et = itemView.findViewById(R.id.kv_edit_value_price_et);
        final TextView kv_edit_value_price_end = itemView.findViewById(R.id.kv_edit_value_price_end);

        //清除EditText绑定
        try {
            Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_price_et);
            if (listeners != null) {
                for (TextWatcher listener : listeners) {
                    kv_edit_value_price_et.removeTextChangedListener(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        kv_edit_value_price_et.setTextColor(Color.parseColor("#4A4C5C"));
        kv_edit_value_price_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        kv_edit_value_price_et.setHint(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
        kv_edit_value_price_end.setText(getAlertEnd(keyValueEntity));
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        String value = keyValueEntity.getDefaultVal();
        if (!TextUtils.isEmpty(value)) {
            kv_edit_value_price_et.setText(value);
            kv_edit_value_price_et.setSelection(value.length());
        }
        kv_edit_value_price_et.addTextChangedListener(new TextWatcher() {
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


        kv_edit_value_price_et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);

        kv_edit_value_price_et.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if(source.equals(".") && dest.toString().length() == 0){
                return "0.";
            }
            if(dest.toString().contains(".")){
                int index = dest.toString().indexOf(".");
                int length = dest.toString().substring(index).length();
                if(length == 3){
                    return "";
                }
            }
            return null;
        }});
    }

    /**
     * 手机item赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForMobile(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_phone_et = itemView.findViewById(R.id.kv_edit_value_phone_et);

        //清除EditText绑定
        try {
            Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_phone_et);
            if (listeners != null) {
                for (TextWatcher listener : listeners) {
                    kv_edit_value_phone_et.removeTextChangedListener(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        kv_edit_value_phone_et.setTextColor(Color.parseColor("#4A4C5C"));
        kv_edit_value_phone_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        kv_edit_value_phone_et.setHint(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        //moblie格式defaultVal是正常手机，value是加密
        String value = keyValueEntity.getVal();
        if (!TextUtils.isEmpty(value)) {
            kv_edit_value_phone_et.setText(value);
            kv_edit_value_phone_et.setSelection(value.length());
        }
        kv_edit_value_phone_et.addTextChangedListener(new TextWatcher() {
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
     * 范围item赋值
     *
     * @param itemView
     * @param keyValueEntity
     */
    private void setItemValueForRange(View itemView, final KeyValueEntity keyValueEntity) {
        TextView kv_edit_key_tv = itemView.findViewById(R.id.kv_edit_key_tv);
        TextView kv_edit_key_required_tv = itemView.findViewById(R.id.kv_edit_key_required_tv);
        final EditText kv_edit_value_min_et = itemView.findViewById(R.id.kv_edit_value_min_et);
        final EditText kv_edit_value_max_et = itemView.findViewById(R.id.kv_edit_value_max_et);
        TextView kv_edit_value_unit_tv = itemView.findViewById(R.id.kv_edit_value_unit_tv);

        //清除EditText绑定
        try {
            Field field = TextView.class.getDeclaredField("mListeners");
            field.setAccessible(true);
            try {
                List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_min_et);
                if (listeners != null) {
                    for (TextWatcher listener : listeners) {
                        kv_edit_value_min_et.removeTextChangedListener(listener);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<TextWatcher> listeners = (List<TextWatcher>) field.get(kv_edit_value_max_et);
                if (listeners != null) {
                    for (TextWatcher listener : listeners) {
                        kv_edit_value_max_et.removeTextChangedListener(listener);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        kv_edit_value_min_et.setTextColor(Color.parseColor("#4A4C5C"));
        kv_edit_value_max_et.setTextColor(Color.parseColor("#4A4C5C"));
        kv_edit_value_min_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        kv_edit_value_max_et.setHintTextColor(Color.parseColor("#C5C5CE"));
        String[] hints = getRangeHint(keyValueEntity);
        kv_edit_value_min_et.setHint(hints[0]);
        kv_edit_value_max_et.setHint(hints[1]);
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        String value = keyValueEntity.getDefaultVal();
        String minValue = "";
        String maxvalue = "";
        if (!TextUtils.isEmpty(value) && value.contains(RANGE_SEPARATOR) && value.split(RANGE_SEPARATOR).length > 0 && value.split(RANGE_SEPARATOR).length <= 2) {
            if (value.split(RANGE_SEPARATOR).length == 1) {
                minValue = value.split(RANGE_SEPARATOR)[0];
                maxvalue = "";
            } else {
                minValue = value.split(RANGE_SEPARATOR)[0];
                maxvalue = value.split(RANGE_SEPARATOR)[1];
            }
        }
        kv_edit_value_min_et.setText(minValue);
        kv_edit_value_max_et.setText(maxvalue);

        //本地标记minRange和maxRange
        keyValueEntity.setRangeMin(minValue);
        keyValueEntity.setRangeMax(maxvalue);

        kv_edit_value_unit_tv.setText(getUnit(keyValueEntity));

        kv_edit_value_min_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String minValue = s.toString().trim();
                //更新本地标记的minRange
                keyValueEntity.setRangeMin(minValue);
                keyValueEntity.setDefaultVal(minValue + RANGE_SEPARATOR + keyValueEntity.getRangeMax());
                keyValueEntity.setVal(keyValueEntity.getDefaultVal());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });
        kv_edit_value_max_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String maxValue = s.toString().trim();
                //更新本地标记的maxRange
                keyValueEntity.setRangeMax(maxValue);
                keyValueEntity.setDefaultVal(keyValueEntity.getRangeMin() + RANGE_SEPARATOR + maxValue);
                keyValueEntity.setVal(keyValueEntity.getDefaultVal());
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
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

        kv_edit_key_tv.setText(keyValueEntity.getKey());
        //是否必填
        if (isItemRequired(keyValueEntity)) {
            kv_edit_key_required_tv.setVisibility(View.VISIBLE);
        } else {
            kv_edit_key_required_tv.setVisibility(View.INVISIBLE);
        }
        //赋值
        String timeRange = keyValueEntity.getDefaultVal();
        String startTime = "";
        String endTime = "";
        if (!TextUtils.isEmpty(timeRange) && timeRange.contains(RANGE_SEPARATOR) && timeRange.split(RANGE_SEPARATOR).length > 0 && timeRange.split(RANGE_SEPARATOR).length <= 2) {
            if (timeRange.split(RANGE_SEPARATOR).length == 1) {
                startTime = timeRange.split(RANGE_SEPARATOR)[0];
                endTime = "";
            } else {
                startTime = timeRange.split(RANGE_SEPARATOR)[0];
                endTime = timeRange.split(RANGE_SEPARATOR)[1];
            }
        }
        if (!TextUtils.isEmpty(startTime)) {
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#4A4C5C"));
            kv_edit_value_start_time_tv.setText(startTime);
        } else {
            kv_edit_value_start_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_start_time_tv.setText(getAlertPrefix(keyValueEntity) + "起始时间");
        }
        if (!TextUtils.isEmpty(endTime)) {
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#4A4C5C"));
            kv_edit_value_end_time_tv.setText(endTime);
        } else {
            kv_edit_value_end_time_tv.setTextColor(Color.parseColor("#C5C5CE"));
            kv_edit_value_end_time_tv.setText(getAlertPrefix(keyValueEntity) + "结束时间");
        }
        //本地标记minRange和maxRange
        keyValueEntity.setRangeMin(startTime);
        keyValueEntity.setRangeMax(endTime);
    }

    /**
     * 获取单位
     *
     * @param keyValueEntity
     * @return
     */
    private String getUnit(KeyValueEntity keyValueEntity) {
        if (getItemAction(keyValueEntity) == ItemAction.RANGE) {
            if (keyValueEntity.getKey().contains("桌")) {
                return "桌";
            } else if (keyValueEntity.getKey().contains("桌预算")) {
                return "元/桌";
            } else if (keyValueEntity.getKey().contains("预算")) {
                return "元";
            }
        }
        return "";
    }

    /**
     * 获取范围输入提示
     *
     * @param keyValueEntity
     * @return
     */
    private String[] getRangeHint(KeyValueEntity keyValueEntity) {
        String[] hints = new String[2];
        if (getItemAction(keyValueEntity) == ItemAction.RANGE) {
            if (keyValueEntity.getKey().contains("预算")) {
                hints[0] = "最低预算";
                hints[1] = "最高预算";
            } else if (keyValueEntity.getKey().contains("桌数")) {
                hints[0] = "最少桌数";
                hints[1] = "最多桌数";
            } else {
                hints[0] = "最小" + keyValueEntity.getKey();
                hints[1] = "最大" + keyValueEntity.getKey();
            }
        }
        return hints;
    }

    /**
     * 获取提示输入的前缀词
     *
     * @param keyValueEntity
     * @return
     */
    public static String getAlertPrefix(KeyValueEntity keyValueEntity) {
        ItemAction itemAction = getItemAction(keyValueEntity);
        String prefix = "";
        switch (itemAction) {
            case INPUT:
                prefix = "请输入";
                break;
            case TEXT:
                prefix = "请输入";
                break;
            case MOBILE:
                prefix = "请输入";
                break;
            case PRICE:
                prefix = "请输入";
                break;
            case DATETIME:
                prefix = "请选择";
                break;
            case SELECTSCHEDULE:
                prefix = "请选择";
                break;
            case DATETIMERANGE:
                prefix = "请选择";
                break;
            case RANGE:
                prefix = "请输入";
                break;
            case MULTIPLESELECT:
                prefix = "请选择";
                break;
            case SELECT:
                prefix = "请选择";
                break;
            case COLLECTIONMULTIPLE:
                prefix = "请选择";
                break;
            case COLLECTION:
                prefix = "请选择";
                break;
            case CITY:
                prefix = "请选择";
                break;
            case SELECTPERSONNEL:
                prefix = "请选择";
                break;
            case SELECTHOTEL:
                prefix = "请选择";
                break;
            case SELECTHALL:
                prefix = "请选择";
                break;
            case SELECTRECORDUSER:
                prefix = "请选择";
            case ADJUST:
                prefix = "请选择";
                break;
            case CHANNEL:
                prefix = "请选择";
                break;
            case READONLY:
                prefix = "请输入";
                break;
        }
        return prefix;
    }

    public static String getAlertEnd(KeyValueEntity keyValueEntity) {
        ItemAction itemAction = getItemAction(keyValueEntity);
        String prefix = "";
        switch (itemAction) {
            case PRICE:
                if (!TextUtils.isEmpty(keyValueEntity.getEndText()))
                    prefix = keyValueEntity.getEndText();
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
        if (keyValueEntity.getEvent() == null) {
            return ItemAction.READONLY;
        }
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.INPUT.valueOf().equals(action)) {
            return ItemAction.INPUT;
        } else if (ItemAction.TEXT.valueOf().equals(action)) {
            return ItemAction.TEXT;
        } else if (ItemAction.MOBILE.valueOf().equals(action)) {
            return ItemAction.MOBILE;
        } else if (ItemAction.PRICE.valueOf().equals(action)) {
            return ItemAction.PRICE;
        } else if (ItemAction.DATETIME.valueOf().equals(action)) {
            return ItemAction.DATETIME;
        } else if (ItemAction.DATETIMERANGE.valueOf().equals(action)) {
            return ItemAction.DATETIMERANGE;
        } else if (ItemAction.RANGE.valueOf().equals(action)) {
            return ItemAction.RANGE;
        } else if (ItemAction.MULTIPLESELECT.valueOf().equals(action)) {
            return ItemAction.MULTIPLESELECT;
        } else if (ItemAction.SELECT.valueOf().equals(action)) {
            return ItemAction.SELECT;
        } else if (ItemAction.COLLECTIONMULTIPLE.valueOf().equals(action)) {
            return ItemAction.COLLECTIONMULTIPLE;
        } else if (ItemAction.COLLECTION.valueOf().equals(action)) {
            return ItemAction.COLLECTION;
        } else if (ItemAction.CITY.valueOf().equals(action)) {
            return ItemAction.CITY;
        } else if (ItemAction.SELECTPERSONNEL.valueOf().equals(action)) {
            return ItemAction.SELECTPERSONNEL;
        } else if (ItemAction.SELECTHOTEL.valueOf().equals(action)) {
            return ItemAction.SELECTHOTEL;
        } else if (ItemAction.SELECTSCHEDULE.valueOf().equals(action)) {
            return ItemAction.SELECTSCHEDULE;
        }else if (ItemAction.SELECTHALL.valueOf().equals(action)) {
            return ItemAction.SELECTHALL;
        }
        else if (ItemAction.SELECTRECORDUSER.valueOf().equals(action)) {
            return ItemAction.SELECTRECORDUSER;
        }
        else if (ItemAction.ADJUST.valueOf().equals(action)) {
            return ItemAction.ADJUST;
        } else if (ItemAction.CHANNEL.valueOf().equals(action)) {
            return ItemAction.CHANNEL;
        } else if (ItemAction.READONLY.valueOf().equals(action)) {
            return ItemAction.READONLY;
        } else {
            return ItemAction.READONLY;
        }
    }

    /**
     * 获取item的view类型
     *
     * @param keyValueEntity
     * @return
     */
    private int getItemViewType(KeyValueEntity keyValueEntity) {
        if (keyValueEntity.getEvent() == null) {
            return ITEM_TYPE_NORMAL;
        }
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.RANGE.valueOf().equals(action)) {
            return ITEM_TYPE_RANGE;
        } else if (ItemAction.DATETIMERANGE.valueOf().equals(action)) {
            return ITEM_TYPE_RANGE_TIME;
        } else if (ItemAction.MOBILE.valueOf().equals(action)) {
            return ITEM_TYPE_MOBILE;
        } else if (ItemAction.COLLECTIONMULTIPLE.valueOf().equals(action) || ItemAction.COLLECTION.valueOf().equals(action)) {
            return ITEM_TYPE_FLOWBUTTON;
        } else if (ItemAction.PRICE.valueOf().equals(action)) {
            return ITEM_TYPE_PRICE;
        }else {
            return ITEM_TYPE_NORMAL;
        }
    }

    /**
     * item是否可操作
     *
     * @param keyValueEntity
     * @return
     */
    private boolean isItemOption(KeyValueEntity keyValueEntity) {
        if (keyValueEntity.getEvent() == null) {
            return false;
        }
        String action = keyValueEntity.getEvent().getAction();
        if (ItemAction.READONLY.valueOf().equals(action)) {
            return false;
        } else {
            return true;
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
     * 设置item事件
     *
     * @param keyValueEntity
     */
    private void setItemEvent(KeyValueEntity keyValueEntity) {
        ItemAction itemAction = getItemAction(keyValueEntity);
        if (ItemAction.INPUT == itemAction || ItemAction.TEXT == itemAction) {  //跳页输入
            inputAction(keyValueEntity);
        } else if (ItemAction.MOBILE == itemAction) {  //手机号输入

        } else if (ItemAction.PRICE == itemAction) {  //金额输入

        } else if (ItemAction.DATETIME == itemAction) {  //时间选择
            timeAction(keyValueEntity);
        } else if (ItemAction.DATETIMERANGE == itemAction) {  //时间范围选择
            timeRangeAction(keyValueEntity);
        } else if (ItemAction.RANGE == itemAction) {  //范围输入

        } else if (ItemAction.MULTIPLESELECT == itemAction) { //多选
            multipleSelectAction(keyValueEntity);
        } else if (ItemAction.SELECT == itemAction) {  //单选
            selectAction(keyValueEntity);
        } else if (ItemAction.COLLECTIONMULTIPLE == itemAction) {  //多选
            multipleCollectionAction(keyValueEntity);
        } else if (ItemAction.COLLECTION == itemAction) {  //单选
            collectionAction(keyValueEntity);
        } else if (ItemAction.CITY == itemAction) {  //城市选择
            cityAction(keyValueEntity);
        } else if (ItemAction.SELECTPERSONNEL == itemAction) {  //人员选择
            selectPersonnelAction(keyValueEntity);
        } else if (ItemAction.SELECTHOTEL == itemAction) {  //人员选择
            selectHotelAction(keyValueEntity);
        }else if (ItemAction.SELECTSCHEDULE == itemAction) {  //人员选择
            selectScheduleAction(keyValueEntity);
        }
        else if (ItemAction.SELECTHALL == itemAction) {  //宴会厅选择
            selectHallAction(keyValueEntity);
        }
        else if (ItemAction.SELECTRECORDUSER == itemAction){
            selectRecordUserAction(keyValueEntity);
        }
        else if (ItemAction.ADJUST == itemAction) {  //调节器
            adjustAction(keyValueEntity);
        } else if (ItemAction.CHANNEL == itemAction) {  //调节器
            channelAction(keyValueEntity);
        } else if (ItemAction.READONLY == itemAction) {  //只读

        }
    }



    private void selectScheduleAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }

        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LiheTimePickerBuilder pickerBuilder = DateDialogUtils.createPickerViewBuilder1(getContext(), new PickerOptions1.onScheduleSelectListener() {
                    @Override
                    public void onScheduleSelect(String schedule, Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String time = format.format(date);
                        keyValueEntity.setDefaultVal(time+("午宴".equals(schedule)?",1":",2"));
                        keyValueEntity.setVal(time+" "+schedule);
                        refreshItem(keyValueEntity);

                    }
                });
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 2);
                pickerBuilder.setRangDate(Calendar.getInstance(),calendar);
                boolean[] type = {true,true,true,false,false,false};
                pickerBuilder.setType(type);
                pickerBuilder.build1().show();


//                new HotelSelectDialog.Builder(context)
//                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
//                        .loadHotelList(keyValueEntity)
//                        .setOnConfirmClickListener(new HotelSelectDialog.OnConfirmClickListener() {
//                            @Override
//                            public void onConfirm(Dialog dialog, String selectText, String selectId) {
//                                if (!TextUtils.isEmpty(selectId)) {
//                                    //更新item
//                                    keyValueEntity.setDefaultVal(selectText);
//                                    keyValueEntity.setVal(selectText);
//                                    keyValueEntity.setTempValue(selectId);
//                                    refreshItem(keyValueEntity);
//                                    if (listener != null) {
//                                        listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
//                                    }
//                                }
//                            }
//                        }).setOnCancelClickListener(new HotelSelectDialog.OnCancelClickListener() {
//                            @Override
//                            public void onCancel(Dialog dialog) {
//                                dialog.dismiss();
//                            }
//                        }).build().show();

            }
        });

    }

    /**
     * 酒店选择操作
     * @param keyValueEntity
     */
    private void selectHotelAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new HotelSelectDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .loadHotelList(keyValueEntity)
                        .setOnConfirmClickListener(new HotelSelectDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, String selectText, String selectId) {
                        if (!TextUtils.isEmpty(selectId)) {
                            //更新item
                            keyValueEntity.setDefaultVal(selectText);
                            keyValueEntity.setVal(selectText);
                            keyValueEntity.setTempValue(selectId);
                            refreshItem(keyValueEntity);
                            if (listener != null) {
                                listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                            }
                        }
                    }
                }).setOnCancelClickListener(new HotelSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();

            }
        });

    }

    private void selectHallAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new HallSelectDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .loadHotelList(keyValueEntity)
                        .setOnConfirmClickListener(new HallSelectDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, String selectText, String selectId) {
                                if (!TextUtils.isEmpty(selectId)) {
                                    //更新item
                                    keyValueEntity.setDefaultVal(selectId);
                                    keyValueEntity.setVal(selectText);
                                    keyValueEntity.setTempValue(selectId);
                                    refreshItem(keyValueEntity);
                                    if (listener != null) {
                                        listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                                    }
                                }
                            }
                        }).setOnCancelClickListener(new HallSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();

            }
        });

    }

    private void selectRecordUserAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }


        kv_edit_value_ll.setOnClickListener(view -> {
            if (keyValueEntity.getExtra() == null){
                ToastUtils.toast("请选择渠道来源");
                return;
            }

            new RecordUserSelectDialog.Builder(context)
                    .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                    .loadHotelList(keyValueEntity)
                    .setOnConfirmClickListener((dialog, selectText, selectId) -> {
                        if (!TextUtils.isEmpty(selectId)) {
                            //更新item
                            keyValueEntity.setDefaultVal(selectId);
                            keyValueEntity.setVal(selectText);
                            refreshItem(keyValueEntity);
                            if (listener != null) {
                                listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                            }
                        }
                    }).setOnCancelClickListener(dialog -> dialog.dismiss()).build().show();
        });

    }

    /**
     * 人员选择操作
     *
     * @param keyValueEntity
     */
    private void selectPersonnelAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<KeyValueEntity> options = keyValueEntity.getEvent().getOptions();
                new PersonSelectDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .setPersonDataAdapter(new PersonSelectDialog.PersonDataAdapter() {
                            @Override
                            public int getCount() {
                                return options.size();
                            }

                            @Override
                            public String getText(int dataPostion) {
                                return options.get(dataPostion).getKey();
                            }

                            @Override
                            public String getId(int dataPostion) {
                                return options.get(dataPostion).getVal();
                            }

                            @Override
                            public String initSelectId() {
                                return keyValueEntity.getDefaultVal();
                            }
                        }).setOnConfirmClickListener(new PersonSelectDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, String selectText, String selectId) {
                        if (!TextUtils.isEmpty(selectId)) {
                            //更新item
                            keyValueEntity.setDefaultVal(selectId);
                            keyValueEntity.setVal(selectText);
                            refreshItem(keyValueEntity);
                            if (listener != null) {
                                listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                            }
                        }
                    }
                }).setOnCancelClickListener(new PersonSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();

            }
        });
    }

    /**
     * 输入操作
     *
     * @param keyValueEntity
     */
    private void inputAction(final KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFilter = null;
                if (keyValueEntity.getExtra()!=null && keyValueEntity.getExtra().get("inputFilter")!=null){
                    inputFilter = (String) keyValueEntity.getExtra().get("inputFilter");
                }

                InputEditUtils.input(context, keyValueEntity.getKey(), keyValueEntity.getDefaultVal(),inputFilter, new InputEditUtils.InputEditCallback() {
                    @Override
                    public void call(String content) {
                        //更新item
                        keyValueEntity.setDefaultVal(content);
                        keyValueEntity.setVal(content);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                    }
                });
            }
        });
    }

    /**
     * 城市选择操作
     *
     * @param keyValueEntity
     */
    private void cityAction(final KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySelectUtils.select(context, getAlertPrefix(keyValueEntity) + keyValueEntity.getKey(), keyValueEntity.getDefaultVal(), new CitySelectUtils.CitySelectCallback() {
                    @Override
                    public void call(CityEntity cityEntity) {
                        //更新item
                        keyValueEntity.setDefaultVal(cityEntity.getCode());
                        keyValueEntity.setVal(cityEntity.getName());
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                    }
                });
            }
        });
    }

    /**
     * 时间选择操作
     *
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
        if (keyValueEntity.getEvent() == null) {
            return;
        }

        //时间处理
        String timeFormat = getTimeFormat(keyValueEntity.getEvent().getFormat());
        final SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectCalendar = Calendar.getInstance();
                if (!TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                    try {
                        Date date = format.parse(keyValueEntity.getDefaultVal());
                        selectCalendar.setTime(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Calendar rangeStartCalendar = Calendar.getInstance();
                Calendar rangeEndCalendar = Calendar.getInstance();
                //当前时间更大
                if (rangeStartCalendar.compareTo(selectCalendar) == 1) {
                    //范围起始时间为选中时间
                    rangeStartCalendar.setTime(selectCalendar.getTime());
                } else if (rangeStartCalendar.compareTo(selectCalendar) == -1) { //选中时间更大
                    //范围结束时间为选中时间延续5年
                    rangeEndCalendar.setTime(selectCalendar.getTime());
                }
                //结束时间延续5年

                if (keyValueEntity.getEndCalendar()!=null){
                    if ("1".equals(keyValueEntity.getEndCalendar())){
                        rangeEndCalendar.set(Calendar.DATE, rangeEndCalendar.get(Calendar.DATE) + 7);
                    }
                }else {
                    rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + 5);
                }

                DateDialogUtils.createPickerViewBuilder(context, (date, v1) -> {
                    String selectTime = format.format(date);
                    //更新item
                    keyValueEntity.setDefaultVal(selectTime);
                    keyValueEntity.setVal(selectTime);
                    refreshItem(keyValueEntity);
                    if (listener != null) {
                        listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                    }
                }).setRangDate(rangeStartCalendar, rangeEndCalendar).setDate(selectCalendar).setType(getTimeTypeArray(keyValueEntity.getEvent().getFormat())).build().show();
            }
        });
    }

    /**
     * 时间选择操作
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
        if (keyValueEntity.getEvent() == null) {
            return;
        }
        String timeFormat = getTimeFormat(keyValueEntity.getEvent().getFormat());
        final SimpleDateFormat format = new SimpleDateFormat(timeFormat);
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
                Calendar rangeStartCalendar = Calendar.getInstance();
                Calendar rangeEndCalendar = Calendar.getInstance();
                //当前时间更大
                if (rangeStartCalendar.compareTo(startCalendar) == 1) {
                    //范围起始时间为选中时间
                    rangeStartCalendar.setTime(startCalendar.getTime());
                } else if (rangeStartCalendar.compareTo(startCalendar) == -1) { //起始时间更大
                    //范围结束时间为选中时间延续5年
                    rangeEndCalendar.setTime(startCalendar.getTime());
                }
                //结束时间延续5年
                rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + 5);

                DateDialogUtils.createPickerViewBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String selectTime = format.format(date);
                        keyValueEntity.setRangeMin(selectTime);
                        //更新item
                        String timeRange = selectTime + RANGE_SEPARATOR + keyValueEntity.getRangeMax();
                        keyValueEntity.setDefaultVal(timeRange);
                        keyValueEntity.setVal(timeRange);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                    }
                }).setRangDate(rangeStartCalendar, rangeEndCalendar).setDate(startCalendar).setType(getTimeTypeArray(keyValueEntity.getEvent().getFormat())).build().show();
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
                Calendar rangeStartCalendar = Calendar.getInstance();
                Calendar rangeEndCalendar = Calendar.getInstance();
                //当前时间更大
                if (rangeStartCalendar.compareTo(endCalendar) == 1) {
                    //范围起始时间为选中时间
                    rangeStartCalendar.setTime(endCalendar.getTime());
                } else if (rangeStartCalendar.compareTo(endCalendar) == -1) { //起始时间更大
                    //范围结束时间为选中时间延续5年
                    rangeEndCalendar.setTime(endCalendar.getTime());
                }
                //结束时间延续5年
                rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + 5);

                DateDialogUtils.createPickerViewBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String selectTime = format.format(date);
                        keyValueEntity.setRangeMax(selectTime);
                        String timeRange = keyValueEntity.getRangeMin() + RANGE_SEPARATOR + selectTime;
                        //更新item
                        keyValueEntity.setDefaultVal(timeRange);
                        keyValueEntity.setVal(timeRange);
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                    }
                }).setRangDate(rangeStartCalendar, rangeEndCalendar).setDate(endCalendar).setType(getTimeTypeArray(keyValueEntity.getEvent().getFormat())).build().show();
            }
        });
    }

    /**
     * 单选操作
     *
     * @param keyValueEntity
     */
    private void selectAction(final KeyValueEntity keyValueEntity) {
        final View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemActionCheckListener != null){
                    if (!onItemActionCheckListener.onCheck(keyValueEntity,getItemAction(keyValueEntity))){
                        return;
                    }
                }

                final List<KeyValueEntity> options = keyValueEntity.getEvent().getOptions();
                String defaultVal = keyValueEntity.getDefaultVal();
                int initSelectDataPosition = -1;
                for (int i = 0; i < options.size(); i++) {
                    KeyValueEntity option = options.get(i);
                    if (!TextUtils.isEmpty(option.getVal())  && option.getVal().equals(defaultVal)) {
                        initSelectDataPosition = i;
                    }
                }
                final int finalInitSelectDataPosition = initSelectDataPosition;
                new BottomSelectDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
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
                        }).setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, int position) {
                        if (finalInitSelectDataPosition != position) {
                            final KeyValueEntity option = options.get(position);
                            //更新item
                            keyValueEntity.setDefaultVal(option.getVal());
                            keyValueEntity.setVal(option.getKey());
                            refreshItem(keyValueEntity);
                            if (listener != null) {
                                listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                            }
                        }
                    }
                }).setOnCancelClickListener(new BottomSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();

            }
        });
    }

    /**
     * 按钮单选操作
     *
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
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_key_buttons_fl.setOnLabelClickListener(new FlowLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                List<Integer> values = kv_edit_key_buttons_fl.getSelectLabelsIndex();
                if (values.size() > 0 && values.get(0) < keyValueEntity.getEvent().getOptions().size()) {
                    final KeyValueEntity option = keyValueEntity.getEvent().getOptions().get(values.get(0));
                    keyValueEntity.setDefaultVal(option.getVal());
                } else {
                    keyValueEntity.setDefaultVal("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });
    }

    /**
     * 多选操作
     *
     * @param keyValueEntity
     */
    private void multipleSelectAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectValues = null;
                if (!TextUtils.isEmpty(keyValueEntity.getDefaultVal())) {
                    String[] values = keyValueEntity.getDefaultVal().split(MULTIPLE_SELECT_SEPARATOR);
                    if (values != null) {
                        selectValues = Arrays.asList(values);
                    }
                }
                final List<KeyValueEntity> options = keyValueEntity.getEvent().getOptions();
                final List<Integer> initSelectPosition = new ArrayList<>();
                for (int i = 0; i < options.size(); i++) {
                    KeyValueEntity entity = options.get(i);
                    if (selectValues != null && entity.getVal() != null && selectValues.contains(entity.getVal())) {
                        initSelectPosition.add(i);
                    }
                }
                new BottomMultipleSelectDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .setFlowLabelAdapter(new FlowFixedLayout.FlowLabelAdapter() {

                            @Override
                            public boolean isSelect(int position) {
                                return initSelectPosition.contains(position);
                            }

                            @Override
                            public int getSize() {
                                return options.size();
                            }

                            @Override
                            public String getLabelText(int position) {
                                return options.get(position).getKey();
                            }
                        }).setOnConfirmClickListener(new BottomMultipleSelectDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<Integer> selectPositions) {
                        StringBuffer valueBuff = new StringBuffer();
                        StringBuffer defValueBuff = new StringBuffer();
                        int size = selectPositions.size();
                        for (int i = 0; i < size; i++) {
                            int position = selectPositions.get(i);
                            valueBuff.append(options.get(position).getKey());
                            defValueBuff.append(options.get(position).getVal());
                            if (i != size - 1) {
                                valueBuff.append(",");
                                defValueBuff.append(",");
                            }
                        }
                        //更新item
                        keyValueEntity.setDefaultVal(defValueBuff.toString());
                        keyValueEntity.setVal(valueBuff.toString());
                        refreshItem(keyValueEntity);
                        if (listener != null) {
                            listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                        }
                    }
                }).setOnCancelClickListener(new BottomMultipleSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
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
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
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
                        if (pos < keyValueEntity.getEvent().getOptions().size()) {
                            final KeyValueEntity option = keyValueEntity.getEvent().getOptions().get(pos);
                            if (i == values.size() - 1) {
                                buffer.append(option.getVal());
                            } else {
                                buffer.append(option.getVal() + MULTIPLE_SELECT_SEPARATOR);
                            }
                        }
                    }
                    keyValueEntity.setDefaultVal(buffer.toString());
                } else {
                    keyValueEntity.setDefaultVal("");
                }
                //更新item
                refreshItem(keyValueEntity);
                if (listener != null) {
                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                }
            }
        });
    }

    /**
     * 调节器操作
     *
     * @param keyValueEntity
     */
    private void adjustAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getOptions() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int optionSize = keyValueEntity.getEvent().getOptions() != null ? keyValueEntity.getEvent().getOptions().size() : 0;
                for (int i = 0; i < optionSize; i++) {
                    KeyValueEntity option = keyValueEntity.getEvent().getOptions().get(i);
                    option.setTempValue(option.getCount());
                }
                new AdjustDialog.Builder(context)
                        .setData(keyValueEntity.getEvent().getOptions())
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .setOnConfirmClickListener(new AdjustDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                StringBuffer buffer = new StringBuffer();
                                //更新item
                                int optionSize = keyValueEntity.getEvent().getOptions() != null ? keyValueEntity.getEvent().getOptions().size() : 0;
                                for (int i = 0; i < optionSize; i++) {
                                    KeyValueEntity option = keyValueEntity.getEvent().getOptions().get(i);
                                    option.setCount(option.getTempValue());
                                    int num = 0;
                                    try {
                                        num = TextUtils.isEmpty(option.getCount()) ? 0 : Integer.parseInt(option.getCount());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (num > 0) {
                                        buffer.append(option.getKey() + "(" + num + ")、");
                                    }
                                }
                                if (buffer.length() > 0) {
                                    buffer.delete(buffer.length() - 1, buffer.length());
                                }
                                keyValueEntity.setVal(buffer.toString());
                                refreshItem(keyValueEntity);
                                if (listener != null) {
                                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                                }
                            }
                        })
                        .build().show();
            }
        });
    }

    /**
     * channel操作
     *
     * @param keyValueEntity
     */
    private void channelAction(final KeyValueEntity keyValueEntity) {
        View itemView = findItemView(keyValueEntity);
        if (itemView == null) {
            return;
        }
        LinearLayout kv_edit_value_ll = itemView.findViewById(R.id.kv_edit_value_ll);
        if (kv_edit_value_ll == null) {
            return;
        }
        if (keyValueEntity.getEvent() == null || keyValueEntity.getEvent().getChannelList() == null) {
            return;
        }
        kv_edit_value_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChannelDialog.Builder(context)
                        .setTitle(getAlertPrefix(keyValueEntity) + keyValueEntity.getKey())
                        .setData(keyValueEntity.getEvent().getChannelList())
                        .setSelectChannelId(keyValueEntity.getDefaultVal())
                        .setOnConfirmClickListener(new ChannelDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, String selectChannelName, String selectChannelId) {
                                keyValueEntity.setVal(selectChannelName);
                                keyValueEntity.setDefaultVal(selectChannelId);
                                refreshItem(keyValueEntity);
                                if (listener != null) {
                                    listener.onEvent(keyValueEntity, getItemAction(keyValueEntity));
                                }
                            }
                        })
                        .build().show();
            }
        });
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

    public List<KeyValueEntity> getData(){
        return kvList;
    }

    /**
     * 获取时间格式类型数组
     *
     * @param format
     * @return
     */
    private boolean[] getTimeTypeArray(String format) {
        if (format == null) {
            return new boolean[]{true, true, true, false, false, false};
        }
        if (format.contains("ss")) {  //年月日时分秒
            return new boolean[]{true, true, true, true, true, true};
        } else if (format.contains("mm")) { //年月日时分
            return new boolean[]{true, true, true, true, true, false};
        } else if (format.contains("HH") || format.contains("hh")) { //年月日时
            return new boolean[]{true, true, true, true, false, false};
        } else if (format.contains("dd")) { //年月日
            return new boolean[]{true, true, true, false, false, false};
        } else { //年月日
            return new boolean[]{true, true, true, false, false, false};
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

    public  interface OnItemActionCheckListener{
         boolean onCheck(KeyValueEntity KeyValueEntity,ItemAction itemAction);
    }

}
