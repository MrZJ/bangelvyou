package com.chongqingliangyou.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.util.ConfigUtil;

/**
 * 关于页面
 */
public class ABoutActivity extends BaseActivity {

    private WebView webAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
        setData();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("关于");
        webAbout = (WebView) findViewById(R.id.web_about);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webAbout.setBackgroundColor(0x00000000);
        } else {
            webAbout.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
        WebSettings webSettings = webAbout.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
//        webSettings.setJavaScriptEnabled(true);
        webAbout.loadUrl(ConfigUtil.HTTP_URL_ABOUT);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webAbout.setWebViewClient(new WebViewClient() {
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
}
