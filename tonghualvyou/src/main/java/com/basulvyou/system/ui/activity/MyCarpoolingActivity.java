package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.MyCarpoolFragmentAdapter;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 我的拼车/我的物流界面
 */
public class MyCarpoolingActivity extends BaseActivity{

    private PagerSlidingTabStrip carpool_tabs;
    private ViewPager carpool_pager;
    private String orderType;//用来区分显示类别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_carpooling);
        orderType = getIntent().getStringExtra("type");
        initView();
        initListener();
        setAdapter();
    }

    private void initView() {
        initTopView();
        if(orderType.equals("104")){
            setTitle("我的物流");
        }else{
            setTitle("我的拼车");
        }
        setLeftBackButton();
        carpool_pager = (ViewPager) findViewById(R.id.carpool_pager);
        carpool_tabs = (PagerSlidingTabStrip) findViewById(R.id.carpool_tabs);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setAdapter() {
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.my_carpool);
        if (location != null) {
            for (int i = 0; i < location.length; i++) {
                list.add(location[i]);
            }
            MyCarpoolFragmentAdapter adapter = new MyCarpoolFragmentAdapter(getSupportFragmentManager(), list,orderType);
            carpool_pager.setAdapter(adapter);
            carpool_tabs.setViewPager(carpool_pager);
        }
    }

}
