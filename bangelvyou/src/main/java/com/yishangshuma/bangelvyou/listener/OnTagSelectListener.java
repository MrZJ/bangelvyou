package com.yishangshuma.bangelvyou.listener;

import com.yishangshuma.bangelvyou.wibget.FlowTagLayout;

import java.util.List;

/**
 * 标签流选择事件接口
 */
public interface OnTagSelectListener {
    void onItemSelect(FlowTagLayout parent, List<Integer> selectedList);
}
