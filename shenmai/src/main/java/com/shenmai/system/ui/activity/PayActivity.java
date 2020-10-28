package com.shenmai.system.ui.activity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.BuyApi;
import com.shenmai.system.entity.Pay;
import com.shenmai.system.utlis.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 调用本地客户端支付
 */
public class PayActivity extends BaseActivity {

    /**微信支付参数 */
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Pay payInfo;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        msgApi.registerApp(Constants.APP_ID);
        orderId = getIntent().getStringExtra("id");
    }

    private void getOrderInfo(){
        httpGetRequest(BuyApi.getWeiXinPayUrl(orderId), BuyApi.API_GET_WEIXIN_PAY);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case BuyApi.API_GET_WEIXIN_PAY:
                weixinHander(json);
                break;
            default:
                break;
        }
    }

    private void weixinHander(String json){
        payInfo = JSON.parseObject(json, Pay.class);
        if(null != payInfo){

        }
    }

    private void weixinPay(){
        /*WeiXinPayUtil weixin = new WeiXinPayUtil(this, msgApi, subject, tradeOutId, price);*/
    }

}
