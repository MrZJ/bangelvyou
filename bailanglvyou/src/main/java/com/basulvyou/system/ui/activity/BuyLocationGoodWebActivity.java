package com.basulvyou.system.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.api.PayApi;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 虚拟商品购物车界面
 *
 */
public class BuyLocationGoodWebActivity extends BaseActivity implements OnClickListener, OnKeyListener{

	private WebView groupCart_web;
	private String comm_id,select_attr,inDate,outDate,type;
	/** 微信支付参数 */
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private StringBuffer questSting;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_location_goods);
		// 微信支付注册app_id
		msgApi.registerApp(Constants.APP_ID);
		comm_id = getIntent().getExtras().getString("id");//商品id
		type = getIntent().getExtras().getString("type");//商品类型虚拟商品\团购商品
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
		WebSettings webSettings = groupCart_web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		if(null != type && type.equals("group")){
			groupCart_web.loadUrl(ConfigUtil.HTTP_GROUP_BUY +configEntity.key +"&group_id="+comm_id);
		}else{
			questSting = new StringBuffer("&comm_id=");
			questSting.append(comm_id);
			questSting.append("&select_attr=").append(select_attr);
			questSting.append("&inDate=").append(inDate);
			if(null != outDate && !"".equals(outDate)){
				questSting.append("&outDate=").append(outDate);
			}
			groupCart_web.loadUrl(ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString());
		}
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	    groupCart_web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				if (url != null && url.endsWith("/m/index.shtml")) {
					BuyLocationGoodWebActivity.this.finish();
				} else if (url != null && url.contains("/service/AppMyOrderInfo.do")) {
					Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this, OrderListActivity.class);
					startActivity(orderIntent);
					BuyLocationGoodWebActivity.this.finish();
				} else if (url != null && url.contains("/m/MMyHome.do")) {
					Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this, OrderListActivity.class);
					startActivity(orderIntent);
					BuyLocationGoodWebActivity.this.finish();
				} else if (url != null && url.contains("/m/MEntpInfo.do?id=")) {
					String[] goodId = url.split("=");
					Intent goodsIntent = new Intent(BuyLocationGoodWebActivity.this, LocationDetailActivity.class);
					if (goodId != null && goodId[1] != null) {
						goodsIntent.putExtra("goods_id", goodId[1]);
						startActivity(goodsIntent);
					}
				} else if (url != null && (url.contains("weixinpay:"))) {// 截取微信支付
					String info = (String) url.substring(url.indexOf(":") + 1);
					isWeixinSupported(msgApi, info);
				} else {
					view.loadUrl(url);
				}
				return true;
			}


			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url != null && url.contains("https://mclient.alipay.com/")) {
					setBackShow(false);
					setTitle("在线支付");
				} else {
					setBackShow(true);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
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
			if(groupCart_web.canGoBack()){
				if(null != type && type.equals("group")){
					if((ConfigUtil.HTTP_GROUP_BUY +configEntity.key +"&group_id="+comm_id).equals(groupCart_web.getUrl())){
						BuyLocationGoodWebActivity.this.finish();
					}else{
						groupCart_web.goBack();
					}
				}else{
					if((ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString()).equals(groupCart_web.getUrl())){
						BuyLocationGoodWebActivity.this.finish();
					}else{
						groupCart_web.goBack();
					}
				}
			}
			if(groupCart_web.canGoBack() && groupCart_web.getUrl().contains("https://mcashier.95516.com/")){
				Intent orderIntent = new Intent(BuyLocationGoodWebActivity.this,OrderListActivity.class);
				startActivity(orderIntent);
				BuyLocationGoodWebActivity.this.finish();
            } /*else if (groupCart_web.canGoBack() && (ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())) {  //表示按返回键时的操作
            	BuyLocationGoodWebActivity.this.finish();
            } */else if (groupCart_web.canGoBack() && groupCart_web.getUrl().contains("/service/AppMyCartInfo.do")){
				if(null != type && type.equals("group")){
					groupCart_web.loadUrl(ConfigUtil.HTTP_GROUP_BUY +configEntity.key +"&group_id="+comm_id);
				}else{
					groupCart_web.loadUrl(ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString());
				}
	        } /*else if (groupCart_web.canGoBack() && !(ConfigUtil.HTTP_CART_URL + configEntity.key).equals(groupCart_web.getUrl())) {  //表示按返回键时的操作
	        	groupCart_web.goBack();
            } */else {
            	BuyLocationGoodWebActivity.this.finish();
            }
			break;
		}
	}

	/**
	 * 加载团购活虚拟商品
	 */
	private void loadGroupOrVis(){
		if(null != type && type.equals("group")){
			groupCart_web.loadUrl(ConfigUtil.HTTP_GROUP_BUY +configEntity.key +"&group_id="+comm_id);
		}else{
			groupCart_web.loadUrl(ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString());
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK && groupCart_web.canGoBack()) {
				if(null != type && type.equals("group")){
					if((ConfigUtil.HTTP_GROUP_BUY +configEntity.key +"&group_id="+comm_id).equals(groupCart_web.getUrl())){
						BuyLocationGoodWebActivity.this.finish();
					}else{
						groupCart_web.goBack();
						return  true;
					}
				}else{
					if((ConfigUtil.HTTP_VIRTUAL_BUY + configEntity.key + questSting.toString()).equals(groupCart_web.getUrl())){
						BuyLocationGoodWebActivity.this.finish();
					}else{
						groupCart_web.goBack();
						return  true;
					}
				}
				if(groupCart_web.getUrl().contains("https://mclient.alipay.com/")){
					loadGroupOrVis();
					return true;
				} else if(groupCart_web.getUrl().contains("https://mcashier.95516.com/")) {
					loadGroupOrVis();
					return true;
				} else if (groupCart_web.getUrl().contains("/service/AppMyCartInfo.do")){
					loadGroupOrVis();
					return true;
				}
            }
            BuyLocationGoodWebActivity.this.finish();
        } 
		return false;
	}

	@Override
	public void httpResponse(String json, int action) {
		switch (action) {
			case PayApi.API_GET_WEIXIN_PAY_INFO:
				handlerWeiXin(json);
				break;
		}
	}

	private void handlerWeiXin(String json) {
		if (null != json && !"".equals(json)) {
			// 调用微信支付
			PayReq req = new PayReq();
			req.appId = JSON.parseObject(json).getString("appid");
			req.nonceStr = JSON.parseObject(json).getString("noncestr");
			req.packageValue = JSON.parseObject(json).getString("package");
			req.partnerId = JSON.parseObject(json).getString("partnerid");
			req.prepayId = JSON.parseObject(json).getString("prepayid");
			req.sign = JSON.parseObject(json).getString("sign");
			req.timeStamp = JSON.parseObject(json).getString("timestamp");
			msgApi.sendReq(req);
		} else {
			Toast.makeText(this, "在线支付失败", Toast.LENGTH_SHORT).show();
		}
	}

}
