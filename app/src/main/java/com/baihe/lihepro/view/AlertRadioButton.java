package com.baihe.lihepro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;

/**
 * Author：xubo
 * Time：2019-09-22
 * Description：带提示的RadioButton,内容文字必须是单行,否则位置会计算出错
 */

public class AlertRadioButton extends AppCompatRadioButton {
    private static final int ALERT_TEXT_COLOR = Color.WHITE;
    private static final int ALERT_BACKGROUND_COLOR = Color.parseColor("#FF6969");
    private static final int TOP_LEFT_VALUE = 0;
    private static final int TOP_RIGHT_VALUE = 1;
    private static final int BOTTOM_LEFT_VALUE = 2;
    private static final int BOTTOM_RIGHT_VALUE = 3;

    public enum AlertGravity {
        TOP_LEFT(TOP_LEFT_VALUE),
        TOP_RIGHT(TOP_RIGHT_VALUE),
        BOTTOM_LEFT(BOTTOM_LEFT_VALUE),
        BOTTOM_RIGHT(BOTTOM_RIGHT_VALUE);

        int value;

        AlertGravity(int value) {
            this.value = value;
        }

        int valueOf() {
            return value;
        }
    }

    private float alertOffsetX;
    private float alertOffsetY;
    private float alertTextSize;
    private int alertTextColor;
    private int alertBackgroundColor;
    private AlertGravity alertGravity = AlertGravity.TOP_RIGHT;

    private RectF contextBoundRect = new RectF();
    private RectF alertRect = new RectF();
    private float alertRaduis;
    private float alertPadding;
    private float alertHalfHeight;
    private float alertHalfWidth;
    private float textOffset;
    private Paint alertBacgroundPaint;
    private Paint alertTextPaint;

    private int alertNumber;
    private boolean isAlert;

    public AlertRadioButton(Context context) {
        this(context, null);
    }

    public AlertRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public AlertRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlertRadioButton);
        alertOffsetX = typedArray.getDimension(R.styleable.AlertRadioButton_alert_offsetX, CommonUtils.dp2px(context, 0));
        alertOffsetY = typedArray.getDimension(R.styleable.AlertRadioButton_alert_offsetY, CommonUtils.dp2px(context, 0));
        alertTextSize = typedArray.getDimension(R.styleable.AlertRadioButton_alert_textSize, CommonUtils.sp2px(context, 10));
        alertTextColor = typedArray.getColor(R.styleable.AlertRadioButton_alert_textColor, ALERT_TEXT_COLOR);
        alertBackgroundColor = typedArray.getColor(R.styleable.AlertRadioButton_alert_backgroundColor, ALERT_BACKGROUND_COLOR);
        int alertGravityValue = typedArray.getInt(R.styleable.AlertRadioButton_alert_gravity, AlertGravity.TOP_RIGHT.valueOf());
        switch (alertGravityValue) {
            case TOP_LEFT_VALUE:
                alertGravity = AlertGravity.TOP_LEFT;
                break;
            case TOP_RIGHT_VALUE:
                alertGravity = AlertGravity.TOP_RIGHT;
                break;
            case BOTTOM_LEFT_VALUE:
                alertGravity = AlertGravity.BOTTOM_LEFT;
                break;
            case BOTTOM_RIGHT_VALUE:
                alertGravity = AlertGravity.BOTTOM_RIGHT;
                break;
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        alertBacgroundPaint = new Paint();
        alertBacgroundPaint.setAntiAlias(true);
        alertBacgroundPaint.setColor(alertBackgroundColor);
        alertTextPaint = new Paint();
        alertTextPaint.setAntiAlias(true);
        alertTextPaint.setColor(alertTextColor);
        alertTextPaint.setTextSize(alertTextSize);

        alertRaduis = CommonUtils.getTextHeightForInt(alertTextPaint) / 2.0f;
        alertPadding = (alertRaduis * 2.0f - CommonUtils.getTextWidth("2", alertTextPaint)) / 2.0f;
        alertHalfHeight = alertRaduis;
        alertHalfWidth = alertRaduis;
        textOffset = CommonUtils.getTextOffset(alertTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String text = getText().toString();
        int textWidth = CommonUtils.getTextWidth(text, getPaint());
        int gravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
        if (getCompoundDrawables()[0] == null && getCompoundDrawables()[1] == null && getCompoundDrawables()[2] == null && getCompoundDrawables()[3] == null) {
            int boundLeft = 0;
            switch (gravity) {
                case Gravity.LEFT:
                    boundLeft = getPaddingLeft();
                    break;
                case Gravity.RIGHT:
                    boundLeft = getMeasuredWidth() - getPaddingRight() - textWidth;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    boundLeft = (getMeasuredWidth() - textWidth) / 2;
                    break;
            }
            contextBoundRect.set(boundLeft, getTotalPaddingTop(), boundLeft + textWidth, getMeasuredHeight() - getTotalPaddingBottom());
        } else {
            Drawable drawableTop = getCompoundDrawables()[1];
            Drawable drawableBottom = getCompoundDrawables()[3];

            int boundTop = getTotalPaddingTop(); //内容边界top

            int drawableTopWidth = 0;
            if (drawableTop != null) {
                boundTop = (boundTop - getCompoundPaddingTop()) + getPaddingTop(); //空余距离加上padding
                drawableTopWidth = drawableTop.getIntrinsicWidth();
            }

            int boundBottom = getMeasuredHeight() - getTotalPaddingBottom(); //内容边界bottom

            int drawableBottomWidth = 0;
            if (drawableBottom != null) {
                boundBottom = getMeasuredHeight() - ((getTotalPaddingBottom() - getCompoundPaddingBottom()) + getPaddingBottom()); //空余距离加上padding
                drawableBottomWidth = drawableBottom.getIntrinsicWidth();
            }

            int centerContentMaxWidth = Math.max(Math.max(drawableTopWidth, drawableBottomWidth), textWidth); //中间区域最大宽度

            int drawableLeftWidthAndPadding = getCompoundPaddingLeft() - getPaddingLeft();
            int drawableRightWidthAndPadding = getCompoundPaddingRight() - getPaddingRight();

            int boundLeft = 0;  //内容边界left
            switch (gravity) {
                case Gravity.LEFT:
                    boundLeft = getPaddingLeft();
                    break;
                case Gravity.RIGHT:
                    getCompoundPaddingRight();
                    boundLeft = getMeasuredWidth() - getCompoundPaddingRight() - centerContentMaxWidth - drawableLeftWidthAndPadding;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    boundLeft = (getMeasuredWidth() - drawableLeftWidthAndPadding - centerContentMaxWidth - drawableRightWidthAndPadding) / 2;
                    break;
            }
            contextBoundRect.set(boundLeft, boundTop, boundLeft + drawableLeftWidthAndPadding + centerContentMaxWidth + drawableRightWidthAndPadding, boundBottom);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //继承的子View绘制需要在红点以下
        if (isAlert) {
            float centerX = 0;
            float centerY = 0;
            switch (alertGravity) {
                case TOP_LEFT:
                    if (alertHalfWidth * 2 - alertOffsetX > contextBoundRect.left) { //如果内容左边部不能容纳提示区域
                        centerX = alertHalfWidth;
                    } else {
                        centerX = contextBoundRect.left + alertOffsetX - alertHalfWidth;
                    }
                    if (alertHalfHeight * 2 - alertOffsetY > contextBoundRect.top) {  //如果内容顶部不能容纳提示区域
                        centerY = alertHalfHeight;
                    } else {
                        centerY = contextBoundRect.top + alertOffsetY - alertHalfHeight;
                    }
                    break;
                case TOP_RIGHT:
                    if (alertHalfWidth * 2 - alertOffsetX > getMeasuredWidth() - contextBoundRect.right) { //如果内容右边部不能容纳提示区域
                        centerX = getMeasuredWidth() - alertHalfWidth;
                    } else {
                        centerX = contextBoundRect.right - alertOffsetX + alertHalfWidth;
                    }
                    if (alertHalfHeight * 2 - alertOffsetY > contextBoundRect.top) {  //如果内容顶部不能容纳提示区域
                        centerY = alertHalfHeight;
                    } else {
                        centerY = contextBoundRect.top + alertOffsetY - alertHalfHeight;
                    }
                    break;
                case BOTTOM_LEFT:
                    if (alertHalfWidth * 2 - alertOffsetX > contextBoundRect.left) { //如果内容左边部不能容纳提示区域
                        centerX = alertHalfWidth;
                    } else {
                        centerX = contextBoundRect.left + alertOffsetX - alertHalfWidth;
                    }
                    if (alertHalfHeight * 2 - alertOffsetY > getMeasuredHeight() - contextBoundRect.bottom) { //如果内容底部不能容纳提示区域
                        centerY = getMeasuredHeight() - alertHalfHeight;
                    } else {
                        centerY = contextBoundRect.bottom - alertOffsetY + alertHalfHeight;
                    }
                    break;
                case BOTTOM_RIGHT:
                    if (alertHalfWidth * 2 - alertOffsetX > getMeasuredWidth() - contextBoundRect.right) { //如果内容右边部不能容纳提示区域
                        centerX = getMeasuredWidth() - alertHalfWidth;
                    } else {
                        centerX = contextBoundRect.right - alertOffsetX + alertHalfWidth;
                    }
                    if (alertHalfHeight * 2 - alertOffsetY > getMeasuredHeight() - contextBoundRect.bottom) { //如果内容底部不能容纳提示区域
                        centerY = getMeasuredHeight() - alertHalfHeight;
                    } else {
                        centerY = contextBoundRect.bottom - alertOffsetY + alertHalfHeight;
                    }
                    break;
            }
            alertRect.set(centerX - alertHalfWidth, centerY - alertHalfHeight, centerX + alertHalfWidth, centerY + alertHalfHeight);
            canvas.drawRoundRect(alertRect, alertRaduis, alertRaduis, alertBacgroundPaint);

            if (alertNumber > 0) {
                if (alertNumber < 10) {
                    String text = String.valueOf(alertNumber);
                    float offsetX = (2 * alertRaduis - CommonUtils.getTextWidth(text, alertTextPaint)) / 2;
                    canvas.drawText(text, alertRect.left + offsetX, alertRect.top + textOffset, alertTextPaint);
                } else if (alertNumber < 100) {
                    String text = String.valueOf(alertNumber);
                    canvas.drawText(text, alertRect.left + alertPadding, alertRect.top + textOffset, alertTextPaint);
                } else {
                    canvas.drawText("99+", alertRect.left + alertPadding, alertRect.top + textOffset, alertTextPaint);
                }
            }
        }
    }

    /**
     * 打开提示
     */
    public void openAlert() {
        isAlert = true;
        invalidate();
    }

    /**
     * 关闭提示
     */
    public void closeAlert() {
        isAlert = false;
        invalidate();
    }

    /**
     * 提示数字,小于0将自动关闭提示
     *
     * @param alertNumber
     */
    public void alert(int alertNumber) {
        if (alertNumber > 0) {
            isAlert = true;
            this.alertNumber = alertNumber;
            if (alertNumber < 10) {
                alertHalfWidth = alertRaduis;
            } else if (alertNumber < 100) {
                alertHalfWidth = (CommonUtils.getTextWidth(String.valueOf(alertNumber), alertTextPaint) + 2 * alertPadding) / 2.0f;
            } else {
                alertHalfWidth = (CommonUtils.getTextWidth("99+", alertTextPaint) + 2 * alertPadding) / 2.0f;
            }
            invalidate();
        } else {
            isAlert = false;
        }
        invalidate();
    }
}
