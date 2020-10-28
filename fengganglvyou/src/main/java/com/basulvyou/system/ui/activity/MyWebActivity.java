package com.basulvyou.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.basulvyou.system.R;

/**
 * 天气web
 */
public class MyWebActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private WebView weather_web;
    private String title, url;

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, MyWebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_web);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initView();
        initListener();
        setData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle(title);
        //初始完成界面
        weather_web = (WebView) findViewById(R.id.weather_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            weather_web.setBackgroundColor(0x00000000);
        } else {
            weather_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        btn_top_goback.setOnClickListener(this);
        weather_web.setOnKeyListener(this);
    }

    private void setData() {
        WebSettings webSettings = weather_web.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        weather_web.loadUrl(url);
        weather_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading(getResources().getString(R.string.load_text), true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                hiddenLoading();
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback://返回
                MyWebActivity.this.finish();
                break;
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                MyWebActivity.this.finish();
            }
        }
        return false;
    }
}
