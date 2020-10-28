package com.shishoureport.system.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.shishoureport.system.entity.ConfigEntity;


/**
 * 网络地址，缓存数据工具类
 */
public class ConfigUtil {
//    public static final String HTTP_URL = "http://60.174.234.249:8111";//外网

//        public static final String HTTP_URL = "http://220.178.14.5:8486";//外网
    public static final String HTTP_URL = "http://192.168.3.132:8080/fesh";//李加辉测试
//        public static final String HTTP_URL = "http://192.168.3.54:8080";//钱俊测试
//    public static final String HTTP_URL = "http://192.168.3.5:8486";//何志娟测试
//    public static final String HTTP_URL = HTTP + "/fesh";
    public static final String HTTP_ADDD_DEVICE = HTTP_URL + "/service.do?method=updateDeviceToken";//半亩园
    public static final String HTTP_HOME = HTTP_URL + "/wap/index.do";//半亩园
    public static final String HTTP_MARKET_URL = HTTP_URL + "/wap/MFuPin.do?method=poorList&base_id=230227";//一户一码
    public static final String HTTP_MY_CAR_URL = HTTP_URL + "/wap/MVedioInfo.do";//视频直播
    public static final String HTTP_MINE_URL = HTTP_URL + "/wap/MMyHome.do";//我的
    public static final String HTTP_NEW_GOOD_URL = HTTP_URL + "/wap/MNewCommInfo.do";//新品
    public static final String HTTP_SEARCH_URL = HTTP_URL + "/wap/MNewSearch.do";//搜索
    public static final String HTTP_CHAT_URL = HTTP_URL + "/wap/MMyChat.do";//聊天
    private static final String USER_NAME = "";
    private static final String KEY = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";

    /**
     * 加载配置信息
     *
     * @param cxt Context
     * @return 配置实体对象
     */
    public static ConfigEntity loadConfig(Context cxt) {
        if (cxt == null) return new ConfigEntity();
        ConfigEntity entity = new ConfigEntity();
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference", Context.MODE_PRIVATE);
        entity.username = share.getString("userName", USER_NAME);
        entity.key = share.getString("key", KEY);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        if (StringUtil.isEmpty(ConfigUtil.phoneIMEI)) {
//            phoneIMEI = UmengRegistrar.getRegistrationId(cxt);
        }
        return entity;
    }

    /**
     * 保存配置信息
     *
     * @param cxt    Context
     * @param entity 配置实体对象
     */
    public static void saveConfig(Context cxt, ConfigEntity entity) {
        if (cxt == null) return;
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", entity.username);
        editor.putString("key", entity.key);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.commit();
    }

    /**
     * 清除配置信息
     *
     * @param cxt Context
     *            配置实体对象
     */
    public static void clearConfig(Context cxt) {
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", "");
        editor.putString("key", "");
        editor.putBoolean("isLogin", false);
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

    public static String getDevToken(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
