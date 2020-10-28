package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.WebSettings;
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
import com.shenmai.system.R;
import com.shenmai.system.UIApplication;
import com.shenmai.system.entity.ConfigEntity;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.http.OkHttpStack;
import com.shenmai.system.listener.TopBarClickListener;
import com.shenmai.system.utlis.AppManager;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StatusBarUtil;
import com.shenmai.system.utlis.StringUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/25.
 */
public class BaseActivity extends FragmentActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    public UIApplication application;
    public View btn_top_goback, btn_top_sidebar, emptyView, loadView;
    protected TextView top_title, tipTextView,top_right_title;
    public ImageView  img_top_goback,rightOne;
    public AppManager appManager;
    private Animation mRotateAnimation;
    public Handler mHandler = new MyHandler(this);
    private TopBarClickListener topBarClickListener;
    public ConfigEntity configEntity;
    private LinearLayout systemBar;
    public static UMShareAPI mShareAPI;
    public EditText top_title_search;

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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        application = (UIApplication) getApplication();
        appManager = AppManager.getInstance();
        appManager.PushActivity(this);
        /*if (!(this instanceof MainActivity)){
            appManager.PushActivity(this);
        }*/
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
        if(mShareAPI == null){
            mShareAPI = UMShareAPI.get(this);
        }
    }

    protected void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    /**初始化activity主view*/
    public void setMainView(){
        setContentView(R.layout.top_bar);
    }

    /**初始化顶部导航栏监听*/
    public void initListener(){
        topBarClickListener = new TopBarClickListener(this);
    }

    /**初始化顶部导航栏*/
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
//        img_top_icon = (ImageView) findViewById(R.id.img_top_icon);
        btn_top_sidebar = findViewById(R.id.btn_top_sidebar);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title_search = (EditText) findViewById(R.id.top_title_search);
        rightOne = (ImageView) findViewById(R.id.img_top_right_one);
//        rightTwo = (ImageView) findViewById(R.id.img_top_right_two);
        top_right_title = (TextView) findViewById(R.id.tv_top_right_text);
//
    }


    /**设置顶部导航左侧返回按钮显示*/
    public void setLeftBackButton(){
        btn_top_goback.setVisibility(View.VISIBLE);
//        btn_top_goback.setTag(TopClickUtil.TOP_BACK);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) top_title_search.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, R.id.btn_top_goback);
        params.leftMargin = 10;
        top_title_search.setLayoutParams(params);
    }

    /**
     * 是否显示返回按钮
     * @param isShow
     */
    public void setLeftBackShow(boolean isShow){
        if(isShow){
            btn_top_goback.setVisibility(View.VISIBLE);
        }else{
            btn_top_goback.setVisibility(View.GONE);
        }
    }

    /**设置右边图片按钮**/
    public void setTopRightImg(int idOne, int tagOne){
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
    }

    /**设置右边图片按钮是否显示**/
    public void isShowTopRightImg(boolean isShow){
        if(isShow){
            btn_top_sidebar.setVisibility(View.VISIBLE);
        } else {
            btn_top_sidebar.setVisibility(View.GONE);
        }
    }

    /**设置右边图片按钮的监听*/
    public void initTopRightImgListener(){
        rightOne.setOnClickListener(topBarClickListener);
    }


    /**设置标题名称*/
    protected void setTitle(String title){
        if(StringUtil.isEmpty(title)){
            top_title.setVisibility(View.GONE);
        } else {
            top_title.setVisibility(View.VISIBLE);
            top_title.setText(title);
            top_title_search.setVisibility(View.GONE);
        }
    }

    /**设置导航右边文字**/
    public void setTopRightTitle(String title, int topTag){
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        top_right_title.setTag(topTag);
        top_right_title.setText(title);
    }

    /**设置顶部导航栏监听*/
    public void initSideBarListener(){
        btn_top_goback.setOnClickListener(topBarClickListener);
        top_right_title.setOnClickListener(topBarClickListener);
        rightOne.setOnClickListener(topBarClickListener);
    }

    protected void onPause(){
        MobclickAgent.onPause(this);
        super.onPause();
        if (UIApplication.mRequestQueue != null) {
            UIApplication.mRequestQueue.cancelAll(this);
            UIApplication.mRequestQueue.getCache().clear();
        }
    }

    protected void onStop(){
        super.onStop();
    }

    /**
     * 控制dialog在activity消失前消失
     */
    protected void onDestroy(){
        super.onDestroy();
    }

    public void finish(){
        if (!(this instanceof MainActivity)){
            appManager.PopActivity();
        }
        super.finish();
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action){
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
    public void httpPostRequest(String url, final Map<String, String> params, final int action){
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
                    if(data instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) data;
                        String jsonString = jsonData.toJSONString();
                        httpResponse(jsonString, action);
                    } else if(data instanceof JSONArray){
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
//                ToastUtil.showToast(getResources().getString(R.string.netError), this, ToastUtil.DELAY_SHORT);
                break;
            case 404:
            case 400:
                Toast.makeText(this, getResources().getString(R.string.Error_404), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast(getResources().getString(R.string.Error_404), this, ToastUtil.DELAY_SHORT);
                break;
            default:
                Toast.makeText(this, "code = " + code + " message = " + arg3.getMessage(), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast("code = " + code + " message = " + arg3.getMessage(), this, ToastUtil.DELAY_SHORT);
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
        if(null != error){
            Toast.makeText(this, error.msg, Toast.LENGTH_SHORT).show();
//            ToastUtil.showToast(error.msg, this, ToastUtil.DELAY_SHORT);
        }
    }

    /**
     * 设置webUserAgent
     * @param webSettings
     * @return
     */
    protected WebSettings setUserAgent(WebSettings webSettings){
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

    /**
     * 友盟第三方分享
     *
     * 商品详情页面分享
     */
    public void shareAction(String title,String des,String imgUrl,String url) {
        ShareAction shareAction = new ShareAction(this);
        if (!StringUtil.isEmpty(title)) {
            shareAction.withTitle(title);
        }
        if(!StringUtil.isEmpty(des)){
            shareAction.withText(des);
        }
        if (!StringUtil.isEmpty(imgUrl)) {
            UMImage image = new UMImage(this, imgUrl);//网络图片
            shareAction.withMedia(image);
        }
        if (!StringUtil.isEmpty(url)) {
            shareAction.withTargetUrl(url);
        }
        shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     * 友盟第三方分享
     *
     * 首页轮播图
     *
     */
    public void shareAction(String des,String imgUrl,String url) {
        ShareAction shareAction = new ShareAction(this);
        shareAction.withTitle("神买商城");
        if(!StringUtil.isEmpty(des)){
            shareAction.withText(des);
        }
        if (!StringUtil.isEmpty(imgUrl)) {
            UMImage image = new UMImage(this, imgUrl);//网络图片
            shareAction.withMedia(image);
        }
        if (!StringUtil.isEmpty(url)) {
            shareAction.withTargetUrl(url);
        }
        shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     * 友盟第三方分享
     *
     * 店铺预览分享及店铺分享
     *
     */
    public void shareAction(String title,String url) {
        ShareAction shareAction = new ShareAction(this);
        if (!StringUtil.isEmpty(title)) {
            shareAction.withText(title).withTitle("神买商城");
        }
        UMImage image = new UMImage(this, R.mipmap.ic_launcher);//应用图片
        shareAction.withMedia(image);
        if (!StringUtil.isEmpty(url)) {
            shareAction.withTargetUrl(url);
        }
        shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setListenerList(umShareListener)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(BaseActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform.equals(SHARE_MEDIA.QQ) && !BaseActivity.this.mShareAPI.isInstall(BaseActivity.this, SHARE_MEDIA.QQ)) {
                Toast.makeText(BaseActivity.this, "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((platform.equals(SHARE_MEDIA.WEIXIN) || platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE))) &&
                    !BaseActivity.this.mShareAPI.isInstall(BaseActivity.this, SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(BaseActivity.this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(BaseActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(BaseActivity.this, "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 跳转到MainActivity界面或者LoginActivity界面
     */
    public void intentMainOrLogin() {
        if (ConfigUtil.loadConfig(getApplicationContext()).isLogin) {
            Intent intentActivity = new Intent(this,
                    MainActivity.class);
            startActivity(intentActivity);
            finish();
        } else {
            Intent intentActivity = new Intent(this,
                    LoginActivity.class);
            startActivity(intentActivity);
            finish();
        }
    }
}
