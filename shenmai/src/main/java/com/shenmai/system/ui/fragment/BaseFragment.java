package com.shenmai.system.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.shenmai.system.R;
import com.shenmai.system.UIApplication;
import com.shenmai.system.entity.ConfigEntity;
import com.shenmai.system.entity.FieldErrors;
import com.shenmai.system.http.OkHttpStack;
import com.shenmai.system.listener.TopBarClickListener;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * Created by KevinLi on 2016/1/27.
 */
public class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    private View btn_top_sidebar, emptyView, loadView, rl_parent, top_line;
    public View topLeft;
    private ImageView topIcon, rightOne, rightTwo;
    public ImageView topBack;
    protected TextView top_title, tipTextView, top_right_title;
    private TopBarClickListener topBarClickListener;
    private Animation mRotateAnimation;
    public ConfigEntity configEntity;
    private LinearLayout systemBar;
    private EditText top_title_search;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        configEntity = ConfigUtil.loadConfig(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_bar, null);
        return view;
    }

    /**
     * 加载顶部标题栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initTopView(View mView) {
        systemBar = (LinearLayout) mView.findViewById(R.id.lin_system_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
        topLeft = mView.findViewById(R.id.btn_top_goback);
        topBack = (ImageView) mView.findViewById(R.id.img_top_goback);
        top_title = (TextView) mView.findViewById(R.id.top_title);
        btn_top_sidebar = mView.findViewById(R.id.btn_top_sidebar);
        top_title_search = (EditText) mView.findViewById(R.id.top_title_search);
        rightOne = (ImageView) mView.findViewById(R.id.img_top_right_one);
        rightTwo = (ImageView) mView.findViewById(R.id.img_top_right_two);
        top_right_title = (TextView) mView.findViewById(R.id.tv_top_right_text);

    }


    /**
     * 隐藏返回按钮
     */
    public void hideBackBtn() {
        topBack.setVisibility(View.GONE);
    }

    /**
     * 显示返回按钮
     */
    public void showBackBtn() {
        topBack.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏top所有，不包括进度
     */
    public void hideAllTop() {
        rl_parent.setVisibility(View.GONE);
        top_line.setVisibility(View.GONE);
        systemBar.setVisibility(View.GONE);
    }

    /**
     * 设置顶部标题栏标志,并设置点击事件标签
     *
     * @param id
     */
    public void setLogoShow(int id, int clickTag) {
        topLeft.setTag(clickTag);
        topBack.setVisibility(View.GONE);
        topIcon.setVisibility(View.VISIBLE);
        topIcon.setBackgroundResource(id);
    }


    /**
     * 设置标题名称
     */
    protected void setTitle(String title) {
        if ("".equals(title)) {
            top_title.setVisibility(View.GONE);
        } else {
            top_title.setVisibility(View.VISIBLE);
            top_title.setText(title);
            top_title_search.setVisibility(View.GONE);
        }
    }

    /**
     * 设置导航右边文字
     **/
    public void setTopRightTitle(String title, int topTag) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setVisibility(View.GONE);
        rightTwo.setVisibility(View.GONE);
        top_right_title.setVisibility(View.VISIBLE);
        top_right_title.setTag(topTag);
        top_right_title.setText(title);
    }

    /**
     * 隐藏导航右边文字
     **/
    public void hideTopRightTitle() {
        btn_top_sidebar.setVisibility(View.GONE);
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
    public void setTopRightImg(int idOne, int tagOne, int idTwo, int tagTwo) {
        btn_top_sidebar.setVisibility(View.VISIBLE);
        rightOne.setVisibility(View.VISIBLE);
        rightTwo.setVisibility(View.VISIBLE);
        rightOne.setTag(tagOne);
        rightTwo.setTag(tagTwo);
        rightOne.setBackgroundResource(idOne);
        rightTwo.setBackgroundResource(idTwo);
    }

    /**
     * 顶部所有按钮的点击事件注册
     **/
    public void initTopListener() {
        if (getActivity() == null) {
            return;
        }
        topBarClickListener = new TopBarClickListener(getActivity());
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
    public void onDestroy() {
        //disMissDialog();
//        ToastUtil.dismiss();
        super.onDestroy();
    }

    /**
     * get请求
     */
    public void httpGetRequest(String url, final int action) {
//        Log.e(TAG, url);
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
//        Log.e(TAG, url);
//        Log.e(TAG, params.toString());
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
        /*if (dialog != null) {
            dialog.dismiss();
        }*/
        if (getActivity() == null) {
            return;
        }
        switch (code) {
            case 0:
                Toast.makeText(getActivity(), getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast(getResources().getString(R.string.netError), this, ToastUtil.DELAY_SHORT);
                break;
            case 404:
            case 400:
                Toast.makeText(getActivity(), getResources().getString(R.string.Error_404), Toast.LENGTH_SHORT).show();
//                ToastUtil.showToast(getResources().getString(R.string.Error_404), this, ToastUtil.DELAY_SHORT);
                break;
            default:
                Toast.makeText(getActivity(), "code = " + code + " message = " + arg3.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (getActivity() == null) {
            return;
        }
        if (null != error) {
            Toast.makeText(getActivity(), error.msg, Toast.LENGTH_SHORT).show();
//            ToastUtil.showToast(error.msg, getActivity(), ToastUtil.DELAY_SHORT);
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

}
