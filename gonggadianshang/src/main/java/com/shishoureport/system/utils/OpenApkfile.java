package com.shishoureport.system.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 打开apk文件
 *
 * @author Administrator
 *
 */
public class OpenApkfile {

	public static Intent openFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		}else {
			return getAllIntent(filePath);
		}
	}

	/**
	 * Android获取一个用于打开所有文件的intent
	 *
	 * @param param
	 * @return
	 */
	public static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	/**
	 * Android获取一个用于打开APK文件的intent
	 *
	 * @param param
	 * @return
	 */
	public static Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

}
