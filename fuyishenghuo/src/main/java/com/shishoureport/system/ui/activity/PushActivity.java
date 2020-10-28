package com.shishoureport.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shishoureport.system.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送显示页面
 *
 * @author Administrator
 */
public class PushActivity extends BaseActivity implements OnClickListener,
        OnKeyListener {

    private WebView web;
    private String uData;// 推送过来的url
    private Map<String, Object> title_map = new HashMap<String, Object>();// 存放标题
    private View btn_top_goback;
    // 键是url
    // 值是标题



    @Override
    public int getLayoutId() {
        return R.layout.activity_push;
    }

    public void initView() {
        initTopView();
        uData = getIntent().getExtras().getString("msg_data");
        web = (WebView) findViewById(R.id.push_webview);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            web.setBackgroundColor(0x00000000);
        } else {
            web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        btn_top_goback = findViewById(R.id.btn_top_goback);
        btn_top_goback.setOnClickListener(this);
        web.setOnKeyListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.loadUrl(uData);
        web.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (null != title_map.get(url)) {
                    setTitle(title_map.get(url).toString());
                }
                if (url != null && (uData).equals(url)) {
                    setLeftBackShow(false);
                } else {
                    setLeftBackShow(true);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null
                        && (url.startsWith("mailto:") || url.startsWith("geo:") || url
                        .startsWith("tel:"))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 结束
                super.onPageFinished(view, url);
            }

        });
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
                if (null != view.getUrl()) {
                    title_map.put(view.getUrl(), title);// 存放标题
                }
            }

        };
        web.setWebChromeClient(wvcc);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (web != null) {
            web.removeAllViews();
            web.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback:// 返回
                if (web.canGoBack() && !(uData).equals(web.getUrl())) { // 表示按返回键时的操作
                    web.goBack();
                }
                break;
        }
    }

    @Override
    public boolean onKey(View arg0, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
                if ((uData).equals(web.getUrl())) {
                    intentMainOrLogin();
                    return true;
                } else if (!(uData).equals(web.getUrl())) {
                    // 表示按返回键时的操作
                    web.goBack(); // 后退
                    return true;
                }
            }
            intentMainOrLogin();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
                if ((uData).equals(web.getUrl())) {
                    intentMainOrLogin();
                    return true;
                } else if (!(uData).equals(web.getUrl())) {
                    // 表示按返回键时的操作
                    web.goBack(); // 后退
                    return true;
                }
            }
            intentMainOrLogin();
        }
        return false;
    }

    /**
     * 是否显示返回按钮
     *
     * @param isShow
     */
    public void setLeftBackShow(boolean isShow) {
        if (isShow) {
            btn_top_goback.setVisibility(View.VISIBLE);
        } else {
            btn_top_goback.setVisibility(View.GONE);
        }
    }

}
