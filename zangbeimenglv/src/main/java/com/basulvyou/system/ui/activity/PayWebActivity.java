package com.basulvyou.system.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.PayApi;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.Constants;
import com.basulvyou.system.util.StringUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 网页支付界面
 */
public class PayWebActivity extends BaseActivity {

    private WebView pay_web;
    private String orderSn;
    private String payType;
    /** 微信支付参数 */
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_web);
        // 微信支付注册app_id
        msgApi.registerApp(Constants.APP_ID);
        orderSn = getIntent().getStringExtra("order_sn");
        payType = getIntent().getStringExtra("pay_type");
        initView();
        initListener();
        setData();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("立即支付");
        pay_web = (WebView) findViewById(R.id.pay_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            pay_web.setBackgroundColor(0x00000000);
        } else {
            pay_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    /**
     * 设置tab数据
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setData(){
        WebSettings webSettings = pay_web.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        pay_web.loadUrl(ConfigUtil.HTTP_PAY_WEB + "&trade_index=" + orderSn + "&key=" + configEntity.key + "&pay_type=" + payType);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        pay_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url != null && url.contains("/m/MMyHome.do")) {
					/*Intent orderIntent = new Intent(PayWebActivity.this,MyOrderActivity.class);
					startActivity(orderIntent);*/
                    PayWebActivity.this.finish();
                } else if (url != null && (url.contains("weixinpay:"))) {// 截取微信支付
                    String info = (String) url.substring(url.indexOf(":") + 1);
                    isWeixinSupported(msgApi, info);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url != null && url.contains("https://mclient.alipay.com/")) {
                    setBackShow(false);
                } else {
                    setBackShow(true);
                }
                super.onPageFinished(view, url);
            }
        });
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
    }

    @Override
    public void httpResponse(String json, int action) {
        switch (action) {
            case PayApi.API_GET_WEIXIN_PAY_INFO:
                handlerWeiXin(json);
                break;
        }
    }

    private void handlerWeiXin(String json) {
        if (!StringUtil.isEmpty(json)) {
            // 调用微信支付
            PayReq req = new PayReq();
            req.appId = JSON.parseObject(json).getString("appid");
            req.nonceStr = JSON.parseObject(json).getString("noncestr");
            req.packageValue = JSON.parseObject(json).getString("package");
            req.partnerId = JSON.parseObject(json).getString("partnerid");
            req.prepayId = JSON.parseObject(json).getString("prepayid");
            req.sign = JSON.parseObject(json).getString("sign");
            req.timeStamp = JSON.parseObject(json).getString("timestamp");
            msgApi.sendReq(req);
            PayWebActivity.this.finish();
        } else {
            Toast.makeText(this, "在线支付失败", Toast.LENGTH_SHORT).show();
        }
    }
}
