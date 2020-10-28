package com.shenmaireview.system.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenmaireview.system.utils.UIUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class NoTopBaseFragment extends Fragment {

    protected View rootView;


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
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //初始化view
    protected abstract void initView();

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
    public void httpPostRequest(Context context, String url, HashMap<String, String> params, final int action){
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

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
