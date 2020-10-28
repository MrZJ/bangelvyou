package com.shishoureport.system.ui.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shishoureport.system.R;
import com.shishoureport.system.UIApplication;
import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.http.OkHttpStack;
import com.shishoureport.system.listener.TopBarClickListener;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.LoadingDialog;
import com.shishoureport.system.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;


/**
 * fragment基类
 */
public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    protected View rootView;
    View topGoBack;
    TextView topTitle;
    LinearLayout systemBar;
    View rightSide;
    ImageView commonImg;
    private TopBarClickListener topBarClickListener;
    public RequestQueue mRequestQueue;
    public ConfigEntity configEntity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        initTitle(rootView);
        if (rootView == null) {
            return null;
        }
        initTopView();
        initView(rootView);
        return rootView;
    }

    private void initTitle(View v) {
        topGoBack = v.findViewById(R.id.btn_top_goback);
        topTitle = (TextView) v.findViewById(R.id.top_title);
        systemBar = (LinearLayout) v.findViewById(R.id.lin_system_bar);
        rightSide = v.findViewById(R.id.btn_top_sidebar);
        commonImg = (ImageView) v.findViewById(R.id.img_top_right_one);
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化view
    protected abstract void initView(View v);

    /**
     * 设置顶部导航左侧按钮显示返回按钮
     */
    public void setLeftBackButton(boolean show) {
        if (show) {
            topGoBack.setVisibility(View.VISIBLE);
        } else {
            topGoBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边搜索按钮
     */
    public void setRightSide(int resId) {
        rightSide.setVisibility(View.VISIBLE);
        commonImg.setBackgroundResource(resId);
    }

    /**
     * 隐藏右边按钮
     */
    public void hideRightSide() {
        rightSide.setVisibility(View.GONE);
    }

    /**
     * 隐藏右边按钮
     */
    public void showRightSide() {
        rightSide.setVisibility(View.VISIBLE);
    }

    /**
     * 设置顶部标题
     *
     * @param str
     */
    public void setTopTitle(String str) {
        topTitle.setText(str);
    }

    /**
     * 加载顶部标题栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initTopView() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int sysbarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
//            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
//            sysBarParams.height = sysbarHeight;
//            systemBar.setLayoutParams(sysBarParams);
//        }
    }

    /**
     * 顶部所有按钮的点击事件注册
     **/
    public void initTopListener() {
        if (getActivity() == null) {
            return;
        }
        topBarClickListener = new TopBarClickListener(getActivity());
        topGoBack.setOnClickListener(topBarClickListener);
    }


    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(getActivity());
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    /**
     * 创建一个进度条
     *
     * @return
     */
    public ProgressBar getProgressBar() {
        ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 8));
        progressBar.setProgressDrawable(getActivity().getResources().getDrawable(
                R.drawable.progress_drawable));
        return progressBar;
    }

    public WebView getWebView() {
        WebView webView = new WebView(getActivity());
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        webView.setVerticalScrollBarEnabled(false);
        return webView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action) {
        Log.e(TAG, url);
        if (getActivity() == null) {
            return;
        }
        if (UIApplication.mRequestQueue == null)
            UIApplication.mRequestQueue = Volley.newRequestQueue(getActivity(), new OkHttpStack());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String content) {
                        httpOnResponse(content, action);
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                errorResponseHandler(error.networkResponse == null ? 0
                                : error.networkResponse.statusCode, error,
                        action);
            }
        });
        UIApplication.mRequestQueue.add(stringRequest);
    }

    /**
     * post请求
     */
    public void httpPostRequest(String url, final Map<String, String> params, final int action) {
        Log.e(TAG, url);
        Log.e(TAG, params.toString());
        if (getActivity() == null) {
            return;
        }
        if (UIApplication.mRequestQueue == null)
            UIApplication.mRequestQueue = Volley.newRequestQueue(getActivity(), new OkHttpStack());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String content) {
                        httpOnResponse(content, action);
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                errorResponseHandler(error.networkResponse == null ? 0
                                : error.networkResponse.statusCode, error,
                        action);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        UIApplication.mRequestQueue.add(stringRequest);
    }

    /**
     * 返回数据处理
     *
     * @param json
     * @param action
     */
    public void httpOnResponse(String json, int action) {
        Log.e(TAG, json);
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject == null) {
                return;
            }
            Object object = jsonObject.get("code");
            FieldErrors error = null;
            if ("200".equals(object.toString())) {
                Object data = jsonObject.get("data");
                if (data != null) {
                    httpResponse(data.toString(), action);
                } else {
                    httpResponse(null, action);
                }
            } else {
                error = JSON.parseObject(json, FieldErrors.class);
                if (error != null) {
                    httpError(error, action);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求异常
     *
     * @param code
     * @param arg3
     * @param action
     */
    public void errorResponseHandler(int code, Throwable arg3, int action) {
        if (getActivity() == null) {
            return;
        }
        switch (code) {
            case 0:
                Toast.makeText(getActivity(), getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
                break;
            case 404:
            case 400:
                Toast.makeText(getActivity(), getResources().getString(R.string.Error_404), Toast.LENGTH_SHORT).show();
                break;
            default:
//                Toast.makeText(getActivity(), "code = " + code + " message = " + arg3.getMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
        httpError(null, action);
    }

    /**
     * 返回数据
     *
     * @param json
     * @param action
     */
    protected void httpResponse(String json, int action) {

    }

    /**
     * 返回Error对象
     *
     * @param error
     * @param action
     */
    protected void httpError(FieldErrors error, int action) {
        if (getActivity() == null) {
            return;
        }
        if (null != error && error.msg != null) {
            showToast(error.msg);
        } else {
            showToast("请求失败，请重试");
        }
    }

    /**
     * 设置webUserAgent
     *
     * @param webSettings
     * @return
     */
    protected WebSettings setUserAgent(WebSettings webSettings) {
        String ua = webSettings.getUserAgentString();
        // 不能设置多个user_id!
        configEntity = ConfigUtil.loadConfig(getActivity());
        if (!StringUtil.isEmpty(configEntity.key)) {
            if (!ua.contains("user_id=")) {
                webSettings.setUserAgentString(ua + ";user_id=" + configEntity.key);
            } else {
                ua = ua.substring(0, ua.indexOf("user_id=") + 8);
                webSettings.setUserAgentString(ua + configEntity.key);
            }
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        return webSettings;
    }


    public void showToast(String toast) {
        if (!StringUtil.isEmpty(toast)) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int toast) {
        Toast.makeText(getActivity(), getString(toast), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {

        }
    }

    public void loadData() {
    }

    /**
     * 显示Dialog
     */
    private Dialog mLoadingDialog;

    public void showDialog() {
        if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
            closeDialog();
            mLoadingDialog = LoadingDialog.createLoadingDialog(getActivity(), "正在加载中...");
            mLoadingDialog.show();
        }
    }

    /**
     * 关闭Dialog
     */
    public void closeDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
