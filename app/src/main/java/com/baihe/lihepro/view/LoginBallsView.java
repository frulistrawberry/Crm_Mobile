package com.baihe.lihepro.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Random;

/**
 * 登录页的动画背景（彩球）
 */
public class LoginBallsView extends View {
    private final int ballsCount = 6;
    private Paint[] paints = new Paint[ballsCount];
    private int[] colors = {Color.parseColor("#F8D18A"), Color.parseColor("#8CDCE6"), Color.parseColor("#FAB4C8"),
            Color.parseColor("#AEB7E8"), Color.parseColor("#8DD2EC"), Color.parseColor("#FAB4C8")};
    private int[] shadowColors = {Color.parseColor("#88F8D18A"), Color.parseColor("#888CDCE6"), Color.parseColor("#88FAB4C8"),
            Color.parseColor("#88AEB7E8"), Color.parseColor("#888DD2EC"), Color.parseColor("#88FAB4C8")};
    private float[] radiuses = new float[ballsCount];
    private float[] r2w = {0.11f, 0.06f, 0.025f, 0.08f, 0.05f, 0.08f};

    private float[] originXs2w = {0f, 0.24f, 0.37f, 0.63f, 0.76f, 0.96f};
    private float[] originYs2h = {0.22f, 0.55f, 0.44f, 0.59f, 0.38f, 0.17f};
    private Origin[] origins = new Origin[ballsCount];
    private Origin[] currentOrigins = new Origin[ballsCount];

    private float[] maxFreeDistances = new float[ballsCount];
    private double[] distancex = new double[ballsCount];
    private double[] distancey = new double[ballsCount];
    private ValueAnimator valueAnimator;

    private float mViewWidth;
    private float mViewHeight;

    public LoginBallsView(Context context) {
        super(context);
        init(null, 0);
    }

    public LoginBallsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoginBallsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private Handler handler;
    private Runnable animate;

    private void init(AttributeSet attrs, int defStyle) {
        // Set up a default TextPaint object
        for (int i = 0; i < ballsCount; i++) {
            paints[i] = new Paint();
            paints[i].setAntiAlias(true);
            paints[i].setDither(true);
            paints[i].setFilterBitmap(true);
        }

        invalidatePaintsAndMeasurements();


        handler = new Handler();

        animate = new Runnable() {
            @Override
            public void run() {
                if (isBallsReady) {
                    startAnimation();
                } else {
                    handler.postDelayed(animate, 1000);
                }
            }
        };

        handler.postDelayed(animate, 1000);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    private void invalidatePaintsAndMeasurements() {
        for (int i = 0; i < ballsCount; i++) {
            paints[i].setColor(colors[i]);
            paints[i].setShadowLayer(10, 0, 0, shadowColors[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            heightSize = (int) (widthSize * 0.51);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
            widthSize = (int) (heightSize / 0.51);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean isBallsReady = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        for (int i = 0; i < ballsCount; i++) {
            radiuses[i] = mViewWidth * r2w[i];
            currentOrigins[i] = new Origin(mViewWidth * originXs2w[i], mViewHeight * originYs2h[i]);
            origins[i] = new Origin(mViewWidth * originXs2w[i], mViewHeight * originYs2h[i]);
        }
        getFreeAreaForEachBall();
        isBallsReady = true;
    }

    /**
     * 每个球最大的活动范围
     */
    private void getFreeAreaForEachBall() {
        for (int i = 0; i < ballsCount; i++) {
            Origin origin = origins[i];
            for (int k = 0; k < ballsCount; k++) {
                if (i != k) {
                    Origin otherOrigin = origins[k];
                    double distance = Math.sqrt((otherOrigin.x - origin.x) * (otherOrigin.x - origin.x) + (otherOrigin.y - origin.y) * (otherOrigin.y - origin.y));
                    distance = (distance - radiuses[i] - radiuses[k]) / 2;
                    maxFreeDistances[i] = maxFreeDistances[i] == 0 ? (float) distance : (float) Math.min(distance, maxFreeDistances[i]);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < ballsCount; i++) {
            canvas.drawCircle(currentOrigins[i].x, currentOrigins[i].y, radiuses[i], paints[i]);
        }
    }

    private double getRandomAngle() {
        return new Random().nextDouble() * 360;
    }

    private void startAnimation() {

        valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration((long) (5000));
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(1);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                for (int i = 0; i < ballsCount; i++) {
                    currentOrigins[i].x = (float) (origins[i].x + ((float) animation.getAnimatedValue()) * distancex[i]);
                    currentOrigins[i].y = (float) (origins[i].y + ((float) animation.getAnimatedValue()) * distancey[i]);
                }
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 每次动画开始时计算随机运动方向
                for (int i = 0; i < ballsCount; i++) {
                    double angle = getRandomAngle();
                    distancex[i] = Math.sin(angle) * maxFreeDistances[i];
                    distancey[i] = Math.cos(angle) * maxFreeDistances[i];
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 结束后更新各个圆心点位置，防止突然闪现
                for (int i = 0; i < ballsCount; i++) {
                    origins[i].x = currentOrigins[i].x;
                    origins[i].y = currentOrigins[i].y;
                }
                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        valueAnimator.start();
    }

    /**
     * 圆心点
     */
    class Origin {
        float x;
        float y;

        public Origin(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
