package com.shishoureport.system.ui.fragment;


import android.graphics.Color;
import android.util.Log;
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
 * 商城
 */
public class HomeFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener {
    LinearLayout linearWeb;
    private WebView webView;
    private Map<String, Object> titleMap = new HashMap<String, Object>();//存放标题 键是url 值是标题
    private ProgressBar webPro;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_infor;
    }

    @Override
    protected void initView(View v) {
        webPro = getProgressBar();
        linearWeb = (LinearLayout) v.findViewById(R.id.infor_top);
        linearWeb.addView(webPro);
        webView = getWebView();
        linearWeb.addView(webView);
        setTopTitle("半亩园");
        initListener();
    }

    private void initListener() {
        webView.setOnKeyListener(this);
        if (topGoBack != null) {
            topGoBack.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (webView==null){
            return;
        }
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        Log.e("HTTP_MARKET_URL", ConfigUtil.HTTP_HOME);
        webView.loadUrl(ConfigUtil.HTTP_HOME);
        webView.setWebChromeClient(new MyWebChromeClient(webPro));
        webView.setWebViewClient(new MyWebViewClient(titleMap, ConfigUtil.HTTP_HOME, this));

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (!(ConfigUtil.HTTP_HOME).equals(webView.getUrl())) {
                //表示按返回键时的操作
                webView.goBack();   //后退
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback://返回
                if (webView.canGoBack() && !(ConfigUtil.HTTP_HOME).equals(webView.getUrl())) {  //表示按返回键时的操作
                    webView.goBack();
                }
                break;
        }
    }
}