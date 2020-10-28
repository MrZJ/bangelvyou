package com.objectreview.system.utlis;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.objectreview.system.entity.ConfigEntity;
import com.objectreview.system.entity.LogisticEntity;

import java.util.List;


/**
 * 网络地址，缓存数据工具类
 * Created by KevinLi on 2016/2/19.
 */
public class ConfigUtil {
    public static final String HTTP = "http://123.57.164.184:8091";
//    public static final String HTTP = "http://192.168.3.5:8788/pd_zhuisu";
    public static final String HTTP_URL = HTTP + "/service";
    public static final String HTTP_URL_TASK_DETAIL = HTTP_URL +"/WorkAssignedLink.do?method=view&id=";//任务详情页面
    public static final String HTTP_URL_ABOUT = HTTP_URL +"/HelpOnUsing.do?method=about";//关于页面
    public static final String HTTP_URL_HELP = HTTP_URL +"/HelpOnUsing.do?mod_id=9009003000";//使用帮助页面
    private static final String USER_NAME = "";
    private static final String USER_TYPE = "";
    private static final String PASS_WORD = "";
    private static final String KEY = "";
    private static final String PROVINCE = "";
    private static final String ADDRESS = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";
    public static List<LogisticEntity> logisticsList;
    public static  boolean IS_DOWNLOAD = false;
    public static long TenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    /**
     * 加载配置信息
     *
     * @param cxt
     *            Context
     * @return 配置实体对象
     */
    public static ConfigEntity loadConfig(Context cxt) {
        ConfigEntity entity = new ConfigEntity();
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",Context.MODE_PRIVATE);
        entity.username = share.getString("userName", USER_NAME);
        entity.usertype = share.getString("userType", USER_TYPE);
        entity.passwordMD5 = share.getString("password", PASS_WORD);
        entity.key = share.getString("key", KEY);
        entity.province = share.getString("province", PROVINCE);
        entity.address = share.getString("address", ADDRESS);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        if(StringUtil.isEmpty(ConfigUtil.phoneIMEI)){
            TelephonyManager TelephonyMgr = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
            ConfigUtil.phoneIMEI = TelephonyMgr.getDeviceId();//获取手机唯一识别码
        }
        return entity;
    }

    /**
     * 保存配置信息
     *
     * @param cxt
     *            Context
     * @param entity
     *            配置实体对象
     */
    public static void saveConfig(Context cxt, ConfigEntity entity){
        SharedPreferences share = cxt.getSharedPreferences("ConfigPerference",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("userName", entity.username);
        editor.putString("userType", entity.usertype);
        editor.putString("password", entity.passwordMD5);
        editor.putString("key", entity.key);
        editor.putString("province", entity.province);
        editor.putString("address", entity.address);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.commit();
    }

}
