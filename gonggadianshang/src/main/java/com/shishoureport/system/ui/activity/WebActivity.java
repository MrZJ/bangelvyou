package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shishoureport.system.R;
import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.listener.ActivityWebViewClient;
import com.shishoureport.system.listener.MyWebChromeClient;
import com.shishoureport.system.utils.ConfigUtil;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class WebActivity extends Activity {
    WebView webView;
    private String mUrl =ConfigUtil.HTTP_HOME;
    public ConfigEntity configEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new ActivityWebViewClient(this, ConfigUtil.HTTP_HOME));
        webView.getSettings().setJavaScriptEnabled(true);
        configEntity = ConfigUtil.loadConfig(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置账户
        if (null != configEntity.key && !"".equals(configEntity.key)) {
            String ua = webSettings.getUserAgentString();
            //不能设置多个user_id!
            if (!ua.contains("uid=")) {
                webSettings.setUserAgentString(ua + ";uid=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("uid=") + 4);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
        webView.setVerticalScrollBarEnabled(false);
        setData();
    }

    public void setData() {
        webView.setWebChromeClient(new MyWebChromeClient(null));
        webView.setWebViewClient(new ActivityWebViewClient(this, ConfigUtil.HTTP_HOME));
        if (mUrl == null) {
            return;
        }

        webView.loadUrl(mUrl);
    }
}