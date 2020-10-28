package com.shenmaireview.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;

import com.shenmaireview.system.R;
import com.shenmaireview.system.ui.activity.NewsListActivity;

import butterknife.Bind;

/**
 *
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{

    /*@Bind(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @Bind(R.id.pager)
    ViewPager pager;*/
    @Bind(R.id.rel_xwzx)
    RelativeLayout xwzx;
    @Bind(R.id.rel_zxgg)
    RelativeLayout zxgg;
    @Bind(R.id.rel_cpzs)
    RelativeLayout cpzs;
    @Bind(R.id.rel_jcbg)
    RelativeLayout jcbg;
    @Bind(R.id.rel_rzjg)
    RelativeLayout rzjg;


//    private NewsFragmentAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        setTopTitle("首页");
        setLeftBackButton(false);
        /*adapter = new NewsFragmentAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tabStrip.setViewPager(pager);*/
        initListener();
    }

    private void initListener(){
        xwzx.setOnClickListener(this);
        zxgg.setOnClickListener(this);
        cpzs.setOnClickListener(this);
        jcbg.setOnClickListener(this);
        rzjg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), NewsListActivity.class);
        switch (v.getId()){
            case R.id.rel_xwzx:
                intent.putExtra("modId","1001003001");
                intent.putExtra("title","新闻资讯");
                break;
            case R.id.rel_zxgg:
                intent.putExtra("modId","1001003002");
                intent.putExtra("title","在线公告");
                break;
            case R.id.rel_cpzs:
                intent.putExtra("modId","1001003003");
                intent.putExtra("title","产品知识");
                break;
            case R.id.rel_jcbg:
                intent.putExtra("modId","1001003004");
                intent.putExtra("title","检测报告");
                break;
            case R.id.rel_rzjg:
                intent.putExtra("modId","1001003005");
                intent.putExtra("title","认证机构");
                break;
        }
        startActivity(intent);
    }

    private class NewsFragmentAdapter extends FragmentPagerAdapter{

        private final String[] TITLES = {"新闻资讯", "在线公告", "产品知识", "检测报告", "认证机构"};
        private final String[] MODID = {"1001003001", "1001003002", "1001003003", "1001003004", "1001003005"};

        public NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle b = new Bundle();
            b.putString("modId", MODID[position]);
            return NewsFragment.getInstance(b);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

    }

}
