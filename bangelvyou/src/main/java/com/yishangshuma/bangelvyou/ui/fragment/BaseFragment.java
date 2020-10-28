package com.yishangshuma.bangelvyou.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yishangshuma.bangelvyou.entity.ConfigEntity;
import com.yishangshuma.bangelvyou.entity.FieldErrors;
import com.yishangshuma.bangelvyou.http.OkHttpStack;
import com.yishangshuma.bangelvyou.listener.TopBarClickListener;
import com.yishangshuma.bangelvyou.ui.activity.BaseActivity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.ToastUtil;

import java.util.Map;

/**
 * Created by KevinLi on 2016/1/27.
 */
public class BaseFragment extends Fragment
{
    public static final String TAG = BaseFragment.class.getSimpleName();
    private View topLeft, topRight, emptyView, loadView;
    private ImageView topBack, topIcon, rightOne, rightTwo;
    private EditText topSearch;
    protected TextView top_title, tipTextView;
    private TopBarClickListener topBarClickListener;
    private Animation mRotateAnimation;
    public RequestQueue mRequestQueue;
    public ConfigEntity configEntity;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mRequestQueue = Volley.newRequestQueue(activity, new OkHttpStack());
        configEntity = ConfigUtil.loadConfig(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.top_bar, null);
        return view;
    }

    /**
     * 加载顶部标题栏
     */
    public void initTopView(View mView){
        topLeft = mView.findViewById(R.id.btn_top_goback);
        topBack = (ImageView) mView.findViewById(R.id.img_top_goback);
        topIcon = (ImageView) mView.findViewById(R.id.img_top_icon);
        topSearch = (EditText) mView.findViewById(R.id.top_title_search);
        top_title = (TextView) mView.findViewById(R.id.top_title);
        topRight = mView.findViewById(R.id.btn_top_sidebar);
        rightOne = (ImageView) mView.findViewById(R.id.img_top_right_one);
        rightTwo = (ImageView) mView.findViewById(R.id.img_top_right_two);

        /**数据加载布局*/
        emptyView = mView.findViewById(R.id.empty_view);
        tipTextView = (TextView) mView.findViewById(R.id.tip_text);
        loadView = mView.findViewById(R.id.loading_img);

        if (null != emptyView) {
            View view = mView.findViewById(R.id.loading_layout);
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
     * 设置顶部标题栏标志,并设置点击事件标签
     * @param id
     */
    public void setLogoShow(int id, int clickTag){
        topLeft.setTag(clickTag);
        topBack.setVisibility(View.GONE);
        topIcon.setVisibility(View.VISIBLE);
        topIcon.setBackgroundResource(id);
    }

    /**设置搜索框显示位置*/
    public void setSearchSeat(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topSearch.getLayoutParams();
        params.leftMargin = 10;
        params.rightMargin = 30;
        topSearch.setLayoutParams(params);
    }

    /**设置标题名称*/
    protected void setTitle(String title){
        if("".equals(title)){
            topSearch.setVisibility(View.VISIBLE);
            top_title.setVisibility(View.GONE);
        } else {
            topSearch.setVisibility(View.GONE);
            top_title.setVisibility(View.VISIBLE);
            top_title.setText(title);
        }
    }

    /**设置右边图片按钮**/
    public void setTopRightImg(int idOne, int tagOne){
        topRight.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setVisibility(View.VISIBLE);
        rightOne.setBackgroundResource(idOne);
    }

    /**设置右边图片按钮**/
    public void setTopRightImg(int idOne, int tagOne, int idTwo){
        topRight.setVisibility(View.VISIBLE);
        rightOne.setVisibility(View.VISIBLE);
        rightTwo.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightOne.setBackgroundResource(idOne);
        rightTwo.setBackgroundResource(idTwo);
    }

    /**顶部所有按钮的点击事件注册**/
    public void initTopListener(){
        if(getActivity() == null){
            return;
        }
        topBarClickListener = new TopBarClickListener(getActivity());
        topSearch.setOnTouchListener(topBarClickListener);
        topLeft.setOnClickListener(topBarClickListener);
        rightOne.setOnClickListener(topBarClickListener);
        rightTwo.setOnClickListener(topBarClickListener);
    }

    /**顶部所有搜索的点击事件注册**/
    public void initSearchListener(){
        if(getActivity() == null){
            return;
        }
        topBarClickListener = new TopBarClickListener(getActivity());
        topSearch.setOnTouchListener(topBarClickListener);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    /**
     * 控制dialog在activity消失前消失
     */
    public void onDestroy(){
        //disMissDialog();
        ToastUtil.dismiss();
        super.onDestroy();
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action){
        Log.e(TAG, url);
        if(getActivity() == null){
            return;
        }
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getActivity(), new OkHttpStack());
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
        if(getActivity() == null){
            return;
        }
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getActivity(), new OkHttpStack());
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
        if(getActivity() == null){
            return;
        }
        hiddenLoading();
        switch (code) {
            case 0:
                ToastUtil.showToast(getResources().getString(R.string.netError), getActivity(), ToastUtil.DELAY_SHORT);
                break;
            case 404:
            case 400:
                ToastUtil.showToast(getResources().getString(R.string.Error_404), getActivity(), ToastUtil.DELAY_SHORT);
                break;
            default:
                ToastUtil.showToast("code = " + code + " message = " + arg3.getMessage()
                        , getActivity(), ToastUtil.DELAY_SHORT);
                break;
        }
        httpError(null,action);
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
        if(getActivity() == null){
            return;
        }
        if(null != error){
            ToastUtil.showToast(error.msg, getActivity(), ToastUtil.DELAY_SHORT);
        }
    }

    /**显示加载布局*/
    public void showLoading(final String msg, final boolean load) {
        if(getActivity() == null){
            return;
        }
        ((BaseActivity) getActivity()).mHandler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText(msg);
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
//                    if (load) {
//                        Log.e("ss","xians3");
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
        if(getActivity() == null){
            return;
        }
        ((BaseActivity) getActivity()).mHandler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });

    }
}
