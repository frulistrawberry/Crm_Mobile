package com.baihe.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.baihe.common.R;

/**
 * Author：xubo
 * Time：2020-07-28
 * Description：
 */
public class FontTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final float LIGHT_WIDTH = 0.0F;
    private static final float NORMAL_WIDTH = 0.5F;
    private static final float HAFT_BOLD_WIDTH = 1.2F;
    private static final float BOLD_WIDTH = 1.8F;

    public static final int LIGHT_VALUE = 0;
    public static final int NORMAL_VALUE = 1;
    public static final int HAFT_BOLD_VALUE = 2;
    public static final int BOLD_VALUE = 3;

    private FontStyle fontStyle = FontStyle.LIGHT;

    public enum FontStyle {
        /**
         * 细体（实际为常规字体，当作设计图的细体）
         */
        LIGHT(LIGHT_VALUE),
        /**
         * 常规字体（实际为中粗体，当作设计图的常规字体）
         */
        NORMAL(NORMAL_VALUE),
        /**
         * 中粗体（实际为粗体，当作设计图的中粗体）
         */
        HALF_BOLD(HAFT_BOLD_VALUE),
        /**
         * 粗体 (更粗的字体)
         */
        BOLD(BOLD_VALUE);

        int value;

        FontStyle(int value) {
            this.value = value;
        }

        public int valueOf() {
            return value;
        }
    }

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView, defStyleAttr, 0);
        int fontStyleValue = typedArray.getInt(R.styleable.FontTextView_font_textStyle, FontStyle.LIGHT.valueOf());
        switch (fontStyleValue) {
            case LIGHT_VALUE:
                fontStyle = FontStyle.LIGHT;
                break;
            case NORMAL_VALUE:
                fontStyle = FontStyle.NORMAL;
                break;
            case HAFT_BOLD_VALUE:
                fontStyle = FontStyle.HALF_BOLD;
                break;
            case BOLD_VALUE:
                fontStyle = FontStyle.BOLD;
                break;
        }
        setFontStyle(fontStyle);
        setIncludeFontPadding(false);
    }

    /**
     * 设置字体大小
     *
     * @param fontStyle
     */
    public void setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
        switch (fontStyle) {
            case BOLD:
                boldStyle();
                break;
            case HALF_BOLD:
                halfBoldStyle();
                break;
            case NORMAL:
                normalStyle();
                break;
            case LIGHT:
            default:
                lightStyle();
                break;
        }
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    private void lightStyle() {
        invalidBold();
        TextPaint textPaint = getPaint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(LIGHT_WIDTH);
    }

    private void normalStyle() {
        invalidBold();
        TextPaint textPaint = getPaint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(NORMAL_WIDTH);
    }

    private void halfBoldStyle() {
        invalidBold();
        TextPaint textPaint = getPaint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(HAFT_BOLD_WIDTH);
    }

    private void boldStyle() {
        invalidBold();
        TextPaint textPaint = getPaint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeWidth(BOLD_WIDTH);
    }

    /**
     * 无效TextView默认的粗体和斜粗体
     */
    private void invalidBold() {
        Typeface typeface = getTypeface();
        if (typeface == null) {
            return;
        }
        //粗体和斜粗体无效
        int style = typeface.getStyle();
        if (style == Typeface.BOLD) {
            setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else if (style == Typeface.BOLD_ITALIC) {
            setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {

    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {

    }
}
