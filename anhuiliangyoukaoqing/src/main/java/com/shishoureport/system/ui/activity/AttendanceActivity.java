package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.adapter.AttandenceAdapter;
import com.shishoureport.system.ui.fragment.AttandanceFragment;
import com.shishoureport.system.ui.fragment.CountFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/8/29.
 * copyright easybiz.
 */

public class AttendanceActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView attendance_tv, count_tv, title_tv;
    private View attendance_layout, account_layout;
    private ImageView attandence_tab_iv, count_iv;
    private List<Fragment> fragments;
    private int selectedColor;
    private int unSelectedColor;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendance;
    }

    public void initView() {
        selectedColor = Color.parseColor("#8CCAF2");
        unSelectedColor = Color.parseColor("#555555");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<>();
        fragments.add(new AttandanceFragment());
        fragments.add(new CountFragment());
        mViewPager.setAdapter(new AttandenceAdapter(getSupportFragmentManager(), fragments));
        attendance_tv = (TextView) findViewById(R.id.attendance_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        count_tv = (TextView) findViewById(R.id.count_tv);
        attandence_tab_iv = (ImageView) findViewById(R.id.attandence_tab_iv);
        count_iv = (ImageView) findViewById(R.id.count_iv);
        attendance_layout = findViewById(R.id.attendance_layout);
        account_layout = findViewById(R.id.account_layout);
        attendance_layout.setOnClickListener(this);
        account_layout.setOnClickListener(this);
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText("考勤打卡");
        swichTab(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attendance_layout:
                swichTab(0);
                break;
            case R.id.account_layout:
                swichTab(1);
                break;
        }
    }

    private void swichTab(int pos) {
        mViewPager.setCurrentItem(pos);
        if (pos == 0) {
            title_tv.setText("安徽粮油质检中心");
            attendance_tv.setTextColor(selectedColor);
            count_tv.setTextColor(unSelectedColor);
            attandence_tab_iv.setImageResource(R.mipmap.attandence_pic_p);
            count_iv.setImageResource(R.mipmap.attandence_count_pic_n);
        } else if (pos == 1) {
            title_tv.setText("统计");
            attendance_tv.setTextColor(unSelectedColor);
            count_tv.setTextColor(selectedColor);
            attandence_tab_iv.setImageResource(R.mipmap.attandence_pic_n);
            count_iv.setImageResource(R.mipmap.attandence_count_pic_p);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            fragments.get(0).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

