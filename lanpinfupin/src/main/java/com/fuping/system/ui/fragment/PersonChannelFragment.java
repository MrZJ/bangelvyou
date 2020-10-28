package com.fuping.system.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fuping.system.R;
import com.fuping.system.utils.ConfigUtil;
import com.fuping.system.utils.StringUtil;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class PersonChannelFragment extends BaseFragment implements View.OnKeyListener {

    private WebView detailWeb;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_person_channel, container, false);
        initView();
        initListener();
        setData();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        hideBackBtn();
        detailWeb = (WebView) mView.findViewById(R.id.notice_policy_detail_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    public void initListener() {
        detailWeb.setOnKeyListener(this);
    }

    private void setData() {
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        //设置账户
        if (!StringUtil.isEmpty(configEntity.key)) {
            String ua = webSettings.getUserAgentString();
            //不能设置多个user_id!
            if (!ua.contains("user_id=")) {
                webSettings.setUserAgentString(ua + ";user_id=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("user_id=") + 8);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
        detailWeb.loadUrl(ConfigUtil.HTTP_URL_PERSON_CHANNEL);
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

        detailWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
