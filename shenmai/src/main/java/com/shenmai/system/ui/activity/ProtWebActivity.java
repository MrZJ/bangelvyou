package com.shenmai.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenmai.system.R;
import com.shenmai.system.utlis.ConfigUtil;

/**
 * 神买电商协议界面
 */
public class ProtWebActivity extends BaseActivity {

    private WebView protWeb;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prot_web);
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle("神买商城注册协议");
        setLeftBackButton();
        protWeb = (WebView) findViewById(R.id.prot_webview);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            protWeb.setBackgroundColor(0x00000000);
        } else {
            protWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = protWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        protWeb.loadUrl(ConfigUtil.HTTP_GOODS_SHOP_PROT);
        protWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
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
                    protWeb.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    protWeb.setVisibility(View.VISIBLE);
                }
            }

        });
    }

}
