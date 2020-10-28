package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.adapter.MyPagerAdaper;
import com.fuping.system.ui.fragment.BaseFragment;
import com.fuping.system.ui.fragment.TongJiFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class DuChaTongJiActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private View country_layout, people_layout;
    private ViewPager viewpager;
    private TextView people_tv, country_tv;
    private int colorNor, colorSel;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DuChaTongJiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ducha_tongji);
        colorNor = getResources().getColor(R.color.text_gray);
        colorSel = getResources().getColor(R.color.text_blue);
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("督查统计");
        initView();
    }

    private void initView() {
        country_layout = findViewById(R.id.country_layout);
        people_layout = findViewById(R.id.people_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        people_tv = (TextView) findViewById(R.id.people_tv);
        country_tv = (TextView) findViewById(R.id.country_tv);
        country_layout.setOnClickListener(this);
        people_layout.setOnClickListener(this);
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(TongJiFragment.getInstance(TongJiFragment.TYPE_COUNTRY));
        fragments.add(TongJiFragment.getInstance(TongJiFragment.TYPE_PEOPLE));
        viewpager.setAdapter(new MyPagerAdaper(getSupportFragmentManager(), fragments));
        viewpager.addOnPageChangeListener(this);
        swichTab(0);
    }

    @Override
    public void onClick(View v) {
        if (v == country_layout) {
            viewpager.setCurrentItem(0);
        } else if (v == people_layout) {
            viewpager.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        swichTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void swichTab(int pos) {
        if (pos == 0) {
            country_tv.setTextColor(colorSel);
            people_tv.setTextColor(colorNor);
        } else {
            country_tv.setTextColor(colorNor);
            people_tv.setTextColor(colorSel);
        }
    }
}
