package com.shenmai.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.BuyApi;
import com.shenmai.system.entity.Pay;
import com.shenmai.system.utlis.Arith;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.Constants;
import com.shenmai.system.utlis.TopClickUtil;
import com.shenmai.system.utlis.WeiXinPayUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 我的订单
 */
public class OrderWebActivity extends BaseActivity implements View.OnClickListener{

    /**微信支付参数 */
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Pay payInfo;
    private WebView orderWeb;
    private LinearLayout linearWeb;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_web);
    }

    @Override
    protected void onStart() {
        super.onStart();
        msgApi.registerApp(Constants.APP_ID);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("我的订单");
        setTopRightImg(R.mipmap.web_close, TopClickUtil.TOP_WEB_CLOSE);
        linearWeb = (LinearLayout) findViewById(R.id.order_webview);
        isShowTopRightImg(false);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderWeb = new WebView(getApplicationContext());
        orderWeb.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        orderWeb.setVerticalScrollBarEnabled(false);
        linearWeb.addView(orderWeb);
        setData();
    }

    @Override
    public void initListener() {
        super.initListener();
        btn_top_goback.setOnClickListener(this);
        initTopRightImgListener();
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            orderWeb.setBackgroundColor(0x00000000);
        } else {
            orderWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(orderWeb.getSettings());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        orderWeb.loadUrl(ConfigUtil.HTTP_GOODS_ORDER);
        orderWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if ((url != null && url.equals(ConfigUtil.HTTP_GOODS_ORDER))) {
                    isShowTopRightImg(false);
                } else {
                    isShowTopRightImg(true);
                }
                if ((url != null && url.contains("https://mclient.alipay.com/"))) {
                    setLeftBackShow(false);
                } else {
                    setLeftBackShow(true);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("weixinpay:")) {
                    String order[] = url.split(":");
                    if (null != order[1] && !"".endsWith(order[1])) {
                        getOrderInfo(order[1]);
                    }
                    return true;
                }
                if (url != null && url.contains("/service/AppMyOrderInfo.do")) {
                    view.loadUrl(ConfigUtil.HTTP_GOODS_ORDER);
                } else if (url != null && url.contains("/m/MMyHome.do")) {
                    view.loadUrl(ConfigUtil.HTTP_GOODS_ORDER);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(orderWeb != null){
                    if (isError) {
                        netError.setVisibility(View.VISIBLE);
                        orderWeb.setVisibility(View.GONE);
                    } else {
                        netError.setVisibility(View.GONE);
                        orderWeb.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getOrderInfo(String orderId){
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
            int maxMoney = (int) (Arith.mul(Double.parseDouble(payInfo.order_money), 100));
            if( maxMoney > 10000000){//微信支付不能超过十万
                Toast.makeText(this, "订单金额超限", Toast.LENGTH_SHORT).show();
                orderWeb.loadUrl(ConfigUtil.HTTP_GOODS_ORDER);
            } else {
                WeiXinPayUtil weixin = new WeiXinPayUtil(this, msgApi, payInfo.order_name, payInfo.out_trade_no, payInfo.order_money);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_top_goback:
                if (orderWeb.canGoBack() && !(ConfigUtil.HTTP_GOODS_ORDER).equals(orderWeb.getUrl())) {  //表示按返回键时的操作
                    orderWeb.goBack();
                }else{
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgApi.unregisterApp();
        linearWeb.removeView(orderWeb);
        orderWeb.removeAllViews();
        orderWeb.destroy();
        System.gc();
    }
}
