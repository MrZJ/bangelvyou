package com.shishoureport.system;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hss01248.notifyutil.NotifyUtil;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.http.OkHttpStack;
import com.shishoureport.system.push.UmengNotificationService;
import com.shishoureport.system.request.UmengUpdateDTRequest;
import com.shishoureport.system.utils.CacheImgUtil;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.xutils.x;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/27.
 */
public class UIApplication extends Application {

    public static final String U_MENG_APPKEY = "5ade89e78f4a9d0a79000027";
    public static final String U_MENG_MESSAGE_SECRET = "db09550725a733128f02f1ce4babe0a6";
    /**
     * 应用程序全局变量
     */
    private static UIApplication app;
    public static RequestQueue mRequestQueue;
    private String umengDeviceToken;

    public String getUmengDeviceToken() {
        return umengDeviceToken;
    }

    public void setUmengDeviceToken(String umengDeviceToken) {
        this.umengDeviceToken = umengDeviceToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fresco.initialize(this);
        CacheImgUtil.getInstance(this);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        x.Ext.init(this);
        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack());
        NotifyUtil.init(getApplicationContext());
        Log.e("jianzhang", "UIApplication  onCreate");
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.setResourcePackageName("com.shishoureport.system");
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("jianzhang", "umeng=" + deviceToken);
                umengDeviceToken = deviceToken;
                User user = MySharepreference.getInstance(getApplicationContext()).getUser();
                if (user != null && !StringUtil.isEmpty(user.id)) {
                    final UmengUpdateDTRequest request = new UmengUpdateDTRequest(umengDeviceToken, user.id);
                    mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, request.getRequestUrl(),
                            new Response.Listener<String>() {
                                public void onResponse(String content) {
                                }
                            }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return request.getRequestParams();
                        }
                    };
                    mRequestQueue.add(stringRequest);
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("jianzhang", "onFailure" + s + ",onFailure" + s1);
            }
        });
        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                Log.e("dealWithCustomAction", "onFailure=" + uMessage.toString());
                super.dealWithCustomAction(context, uMessage);
            }
        });
        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);
    }

    public static UIApplication getInstance() {
        if (null == app) {
            app = new UIApplication();
        }
        return app;
    }
}
