package com.yishangshuma.bangelvyou.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.listener.ShareClickListener;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

/**
 新闻详情页面
 */
public class NewsDetailActivity extends BaseActivity implements View.OnKeyListener{

    private WebView newsDetailWeb;
    private String id;
    private ShareClickListener shareClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        id = getIntent().getExtras().getString("id");
        initView();
        initListener();
        setData();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("新闻");
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHA);
        newsDetailWeb = (WebView) findViewById(R.id.news_detail_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            newsDetailWeb.setBackgroundColor(0x00000000);
        } else {
            newsDetailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        newsDetailWeb.setOnKeyListener(this);
        shareClickListener = new ShareClickListener(this);
        rightOne.setOnClickListener(shareClickListener);
        shareClickListener.setNewsUrl(id);
    }

    private void setData(){
        WebSettings webSettings = newsDetailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        newsDetailWeb.loadUrl(ConfigUtil.HTTP_URL_NEWS_DETAIL + id);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        newsDetailWeb.setWebViewClient(new WebViewClient() {
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
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
