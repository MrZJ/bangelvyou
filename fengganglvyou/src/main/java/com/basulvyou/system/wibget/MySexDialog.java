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
 * on 2017/6/23.
 * copyright easybiz.
 */

public class MySexDialog extends Dialog implements View.OnClickListener {

    public interface onSexClick {
        void onManCkick();

        void onMaleClick();

    }

    private View man, male;
    private onSexClick mClick;

    public MySexDialog(@NonNull Context context, onSexClick click) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.mClick = click;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sex_select_dialog, null);
        man = view.findViewById(R.id.man);
        male = view.findViewById(R.id.male);
        man.setOnClickListener(this);
        male.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.man:
                if (mClick != null) {
                    mClick.onManCkick();
                }
                break;
            case R.id.male:
                if (mClick != null) {
                    mClick.onMaleClick();
                }
                break;
        }
    }

}
