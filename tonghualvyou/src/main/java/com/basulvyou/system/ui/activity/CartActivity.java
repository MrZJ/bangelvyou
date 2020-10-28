package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.PayApi;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 购物车界面
 */
public class CartActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private WebView cart_web;
    /** 微信支付参数 */
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        // 微信支付注册app_id
        msgApi.registerApp(Constants.APP_ID);
        initView();
        initListener();
    }

    public void onResume(){
        super.onResume();
        if(configEntity.isLogin){
            setData();
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("购物车");
        //初始完成界面
        cart_web = (WebView) findViewById(R.id.cart_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            cart_web.setBackgroundColor(0x00000000);
        } else {
            cart_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        btn_top_goback.setOnClickListener(this);
        cart_web.setOnKeyListener(this);
    }

    private void setData(){
        WebSettings webSettings = cart_web.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        cart_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url != null && url.endsWith("/m/index.shtml")) {
                    MainActivity.index = 1;
                    CartActivity.this.finish();
                } else if(url != null && url.contains("/service/AppMyOrderInfo.do")){
                    Intent orderIntent = new Intent(CartActivity.this,OrderListActivity.class);
                    startActivity(orderIntent);
                    CartActivity.this.finish();
                } else if(url != null && url.contains("/m/MMyHome.do")){
                    Intent orderIntent = new Intent(CartActivity.this,OrderListActivity.class);
                    startActivity(orderIntent);
                    CartActivity.this.finish();
                } else if(url != null && url.contains("/m/MEntpInfo.do?id=")){
                    String[] goodId=url.split("=");
                    Intent goodsIntent = new Intent(CartActivity.this,GoodsDetailActivity.class);
                    if (goodId != null && goodId[1] != null) {
                        goodsIntent.putExtra("goods_id", goodId[1]);
                        startActivity(goodsIntent);
                    }
                } else if (url != null && (url.contains("weixinpay:"))) {// 截取微信支付
                    String info = url.substring(url.indexOf(":") + 1);
                    isWeixinSupported(msgApi, info);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url != null && url.contains("https://mclient.alipay.com/")) {
                    setBackShow(false);
                    setTitle("在线支付");
                } else {
                    setBackShow(true);
                }
//                showLoadingDialog();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                //结束
//                disMissDialog();
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_top_goback://返回
                if (cart_web.canGoBack() && cart_web.getUrl().contains("https://mcashier.95516.com/")) {
                    Intent orderIntent = new Intent(CartActivity.this, OrderListActivity.class);
                    startActivity(orderIntent);
                    CartActivity.this.finish();
                } else if (cart_web.canGoBack() && (ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {  //表示按返回键时的操作
                    CartActivity.this.finish();
                } else if (cart_web.canGoBack() && cart_web.getUrl().contains("/service/AppMyCartInfo.do")) {
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                } else if (cart_web.canGoBack() && !(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {  //表示按返回键时的操作
                    cart_web.goBack();
                } else {
                    CartActivity.this.finish();
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && cart_web.canGoBack()) {
                if ((ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {
                    CartActivity.this.finish();
                    return true;
                } else if (cart_web.getUrl().contains("https://mclient.alipay.com/")) {
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if (cart_web.getUrl().contains("https://mcashier.95516.com/")) {
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if (cart_web.getUrl().contains("/service/AppMyCartInfo.do")) {
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if (!(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {
                    //表示按返回键时的操作
                    cart_web.goBack();   //后退
                    return true;
                }
            }
            CartActivity.this.finish();
        }
        return false;
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
        if (null != json && !"".equals(json)) {
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
        } else {
            Toast.makeText(this, "在线支付失败", Toast.LENGTH_SHORT).show();
        }
    }

}
