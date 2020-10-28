package com.shenmailogistics.system.utils;

import android.content.Context;

/**
 * 系统状态栏工具
 */
public class StatusBarUtil {
    /**
     * 获取系统状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
 }

