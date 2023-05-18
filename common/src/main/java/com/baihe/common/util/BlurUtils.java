package com.baihe.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

public class BlurUtils {

    public static Bitmap blur(Activity activity){
        Bitmap bitmap = null;
        try {
            View decorView = activity.findViewById(android.R.id.content);
            decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            Bitmap image = decorView.getDrawingCache();
            bitmap = ImageUtils.fastBlur(image,0.1f, 15);
            decorView.destroyDrawingCache();
            decorView.setDrawingCacheEnabled(false);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
