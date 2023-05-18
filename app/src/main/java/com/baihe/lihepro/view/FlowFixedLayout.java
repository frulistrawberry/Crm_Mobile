package com.baihe.lihepro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-19
 * Description：固定列流式布局
 */

public class FlowFixedLayout extends ViewGroup {
    /**
     * Label选中策略
     */
    public enum LabelSelect {
        /**
         * 正常(不能多选或单选)
         */
        NORMAL,
        /**
         * 单选
         */
        SINGLE,
        /**
         * 多选
         */
        MULTI
    }

    /**
     * 默认固定列数
     */
    private static final int LABEL_ROWS_DEFAULT = 3;
    /**
     * laybel默认文字颜色
     */
    private static final int LABEL_TEXT_COLOR_DEFAULT = Color.parseColor("#4A4C5C");
    /**
     * laybel默认背景资源
     */
    private static final int LABEL_BACKGROUND_DEFAULT = R.drawable.flowfixedlayout_label_background_default;

    private int labelRows;
    private float labelTextSize;
    private int labelTextColor;
    private int labelTextLeftMagin;
    private int labelTextTopMagin;
    private int labelTextRightMagin;
    private int labelTextBottomMagin;
    private int labelHorizontalSpaceSize;
    private int labelVerticalSpaceSize;
    private int labelBackground;
    private int labelSelectBackground;
    private int labelSelectTextColor;
    private int labelMultiSelectMaxNum;
    private int labelMultiSelectMinNum;
    private boolean isSingleCancel;
    private boolean labelFillMode;

    private Context context;
    private TextPaint textPaint;
    private Paint paint;
    private int rowWidth;
    private int rowHeight;

    /**
     * label选中策略
     */
    private LabelSelect labelSelect;
    /**
     * label点击监听
     */
    private OnLabelClickListener onLabelClickListener;
    /**
     * 选中的labels
     */
    private List<Integer> labelsIndexSelect = new ArrayList<>();

    public FlowFixedLayout(Context context) {
        this(context, null);
    }

    public FlowFixedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowFixedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowFixedLayout, defStyleAttr, 0);
        labelRows = typedArray.getInteger(R.styleable.FlowFixedLayout_ffl_label_rows, LABEL_ROWS_DEFAULT);
        if (labelRows <= 0) {
            labelRows = LABEL_ROWS_DEFAULT;
        }
        labelTextSize = typedArray.getDimension(R.styleable.FlowFixedLayout_ffl_label_textSize, CommonUtils.sp2px(context, 12));
        labelTextColor = typedArray.getColor(R.styleable.FlowFixedLayout_ffl_label_textColor, LABEL_TEXT_COLOR_DEFAULT);
        labelHorizontalSpaceSize = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_horizontal_space, CommonUtils.dp2pxForInt(context, 11));
        if (labelHorizontalSpaceSize <= 0) {
            labelHorizontalSpaceSize = CommonUtils.dp2pxForInt(context, 11);
        }
        labelVerticalSpaceSize = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_vertical_space, CommonUtils.dp2pxForInt(context, 10));
        if (labelVerticalSpaceSize <= 0) {
            labelVerticalSpaceSize = CommonUtils.dp2pxForInt(context, 10);
        }
        labelTextLeftMagin = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_text_maginLeft, CommonUtils.dp2pxForInt(context, 7));
        labelTextTopMagin = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_text_maginTop, CommonUtils.dp2pxForInt(context, 4.5f));
        labelTextRightMagin = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_text_maginRight, CommonUtils.dp2pxForInt(context, 7));
        labelTextBottomMagin = typedArray.getDimensionPixelSize(R.styleable.FlowFixedLayout_ffl_label_text_maginBottom, CommonUtils.dp2pxForInt(context, 4.5f));
        labelBackground = typedArray.getResourceId(R.styleable.FlowFixedLayout_ffl_label_background, LABEL_BACKGROUND_DEFAULT);
        labelSelectBackground = typedArray.getResourceId(R.styleable.FlowFixedLayout_ffl_label_select_background, -1);
        //如果设置选中背景,则label开启可选状态
        if (labelSelectBackground != -1) {
            boolean isMultiSelect = typedArray.getBoolean(R.styleable.FlowFixedLayout_ffl_label_multiSelect, true);
            if (isMultiSelect) {
                labelSelect = LabelSelect.MULTI;
                labelMultiSelectMaxNum = typedArray.getInt(R.styleable.FlowFixedLayout_ffl_label_multiSelect_maxNum, Integer.MAX_VALUE);
                labelMultiSelectMinNum = typedArray.getInt(R.styleable.FlowFixedLayout_ffl_label_multiSelect_minNum, 0);
            } else {
                labelSelect = LabelSelect.SINGLE;
            }
        } else {
            labelSelect = LabelSelect.NORMAL;
        }
        labelSelectTextColor = typedArray.getColor(R.styleable.FlowFixedLayout_ffl_label_select_textColor, labelTextColor);
        labelFillMode = typedArray.getBoolean(R.styleable.FlowFixedLayout_ffl_label_fill_mode, false);
        typedArray.recycle();

        init(context);
    }

    private void init(Context context) {
        this.context = context;
        textPaint = new TextPaint();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * 阻塞错误添加childView，childView只能通过{@link FlowFixedLayout#addLabel(String)}添加
     *
     * @param child
     * @param params
     */
    @Override
    public void addView(View child, LayoutParams params) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        //计算每列的宽度
        rowWidth = (int) ((float) (measuredWidth - getPaddingLeft() - getPaddingRight() - (labelRows - 1) * labelHorizontalSpaceSize) / labelRows);
        //文字适配大小(sp)
        float textSizeForSp = CommonUtils.pxTosp(context, labelTextSize);
        //计算每列的高度
        textPaint.setTextSize(labelTextSize);
        rowHeight = getTextHeightForInt(textPaint) + labelTextTopMagin + labelTextBottomMagin;

        int childCount = getChildCount();
        //行数
        int lineNum = 0;
        //当前行的单元空间占位数
        int linePlaceholderNum = 0;
        for (int index = 0; index < childCount; index++) {
            //占有一个单元空间
            linePlaceholderNum++;
            int labelWidth = rowWidth;

            TextView childView = (TextView) getChildAt(index);
            childView.setTextSize(textSizeForSp);
            //开启填充占位模式并且有本行存在空余占位
            if (labelFillMode && linePlaceholderNum < labelRows) {
                int textWidth = getTextWidth(childView.getText().toString(), textPaint);
                while (labelWidth - (labelTextLeftMagin + labelTextRightMagin) < textWidth) {
                    labelWidth += (labelHorizontalSpaceSize + rowWidth);
                    linePlaceholderNum++;
                    //本行已经占满空余单元格
                    LabelTagInfo tagInfo = (LabelTagInfo) childView.getTag();
                    tagInfo.placeholderNum = linePlaceholderNum;
                    if (linePlaceholderNum >= 3) {
                        break;
                    }
                }

            }

            //确定文字分配的大小
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(labelWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            //大于等于列数则换行
            if (linePlaceholderNum >= labelRows) {
                linePlaceholderNum = 0;
                lineNum++;
            }
        }
        //行数
        lineNum = linePlaceholderNum > 0 ? lineNum + 1 : lineNum;

        int measuredHeight = lineNum * rowHeight + (lineNum - 1) * labelVerticalSpaceSize + getPaddingTop() + getPaddingBottom();
        if (lineNum == 0) {
            measuredHeight = 0;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        labelsIndexSelect.clear();
        int childCount = getChildCount();
        //当前行的单元空间占位数
        int linePlaceholderNum = 0;
        //当前行数
        int currentLineNum = 1;
        int left = getPaddingLeft();
        int top = getPaddingTop();
        for (int index = 0; index < childCount; index++) {
            TextView childView = (TextView) getChildAt(index);
            LabelTagInfo tagInfo = (LabelTagInfo) childView.getTag();
            int placeholderNum = tagInfo.placeholderNum;

            int right = left + childView.getMeasuredWidth();
            int bottom = top + childView.getMeasuredHeight();
            childView.layout(left, top, right, bottom);

            //记录子view的索引和选中状态
            tagInfo.index = index;
            if (tagInfo.isSelect) {
                labelsIndexSelect.add(index);
            }

            //占有测量出的占位空间
            linePlaceholderNum += placeholderNum;
            //更新下一个label的left
            left = right + labelHorizontalSpaceSize;
            //大于等于列数则换行
            if (linePlaceholderNum >= 3) {
                linePlaceholderNum = 0;
                currentLineNum++;
                left = getPaddingLeft();
                top += (rowHeight + labelVerticalSpaceSize);
            }

        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 添加label文本
     *
     * @param labelText
     */
    public void addLabel(String labelText) {
        addLabel(labelText, this.labelTextColor);
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextColor
     */
    public void addLabel(String labelText, int labelTextColor) {
        addLabel(labelText, labelTextColor, this.labelBackground);
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextColor
     * @param labelBackground
     */
    public void addLabel(String labelText, int labelTextColor, int labelBackground) {
        addLabel(labelText, labelTextColor, this.labelBackground, false);
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextColor
     * @param labelBackground
     * @param isSelect
     */
    public void addLabel(String labelText, int labelTextColor, int labelBackground, boolean isSelect) {
        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        if (labelSelect != LabelSelect.NORMAL && isSelect) {
            if (labelSelectBackground != -1) {
                textView.setBackgroundResource(labelSelectBackground);
            } else {
                textView.setBackgroundResource(labelBackground);
            }
            textView.setTextColor(labelSelectTextColor);
        } else {
            textView.setTextColor(labelTextColor);
            textView.setBackgroundResource(labelBackground);
        }
        textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.backgroundId = labelBackground;
        tagInfo.textColor = labelTextColor;
        tagInfo.isSelect = isSelect;
        textView.setTag(tagInfo);

        if (onLabelClickListener != null) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAndSelect(textView);
                }
            });
        }
        addView(textView);
    }

    /**
     * 添加label文本集合
     *
     * @param labelTexts
     */
    public void addLabels(List<String> labelTexts) {
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText);
            }
        }
    }

    /**
     * 添加label文本集合
     *
     * @param labelTexts
     * @param labelTextColor
     */
    public void addLabels(List<String> labelTexts, int labelTextColor) {
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextColor);
            }
        }
    }

    /**
     * 添加label文本集合
     *
     * @param labelTexts
     * @param labelTextColor
     * @param labelBackground
     */
    public void addLabels(List<String> labelTexts, int labelTextColor, int labelBackground) {
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextColor, labelBackground);
            }
        }
    }

    /**
     * 删除label
     *
     * @param index
     */
    public void removeLabel(int index) {
        removeViewAt(index);
    }

    /**
     * 插入label
     *
     * @param index
     * @param labelText
     * @param labelTextColor
     * @param labelBackground
     * @param isSelect
     */
    public void insert(int index, String labelText, int labelTextColor, int labelBackground, boolean isSelect) {
        labelTextColor = labelTextColor == -1 ? this.labelTextColor : labelTextColor;
        labelBackground = labelBackground == -1 ? this.labelBackground : labelBackground;

        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        if (labelSelect != LabelSelect.NORMAL && isSelect) {
            if (labelSelectBackground != -1) {
                textView.setBackgroundResource(labelSelectBackground);
            } else {
                textView.setBackgroundResource(labelBackground);
            }
            textView.setTextColor(labelSelectTextColor);
        } else {
            textView.setTextColor(labelTextColor);
            textView.setBackgroundResource(labelBackground);
        }
        textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.backgroundId = labelBackground;
        tagInfo.textColor = labelTextColor;
        tagInfo.isSelect = isSelect;
        textView.setTag(tagInfo);

        if (onLabelClickListener != null) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAndSelect(textView);
                }
            });
        }
        addView(textView, index);
    }

    /**
     * 设置label文本(会清空之前labels)
     *
     * @param labelText
     */
    public void setLabel(String labelText) {
        rest();
        addLabel(labelText);
    }

    /**
     * 设置label文本集合(会清空之前labels)
     *
     * @param labelTexts
     */
    public void setLabels(List<String> labelTexts) {
        rest();
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText);
            }
        }
    }

    /**
     * 设置label文本集合(会清空之前labels)
     *
     * @param labelTexts
     * @param labelTextColor
     */
    public void setLabels(List<String> labelTexts, int labelTextColor) {
        rest();
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextColor);
            }
        }
    }

    /**
     * 设置label文本集合(会清空之前labels)
     *
     * @param labelTexts
     * @param labelTextColor
     * @param labelBackground
     */
    public void setLabels(List<String> labelTexts, int labelTextColor, int labelBackground) {
        rest();
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextColor, labelBackground);
            }
        }
    }

    /**
     * 添加label适配器
     * 当遇到添加的model集合是非字符串集合,可使用适配器的方式添加
     *
     * @param labelAdapter
     */
    public void addLabelAdapter(FlowLabelAdapter labelAdapter) {
        int labelSize = labelAdapter.getSize();
        if (labelSize <= 0) {
            return;
        }
        for (int i = 0; i < labelSize; i++) {
            boolean isSelect = labelAdapter.isSelect(i);
            String labelText = labelAdapter.getLabelText(i);
            if (labelText == null) {
                labelText = "";
            }
            int labelTextColor = labelAdapter.getLabelTextColor(i);
            labelTextColor = labelTextColor == -1 ? this.labelTextColor : labelTextColor;
            int labelBackground = labelAdapter.getLabelBackground(i);
            labelBackground = labelBackground == -1 ? this.labelBackground : labelBackground;
            addLabel(labelText, labelTextColor, labelBackground, isSelect);
        }
    }

    /**
     * 设置label适配器(会清空之前的labels)
     * 当遇到添加的model集合是非字符串集合,可使用适配器的方式添加
     *
     * @param labelAdapter
     */
    public void setLabelAdapter(FlowLabelAdapter labelAdapter) {
        rest();
        addLabelAdapter(labelAdapter);
    }

    /**
     * 重置label
     */
    public void rest() {
        labelsIndexSelect.clear();
        removeAllViews();
    }

    /**
     * 设置labels选中策略
     *
     * @param labelSelect
     * @param labelSelectBackground
     */
    public void setLabelSelect(LabelSelect labelSelect, @DrawableRes int labelSelectBackground, @ColorInt int labelSelectTextColor) {
        this.labelSelect = labelSelect;
        this.labelSelectBackground = labelSelectBackground;
        this.labelSelectTextColor = labelSelectTextColor;
    }

    /**
     * 设置开启单选取消
     *
     * @param isSingleCancel
     */
    public void setEnableSingleCancel(boolean isSingleCancel) {
        this.isSingleCancel = isSingleCancel;
    }

    /**
     * 设置开启开启填充模式
     *
     * @param labelFillMode
     */
    public void setEnableFillMode(boolean labelFillMode) {
        this.labelFillMode = labelFillMode;
    }

    /**
     * 设置多选做少选择的数量
     *
     * @param minNum
     */
    public void setMultiSelectMinNum(@IntRange(from = 0, to = Integer.MAX_VALUE - 1) int minNum) {
        if (labelSelect != LabelSelect.MULTI || minNum >= labelMultiSelectMaxNum) {
            return;
        }
        labelMultiSelectMinNum = minNum;
    }

    /**
     * 设置多选做多选择的数量
     *
     * @param maxNum
     */
    public void setMultiSelectMaxNum(@IntRange(from = 1, to = Integer.MAX_VALUE) int maxNum) {
        if (labelSelect != LabelSelect.MULTI || maxNum <= labelMultiSelectMinNum) {
            return;
        }
        labelMultiSelectMaxNum = maxNum;
    }

    /**
     * 设置label点击监听
     *
     * @param onLabelClickListener
     */
    public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener) {
        this.onLabelClickListener = onLabelClickListener;
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View childView = getChildAt(index);
            if (childView instanceof TextView) {
                final TextView textView = (TextView) childView;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickAndSelect(textView);
                    }
                });
            }
        }
    }

    /**
     * 点击和选中处理
     *
     * @param textView
     */
    private void clickAndSelect(TextView textView) {
        LabelTagInfo tagInfo = (LabelTagInfo) textView.getTag();
        if (tagInfo == null) {
            return;
        }
        int index = tagInfo.index;
        if (labelSelect == LabelSelect.SINGLE) { //单选处理
            if (!labelsIndexSelect.contains(index)) {  //未选中自己
                for (int i = 0; i < labelsIndexSelect.size(); i++) {
                    Integer selectIndex = labelsIndexSelect.get(i);
                    View selectView = getChildAt(selectIndex);
                    if (selectView instanceof TextView) {
                        TextView selectTextView = (TextView) selectView;
                        LabelTagInfo selectTagInfo = (LabelTagInfo) selectTextView.getTag();
                        if (selectTagInfo != null) {
                            //设为不选中
                            selectTagInfo.isSelect = false;
                            selectView.setBackgroundResource(selectTagInfo.backgroundId);
                            selectTextView.setTextColor(selectTagInfo.textColor);
                        }
                    }
                }
                //清空之前的选中label索引
                labelsIndexSelect.clear();

                //设置为选中状态
                tagInfo.isSelect = true;
                //手动记录新索引并设置背景
                labelsIndexSelect.add(index);
                if (labelSelectBackground != -1) {
                    textView.setBackgroundResource(labelSelectBackground);
                }
                textView.setTextColor(labelSelectTextColor);
            } else {
                if (isSingleCancel) {  //如果开启单选可取消
                    Object removeObject = index;
                    labelsIndexSelect.remove(removeObject);
                    //设置为未选中状态
                    tagInfo.isSelect = false;
                    textView.setBackgroundResource(tagInfo.backgroundId);
                    textView.setTextColor(tagInfo.textColor);
                }
            }
        } else if (labelSelect == LabelSelect.MULTI) { //多选处理
            if (labelsIndexSelect.contains(index)) {   //取消选中
                if (labelsIndexSelect.size() <= labelMultiSelectMinNum) {  //小于最小的选中数量不操作
                    if (onLabelClickListener != null) {
                        onLabelClickListener.onLabelClick(textView.getText().toString(), index);
                    }
                    return;
                }
                Object removeObject = index;
                labelsIndexSelect.remove(removeObject);
                //设置为未选中状态
                tagInfo.isSelect = false;
                textView.setBackgroundResource(tagInfo.backgroundId);
                textView.setTextColor(tagInfo.textColor);
            } else {   //选中
                if (labelsIndexSelect.size() >= labelMultiSelectMaxNum) {  //超过最大的选中数量不操作
                    if (onLabelClickListener != null) {
                        onLabelClickListener.onLabelClick(textView.getText().toString(), index);
                    }
                    return;
                }
                labelsIndexSelect.add(index);
                //设置为选中状态
                tagInfo.isSelect = true;
                if (labelSelectBackground != -1) {
                    textView.setBackgroundResource(labelSelectBackground);
                }
                textView.setTextColor(labelSelectTextColor);
            }
        }
        if (onLabelClickListener != null) {
            onLabelClickListener.onLabelClick(textView.getText().toString(), index);
        }
    }

    private int getTextHeightForInt(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return fontMetrics.bottom - fontMetrics.top;
    }

    public int getTextWidth(String text, Paint paint) {
        float textWidth = 0;
        for (int index = 0; index < text.length(); index++) {
            char ch = text.charAt(index);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            paint.getTextWidths(srt, widths);
            textWidth += widths[0];
        }
        return (int) (textWidth + 0.5f);
    }


    /**
     * 返回选中labels的索引集合
     *
     * @return
     */
    public List<Integer> getSelectLabelsIndex() {
        return labelsIndexSelect;
    }

    /**
     * 去除选中状态
     *
     * @param index
     */
    public void unSelect(Integer index) {
        if (labelSelect == LabelSelect.SINGLE || labelSelect == LabelSelect.MULTI && labelsIndexSelect.contains(index) && index >= 0 && index < getChildCount()) {
            View selectView = getChildAt(index);
            Object tagObject = selectView.getTag();
            if (selectView instanceof TextView && tagObject != null && tagObject instanceof LabelTagInfo) {
                TextView selectTextView = (TextView) selectView;
                LabelTagInfo labelTagInfo = (LabelTagInfo) tagObject;
                labelTagInfo.isSelect = false;
                selectTextView.setBackgroundResource(labelTagInfo.backgroundId);
                selectTextView.setTextColor(labelTagInfo.textColor);
                labelsIndexSelect.remove(index);
            }
        }
    }

    /**
     * 清除选中label的选中状态
     */
    public void clearSelectLabelStatus() {
        if (labelSelect == LabelSelect.NORMAL) {
            return;
        }
        for (int index = 0; index < labelsIndexSelect.size(); index++) {
            int childIndex = labelsIndexSelect.get(index);
            View childView = getChildAt(childIndex);
            if (childView instanceof TextView) {
                TextView childTextView = (TextView) childView;
                LabelTagInfo tagInfo = (LabelTagInfo) childTextView.getTag();
                if (tagInfo == null) {
                    //记录tag信息
                    tagInfo = new LabelTagInfo();
                    tagInfo.backgroundId = labelBackground;
                    tagInfo.textColor = labelTextColor;
                    childTextView.setTag(tagInfo);
                }
                tagInfo.isSelect = false;
                childTextView.setBackgroundResource(tagInfo.backgroundId);
                childTextView.setTextColor(tagInfo.textColor);
            }
        }
        labelsIndexSelect.clear();
    }

    /**
     * label点击监听
     */
    public interface OnLabelClickListener {
        void onLabelClick(String text, int index);
    }

    /**
     * 固定列流式布局适配器
     */
    public static abstract class FlowLabelAdapter {
        /**
         * label标签数量
         *
         * @return
         */
        public abstract int getSize();

        /**
         * 对应label的文本
         *
         * @param position
         * @return
         */
        public abstract String getLabelText(int position);

        /**
         * 是否被选中
         *
         * @param position
         * @return
         */
        public boolean isSelect(int position) {
            return false;
        }

        /**
         * 对应label的字体颜色,不重写会默认返回-1(会使用默认字体颜色)
         *
         * @param position
         * @return
         */
        public int getLabelTextColor(int position) {
            return -1;
        }

        /**
         * 对应label的background资源id,不重写会默认返回-1(会使用默认背景资源)
         *
         * @param position
         * @return
         */
        public int getLabelBackground(int position) {
            return -1;
        }
    }

    /**
     * label tag信息
     */
    public class LabelTagInfo {

        public LabelTagInfo() {
            this.placeholderNum = 1;
        }

        /**
         * 记录label索引
         */
        public int index;
        /**
         * 记录label背景
         */
        public int backgroundId;
        /**
         * 记录label文字颜色
         */
        public int textColor;
        /**
         * 记录label是否选中
         */
        public boolean isSelect;
        /**
         * 记录实际占位数，默认每个label占位为1
         */
        public int placeholderNum;
    }
}
