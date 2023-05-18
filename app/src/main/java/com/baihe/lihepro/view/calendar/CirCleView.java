package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CirCleView extends View {

    private String colorStr;
    private Paint mPaint;

    public CirCleView(Context context) {
        super(context);
        init();
    }

    public CirCleView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init();
    }

    private void init(){
        colorStr = "#FF1E5A";
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);//画笔属性是实心圆
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(colorStr));
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
        mPaint.setColor(Color.parseColor(colorStr));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, getWidth() >> 1,mPaint);
    }
}
