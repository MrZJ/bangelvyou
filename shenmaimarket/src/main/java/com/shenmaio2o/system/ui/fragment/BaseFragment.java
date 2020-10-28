package com.shenmaio2o.system.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenmaio2o.system.R;
import com.shenmaio2o.system.listener.TopBarClickListener;
import com.shenmaio2o.system.utlis.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * fragment基类
 */
public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    protected View rootView;
    @Bind(R.id.btn_top_goback)
    View topGoBack;
    @Bind(R.id.top_title)
    TextView topTitle;
    @Bind(R.id.lin_system_bar)
    LinearLayout systemBar;
    @Bind(R.id.btn_top_sidebar)
    View rightSide;
    @Bind(R.id.img_top_right_one)
    ImageView commonImg;
    private TopBarClickListener topBarClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initTopView();
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化view
    protected abstract void initView();

    /**
     * 设置顶部导航左侧按钮显示返回按钮
     */
    public void setLeftBackButton(boolean show){
        if(show){
            topGoBack.setVisibility(View.VISIBLE);
        } else {
            topGoBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边搜索按钮
     */
    public void setRightSide(int resId){
        rightSide.setVisibility(View.VISIBLE);
        commonImg.setBackgroundResource(resId);
    }

    /**
     * 隐藏右边按钮
     */
    public void hideRightSide(){
        rightSide.setVisibility(View.GONE);
    }

    /**
     * 隐藏右边按钮
     */
    public void showRightSide(){
        rightSide.setVisibility(View.VISIBLE);
    }

    /**
     * 设置顶部标题
     * @param str
     */
    public void setTopTitle(String str){
        topTitle.setText(str);
    }

    /**
     * 加载顶部标题栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initTopView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = UIUtils.getStatusBarHeight();
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
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
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Get请求
     * @param context
     * @param url
     * @param action
     */
    public void httpGetRequest(Context context, String url, final int action){
        if(UIUtils.isNetworkAvailable()){
            OkGo.get(url).tag(context).execute(new StringCallback() {

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    httpOnResponse(s,action);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    UIUtils.showToast("网络请求失败");
                }
            });
        }else{
            UIUtils.showToast("当前网络不可用");
        }
    }

    /**
     * Post请求
     */
    public void httpPostRequest(Context context,String url, HashMap<String, String> params, final int action){
        if(UIUtils.isNetworkAvailable()){
            OkGo.post(url).tag(context).params(params).execute(new StringCallback() {

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    httpOnResponse(s,action);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    UIUtils.showToast("网络请求失败");
                }
            });
        }else{
            UIUtils.showToast("当前网络不可用");
        }
    }

    /**
     * 返回数据处理
     * @param json
     * @param action
     */
    private void httpOnResponse(String json, int action) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject == null) {
                return;
            }
            Object object = jsonObject.get("code");
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
                    UIUtils.showToast(JSON.parseObject(json).getString("msg"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子类数据处理
     * @param json
     * @param action
     */
    protected void httpResponse(String json, int action){

    }

}
