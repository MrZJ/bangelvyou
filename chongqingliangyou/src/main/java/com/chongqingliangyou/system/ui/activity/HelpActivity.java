package com.chongqingliangyou.system.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.util.ConfigUtil;

/**
 * 使用帮助页面
 *
 * @author Administrator
 *
 */
public class HelpActivity extends BaseActivity {

	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		initView();
		initListener();
	}

	@Override
	public void initListener() {
		super.initListener();
		initSideBarListener();
	}

	private void initView() {
		initTopView();
		setLeftBackButton();
		setTitle("帮助");
		web = (WebView) findViewById(R.id.wv_help);
		if (android.os.Build.VERSION.SDK_INT < 16) {
			web.setBackgroundColor(0x00000000);
		} else {
			web.setBackgroundColor(Color.argb(1, 0, 0, 0));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		WebSettings webSettings = web.getSettings();
//		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		web.loadUrl(ConfigUtil.HTTP_URL_HELP);
		web.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				showLoading("正在加载...",true);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				hiddenLoading();
				super.onPageFinished(view, url);
			}

		});
	}

}
