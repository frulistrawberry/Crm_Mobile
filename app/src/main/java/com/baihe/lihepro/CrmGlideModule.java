package com.baihe.lihepro;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

/**
 * Author：xubo
 * Time：2020-08-05
 * Description：
 */
@GlideModule
public class CrmGlideModule extends AppGlideModule {
    /**
     * 最大本地缓存大小
     */
    private static final long MAX_DISK_CACHE_SIZE = 250 * 1024 * 1024;
    /**
     * 一般设备的最大内存比例
     */
    private static final float LOW_MAX_MEMORY_SIZE_MULTIPLIER = 0.3f;
    /**
     * 低内存设备的最大内存比例
     */
    private static final float MAX_MEMORY_SIZE_MULTIPLIER = 0.4f;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        /** 分配的最大内存 */
        long maxMemorySize = (long) (isLowRamDevice(activityManager) ? Runtime.getRuntime().maxMemory() * LOW_MAX_MEMORY_SIZE_MULTIPLIER : Runtime.getRuntime().maxMemory() * MAX_MEMORY_SIZE_MULTIPLIER);

        float memoryCacheWeight = 1.0f;
        float bitmapPoolCacheWeight = memoryCacheWeight * 2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bitmapPoolCacheWeight = memoryCacheWeight / 2;
        }
        /** 内存缓存大小 */
        long memoryCacheSize = (long) (maxMemorySize * memoryCacheWeight / (memoryCacheWeight + bitmapPoolCacheWeight));
        /**bitmap缓存池大小*/
        long bitmapPoolCacheSize = (long) (maxMemorySize * bitmapPoolCacheWeight / (memoryCacheWeight + bitmapPoolCacheWeight));

        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        //设置bitmap缓存池大小
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolCacheSize));
        //设置本地缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, getAppCacheDir(context) + "/picture", MAX_DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * 是否是低内存设备
     *
     * @param activityManager
     * @return
     */
    private boolean isLowRamDevice(ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return activityManager.isLowRamDevice();
        } else {
            return false;
        }
    }

    /**
     * 获取应用缓存目录
     *
     * @param context
     * @return
     */
    private String getAppCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null) {
                return cacheFile.getAbsolutePath();
            } else {
                String cacheDir = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName() + "/cache";
                cacheFile = new File(cacheDir);
                if (!cacheFile.exists()) {
                    cacheFile.mkdirs();
                }
                return cacheFile.getAbsolutePath();
            }
        } else {
            return context.getCacheDir().getAbsolutePath();
        }
    }

}
