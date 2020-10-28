package com.shishoureport.system.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.TabEntity;
import com.shishoureport.system.ui.fragment.HomeFragment;
import com.shishoureport.system.ui.fragment.InforFragment;
import com.shishoureport.system.ui.fragment.MineFragment;
import com.shishoureport.system.ui.fragment.ReportFragment;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"头条","资讯","日报","我的"};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.home_n, R.mipmap.infor_n,R.mipmap.report_n,R.mipmap.mine_n};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.home_p, R.mipmap.infor_p,R.mipmap.report_p, R.mipmap.mine_p};
    private static int tabLayoutHeight;
    public int state = 0;//记录当前在哪个页面
    private static boolean isExit = false;
    private HomeFragment homeFragment;
    private InforFragment inforFragment;
    public ReportFragment reportFragment;
    private MineFragment mineFragment;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        initTab();
        //初始化frament
        initFragment();
        tabLayout.measure(0,0);
        tabLayoutHeight=tabLayout.getMeasuredHeight();
        isUpdateApk();
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mPressImageViewArray[i], mImageViewArray[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
                reSwitchTo(position);
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        inforFragment = new InforFragment();
        reportFragment = new ReportFragment();
        mineFragment = new MineFragment();

        transaction.add(R.id.fl_body, homeFragment, "homeFragment");
        transaction.add(R.id.fl_body, inforFragment, "inforFragment");
        transaction.add(R.id.fl_body, reportFragment, "reportFragment");
        transaction.add(R.id.fl_body, mineFragment, "mineFragment");
        transaction.commit();
        SwitchTo(state);
        tabLayout.setCurrentTab(state);
    }

    /**
     * 切换
     */
    public void SwitchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        state = position;
        switch (position) {
            //头条
            case 0:
                transaction.show(homeFragment);
                transaction.hide(inforFragment);
                transaction.hide(reportFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            //资讯
            case 1:
                transaction.show(inforFragment);
                transaction.hide(homeFragment);
                transaction.hide(reportFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            //日报
            case 2:
                transaction.show(reportFragment);
                reportFragment.onResume();
                transaction.hide(homeFragment);
                transaction.hide(inforFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            //我的
            case 3:
                transaction.show(mineFragment);
                mineFragment.onResume();
                transaction.hide(homeFragment);
                transaction.hide(inforFragment);
                transaction.hide(reportFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 刷新
     */
    public void reSwitchTo(int position) {
        switch (position) {
            //头条
            case 0:
                homeFragment.onResume();
                break;
            case 1:
                inforFragment.onResume();
                break;
            case 2:
                reportFragment.onResume();
                break;
            case 3:
                mineFragment.onResume();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
