package com.baihe.lihepro.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ExitAppActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.KeyValueEntity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class Utils {

    /**
     * 关闭app
     *
     * @param context
     */
    public static void exitApp(Context context) {
        ExitAppActivity.start(context);
    }

    /**
     * start task
     *
     * @param context
     */
    public static void startTaskActivity(Context context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 价格格式化
     *
     * @param value
     * @return
     */
    public String formIncom(String value) {
        DecimalFormat format = new DecimalFormat("0.00");
////        String income = format.format(value);
//        double dVlaue = Double.parseDouble(value);
//        if (dVlaue >= 10000) {
//          d  dVlaue/10000;
//        }else if(dVlaue >= 100000000){
//
//        }
        return format.format(value);
    }

    /**
     * 外呼客户
     *
     * @param customerId
     * @param companyId
     * @param baseActivity
     */
    public static void call(String customerId, String companyId, final BaseActivity baseActivity) {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("id", customerId)
                .putParamValue("type", "customer")
                .putParamValue("status", "1")
                .putParamValue("companyId", companyId)
                .putParamValue("platform", "Mobile");
        HttpRequest.create(UrlConstant.ALL_DIAOUT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("外呼成功，请等待接听");
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void before() {
                super.before();
                baseActivity.showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                baseActivity.dismissOptionLoading();
            }
        });
    }

    /**
     * 外呼客户
     *
     * @param customerId
     * @param companyId
     * @param baseFragment
     */
    public static void call(String customerId, String companyId, final BaseFragment baseFragment) {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("id", customerId)
                .putParamValue("type", "customer")
                .putParamValue("status", "1")
                .putParamValue("companyId", companyId)
                .putParamValue("platform", "Mobile");
        HttpRequest.create(UrlConstant.ALL_DIAOUT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("外呼成功，请等待接听");
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void before() {
                super.before();
                baseFragment.showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                baseFragment.dismissOptionLoading();
            }
        });
    }

    /**
     * 外呼联系人
     *
     * @param customerId
     * @param companyId
     * @param contactUserId
     * @param baseActivity
     */
    public static void call(String customerId, String companyId, String contactUserId, final BaseActivity baseActivity) {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("id", customerId)
                .putParamValue("type", "customer")
                .putParamValue("status", "1")
                .putParamValue("companyId", companyId)
                .putParamValue("platform", "Mobile");
        HttpRequest.create(UrlConstant.ALL_DIAOUT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("外呼成功，请等待接听");
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void before() {
                super.before();
                baseActivity.showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                baseActivity.dismissOptionLoading();
            }
        });
    }

    /**
     * 外呼联系人
     *
     * @param customerId
     * @param companyId
     * @param contactUserId
     * @param baseFragment
     */
    public static void call(String customerId, String companyId, String contactUserId, final BaseFragment baseFragment) {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("id", customerId)
                .putParamValue("type", "customer")
                .putParamValue("status", "1")
                .putParamValue("companyId", companyId)
                .putParamValue("platform", "Mobile");
        HttpRequest.create(UrlConstant.ALL_DIAOUT_URL).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String response) {
                ToastUtils.toast("外呼成功，请等待接听");
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void before() {
                super.before();
                baseFragment.showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                baseFragment.dismissOptionLoading();
            }
        });
    }

    public static void unLockMobile(final View parentView, final TextView mobile, final TextView unlock, final ImageView lockIcon, final KeyValueEntity keyValueEntity, String customerId, final BaseFragment baseFragment) {
        if (keyValueEntity.getEvent().isUnlock()) {
            mobile.setText(keyValueEntity.getVal());
            lockIcon.setImageResource(R.drawable.unlock_mobile);
            keyValueEntity.getEvent().setUnlock(false);
        } else {
            HttpRequest.create(UrlConstant.CUSTOMER_IMPORTANT_INFO_URL).putParam(JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("type", "1")).get(new CallBack<String>() {
                @Override
                public String doInBackground(String response) {
                    return response;
                }

                @Override
                public void success(String response) {
                    mobile.setText(response);
                    lockIcon.setImageResource(R.drawable.lock_mobile);
                    keyValueEntity.getEvent().setUnlock(true);

                    int count = keyValueEntity.getEvent().getPhoneNum() - 1;
                    keyValueEntity.getEvent().setPhoneNum(count);
                    if (count > 0) {
                        unlock.setText("可看" + count + "次");
                    } else {
                        parentView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void error() {
                    ToastUtils.toastNetError();
                }

                @Override
                public void fail() {
                    ToastUtils.toastNetWorkFail();
                }

                @Override
                public void before() {
                    super.before();
                    baseFragment.showOptionLoading();
                }

                @Override
                public void after() {
                    super.after();
                    baseFragment.dismissOptionLoading();
                }
            });
        }
    }
    public static void unLockMobile(final View parentView, final TextView mobile, final TextView unlock, final ImageView lockIcon, final KeyValueEntity keyValueEntity, String customerId, final BaseActivity baseFragment) {
        if (keyValueEntity.getEvent().isUnlock()) {
            mobile.setText(keyValueEntity.getVal());
            lockIcon.setImageResource(R.drawable.unlock_mobile);
            keyValueEntity.getEvent().setUnlock(false);
        } else {
            HttpRequest.create(UrlConstant.CUSTOMER_IMPORTANT_INFO_URL).putParam(JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("type", "1")).get(new CallBack<String>() {
                @Override
                public String doInBackground(String response) {
                    return response;
                }

                @Override
                public void success(String response) {
                    mobile.setText(response);
                    lockIcon.setImageResource(R.drawable.lock_mobile);
                    keyValueEntity.getEvent().setUnlock(true);

                    int count = keyValueEntity.getEvent().getPhoneNum() - 1;
                    keyValueEntity.getEvent().setPhoneNum(count);
                    if (count > 0) {
                        unlock.setText("可看" + count + "次");
                    } else {
                        parentView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void error() {
                    ToastUtils.toastNetError();
                }

                @Override
                public void fail() {
                    ToastUtils.toastNetWorkFail();
                }

                @Override
                public void before() {
                    super.before();
                    baseFragment.showOptionLoading();
                }

                @Override
                public void after() {
                    super.after();
                    baseFragment.dismissOptionLoading();
                }
            });
        }
    }


    public static void unLockWechat(final View parentView, final TextView mobile, final TextView unlock, final ImageView lockIcon, final KeyValueEntity keyValueEntity, String customerId, final BaseFragment baseFragment) {
        if (keyValueEntity.getEvent().isUnlock()) {
            mobile.setText(keyValueEntity.getVal());
            lockIcon.setImageResource(R.drawable.unlock_mobile);
            keyValueEntity.getEvent().setUnlock(false);
        } else {
            HttpRequest.create(UrlConstant.CUSTOMER_IMPORTANT_INFO_URL).putParam(JsonParam.newInstance("params").putParamValue("customerId", customerId).putParamValue("type", "2")).get(new CallBack<String>() {
                @Override
                public String doInBackground(String response) {
                    return response;
                }

                @Override
                public void success(String response) {
                    mobile.setText(response);
                    lockIcon.setImageResource(R.drawable.lock_mobile);
                    keyValueEntity.getEvent().setUnlock(true);

                    int count = keyValueEntity.getEvent().getWechatNum() - 1;
                    keyValueEntity.getEvent().setWechatNum(count);
                    if (count > 0) {
                        unlock.setText("可看" + count + "次");
                    } else {
                        parentView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void error() {
                    ToastUtils.toastNetError();
                }

                @Override
                public void fail() {
                    ToastUtils.toastNetWorkFail();
                }

                @Override
                public void before() {
                    super.before();
                    baseFragment.showOptionLoading();
                }

                @Override
                public void after() {
                    super.after();
                    baseFragment.dismissOptionLoading();
                }
            });
        }
    }


    public interface BottomCallback {
        void click(int type);
    }

    /**
     * 显示底部按钮
     *
     * @param parent
     * @param button1
     * @param button2
     * @param list
     * @param callback
     */
    public static void bottomButtons(ViewGroup parent, TextView button1, TextView button2, final List<ButtonTypeEntity> list, final BottomCallback callback) {
        if (list == null || list.size() == 0) {
            parent.setVisibility(View.GONE);
        } else if (list.size() == 1) {
            parent.setVisibility(View.VISIBLE);
            button1.setVisibility(View.GONE);
            button2.setText(list.get(0).getName());
            if ("1".equals(list.get(0).getDisabled())) {
                button2.setBackgroundResource(R.drawable.button_round_disable);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(0).getType() == 105) {
                            ToastUtils.toast("当前客户合同审批通过后才可创建协议");
                        } else if (list.get(0).getType() == 106) {
                            ToastUtils.toast("客户有效进店后才可签单");
                        }
                    }
                });
            } else {
                button2.setBackgroundResource(R.drawable.button_round_blue2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.click(list.get(0).getType());
                        }
                    }
                });
            }
        } else {
            parent.setVisibility(View.VISIBLE);
            button1.setText(list.get(0).getName());
            if ("1".equals(list.get(0).getDisabled())) {
                button1.setBackgroundResource(R.drawable.button_round_disable);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(0).getType() == 105) {
                            ToastUtils.toast("当前客户合同审批通过后才可创建协议");
                        } else if (list.get(0).getType() == 106) {
                            ToastUtils.toast("客户有效进店后才可签单");
                        }
                    }
                });
            } else {
                button1.setBackgroundResource(R.drawable.button_round_white);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.click(list.get(0).getType());
                        }
                    }
                });
            }
            button2.setText(list.get(1).getName());
            if ("1".equals(list.get(1).getDisabled())) {
                button2.setBackgroundResource(R.drawable.button_round_disable);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(1).getType() == 105) {
                            ToastUtils.toast("当前客户合同审批通过后才可创建协议");
                        } else if (list.get(1).getType() == 106) {
                            ToastUtils.toast("客户有效进店后才可签单");
                        }
                    }
                });
            } else {
                button2.setBackgroundResource(R.drawable.button_round_blue2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.click(list.get(1).getType());
                        }
                    }
                });
            }
        }
    }

    public static String formatFloat(double value, int digits) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(digits);
        format.setGroupingSize(0);
        format.setRoundingMode(RoundingMode.FLOOR);
        return format.format(value);
    }

    /**
     * 格式化价格
     *
     * @param price
     * @return
     */
    public static String[] formatPrice(String price) {
        try {
            double value;
            String unit;
            double priceValue = Double.parseDouble(price);
            if (priceValue >= 100000000D) {
                value = priceValue / 100000000D;
                unit = "亿元";
            } else if (priceValue >= 10000D) {
                value = priceValue / 10000D;
                unit = "万元";
            } else {
                value = priceValue;
                unit = "元";
            }
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(2);
            format.setGroupingSize(0);
            format.setRoundingMode(RoundingMode.FLOOR);

            String priceStr = format.format(value);
            return new String[]{priceStr, unit};
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new String[]{price, "元"};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"0", "元"};
        }
    }

    public static String formatNumText(int num) {
        if (num >= 0) {
            Map<Integer, String> map = new HashMap<>();
            map.put(0, "零");
            map.put(1, "一");
            map.put(2, "二");
            map.put(3, "三");
            map.put(4, "四");
            map.put(5, "五");
            map.put(6, "六");
            map.put(7, "七");
            map.put(8, "八");
            map.put(9, "九");
            map.put(10, "十");

            StringBuffer buffer = new StringBuffer();
            //亿
            int billion = num / 100000000;
            if (billion >= 10) {
                int billionParent = billion / 10;
                buffer.append(map.get(billionParent) + "十");
                int billionChild = billion % 10;
                if (billionChild > 0) {
                    buffer.append(map.get(billionChild) + "亿");
                }
                buffer.append("亿");
            } else if (billion > 0) {
                buffer.append(map.get(billion) + "亿");
            }
            //万
            num = num % 100000000; //去掉亿位以上

            StringBuffer wanBuffer = new StringBuffer();
            int wanNum = num / 10000;
            if (wanNum >= 1000) {
                wanBuffer.append(map.get(wanNum / 1000) + "千");
            }
            wanNum = wanNum % 1000;
            if (wanNum >= 100) {
                wanBuffer.append(map.get(wanNum / 100) + "百");
            }
            wanNum = wanNum % 100;
            if (wanNum >= 10) {
                wanBuffer.append(map.get(wanNum / 10) + "十");
                wanNum = wanNum % 10;
                if (wanNum > 0) {
                    wanBuffer.append(map.get(wanNum));
                }
            } else {
                wanNum = wanNum % 10;
                if (wanNum > 0) {
                    wanBuffer.append("零" + map.get(wanNum));
                }
            }
            if (wanBuffer.length() > 0) {
                buffer.append(wanBuffer.toString() + "万");
            }
            //千
            num = num % 10000;  //去掉万位以上
            if (num >= 1000) {
                buffer.append(map.get(wanNum / 1000) + "千");
            }

            num = num % 1000; //去掉千位以上
            if (num >= 100) {
                buffer.append(map.get(wanNum / 100) + "百");
            }
            num = num % 100;  //去掉百位以上
            if (num >= 10) {
                if (num / 10 == 1 && buffer.toString().length() == 0) {
                    buffer.append("十");
                } else {
                    buffer.append(map.get(num / 10) + "十");
                }
                num = num % 10; //去掉十位以上
                if (num > 0) {
                    buffer.append(map.get(num));
                }
            } else {
                if (num > 0) {
                    if (buffer.toString().length() > 0) {
                        buffer.append("零" + map.get(num % 10));
                    } else {
                        buffer.append(map.get(num % 10));
                    }
                }
            }
            return buffer.toString();
        }
        return "";
    }
}
