package com.yishangshuma.bangelvyou.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.util.ConfigUtil;

/**
 * 网页支付界面
 */
public class PayWebActivity extends BaseActivity {

    private WebView pay_web;
    private String orderSn;
    private String payType;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_web);
        orderSn = getIntent().getStringExtra("order_sn");
        payType = getIntent().getStringExtra("pay_type");
        initView();
        initListener();
        if(payType.equals("2")){
//            微信支付
        }else{
            setData();
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("立即支付");
        pay_web = (WebView) findViewById(R.id.pay_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            pay_web.setBackgroundColor(0x00000000);
        } else {
            pay_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    /**
     * 设置tab数据
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setData(){
		/*MyWebViewClient myWebViewClient = new MyWebViewClient(KuaiDiActivity.this);
		kuaidi_web.setWebViewClient(myWebViewClient);*/
        WebSettings webSettings = pay_web.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        pay_web.loadUrl(ConfigUtil.HTTP_PAY_WEB + "&trade_index=" + orderSn + "&key=" + configEntity.key + "&pay_type=" + payType);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        pay_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if(url != null && url.contains("/m/MMyHome.do")){
					/*Intent orderIntent = new Intent(PayWebActivity.this,MyOrderActivity.class);
					startActivity(orderIntent);*/
                    PayWebActivity.this.finish();
                } else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url){
                if( url != null && url.contains("https://mclient.alipay.com/") ){
                    setBackShow(false);
                } else {
                    setBackShow(true);
                }
                //结束
//                disMissDialog();
                super.onPageFinished(view, url);
            }
        });
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
    }
}
