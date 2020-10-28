package com.shishoureport.system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shishoureport.system.entity.ConfigEntity;


/**
 * 网络地址，缓存数据工具类
 */
public class ConfigUtil {
    public static final String HTTP = "http://192.168.3.54:8080";//内网测试地址http://192.168.3.16:8080/sd_ly/service/
    public static final String HTTP_URL = HTTP + "/mmt_base/service";
    public static final String HTTP_HOME = HTTP_URL + "/HomeAppOneService.do?method=index&op=index";
    public static final String HTTP_INFORMATION_URL = HTTP_URL + "/IndexShop.do";//商城
    public static final String HTTP_REPORT_URL = HTTP_URL + "/MMyCartInfo.do";//日报
    public static final String HTTP_MINE_URL = HTTP_URL + "/MMyHome.do";//我的
    public static final String HTTP_SEARCH_URL = HTTP_URL + "/WapSearch!search.do";//搜索
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

}
