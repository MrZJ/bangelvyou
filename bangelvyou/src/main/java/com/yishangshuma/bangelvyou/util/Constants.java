package com.yishangshuma.bangelvyou.util;

/**
 * 微信支付配置文件
 */
public class Constants {

   //appid
   //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
   public static final String APP_ID = "wxae8cbc71b26335ab";

   //商户号
   public static final String MCH_ID = "1323974501";

   //  API密钥，在商户平台设置
   public static final  String API_KEY="q123456wertyuiopasdfghjklzxcvbnm";

   //商户回调地址
   public static final  String NOTIFY_URL="http://chaoshi.mymyty.com/weixin/NotifyWeixinForApp.do";

}
