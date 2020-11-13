package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shishoureport.system.R;
import com.shishoureport.system.databinding.ActivityApproveBinding;
import com.shishoureport.system.entity.TabEntity;
import com.shishoureport.system.ui.adapter.AttandenceAdapter;
import com.shishoureport.system.ui.fragment.ApproveApplyPurchaseListFragment;
import com.shishoureport.system.ui.fragment.ApproveApplyUseListFragment;
import com.shishoureport.system.ui.fragment.ApproveApplyWorkerListFragment;
import com.shishoureport.system.ui.fragment.ApproveCarmanageListFragment;
import com.shishoureport.system.ui.fragment.ApproveOverTimeListFragment;
import com.shishoureport.system.ui.fragment.BusTraListFragment;
import com.shishoureport.system.ui.fragment.LeaveListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/8/31.
 * copyright easybiz.
 */

public class ApproveActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private int selectedColor;
    private int unSelectedColor;
    private int mCurentPage = -1;
    private ActivityApproveBinding mBinding;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApproveActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return -1;
    }

    public void initView() {
        mBinding = ActivityApproveBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        ArrayList<CustomTabEntity> list = new ArrayList<>();
        list.add(new TabEntity("加班单"));
        list.add(new TabEntity("用车单"));
        list.add(new TabEntity("请假单"));
        list.add(new TabEntity("出差单"));
        list.add(new TabEntity("人员申请"));
        list.add(new TabEntity("采购申请"));
        list.add(new TabEntity("领用申请"));
        mBinding.tabLayout.setTabData(list);
        selectedColor = Color.parseColor("#8CCAF2");
        unSelectedColor = Color.parseColor("#555555");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.addOnPageChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(ApproveOverTimeListFragment.getInstance());
        fragments.add(ApproveCarmanageListFragment.getInstance());
        fragments.add(LeaveListFragment.getInstance(LeaveListFragment.TYPE_LEAVE_APP_WAITE_APPROVE));
        fragments.add(BusTraListFragment.getInstance(BusTraListFragment.TYPE_BUS_TRA_WAITE_APPROVE));

        fragments.add(ApproveApplyWorkerListFragment.getInstance());
        fragments.add(ApproveApplyPurchaseListFragment.getInstance());
        fragments.add(ApproveApplyUseListFragment.getInstance());

        mViewPager.setAdapter(new AttandenceAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(4);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);

        title_tv.setText("待我审批");
        mBinding.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.setCurrentItem(0);
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
