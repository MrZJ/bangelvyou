package com.shenmai.system.utlis;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/11/24 0024.
 *
 * 得到屏幕宽度和高度
 */
public class GetHeightAndWidthUtil {

    public static int width,height;//屏幕宽度和高度 px单位

    public static void getHeightAndWidth(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

}
