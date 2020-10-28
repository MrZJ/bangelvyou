package com.shishoureport.system.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shishoureport.system.R;
import com.shishoureport.system.listener.MyWebChromeClient;
import com.shishoureport.system.listener.MyWebViewClient;
import com.shishoureport.system.utils.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/6/2.
 * copyright easybiz.
 */

public class WebFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener {
    public static final String WEB_URL_KEY = "web_url_key";
    LinearLayout linearWeb;
    private WebView webView;
    private Map<String, Object> titleMap = new HashMap<String, Object>();//存放标题 键是url 值是标题
    private ProgressBar webPro;
    private String mUrl;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View v) {
        Bundle b = getArguments();
        mUrl = b.getString(WEB_URL_KEY);
        setTopTitle("我的");
        linearWeb = (LinearLayout) v.findViewById(R.id.mine_webview);
        webPro = getProgressBar();
        linearWeb.addView(webPro);
        webView = getWebView();
        linearWeb.addView(webView);
        initListener();
    }

    private void initListener() {
        webView.setOnKeyListener(this);
        topGoBack.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (mUrl == null) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        webView.loadUrl(mUrl);
        webView.setWebChromeClient(new MyWebChromeClient(webPro));
        webView.setWebViewClient(new MyWebViewClient(titleMap, ConfigUtil.HTTP_MINE_URL, this));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback://返回
                if (webView.canGoBack()) {  //表示按返回键时的操作
                    webView.goBack();
                } else {
                    getActivity().finish();
                }
                break;
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (!(ConfigUtil.HTTP_MINE_URL).equals(webView.getUrl())) {
                //表示按返回键时的操作
                webView.loadUrl(ConfigUtil.HTTP_MINE_URL);   //后退
                return true;
            }
        }
        return false;
    }
}
