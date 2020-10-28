package com.basulvyou.system.wibget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.basulvyou.system.R;

/**
 * Created by jianzhang.
 * on 2017/6/22.
 * copyright easybiz.
 */

public class MyProgressDialog extends Dialog {
    public MyProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intView();
    }

    private void intView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_dialog, null);
        setContentView(view);
    }
}
