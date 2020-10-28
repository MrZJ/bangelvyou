package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;

import com.yishangshuma.bangelvyou.R;

/**
 * 优惠券说明界面
 */
public class CouponSpecActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_spec);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("优惠券说明");
        setLeftBackButton();
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

}
