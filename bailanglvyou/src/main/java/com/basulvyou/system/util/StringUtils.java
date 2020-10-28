package com.basulvyou.system.util;

/**
 * Created by jianzhang.
 * on 2017/8/23.
 * copyright easybiz.
 */

class StringUtils {
    public static boolean isNotBlank(String body) {
        if (body != null && !body.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(String path) {
        if (path == null || path.equals("")) {
            return true;
        }
        return false;
    }
}
