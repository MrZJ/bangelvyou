package com.shishoureport.system.ui.activity;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shishoureport.system.R;
import com.shishoureport.system.listener.MyWebChromeClient;
import com.shishoureport.system.utils.ConfigUtil;

import butterknife.Bind;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements View.OnKeyListener,View.OnClickListener{

    @Bind(R.id.search_webview)
    LinearLayout linearWeb;
    private WebView webView;
    private ProgressBar webPro;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        initTopView();
        setLeftBackButton(true);
        webPro = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        webPro.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 8));
        webPro.setProgressDrawable(getResources().getDrawable(
                R.drawable.progress_drawable));
        linearWeb.addView(webPro);
        webView = new WebView(this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        webView.setVerticalScrollBarEnabled(false);
        linearWeb.addView(webView);
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTopTitle("搜索");
        setData();
    }

    private void initListener(){
        webView.setOnKeyListener(this);
        topGoBack.setOnClickListener(this);
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(webView.getSettings());
        webView.loadUrl(ConfigUtil.HTTP_SEARCH_URL);
        webView.setWebChromeClient(new MyWebChromeClient(webPro));
        webView.setWebViewClient(new WebViewClient(){});
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_top_goback://返回
                if (webView.canGoBack() && !(ConfigUtil.HTTP_SEARCH_URL).equals(webView.getUrl())) {  //表示按返回键时的操作
                    webView.goBack();
                }else{
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (!(ConfigUtil.HTTP_SEARCH_URL).equals(webView.getUrl())) {
                //表示按返回键时的操作
                webView.goBack();   //后退
                return true;
            }
        }
        return false;
    }
}
