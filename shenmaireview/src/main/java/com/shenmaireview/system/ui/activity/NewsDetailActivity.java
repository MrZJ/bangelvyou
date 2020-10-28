package com.shenmaireview.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenmaireview.system.R;
import com.shenmaireview.system.utils.ConfigUtil;

import butterknife.Bind;

public class NewsDetailActivity extends BaseActivity {

    @Bind(R.id.news_detail_web)
    WebView detailWeb;
    private String newsId,newsTitle;


    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        initTopView();
        newsId = getIntent().getStringExtra("newsId");
        newsTitle = getIntent().getStringExtra("title");
        setTopTitle(newsTitle);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        setData();
        initTopListener();
    }


    private void setData(){
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        detailWeb.loadUrl(ConfigUtil.HTTP_NEWSDETAIL_URL+newsId);
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
}
