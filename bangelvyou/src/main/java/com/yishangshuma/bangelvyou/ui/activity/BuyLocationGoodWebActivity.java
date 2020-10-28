package com.yishangshuma.bangelvyou.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.util.ConfigUtil;

/**
 * 虚拟商品购物车界面
 *
 */
public class BuyLocationGoodWebActivity extends BaseActivity implements OnClickListener, OnKeyListener{

	private WebView groupCart_web;
	private String comm_id,select_attr,inDate,outDate;
    
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_location_goods);
		comm_id = getIntent().getExtras().getString("id");//商品id
		select_attr = getIntent().getExtras().getString("select_attr");//商品属性
		inDate = getIntent().getExtras().getString("inDate");//进店时间
		outDate = getIntent().getExtras().getString("outDate");//离店时间
		initView();
		initListener();
		setData();
	}

	/**
	 * 初始化界面
	 */
	private void initView(){
		initTopView();
		setLeftBackButton();
		setTitle("提交订单");
		groupCart_web = (WebView) findViewById(R.id.web_buy_location);
		if (android.os.Build.VERSION.SDK_INT < 16) {
			groupCart_web.setBackgroundColor(0x00000000);
		} else {
			groupCart_web.setBackgroundColor(Color.argb(1, 0, 0, 0));
		}
	}
	
	/**
	 * 设置tab数据
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void setData(){
		StringBuffer questSting = new StringBuffer("&comm_id=");
		questSting.append(comm_id);
		questSting.append("&select_attr=").append(select_attr);
		questSting.append("&inDate=").append(inDate);
		if(null != outDate && !"".equals(outDate)){
			questSting.append("&outDate=").append(outDate);
		}
		WebSettings webSettings = groupCart_web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		groupCart_web.loadUrl(ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString());
		Log.e("ss",ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString());
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	    groupCart_web.setWebViewClient(new WebViewClient(){
			 @Override
	         public boolean shouldOverrideUrlLoading(WebView view, String url) {
				 //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				 if (url != null && url.endsWith("/m/index.shtml")) {
					    BuyLocationGoodWebActivity.this.finish();
					} else if(url != null && url.contains("/service/AppMyOrderInfo.do")){
						Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this,OrderListActivity.class);
						startActivity(orderIntent);
						BuyLocationGoodWebActivity.this.finish();
					} else if(url != null && url.contains("/m/MMyHome.do")){
						Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this,OrderListActivity.class);
						startActivity(orderIntent);
						BuyLocationGoodWebActivity.this.finish();
					} else if(url != null && url.contains("/m/MEntpInfo.do?id=")){
						String[] goodId=url.split("=");
						Intent goodsIntent = new Intent(BuyLocationGoodWebActivity.this, LocationDetailActivity.class);
						if (goodId != null && goodId[1] != null) {
							goodsIntent.putExtra("goods_id", goodId[1]);
							startActivity(goodsIntent);
						}
					} else {
						view.loadUrl(url);
					}
	             return true;
	         }
			 
			 
		    @Override  
			public void onPageStarted(WebView view, String url, Bitmap favicon){
				if( url != null && url.contains("https://mclient.alipay.com/") ){
            		setBackShow(false);
					setTitle("在线支付");
            	} else {
            		setBackShow(true);
            	}
				super.onPageStarted(view, url, favicon);
			}
		    
			@Override  
			public void onPageFinished(WebView view, String url){  
				//结束  
//				disMissDialog();
				super.onPageFinished(view, url);  
			} 
        });
	}

	public void initListener(){
		super.initListener();
		initSideBarListener();
		btn_top_goback.setOnClickListener(this);
		groupCart_web.setOnKeyListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_top_goback://返回
			if(groupCart_web.canGoBack() && groupCart_web.getUrl().contains("https://mcashier.95516.com/")){
				Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this,OrderListActivity.class);
				startActivity(orderIntent);
				BuyLocationGoodWebActivity.this.finish();
            } else if (groupCart_web.canGoBack() && (ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())) {  //表示按返回键时的操作  
            	BuyLocationGoodWebActivity.this.finish();
            } else if (groupCart_web.canGoBack() && groupCart_web.getUrl().contains("/service/AppMyCartInfo.do")){
            	groupCart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
	        } else if (groupCart_web.canGoBack() && !(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())) {  //表示按返回键时的操作  
	        	groupCart_web.goBack();
            } else {
            	BuyLocationGoodWebActivity.this.finish();
            }
			break;
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK && groupCart_web.canGoBack()) {  
            	if((ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())){
            		BuyLocationGoodWebActivity.this.finish();
            	   return true;
            	} else if(groupCart_web.getUrl().contains("https://mclient.alipay.com/")){
            		groupCart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
 				   return true;  
            	} else if(groupCart_web.getUrl().contains("https://mcashier.95516.com/")){
            		groupCart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
				   return true;
				} else if (groupCart_web.getUrl().contains("/service/AppMyCartInfo.do")){
					groupCart_web.loadUrl(ConfigUtil.HTTP_CART_URL + configEntity.key);
				   return true;
		        } else if (!(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())) {
 	               //表示按返回键时的操作  
		           groupCart_web.goBack();   //后退 
 	               return true;  
 				}        	
            } 
            BuyLocationGoodWebActivity.this.finish();
        } 
		return false;
	}
}
