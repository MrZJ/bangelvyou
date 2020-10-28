package com.shishoureport.system.wibget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shishoureport.system.R;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    public interface CustomInterface {
        void okClick();

        void cancelClick();
    }

    private Context mContext;
    private String title, ok, cancel, tips;
    private TextView title_tv, ok_btn, cancel_btn, tips_tv;
    private CustomInterface mInterface;

    public CustomDialog(@NonNull Context context, String title, String cancel, String ok, String tips, CustomInterface customInterface) {
        super(context);
        mContext = context;
        this.title = title;
        this.cancel = cancel;
        this.ok = ok;
        this.tips = tips;
        mInterface = customInterface;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom, null);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        ok_btn = (TextView) view.findViewById(R.id.ok_btn);
        tips_tv = (TextView) view.findViewById(R.id.tips_tv);
        title_tv.setText(title);
        ok_btn.setText(ok);
        cancel_btn.setText(cancel);
        tips_tv.setText(tips);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                dismiss();
                if (mInterface != null) {
                    mInterface.okClick();
                }
                break;
            case R.id.cancel_btn:
                dismiss();
                if (mInterface != null) {
                    mInterface.cancelClick();
                }
                break;
        }
    }
}
