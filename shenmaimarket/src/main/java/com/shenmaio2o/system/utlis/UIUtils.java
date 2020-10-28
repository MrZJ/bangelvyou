package com.shenmaio2o.system.utlis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.shenmaio2o.system.UIApplication;


/**
 * UI工具类
 */
public class UIUtils {
    /**获取上下文*/
    public static Context getContext(){
        return UIApplication.getApplication();
    }

    /** dip转换px */
    public static int dip2px(int dip) {
        final float scale = UIApplication.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** pxz转换dip */
    public static int px2dip(int px) {
        final float scale = UIApplication.getApplication().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp( float pxValue) {
        final float fontScale = UIApplication.getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        final float fontScale = UIApplication.getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**网络是否可用
     * 检测当的网络（WLAN、3G/2G）状态
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) UIUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static void showToast(String txt){
        Toast.makeText(UIUtils.getContext(), txt, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取系统状态栏的高度
     * @return
     */
    public static int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = UIUtils.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = UIUtils.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
