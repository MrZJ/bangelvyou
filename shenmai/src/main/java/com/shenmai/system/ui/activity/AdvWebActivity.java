package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.shenmai.system.R;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;

/**
 * 首页广告web显示页面
 */
public class AdvWebActivity extends BaseActivity implements View.OnClickListener{

    private WebView advWeb;
    private ImageView back, share;
    private String title, url, image_path;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        image_path = getIntent().getStringExtra("image_path");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        if(!StringUtil.isEmpty(title)){
            setTitle(title);
        } else {
            setTitle("首页广告");
        }
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHARE);
        /*back = (ImageView) findViewById(R.id.img_adv_web_back);
        share = (ImageView) findViewById(R.id.img_adv_web_share);*/
        advWeb = (WebView) findViewById(R.id.adv_webview);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        rightOne.setOnClickListener(this);
        btn_top_goback.setOnClickListener(this);
        /*back.setOnClickListener(this);
        share.setOnClickListener(this);*/
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            advWeb.setBackgroundColor(0x00000000);
        } else {
            advWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = advWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        advWeb.loadUrl(url);
        advWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("MEntpInfo.do?id=")) {
                    String id = url.substring(url.indexOf("id=") + 3);
                    Intent intent = new Intent(AdvWebActivity.this, GoodsDetailActivity.class);
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
                    advWeb.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    advWeb.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_top_goback:
                if (advWeb.canGoBack() && !(url).equals(advWeb.getUrl())) {  //表示按返回键时的操作
                    advWeb.goBack();
                }else{
                    finish();
                }
                break;
            case R.id.img_top_right_one:
                shareAction(title, image_path, url);
                break;
        }
    }
}
