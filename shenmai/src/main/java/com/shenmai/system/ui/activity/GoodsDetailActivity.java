package com.shenmai.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.shenmai.system.R;
import com.shenmai.system.api.GoodsApi;
import com.shenmai.system.entity.GoodsDetailShareEntity;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.utlis.TopClickUtil;

/**
 * 商品详情页面
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener{

    private WebView detailWeb;
    private LinearLayout linearWeb;
    private String goodsId;
    private String imgUrl;//分享图片的url
    private String title;//分享的title
    private String url;//分享的url
    private String des;//分享的内容
    private boolean isShare;
    private boolean isError = false;
    private View netError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        goodsId = getIntent().getStringExtra("id");
        loadData();
        initView();
        initListener();
        setData();
    }

    private void loadData() {
        httpGetRequest(GoodsApi.getActionGoodsDetailShare(goodsId), GoodsApi.API_GOODS_DETAIL_SHARE);
    }

    @Override
    protected void httpResponse(String json, int action) {
        switch (action) {
            case GoodsApi.API_GOODS_DETAIL_SHARE:
                GoodsDetailShareEntity entity = JSON.parseObject(json, GoodsDetailShareEntity.class);
                if (entity != null) {
                    imgUrl = entity.comm_image_path;
                    title = entity.sub_title;
                    des = entity.comm_name;
                    url = entity.comm_url;
                    if (!StringUtil.isEmpty(title) && !StringUtil.isEmpty(url)) {
                        isShare = true;
                    }
                }
                break;
        }
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("商品详情");
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHARE);
        linearWeb = (LinearLayout) findViewById(R.id.detail_webview);
        netError = findViewById(R.id.rel_net_error);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        rightOne.setOnClickListener(this);
        btn_top_goback.setOnClickListener(this);
    }

    private void setData(){
        detailWeb = new WebView(getApplicationContext());
        detailWeb.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        detailWeb.setVerticalScrollBarEnabled(false);
        linearWeb.addView(detailWeb);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        WebSettings webSettings = setUserAgent(detailWeb.getSettings());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        detailWeb.loadUrl(ConfigUtil.HTTP_GOODS_DETAIL_URL + "id=" + goodsId);
        detailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("/MMyCartInfoPt.do")) {
                    MainActivity.cartState = 2;
                    appManager.finishOtherActivity();
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
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
                    detailWeb.setVisibility(View.GONE);
                } else {
                    netError.setVisibility(View.GONE);
                    detailWeb.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_top_goback:
                if (detailWeb.canGoBack() && !(ConfigUtil.HTTP_GOODS_DETAIL_URL + "id=" + goodsId).equals(detailWeb.getUrl())) {  //表示按返回键时的操作
                    detailWeb.goBack();
                }else{
                    finish();
                }
                break;
            case R.id.img_top_right_one:
                if (isShare) {
                    shareAction(title, des, imgUrl, url);
                } else {
                    Toast.makeText(this, "未得到分享数据...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linearWeb.removeView(detailWeb);
        detailWeb.removeAllViews();
        detailWeb.destroy();
    }
}
