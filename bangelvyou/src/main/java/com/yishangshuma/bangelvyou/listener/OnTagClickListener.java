package com.yishangshuma.bangelvyou.listener;

import android.view.View;

import com.yishangshuma.bangelvyou.wibget.FlowTagLayout;

/**
 * 标签流点击时间接口
 */
public interface OnTagClickListener {
    void onItemClick(FlowTagLayout parent, View view, int position);
}
