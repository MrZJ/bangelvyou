package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.BuyApi;
import com.shenmai.system.entity.Pay;
import com.shenmai.system.ui.activity.MainActivity;
import com.shenmai.system.ui.activity.OrderWebActivity;
import com.shenmai.system.utlis.Arith;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.Constants;
import com.shenmai.system.utlis.WeiXinPayUtil;

/**
 * 购物车页面
 */
public class CartFragment extends BaseFragment implements View.OnClickListener, View.OnKeyListener {

    private View mView;
    private WebView cart_web;
    private LinearLayout linearWeb;
    private Pay payInfo;
    private boolean isError = false;
    private View netError;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_cart, container, false);
        }
        ((MainActivity)getActivity()).msgApi.registerApp(Constants.APP_ID);
        initView();
        initListener();
        return mView;
    }

    public void onResume(){
        super.onResume();
        if(configEntity.isLogin && !this.isHidden()){
            setData();
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView(mView);
        setTitle("购物车");
        hideBackBtn();
        linearWeb = (LinearLayout) mView.findViewById(R.id.cart_web);
        netError = mView.findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    private void initListener(){
        topLeft.setOnClickListener(this);
//        cart_web.setOnKeyListener(this);
    }

    private void setData(){
        cart_web = new WebView(getActivity());
        cart_web.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        cart_web.setVerticalScrollBarEnabled(false);
        linearWeb.addView(cart_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            cart_web.setBackgroundColor(0x00000000);
        } else {
            cart_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(cart_web.getSettings());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        cart_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url != null && url.startsWith("weixinpay:")){
                    String order[] = url.split(":");
                    if(null != order[1] && !"".endsWith(order[1])){
                        getOrderInfo(order[1]);
                    }
                    return true;
                }
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url != null && url.endsWith("/m/index.shtml")) {
                    if(getActivity() != null){
                        ((MainActivity) getActivity()).setTabSelection(1);
                        ((MainActivity) getActivity()).state = 1;
                    }
                } else if(url != null && url.contains("/service/MMyOrder.do")){
                    if(getActivity() != null){
                        ((MainActivity) getActivity()).setTabSelection(3);
                        ((MainActivity) getActivity()).state = 3;
                        Intent orderIntent = new Intent(getActivity(),OrderWebActivity.class);
                        startActivity(orderIntent);
                    }
                } else if(url != null && url.contains("/m/MMyHome.do")){
                    if(getActivity() != null){
                        ((MainActivity) getActivity()).setTabSelection(3);
                        ((MainActivity) getActivity()).state = 3;
                        Intent orderIntent = new Intent(getActivity(),OrderWebActivity.class);
                        startActivity(orderIntent);
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                if((ConfigUtil.HTTP_CART_URL + configEntity.key).equals(url) ||
                        (url != null && url.contains("https://mclient.alipay.com/"))){
                    hideBackBtn();
                } else {
                    showBackBtn();
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isError) {
                    netError.setVisibility(View.VISIBLE);
                    cart_web.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    cart_web.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(getActivity() == null){
            return;
        }
        switch(v.getId()){
            case R.id.btn_top_goback://返回
                if(cart_web.canGoBack() && cart_web.getUrl().contains("https://mcashier.95516.com/")){
                    if(getActivity() != null){
                        ((MainActivity) getActivity()).setTabSelection(3);
                        ((MainActivity) getActivity()).state = 3;
                        Intent orderIntent = new Intent(getActivity(),OrderWebActivity.class);
                        startActivity(orderIntent);
                    }
                } else if (cart_web.canGoBack() && cart_web.getUrl().contains("/service/MMyCartInfoPt.do")){
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                } else if (cart_web.canGoBack() && !(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {  //表示按返回键时的操作
                    cart_web.goBack();
                }
                break;
        }
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
                Toast.makeText(getActivity(),"订单金额超限,请重新选购",Toast.LENGTH_SHORT).show();
                if(getActivity() != null){
                    ((MainActivity) getActivity()).setTabSelection(1);
                    ((MainActivity) getActivity()).state = 1;
                }
            } else {
                WeiXinPayUtil weixin = new WeiXinPayUtil(getActivity(), ((MainActivity)getActivity()).msgApi, payInfo.order_name, payInfo.out_trade_no, payInfo.order_money);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && cart_web.canGoBack()) {
                if((ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())){
                    return false;
                } else if(cart_web.getUrl().contains("https://mclient.alipay.com/")){
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if(cart_web.getUrl().contains("https://mcashier.95516.com/")){
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if (cart_web.getUrl().contains("/service/AppMyCartInfo.do")){
                    cart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
                    return true;
                } else if (!(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(cart_web.getUrl())) {
                    //表示按返回键时的操作
                    cart_web.goBack();   //后退
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).msgApi.unregisterApp();
        linearWeb.removeView(cart_web);
        cart_web.removeAllViews();
        cart_web.destroy();
        System.gc();
    }
}

