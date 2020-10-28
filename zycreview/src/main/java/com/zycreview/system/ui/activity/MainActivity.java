package com.zycreview.system.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zycreview.system.R;
import com.zycreview.system.ui.fragment.MineFragment;
import com.zycreview.system.ui.fragment.NoticeFragment;
import com.zycreview.system.ui.fragment.PolicyFragment;
import com.zycreview.system.ui.fragment.ScanFragment;
import com.zycreview.system.ui.fragment.SearchFragment;
import com.zycreview.system.utils.CacheImgUtil;
import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.StringUtil;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private View tab_notice, tab_policy, tab_search, tab_mine, tab_scan;
    private TextView tab_notice_text, tab_policy_text, tab_search_text, tab_mine_text, tab_scan_text;
    private ImageView tab_notice_image, tab_policy_image, tab_search_image, tab_mine_image, tab_scan_image;
    //Tab选项卡的文字
    private String mTextviewArray[] = {"政策公告", "药材认知", "扫描", "查询企业", "我的"};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.notice_n, R.mipmap.policy_n, R.mipmap.scan_n,
            R.mipmap.search_n, R.mipmap.mine_n};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.notice_p, R.mipmap.policy_p, R.mipmap.scan_p,
            R.mipmap.search_p, R.mipmap.mine_p};
    private FragmentManager fm;
    private NoticeFragment noticeFragment;
    private PolicyFragment policyFragment;
    private ScanFragment scanFragment;
    private SearchFragment searchFragment;
    private MineFragment mineFragment;
    public int state = 0;//记录当前在哪个页面
    private static boolean isExit = false;
    private final int CAMERA = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        intiView();
        setData();
        initListener();
        setTabSelection(0);
        isUpdateApk();
    }

    private void intiView() {
        tab_notice = findViewById(R.id.tab_notice);
        tab_notice_text = (TextView) tab_notice.findViewById(R.id.textview);
        tab_notice_image = (ImageView) tab_notice.findViewById(R.id.imageview);
        tab_policy = findViewById(R.id.tab_policy);
        tab_policy_text = (TextView) tab_policy.findViewById(R.id.textview);
        tab_policy_image = (ImageView) tab_policy.findViewById(R.id.imageview);
        tab_scan = findViewById(R.id.tab_scan);
        tab_scan_text = (TextView) tab_scan.findViewById(R.id.textview);
        tab_scan_image = (ImageView) tab_scan.findViewById(R.id.imageview);
        tab_search = findViewById(R.id.tab_search);
        tab_search_text = (TextView) tab_search.findViewById(R.id.textview);
        tab_search_image = (ImageView) tab_search.findViewById(R.id.imageview);
        tab_mine = findViewById(R.id.tab_mine);
        tab_mine_text = (TextView) tab_mine.findViewById(R.id.textview);
        tab_mine_image = (ImageView) tab_mine.findViewById(R.id.imageview);
    }

    /**
     * 设置tab数据
     */
    private void setData() {
        tab_notice_text.setText(mTextviewArray[0]);
        tab_policy_text.setText(mTextviewArray[1]);
        tab_scan_text.setText(mTextviewArray[2]);
        tab_search_text.setText(mTextviewArray[3]);
        tab_mine_text.setText(mTextviewArray[4]);
        clearSelection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (state == 2) {
            setTabSelection(2);
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tab_notice_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_policy_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_scan_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_search_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_mine_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_notice_image.setBackgroundResource(mImageViewArray[0]);
        tab_policy_image.setBackgroundResource(mImageViewArray[1]);
        tab_scan_image.setBackgroundResource(mImageViewArray[2]);
        tab_search_image.setBackgroundResource(mImageViewArray[3]);
        tab_mine_image.setBackgroundResource(mImageViewArray[4]);
    }

    public void initListener() {
        tab_notice.setOnClickListener(this);
        tab_policy.setOnClickListener(this);
        tab_scan.setOnClickListener(this);
        tab_search.setOnClickListener(this);
        tab_mine.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_notice:
                // 当点击了公告tab时，选中第1个tab
                if (state == 0) {
                    return;
                } else {
                    state = 0;
                }
                setTabSelection(0);
                break;
            case R.id.tab_policy:
                // 当点击了政策tab时，选中第2个tab
                if (state == 1) {
                    return;
                } else {
                    state = 1;
                }
                setTabSelection(1);
                break;
            case R.id.tab_scan:
                // 当点击了扫描tab时，选中第3个tab
                if (state == 2) {
                    return;
                } else {
                    state = 2;
                }
                setTabSelection(2);
                break;
            case R.id.tab_search:
                // 当点击了搜索tab时，选中第4个tab
                if (state == 3) {
                    return;
                } else {
                    state = 3;
                }
                setTabSelection(3);
                break;
            case R.id.tab_mine:
                // 当点击了我的tab时，选中第5个tab
                if (state == 4) {
                    return;
                } else {
                    state = 4;
                }
                setTabSelection(4);
                break;
            default:
                break;

        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。
     */
    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了公告tab时，改变控件的图片和文字颜色
                tab_notice_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_notice_image.setBackgroundResource(mPressImageViewArray[0]);
                if (noticeFragment == null) {
                    // 如果noticeFragment为空，则创建一个并添加到界面上
                    noticeFragment = new NoticeFragment();
                    transaction.add(R.id.tabcontent, noticeFragment);
                } else {
                    // 如果noticeFragment不为空，则直接将它显示出来
                    transaction.show(noticeFragment);
                }
                break;
            case 1:
                // 当点击了政策tab时，改变控件的图片和文字颜色
                tab_policy_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_policy_image.setBackgroundResource(mPressImageViewArray[1]);
                if (policyFragment == null) {
                    // 如果policyFragment为空，则创建一个并添加到界面上
                    policyFragment = new PolicyFragment();
                    transaction.add(R.id.tabcontent, policyFragment);
                } else {
                    // 如果policyFragment不为空，则直接将它显示出来
                    transaction.show(policyFragment);
                }
                break;
            case 2:
                // 当点击了当地tab时，改变控件的图片和文字颜色
                tab_scan_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_scan_image.setBackgroundResource(mPressImageViewArray[2]);
                if (scanFragment != null) {
                    removeFragment(transaction, scanFragment);
                }
                scanFragment = null;
                scanFragment = new ScanFragment();
                transaction.add(R.id.tabcontent, scanFragment);
                break;
            case 3:
                // 当点击了搜索tab时，改变控件的图片和文字颜色
                tab_search_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_search_image.setBackgroundResource(mPressImageViewArray[3]);
                if (searchFragment != null) {
                    removeFragment(transaction, searchFragment);
                }
                searchFragment = null;
                searchFragment = new SearchFragment();
                transaction.add(R.id.tabcontent, searchFragment);
                break;
            case 4:
                // 当点击了我的tab时，改变控件的图片和文字颜色
                tab_mine_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_mine_image.setBackgroundResource(mPressImageViewArray[4]);
                if (mineFragment != null) {
                    removeFragment(transaction, mineFragment);
                }
                mineFragment = null;
                mineFragment = new MineFragment();
                transaction.add(R.id.tabcontent, mineFragment);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 删除Fragment
     **/
    public void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        transaction.remove(fragment);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (noticeFragment != null) {
            transaction.hide(noticeFragment);
        }
        if (policyFragment != null) {
            transaction.hide(policyFragment);
        }
        if (scanFragment != null) {
            transaction.hide(scanFragment);
            transaction.remove(scanFragment);
        }
        if (searchFragment != null) {
            transaction.hide(searchFragment);
            transaction.remove(searchFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
            transaction.remove(mineFragment);
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
            if (ConfigUtil.IS_DOWNLOAD) {
                Toast.makeText(getApplicationContext(), "最新安装包正在下载...", Toast.LENGTH_SHORT).show();
            } else {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                handler.sendEmptyMessageDelayed(0, 2000);
            }
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
