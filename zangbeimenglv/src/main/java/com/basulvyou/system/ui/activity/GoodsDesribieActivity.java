package com.basulvyou.system.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.basulvyou.system.R;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;


/**
 * 商品信息web页面
 */
public class GoodsDesribieActivity extends BaseActivity {

    private WebView desWeb;
    private String comm_id,good_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_desribie);
        comm_id = getIntent().getExtras().getString("comm_id");
        good_type = getIntent().getExtras().getString("goodType");
        initView();
        initListener();
        setData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("商品信息");
        desWeb = (WebView) findViewById(R.id.web_goods_describe);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            desWeb.setBackgroundColor(0x00000000);
        }else{
            desWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
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
        WebSettings webSettings=desWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (!StringUtil.isEmpty(good_type) && good_type.equals("group")) {
            desWeb.loadUrl(ConfigUtil.HTTP_GROUP_GOOD_DESCRIBE + comm_id);
        } else {
            desWeb.loadUrl(ConfigUtil.HTTP_GOOD_DESCRIBE + comm_id);
        }
        desWeb.setWebViewClient(new WebViewClient() {
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
