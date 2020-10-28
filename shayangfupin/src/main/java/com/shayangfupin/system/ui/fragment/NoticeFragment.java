package com.shayangfupin.system.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.shayangfupin.system.R;
import com.shayangfupin.system.adapter.NoticeFragmentAdapter;
import com.shayangfupin.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 资讯界面
 */
public class NoticeFragment extends BaseFragment {

    private PagerSlidingTabStrip notice_tabs;
    private ViewPager notice_pager;
    private View mView, emptyView;
    private ImageView loadView;
    private NoticeFragmentAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();
    private Animation mRotateAnimation;
    public Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notice, container, false);
        initView();
        initListener();
        setAdapter();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        hideBackBtn();
        setTitle("资讯");
        notice_pager = (ViewPager) mView.findViewById(R.id.notice_pager);
        notice_tabs = (PagerSlidingTabStrip) mView.findViewById(R.id.notice_tabs);
    }

    private void setAdapter() {
        String[] notices = getResources().getStringArray(R.array.notice);
        if (notices != null) {
            for (int i = 0; i < notices.length; i++) {
                list.add(notices[i]);
            }
            adapter = new NoticeFragmentAdapter(getFragmentManager(), list);
            notice_pager.setAdapter(adapter);
            notice_tabs.setViewPager(notice_pager);
            notice_pager.setCurrentItem(0);
        }
    }

    private void initListener() {
        initTopListener();
    }

    /**
     * 显示加载布局
     */
    public void showLoading() {
        handler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText("正在加载中...");
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
                }
            }
        });

    }

    /**
     * dimiss加载布局
     */
    public void hiddenLoading() {
        handler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });
    }

}
