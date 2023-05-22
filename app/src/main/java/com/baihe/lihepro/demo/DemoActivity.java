package com.baihe.lihepro.demo;

import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.ToastUtils;
import com.baihe.imageloader.ImageLoaderUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.MainActivity;



@Route(path = "/demo/activity")
public class DemoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
        initData();
    }

    private void initView(){
        imageView = (ImageView) findViewById(R.id.image_pic);
    }
    private void initData(){
        ImageLoaderUtils.init();
        ImageLoaderUtils.getInstance().loadCircleImage(this,imageView,"https://t7.baidu.com/it/u=376303577,3502948048&fm=193&f=GIF");
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
