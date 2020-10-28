package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.OrderTypeFragmentAdapter;
import com.yishangshuma.bangelvyou.entity.OrderTypeEntity;
import com.yishangshuma.bangelvyou.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 我的订单界面
 */
public class OrderListActivity extends BaseActivity {

    private PagerSlidingTabStrip orderTypeTabs;
    private ViewPager orderListPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initView();
        initListener();
    }

    private void initView(){
        initTopView();
        setTitle("我的订单");
        setLeftBackButton();
        orderTypeTabs = (PagerSlidingTabStrip) findViewById(R.id.tab_order_list_ordertype);
        orderListPager = (ViewPager) findViewById(R.id.viewpage_order_list_showlist);
        ArrayList<OrderTypeEntity> list = new ArrayList<>();
        OrderTypeEntity ywcEntity = new OrderTypeEntity();
        ywcEntity.orderTypeName ="全部";
        ywcEntity.orderTypeKey ="0";
        list.add(ywcEntity);
        OrderTypeEntity dfkEntity = new OrderTypeEntity();
        dfkEntity.orderTypeName ="待付款";
        dfkEntity.orderTypeKey ="1";
        list.add(dfkEntity);
        OrderTypeEntity dfhEntity = new OrderTypeEntity();
        dfhEntity.orderTypeName ="待发货";
        dfhEntity.orderTypeKey ="2";
        list.add(dfhEntity);
        OrderTypeEntity dshEntity = new OrderTypeEntity();
        dshEntity.orderTypeName ="待收货";
        dshEntity.orderTypeKey ="3";
        list.add(dshEntity);
        OrderTypeEntity dpjEntity = new OrderTypeEntity();
        dpjEntity.orderTypeName ="待评价";
        dpjEntity.orderTypeKey ="4";
        list.add(dpjEntity);
        orderListPager.setAdapter(new OrderTypeFragmentAdapter(getSupportFragmentManager(), list));
        orderTypeTabs.setViewPager(orderListPager);
        orderListPager.setCurrentItem(0);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }
}
