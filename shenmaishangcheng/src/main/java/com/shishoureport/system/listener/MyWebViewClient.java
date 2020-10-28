package com.shishoureport.system.listener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.ui.fragment.BaseFragment;
import com.shishoureport.system.utils.ConfigUtil;

import java.lang.ref.WeakReference;
import java.util.Map;


public class MyWebViewClient extends WebViewClient {

    private Map<String, Object> title_map;
    private String config_url;
    private WeakReference<BaseFragment> mRf;
    private BaseFragment mFragment;

    public MyWebViewClient(Map<String, Object> title_map, String config_url, BaseFragment fragment) {
        this.title_map = title_map;
        this.config_url = config_url;
        mRf = new WeakReference<BaseFragment>(fragment);
    }

    @Override
    public void onPageStarted(WebView view, final String url, Bitmap favicon) {
        mFragment = mRf.get();
        if (mFragment == null) {
            return;
        }
        if (!config_url.equals(url)) {
            mFragment.setLeftBackButton(true);
        } else {
            mFragment.setLeftBackButton(false);
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mFragment = mRf.get();
        if (mFragment == null) {
            return false;
        }
        if (url != null && (url.contains("objc"))) {
            String info[] = url.split(":");
            ConfigEntity configEntity = ConfigUtil.loadConfig(mFragment.getActivity());
            configEntity.key = info[1];
            ConfigUtil.saveConfig(mFragment.getActivity(), configEntity);
            mFragment.onResume();
            return true;
        }
        if (url != null && (url.contains("/wap/MMyCartInfo.do?method=step3"))) {
            mFragment.onResume();
        }
        if (url != null && (url.startsWith("zhuxiao"))) {
            ConfigEntity configEntity = ConfigUtil.loadConfig(mFragment.getActivity());
            configEntity.key = "";
            ConfigUtil.saveConfig(mFragment.getActivity(), configEntity);
            WebSettings webSettings = view.getSettings();
            String ua = webSettings.getUserAgentString();
            ua = ua.substring(0, ua.indexOf("key=") + 4);
            webSettings.setUserAgentString(ua);
            view.loadUrl(ConfigUtil.HTTP_MINE_URL);
            return true;
        }
        if (url != null && (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:"))) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mFragment.startActivity(intent);
            return true;
        }
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//		Log.e("ssEnd",view.getTitle());
//		title_map.put(url,view.getTitle());
//		mFragment.setTopTitle(view.getTitle());
    }

}
