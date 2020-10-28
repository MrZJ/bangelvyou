package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shenmai.system.R;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.TopClickUtil;

/**
 * 店铺预览界面
 */
public class ShopSeeWebActivity extends BaseActivity implements View.OnClickListener{

    private WebView shopWeb;
    private LinearLayout linearWeb;
    private ImageView back, share;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_see_web);
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle("店铺预览");
        setLeftBackButton();
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHARE);
        /*back = (ImageView) findViewById(R.id.img_shop_web_back);
        share = (ImageView) findViewById(R.id.img_shop_web_share);*/
        linearWeb = (LinearLayout) findViewById(R.id.shop_webview);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        /*back.setOnClickListener(this);
        share.setOnClickListener(this);*/
        rightOne.setOnClickListener(this);
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
        shopWeb.loadUrl(ConfigUtil.HTTP_SHOP_SEE + configEntity.key);
        shopWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("MEntpInfo.do?id=")) {
                    String id = url.substring(url.indexOf("id=") + 3);
                    Intent intent = new Intent(ShopSeeWebActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    return true;
                }
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
                if (isError) {
                    netError.setVisibility(View.VISIBLE);
                    shopWeb.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    shopWeb.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_top_goback:
                finish();
                break;
            case R.id.img_top_right_one:
                shareAction(configEntity.username + "的小店", ConfigUtil.HTTP_SHOP_SEE + configEntity.key);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearWeb.removeView(shopWeb);
        shopWeb.removeAllViews();
        shopWeb.destroy();
        System.gc();
    }
}
