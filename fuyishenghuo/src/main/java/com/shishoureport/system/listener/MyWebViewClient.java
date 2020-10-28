package com.shishoureport.system.listener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shishoureport.system.UIApplication;
import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.http.OkHttpStack;
import com.shishoureport.system.pay.PayFactory;
import com.shishoureport.system.pay.PayInterface;
import com.shishoureport.system.pay.PayResult;
import com.shishoureport.system.ui.fragment.BaseFragment;
import com.shishoureport.system.utils.ConfigUtil;
import com.umeng.message.PushAgent;
//import com.umeng.message.UmengRegistrar;

import java.lang.ref.WeakReference;
import java.util.Map;


public class MyWebViewClient extends WebViewClient implements PayInterface.AliPayCallBack {

    private Map<String, Object> title_map;
    private String config_url;
    private WeakReference<BaseFragment> mRf;
    private BaseFragment mFragment;
    private String startUrl;

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
        startUrl = url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mFragment = mRf.get();
        if (mFragment == null) {
            return false;
        }
        if (TextUtils.isEmpty(startUrl) || startUrl.equals(url)) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        //截取分享地址
        if (url != null && (url.contains("share"))) {
            String info[] = url.split(":");
            for (int i = 0; i < info.length; i++) {
                Log.e("info", "info=====" + info[i]);
            }
        }
        if (url != null && (url.contains("objc"))) {
            String info[] = url.split(":");
            ConfigEntity configEntity = ConfigUtil.loadConfig(mFragment.getActivity());
            configEntity.key = info[1];
            ConfigUtil.saveConfig(mFragment.getActivity(), configEntity);
            StringBuilder add_dev = new StringBuilder(ConfigUtil.HTTP_ADDD_DEVICE);
            add_dev.append("&id=").append(configEntity.key).append("&device=android").append("&device_tokens=").append(PushAgent.getInstance(mFragment.getActivity()).getRegistrationId());
            if (UIApplication.mRequestQueue == null)
                UIApplication.mRequestQueue = Volley.newRequestQueue(mFragment.getActivity(), new OkHttpStack());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, add_dev.toString(),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String content) {
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                }
            });
            Log.e("jianzhang", add_dev.toString());
            UIApplication.mRequestQueue.add(stringRequest);
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
        if (url != null && (url.contains("alipay"))) {
            Log.e("alipay", url);
            String aliPayStr = url.substring(url.indexOf("alipay_sdk"));
            Log.e("aliPayStr", aliPayStr);
            payAli(aliPayStr);
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
        mFragment = mRf.get();
        if (mFragment == null) {
            return;
        }
        mFragment.getWebView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

    }

    public void payAli(String payInfo) {
        PayInterface payInterface = PayFactory.getPay(mFragment.getActivity(), PayFactory.PAY_TYPE_ALI);
        payInterface.onPay(payInfo);
        payInterface.setPayCallBack(this);
    }

    @Override
    public void onPayResult(String result) {
        Log.e("onPayResult", "result" + result);
        PayResult payResult = new PayResult(result);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultStatus = payResult.getResultStatus();
        Log.e("onPayResult", "resultStatus==" + resultStatus);
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
//            AliPayResult aliPayResult = JSONObject.parseObject(payResult.getResult(), AliPayResult.class);
//            AliCheckRequest request = new AliCheckRequest(token, aliPayResult.alipay_trade_app_pay_response.out_trade_no,
//                    aliPayResult.alipay_trade_app_pay_response.total_amount,
//                    aliPayResult.alipay_trade_app_pay_response.seller_id,
//                    aliPayResult.alipay_trade_app_pay_response.app_id,
//                    aliPayResult.sign,
//                    aliPayResult.sign_type);
//            httpGetRequest(request.getUrl(), AliCheckRequest.ALI_CHECK_REQUEST);
            Toast.makeText(mFragment.getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
//            finish();
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(mFragment.getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
//                finish();
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(mFragment.getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }
}
