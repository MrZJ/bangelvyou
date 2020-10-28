package com.shenmaio2o.system.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shenmaio2o.system.R;
import com.shenmaio2o.system.UIApplication;
import com.shenmaio2o.system.listener.TopBarClickListener;
import com.shenmaio2o.system.utlis.AppManager;
import com.shenmaio2o.system.utlis.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


public abstract class BaseActivity extends FragmentActivity {
    
    View topGoBack;
    TextView topTitle;
    LinearLayout systemBar;
    public UIApplication application;
    public AppManager appManager;
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
        ButterKnife.bind(this);
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
    public void setLeftBackButton(boolean show){
        if(show){
            topGoBack.setVisibility(View.VISIBLE);
        } else {
            topGoBack.setVisibility(View.GONE);
        }
    }

    /**初始化顶部导航栏*/
    public void initTopView() {
        topGoBack = findViewById(R.id.btn_top_goback);
        topTitle = (TextView) findViewById(R.id.top_title);
        systemBar = (LinearLayout) findViewById(R.id.lin_system_bar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int sysbarHeight = UIUtils.getStatusBarHeight();
            LinearLayout.LayoutParams sysBarParams = (LinearLayout.LayoutParams) systemBar.getLayoutParams();
            sysBarParams.height = sysbarHeight;
            systemBar.setLayoutParams(sysBarParams);
        }
    }

    /**初始化顶部导航栏监听*/
    public void initTopListener(){
        TopBarClickListener topBarClickListener = new TopBarClickListener(this);
        topGoBack.setOnClickListener(topBarClickListener);
    }

    /**
     * 设置顶部标题
     * @param str
     */
    public void setTopTitle(String str){
        topTitle.setText(str);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//统计时长
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public void finish(){
        if (!(this instanceof MainActivity)){
            appManager.PopActivity();
        }
        super.finish();
    }

    /**
     * 获取apk的版本号 currentVersionCode
     */
    public  int getAPPVersionCode() {
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
