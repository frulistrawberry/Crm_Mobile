package com.baihe.common.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.IntRange;
import androidx.annotation.Size;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author：xubo
 * Time：2019-05-15
 * Description：bitmap工具类
 */

public class BitmapUtils {
    /**
     * 通过资源文件创建指定大小的bitmap(居中剪裁)
     * @param context 上下文
     * @param resId 资源id
     * @param outWidth bitmap指定宽度
     * @param outHeight bitmap指定高度
     * @return
     */
    public static Bitmap creatBitmap(Context context, int resId, @Size(min = 1) int outWidth, @Size(min = 1) int outHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int inSampleSize = calculateInSampleSize(options, outWidth, outHeight, true);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return creatBitmap(bitmap, outWidth, outHeight, true, true);
    }

    /**
     * 通过图片路径创建指定大小的bitmap(居中剪裁)
     * @param picturePath 图片路径
     * @param outWidth bitmap指定宽度
     * @param outHeight bitmap指定高度
     * @return
     */
    public static Bitmap creatBitmap(String picturePath, @Size(min = 1) int outWidth, @Size(min = 1) int outHeight) {
        int degree = readPictureDegree(picturePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        int inSampleSize = calculateInSampleSize(options, outWidth, outHeight, true);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        return creatBitmap(bitmap, outWidth, outHeight, true, true);
    }

    /**
     * 通过bitmap创建指定大小的bitmap(居中剪裁)
     * @param bitmap 目标bitmap
     * @param outWidth bitmap指定宽度
     * @param outHeight bitmap指定高度
     * @param alpha 图片是否有alpha通道(是否有透明部分)
     * @param isRecycle 原有bitmap是否自动释放(无需复用或无其他引用可使用true自动释放)
     * @return
     */
    public static Bitmap creatBitmap(Bitmap bitmap, @Size(min = 1) int outWidth, @Size(min = 1) int outHeight, boolean alpha, boolean isRecycle) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth == outWidth && bitmapHeight == outHeight) {
            return bitmap;
        }
        Bitmap targetBitmap;
        if (alpha) {
            targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        } else {
            targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565);
        }
        float scaleW = (float) outWidth / bitmapWidth;
        float scaleH = (float) outHeight / bitmapHeight;
        float scale = scaleW > scaleH ? scaleW : scaleH;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap drawBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        if (isRecycle && drawBitmap != bitmap) {
            bitmap.recycle();
        }
        Canvas canvas = new Canvas(targetBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(drawBitmap, -(drawBitmap.getWidth() - outWidth) / 2, -(drawBitmap.getHeight() - outHeight) / 2, paint);
        drawBitmap.recycle();
        return targetBitmap;
    }

    /**
     * 通过资源文件创建指定比例的bitmap
     * 设置参考宽高可使生成的bitmap接近参考大小
     * 不设置参考宽高(小于等于0),生成的图片是原图上剪裁比例bitmap
     * @param context 上下文
     * @param resId 资源id
     * @param aspectRatio 宽高比
     * @param referWidth 参考宽 小于等于0表示不指定宽参考;如果有参考高,以参考高为准
     * @param referHeight 参考高 小于等于0表示指定高参考;如果有参考宽,以参考宽为准
     * @param sampling 采样模式
     * @return
     */
    public static Bitmap creatBitmap(Context context, int resId, float aspectRatio, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, boolean sampling) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize;
        if (referWidth <= 0 && referHeight <= 0) {
            inSampleSize = 1;
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            inSampleSize = calculateInSampleSize(options, referWidth, referHeight, sampling);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return creatBitmap(bitmap, aspectRatio, true, true);
    }

    /**
     * 通过图片路径创建指定比例的bitmap
     * 设置参考宽高可使生成的bitmap接近参考大小
     * 不设置参考宽高(小于等于0),生成的图片是原图上剪裁比例bitmap
     * @param picturePath 图片路径
     * @param aspectRatio 宽高比
     * @param referWidth 参考宽 小于等于0表示不指定宽参考;如果有参考高,以参考高为准
     * @param referHeight 参考高 小于等于0表示指定高参考;如果有参考宽,以参考宽为准
     * @param sampling 采样模式
     * @return
     */
    public static Bitmap creatBitmap(String picturePath, float aspectRatio, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, boolean sampling) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize;
        if (referWidth <= 0 && referHeight <= 0) {
            inSampleSize = 1;
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            inSampleSize = calculateInSampleSize(options, referWidth, referHeight, sampling);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        return creatBitmap(bitmap, aspectRatio, true, true);
    }


    /**
     * 通过图片路径创建限定最大大小的bitmap
     * @param picturePath 图片路径
     * @param maxSize bitmap最大大小
     * @return
     */
    public static Bitmap creatBitmap(String picturePath, long maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        while (bitmap.getRowBytes() * bitmap.getHeight() > maxSize) {
            bitmap.recycle();
            inSampleSize *= 2;
            options.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeFile(picturePath, options);
        }
        return bitmap;
    }

    /**
     * 通过bitmap创建指定比例的bitmap
     * @param bitmap 目标bitmap
     * @param aspectRatio 宽高比
     * @param alpha 图片是否有alpha通道(是否有透明部分)
     * @param isRecycle 原有bitmap是否自动释放(无需复用或无其他引用可使用true自动释放)
     * @return
     */
    public static Bitmap creatBitmap(Bitmap bitmap, float aspectRatio, boolean alpha, boolean isRecycle) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float bitmapAspectRatio = (float) bitmapWidth / bitmapHeight;
        if (bitmapAspectRatio == aspectRatio) {
            return bitmap;
        }
        int outWidth;
        int outHeight;
        if (bitmapAspectRatio > aspectRatio) {
            outWidth = (int) (bitmapHeight * aspectRatio);
            outHeight = bitmapHeight;
        } else {
            outWidth = bitmapWidth;
            outHeight = (int) (bitmapWidth / aspectRatio);
        }
        Bitmap targetBitmap;
        if (alpha) {
            targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        } else {
            targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(targetBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, -(bitmapWidth - outWidth) / 2, -(bitmapHeight - outHeight) / 2, paint);
        if (isRecycle) {
            bitmap.recycle();
        }
        return targetBitmap;
    }


    /**
     * 通过参考信息创建Bitmap
     * @param context 上下文
     * @param resId 资源id
     * @param referWidth 参考宽 等于0表示不指定宽参考;如果有参考高,以参考高为准
     * @param referHeight 参考高 等于0表示不指定高参考;如果有参考宽,以参考宽为准
     * @param sampling 采样模式
     * @return
     */
    public static Bitmap creatBitmapForRefer(Context context, int resId, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, boolean sampling) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int inSampleSize = calculateInSampleSize(options, referWidth, referHeight, sampling);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    /**
     * 通过参考信息创建Bitmap
     * @param picturePath 图片路径
     * @param referWidth 参考宽 等于0表示不指定宽参考;如果有参考高,以参考高为准
     * @param referHeight 参考高 等于0表示不指定高参考;如果有参考宽,以参考宽为准
     * @param sampling 采样模式
     * @return
     */
    public static Bitmap creatBitmapForRefer(String picturePath, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, boolean sampling) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        int inSampleSize = calculateInSampleSize(options, referWidth, referHeight, sampling);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    /**
     * 目标bitmap保存至本地
     * @param bitmap 目标bitmap
     * @param saveFolderPath 保存文件夹
     * @param saveFileName 保存文件名
     * @param quality 压缩质量
     * @param alpha 是否有透明通道
     * @return
     */
    public static String saveBitmap(Bitmap bitmap, String saveFolderPath, String saveFileName, @IntRange(from = 0, to = 100) int quality, boolean alpha) {
        File saveFolderFile = new File(saveFolderPath);
        saveFolderFile.mkdirs();
        File saveFile = new File(saveFolderFile, saveFileName);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            quality = quality > 100 ? 100 : quality;
            quality = quality < 0 ? 0 : quality;
            if (alpha) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return saveFile.getAbsolutePath();
    }

    /**
     * 压缩图片
     * @param picturePath 图片路径
     * @param referWidth 参考宽
     * @param referHeight 参考高
     * @param saveFolderPath 保存目录
     * @param saveFileName 保存文件名
     * @param quality 压缩质量
     * @param sampling 采样模式
     * @return
     */
    public static String compressPicture(String picturePath, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, String saveFolderPath, String saveFileName, @IntRange(from = 0, to = 100) int quality, boolean sampling) {
        Bitmap bitmap = creatBitmapForRefer(picturePath, referWidth, referHeight, sampling);
        int degree = readPictureDegree(picturePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap compressBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (compressBitmap != bitmap) {
            bitmap.recycle();
        }
        String savePath = saveBitmap(compressBitmap, saveFolderPath, saveFileName, quality, false);
        compressBitmap.recycle();
        return savePath;
    }

    /**
     * 压缩图片
     * @param picturePath 图片路径
     * @param referWidth 参考宽
     * @param referHeight 参考高
     * @param saveFolderPath 保存目录
     * @param saveFileName 保存文件名
     * @param quality 压缩质量
     * @param sampling 采样模式
     * @return
     */
    public static CompressPictureInfo compressPicture2(String picturePath, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, String saveFolderPath, String saveFileName, @IntRange(from = 0, to = 100) int quality, boolean sampling) {
        Bitmap bitmap = creatBitmapForRefer(picturePath, referWidth, referHeight, sampling);
        int degree = readPictureDegree(picturePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap compressBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (compressBitmap != bitmap) {
            bitmap.recycle();
        }
        String savePath = saveBitmap(compressBitmap, saveFolderPath, saveFileName, quality, false);

        CompressPictureInfo compressPictureInfo = new CompressPictureInfo();
        compressPictureInfo.width = compressBitmap.getWidth();
        compressPictureInfo.height = compressBitmap.getHeight();
        compressPictureInfo.compressPicturePath = savePath;

        compressBitmap.recycle();
        return compressPictureInfo;
    }

    /**
     * 读取本地图片角度
     * @param picturePath 图片路径
     * @return
     */
    public static int readPictureDegree(String picturePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(picturePath);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 计算采样值
     * @param options
     * @param referWidth
     * @param referHeight
     * @param sampling 采样模式,true将严格按照参考宽高值来设定采样值,false只需其中一边(参考高或参考边)符合即可
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, @Size(min = 0) int referWidth, @Size(min = 0) int referHeight, boolean sampling) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (referWidth > 0 && referHeight > 0) {
            if (sampling) {
                if (width > referWidth && height > referHeight) {
                    final int halfHeight = height / 2;
                    final int halfWidth = width / 2;
                    while ((halfHeight / inSampleSize) > referHeight && (halfWidth / inSampleSize) > referWidth) {
                        inSampleSize *= 2;
                    }
                }
            } else {
                if (width > referWidth || height > referHeight) {
                    final int halfHeight = height / 2;
                    final int halfWidth = width / 2;
                    while ((halfHeight / inSampleSize) > referHeight || (halfWidth / inSampleSize) > referWidth) {
                        inSampleSize *= 2;
                    }
                }
            }
        } else if (referWidth > 0 && width > referWidth) {
            int halfWidth = width / 2;
            while ((halfWidth / inSampleSize) > referWidth) {
                inSampleSize *= 2;
            }
        } else if (referHeight > 0 && height > referHeight) {
            final int halfHeight = height / 2;
            while ((halfHeight / inSampleSize) > referHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap captureView(LinearLayout view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.setGravity(Gravity.CENTER);
        return bitmap;
    }






    /**
     * 压缩图片信息
     */
    public static class CompressPictureInfo {
        /**
         * 压缩图片本地路径
         */
        public String compressPicturePath;
        /**
         * 压缩图片宽度
         */
        public int width;
        /**
         * 压缩图片高度
         */
        public int height;
    }

}
