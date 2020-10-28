package com.shenmai.system.ui.activity;


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

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.shenmai.system.R;
import com.shenmai.system.ui.fragment.CartFragment;
import com.shenmai.system.ui.fragment.MineFragment;
import com.shenmai.system.ui.fragment.ShopFragment;
import com.shenmai.system.ui.fragment.StoreFragment;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private View tab_shop, tab_store, tab_cart, tab_mine;
    private TextView tab_shop_text, tab_store_text, tab_cart_text, tab_mine_text;
    private ImageView tab_shop_image, tab_store_image, tab_cart_image, tab_mine_image;
    //Tab选项卡的文字
    private String mTextviewArray[] = {"店铺", "市场", "购物车", "我的"};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.mipmap.shop_n, R.mipmap.store_n, R.mipmap.cart_n, R.mipmap.mine_n};
    //定义数组来存放按钮图片
    private int mPressImageViewArray[] = {R.mipmap.shop_p, R.mipmap.store_p, R.mipmap.cart_p, R.mipmap.mine_p};
    private FragmentManager fm;
    public int state = 1;//记录当前在哪个页面
    private static boolean isExit = false;
    private StoreFragment storeFragment;
    private MineFragment mineFragment;
    private CartFragment cartFragment;
    private ShopFragment shopFragment;
    public static int cartState = 0;//选择的CartFragment时为2
    /**微信支付参数 */
    public final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

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
        cartState = 0;
        fm = getSupportFragmentManager();
        initView();
        initListener();
        setData();
        BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
    }

    private void initView(){
        tab_shop = findViewById(R.id.tab_shop);
        tab_shop_text = (TextView) tab_shop.findViewById(R.id.textview);
        tab_shop_image = (ImageView) tab_shop.findViewById(R.id.imageview);
        tab_store = findViewById(R.id.tab_store);
        tab_store_text = (TextView) tab_store.findViewById(R.id.textview);
        tab_store_image = (ImageView) tab_store.findViewById(R.id.imageview);
        tab_cart = findViewById(R.id.tab_cart);
        tab_cart_text = (TextView) tab_cart.findViewById(R.id.textview);
        tab_cart_image = (ImageView) tab_cart.findViewById(R.id.imageview);
        tab_mine = findViewById(R.id.tab_mine);
        tab_mine_text = (TextView) tab_mine.findViewById(R.id.textview);
        tab_mine_image = (ImageView) tab_mine.findViewById(R.id.imageview);
        if(configEntity.userRole.equals("1")){
            tab_shop.setVisibility(View.VISIBLE);
            mTextviewArray[1] = "分销";
        } else {
            tab_shop.setVisibility(View.GONE);
            mTextviewArray[1] = "市场";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cartState == 2){
            setTabSelection(2);
        } else {
            setTabSelection(state);
        }
    }

    public void initListener(){
        tab_shop.setOnClickListener(this);
        tab_store.setOnClickListener(this);
        tab_cart.setOnClickListener(this);
        tab_mine.setOnClickListener(this);
    }

    /**
     * 设置tab数据
     */
    private void setData() {
        tab_shop_text.setText(mTextviewArray[0]);
        tab_store_text.setText(mTextviewArray[1]);
        tab_cart_text.setText(mTextviewArray[2]);
        tab_mine_text.setText(mTextviewArray[3]);
        clearSelection();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tab_shop_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_store_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_cart_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_mine_text.setTextColor(this.getResources().getColor(R.color.main_text_color_nomal));
        tab_shop_image.setBackgroundResource(mImageViewArray[0]);
        tab_store_image.setBackgroundResource(mImageViewArray[1]);
        tab_cart_image.setBackgroundResource(mImageViewArray[2]);
        tab_mine_image.setBackgroundResource(mImageViewArray[3]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_shop:
                // 当点击了小店tab时，选中第1个tab
                if (state == 0) {
                    return;
                } else {
                    state = 0;
                }
                setTabSelection(0);
                break;
            case R.id.tab_store:
                // 当点击了市场tab时，选中第2个tab
                if (state == 1) {
                    return;
                } else {
                    state = 1;
                }
                setTabSelection(1);
                break;
            case R.id.tab_cart:
                state = 2;
                setTabSelection(2);
                break;
            case R.id.tab_mine:
                // 当点击了我的tab时，选中第4个tab
                if (state == 3) {
                    return;
                } else {
                    state = 3;
                }
                setTabSelection(3);
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
        cartState = 0;//清空条件
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了首页tab时，改变控件的图片和文字颜色
                tab_shop_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_shop_image.setBackgroundResource(mPressImageViewArray[0]);
                if (shopFragment == null) {
                    // 如果shopFragment为空，则创建一个并添加到界面上
                    shopFragment = new ShopFragment();
                    transaction.add(R.id.tabcontent, shopFragment);
                } else {
                    // 如果shopFragment不为空，则直接将它显示出来
                    transaction.show(shopFragment);
                }
                break;
            case 1:
                // 当点击了市场tab时，改变控件的图片和文字颜色
                tab_store_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_store_image.setBackgroundResource(mPressImageViewArray[1]);
                if (storeFragment == null) {
                    // 如果storeFragment为空，则创建一个并添加到界面上
                    storeFragment = new StoreFragment();
                    transaction.add(R.id.tabcontent, storeFragment);
                } else {
                    // 如果storeFragment不为空，则直接将它显示出来
                    transaction.show(storeFragment);
                }
                break;
            case 2:
                state = 2;
                // 当点击了购物车tab时，改变控件的图片和文字颜色
                tab_cart_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_cart_image.setBackgroundResource(mPressImageViewArray[2]);
                if (cartFragment == null) {
                    // 如果cartFragment为空，则创建一个并添加到界面上
                    cartFragment = new CartFragment();
                    transaction.add(R.id.tabcontent, cartFragment);
                } else {
                    // 如果cartFragment不为空，则直接将它显示出来
                    transaction.show(cartFragment);
                }
                break;
            case 3:
                // 当点击了我的tab时，改变控件的图片和文字颜色
                tab_mine_text.setTextColor(this.getResources().getColor(R.color.main_text_color_select));
                tab_mine_image.setBackgroundResource(mPressImageViewArray[3]);
                if (mineFragment == null) {
                    // 如果mineFragment为空，则创建一个并添加到界面上
                    mineFragment = new MineFragment();
                    transaction.add(R.id.tabcontent, mineFragment);
                } else {
                    // 如果mineFragment不为空，则直接将它显示出来
                    transaction.show(mineFragment);
                }
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
        if (shopFragment != null) {
            transaction.hide(shopFragment);
        }
        if (storeFragment != null) {
            transaction.hide(storeFragment);
        }
        if (cartFragment != null) {
            transaction.hide(cartFragment);
            transaction.remove(cartFragment);
            cartFragment = null;
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    //百度自动更新回调
    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {

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
