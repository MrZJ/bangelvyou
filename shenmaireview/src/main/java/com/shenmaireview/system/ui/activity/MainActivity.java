package com.shenmaireview.system.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shenmaireview.system.R;
import com.shenmaireview.system.bean.TabEntity;
import com.shenmaireview.system.ui.fragment.AboutFragment;
import com.shenmaireview.system.ui.fragment.HomeFragment;
import com.shenmaireview.system.ui.fragment.ScanFragment;
import com.shenmaireview.system.utils.CacheImgUtil;
import com.shenmaireview.system.utils.ConfigUtil;
import com.shenmaireview.system.utils.StringUtil;

import java.util.ArrayList;

import butterknife.Bind;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"头条","扫一扫","关于我们"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.notice_n, R.mipmap.scan_n,R.mipmap.mine_n};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.notice_p, R.mipmap.scan_p, R.mipmap.mine_p};

    private HomeFragment homeFragment;
    private ScanFragment scanFragment;
    private AboutFragment aboutFragment;
    private static boolean isExit = false;
    public int state = 0;//记录当前在哪个页面

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
        tabLayout.setCurrentTab(state);
        SwitchTo(state);
        tabLayout.measure(0,0);
        updateApk();
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

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (state == 1) {
            SwitchTo(state);
        }
    }

    /**
     * 切换
     */
    public void SwitchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        state = position;
        hideFragments(transaction);
        switch (position) {
            //头条
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_body, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            //资讯
            case 1:
                scanFragment = new ScanFragment();
                transaction.add(R.id.fl_body, scanFragment);
                break;
            //日报
            case 2:
                if (aboutFragment == null) {
                    aboutFragment = new AboutFragment();
                    transaction.add(R.id.fl_body, aboutFragment);
                } else {
                    transaction.show(aboutFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
        if (scanFragment != null) {
            transaction.hide(scanFragment);
            transaction.remove(scanFragment);
            scanFragment = null;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final String picturePath = CacheImgUtil.getImageAbsolutePath(MainActivity.this, data.getData());
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (StringUtil.isEmpty(result)) {
                        Toast.makeText(MainActivity.this, "图片扫描失败", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!result.startsWith(ConfigUtil.HTTP)) {
                            Toast.makeText(MainActivity.this, "无效的二维码", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, ScanResultActivity.class);
                            intent.putExtra("scanUrl", result);
                            startActivity(intent);
                        }
                    }
                }
            }.execute();
        }
    }

}
