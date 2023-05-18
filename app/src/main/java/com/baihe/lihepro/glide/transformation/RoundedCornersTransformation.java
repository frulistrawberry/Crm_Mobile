package com.baihe.lihepro.glide.transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Author：xubo
 * Time：2020-08-04
 * Description：
 */
public class RoundedCornersTransformation extends BitmapTransformation {
    private static final String ID = RoundedCornersTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    /** 圆角大小 */
    private float roundRadius;
    /** 宽高比 (不设置使用图片默认宽高比) */
    private float aspectRatio;
    /** 输出图片宽 */
    private int outWidth;

    public RoundedCornersTransformation(float roundRadius) {
        this.roundRadius = roundRadius;
        this.aspectRatio = 0;
    }

    public RoundedCornersTransformation(float roundRadius, float aspectRatio) {
        this.roundRadius = roundRadius;
        this.aspectRatio = aspectRatio;
    }

    public RoundedCornersTransformation(float roundRadius, float aspectRatio, int outWidth) {
        this.roundRadius = roundRadius;
        this.aspectRatio = aspectRatio;
        this.outWidth = outWidth;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int oldWidth = toTransform.getWidth();
        int oldHeight = toTransform.getHeight();
        Bitmap bitmap;
        if (aspectRatio == 0) {
            bitmap = Bitmap.createBitmap(oldWidth, oldHeight, Bitmap.Config.ARGB_8888);
        } else {
            float oldAspectRatio = (float) oldWidth / oldHeight;
            if (oldAspectRatio > aspectRatio) {  //以长为标准
                int newWidth = (int) (oldHeight * aspectRatio);
                bitmap = Bitmap.createBitmap(newWidth, oldHeight, Bitmap.Config.ARGB_8888);
            } else {  //以宽为标准
                int newHeight = (int) (oldWidth / aspectRatio);
                bitmap = Bitmap.createBitmap(oldWidth, newHeight, Bitmap.Config.ARGB_8888);
            }
        }
        Canvas canvas = new Canvas(bitmap);
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(toTransform, -(oldWidth - bitmap.getWidth()) / 2, -(oldHeight - bitmap.getHeight()) / 2, paint);
        if (this.outWidth > 0) {
            float scale = (float) this.outWidth / bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (newBitmap != bitmap) {
                bitmap.recycle();
            }
            return newBitmap;
        }
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] roundRadiusByte = ByteBuffer.allocate(4).putFloat(roundRadius).array();
        messageDigest.update(roundRadiusByte);

        byte[] aspectRatioByte = ByteBuffer.allocate(4).putFloat(aspectRatio).array();
        messageDigest.update(aspectRatioByte);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoundedCornersTransformation) {
            RoundedCornersTransformation other = (RoundedCornersTransformation) obj;
            return roundRadius == other.roundRadius && aspectRatio == other.aspectRatio;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (ID + String.valueOf(roundRadius) + String.valueOf(aspectRatio)).hashCode();
    }

}
