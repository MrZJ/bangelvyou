package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.basulvyou.system.R;
import com.basulvyou.system.api.ShareApi;
import com.basulvyou.system.listener.ShareClickListener;
import com.basulvyou.system.ui.activity.LoginActivity;
import com.basulvyou.system.util.ConfigUtil;

/**
 * 问吧界面
 */
public class AskFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    private WebView askWeb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_ask, container, false);
        initView();
        initListener();
        showLoading(getResources().getString(R.string.load_text), true);
        return mView;
    }

    private void initView(){
        initTopView(mView);
        setTitle("问吧");
        hideBackBtn();
        askWeb = (WebView) mView.findViewById(R.id.web_ask);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            askWeb.setBackgroundColor(0x00000000);
        } else {
            askWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(getActivity());
        setData();
    }

    private void initListener(){
        topLeft.setOnClickListener(this);
    }

    private void setData(){
        WebSettings webSettings = askWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        if(configEntity.isLogin){
            askWeb.loadUrl(ConfigUtil.HTTP_ASK_BAR+"&key="+configEntity.key);
        }else{
            askWeb.loadUrl(ConfigUtil.HTTP_ASK_BAR);
    }
        askWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url != null && (url.endsWith("return"))) {
                    if(configEntity.isLogin){
                        view.loadUrl(ConfigUtil.HTTP_ASK_BAR+"&key="+configEntity.key);
                    }else{
                        view.loadUrl(ConfigUtil.HTTP_ASK_BAR);
                    }
                }
                if(configEntity.isLogin){
                    if (url.equals(ConfigUtil.HTTP_ASK_BAR + "&key=" + configEntity.key)) {
                        hideBackBtn();
                    } else {
                        showBackBtn();
                    }
                }else{
                    if (url.equals(ConfigUtil.HTTP_ASK_BAR)) {
                        hideBackBtn();
                    } else {
                        showBackBtn();
                    }
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!configEntity.isLogin && url.contains("login.shtml")) {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                    return true;
                }
                if (url != null && (url.startsWith("share:"))) {
                    String info[] = url.split(":");
                    if (null != info[1]) {
                        httpGetRequest(ShareApi.getCheckTokenUrl(info[1], info[2]), ShareApi.API_SHARE_CONTENT);
                    }
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hiddenLoading();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback:// 返回
                if (askWeb.canGoBack() && !(ConfigUtil.HTTP_ASK_BAR).equals(askWeb.getUrl())) { // 表示按返回键时的操作
                    askWeb.goBack();
                }
                break;
        }
    }

    /**
     * 返回数据
     *
     * @param json
     * @param action
     */
    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShareApi.API_SHARE_CONTENT:
                JSONObject object = JSON.parseObject(json);
                String title = object.getString("title");
                String url = object.getString("url");
                String shareUrl = ConfigUtil.HTTP_URL+url;
                ShareClickListener share = new ShareClickListener(getActivity());
                share.showWenBaShare(title,shareUrl);
                break;
            default:
                break;
        }
    }
}
