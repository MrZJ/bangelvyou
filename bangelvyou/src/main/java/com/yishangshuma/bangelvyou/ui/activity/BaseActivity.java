package com.yishangshuma.bangelvyou.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.UIApplication;
import com.yishangshuma.bangelvyou.entity.ConfigEntity;
import com.yishangshuma.bangelvyou.entity.FieldErrors;
import com.yishangshuma.bangelvyou.http.OkHttpStack;
import com.yishangshuma.bangelvyou.listener.TopBarClickListener;
import com.yishangshuma.bangelvyou.util.AppManager;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.ToastUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

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
    private ImageView  img_top_goback;
    private ImageView img_top_icon;
    public ImageView rightOne;
    public ImageView rightTwo;
    public EditText top_title_search;
    public AppManager appManager;
    private Animation mRotateAnimation;
    public Handler mHandler = new MyHandler(this);
    public RequestQueue mRequestQueue;
    private TopBarClickListener topBarClickListener;
    public ConfigEntity configEntity;

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
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
        application = (UIApplication) getApplication();
        appManager = AppManager.getInstance();
        if (!(this instanceof MainActivity)){
            appManager.PushActivity(this);
        }
        configEntity = ConfigUtil.loadConfig(getApplicationContext());
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

    /**设置顶部导航左侧返回按钮显示*/
    public void setLeftBackButton(){
        btn_top_goback.setVisibility(View.VISIBLE);
        btn_top_goback.setTag(TopClickUtil.TOP_BACK);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) top_title_search.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, R.id.btn_top_goback);
        params.leftMargin = 10;
        top_title_search.setLayoutParams(params);
    }

    /**
     * 是否显示返回按钮
     * @param isShow
     */
    public void setBackShow(boolean isShow){
        if(isShow){
            btn_top_goback.setVisibility(View.VISIBLE);
        }else{
            btn_top_goback.setVisibility(View.GONE);
        }
    }

    /**
     * 设置顶部导航左侧按钮显示设定的图标,并且给顶部按钮设置tag
     * @param clickTag:按钮设置tag，值在TopClickUtil工具类中设置
     */
    public void setLeftImgButton(int id, int clickTag){
        btn_top_goback.setVisibility(View.VISIBLE);
        btn_top_goback.setTag(clickTag);
        img_top_goback.setVisibility(View.GONE);
        img_top_icon.setVisibility(View.VISIBLE);
        img_top_icon.setBackgroundResource(id);
    }

    /**设置导航右边文字**/
    public void setTopRightTitle(String title, int topTag){
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        if(topTag == TopClickUtil.TOP_NOT){
            int Nend = title.indexOf("封");
            SpannableStringBuilder style = new SpannableStringBuilder(title);
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FFAE2D")), 0, Nend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            top_right_title.setTag(topTag);
            top_right_title.setText(style);
        }else{
            top_right_title.setTag(topTag);
            top_right_title.setText(title);
        }
    }

    /**设置标题名称*/
    protected void setTitle(String title){
        if("".equals(title)){
            top_title_search.setVisibility(View.VISIBLE);
            top_title.setVisibility(View.GONE);
        } else {
            top_title_search.setVisibility(View.GONE);
            top_title.setVisibility(View.VISIBLE);
            top_title.setText(title);
        }
    }

    /**设置右边图片按钮**/
    public void setTopRightImg(int idOne, int tagOne){
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
    }

    /**设置右边图片按钮**/
    public void setTopRightImg(int idOne,int tagOne, int idTwo){
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightTwo.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
        rightTwo.setBackgroundResource(idTwo);
    }

    /**设置顶部导航栏监听*/
    public void initSideBarListener(){
        btn_top_sidebar.setOnClickListener(topBarClickListener);
        btn_top_goback.setOnClickListener(topBarClickListener);
        top_title_search.setOnClickListener(topBarClickListener);
        rightOne.setOnClickListener(topBarClickListener);
        rightTwo.setOnClickListener(topBarClickListener);
        top_right_title.setOnClickListener(topBarClickListener);

    }
    protected void onPause(){
        mRequestQueue.cancelAll(this);
        MobclickAgent.onPause(this);
        super.onPause();
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
        Log.e(TAG, url);
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
        mRequestQueue.add(stringRequest);
    }

    /**
     * post请求
     */
    public void httpPostRequest(String url, final Map<String, String> params, final int action){
        Log.e(TAG, url);
        Log.e(TAG, params.toString());
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
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
        mRequestQueue.add(stringRequest);
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

        /*if (dialog != null) {
            dialog.dismiss();
        }*/
        hiddenLoading();
        switch (code) {
            case 0:
                ToastUtil.showToast(getResources().getString(R.string.netError), this, ToastUtil.DELAY_SHORT);
                break;
            case 404:
            case 400:
                ToastUtil.showToast(getResources().getString(R.string.Error_404), this, ToastUtil.DELAY_SHORT);
                break;
            default:
                ToastUtil.showToast("code = " + code + " message = " + arg3.getMessage()
                        , this, ToastUtil.DELAY_SHORT);
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
            ToastUtil.showToast(error.msg, this, ToastUtil.DELAY_SHORT);
        }
    }

    /**显示加载布局*/
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

    /**dimiss加载布局*/
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
}