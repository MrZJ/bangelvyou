package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.ui.fragment.OrderTypeFragment;

/**
 * 我的订单界面
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener{

    private TextView tab_main_text_1, tab_main_text_2, tab_main_text_3, tab_main_text_4, tab_main_text_5;
    private OrderTypeFragment myOrderFragment1, myOrderFragment2, myOrderFragment3, myOrderFragment4
            , myOrderFragment5;
    private android.support.v4.app.FragmentManager fm;
    private int state = 0;//记录当前在哪个页面  全部:0 待付款:1 待发货:2 待收货:3 待评价:4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initView();
        initListener();
        clearSelection();
        fm = getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTabSelection(state);
    }

    private void initView(){
        initTopView();
        setTitle("我的订单");
        setLeftBackButton();
        tab_main_text_1 = (TextView) findViewById(R.id.tv_my_order_1);
        tab_main_text_2 = (TextView) findViewById(R.id.tv_my_order_2);
        tab_main_text_3 = (TextView) findViewById(R.id.tv_my_order_3);
        tab_main_text_4 = (TextView) findViewById(R.id.tv_my_order_4);
        tab_main_text_5 = (TextView) findViewById(R.id.tv_my_order_5);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        tab_main_text_1.setOnClickListener(this);
        tab_main_text_2.setOnClickListener(this);
        tab_main_text_3.setOnClickListener(this);
        tab_main_text_4.setOnClickListener(this);
        tab_main_text_5.setOnClickListener(this);
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tab_main_text_1.setTextColor(getResources().getColor(R.color.order_nomal));
        tab_main_text_2.setTextColor(getResources().getColor(R.color.order_nomal));
        tab_main_text_3.setTextColor(getResources().getColor(R.color.order_nomal));
        tab_main_text_4.setTextColor(getResources().getColor(R.color.order_nomal));
        tab_main_text_5.setTextColor(getResources().getColor(R.color.order_nomal));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_order_1:
                // 当点击了消息tab时，选中第1个tab
                if(state == 0){
                    return;
                } else {
                    state = 0;
                }
                setTabSelection(0);
                break;
            case R.id.tv_my_order_2:
                // 当点击了联系人tab时，选中第2个tab
                if(state == 1){
                    return;
                } else {
                    state = 1;
                }
                setTabSelection(1);
                break;
            case R.id.tv_my_order_3:
                // 当点击了联系人tab时，选中第2个tab
                if(state == 2){
                    return;
                } else {
                    state = 2;
                }
                setTabSelection(2);
                break;
            case R.id.tv_my_order_4:
                // 当点击了联系人tab时，选中第2个tab
                if(state == 3){
                    return;
                } else {
                    state = 3;
                }
                setTabSelection(3);
                break;
            case R.id.tv_my_order_5:
                // 当点击了联系人tab时，选中第2个tab
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
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        Bundle bundle = new Bundle();
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                tab_main_text_1.setTextColor(getResources().getColor(R.color.order_select));
                if (myOrderFragment1 != null) {
                    removeFragment(transaction, myOrderFragment1);
                }
                myOrderFragment1 = null;
                myOrderFragment1 = new OrderTypeFragment();
                bundle.putString("key", "0");
                myOrderFragment1.setArguments(bundle);
                transaction.add(R.id.order_list_tabcontent, myOrderFragment1);
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                tab_main_text_2.setTextColor(getResources().getColor(R.color.order_select));
                if (myOrderFragment2 != null) {
                    removeFragment(transaction, myOrderFragment2);
                }
                myOrderFragment2 = null;
                myOrderFragment2 = new OrderTypeFragment();
                bundle.putString("key", "1");
                myOrderFragment2.setArguments(bundle);
                transaction.add(R.id.order_list_tabcontent, myOrderFragment2);
                break;
            case 2:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                tab_main_text_3.setTextColor(getResources().getColor(R.color.order_select));
                if (myOrderFragment3 != null) {
                    removeFragment(transaction, myOrderFragment3);
                }
                myOrderFragment3 = null;
                myOrderFragment3 = new OrderTypeFragment();
                bundle.putString("key", "2");
                myOrderFragment3.setArguments(bundle);
                transaction.add(R.id.order_list_tabcontent, myOrderFragment3);
                break;
            case 3:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                tab_main_text_4.setTextColor(getResources().getColor(R.color.order_select));
                if (myOrderFragment4 != null) {
                    removeFragment(transaction, myOrderFragment4);
                }
                myOrderFragment4 = null;
                myOrderFragment4 = new OrderTypeFragment();
                bundle.putString("key", "3");
                myOrderFragment4.setArguments(bundle);
                transaction.add(R.id.order_list_tabcontent, myOrderFragment4);
                break;
            case 4:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                tab_main_text_5.setTextColor(getResources().getColor(R.color.order_select));
                if (myOrderFragment5 != null) {
                    removeFragment(transaction, myOrderFragment5);
                }
                myOrderFragment5 = null;
                myOrderFragment5 = new OrderTypeFragment();
                bundle.putString("key", "4");
                myOrderFragment5.setArguments(bundle);
                transaction.add(R.id.order_list_tabcontent, myOrderFragment5);
                break;
        }
        transaction.commit();
    }

    /** 删除Fragment **/
    public void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        transaction.remove(fragment);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (myOrderFragment1 != null) {
            transaction.hide(myOrderFragment1);
        }
        if (myOrderFragment2 != null) {
            transaction.hide(myOrderFragment2);
        }
        if (myOrderFragment3 != null) {
            transaction.hide(myOrderFragment3);
        }
        if (myOrderFragment4 != null) {
            transaction.hide(myOrderFragment4);
        }
        if (myOrderFragment5 != null) {
            transaction.hide(myOrderFragment5);
        }
    }

}
