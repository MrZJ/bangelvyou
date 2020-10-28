package com.shishoureport.system.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jianzhang.
 * on 2018/5/11.
 * copyright easybiz.
 */
public class CheckPermission {

    public static int REQUEST_EXTERNAL_STORAGE = 1;

    public static String[] PERMISSIONS_STORAGE = {

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    /**
     * 读写权限申请回调true有权限false无权限
     *
     * @param mContext
     */

    public static void writeExternalStorage(final Context mContext, PermissionCallBack mCallBack) {

        //检查是否拥有相应的权限

        int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //没有权限发起申请

        if (permission != PackageManager.PERMISSION_GRANTED) {

            //6.0及以上版本才需要申请权限

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            else {
                mCallBack.setOnPermissionListener(false);
            }
        } else {
            mCallBack.setOnPermissionListener(true);
        }
    }

    /**
     * 读写权限申请回调true有权限false无权限
     *
     * @param mContext
     */

    public static void writeExternalStorage(final Context mContext, PermissionCallBack mCallBack, int requestCode) {

        //检查是否拥有相应的权限

        int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //没有权限发起申请

        if (permission != PackageManager.PERMISSION_GRANTED) {

            //6.0及以上版本才需要申请权限

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_STORAGE, requestCode);

            else {

                mCallBack.setOnPermissionListener(false);

            }

        } else {

            mCallBack.setOnPermissionListener(true);

        }
    }

    public interface PermissionCallBack {
        void setOnPermissionListener(boolean allow);
    }
}
