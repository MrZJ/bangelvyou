package com.fuping.system.utils;


import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.fuping.system.R;


/**
 * 提示框工具类
 * @author KevinLi
 *
 */
public class ToastUtil {

	static Dialog mDialog;

	public static final long DELAY_SHORT = 700;

	public static void showToast(String msg, Context activityContext) {

		if (null != mDialog) {

			TextView textView = (TextView) mDialog.findViewById(R.id.toast_msg);

			textView.setText(msg);
			return;
		}

		mDialog = new Dialog(activityContext, R.style.myToast);

		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(R.layout.toast_layout);
		LayoutParams params = mDialog.getWindow().getAttributes();

		params.dimAmount = 0.0f;
		params.flags = params.flags
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_NOT_TOUCH_MODAL;

		mDialog.getWindow().setAttributes(params);

		TextView textView = (TextView) mDialog.findViewById(R.id.toast_msg);

		textView.setText(msg);

		mDialog.show();

	}

	public static void showToast(String msg, Context activityContext,
			long dissMissDelay) {

		showToast(msg, activityContext);

		dismissDelay(dissMissDelay);

	}

	public static void setMessage(String msg) {
		if (null != mDialog && mDialog.isShowing()) {
			TextView textView = (TextView) mDialog.findViewById(R.id.toast_msg);

			textView.setText(msg);
		}

	}

	public static void setMessage(String msg, long time) {
		if (null != mDialog && mDialog.isShowing()) {
			TextView textView = (TextView) mDialog.findViewById(R.id.toast_msg);

			textView.setText(msg);

			dismissDelay(time);
		}

	}

	public static void dismissDelay(long time) {

		if (null == mDialog) {
			return;
		}
		TextView textView = (TextView) mDialog.findViewById(R.id.toast_msg);

		textView.postDelayed(new Runnable() {

			public void run() {
				if (null != mDialog) {

					synchronized (ToastUtil.class) {
						if (null != mDialog && mDialog.isShowing()) {
							try {

								mDialog.dismiss();
								mDialog = null;

							} catch (Exception e) {
							}
						}
					}

				}
			}
		}, time);

	}

	public static void dismiss() {
		if (null != mDialog) {
			synchronized (ToastUtil.class) {
				if (null != mDialog) {
					mDialog.dismiss();
					mDialog = null;
				}
			}
		}
	}

}
