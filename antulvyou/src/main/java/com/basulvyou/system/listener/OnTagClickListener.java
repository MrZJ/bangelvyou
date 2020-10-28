package com.basulvyou.system.listener;

import android.view.View;

import com.basulvyou.system.wibget.FlowTagLayout;


/**
 * 标签流点击时间接口
 */
public interface OnTagClickListener {
    void onItemClick(FlowTagLayout parent, View view, int position);
}
