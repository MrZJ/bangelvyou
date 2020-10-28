package com.zycreview.system.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SoftInputManager {

    public static void closeInputBoard(Activity c) {
        if (c == null) return;
        InputMethodManager im = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(c.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
