package com.zycreview.system.wibget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zycreview.system.R;


/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

    public interface DialogClickInterface {
        void onclickOk();

        void onclickCancel();
    }

    private TextView titleTxv;        //标题
    private TextView msgTxv;        //提示内容文字
    private TextView positiveTxv;        //确定按钮
    private TextView negativeTxv;        //取消按钮
    private DialogClickInterface mDiadlogClick;

    /**
     * @param context
     */
    public UpdateDialog(Context context) {
        super(context, R.style.CustomDialog);        //自定义style主要去掉标题，标题将在setCustomView中自定义设置
        setCustomView();
    }

    public UpdateDialog(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
        super(context, R.style.CustomDialog);
        this.setCancelable(cancelable);
        this.setOnCancelListener(cancelListener);
        setCustomView();
    }

    public UpdateDialog(Context context, int theme) {
        super(context, R.style.CustomDialog);
        setCustomView();
    }

    /**
     * 设置整个弹出框的视图
     */
    private void setCustomView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.update_custom_dialog, null);
        titleTxv = (TextView) mView.findViewById(R.id.title);
        msgTxv = (TextView) mView.findViewById(R.id.message);
        positiveTxv = (TextView) mView.findViewById(R.id.positiveButton);
        negativeTxv = (TextView) mView.findViewById(R.id.negativeButton);
        super.setContentView(mView);
    }

    @Override
    public void setContentView(View view) {
        //重写本方法，使外部调用时不可破坏控件的视图。
        //也可以使用本方法改变CustomDialog的内容部分视图，比如让用户把内容视图变成复选框列表，图片等。这需要获取mView视图里的其它控件
    }

    /**
     * 设置提示内容文字
     *
     * @param msg
     */
    public void setMessage(CharSequence msg) {
        msgTxv.setText(msg);
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveTxv.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeTxv.setOnClickListener(listener);
    }

    public void setDialogClick(DialogClickInterface click) {
        mDiadlogClick = click;
        if (positiveTxv != null) {
            positiveTxv.setOnClickListener(this);
        }
        if (negativeTxv != null) {
            negativeTxv.setOnClickListener(this);
        }
    }

    public void setOkText(String text) {
        if (positiveTxv != null) {
            positiveTxv.setText(text);
        }
    }

    public void setCancelText(String text) {
        if (negativeTxv != null) {
            negativeTxv.setText(text);
        }
    }

    public void setTitleText(String text) {
        if (titleTxv != null) {
            titleTxv.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v == positiveTxv) {
            if (mDiadlogClick != null) {
                mDiadlogClick.onclickOk();
            }
        } else if (v == negativeTxv) {
            if (mDiadlogClick != null) {
                mDiadlogClick.onclickCancel();
            }
        }
    }
}
