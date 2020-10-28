package com.zycreview.system.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * 是否打开wifi 并且提醒打开
 *
 * @author Administrator
 *
 */
public class WifiNetworkUtil {

	public static boolean OpenNetworkSetting(final Activity activity) {
		// 检查有没有网络
		WifiManager connectivityManager = (WifiManager) activity.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		int wifiState = connectivityManager.getWifiState();
		//模拟器是用电脑上网，有可能设置飞行模式activeNetworkInfo！=null
		//在真机测试。真机也要上网
		//用一台笔记本电脑，做wifi连接
		if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
			// 没网，显示一个dialog,
			Builder dialog = new Builder(activity);
			dialog.setMessage("亲，连接的不是无线网，下载将要消耗流量");
			// 打开
			dialog.setPositiveButton("打开", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						//不同的android版本网络设置界面activity中的intent-filetr,action是不一样的
						//不同的android版本的代码是不一样的。
						int androidVersion=android.os.Build.VERSION.SDK_INT;
						//通过代码得到手机厂商名称，
						//不同厂商的手机的代码是不一样的。
						//有的手机能得到手机号，大部分手机能得到sim卡中的串号，串号每个手机是唯一的。
						if (androidVersion>=10)
						{
						// 打开系统自带的网络设置界面
						Intent intent = new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS);
						activity.startActivity(intent);
						}

					} catch (Exception e) {
					}

				}
			});
			// 取消
			dialog.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			dialog.show();
		}else {
			return true;
		}
		return false;
	}
}
