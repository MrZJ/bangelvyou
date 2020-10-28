package com.fuping.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fuping.system.R;
import com.fuping.system.utils.ConfigUtil;
import com.fuping.system.utils.StringUtil;

/**
 * 个人信息
 * 我的帮扶人
 */
public class InfoAndHelperActivity extends BaseActivity implements View.OnKeyListener,View.OnClickListener{

    private WebView detailWeb;
    private String type;
    private String loadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_and_helper);
        type = getIntent().getStringExtra("type");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle(type);
        detailWeb = (WebView) findViewById(R.id.info_helper_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        btn_top_goback.setOnClickListener(this);
        detailWeb.setOnKeyListener(this);
    }

    private void setData(){
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        if (!StringUtil.isEmpty(configEntity.key)) {
            String ua = webSettings.getUserAgentString();
            //不能设置多个user_id!
            if (!ua.contains("user_id=")) {
                webSettings.setUserAgentString(ua + ";user_id=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("user_id=") + 8);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
        if (type.equals("个人信息")) {
            loadUrl = ConfigUtil.HTTP_URL_MINE_INFO;
        } else if (type.equals("我的帮扶人")) {
            loadUrl = ConfigUtil.HTTP_URL_MINE_HELP;
        } else if (type.equals("关于我们")) {
            loadUrl = ConfigUtil.HTTP_URL_ABOUT_US;
        }
        detailWeb.loadUrl(loadUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
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
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && detailWeb.canGoBack()) {
            if (!StringUtil.isEmpty(loadUrl) && !(loadUrl).equals(detailWeb.getUrl())) {
                //表示按返回键时的操作
                detailWeb.goBack();   //后退
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_top_goback://返回
                if (detailWeb.canGoBack() && !StringUtil.isEmpty(loadUrl) && !(loadUrl).equals(detailWeb.getUrl())) {  //表示按返回键时的操作
                    detailWeb.goBack();
                } else {
                    finish();
                }
                break;
        }
    }
}
