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
 * 任务详情
 */
public class TaskDetailActivity extends BaseActivity {

    private WebView taskDetailWeb;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        taskId = getIntent().getStringExtra("id");
        initView();
        initListener();
        setData();
    }

    private  void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("任务详情");
        taskDetailWeb = (WebView) findViewById(R.id.web_task_detail);
        if (android.os.Build.VERSION.SDK_INT < 16) {
            taskDetailWeb.setBackgroundColor(0x00000000);
        } else {
            taskDetailWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
        WebSettings webSettings = taskDetailWeb.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //只从网络获取数据
//        webSettings.setJavaScriptEnabled(true);
        taskDetailWeb.loadUrl(ConfigUtil.HTTP_URL_TASK_DETAIL + taskId);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        taskDetailWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading("努力加载中...", true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hiddenLoading();
                //结束
                super.onPageFinished(view, url);
            }
        });
    }
}
