package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.BitmapUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.FontTextView;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.CrmAlertDialog;
import com.baihe.lihepro.entity.PayCodeCreateEntity;
import com.github.xubo.statusbarutils.StatusBarUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class PayCodeDetailActivity extends BaseActivity {

    private ImageView destroyBtn;
    private Toolbar toolbar;
    private FontTextView moneyTv;
    private FontTextView moneyTv1;
    private ImageView codeIv;
    private ImageView codeIv1;
    private FontTextView nameTv;
    private FontTextView nameTv1;
    private TextView dateTv;
    private TextView dateTv1;
    private TextView hotelTv;
    private TextView hotelTv1;
    private TextView orderIdTv;
    private TextView orderIdTv1;
    private TextView shareBtn;
    private TextView saveBtn;
    private ImageView destroyIv;
    private LinearLayout bottomLayout;
    private ImageView logoIv;
    private LinearLayout picView;

    private PayCodeCreateEntity data;

    private String receivablesId;

    private String receivables_id;

    private CountDownTimer timer;

    private List<HttpRequest> requests;

    private int flag;

    public static void start(Context context,PayCodeCreateEntity data){
        Intent intent = new Intent(context, PayCodeDetailActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }

    public static void start(Activity context, PayCodeCreateEntity data, int flag){
        Intent intent = new Intent(context, PayCodeDetailActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("flag",flag);
        context.startActivityForResult(intent,1001);
    }

    public static void start(Context context,String receivablesId){
        Intent intent = new Intent(context, PayCodeDetailActivity.class);
        intent.putExtra("receivablesId",receivablesId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_pay_code_detail_title);
        setContentView(R.layout.activity_pay_code_detail);
        initData();
        initView();
        listener();
        requests = new LinkedList<>();
        timer = new CountDownTimer(60*60*1000,1000) {
            @Override
            public void onTick(long l) {
                queryPayResult();
            }

            @Override
            public void onFinish() {

            }
        };

        if (flag == 1){
            timer.start();
        }

        if (!TextUtils.isEmpty(receivablesId)){
            loadData();
        }
        if (data!=null){
            fillData();
        }

    }

    private void initData(){
        receivablesId = getIntent().getStringExtra("receivablesId");
        flag = getIntent().getIntExtra("flag",0);
        data = (PayCodeCreateEntity) getIntent().getSerializableExtra("data");
    }

    private void initView() {
        picView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_pay_code_pic,null);
        destroyBtn = findViewById(R.id.btn_destroy);
        toolbar = findViewById(R.id.tb);
        moneyTv = findViewById(R.id.tv_money);
        moneyTv1 = picView.findViewById(R.id.tv_money);
        codeIv = findViewById(R.id.iv_code);
        codeIv1 = picView.findViewById(R.id.iv_code);
        nameTv = findViewById(R.id.tv_name);
        nameTv1 = picView.findViewById(R.id.tv_name);
        dateTv = findViewById(R.id.tv_date);
        dateTv1 = picView.findViewById(R.id.tv_date);
        hotelTv = findViewById(R.id.tv_hotel);
        hotelTv1 = picView.findViewById(R.id.tv_hotel);
        orderIdTv = findViewById(R.id.tv_order_id);
        orderIdTv1 = picView.findViewById(R.id.tv_order_id);
        logoIv = picView.findViewById(R.id.iv_logo);
        shareBtn = findViewById(R.id.tv_share);
        saveBtn = findViewById(R.id.tv_save);
        destroyIv = findViewById(R.id.iv_destroy);
        bottomLayout = findViewById(R.id.ll_bottom);
        StatusBarUtils.setStatusBarColorLight(this, Color.parseColor("#2DB4E6"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
           timer.cancel();
        }
        if (requests!=null){
            for (HttpRequest request : requests) {
                if (request != null) {
                    request.cancel();
                }
            }
        }
    }

    private void listener(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        destroyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new CrmAlertDialog.Builder(context)
                        .setTitle("提示")
                        .setContent("操作作废码后，该付款码不再生效，请确认是否执行此操作？")
                        .setConfirmListener(new CrmAlertDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                dialog.dismiss();
                                if (!TextUtils.isEmpty(receivablesId)) {
                                    destroyCode();
                                }
                            }
                        }).setCancelListener(new CrmAlertDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();



            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data != null && !TextUtils.isEmpty(data.getShort_url())) {
                    AndPermission.with(PayCodeDetailActivity.this)
                            .runtime()
                            .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    downloadImage();
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    ToastUtils.toast("请打开读取权限");
                                }
                            })
                            .start();

                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data!=null){

                    AndPermission.with(PayCodeDetailActivity.this)
                            .runtime()
                            .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    shareToWeChat();
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> data) {
                                    ToastUtils.toast("请打开读取权限");
                                }
                            })
                            .start();
                }
            }
        });
    }

    private void loadData(){
        JsonParam jsonParam = JsonParam.newInstance("params");
        jsonParam.putParamValue("receivablesId", receivablesId);
        HttpRequest.create(UrlConstant.PAY_CODE_GET_PAY_QR_CODE)
                .tag("查看付款码")
                .putParam(jsonParam)
                .get(new CallBack<PayCodeCreateEntity>() {
                    @Override
                    public PayCodeCreateEntity doInBackground(String response) {
                        return JsonUtils.parse(response, PayCodeCreateEntity.class);
                    }

                    @Override
                    public void success(PayCodeCreateEntity entity) {
                        data = entity;
                        fillData();
                    }

                    @Override
                    public void error() {

                    }

                    @Override
                    public void fail() {

                    }

                    @Override
                    public void before() {
                        super.before();
                        showOptionLoading();
                    }

                    @Override
                    public void after() {
                        super.after();
                        dismissOptionLoading();
                    }
                });
    }

    private void queryPayResult(){
        JsonParam jsonParam = JsonParam.newInstance("params");
        jsonParam.putParamValue("id",receivables_id);
        HttpRequest request = HttpRequest.create(UrlConstant.SCHEDULE_CHECK_PAY)
                .putParam(jsonParam)
                .get(new CallBack<String>() {
                    @Override
                    public String doInBackground(String response) {
                        return response;
                    }

                    @Override
                    public void success(String o) {
                        if ("1".equals(o)){
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void error() {

                    }

                    @Override
                    public void fail() {

                    }
                });
        requests.add(request);

    }

    private void destroyCode() {
        JsonParam jsonParam = JsonParam.newInstance("params");
        jsonParam.putParamValue("receivablesId", receivablesId);
        HttpRequest.create(UrlConstant.PAY_CODE_CANCEL_QR_CODE)
                .tag("付款码作废")
                .putParam(jsonParam)
                .get(new CallBack<String>() {
                    @Override
                    public String doInBackground(String response) {
                        return response;
                    }

                    @Override
                    public void success(String entity) {
                        data.setIs_finsh("3");
                        fillData();
                    }

                    @Override
                    public void error() {

                    }

                    @Override
                    public void fail() {

                    }

                    @Override
                    public void before() {
                        super.before();
                        showOptionLoading();
                    }

                    @Override
                    public void after() {
                        super.after();
                        dismissOptionLoading();
                    }
                });
    }

    private void downloadImage(){
        File appDir = new File(Environment.getExternalStorageDirectory(), "礼合Crm");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir,fileName);
        Bitmap bitmap = BitmapUtils.captureView(picView);
        BitmapUtils.saveBitmap(bitmap,appDir.getAbsolutePath(),fileName,100,false);
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);
        ToastUtils.toast("付款码保存成功，请到本机图库查看");
    }

    private void shareToWeChat(){
        String title = TextUtils.isEmpty(data.getCompany_name())?"等待支付":data.getCompany_name();
        String desc = TextUtils.isEmpty(data.getCategory_name())?"您本次待支付订单已生成，请查收！":data.getCategory_name();
        String imageUrl = TextUtils.isEmpty(data.getIcon_url())?"":data.getIcon_url();
        String shareUrl = data.getShort_url();
        UMWeb shareContent = new UMWeb(shareUrl);
        shareContent.setTitle(title);
        shareContent.setDescription(desc);
        if (!TextUtils.isEmpty(imageUrl))
            shareContent.setThumb(new UMImage(context,imageUrl));

        new ShareAction(PayCodeDetailActivity.this).withMedia(shareContent)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .share();

    }

    private void fillData(){
        if (data !=null){
            double money = data.getPlan_money();
            moneyTv.setText(money+"");
            moneyTv1.setText(money+"");
            GlideApp.with(this).load(data.getQrCode()).into(codeIv);
            GlideApp.with(this).load(data.getQrCode()).into(codeIv1);
            nameTv.setText(TextUtils.isEmpty(data.getFamily())?"未填写":data.getFamily());
            nameTv1.setText(TextUtils.isEmpty(data.getFamily())?"未填写":data.getFamily());
            dateTv.setText(TextUtils.isEmpty(data.getWedding_date())?"未填写":data.getWedding_date());
            dateTv1.setText(TextUtils.isEmpty(data.getWedding_date())?"未填写":data.getWedding_date());
            hotelTv.setText(TextUtils.isEmpty(data.getHotel())?"未填写":data.getHotel());
            hotelTv1.setText(TextUtils.isEmpty(data.getHotel())?"未填写":data.getHotel());
            Object orderId = data.getOrder_id();
            receivables_id = data.getReceivables_id();
            if (orderId instanceof String){
                orderIdTv.setText(TextUtils.isEmpty((String) orderId)?"待同步":(String)orderId);
                orderIdTv1.setText(TextUtils.isEmpty((String) orderId)?"待同步":(String)orderId);
            }
            if (orderId instanceof Integer){
                orderIdTv.setText(String.valueOf(orderId));
                orderIdTv1.setText(String.valueOf(orderId));
            }

            if (TextUtils.isEmpty(receivablesId) || !"0".equals(data.getIs_finsh()))
                destroyBtn.setVisibility(View.GONE);
            else
                destroyBtn.setVisibility(View.VISIBLE);
            if ("0".equals(data.getIs_finsh())){
                bottomLayout.setVisibility(View.VISIBLE);
            }else {
                bottomLayout.setVisibility(View.GONE);
            }
            if ("3".equals(data.getIs_finsh())){
                destroyIv.setVisibility(View.VISIBLE);
            }else {
                destroyIv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(data.getLogo_url()))
                GlideApp.with(this).load(data.getLogo_url()).into(logoIv);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
