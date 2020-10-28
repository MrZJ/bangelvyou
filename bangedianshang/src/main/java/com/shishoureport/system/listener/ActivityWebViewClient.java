package com.shishoureport.system.listener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.ui.activity.WebActivity;
import com.shishoureport.system.utils.ConfigUtil;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class ActivityWebViewClient extends WebViewClient{
    private WebActivity activity;
    private String config_url;

    public ActivityWebViewClient(WebActivity activity, String config_url) {
        this.activity = activity;
        this.config_url = config_url;
    }

    private String startUrl;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        startUrl = url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(TextUtils.isEmpty(startUrl) || startUrl.equals(url)){
            return super.shouldOverrideUrlLoading(view, url);
        }
        Log.e("UrlLoading",url);
        if (activity == null) {
            return false;
        }
        if (url != null && (url.contains("objc"))) {
            String info[] = url.split(":");
            ConfigEntity configEntity = ConfigUtil.loadConfig(activity);
            configEntity.key = info[1];
            ConfigUtil.saveConfig(activity, configEntity);
//            StringBuilder add_dev = new StringBuilder(ConfigUtil.HTTP_HOME);
//            add_dev.append("&id=").append(configEntity.key).append("&device=android").append("&device_tokens=").append(PushAgent.getInstance(mFragment.getActivity()).getRegistrationId());
//            if (UIApplication.mRequestQueue == null)
//                UIApplication.mRequestQueue = Volley.newRequestQueue(mFragment.getActivity(), new OkHttpStack());
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, add_dev.toString(),
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String content) {
//                        }
//                    }, new Response.ErrorListener() {
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//            Log.e("jianzhang", add_dev.toString());
//            UIApplication.mRequestQueue.add(stringRequest);
            activity.setData();
            return true;
        }
        if (url != null && (url.contains("/wap/MMyCartInfo.do?method=step3"))) {
            activity.setData();
        }
        if (url != null && (url.startsWith("zhuxiao"))) {
            ConfigEntity configEntity = ConfigUtil.loadConfig(activity);
            configEntity.key = "";
            ConfigUtil.saveConfig(activity, configEntity);
            WebSettings webSettings = view.getSettings();
            String ua = webSettings.getUserAgentString();
            ua = ua.substring(0, ua.indexOf("uid=") + 4);
            webSettings.setUserAgentString(ua);
            view.loadUrl(ConfigUtil.HTTP_HOME);
            return true;
        }
        if (url != null && (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:"))) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
//        if (url != null && (url.contains("alipay"))) {
//            Log.e("alipay", url);
//            String aliPayStr = url.substring(url.indexOf("alipay_sdk"));
//            Log.e("aliPayStr", aliPayStr);
//            payAli(aliPayStr);
//            return true;
//        }
        view.loadUrl(url);
        return true;
    }
}
