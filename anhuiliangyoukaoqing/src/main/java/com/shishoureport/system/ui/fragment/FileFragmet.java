package com.shishoureport.system.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.activity.ReleaseFileActivity;
import com.shishoureport.system.ui.adapter.AttandenceAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.fragment.FileListFragment.ACTIPN_REFRESH_LIST;

/**
 * 资讯
 */
public class FileFragmet extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ImageView img_top_right_one;
    private TextView attendance_tv, count_tv, title_tv, solved_tv, waite_approve_tv;
    private View img_top_goback, waite_approve_layout, tab_line0, attendance_layout, account_layout, tab_line, tab_line1, solved_line, solved_layout;
    private List<Fragment> fragments;
    private int selectedColor;
    private int unSelectedColor;


    @Override
    protected int getLayoutResource() {
        selectedColor = Color.parseColor("#4B87DE");
        unSelectedColor = Color.parseColor("#555555");
        return R.layout.fragment_file;
    }

    protected void initView(View v) {
        title_tv = (TextView) v.findViewById(R.id.top_title);
        img_top_goback = v.findViewById(R.id.back_tv);
        v.findViewById(R.id.btn_top_sidebar).setVisibility(View.VISIBLE);
        img_top_right_one = (ImageView) v.findViewById(R.id.img_top_right_one);
        img_top_right_one.setImageResource(R.mipmap.add_file);
        img_top_right_one.setOnClickListener(this);
        img_top_goback.setVisibility(View.GONE);
        title_tv.setText("任务");
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        fragments = new ArrayList<>();
        fragments.add(FileListFragment.getInstance(FileListFragment.TYPE_FILE_SEND));
        fragments.add(FileListFragment.getInstance(FileListFragment.TYPE_WAITE_SOLVED));
        fragments.add(FileListFragment.getInstance(FileListFragment.TYPE_FILE_REC));
        fragments.add(FileListFragment.getInstance(FileListFragment.TYPE_WTAITE_AUDIT));
        mViewPager.setAdapter(new AttandenceAdapter(getFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.addOnPageChangeListener(this);
        attendance_tv = (TextView) v.findViewById(R.id.attendance_tv);
        title_tv = (TextView) v.findViewById(R.id.title_tv);
        count_tv = (TextView) v.findViewById(R.id.count_tv);
        waite_approve_tv = (TextView) v.findViewById(R.id.waite_approve_tv);
        waite_approve_layout = v.findViewById(R.id.waite_approve_layout);
        tab_line0 = v.findViewById(R.id.tab_line0);
        waite_approve_layout.setOnClickListener(this);
        attendance_layout = v.findViewById(R.id.attendance_layout);
        account_layout = v.findViewById(R.id.account_layout);
        tab_line = v.findViewById(R.id.tab_line);
        tab_line1 = v.findViewById(R.id.tab_line1);
        solved_tv = (TextView) v.findViewById(R.id.solved_tv);
        solved_line = v.findViewById(R.id.solved_line);
        solved_layout = v.findViewById(R.id.solved_layout);
        attendance_layout.setOnClickListener(this);
        account_layout.setOnClickListener(this);
        solved_layout.setOnClickListener(this);
        setTopTitle("任务");
        mViewPager.setCurrentItem(0);
        registerBroadCast();
    }

    public void setCheckVisbal(int visbal) {
        if (waite_approve_layout != null) {
            waite_approve_layout.setVisibility(visbal);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attendance_layout:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.account_layout:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.solved_layout:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.waite_approve_layout:
                mViewPager.setCurrentItem(3, false);
                break;
            case R.id.img_top_right_one:
                ReleaseFileActivity.startActivity(getActivity());
                break;
        }
    }

    private void swichTab(int pos) {
        if (pos == 0) {
            waite_approve_tv.setTextColor(unSelectedColor);
            tab_line0.setVisibility(View.GONE);
            attendance_tv.setTextColor(selectedColor);
            count_tv.setTextColor(unSelectedColor);
            solved_tv.setTextColor(unSelectedColor);
            tab_line.setVisibility(View.VISIBLE);
            tab_line1.setVisibility(View.GONE);
            solved_line.setVisibility(View.GONE);
        } else if (pos == 1) {
            attendance_tv.setTextColor(unSelectedColor);
            count_tv.setTextColor(selectedColor);
            solved_tv.setTextColor(unSelectedColor);
            tab_line.setVisibility(View.GONE);
            tab_line1.setVisibility(View.VISIBLE);
            solved_line.setVisibility(View.GONE);
            waite_approve_tv.setTextColor(unSelectedColor);
            tab_line0.setVisibility(View.GONE);
        } else if (pos == 2) {
            attendance_tv.setTextColor(unSelectedColor);
            count_tv.setTextColor(unSelectedColor);
            solved_tv.setTextColor(selectedColor);
            tab_line.setVisibility(View.GONE);
            tab_line1.setVisibility(View.GONE);
            solved_line.setVisibility(View.VISIBLE);
            waite_approve_tv.setTextColor(unSelectedColor);
            tab_line0.setVisibility(View.GONE);
        } else if (pos == 3) {
            waite_approve_tv.setTextColor(selectedColor);
            tab_line0.setVisibility(View.VISIBLE);
            attendance_tv.setTextColor(unSelectedColor);
            count_tv.setTextColor(unSelectedColor);
            solved_tv.setTextColor(unSelectedColor);
            tab_line.setVisibility(View.GONE);
            tab_line1.setVisibility(View.GONE);
            solved_line.setVisibility(View.GONE);
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

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTIPN_REFRESH_LIST);
        getActivity().registerReceiver(receiver, filter);
    }

    private void unRegisterBroadcast() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i = 0; i < fragments.size(); i++) {
                FileListFragment fragment = (FileListFragment) fragments.get(i);
                fragment.refreshList();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterBroadcast();
    }
}