package com.shishoureport.system.ui.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
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
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener {

    LinearLayout linearWeb;
    private WebView webView;
    private Map<String, Object> titleMap = new HashMap<String, Object>();//存放标题 键是url 值是标题
    private ProgressBar webPro;
    private View top_bg;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initView(View v) {
//        setTopTitle("我的");
//        linearWeb = (LinearLayout) v.findViewById(R.id.mine_webview);
//        webPro = getProgressBar();
//        linearWeb.addView(webPro);
//        webView = getWebView();
//        linearWeb.addView(webView);
        top_bg=v.findViewById(R.id.top_bg);
        top_bg.setBackgroundResource(R.color.color_dd7117);
        webPro = (ProgressBar) v.findViewById(R.id.webPro);
        webView = (WebView) v.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setVerticalScrollBarEnabled(false);
        //设置账户
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (null != configEntity.key && !"".equals(configEntity.key)) {
            String ua = webSettings.getUserAgentString();
            //不能设置多个user_id!
            if (!ua.contains("key=")) {
                webSettings.setUserAgentString(ua + ";key=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("key=") + 4);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
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
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(webView.getSettings());
        Log.e("HTTP_MARKET_URL", ConfigUtil.HTTP_MINE_URL);
        webView.loadUrl(ConfigUtil.HTTP_MINE_URL);
        webView.setWebChromeClient(new MyWebChromeClient(webPro));
        webView.setWebViewClient(new MyWebViewClient(titleMap, ConfigUtil.HTTP_MINE_URL, this));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback://返回
                if (webView.canGoBack() && !(ConfigUtil.HTTP_MINE_URL).equals(webView.getUrl())) {  //表示按返回键时的操作
                    webView.loadUrl(ConfigUtil.HTTP_MINE_URL);
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
