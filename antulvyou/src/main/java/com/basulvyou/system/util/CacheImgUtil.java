package com.basulvyou.system.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

/**
 * 缓存图片工具类
 */
public class CacheImgUtil {

	private static final String ProjectName = "basu";
	private static final String CacheName = "img";
    private static final String DataCacheName = "data";
	private static CacheImgUtil INSTANCE = null;
    public static String PATH_CACHE;
    public static String PATH_DATA_CACHE;//文件缓存路径
	public static String adv_top, adv1, adv2, adv3, adv4, adv5, adv6, adv7, adv8, adv9, adv10, adv11
	, img_logo,user_icon,home_btn1,home_btn2,home_btn3,home_btn4,home_btn5,home_btn6,home_btn7,home_btn8,home_btn9,home_btn10,home_btn11,home_btn12;

    /**
     *
     * 初始化目录
     *
     * */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
        	PATH_CACHE = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + ProjectName
                    + File.separator + CacheName;
            PATH_DATA_CACHE = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + ProjectName
                    + File.separator + DataCacheName;
        } else {// 如果SD卡不存在，就保存到本应用的目录下
        	PATH_CACHE = context.getFilesDir().getAbsolutePath()
                    + File.separator + ProjectName
                    + File.separator + CacheName;
            PATH_DATA_CACHE = context.getFilesDir().getAbsolutePath()
                    + File.separator + ProjectName +
                    File.separator + DataCacheName;
        }
        File file = new File(PATH_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dataFile = new File(PATH_DATA_CACHE);
        if (!dataFile.exists()) {
            dataFile.mkdirs();
        }
        adv_top = PATH_CACHE + File.separator + "advtop.png";
    	adv1 = PATH_CACHE + File.separator + "adv1.png";
    	adv2 = PATH_CACHE + File.separator + "adv2.png";
    	adv3 = PATH_CACHE + File.separator + "adv3.png";
    	adv4 = PATH_CACHE + File.separator + "adv4.png";
    	adv5 = PATH_CACHE + File.separator + "adv5.png";
    	adv6 = PATH_CACHE + File.separator + "adv6.png";
    	adv7 = PATH_CACHE + File.separator + "adv7.png";
    	adv8 = PATH_CACHE + File.separator + "adv8.png";
    	adv9 = PATH_CACHE + File.separator + "adv9.png";
    	adv10 = PATH_CACHE + File.separator + "adv10.png";
    	adv11 = PATH_CACHE + File.separator + "adv11.png";
    	img_logo = PATH_CACHE + File.separator + "logo.png";
    	user_icon = PATH_CACHE + File.separator + "user_icon.png";
    	home_btn1 = PATH_CACHE + File.separator + "home_btn1.png";
    	home_btn2 = PATH_CACHE + File.separator + "home_btn2.png";
    	home_btn3 = PATH_CACHE + File.separator + "home_btn3.png";
    	home_btn4 = PATH_CACHE + File.separator + "home_btn4.png";
        home_btn5 = PATH_CACHE + File.separator + "home_btn5.png";
    	home_btn6 = PATH_CACHE + File.separator + "home_btn6.png";
    	home_btn7 = PATH_CACHE + File.separator + "home_btn7.png";
    	home_btn8 = PATH_CACHE + File.separator + "home_btn8.png";
        home_btn9 = PATH_CACHE + File.separator + "home_btn9.png";
    	home_btn10 = PATH_CACHE + File.separator + "home_btn10.png";
    	home_btn11 = PATH_CACHE + File.separator + "home_btn11.png";
    	home_btn12 = PATH_CACHE + File.separator + "home_btn12.png";
    }

    public static CacheImgUtil getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CacheImgUtil(context);
        }
        return INSTANCE;
    }

    private CacheImgUtil(Context context) {
        init(context);
    }

    /**
     * 清楚缓存图片
     */
    public static void wipeCache() {
         new File(adv_top).delete();
         new File(adv1).delete();
         new File(adv2).delete();
         new File(adv3).delete();
         new File(adv4).delete();
         new File(adv5).delete();
         new File(adv6).delete();
         new File(adv7).delete();
         new File(adv8).delete();
         new File(adv9).delete();
         new File(adv10).delete();
         new File(adv11).delete();
         new File(home_btn1).delete();
         new File(home_btn2).delete();
         new File(home_btn3).delete();
         new File(home_btn4).delete();
         new File(home_btn5).delete();
         new File(home_btn6).delete();
         new File(home_btn7).delete();
         new File(home_btn8).delete();
         new File(home_btn9).delete();
         new File(home_btn10).delete();
         new File(home_btn11).delete();
         new File(home_btn12).delete();
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
