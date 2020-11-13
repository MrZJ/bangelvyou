package com.shishoureport.system.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.activity.BaseActivity;
import com.shishoureport.system.ui.fragment.MedicalListFragment;

/**
 * @date
 **/
public class MedicalListActivity extends BaseActivity {

    public static void start(Activity context, boolean isReserve, int reqCode) {
        Intent intent = new Intent(context, MedicalListActivity.class);
        intent.putExtra("isReserve", isReserve);//是否是库存
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_medical_list;
    }

    @Override
    public void initView() {
        findViewById(R.id.back_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, MedicalListFragment.getInstance(getIntent().getBooleanExtra("isReserve", false)))
                .commitAllowingStateLoss();
    }
}
