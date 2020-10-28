package com.shishoureport.system.utils;

/**
 * Created by jianzhang.
 * on 2017/9/12.
 * copyright easybiz.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shishoureport.system.R;

/**
 * 公用的弹出框
 *
 * @author lining
 */
public class LoadingDialog {

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialog_view);
        // 页面中的Img
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tipTextView);
        // 显示文本
        tipText.setText(msg);

        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }
}
