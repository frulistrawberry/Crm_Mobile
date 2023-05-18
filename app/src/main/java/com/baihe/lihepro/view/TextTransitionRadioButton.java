package com.baihe.lihepro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2019-09-22
 * Description：文字过渡转换RadioButton
 */
public class TextTransitionRadioButton extends AlertRadioButton {
    private static final int LEFT_VALUE = 0;
    private static final int CENTER_VALUE = 1;
    private static final int RIGHT_VALUE = 2;

    public enum TextGravity {
        LEFT(LEFT_VALUE),
        CENTER(CENTER_VALUE),
        RIGHT(RIGHT_VALUE);

        int value;

        TextGravity(int value) {
            this.value = value;
        }

        int valueOf() {
            return value;
        }
    }

    private boolean selectedTextBold;
    private boolean unSelectedTextBold;
    private float selectedTextSize;
    private float unSelectedTextSize;
    private int selectedTextColor;
    private int unSelectedTextColor;
    private boolean isTransition;
    private TextGravity textGravity;
    private Paint textPaint;
    private float percentage = 1.0F;
    private List<CharInfo> charInfos = new ArrayList<>();
    private List<Float> offsetXValues = new ArrayList<>();
    private List<Float> offsetYValues = new ArrayList<>();
    private int selectTextWidth;
    private float selectTextHeight;
    private float selectTextOffset;
    private Context context;

    public TextTransitionRadioButton(Context context) {
        this(context, null);
    }

    public TextTransitionRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public TextTransitionRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextTransitionRadioButton, defStyleAttr, 0);
        selectedTextBold = typedArray.getBoolean(R.styleable.TextTransitionRadioButton_selected_textBold, false);
        unSelectedTextBold = typedArray.getBoolean(R.styleable.TextTransitionRadioButton_unselected_textBold, false);
        selectedTextSize = typedArray.getDimension(R.styleable.TextTransitionRadioButton_selected_textSize, CommonUtils.sp2px(context, 20));
        unSelectedTextSize = typedArray.getDimension(R.styleable.TextTransitionRadioButton_unselected_textSize, CommonUtils.sp2px(context, 16));
        selectedTextColor = typedArray.getColor(R.styleable.TextTransitionRadioButton_selected_textColor, Color.parseColor("#4A4C5C"));
        unSelectedTextColor = typedArray.getColor(R.styleable.TextTransitionRadioButton_unselected_textColor, Color.parseColor("#4A4C5C"));
        isTransition = typedArray.getBoolean(R.styleable.TextTransitionRadioButton_isTransition, true);
        int textGravityValue = typedArray.getInteger(R.styleable.TextTransitionRadioButton_text_gravity, TextGravity.CENTER.valueOf());
        switch (textGravityValue) {
            case LEFT_VALUE:
                textGravity = TextGravity.LEFT;
                break;
            case CENTER_VALUE:
                textGravity = TextGravity.CENTER;
                break;
            case RIGHT_VALUE:
                textGravity = TextGravity.RIGHT;
                break;
        }
        typedArray.recycle();
        setButtonDrawable(null);
        setCompoundDrawables(null, null, null, null);
        float maxTextSize = selectedTextSize > unSelectedTextSize ? selectedTextSize : unSelectedTextSize;
        //把自身填充到最大值
        setTextSize(CommonUtils.pxTosp(context, maxTextSize));
        //隐藏自身文字
        setTextColor(Color.TRANSPARENT);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        initTextInfos(getText().toString());
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                initTextInfos(editable.toString());
            }
        });
    }

    public void setPercentage(@FloatRange(from = 0.0F, to = 1.0F) float percentage) {
        this.percentage = percentage;
    }

    private void initTextInfos(String content) {
        charInfos.clear();
        offsetXValues.clear();
        offsetYValues.clear();

        textPaint.setTextSize(unSelectedTextSize);
        textPaint.setFakeBoldText(unSelectedTextBold);
        int unSelectTextWidth = 0;
        float unSelectTextHeight = CommonUtils.getTextHeight(textPaint);
        float[] unSelectTextXValues = new float[content.length()];
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            float[] widths = new float[1];
            String charStr = String.valueOf(ch);
            textPaint.getTextWidths(charStr, widths);
            unSelectTextXValues[i] = unSelectTextWidth;
            unSelectTextWidth += widths[0];
        }

        textPaint.setTextSize(selectedTextSize);
        textPaint.setFakeBoldText(selectedTextBold);
        selectTextWidth = 0;
        selectTextHeight = CommonUtils.getTextHeight(textPaint);
        selectTextOffset = CommonUtils.getTextOffset(textPaint);
        float[] selectTextXValues = new float[content.length()];
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            float[] widths = new float[1];
            String charStr = String.valueOf(ch);
            textPaint.getTextWidths(charStr, widths);
            CharInfo charInfo = new CharInfo();
            charInfo.y = selectTextOffset;
            charInfo.x = selectTextWidth;
            charInfo.charStr = charStr;
            charInfos.add(charInfo);
            selectTextXValues[i] = selectTextWidth;
            selectTextWidth += widths[0];
        }

        int maxwidth = selectTextWidth > unSelectTextWidth ? selectTextWidth : unSelectTextWidth;
        int unSelectTextLeft = 0;
        switch (textGravity) {
            case LEFT:
                unSelectTextLeft = 0;
                break;
            case CENTER:
                unSelectTextLeft = (maxwidth - unSelectTextWidth) / 2;
                break;
            case RIGHT:
                unSelectTextLeft = maxwidth - unSelectTextWidth;
                break;
        }
        int selectTextLeft = 0;
        switch (textGravity) {
            case LEFT:
                selectTextLeft = 0;
                break;
            case CENTER:
                selectTextLeft = (maxwidth - selectTextWidth) / 2;
                break;
            case RIGHT:
                selectTextLeft = maxwidth - selectTextWidth;
                break;
        }
        for (int i = 0; i < content.length(); i++) {
            float offsetXValue = (unSelectTextXValues[i] + unSelectTextLeft) - (selectTextXValues[i] + selectTextLeft);
            float offsetYValue = (unSelectTextHeight - selectTextHeight) / 2;
            offsetXValues.add(offsetXValue);
            offsetYValues.add(offsetYValue);
        }
    }

    /**
     * 选中到不选中的百分比进程,0代表被选中, 1代表不被选中(离开)
     *
     * @param percentage
     */
    public void move(float percentage) {
        if (isTransition) {
            this.percentage = percentage;
            invalidate();
        } else {
            if (percentage == 0 || percentage == 1) {
                this.percentage = percentage;
                invalidate();
            }
        }
    }

    public void setSelectedTextBold(boolean selectedTextBold) {
        this.selectedTextBold = selectedTextBold;
    }

    public void setUnSelectedTextBold(boolean unSelectedTextBold) {
        this.unSelectedTextBold = unSelectedTextBold;
    }

    public void setSelectedTextSize(float selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
        float maxTextSize = selectedTextSize > unSelectedTextSize ? selectedTextSize : unSelectedTextSize;
        //把自身填充到最大值
        setTextSize(CommonUtils.pxTosp(context, maxTextSize));
    }

    public void setUnSelectedTextSize(float unSelectedTextSize) {
        this.unSelectedTextSize = unSelectedTextSize;
        float maxTextSize = selectedTextSize > unSelectedTextSize ? selectedTextSize : unSelectedTextSize;
        //把自身填充到最大值
        setTextSize(CommonUtils.pxTosp(context, maxTextSize));
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public void setUnSelectedTextColor(int unSelectedTextColor) {
        this.unSelectedTextColor = unSelectedTextColor;
    }

    public void setTextGravity(TextGravity textGravity) {
        this.textGravity = textGravity;
    }

    public void setTransition(boolean transition) {
        isTransition = transition;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (charInfos.size() > 0) {
            float textSize = selectedTextSize + (unSelectedTextSize - selectedTextSize) * percentage;
            textPaint.setColor(CommonUtils.transformColor(selectedTextColor, unSelectedTextColor, percentage));
            textPaint.setTextSize(textSize);
            if (percentage < 0.5) {
                if (selectedTextBold) {
                    textPaint.setFakeBoldText(true);
                } else {
                    textPaint.setFakeBoldText(false);
                }
            } else {
                if (unSelectedTextBold) {
                    textPaint.setFakeBoldText(true);
                } else {
                    textPaint.setFakeBoldText(false);
                }
            }

            for (int i = 0; i < charInfos.size(); i++) {
                float offsetX = offsetXValues.get(i) * percentage;
                float offsetY = offsetYValues.get(i) * percentage;
                canvas.drawText(charInfos.get(i).charStr, charInfos.get(i).x + offsetX, (getHeight() - selectTextHeight) / 2 + selectTextOffset + offsetY, textPaint);
            }
        }
    }

    public class CharInfo {
        public float x;
        public float y;
        public String charStr;
    }
}
