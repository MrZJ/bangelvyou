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
 * 公告详情页面
 * 轮播链接详情页面
 */
public class NoticeActivity extends BaseActivity implements View.OnKeyListener{

    private WebView detailWeb;
    private String title,uid,linkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        title = getIntent().getStringExtra("title");
        uid = getIntent().getStringExtra("uid");
        linkUrl = getIntent().getStringExtra("link_url");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle(title);
        detailWeb = (WebView) findViewById(R.id.notice_policy_detail_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        detailWeb.setOnKeyListener(this);
    }

    private void setData(){
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        if(StringUtil.isEmpty(linkUrl)){
            detailWeb.loadUrl(ConfigUtil.HTTP_URL_NOTICE_DETAIL+uid);
        }else{
            detailWeb.loadUrl(linkUrl);
        }
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
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
