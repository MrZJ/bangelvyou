package com.shishoureport.system.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

public class CommUtil {
    private static final String SP_NAME = "device_id";
    private static final String SP_KEY_DEVICE_ID = "deviceId";
    private static final String TAG = "CommUtil";
    private static final String TEMP_FILE_NAME = "system_file";

    public static String getDevToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString(SP_KEY_DEVICE_ID, null);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        deviceId = getIMEI(context);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = createUUID(context);
        }
        sharedPreferences.edit()
                .putString(SP_KEY_DEVICE_ID, deviceId)
                .apply();
        return deviceId;
    }

    private static String getIMEI(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return null;
            }
            @SuppressLint({"MissingPermission", "HardwareIds"}) String imei = telephonyManager.getDeviceId();
            return imei;
        } catch (Exception e) {
            return null;
        }
    }

    private static String createUUID(Context context) {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
