package com.shenmaireview.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenmaireview.system.R;

import butterknife.Bind;


/**
 * 扫描结果页面
 */
public class ScanResultActivity extends BaseActivity {

    @Bind(R.id.scan_detail_web)
    WebView detailWeb;
    private String scanUrl;


    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_result;
    }

    @Override
    public void initView() {
        initTopView();
        scanUrl = getIntent().getStringExtra("scanUrl");
        setTopTitle("农产品追溯详情");
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
        detailWeb.loadUrl(scanUrl);
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