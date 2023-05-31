package com.baihe.imageloader.glide;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.baihe.imageloader.BaseImageLoaderStrategy;
import com.baihe.imageloader.ImageLoaderConfig;
import com.baihe.lib_imageloader.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Glide的策略实现类
 */
public class GlideLoaderStrategy implements BaseImageLoaderStrategy {

    private static final String TAG = GlideLoaderStrategy.class.getSimpleName();
    private static int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static int MAX_MEMORY_CACHE = 1024 * 1024 * 10;

    private ImageLoaderConfig mImageLoaderConfig;

    private RequestOptions mOptions;
    private RequestOptions mOptionsCircle;

    private RequestOptions mOptionsCustom;

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 10; //圆角
        public static final int MARGIN = 5;  //边距
        public static final float THUMB_SIZE = 0.5f; //0-1之间  10%原图的大小
    }

    /**
     * 初始化默认配置
     *
     * @param config ImageLoaderConfig
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptions(ImageLoaderConfig config) {
        if (mOptions == null) {
            mOptions = new RequestOptions();
            mOptions.error(config.getErrorPicRes())
                    .placeholder(config.getPlacePicRes())
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        return mOptions;
    }


    /**
     * 初始化自定义配置
     * @param errorRes 加载失败图片
     * @param placeRes 占位图
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptionsCustom(int errorRes,int placeRes) {
        if (mOptionsCustom == null) {
            mOptionsCustom = new RequestOptions();
        }
        mOptionsCustom.error(errorRes)
                .placeholder(placeRes)
                //下载的优先级
                .priority(Priority.NORMAL)
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return mOptionsCustom;
    }

    /**
     * 初始化加载圆形图片的配置
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptionsCircle() {
        if (mOptionsCircle == null) {
            mOptionsCircle = new RequestOptions();
            mOptionsCircle.placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        return mOptionsCircle;
    }

    /**
     * 初始化加载Gif的配置
     *
     * @param config ImageLoaderConfig
     */
    @SuppressLint("CheckResult")
    private RequestOptions getGifOptions(ImageLoaderConfig config) {
        if (mOptions == null) {
            mOptions = new RequestOptions();
            mOptions.error(config.getErrorPicRes())
                    .placeholder(config.getPlacePicRes())
                    //下载的优先级
                    .priority(Priority.NORMAL)
                    //缓存策略
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        return mOptions;
    }

    @Override
    public void setLoaderConfig(ImageLoaderConfig config) {
        mImageLoaderConfig = config;
    }

    @Override
    public void loadImage(Context context, ImageView view, Object imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(getOptions(mImageLoaderConfig))
                //先加载缩略图 然后在加载全图
                .thumbnail(Contants.THUMB_SIZE)
                .into(view);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, Object imgUrl, int errorRes, int placeRes) {
        Glide.with(context)
                .load(imgUrl)
                .apply(getOptionsCustom(errorRes,placeRes))
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    @Override
    public void displayFromDrawable(Context context, int imageId, ImageView imageView) {
        Glide.with(context)
                .load(imageId)
                .thumbnail(Contants.THUMB_SIZE)
                .apply(getOptions(mImageLoaderConfig))
                .into(imageView);
    }

    @Override
    public void displayFromSDCard(Context context,String uri, ImageView imageView) {
        Glide.with(context)
                .load(Uri.fromFile(new File(uri)))
                .thumbnail(Contants.THUMB_SIZE)
                .apply(getOptions(mImageLoaderConfig))
                .into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(getOptionsCircle())
                .apply(bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, ImageView imageView, Object imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(getGifOptions(mImageLoaderConfig))
                .into(imageView);
    }

    @Override
    public void loadCornerImage(Context context, ImageView imageView, String imgUrl, int radius) {
        Glide.with(context)
                .load(imgUrl)
                .thumbnail(Contants.THUMB_SIZE)
                .apply(getOptions(mImageLoaderConfig))
                .apply(bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

    @Override
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

}
