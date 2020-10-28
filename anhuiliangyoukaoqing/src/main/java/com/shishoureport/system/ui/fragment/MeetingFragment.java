package com.shishoureport.system.ui.fragment;


import android.view.View;

import com.shishoureport.system.R;

/**
 * 购物车
 */
public class MeetingFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView(View v) {
        setLeftBackButton(false);
        setTopTitle("会议");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
