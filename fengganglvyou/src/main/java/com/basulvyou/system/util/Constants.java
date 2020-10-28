package com.basulvyou.system.util;

/**
 * 微信支付配置文件
 */
public class Constants {

   //appid
   //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
   public static final String APP_ID = "wxd59edc3f15a62928";

   //商户号
   public static final String MCH_ID = "1356760802";

   //  API密钥，在商户平台设置
   public static final  String API_KEY="q123456wertyuiopasdfghjklzxcvbnm";

   //商户回调地址
   public static final  String NOTIFY_URL="http://chaoshi.mymyty.com/weixin/NotifyWeixinForApp.do";

}
