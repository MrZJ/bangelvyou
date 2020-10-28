package com.shishoureport.system.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hss01248.notifyutil.NotifyUtil;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.shishoureport.system.R;
import com.shishoureport.system.UIApplication;
import com.shishoureport.system.entity.ConfigEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.http.OkHttpStack;
import com.shishoureport.system.listener.TopBarClickListener;
import com.shishoureport.system.utils.AppManager;
import com.shishoureport.system.utils.CacheImgUtil;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.OpenApkfile;
import com.shishoureport.system.utils.StatusBarUtil;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.UpdateDialog;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;


public abstract class BaseActivity extends FragmentActivity {

    View topGoBack;
    TextView topTitle;
    LinearLayout systemBar;
    public UIApplication application;
    public AppManager appManager;
    public ConfigEntity configEntity;
    private String locationSrc;//文件路径
    public Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private final WeakReference<BaseActivity> mActivity;

        public MyHandler(BaseActivity activity) {

            mActivity = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {

            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().processMessage(msg);
        }
    }

    protected void processMessage(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        application = (UIApplication) getApplication();
        appManager = AppManager.getInstance();
        appManager.PushActivity(this);
        initView();
    }

    //获取布局文件
    public abstract int getLayoutId();

    public abstract void initView();

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
     * 初始化顶部导航栏
     */
    public void initTopView() {
        topGoBack = findViewById(R.id.btn_top_goback);
        topTitle = (TextView) findViewById(R.id.top_title);
        systemBar = (LinearLayout) findViewById(R.id.lin_system_bar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = StatusBarUtil.getStatusBarHeight(this);
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
    }

    /**
     * 初始化顶部导航栏监听
     */
    public void initTopListener() {
        TopBarClickListener topBarClickListener = new TopBarClickListener(this);
        topGoBack.setOnClickListener(topBarClickListener);
    }

    /**
     * 设置顶部标题
     *
     * @param str
     */
    public void setTopTitle(String str) {
        topTitle.setText(str);
    }

    protected void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
        MobclickAgent.onResume(this);//统计时长
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action) {
//        Log.e(TAG, url);
        if (UIApplication.mRequestQueue == null)
            UIApplication.mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
//        Log.e(TAG, url);
//        Log.e(TAG, params.toString());
        if (UIApplication.mRequestQueue == null)
            UIApplication.mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
//        Log.e(TAG, json);
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject == null) {
                return;
            }
            Object object = jsonObject.get("code");
            FieldErrors error = null;
            if (object instanceof String) {
                if ("200".equals(object.toString())) {
                    Object data = jsonObject.get("datas");
                    if (data instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) data;
                        String jsonString = jsonData.toJSONString();
                        httpResponse(jsonString, action);
                    } else if (data instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) data;
                        String jsonString = jSONArray.toJSONString();
                        httpResponse(jsonString, action);
                    }
                } else {
                    error = JSON.parseObject(json, FieldErrors.class);
                    if (error != null) {
                        httpError(error, action);
                    }
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
        switch (code) {
            case 0:
                Toast.makeText(this, getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
                break;
            case 404:
            case 400:
                Toast.makeText(this, getResources().getString(R.string.Error_404), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "code = " + code + " message = " + arg3.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (null != error) {
            Toast.makeText(this, error.msg, Toast.LENGTH_SHORT).show();
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
        if (!ua.contains("user_id=")) {
            webSettings.setUserAgentString(ua + ";user_id=" + configEntity.key);
        } else {
            ua = ua.substring(0, ua.indexOf("user_id=") + 8);
            webSettings.setUserAgentString(ua + configEntity.key);
        }
        return webSettings;
    }

    public void finish() {
        if (!(this instanceof MainActivity)) {
            appManager.PopActivity();
        }
        super.finish();
    }

    /**
     * 是否更新apk
     */
    public void isUpdateApk() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {

            }

            @Override
            public void onUpdateAvailable(String s) {
                final AppBean appBean = getAppBeanFromString(s);
                if (Integer.parseInt(appBean.getVersionCode()) >= getAPPVersionCode()) {
                    OpenHintUpdate(appBean.getDownloadURL(), appBean.getReleaseNote(), appBean.getVersionCode());
                }
            }
        });
    }

    /**
     * 提示用户更新
     */
    private void OpenHintUpdate(final String url, final String log, final String code) {
        final UpdateDialog updateDialog = new UpdateDialog(this);
        updateDialog.setMessage(log);
        updateDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }

        });
        updateDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(url)) {
                    locationSrc = CacheImgUtil.PATH_DATA_CACHE + "/"
                            + "shenmaishangcheng"
                            + code + ".apk";
                    File file = new File(locationSrc);
                    if (file.exists()) {
                        Intent intent = OpenApkfile.openFile(locationSrc);
                        startActivity(intent);
                    } else {
                        download(url);
                    }
                    updateDialog.dismiss();
                }
            }

        });
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
    }

    /**
     * 下载apk
     *
     * @param url
     */
    public void download(String url) {
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(locationSrc);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File file) {
                NotifyUtil.cancelAll();
                Intent intent = OpenApkfile.openFile(locationSrc);
                startActivity(intent);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (!StringUtil.isEmpty(throwable.getMessage())) {
                    Toast.makeText(BaseActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean b) {
                int progress = (int) (100 * current / total);
                NotifyUtil.buildProgress(102, R.mipmap.ic_launcher, "正在下载", progress, 100).setOnGoing().show();
            }
        });
    }

    /**
     * 获取apk的版本号 currentVersionCode
     */
    public int getAPPVersionCode() {
        int currentVersionCode = 1;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }

    /**
     * 跳转到MainActivity界面或者LoginActivity界面
     */
    public void intentMainOrLogin() {
        if (appManager.getCurrActivity().toString().contains("PushActivity")) {
            if (ConfigUtil.loadConfig(getApplicationContext()).isLogin) {
                IntentOtherActivity(MainActivity.class);
            } else {
            }
        }
        finish();
    }
    /**
     * 跳转封装
     *
     * @param activity
     */
    public void IntentOtherActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
