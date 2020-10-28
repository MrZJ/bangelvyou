package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.basulvyou.system.R;
import com.basulvyou.system.api.ShareTextApi;
import com.basulvyou.system.listener.ShareClickListener;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;

/**
 * 热门分享详情页面
 */
public class ShareDetailActivity extends BaseActivity implements View.OnClickListener{

    private String shareId, commentNum, shareTitle;
    private ShareClickListener shareClickListener;
    private Button commentBtn;
    private WebView detailWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        shareId = getIntent().getStringExtra("shareId");
        commentNum = getIntent().getStringExtra("commentNum");
        shareTitle = getIntent().getStringExtra("shareTitle");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("详情信息");
        setTopRightImg(R.mipmap.top_share,TopClickUtil.TOP_SHA);
        detailWeb = (WebView) findViewById(R.id.web_share_detail);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        commentBtn = (Button) findViewById(R.id.btn_share_detail_comment);
        if(!StringUtil.isEmpty(commentNum)){
            commentBtn.setText("评论(" + commentNum + ")");
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        shareClickListener = new ShareClickListener(this);
        setShareNameAndImg();
        rightOne.setOnClickListener(shareClickListener);
        commentBtn.setOnClickListener(this);
    }

    /**
     * 设置分享文字和图片
     */
    private void setShareNameAndImg() {
        if (shareClickListener != null) {
            shareClickListener.setcultureUrl(ConfigUtil.HTTP_SHARE_DETAIL_WEB + shareId);
            shareClickListener.setContent(shareTitle);
        }
    }

    /**
     * 点赞分享
     */
    public void zan(){
        httpGetRequest(ShareTextApi.getZanShareUrl(configEntity.key, shareId), ShareTextApi.API_GET_SHARE_ZAN);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case ShareTextApi.API_GET_SHARE_ZAN:
                Toast.makeText(this,"点赞成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 把数据在界面上显示
     */
    private void setData(){
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        detailWeb.loadUrl(ConfigUtil.HTTP_SHARE_DETAIL_WEB + shareId);
        detailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading(getResources().getString(R.string.load_text), true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                hiddenLoading();
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share_detail_comment:
                Intent commentIntent = new Intent(this,ShareCommentActivity.class);
                commentIntent.putExtra("shareId",shareId);
                startActivity(commentIntent);
                break;
        }
    }
}
