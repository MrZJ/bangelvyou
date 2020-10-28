package com.shishoureport.system.pay;

/**
 * Created by jianzhang.
 * on 2017/12/5.
 * copyright easybiz.
 */

public abstract class PayInterface {
    protected AliPayCallBack payCallBack;
    public interface AliPayCallBack {
        void onPayResult(String result);
    }

    public abstract void onPay(Object o);

    public void setPayCallBack(AliPayCallBack payCallBack) {
        this.payCallBack = payCallBack;
    }
}
