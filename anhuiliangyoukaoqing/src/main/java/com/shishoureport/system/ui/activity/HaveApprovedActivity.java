package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.request.AttendanceAuditListRequest;
import com.shishoureport.system.ui.adapter.AttandenceAdapter;
import com.shishoureport.system.ui.fragment.AuditListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/8/31.
 * copyright easybiz.
 */

public class HaveApprovedActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private TextView approve_leave_app_tv, approve_business_tra__tv, leave_app_tv, business_tra_tv, title_tv;
    private View approve_leave_app_layout, approve_business_tra_layout, leave_app_layout, business_tra_layout, approve_leave_app_line, approve_business_tra_tab_line, leave_app_tab_line, business_tra_line1;
    private List<Fragment> fragments;
    private int selectedColor;
    private int unSelectedColor;
    private int mCurentPage = -1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HaveApprovedActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_have_approved;
    }

    public void initView() {
        selectedColor = Color.parseColor("#8CCAF2");
        unSelectedColor = Color.parseColor("#555555");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.addOnPageChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_OVERT_TIME));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_CAR_MANAGE));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_LEAVE_APP));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_BUSINESS_TRAVEL));
        mViewPager.setAdapter(new AttandenceAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(4);
        title_tv = (TextView) findViewById(R.id.title_tv);

        approve_leave_app_tv = (TextView) findViewById(R.id.approve_leave_app_tv);
        approve_business_tra__tv = (TextView) findViewById(R.id.approve_business_tra__tv);
        leave_app_tv = (TextView) findViewById(R.id.leave_app_tv);
        business_tra_tv = (TextView) findViewById(R.id.business_tra_tv);

        approve_leave_app_layout = findViewById(R.id.approve_leave_app_layout);
        approve_business_tra_layout = findViewById(R.id.approve_business_tra_layout);
        leave_app_layout = findViewById(R.id.leave_app_layout);
        business_tra_layout = findViewById(R.id.business_tra_layout);
        approve_leave_app_layout.setOnClickListener(this);
        approve_business_tra_layout.setOnClickListener(this);
        leave_app_layout.setOnClickListener(this);
        business_tra_layout.setOnClickListener(this);

        approve_leave_app_line = findViewById(R.id.approve_leave_app_line);
        approve_business_tra_tab_line = findViewById(R.id.approve_business_tra_tab_line);
        leave_app_tab_line = findViewById(R.id.leave_app_tab_line);
        business_tra_line1 = findViewById(R.id.business_tra_line1);
        title_tv.setText("已审批的");
        swichTab(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approve_leave_app_layout:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.approve_business_tra_layout:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.leave_app_layout:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.business_tra_layout:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    private void swichTab(int pos) {
        mCurentPage = pos;
        if (pos == 0) {
            approve_leave_app_tv.setTextColor(selectedColor);
            approve_business_tra__tv.setTextColor(unSelectedColor);
            leave_app_tv.setTextColor(unSelectedColor);
            business_tra_tv.setTextColor(unSelectedColor);
            approve_leave_app_line.setVisibility(View.VISIBLE);
            approve_business_tra_tab_line.setVisibility(View.GONE);
            leave_app_tab_line.setVisibility(View.GONE);
            business_tra_line1.setVisibility(View.GONE);
        } else if (pos == 1) {
            approve_leave_app_tv.setTextColor(unSelectedColor);
            approve_business_tra__tv.setTextColor(selectedColor);
            leave_app_tv.setTextColor(unSelectedColor);
            business_tra_tv.setTextColor(unSelectedColor);
            approve_leave_app_line.setVisibility(View.GONE);
            approve_business_tra_tab_line.setVisibility(View.VISIBLE);
            leave_app_tab_line.setVisibility(View.GONE);
            business_tra_line1.setVisibility(View.GONE);
        } else if (pos == 2) {
            approve_leave_app_tv.setTextColor(unSelectedColor);
            approve_business_tra__tv.setTextColor(unSelectedColor);
            leave_app_tv.setTextColor(selectedColor);
            business_tra_tv.setTextColor(unSelectedColor);
            approve_leave_app_line.setVisibility(View.GONE);
            approve_business_tra_tab_line.setVisibility(View.GONE);
            leave_app_tab_line.setVisibility(View.VISIBLE);
            business_tra_line1.setVisibility(View.GONE);
        } else if (pos == 3) {
            approve_leave_app_tv.setTextColor(unSelectedColor);
            approve_business_tra__tv.setTextColor(unSelectedColor);
            leave_app_tv.setTextColor(unSelectedColor);
            business_tra_tv.setTextColor(selectedColor);
            approve_leave_app_line.setVisibility(View.GONE);
            approve_business_tra_tab_line.setVisibility(View.GONE);
            leave_app_tab_line.setVisibility(View.GONE);
            business_tra_line1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        swichTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            fragments.get(mCurentPage).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }
    }
}
