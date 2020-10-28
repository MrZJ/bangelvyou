package com.shishoureport.system.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.TabEntity;
import com.shishoureport.system.ui.fragment.ChatFragment;
import com.shishoureport.system.ui.fragment.HomeFragment;
import com.shishoureport.system.ui.fragment.HomePageFragment;
import com.shishoureport.system.ui.fragment.MarketFragment;
import com.shishoureport.system.ui.fragment.MineFragment;
import com.shishoureport.system.ui.fragment.NewGoodFragment;
import com.shishoureport.system.ui.fragment.SearchFragment;
import com.shishoureport.system.ui.fragment.ShopingCarFragment;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    CommonTabLayout tabLayout;

    private String[] mTitles = {"首页", " 新品", "搜索", "聊天", "个人中心"};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.home, R.drawable.ng, R.drawable.s, R.drawable.c, R.drawable.p};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.drawable.home1, R.drawable.ng1, R.drawable.s1, R.drawable.c1, R.drawable.p1};
    private static int tabLayoutHeight;
    public int state = 0;//记录当前在哪个页面
    private static boolean isExit = false;
    private HomePageFragment homeFragment;
    private MarketFragment marketFragment;
    public ShopingCarFragment shopingCarFragment;
    private MineFragment mineFragment;
    private NewGoodFragment newGoodFragment;
    private SearchFragment searchFragment;
    private ChatFragment chatFragment;


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
        tabLayout.measure(0, 0);
        tabLayoutHeight = tabLayout.getMeasuredHeight();
        isUpdateApk();
        pushActivity();
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        tabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);
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
        homeFragment = new HomePageFragment();
        marketFragment = new MarketFragment();
        shopingCarFragment = new ShopingCarFragment();
        mineFragment = new MineFragment();
        newGoodFragment = new NewGoodFragment();
        searchFragment = new SearchFragment();
        chatFragment = new ChatFragment();

        transaction.add(R.id.fl_body, homeFragment, "homeFragment");
        transaction.add(R.id.fl_body, newGoodFragment, "newGoodFragment");
        transaction.add(R.id.fl_body, searchFragment, "searchFragment");
        transaction.add(R.id.fl_body, chatFragment, "chatFragment");
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
            //首页
            case 0:
                transaction.show(homeFragment);
                transaction.hide(newGoodFragment);
                transaction.hide(searchFragment);
                transaction.hide(chatFragment);
                transaction.hide(mineFragment);
                homeFragment.onResume();
                transaction.commitAllowingStateLoss();
                break;
            //新品
            case 1:
                transaction.hide(homeFragment);
                transaction.show(newGoodFragment);
                transaction.hide(searchFragment);
                transaction.hide(chatFragment);
                transaction.hide(mineFragment);
                newGoodFragment.onResume();
                transaction.commitAllowingStateLoss();
                break;
            //搜索
            case 2:
                transaction.hide(homeFragment);
                transaction.hide(newGoodFragment);
                transaction.show(searchFragment);
                transaction.hide(chatFragment);
                transaction.hide(mineFragment);
                searchFragment.onResume();
                transaction.commitAllowingStateLoss();
                break;
            //聊天
            case 3:
                transaction.hide(homeFragment);
                transaction.hide(newGoodFragment);
                transaction.hide(searchFragment);
                transaction.show(chatFragment);
                transaction.hide(mineFragment);
                chatFragment.onResume();
                transaction.commitAllowingStateLoss();
                break;
            //个人中心
            case 4:
                transaction.hide(homeFragment);
                transaction.hide(newGoodFragment);
                transaction.hide(searchFragment);
                transaction.hide(chatFragment);
                transaction.show(mineFragment);
                mineFragment.onResume();
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
                newGoodFragment.onResume();
                break;
            case 2:
                searchFragment.onResume();
                break;
            case 3:
                chatFragment.onResume();
                break;
            case 4:
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

    /**
     * 如果是推送启动的应用，接收推送信息，跳转相应界面
     */
    private void pushActivity() {
        String key = getIntent().getStringExtra("key");
        String uData = getIntent().getStringExtra("uData");
        if (null != uData && !"".equals(uData)) {
            Intent intent = new Intent(this, PushActivity.class);
            intent.putExtra("msg_data", uData);
            startActivity(intent);
        }
    }
}
