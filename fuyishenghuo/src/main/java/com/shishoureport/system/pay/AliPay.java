package com.shishoureport.system.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

/**
 * Created by jianzhang.
 * on 2017/12/5.
 * copyright easybiz.
 */

public class AliPay extends PayInterface {
    private Context mContext;
    private Handler mHandler;

    public static AliPay getInstance(Context context) {
        return new AliPay(context);
    }

    public AliPay(Context context) {
        mContext = context;
        mHandler = new Handler(context.getMainLooper());
    }


    @Override
    public void onPay(Object o) {
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = (String) o;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                final String result = alipay.pay(payInfo, true);
                Log.e("alipay.pay", result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (payCallBack != null) {
                            payCallBack.onPayResult(result);
                        }
                    }
                });
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
