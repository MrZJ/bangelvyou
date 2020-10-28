package com.basulvyou.system.ui.activity;

import android.content.Intent;
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

import com.basulvyou.system.R;
import com.basulvyou.system.ui.fragment.AskFragment;
import com.basulvyou.system.ui.fragment.HomeFragment;
import com.basulvyou.system.ui.fragment.LocationFragment;
import com.basulvyou.system.ui.fragment.MineFragment;
import com.basulvyou.system.util.ConfigUtil;


/**
 * Created by Administrator on 2016/1/25.
 * 主活动
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private View tab_home, tab_location, tab_special, tab_mine, tab_share;
    private TextView tab_home_text, tab_location_text, tab_special_text, tab_mine_text , tab_share_text;
    private ImageView tab_home_image, tab_location_image, tab_special_image,tab_mine_image, tab_share_image;
    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "当地", "", "问吧", "我的"};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.home_g, R.mipmap.location_g,R.mipmap.share_text,
            R.mipmap.ask_g, R.mipmap.my_g};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.home, R.mipmap.location,R.mipmap.share_text,
            R.mipmap.ask, R.mipmap.my};
    public int state = 0;//记录当前在哪个页面  首页:0 当地:1 特产:2 我的:3;
    private FragmentManager fm;
    private HomeFragment homeFragment;
    private LocationFragment locationFragment;
    private MineFragment mineFragment;
    private AskFragment askFragment;
    private static boolean isExit = false;
    public static int index = -1;//选择的fragment
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

    @Override
    protected void onResume() {
        super.onResume();
        if (index != -1) {
            if(state == index){
                return;
            } else {
                state = index;
            }
            setTabSelection(state);
            index = -1;
        }
    }

    private void intiView(){
        tab_home = findViewById(R.id.tab_home);
        tab_home_text = (TextView) tab_home.findViewById(R.id.textview);
        tab_home_image = (ImageView) tab_home.findViewById(R.id.imageview);
        tab_location = findViewById(R.id.tab_location);
        tab_location_text = (TextView) tab_location.findViewById(R.id.textview);
        tab_location_image = (ImageView) tab_location.findViewById(R.id.imageview);
        tab_share = findViewById(R.id.tab_shareText);
        tab_share_text = (TextView) tab_share.findViewById(R.id.textview);
        tab_share_image = (ImageView) tab_share.findViewById(R.id.imageview);
        tab_special = findViewById(R.id.tab_special);
        tab_special_text = (TextView) tab_special.findViewById(R.id.textview);
        tab_special_image = (ImageView) tab_special.findViewById(R.id.imageview);
        tab_mine = findViewById(R.id.tab_mine);
        tab_mine_text = (TextView) tab_mine.findViewById(R.id.textview);
        tab_mine_image = (ImageView) tab_mine.findViewById(R.id.imageview);
    }

    /**
     * 设置tab数据
     */
    private void setData(){
        tab_home_text.setText(mTextviewArray[0]);
        tab_location_text.setText(mTextviewArray[1]);
        tab_share_text.setText(mTextviewArray[2]);
        tab_special_text.setText(mTextviewArray[3]);
        tab_mine_text.setText(mTextviewArray[4]);
        clearSelection();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tab_home_text.setTextColor(this.getResources().getColor(R.color.main_text_color));
        tab_location_text.setTextColor(this.getResources().getColor(R.color.main_text_color));
        tab_share_text.setTextColor(this.getResources().getColor(R.color.main_text_color));
        tab_special_text.setTextColor(this.getResources().getColor(R.color.main_text_color));
        tab_mine_text.setTextColor(this.getResources().getColor(R.color.main_text_color));
        tab_home_image.setBackgroundResource(mImageViewArray[0]);
        tab_location_image.setBackgroundResource(mImageViewArray[1]);
        tab_share_image.setBackgroundResource(mImageViewArray[2]);
        tab_special_image.setBackgroundResource(mImageViewArray[3]);
        tab_mine_image.setBackgroundResource(mImageViewArray[4]);
    }

    public void initListener()
    {
        tab_home.setOnClickListener(this);
        tab_location.setOnClickListener(this);
        tab_share.setOnClickListener(this);
        tab_special.setOnClickListener(this);
        tab_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tab_home:
                // 当点击了首页tab时，选中第1个tab
                if(state == 0){
                    return;
                } else {
                    state = 0;
                }
                setTabSelection(0);
                break;
            case R.id.tab_location:
                // 当点击了当地tab时，选中第2个tab
                if(state == 1){
                    return;
                } else {
                    state = 1;
                }
                setTabSelection(1);
                break;
            case R.id.tab_shareText:
                Intent intent = new Intent(this, ShareTextActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_special:
                // 当点击了问吧tab时，选中第3个tab
                /*if(state == 2){
                    return;
                } else {
                    state = 2;
                } */
                state = 3;
                setTabSelection(3);
                break;
            case R.id.tab_mine:
                // 当点击了我的tab时，选中第4个tab
                if(state == 4){
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
     * @param index
     *            每个tab页对应的下标。
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
                // 当点击了首页tab时，改变控件的图片和文字颜色
                tab_home_text.setTextColor(this.getResources().getColor(R.color.class_text_color));
                tab_home_image.setBackgroundResource(mPressImageViewArray[0]);
                if (homeFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.tabcontent, homeFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                // 当点击了当地tab时，改变控件的图片和文字颜色
                tab_location_text.setTextColor(this.getResources().getColor(R.color.class_text_color));
                tab_location_image.setBackgroundResource(mPressImageViewArray[1]);
                if (locationFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    locationFragment = new LocationFragment();
                    transaction.add(R.id.tabcontent, locationFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(locationFragment);
                }
                break;
            case 3:
                // 当点击了问吧tab时，改变控件的图片和文字颜色
                tab_special_text.setTextColor(this.getResources().getColor(R.color.class_text_color));
                tab_special_image.setBackgroundResource(mPressImageViewArray[3]);
                if (askFragment != null) {
                    removeFragment(transaction,askFragment);
                }
                askFragment = null;
                askFragment = new AskFragment();
                transaction.add(R.id.tabcontent, askFragment);
                break;
            case 4:
                // 当点击了我的tab时，改变控件的图片和文字颜色
                tab_mine_text.setTextColor(this.getResources().getColor(R.color.class_text_color));
                tab_mine_image.setBackgroundResource(mPressImageViewArray[4]);
                if (mineFragment != null) {
                    removeFragment(transaction, mineFragment);
                }
                mineFragment = null;
                mineFragment = new MineFragment();
                transaction.add(R.id.tabcontent, mineFragment);
                break;
        }
        transaction.commit();
    }

    /** 删除Fragment **/
    public void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        transaction.remove(fragment);
        //transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (locationFragment != null) {
            transaction.hide(locationFragment);
        }
        if (askFragment != null) {
            transaction.hide(askFragment);
            transaction.remove(askFragment);
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
}
