package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.RescueFragmentAdapter;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 报警救援厕所信息显示界面
 */
public class AlarmRescueActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private PagerSlidingTabStrip rescue_tabs;
    private ViewPager rescue_pager;
    private RescueFragmentAdapter adapter;
    private String index;
    private String rescueTypeId[] = {"21054", "21064", "21068"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_rescue);
        index = getIntent().getStringExtra("index");
        initView();
        initListener();
        setAdapter();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        rescue_pager = (ViewPager) findViewById(R.id.rescue_pager);
        rescue_tabs = (PagerSlidingTabStrip) findViewById(R.id.rescue_tabs);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        rescue_pager.addOnPageChangeListener(this);
    }

    private void setAdapter() {
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.rescue);
        if (location != null) {
            for (int i = 0; i < location.length; i++) {
                list.add(location[i]);
            }
            adapter = new RescueFragmentAdapter(getSupportFragmentManager(), list);
            rescue_pager.setAdapter(adapter);
            rescue_tabs.setViewPager(rescue_pager);
            for (int i = 0; i < rescueTypeId.length; i++) {
                if (index.equals(rescueTypeId[i])) {
                    rescue_pager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position){
            case 0:
                setTitle("报警救援");
                break;
            case 1:
                setTitle("厕所信息");
                break;
            case 2:
                setTitle("加油站");
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
