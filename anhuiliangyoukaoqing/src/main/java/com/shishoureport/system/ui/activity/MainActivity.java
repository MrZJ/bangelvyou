package com.shishoureport.system.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.MyBaseData;
import com.shishoureport.system.entity.TabEntity;
import com.shishoureport.system.request.GetBaseDataRequest;
import com.shishoureport.system.ui.fragment.FileFragmet;
import com.shishoureport.system.ui.fragment.MeetingFragment;
import com.shishoureport.system.ui.fragment.MineFragment;
import com.shishoureport.system.ui.fragment.WorkFragment;
import com.shishoureport.system.utils.CheckPermission;
import com.shishoureport.system.utils.MySharepreference;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements CheckPermission.PermissionCallBack {

    CommonTabLayout tabLayout;

    private String[] mTitles = {"办公", "任务", "会议", "我的"};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.home_n, R.mipmap.infor_n, R.mipmap.report_n, R.mipmap.mine_n};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.home_p, R.mipmap.infor_p, R.mipmap.report_p, R.mipmap.mine_p};
    private static int tabLayoutHeight;
    public int state = 0;//记录当前在哪个页面
    private static boolean isExit = false;
    private WorkFragment workFragment;
    private FileFragmet fileFragment;
    public MeetingFragment meetingFragment;
    private MineFragment mineFragment;

    public static void startAcitivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

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
        loadBaseData();
        isUpdateApk();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPermission.writeExternalStorage(this, this);
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
        fileFragment = new FileFragmet();
        workFragment = WorkFragment.getInstance(fileFragment);
        meetingFragment = new MeetingFragment();
        mineFragment = new MineFragment();

        transaction.add(R.id.fl_body, workFragment, "workFragment");
        transaction.add(R.id.fl_body, fileFragment, "fileFragment");
        transaction.add(R.id.fl_body, meetingFragment, "meetingFragment");
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
                transaction.show(workFragment);
                transaction.hide(fileFragment);
                transaction.hide(meetingFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            //资讯
            case 1:
                transaction.show(fileFragment);
                transaction.hide(workFragment);
                transaction.hide(meetingFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            //日报
            case 2:
                transaction.show(meetingFragment);
                meetingFragment.onResume();
                transaction.hide(workFragment);
                transaction.hide(fileFragment);
                transaction.hide(mineFragment);
                transaction.commitAllowingStateLoss();

                try {
                    Intent intent = new Intent("android.intent.action.view");
                    ComponentName cpn = new ComponentName("com.netmeeting", "com.netmeeting.activity.LoginLoadingActivity");
                    intent.putExtra("meetingName", "1454792260@qq.com");
                    intent.putExtra("meetingPwd", "724796577");
                    intent.setComponent(cpn);
                    intent.addCategory("android.intent.category.LAUNCHER");
                    this.startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("请安装视频软件");
                }
                break;
            //我的
            case 3:
                transaction.show(mineFragment);
                mineFragment.onResume();
                transaction.hide(workFragment);
                transaction.hide(fileFragment);
                transaction.hide(meetingFragment);
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
                workFragment.onResume();
                break;
            case 1:
                fileFragment.onResume();
                break;
            case 2:
                meetingFragment.onResume();
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

    private void loadBaseData() {
        GetBaseDataRequest request = new GetBaseDataRequest();
        httpGetRequest(request.getRequestUrl(), GetBaseDataRequest.GET_BASE_DATA_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case GetBaseDataRequest.GET_BASE_DATA_REQUEST:
                handlerRequest(json);
                break;
        }
    }

    private void handlerRequest(String json) {
        MyBaseData myBaseData = JSONObject.parseObject(json, MyBaseData.class);
        if (myBaseData != null && myBaseData.map != null) {
            MySharepreference.getInstance(this).putString(MySharepreference.ENTITY90LIST, JSONObject.toJSONString(myBaseData.map.entity90List));
            MySharepreference.getInstance(this).putString(MySharepreference.ENTITY100LIST, JSONObject.toJSONString(myBaseData.map.entity100List));
            MySharepreference.getInstance(this).putString(MySharepreference.ENTITY110LIST, JSONObject.toJSONString(myBaseData.map.entity110List));
            MySharepreference.getInstance(this).putString(MySharepreference.ENTITY500LIST, JSONObject.toJSONString(myBaseData.map.entity500List));
            MySharepreference.getInstance(this).putString(MySharepreference.ENTITY_USER, JSONObject.toJSONString(myBaseData.map.busInfoList));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CheckPermission.REQUEST_EXTERNAL_STORAGE) {
            //判断权限是否申请成功
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("读写权限申请成功");
            } else {
                showToast("读写权限申请失败");
            }
            return;

        }
    }

    @Override
    public void setOnPermissionListener(boolean allow) {

    }
}
