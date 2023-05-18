package com.baihe.lihepro.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Author：xubo
 * Time：2020-08-05
 * Description：
 */
public class PhotoBrowserView extends AppCompatImageView {
    /**
     * 操作模式
     */
    private enum MODE {
        /**
         * 普通
         */
        NONE,
        /**
         * 单点移动
         */
        DRAG,
        /**
         * 缩放
         */
        ZOOM
    }

    /**
     * 最小缩放值配置
     */
    private static final float MIN_SCALE = 1F;
    /**
     * 最大缩放值配置
     */
    private static final float MAX_SCALE = 3.5F;
    /**
     * 最大回退缩放配置
     */
    private static final float MIN_BACK_SCALE = 0.8F;
    /**
     * 最小回退缩放值配置
     */
    private static final float MAX_BACK_SCALE = 5.0F;
    /**
     * 缩放两指间的最小距离配置
     */
    private static final float ZOOM_MIN_SPACE = 5F;
    /**
     * 移动缩放动画一般执行时间配置
     */
    private static final int MOVE_SCALE_ANIMATION_TIME = 200;

    /**
     * 最小缩放值
     */
    private float minScale;
    /**
     * 最大缩放值
     */
    private float maxScale;
    /**
     * 最大回退缩放
     */
    private float minBackScale;
    /**
     * 最小回退缩放值
     */
    private float maxBackScale;
    /**
     * 缩放两指间的最小距离
     */
    private float zoomMinSpace;

    /**
     * 操作模式
     */
    private MODE mode = MODE.NONE;
    /**
     * 图片矩阵
     */
    private Matrix matrix;
    /**
     * view宽度
     */
    private int width;
    /**
     * view高度
     */
    private int height;
    /**
     * 原始图片的宽度
     */
    private int drawableWidth;
    /**
     * 原始图片的高度
     */
    private int drawableHeight;
    /**
     * 触摸按压点
     */
    private PointF point;
    /**
     * 两指之间的中点
     */
    private PointF midPoint;
    /**
     * 默认缩放值
     */
    private float scale;
    /**
     * 当前缩放值
     */
    private float currentScale;
    /**
     * 手势库
     */
    private GestureDetector gestureDetector;
    /**
     * 开始缩放时两指之间的距离
     */
    private float preSpace;
    /**
     * 是否阻止手势事件
     */
    private boolean isStopTouch;
    /**
     * 是否开始动画
     */
    private boolean isStartAnimation;
    /**
     * 是否可以向左划
     */
    private boolean canLeft;
    /**
     * 是否可以向右划
     */
    private boolean canRight;
    /**
     * 向左滑动
     */
    private boolean scrollLeft;
    /**
     * 向右滑动
     */
    private boolean scrollRight;
    /**
     * 该子类对应的索引
     */
    private int index;
    /**
     * ViewPage拥有子类的数量
     */
    private int childCount;
    /**
     * 矩阵
     */
    private RectF rect;

    private float initRectLeft;
    private float initRectRight;

    public PhotoBrowserView(Context context) {
        this(context, null);
    }

    public PhotoBrowserView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public PhotoBrowserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setScaleType(ImageView.ScaleType.MATRIX);
        matrix = new Matrix();
        point = new PointF();
        midPoint = new PointF();
        float density = context.getResources().getDisplayMetrics().density;
        zoomMinSpace = density * ZOOM_MIN_SPACE;
        isStopTouch = true;
        rect = new RectF();

        setLongClickable(true);
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setOnDoubleTapListener(doubleTapListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    /**
     * 初始化矩阵
     */
    public void initMatrix() {
        matrix.reset();
        Drawable drawable = getDrawable();
        if (drawable == null || width == 0 || height == 0) {
            return;
        }
        drawableWidth = drawable.getIntrinsicWidth();
        drawableHeight = drawable.getIntrinsicHeight();
        float scaleX = (float) width / drawableWidth;
        float scaleY = (float) height / drawableHeight;
        scale = scaleX;
        minScale = scale * MIN_SCALE;
        maxScale = scale * MAX_SCALE;
        minBackScale = scale * MIN_BACK_SCALE;
        maxBackScale = scale * MAX_BACK_SCALE;
        currentScale = scale;
        matrix.postScale(scale, scale);
        Float[] translates = getTranslate(matrix);
        initRectLeft = translates[0];
        initRectRight = initRectLeft + drawableWidth * scale;
        matrix.postTranslate(translates[0], translates[1]);
        updateMatrix(matrix);
    }

    /**
     * 获取偏移量
     *
     * @param matrix
     * @return x, y轴的偏移数组
     */
    private Float[] getTranslate(Matrix matrix) {
        RectF rectF = new RectF(0, 0, drawableWidth, drawableHeight);
        matrix.mapRect(rectF);
        float newDrawableHeight = rectF.height();
        float newDrawableWidth = rectF.width();
        float dx = 0, dy = 0;
        if (newDrawableWidth <= width) {
            dx = (width - newDrawableWidth) / 2 - rectF.left;
        } else if (newDrawableWidth > width) {
            if (!(rectF.left < 0 && rectF.right > width)) {
                if (rectF.left < 0) {
                    dx = width - rectF.right;
                } else if (rectF.right > width) {
                    dx = -rectF.left;
                }
            }
        }
        if (newDrawableHeight <= height) {
            dy = (height - newDrawableHeight) / 2 - rectF.top;
        } else if (newDrawableHeight > height) {
            if (!(rectF.top < 0 && rectF.bottom > height)) {
                if (rectF.top < 0) {
                    dy = height - rectF.bottom;
                } else if (rectF.bottom > height) {
                    dy = -rectF.top;
                }
            }
        }
        Float[] floats = {dx, dy};
        return floats;
    }

    /**
     * 更新矩阵
     *
     * @param matrix
     */
    private void updateMatrix(Matrix matrix) {
        rect.set(0, 0, drawableWidth, drawableHeight);
        matrix.mapRect(rect);
        if (rect.left == initRectLeft || (currentScale != scale && rect.left == 0)) {
            canLeft = true;
        } else {
            canLeft = false;
        }
        if (rect.right == initRectRight || (currentScale != scale && rect.right == width)) {
            canRight = true;
        } else {
            canRight = false;
        }
        setImageMatrix(matrix);
    }

    /**
     * 获取两指之间的距离
     *
     * @param event
     * @return
     */
    private float getSpacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0f;
    }

    /**
     * 设置两指之间的中心点
     *
     * @param point
     * @param event
     */
    private void setMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (canLeft) {
                        scrollLeft = true;
                    } else {
                        scrollLeft = false;
                    }
                    if (canRight) {
                        scrollRight = true;
                    } else {
                        scrollRight = false;
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mode = MODE.DRAG;
                    point.set(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    preSpace = getSpacing(event);
                    setMidPoint(midPoint, event);
                    if (preSpace >= zoomMinSpace) {
                        mode = MODE.ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float currentSpace = getSpacing(event);
                    if (mode == MODE.DRAG) {
                        float x = event.getX();
                        float dx = x - point.x;
                        if (scrollLeft && dx > 0f && index != 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        if (scrollRight && dx < 0f && index != (childCount - 1)) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    if (currentSpace >= zoomMinSpace && mode == MODE.ZOOM) {
                        float scale = currentSpace / preSpace;
                        if (scale > 1) {
                            scale = (scale - 1) / 2 + 1;
                        } else if (scale < 1) {
                            scale = 1 - Math.abs((scale - 1) / 2);
                        }
                        float currentScale = scale * this.currentScale;
                        if (currentScale >= minBackScale && currentScale <= maxBackScale) {
                            this.currentScale = currentScale;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            preSpace = currentSpace;
                            updateMatrix(matrix);
                        } else if (currentScale > maxBackScale) {
                            scale = maxBackScale / this.currentScale;
                            this.currentScale = maxBackScale;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            preSpace = currentSpace;
                            updateMatrix(matrix);
                        } else if (currentScale < minBackScale) {
                            scale = minBackScale / this.currentScale;
                            this.currentScale = minBackScale;
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            preSpace = currentSpace;
                            updateMatrix(matrix);
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = MODE.DRAG;
                    float scale = 1.0f;
                    if (currentScale < minScale) {
                        scale = minScale / currentScale;
                        currentScale = minScale;
                    } else if (currentScale > maxScale) {
                        scale = maxScale / currentScale;
                        currentScale = maxScale;
                    }
                    Matrix startMatrix = new Matrix();
                    startMatrix.set(matrix);
                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    Float[] translates = getTranslate(matrix);
                    matrix.postTranslate(translates[0], translates[1]);
                    moveScaleAnimation(scale, translates[0], translates[1], midPoint.x, midPoint.y, startMatrix, matrix);
                    if (isStartAnimation) {
                        isStopTouch = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mode = MODE.NONE;
                    break;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 滑动缩放动画
     *
     * @param scale
     * @param dx
     * @param dy
     * @param midX
     * @param midY
     * @param startMatrix
     * @param endMatrix
     * @param animationTime
     */
    private void moveScaleAnimation(final float scale, final float dx, final float dy, final float midX,
                                    final float midY, final Matrix startMatrix, final Matrix endMatrix, int animationTime) {
        isStartAnimation = true;
        final Matrix matrix = new Matrix();
        final float difference = Math.abs(1 - scale);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(animationTime);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                float value = (Float) animation.getAnimatedValue();
                float tempScale = value * difference;
                float tempDx = value * dx;
                float tempDy = value * dy;
                float cScaele = 0f;
                if (scale > 1) {
                    cScaele = tempScale + 1;
                } else {
                    cScaele = 1 - tempScale;
                }
                matrix.set(startMatrix);
                matrix.postScale(cScaele, cScaele, midX, midY);
                matrix.postTranslate(tempDx, tempDy);
                updateMatrix(matrix);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                isStartAnimation = false;
                isStopTouch = false;
                updateMatrix(endMatrix);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
            }
        });
        valueAnimator.start();

    }

    /**
     * 滑动缩放动画
     *
     * @param scale
     * @param dx
     * @param dy
     * @param midX
     * @param midY
     * @param startMatrix
     * @param endMatrix
     * @param animationTime
     * @param restListener
     */
    private void moveScaleAnimation(final float scale, final float dx, final float dy, final float midX,
                                    final float midY, final Matrix startMatrix, final Matrix endMatrix, int animationTime,
                                    final RestListener restListener) {
        isStartAnimation = true;
        final Matrix matrix = new Matrix();
        final float difference = Math.abs(1 - scale);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(animationTime);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                float value = (Float) animation.getAnimatedValue();
                float tempScale = value * difference;
                float tempDx = value * dx;
                float tempDy = value * dy;
                float cScaele = 0f;
                if (scale > 1) {
                    cScaele = tempScale + 1;
                } else {
                    cScaele = 1 - tempScale;
                }
                matrix.set(startMatrix);
                matrix.postScale(cScaele, cScaele, midX, midY);
                matrix.postTranslate(tempDx, tempDy);
                updateMatrix(matrix);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                isStartAnimation = false;
                isStopTouch = false;
                updateMatrix(endMatrix);
                restListener.restFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
            }
        });
        valueAnimator.start();

    }

    /**
     * 滑动缩放动画
     *
     * @param scale
     * @param dx
     * @param dy
     * @param midX
     * @param midY
     * @param startMatrix
     * @param endMatrix
     */
    private void moveScaleAnimation(final float scale, final float dx, final float dy, final float midX,
                                    final float midY, final Matrix startMatrix, final Matrix endMatrix) {
        moveScaleAnimation(scale, dx, dy, midX, midY, startMatrix, endMatrix, MOVE_SCALE_ANIMATION_TIME);
    }

    @Override
    public void setImageResource(int resId) {
        // TODO Auto-generated method stub
        super.setImageResource(resId);
        isStopTouch = false;
        initMatrix();
    }

    @Override
    public void setImageURI(Uri uri) {
        // TODO Auto-generated method stub
        super.setImageURI(uri);
        isStopTouch = false;
        initMatrix();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        // TODO Auto-generated method stub
        super.setImageDrawable(drawable);
        isStopTouch = false;
        initMatrix();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        // TODO Auto-generated method stub
        super.setImageBitmap(bm);
        isStopTouch = false;
        initMatrix();
    }

    public interface RestListener {
        void restFinish();
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            if (isStopTouch) {
                return false;
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mode == MODE.ZOOM) {
                matrix.postTranslate(-distanceX / 2, -distanceY / 2);
                updateMatrix(matrix);
            } else if (mode == MODE.DRAG) {
                matrix.postTranslate(-distanceX, -distanceY);
                RectF rectF = new RectF(0, 0, drawableWidth, drawableHeight);
                matrix.mapRect(rectF);
                float newDrawableHeight = rectF.height();
                float newDrawableWidth = rectF.width();
                float dx = 0, dy = 0;
                if (newDrawableWidth > width) {
                    if (rectF.left > 0) {
                        dx = -rectF.left;
                    }
                    if (rectF.right < width) {
                        dx = width - rectF.right;
                    }
                } else {
                    dx = distanceX;
                }
                if (newDrawableHeight > height) {
                    if (rectF.top > 0) {
                        dy = -rectF.top;
                    }
                    if (rectF.bottom < height) {
                        dy = height - rectF.bottom;
                    }
                } else {
                    dy = distanceY;
                }
                matrix.postTranslate(dx, dy);
                updateMatrix(matrix);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    private GestureDetector.OnDoubleTapListener doubleTapListener = new GestureDetector.OnDoubleTapListener() {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float scale = 1.0f;
            if (currentScale > minScale) {
                scale = minScale / currentScale;
                currentScale = minScale;
            } else {
                scale = maxScale / currentScale;
                currentScale = maxScale;
            }
            Matrix startMatrix = new Matrix();
            startMatrix.set(matrix);
            matrix.postScale(scale, scale, e.getX(), e.getY());
            Float[] translates = getTranslate(matrix);
            matrix.postTranslate(translates[0], translates[1]);
            moveScaleAnimation(scale, translates[0], translates[1], e.getX(), e.getY(), startMatrix, matrix, 280);
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };

    public void setParams(int childIndex, int childCount) {
        this.index = childIndex;
        this.childCount = childCount;
    }
}
