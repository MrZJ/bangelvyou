package com.zycreview.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.zycreview.system.R;

public class MineWebActivity extends BaseActivity implements View.OnKeyListener{

    private WebView detailWeb;
    private LinearLayout linearWeb;
    private String title,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle(title);
        linearWeb = (LinearLayout) findViewById(R.id.mine_web);
        detailWeb = new WebView(this);
        detailWeb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        detailWeb.setVerticalScrollBarEnabled(false);
        linearWeb.addView(detailWeb);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        detailWeb.setOnKeyListener(this);
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        detailWeb.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        detailWeb.setWebChromeClient(new WebChromeClient());
        detailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailWeb.removeAllViews();
        detailWeb.destroy();
    }
}
