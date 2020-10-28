package com.basulvyou.system.ui.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.basulvyou.system.R;
import com.basulvyou.system.UIApplication;
import com.basulvyou.system.api.PayApi;
import com.basulvyou.system.entity.ConfigEntity;
import com.basulvyou.system.entity.FieldErrors;
import com.basulvyou.system.http.OkHttpStack;
import com.basulvyou.system.listener.TopBarClickListener;
import com.basulvyou.system.util.AppManager;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.OpenApkfile;
import com.basulvyou.system.util.StatusBarUtil;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.util.WifiNetworkUtil;
import com.basulvyou.system.wibget.UpdateDialog;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/25.
 */
public class BaseActivity extends FragmentActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    public UIApplication application;
    public View btn_top_goback, btn_top_sidebar, emptyView, loadView;
    protected TextView top_title, top_right_title, tipTextView;
    private ImageView img_top_goback;
    private ImageView img_top_icon;
    public ImageView rightOne;
    public ImageView rightTwo;
    public EditText top_title_search;
    public AppManager appManager;
    private Animation mRotateAnimation;
    public Handler mHandler = new MyHandler(this);
    //    public RequestQueue mRequestQueue;
    private TopBarClickListener topBarClickListener;
    public ConfigEntity configEntity;
    private LinearLayout systemBar;
    private boolean isCurrent = true;
    private static int NOTIFICATION_ID = 111;
    private String locationSrc;//文件路径
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private boolean isFirstConnectWifi;
    public boolean isHandUpdateApk = false;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
        application = (UIApplication) getApplication();
        appManager = AppManager.getInstance();
        if (!(this instanceof MainActivity)) {
            appManager.PushActivity(this);
        }
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
        isCurrent = true;
    }

    /**
     * 初始化activity主view
     */
    public void setMainView() {
        setContentView(R.layout.top_bar);
    }

    /**
     * 初始化顶部导航栏监听
     */
    public void initListener() {
        topBarClickListener = new TopBarClickListener(this);
    }

    /**
     * 初始化顶部导航栏
     */
    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    public void initTopView() {
        systemBar = (LinearLayout) findViewById(R.id.lin_system_bar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = StatusBarUtil.getStatusBarHeight(this);
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
        btn_top_goback = findViewById(R.id.btn_top_goback);
        img_top_goback = (ImageView) findViewById(R.id.img_top_goback);
        img_top_icon = (ImageView) findViewById(R.id.img_top_icon);
        btn_top_sidebar = findViewById(R.id.btn_top_sidebar);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title_search = (EditText) findViewById(R.id.top_title_search);
        rightOne = (ImageView) findViewById(R.id.img_top_right_one);
        rightTwo = (ImageView) findViewById(R.id.img_top_right_two);
        top_right_title = (TextView) findViewById(R.id.tv_top_right_text);

        /**数据加载布局*/
        emptyView = findViewById(R.id.empty_view);
        tipTextView = (TextView) findViewById(R.id.tip_text);
        loadView = findViewById(R.id.loading_img);

        if (null != emptyView) {
            View view = findViewById(R.id.loading_layout);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

            if (null != params) {
                DisplayMetrics displayMetrics = getResources()
                        .getDisplayMetrics();

                int height = displayMetrics.heightPixels;
                params.topMargin = (int) (height * 0.25);
                view.setLayoutParams(params);
            }
            mRotateAnimation = AsynImageUtil.mRotateAnimation;

            mRotateAnimation
                    .setInterpolator(AsynImageUtil.ANIMATION_INTERPOLATOR);
            mRotateAnimation
                    .setDuration(AsynImageUtil.ROTATION_ANIMATION_DURATION_SHORT);
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setRepeatMode(Animation.RESTART);
        }
    }

    /**
     * 设置顶部导航左侧返回按钮显示
     */
    public void setLeftBackButton() {
        btn_top_goback.setVisibility(View.VISIBLE);
        btn_top_goback.setTag(TopClickUtil.TOP_BACK);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) top_title_search.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, R.id.btn_top_goback);
        params.leftMargin = 10;
        top_title_search.setLayoutParams(params);
    }

    /**
     * 是否显示返回按钮
     *
     * @param isShow
     */
    public void setBackShow(boolean isShow) {
        if (isShow) {
            btn_top_goback.setVisibility(View.VISIBLE);
        } else {
            btn_top_goback.setVisibility(View.GONE);
        }
    }

    /**
     * 设置顶部导航左侧按钮显示设定的图标,并且给顶部按钮设置tag
     *
     * @param clickTag:按钮设置tag，值在TopClickUtil工具类中设置
     */
    public void setLeftImgButton(int id, int clickTag) {
        btn_top_goback.setVisibility(View.VISIBLE);
        btn_top_goback.setTag(clickTag);
        img_top_goback.setVisibility(View.GONE);
        img_top_icon.setVisibility(View.VISIBLE);
        img_top_icon.setBackgroundResource(id);
    }

    /**
     * 设置导航右边文字
     **/
    public void setTopRightTitle(String title, int topTag) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        if (topTag == TopClickUtil.TOP_NOT) {
            int Nend = title.indexOf("封");
            SpannableStringBuilder style = new SpannableStringBuilder(title);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFAE2D")), 0, Nend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            top_right_title.setTag(topTag);
            top_right_title.setText(style);
        } else {
            top_right_title.setTag(topTag);
            top_right_title.setText(title);
        }
    }

    /**
     * 设置导航右边文字
     **/
    public void setTopRightTitleAdd(String addr, int topTag) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        top_right_title.setTextSize(12);
        top_right_title.setTag(topTag);
        top_right_title.setText(addr);
    }
    public void setTopRightTitleClick(View.OnClickListener click){
        top_right_title.setOnClickListener(click);
    }

    /**
     * 设置导航右边文字和图片
     **/
    public void setTopRightTitleAndImg(String title, int topTag, int idOne, int tagOne) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setBackgroundResource(idOne);
        top_right_title.setTag(topTag);
        top_right_title.setText(title);
    }

    /**
     * 隐藏导航右边
     **/
    public void hideTopRight() {
        btn_top_sidebar.setVisibility(View.GONE);
    }

    /**
     * 设置标题名称
     */
    protected void setTitle(String title) {
        if ("".equals(title)) {
            top_title_search.setVisibility(View.VISIBLE);
            top_title.setVisibility(View.GONE);
        } else {
            top_title_search.setVisibility(View.GONE);
            top_title.setVisibility(View.VISIBLE);
            top_title.setText(title);
        }
    }

    /**
     * 设置右边图片按钮
     **/
    public void setTopRightImg(int idOne, int tagOne) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
    }

    /**
     * 设置右边图片按钮
     **/
    public void setTopRightImg(int idOne, int tagOne, int idTwo) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightTwo.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
        rightTwo.setBackgroundResource(idTwo);
    }

    /**
     * 设置顶部导航栏监听
     */
    public void initSideBarListener() {
        btn_top_sidebar.setOnClickListener(topBarClickListener);
        btn_top_goback.setOnClickListener(topBarClickListener);
        top_title_search.setOnClickListener(topBarClickListener);
        rightOne.setOnClickListener(topBarClickListener);
        rightTwo.setOnClickListener(topBarClickListener);
        top_right_title.setOnClickListener(topBarClickListener);

    }

    protected void onPause() {
        application.mRequestQueue.cancelAll(this);
        MobclickAgent.onPause(this);
        isCurrent = false;
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    /**
     * 控制dialog在activity消失前消失
     */
    protected void onDestroy() {
        super.onDestroy();
    }

    public void finish() {
        if (!(this instanceof MainActivity)) {
            appManager.PopActivity();
        }
        super.finish();
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action) {
        Log.e(TAG, url);
        if (application.mRequestQueue == null)
            application.mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
        application.mRequestQueue.add(stringRequest);
    }

    /**
     * post请求
     */
    public void httpPostRequest(String url, final Map<String, String> params, final int action) {
        Log.e(TAG, url);
//        Log.e(TAG, params.toString());
        if (application.mRequestQueue == null)
            application.mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
        application.mRequestQueue.add(stringRequest);
    }

    /**
     * 返回数据处理
     *
     * @param json
     * @param action
     */
    public void httpOnResponse(String json, int action) {

        /*if (dialog != null) {
            dialog.dismiss();
        }*/
        hiddenLoading();
        Log.e(TAG, json);
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

        /*if (dialog != null) {
            dialog.dismiss();
        }*/
        hiddenLoading();
        switch (code) {
            case 0:
                Toast.makeText(this, getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast(getResources().getString(R.string.netError), this, ToastUtil.DELAY_SHORT);
                break;
            case 404:
            case 400:
                Toast.makeText(this, getResources().getString(R.string.Error_404), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast(getResources().getString(R.string.Error_404), this, ToastUtil.DELAY_SHORT);
                break;
            default:
//                Toast.makeText(this, "code = " + code + " message = " + arg3.getMessage(), Toast.LENGTH_SHORT).show();
                /*ToastUtil.showToast("code = " + code + " message = " + arg3.getMessage()
                        , this, ToastUtil.DELAY_SHORT);*/
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
//            ToastUtil.showToast(error.msg, this, ToastUtil.DELAY_SHORT);
        }
    }

    /**
     * 显示加载布局
     */
    public void showLoading(final String msg, final boolean load) {
        mHandler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText(msg);
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
//                    if (load) {
//                        loadView.setVisibility(View.VISIBLE);
//                        loadView.startAnimation(mRotateAnimation);
//                    } else {
//                        loadView.clearAnimation();
//                        loadView.setVisibility(View.INVISIBLE);
//                    }
                }
            }
        });

    }

    /**
     * dimiss加载布局
     */
    public void hiddenLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });

    }

    /**
     * 微信是否支持
     */
    public void isWeixinSupported(IWXAPI msgApi, String info) {
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (isPaySupported) {
            if (null != info) {
                getWeiXinPayInfo(info);
            }
        } else {
            Toast.makeText(this, "未安装微信或者微信版本过低", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求微信支付参数
     */
    private void getWeiXinPayInfo(String id) {
        httpGetRequest(PayApi.getWeiXinPayInfo(id),
                PayApi.API_GET_WEIXIN_PAY_INFO);
    }

    /**
     * 是否更新apk
     */
    public void isUpdateApk() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                if (isHandUpdateApk) {
                    Toast.makeText(BaseActivity.this, "当前为最新版本", Toast.LENGTH_SHORT).show();
                }
                setBtnUpdateClickable();
            }

            @Override
            public void onUpdateAvailable(String s) {
                final AppBean appBean = getAppBeanFromString(s);
                if (Integer.parseInt(appBean.getVersionCode()) > getAppVersionCode()) {
                    if (isCurrent) {
                        OpenHintUpdate(appBean.getDownloadURL(), appBean.getReleaseNote(), appBean.getVersionCode());
                    }
                } else {
                    if (isHandUpdateApk) {
                        Toast.makeText(BaseActivity.this, "当前为最新版本", Toast.LENGTH_SHORT).show();
                    }
                    setBtnUpdateClickable();
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
                setBtnUpdateClickable();
            }

        });
        updateDialog.setOnPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(url)) {
                    locationSrc = CacheImgUtil.PATH_DATA_CACHE + "/"
                            + "basulvyou"
                            + code + ".apk";
                    File file = new File(locationSrc);
                    if (file.exists()) {
                        clear();
                        Intent intent = OpenApkfile.openFile(locationSrc);
                        startActivity(intent);
                        setBtnUpdateClickable();
                    } else {
                        boolean connect = WifiNetworkUtil.OpenNetworkSetting(BaseActivity.this);
                        if (!connect && !isFirstConnectWifi) {
                            updateDialog.dismiss();
                            setBtnUpdateClickable();
                            isFirstConnectWifi = true;
                            return;
                        }
                        download(url);
                    }
                    updateDialog.dismiss();
                }
            }

        });

        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        updateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setBtnUpdateClickable();
            }
        });
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
                complateDownload();
                setBtnUpdateClickable();
                ConfigUtil.IS_DOWNLOAD = false;
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (!StringUtil.isEmpty(throwable.getMessage())) {
                    Toast.makeText(BaseActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    manager.cancel(NOTIFICATION_ID);
                    ConfigUtil.IS_DOWNLOAD = false;
                    setBtnUpdateClickable();
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
                startDownload();
            }

            @Override
            public void onLoading(long total, long current, boolean b) {
                try {
                    ConfigUtil.IS_DOWNLOAD = true;
                    int progress = (int) (100 * current / total);
                    downLoading(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化通知
     */
    public void initNotification() {
        manager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                // .setDefaults(Notification.DEFAULT_LIGHTS)//
                // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher);

    }

    /**
     * 清除通知
     */
    public void clear() {
        if (manager != null) {
            manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(NOTIFICATION_ID);
        }
    }

    /**
     * 开始下载通知
     */
    public void startDownload() {
        clear();
        initNotification();
        builder.setContentText("开始下载...").setContentTitle("下载");
        Notification n = builder.build();
        manager.notify(NOTIFICATION_ID, n);
    }

    /**
     * 正在下载的通知
     *
     * @param progress 进度
     * @throws InterruptedException
     */
    public void downLoading(int progress) throws InterruptedException {
        builder.setContentText("正在下载:" + progress + "%")
                .setProgress(100, progress, false).setContentTitle("下载中");
        Notification n1 = builder.build();
        manager.notify(NOTIFICATION_ID, n1);
        if (android.os.Build.VERSION.SDK_INT > 19) {
            Thread.sleep(50);
        }
    }

    /**
     * 完成下载通知
     */
    public void complateDownload() {
        clear();
        initNotification();
        builder.setContentText("下载完成	").setContentTitle("下载")
                .setLights(Color.BLUE, 1000, 1000).setAutoCancel(true);
        if (locationSrc != null) {
            Intent intent = OpenApkfile.openFile(locationSrc);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(BaseActivity.this, 0, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
            locationSrc = null;
        }
        Notification n2 = builder.build();
        manager.notify(NOTIFICATION_ID, n2);
    }

    public void setBtnUpdateClickable() {
    }

    /**
     * 获取软件当前版本信息
     *
     * @return
     */
    private int getAppVersionCode() {
        try {
            String pkName = this.getPackageName();
            int versionCode = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {

        }
        return 1;
    }
}
