package com.zycreview.system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.umeng.message.UmengRegistrar;
import com.zycreview.system.entity.ConfigEntity;
import com.zycreview.system.entity.UrlListBean;

import java.util.HashMap;
import java.util.Map;


/**
 * 网络地址，缓存数据工具类
 * Created by KevinLi on 2016/2/19.
 */
public class ConfigUtil {
    //    public static final String HTTP = "http://192.168.3.72:8080/sxzyc_v2";//胡
    public static final String HTTP = "http://www.sxszyczs.com";//外网地址
//    public static final String HTTP = "http://192.168.3.74:8080/sxzyc_v2";//张测试地址
//    public static final String HTTP = "http://192.168.3.16:8088/sxzyc_v2";//刘测试地址
//    public static final String HTTP = "http://192.168.3.56:18080/sxzyc_v2";//胡测试地址
//    public static final String HTTP = "http://192.168.3.56:28080/sxzyc_v2/";//胡文康测试地址
//    public static final String HTTP = "http://192.168.3.5:8482/";//王亚丽测试地址
    public static final String HTTP_URL = HTTP + "/manager";
    public static final String HTTP_URL_POLICY_DETAIL = HTTP + "/m/MNewsInfo.do?method=view&mobileLogin=true&uuid=";//政策详情页面
    public static final String HTTP_URL_SCAN_DETAIL = HTTP + "/website/TraceBackWap.do?";//追溯结果详情页面
    public static final String HTTP_URL_ABOUT_US = HTTP + "/m/MNewsInfo.do?method=aboutUs";//关于我们
    public static final String HTTP_URL_ENTP_INFO = HTTP + "/m/MSearchEntp.do?method=getEntpInfoDetails&isApp=true&entp_id=";//关于我们
    public static final String HTTP_URL_HELP = HTTP_URL + "/HelpOnUsing.do?mod_id=9009003000";//使用帮助页面
    private static final String USER_NAME = "";
    private static final String USER_TYPE = "";
    private static final String ENTP_ID = "";
    private static final String PASS_WORD = "";
    private static final String KEY = "";
    private static final String PROVINCE = "";
    private static final String ADDRESS = "";
    private static final boolean IS_LOGIN = false;
    private static final boolean IS_FIRST = true;
    public static String phoneIMEI = "";
    public static long TenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
    public static boolean IS_DOWNLOAD = false;

    /**
     * 地区选择
     */
    /**
     * 所有省
     */
    public static String[] mProvinceDatas;

    /**
     * 山西省
     */
    public static String[] shanxiProvince = {"山西省"};
    /**
     * key - 省 value - 市
     */
    public static Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    public static Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    public static Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * key - 省 values - 邮编
     */
    public static Map<String, String> mPZipcodeDatasMap = new HashMap<String, String>();

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
        entity.usertype = share.getString("userType", USER_TYPE);
        entity.entpId = share.getString("entpId", ENTP_ID);
        entity.passwordMD5 = share.getString("password", PASS_WORD);
        entity.key = share.getString("key", KEY);
        entity.province = share.getString("province", PROVINCE);
        entity.address = share.getString("address", ADDRESS);
        entity.isLogin = share.getBoolean("isLogin", IS_LOGIN);
        entity.isFirst = share.getBoolean("isFirst", IS_FIRST);
        String urlList = share.getString("urlList", null);
        if (!StringUtil.isEmpty(urlList)) {
            entity.urlList = JSONArray.parseArray(urlList, UrlListBean.class);
        }
        if (StringUtil.isEmpty(ConfigUtil.phoneIMEI)) {
            ConfigUtil.phoneIMEI = UmengRegistrar.getRegistrationId(cxt);//获取手机唯一识别码
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
        editor.putString("userType", entity.usertype);
        editor.putString("entpId", entity.entpId);
        editor.putString("password", entity.passwordMD5);
        editor.putString("key", entity.key);
        editor.putString("province", entity.province);
        editor.putString("address", entity.address);
        editor.putBoolean("isLogin", entity.isLogin);
        editor.putBoolean("isFirst", entity.isFirst);
        editor.putString("urlList", JSONArray.toJSONString(entity.urlList));
        editor.commit();
    }

}
