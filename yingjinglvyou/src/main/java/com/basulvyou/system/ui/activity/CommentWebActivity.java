package com.basulvyou.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.basulvyou.system.R;
import com.basulvyou.system.util.ConfigUtil;


/**
 * 商品web评论显示页面
 */
public class CommentWebActivity extends BaseActivity {

    private WebView commentWeb;
    private String comm_id,comm_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_web);
        comm_id = getIntent().getExtras().getString("comm_id");
        comm_type = getIntent().getExtras().getString("goodType");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("更多评论");
        commentWeb = (WebView) findViewById(R.id.web_goods_comment);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            commentWeb.setBackgroundColor(0x00000000);
        }else{
            commentWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    /**
     * 设置数据
     */
    private void setData() {
        WebSettings webSettings=commentWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(null != comm_type && comm_type.equals("group")){
            commentWeb.loadUrl(ConfigUtil.HTTP_GOOD_COMMENT + comm_id +"&comm_type=50");
        }else{
            commentWeb.loadUrl(ConfigUtil.HTTP_GOOD_COMMENT + comm_id);
        }
        commentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
//                disMissDialog();
                super.onPageFinished(view, url);
            }
        });
    }
}
