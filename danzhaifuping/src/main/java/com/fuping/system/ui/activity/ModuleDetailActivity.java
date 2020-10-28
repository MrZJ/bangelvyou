package com.fuping.system.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fuping.system.R;
import com.fuping.system.utils.ConfigUtil;
import com.fuping.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 相关模块详情页面
 */
public class ModuleDetailActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    private WebView detailWeb;
    private String moduleType;
    private String loadUrl;
    private Map<String, String> title_map = new HashMap();//存放标题 键是url 值是标题
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessages;
    private final static int FILECHOOSER_RESULTCODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_detail);
        moduleType = getIntent().getStringExtra("moduleType");
        initView();
        initListener();
        setData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(R.mipmap.cha);
        top_right_title.setText("首页");
        detailWeb = (WebView) findViewById(R.id.module_detail_web);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            detailWeb.setBackgroundColor(0x00000000);
        } else {
            detailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        btn_top_goback.setOnClickListener(this);
        rightOne.setOnClickListener(this);
        detailWeb.setOnKeyListener(this);
    }

    private void setData() {
        WebSettings webSettings = detailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
        webSettings.setJavaScriptEnabled(true);
        //设置账户
        if (!StringUtil.isEmpty(configEntity.key)) {
            String ua = webSettings.getUserAgentString();
            //不能设置多个user_id!
            if (!ua.contains("user_id=")) {
                webSettings.setUserAgentString(ua + ";user_id=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("user_id=") + 8);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
        if (moduleType.equals("helpObj")) {
            loadUrl = ConfigUtil.HTTP_URL_HELP_OBJECT;
        } else if (moduleType.equals("planMag")) {
            loadUrl = ConfigUtil.HTTP_URL_PLAN_MAG;
        } else if (moduleType.equals("planMag3")) {
            loadUrl = ConfigUtil.HTTP_URL_PLAN_MAG_3;
        } else if (moduleType.equals("logMag")) {
            loadUrl = ConfigUtil.HTTP_URL_LOGIN_MAG;
        } else if (moduleType.equals("mail")) {
            loadUrl = ConfigUtil.HTTP_URL_MAIL;
        }
        loadingUrl();
    }

    /**
     * webView加载url
     */
    private void loadingUrl() {
        detailWeb.loadUrl(loadUrl);
        detailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading("正在加载中...", true);
                if (!StringUtil.isEmpty(title_map.get(url))) {
                    setTitle(title_map.get(url));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //结束
                super.onPageFinished(view, url);
                hiddenLoading();
            }
        });
        detailWeb.setWebChromeClient(new WebChromeClient() {

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"),
                        FILECHOOSER_RESULTCODE);
            }

            // 3.0 + 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), FILECHOOSER_RESULTCODE);
            }

            // Android < 3.0 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//	     intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), FILECHOOSER_RESULTCODE);
            }

            // Android > 5.0 调用这个方法
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsgs,
                                             android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadMessages = uploadMsgs;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//	     intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), FILECHOOSER_RESULTCODE);
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
                if (view.getUrl() != null) {
                    title_map.put(view.getUrl(), title);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (Build.VERSION.SDK_INT < 21) {
                if (null == mUploadMessage) return;
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            } else {
                if (null == mUploadMessages) return;
                Uri results[] = new Uri[1];
                Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                        : intent.getData();
                if (result != null) {
                    results[0] = result;
                    mUploadMessages.onReceiveValue(results);
                    mUploadMessages = null;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title_map.clear();
        title_map = null;
        detailWeb.destroy();
        detailWeb = null;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK && detailWeb.canGoBack()) {
            detailWeb.goBack();   //后退
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback://返回
                if (detailWeb.canGoBack()) {  //表示按返回键时的操作
                    detailWeb.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.img_top_right_one:
                finish();
                break;
        }
    }
}
