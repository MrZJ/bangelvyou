package com.basulvyou.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.basulvyou.system.R;
import com.basulvyou.system.util.ConfigUtil;

/**
 * 优惠券领取界面
 */
public class PointMallActivity extends BaseActivity {

    private WebView couponWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_mall);
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("优惠券");
        couponWeb = (WebView) findViewById(R.id.coupon_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            couponWeb.setBackgroundColor(0x00000000);
        } else {
            couponWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
        WebSettings webSettings = couponWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        couponWeb.loadUrl(ConfigUtil.HTTP_COUPON_CHANGE + configEntity.key);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        couponWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                super.onPageFinished(view, url);
            }
        });
    }


}
