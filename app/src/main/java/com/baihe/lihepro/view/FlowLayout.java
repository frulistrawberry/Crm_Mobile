package com.baihe.lihepro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntRange;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2019-10-21
 * Description：自定义流式布局
 */

public class FlowLayout extends ViewGroup {
    /**
     * 换行标记
     */
    private static final String TAG_LINE_MARK = "TAG_LINE_MARK";

    /**
     * Label上下对齐策略
     */
    public enum VerticalGravity {
        /**
         * 顶边对齐
         */
        TOP,
        /**
         * 居中
         */
        CENTER,
        /**
         * 底边对齐
         */
        BUTTOM
    }

    /**
     * Label整体位置排列策略
     */
    public enum LabelsGravity {
        /**
         * 居中
         */
        CENTER,
        /**
         * 正常
         */
        NORMAL
    }

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
     * laybel默认背景资源
     */
    private static final int LABEL_BACKGROUND_DEFAULT = R.drawable.flowlayout_label_background_default;
    /**
     * laybel默认文字颜色
     */
    private static final int LABEL_TEXT_COLOR_DEFAULT = Color.rgb(94, 102, 113);

    private int labelBackground;
    private float labelTextSize;
    private int labelTextColor;
    private int labelTextLeftMagin;
    private int labelTextTopMagin;
    private int labelTextRightMagin;
    private int labelTextBottomMagin;
    private int labelHorizontalSpaceSize;
    private int labelVerticalSpaceSize;
    private int labelSelectBackground;
    private int labelSelectTextColor;

    private int labelMultiSelectMaxNum;
    private int labelMultiSelectMinNum;

    //保存每行label的高大高度(防止label的高度不一,排版絮乱不同意问题)
    private Map<Integer, Integer> lineLabelsHeightMap = new HashMap<Integer, Integer>();
    /**
     * 当每行label高度不统一时,label的排列策略
     */
    private VerticalGravity labelVerticalGravity;
    /**
     * label整体位置排列策略
     */
    private LabelsGravity labelsGravity;
    /**
     * label选中策略
     */
    private LabelSelect labelSelect;
    /**
     * 上下文
     */
    private Context context;

    /**
     * 是否单行显示
     */
    private boolean singleLine;
    /**
     * 是否单行无限延伸
     */
    private boolean singleLineExtend;
    /**
     * label点击监听
     */
    private OnLabelClickListener onLabelClickListener;
    /**
     * 单选选中是否可取消
     */
    private boolean labelSelectCancel;

    /**
     * 全局字体大小
     */
    private float globalTextSize;

    /**
     * 所有行的labels的偏移量
     */
    List<Integer> linesLabelsOffset = new ArrayList<Integer>();
    /**
     * 所有行的labels的换行索引Map
     */
    private Map<Integer, String> linesLabelsIndexMap = new HashMap<Integer, String>();
    /**
     * 选中的labels
     */
    private List<Integer> labelsIndexSelect = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, 0);
        labelBackground = typedArray.getResourceId(R.styleable.FlowLayout_label_background, LABEL_BACKGROUND_DEFAULT);
        labelTextSize = CommonUtils.pxTosp(context, typedArray.getDimension(R.styleable.FlowLayout_label_textSize, CommonUtils.sp2px(context, 13)));
        labelTextColor = typedArray.getColor(R.styleable.FlowLayout_label_textColor, LABEL_TEXT_COLOR_DEFAULT);
        labelTextLeftMagin = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_text_maginLeft, 0);
        labelTextTopMagin = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_text_maginTop, 0);
        labelTextRightMagin = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_text_maginRight, 0);
        labelTextBottomMagin = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_text_maginBottom, 0);
        labelHorizontalSpaceSize = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_horizontal_space, CommonUtils.dp2pxForInt(context, 8));
        labelVerticalSpaceSize = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_label_vertical_space, CommonUtils.dp2pxForInt(context, 8));
        singleLine = typedArray.getBoolean(R.styleable.FlowLayout_singleLine, false);
        singleLineExtend = typedArray.getBoolean(R.styleable.FlowLayout_singleLineExtend, false);
        labelSelectBackground = typedArray.getResourceId(R.styleable.FlowLayout_label_select_background, -1);
        labelSelectCancel = typedArray.getBoolean(R.styleable.FlowLayout_label_select_cancel, false);
        //如果设置选中背景,则label开启可选状态
        if (labelSelectBackground != -1) {
            boolean isMultiSelect = typedArray.getBoolean(R.styleable.FlowLayout_label_multiSelect, false);
            if (isMultiSelect) {
                labelSelect = LabelSelect.MULTI;
                labelMultiSelectMinNum = typedArray.getInt(R.styleable.FlowLayout_label_multiSelect_minNum, 0);
                labelMultiSelectMaxNum = typedArray.getInt(R.styleable.FlowLayout_label_multiSelect_maxNum, Integer.MAX_VALUE);
            } else {
                labelSelect = LabelSelect.SINGLE;
            }
        } else {
            labelSelect = LabelSelect.NORMAL;
        }
        labelSelectTextColor = typedArray.getColor(R.styleable.FlowLayout_label_select_textColor, labelTextColor);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.labelVerticalGravity = VerticalGravity.CENTER;
        this.labelsGravity = LabelsGravity.NORMAL;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        //所有labels显示的宽度
        int allLabelsWidh = 0;
        //所有labels显示高度
        int allLabelsHeight = 0;
        //每行labels的高度
        int linelabelsHeight = 0;
        //每行labels的宽度
        int lineLabelsWidth = 0;
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        //每行最大宽度
        int maxWidth = measuredWidth - paddingLeft - paddingRight;

        lineLabelsHeightMap.clear();

        int childCount = getChildCount();
        int line = 0;
        for (int index = 0; index < childCount; index++) {
            View childView = getChildAt(index);
            if (globalTextSize > 0 && childView instanceof TextView) {
                TextView childTextView = ((TextView) childView);
                childTextView.setTextSize(globalTextSize);
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            //假如label宽度比父View宽度大,则label宽度是父View宽度
            if (childMeasuredWidth > maxWidth) {
                childMeasuredWidth = maxWidth;
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childMeasuredHeight, MeasureSpec.EXACTLY);
                //重新给予View大小
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
            //排版label所需的宽度
            int labelNeedWidth = (lineLabelsWidth == 0 ? childMeasuredWidth : childMeasuredWidth + labelHorizontalSpaceSize);

            if (lineLabelsWidth + labelNeedWidth > (singleLine && singleLineExtend ? Integer.MAX_VALUE : maxWidth)) {  //换行
                //保存上一行的高度
                lineLabelsHeightMap.put(line, linelabelsHeight);

                //记录上一行信息
                allLabelsWidh = Math.max(lineLabelsWidth, allLabelsWidh); //比较上一行最大宽度和当前保存的最大宽度,取出最大宽度
                if (line == 0) {
                    allLabelsHeight = allLabelsHeight + linelabelsHeight; //当前labels的排列高度
                } else {
                    allLabelsHeight = allLabelsHeight + linelabelsHeight + labelVerticalSpaceSize; //当前labels的排列高度
                }

                //换行则退出当前测量
                if (singleLine) {
                    break;
                }

                line++;

                //如果在最后一个label换行
                if (index == childCount - 1) {
                    allLabelsWidh = Math.max(childMeasuredWidth, allLabelsWidh); //比较当前label的宽度和当前保存的最大宽度,取出最大宽度
                    allLabelsHeight = allLabelsHeight + childMeasuredHeight + labelVerticalSpaceSize; //当前labels的排列高度

                    //最后一个换行,保存最一行高度
                    lineLabelsHeightMap.put(line, linelabelsHeight);
                    break;
                }
                //重置信息
                linelabelsHeight = childMeasuredHeight; //初始当前行的labels的宽度
                lineLabelsWidth = childMeasuredWidth; //重置每行的当前宽度
            } else {
                //如果最后一个不换行
                if (index == childCount - 1) {
                    lineLabelsWidth = lineLabelsWidth + labelNeedWidth; //当前行labels最大宽度
                    linelabelsHeight = Math.max(linelabelsHeight, childMeasuredHeight); //取出每行labels的最大高度
                    allLabelsWidh = Math.max(lineLabelsWidth, allLabelsWidh); //比较当前行最大宽度和当前保存的最大宽度,取出最大宽度
                    if (line == 0) {
                        allLabelsHeight = allLabelsHeight + linelabelsHeight; //当前labels的排列高度
                    } else {
                        allLabelsHeight = allLabelsHeight + linelabelsHeight + labelVerticalSpaceSize; //当前labels的排列高度
                    }

                    //最后一个不换行,更新本行高度(可能最后一个行更高)
                    lineLabelsHeightMap.put(line, linelabelsHeight);
                } else {
                    lineLabelsWidth = lineLabelsWidth + labelNeedWidth; //当前行labels记录的宽度
                    linelabelsHeight = Math.max(linelabelsHeight, childMeasuredHeight); //取出每行label中的最大高度
                }
            }
        }
        int realMeasuredWidth = allLabelsWidh + paddingLeft + paddingRight;
        if (!singleLine && line > 0) {
            realMeasuredWidth = measuredWidth;
        }
        setMeasuredDimension(realMeasuredWidth, allLabelsHeight + paddingTop + paddingBottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int measuredWidth = getMeasuredWidth();

        int childCount = getChildCount();
        int childRight = left;
        int childButtom = getPaddingTop();
        int line = 0;
        linesLabelsOffset.clear();
        linesLabelsIndexMap.clear();
        for (int index = 0; index < childCount; index++) {
            View childView = getChildAt(index);
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            //排版label所需要的宽度(每行的起始label所需宽度是label宽度,其他则label宽度加上边距)
            int labelNeedWidth = (childRight == left ? childMeasuredWidth : childMeasuredWidth + labelHorizontalSpaceSize);
            int top = 0;
            if (childRight + labelNeedWidth > measuredWidth) { //换行

                //如果有居中排列策略, 记录偏移量
                if (labelsGravity == LabelsGravity.CENTER) {
                    int lineRigit = getChildAt(index - 1).getRight();
                    linesLabelsOffset.add((measuredWidth - lineRigit) / 2);
                    linesLabelsIndexMap.put(index - 1, TAG_LINE_MARK);
                }

                //单行显示终止排版
                if (singleLine) {
                    break;
                }

                childRight = left + childMeasuredWidth;

                //换行累加上上一行的高度
                childButtom = childButtom + lineLabelsHeightMap.get(line) + labelVerticalSpaceSize;
                line++;
                int lineLabelsHeight = lineLabelsHeightMap.get(line);

                switch (labelVerticalGravity) {
                    case TOP:
                        top = childButtom;
                        break;
                    case CENTER:
                        top = childButtom + (lineLabelsHeight - childMeasuredHeight) / 2;
                        break;
                    case BUTTOM:
                        top = childButtom + lineLabelsHeight - childMeasuredHeight;
                        break;
                }

            } else {
                childRight += labelNeedWidth;

                int lineLabelsHeight = lineLabelsHeightMap.get(line);

                switch (labelVerticalGravity) {
                    case TOP:
                        top = childButtom;
                        break;
                    case CENTER:
                        top = childButtom + (lineLabelsHeight - childMeasuredHeight) / 2;
                        break;
                    case BUTTOM:
                        top = childButtom + lineLabelsHeight - childMeasuredHeight;
                        break;
                }
            }
            childView.layout(childRight - childMeasuredWidth, top, childRight, top + childMeasuredHeight);

            //记录重新排版后,子view的索引和选中状态
            if (childView instanceof TextView) {
                LabelTagInfo tagInfo = (LabelTagInfo) childView.getTag();
                tagInfo.index = index;
                if (tagInfo.isSelect && !labelsIndexSelect.contains(index)) {
                    labelsIndexSelect.add(index);
                }
            }

            //如果有居中排列策略并且是最后一个, 记录偏移量
            if (labelsGravity == LabelsGravity.CENTER && index == childCount - 1) {
                int lineRigit = getChildAt(childCount - 1).getRight();
                linesLabelsOffset.add((measuredWidth - lineRigit) / 2);
                linesLabelsIndexMap.put(childCount - 1, TAG_LINE_MARK);
            }
        }
        //如果有居中排列策略,重新排列labels
        if (labelsGravity == LabelsGravity.CENTER) {
            int tagIndex = 0;
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int offsetX = linesLabelsOffset.get(tagIndex);
                childView.layout(childView.getLeft() + offsetX, childView.getTop(), childView.getRight() + offsetX, childView.getBottom());
                //有换行标记则换行,取出下一行的偏移值
                String tag = linesLabelsIndexMap.get(i);
                if (tag != null && TAG_LINE_MARK.equals(tag)) {
                    //单行显示终止排版
                    if (singleLine) {
                        break;
                    }
                    tagIndex += 1;
                }
            }
        }
    }

    /**
     * 设置label间的横向间距
     *
     * @param labelHorizontalSpaceSize
     */
    public void setLabelHorizontalSpaceSize(int labelHorizontalSpaceSize) {
        this.labelHorizontalSpaceSize = labelHorizontalSpaceSize;
    }

    /**
     * 设置label间的纵向间距
     *
     * @param labelVerticalSpaceSize
     */
    public void setLabelVerticalSpaceSize(int labelVerticalSpaceSize) {
        this.labelVerticalSpaceSize = labelVerticalSpaceSize;
    }

    /**
     * 设置每行label不统一时的排版策略
     *
     * @param labelVerticalGravity
     */
    public void setLabelVerticalGravity(VerticalGravity labelVerticalGravity) {
        this.labelVerticalGravity = labelVerticalGravity;
    }

    /**
     * 设置labels整体位置排列策略
     *
     * @param labelGravity
     */
    public void setLabelsGravity(LabelsGravity labelGravity) {
        this.labelsGravity = labelGravity;
    }

    /**
     * 单行显示
     */
    public void singleLine() {
        singleLine = true;
    }

    /**
     * 设置选择模式
     *
     * @param labelSelect
     */
    public void setLabelSelect(LabelSelect labelSelect) {
        this.labelSelect = labelSelect;
        if (labelSelect == LabelSelect.MULTI) {
            labelMultiSelectMaxNum = Integer.MAX_VALUE;
        } else {
            labelMultiSelectMaxNum = 0;
        }
    }

    /**
     * 设置多选做少选择的数量
     *
     * @param minNum
     */
    public void setMultiSelectNum(@IntRange(from = 0, to = Integer.MAX_VALUE - 1) int minNum) {
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
     * 添加label文本
     *
     * @param labelText
     */
    public void addLabel(String labelText) {
        addLabel(labelText, this.labelTextSize, this.labelTextColor);
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     */
    public void addLabel(String labelText, float labelTextSize, int labelTextColor) {
        addLabel(labelText, labelTextSize, labelTextColor, this.labelBackground);
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackground
     */
    public void addLabel(String labelText, float labelTextSize, int labelTextColor, int labelBackground) {
        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
        textView.setTextColor(labelTextColor);
        textView.setBackgroundResource(labelBackground);
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackground;
        tagInfo.isSelect = false;
        textView.setTag(tagInfo);

        if (onLabelClickListener != null) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAndSelect(textView);
                }
            });
        }
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackground
     * @param isSelect
     */
    public void addLabel(String labelText, float labelTextSize, int labelTextColor, int labelBackground, boolean isSelect) {
        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
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
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackground;
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
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackgroundDrawable
     */
    public void addLabel(String labelText, float labelTextSize, int labelTextColor, Drawable labelBackgroundDrawable) {
        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
        textView.setTextColor(labelTextColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(labelBackgroundDrawable);
        } else {
            textView.setBackgroundDrawable(labelBackgroundDrawable);
        }
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackgroundDrawable;
        tagInfo.isSelect = false;
        textView.setTag(tagInfo);

        if (onLabelClickListener != null) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAndSelect(textView);
                }
            });
        }
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加label文本
     *
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackgroundDrawable
     * @param isSelect
     */
    public void addLabel(String labelText, float labelTextSize, int labelTextColor, Drawable labelBackgroundDrawable, boolean isSelect) {
        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
        if (labelSelect != LabelSelect.NORMAL && isSelect) {
            if (labelSelectBackground != -1) {
                textView.setBackgroundResource(labelSelectBackground);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    textView.setBackground(labelBackgroundDrawable);
                } else {
                    textView.setBackgroundDrawable(labelBackgroundDrawable);
                }
            }
            textView.setTextColor(labelSelectTextColor);
        } else {
            textView.setTextColor(labelTextColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(labelBackgroundDrawable);
            } else {
                textView.setBackgroundDrawable(labelBackgroundDrawable);
            }
        }
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackgroundDrawable;
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
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
     * @param labelTextSize
     * @param labelTextColor
     */
    public void addLabels(List<String> labelTexts, float labelTextSize, int labelTextColor) {
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextSize, labelTextColor);
            }
        }
    }

    /**
     * 添加label文本集合
     *
     * @param labelTexts
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackground
     */
    public void addLabels(List<String> labelTexts, float labelTextSize, int labelTextColor, int labelBackground) {
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextSize, labelTextColor, labelBackground);
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
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackground
     * @param isSelect
     */
    public void insertLabel(int index, String labelText, float labelTextSize, int labelTextColor, int labelBackground, boolean isSelect) {
        labelTextSize = labelTextSize <= 0 ? this.labelTextSize : labelTextSize;
        labelTextColor = labelTextColor == -1 ? this.labelTextColor : labelTextColor;
        labelBackground = labelBackground == -1 ? this.labelBackground : labelBackground;

        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
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
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackground;
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
        addView(textView, index, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 插入label
     *
     * @param index
     * @param labelText
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackgroundDrawable
     * @param isSelect
     */
    public void insertLabel(int index, String labelText, float labelTextSize, int labelTextColor, Drawable labelBackgroundDrawable, boolean isSelect) {
        labelTextSize = labelTextSize <= 0 ? this.labelTextSize : labelTextSize;
        labelTextColor = labelTextColor == -1 ? this.labelTextColor : labelTextColor;
        labelBackgroundDrawable = labelBackgroundDrawable == null ? getResources().getDrawable(labelBackground) : labelBackgroundDrawable;

        final TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(TextUtils.isEmpty(labelText) ? "" : labelText);
        textView.setTextSize(labelTextSize > 0 ? labelTextSize : this.labelTextSize);
        textView.setSingleLine();
        if (labelSelect != LabelSelect.NORMAL && isSelect) {
            if (labelSelectBackground != -1) {
                textView.setBackgroundResource(labelSelectBackground);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    textView.setBackground(labelBackgroundDrawable);
                } else {
                    textView.setBackgroundDrawable(labelBackgroundDrawable);
                }
            }
            textView.setTextColor(labelSelectTextColor);
        } else {
            textView.setTextColor(labelTextColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(labelBackgroundDrawable);
            } else {
                textView.setBackgroundDrawable(labelBackgroundDrawable);
            }
        }
        Drawable backgroundDrawable = textView.getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (android.graphics.drawable.GradientDrawable) backgroundDrawable;
            Rect paddingRect = new Rect();
            gradientDrawable.getPadding(paddingRect);
            if (paddingRect.left > 0 || paddingRect.top > 0 || paddingRect.right > 0 || paddingRect.bottom > 0) {
                textView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
            } else {
                textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
            }
        } else {
            textView.setPadding(labelTextLeftMagin, labelTextTopMagin, labelTextRightMagin, labelTextBottomMagin);
        }

        //记录tag信息
        LabelTagInfo tagInfo = new LabelTagInfo();
        tagInfo.background = labelBackgroundDrawable;
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
        addView(textView, index, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
     * @param labelTextSize
     * @param labelTextColor
     */
    public void setLabels(List<String> labelTexts, float labelTextSize, int labelTextColor) {
        rest();
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextSize, labelTextColor);
            }
        }
    }

    /**
     * 设置label文本集合(会清空之前labels)
     *
     * @param labelTexts
     * @param labelTextSize
     * @param labelTextColor
     * @param labelBackground
     */
    public void setLabels(List<String> labelTexts, float labelTextSize, int labelTextColor, int labelBackground) {
        rest();
        if (labelTexts != null && labelTexts.size() > 0) {
            for (String labelText : labelTexts) {
                addLabel(labelText, labelTextSize, labelTextColor, labelBackground);
            }
        }
    }

    /**
     * 重置label
     */
    public void rest() {
        linesLabelsOffset.clear();
        linesLabelsIndexMap.clear();
        labelsIndexSelect.clear();
        removeAllViews();
    }

    /**
     * 设置全局字体大小
     * 如何该属性被设置,label设置的字体大小将替代
     */
    public void setGlobalTextSize(float globalTextSize) {
        this.globalTextSize = globalTextSize;
        requestLayout();
        invalidate();
    }

    /**
     * 设置label点击监听
     *
     * @param onLabelClickListener
     */
    public void setOnLabelClickListener(final OnLabelClickListener onLabelClickListener) {
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
            if (!labelsIndexSelect.contains(index)) { //未选中自己
                for (int i = 0; i < labelsIndexSelect.size(); i++) {
                    Integer selectIndex = labelsIndexSelect.get(i);
                    View selectView = getChildAt(selectIndex);
                    if (selectView instanceof TextView) {
                        TextView selectTextView = (TextView) selectView;
                        LabelTagInfo selectTagInfo = (LabelTagInfo) selectTextView.getTag();
                        if (selectTagInfo != null) {
                            //设为不选中
                            selectTagInfo.isSelect = false;
                            Object object = selectTagInfo.background;
                            if (object instanceof Drawable) {
                                Drawable backgroundDrawable = (Drawable) object;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    selectView.setBackground(backgroundDrawable);
                                } else {
                                    selectView.setBackgroundDrawable(backgroundDrawable);
                                }
                            } else if (object instanceof Integer) {
                                int backgroundId = (int) object;
                                selectView.setBackgroundResource(backgroundId);
                            }
                            selectTextView.setTextColor(labelTextColor);
                        }
                    }
                }
                //清空之前的选中label索引
                labelsIndexSelect.clear();

                //设置为选中状态
                tagInfo.isSelect = true;
                //手动记录新索引并设置背景
                labelsIndexSelect.add(index);
                textView.setBackgroundResource(labelSelectBackground);
                textView.setTextColor(labelSelectTextColor);
            } else {
                if (labelSelectCancel) {
                    for (int i = 0; i < labelsIndexSelect.size(); i++) {
                        Integer selectIndex = labelsIndexSelect.get(i);
                        View selectView = getChildAt(selectIndex);
                        if (selectView instanceof TextView) {
                            TextView selectTextView = (TextView) selectView;
                            LabelTagInfo selectTagInfo = (LabelTagInfo) selectTextView.getTag();
                            if (selectTagInfo != null) {
                                //设为不选中
                                selectTagInfo.isSelect = false;
                                Object object = selectTagInfo.background;
                                if (object instanceof Drawable) {
                                    Drawable backgroundDrawable = (Drawable) object;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        selectView.setBackground(backgroundDrawable);
                                    } else {
                                        selectView.setBackgroundDrawable(backgroundDrawable);
                                    }
                                } else if (object instanceof Integer) {
                                    int backgroundId = (int) object;
                                    selectView.setBackgroundResource(backgroundId);
                                }
                                selectTextView.setTextColor(labelTextColor);
                            }
                        }
                    }
                    //清空之前的选中label索引
                    labelsIndexSelect.clear();
                }
            }
        } else if (labelSelect == LabelSelect.MULTI) { //多选处理
            if (labelsIndexSelect.contains(index)) {  //取消选中
                if (labelsIndexSelect.size() <= labelMultiSelectMinNum) {  //小于最小的选中数量不操作
                    onLabelClickListener.onLabelClick(textView.getText().toString(), index);
                    return;
                }
                Object removeObject = index;
                labelsIndexSelect.remove(removeObject);
                //设置为未选中状态
                tagInfo.isSelect = false;
                Object object = tagInfo.background;
                if (object instanceof Drawable) {
                    Drawable backgroundDrawable = (Drawable) object;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        textView.setBackground(backgroundDrawable);
                    } else {
                        textView.setBackgroundDrawable(backgroundDrawable);
                    }
                } else if (object instanceof Integer) {
                    int backgroundId = (int) object;
                    textView.setBackgroundResource(backgroundId);
                }
                textView.setTextColor(labelTextColor);
            } else { //选中
                if (labelsIndexSelect.size() >= labelMultiSelectMaxNum) {  //超过最大的选中数量不操作
                    onLabelClickListener.onLabelClick(textView.getText().toString(), index);
                    return;
                }
                labelsIndexSelect.add(index);
                //设置为选中状态
                tagInfo.isSelect = true;
                textView.setBackgroundResource(labelSelectBackground);
                textView.setTextColor(labelSelectTextColor);
            }
        }
        onLabelClickListener.onLabelClick(textView.getText().toString(), index);
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
     * 清除选中label的选中状态
     */
    public void clearSelectLabelStatus() {
        if (labelSelect == FlowLayout.LabelSelect.NORMAL) {
            return;
        }
        for (int index = 0; index < labelsIndexSelect.size(); index++) {
            int childIndex = labelsIndexSelect.get(index);
            View childView = getChildAt(childIndex);
            if (childView instanceof TextView) {
                TextView childTextView = (TextView) childView;
                LabelTagInfo tagInfo = (LabelTagInfo) childTextView.getTag();
                if (tagInfo != null) {
                    tagInfo.isSelect = false;
                    Object object = tagInfo.background;
                    if (object instanceof Drawable) {
                        Drawable backgroundDrawable = (Drawable) object;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            childTextView.setBackground(backgroundDrawable);
                        } else {
                            childTextView.setBackgroundDrawable(backgroundDrawable);
                        }
                    } else if (object instanceof Integer) {
                        int backgroundId = (int) object;
                        childTextView.setBackgroundResource(backgroundId);
                    }
                }
                childTextView.setTextColor(labelTextColor);
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
     * 添加label文本适配器
     * 当遇到添加的文本集时,是非字符串集合,可使用适配器的方式添加
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
            float labelTextSize = labelAdapter.getLabelTextSize(i);
            labelTextSize = labelTextSize > 0 ? labelTextSize : this.labelTextSize;
            int labelTextColor = labelAdapter.getLabelTextColor(i);
            labelTextColor = labelTextColor == -1 ? this.labelTextColor : labelTextColor;
            int labelBackground = labelAdapter.getLabelBackground(i);
            labelBackground = labelBackground == -1 ? this.labelBackground : labelBackground;
            Drawable backgroundDrawable = labelAdapter.getLabelBackgroundDrawable(i);
            if (backgroundDrawable != null) {
                addLabel(labelText, labelTextSize, labelTextColor, backgroundDrawable, isSelect);
            } else {
                addLabel(labelText, labelTextSize, labelTextColor, labelBackground, isSelect);
            }
        }
    }

    /**
     * 设置label文本适配器(会清空之前的labels)
     * 当遇到添加的文本集时,是非字符串集合,可使用适配器的方式添加
     *
     * @param labelAdapter
     */
    public void setLabelAdapter(FlowLabelAdapter labelAdapter) {
        rest();
        addLabelAdapter(labelAdapter);
    }

    /**
     * label tag信息
     */
    public class LabelTagInfo {
        /**
         * 记录label索引
         */
        public int index;
        /**
         * 记录label背景
         */
        public Object background;
        /**
         * 记录label是否选中
         */
        public boolean isSelect;
    }

    /**
     * 标签流式布局适配器
     */
    public abstract static class FlowLabelAdapter {
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
         * 对应label的字体大小(单位sp),不重写会默认返回-1(会使用默认字体大小)
         *
         * @param position
         * @return
         */
        public float getLabelTextSize(int position) {
            return -1;
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

        /**
         * 对应label的background drawable,不重写会默认返回null
         * 当它返回不为null,则优先级大于getLabelBackground
         *
         * @param position
         * @return
         */
        public Drawable getLabelBackgroundDrawable(int position) {
            return null;
        }
    }

}
