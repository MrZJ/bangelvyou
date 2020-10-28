package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.shenmai.system.R;
import com.shenmai.system.api.RegisterApi;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.TopClickUtil;

import java.util.HashMap;

/**
 * 提示开店界面
 */
public class NoticeActivity extends BaseActivity implements View.OnClickListener{

    private WebView noticeWeb;
    private TextView open, view;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setTitle("直接购买");
        setLeftBackShow(false);
        setTopRightTitle("注销", TopClickUtil.TOP_RESET);
        noticeWeb = (WebView) findViewById(R.id.notice_webview);
        open = (TextView) findViewById(R.id.tv_notice_open);
        view = (TextView) findViewById(R.id.tv_notice_view);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    private void setData(){
        if (android.os.Build.VERSION.SDK_INT < 16) {
            noticeWeb.setBackgroundColor(0x00000000);
        } else {
            noticeWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = noticeWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        noticeWeb.loadUrl(ConfigUtil.HTTP_GOODS_OPENSHOP_NOTICE);
        noticeWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isError) {
                    netError.setVisibility(View.VISIBLE);
                    noticeWeb.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    noticeWeb.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        open.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_notice_open:
                resShop();
                break;
            case R.id.tv_notice_view:
                startActivity(new Intent(this,MainActivity.class));
                this.finish();
                break;
        }
    }

    /**
     * 申请开店
     */
    private void resShop(){
        httpPostRequest(RegisterApi.getResShopUrl(),getRequestParams(),RegisterApi.API_UPDATE_RES_SHOP);
    }

    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", configEntity.key);
        return params;
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case RegisterApi.API_UPDATE_RES_SHOP:
                Toast.makeText(this,"申请开店成功",Toast.LENGTH_SHORT).show();
                configEntity.userRole = "1";
                ConfigUtil.saveConfig(this, configEntity);
                startActivity(new Intent(this,MainActivity.class));
                this.finish();
                break;
        }
    }
}
