package com.shishoureport.system.utils;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by jianzhang.
 * on 2017/5/25.
 * copyright easybiz.
 */

public class FrescoHelper {
    public static void loadImage(String url, SimpleDraweeView simpleDraweeView) {
        if (!StringUtil.isEmpty(url)) {
            simpleDraweeView.setImageURI(url);
        }
    }
}
