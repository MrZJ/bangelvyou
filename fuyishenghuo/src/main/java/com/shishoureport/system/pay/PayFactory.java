package com.shishoureport.system.pay;

import android.content.Context;

/**
 * Created by jianzhang.
 * on 2017/12/5.
 * copyright easybiz.
 */

public class PayFactory {
    public static final int PAY_TYPE_WX = 0;
    public static final int PAY_TYPE_ALI = 1;

    public static PayInterface getPay(Context context, int payType) {
        if (payType == PAY_TYPE_WX) {
//            return WeiXinPay.getInstance(context);
            return null;
        } else if (payType == PAY_TYPE_ALI) {
            return AliPay.getInstance(context);
        } else {
            return null;
        }
    }
}
