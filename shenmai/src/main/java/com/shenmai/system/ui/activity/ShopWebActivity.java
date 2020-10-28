package com.shenmai.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.shenmai.system.R;

/**
 * 小店模块需要显示的web
 */
public class ShopWebActivity extends BaseActivity implements View.OnClickListener{

    private WebView shopWeb;
    private LinearLayout linearWeb;
    private String title;
    private String url;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle(title);
        linearWeb = (LinearLayout) findViewById(R.id.shop_webview);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        btn_top_goback.setOnClickListener(this);
    }

    private void setData(){
        shopWeb = new WebView(getApplicationContext());
        shopWeb.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        shopWeb.setVerticalScrollBarEnabled(false);
        linearWeb.addView(shopWeb);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            shopWeb.setBackgroundColor(0x00000000);
        } else {
            shopWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(shopWeb.getSettings());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        shopWeb.loadUrl(url);
        shopWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
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
                if(shopWeb != null){
                    if (isError) {
                        netError.setVisibility(View.VISIBLE);
                        shopWeb.setVisibility(View.GONE);
                    } else {
                        netError.setVisibility(View.GONE);
                        shopWeb.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback:
                if (!title.equals("营收") && shopWeb.canGoBack() && !url.equals(shopWeb.getUrl())) {
                    shopWeb.goBack();
                    return;
                }
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title = null;
        url = null;
        linearWeb.removeView(shopWeb);
        shopWeb.removeAllViews();
        shopWeb.destroy();
        System.gc();
    }
}
