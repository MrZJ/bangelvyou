package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityHaveApprovedBinding;
import com.shishoureport.system.entity.TabEntity;
import com.shishoureport.system.request.AttendanceAuditListRequest;
import com.shishoureport.system.ui.adapter.AttandenceAdapter;
import com.shishoureport.system.ui.fragment.ApproveApplyPurchaseListFragment;
import com.shishoureport.system.ui.fragment.ApproveApplyUseListFragment;
import com.shishoureport.system.ui.fragment.ApproveApplyWorkerListFragment;
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
    private List<Fragment> fragments;
    private int mCurentPage = -1;
    private ActivityHaveApprovedBinding mBinding;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HaveApprovedActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return -1;
    }

    public void initView() {
        mBinding = ActivityHaveApprovedBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<CustomTabEntity> list = new ArrayList<>();
        list.add(new TabEntity("加班单"));
        list.add(new TabEntity("用车单"));
        list.add(new TabEntity("请假单"));
        list.add(new TabEntity("出差单"));
        list.add(new TabEntity("人员申请"));
        list.add(new TabEntity("采购申请"));
        list.add(new TabEntity("领用申请"));
        mViewPager.addOnPageChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_OVERT_TIME));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_CAR_MANAGE));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_LEAVE_APP));
        fragments.add(AuditListFragment.getInstance(AttendanceAuditListRequest.TYPE_BUSINESS_TRAVEL));

        fragments.add(ApproveApplyWorkerListFragment.getInstance());
        fragments.add(ApproveApplyPurchaseListFragment.getInstance());
        fragments.add(ApproveApplyUseListFragment.getInstance());
        mViewPager.setAdapter(new AttandenceAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(4);
        mBinding.titleLayout.titleTv.setText("已审批的");
        mBinding.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        switchTab(0);
    }

    @Override
    public void onClick(View v) {
    }

    private void switchTab(int pos) {
        mCurentPage = pos;
        mBinding.tabLayout.setCurrentTab(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switchTab(position);
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
