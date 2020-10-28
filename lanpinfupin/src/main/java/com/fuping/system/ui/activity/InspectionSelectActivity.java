package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.fuping.system.R;
import com.fuping.system.ui.fragment.BaseFragment;
import com.fuping.system.ui.fragment.CountryInpectionSelectFragment;
import com.fuping.system.ui.fragment.PeopleInpectionSelectFragment;
import com.fuping.system.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class InspectionSelectActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    public static final int TYPE_COUNTRY = 1;
    public static final int TYPE_PEOPLE = 2;
    private TextView people_tv, country_tv;
    private View town_layout, country_layout, people_layout, people_tag, country_tag;
    private List<BaseFragment> fragments = new ArrayList<>();
    private FragmentManager manager;
    private int mCurentIndex;
    private int colorNor, colorSel;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, InspectionSelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_select);
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("督查筛选统计");
        btn_top_sidebar.setVisibility(View.VISIBLE);
        top_right_title.setVisibility(View.VISIBLE);
        top_right_title.setText("督查统计");
        top_right_title.setOnClickListener(this);
        colorNor = getResources().getColor(R.color.text_gray);
        colorSel = getResources().getColor(R.color.text_blue);
        initView();
    }

    private void initView() {
        town_layout = findViewById(R.id.town_layout);
        country_layout = findViewById(R.id.country_layout);
        people_layout = findViewById(R.id.people_layout);
        town_layout.setOnClickListener(this);
        country_layout.setOnClickListener(this);
        people_layout.setOnClickListener(this);
        people_tv = (TextView) findViewById(R.id.people_tv);
        country_tv = (TextView) findViewById(R.id.country_tv);
        people_tag = findViewById(R.id.people_tag);
        country_tag = findViewById(R.id.country_tag);
        fragments.add(CountryInpectionSelectFragment.getInstance(TYPE_COUNTRY));
        fragments.add(PeopleInpectionSelectFragment.getInstance(TYPE_PEOPLE));
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragments.get(0));
        transaction.add(R.id.fragment_container, fragments.get(1)).commit();
        mCurentIndex = 0;
        swichFragment(0);
    }

    @Override
    public void onClick(View v) {
        if (v == town_layout) {
        } else if (v == country_layout) {
            swichFragment(0);
        } else if (v == people_layout) {
            swichFragment(1);
        } else if (v == top_right_title) {
            DuChaTongJiActivity.startActivity(this);
        }
    }

    private void swichFragment(int pos) {
        mCurentIndex = pos;
        if (pos == 0) {
            people_tv.setTextColor(colorNor);
            country_tv.setTextColor(colorSel);
            country_tag.setBackgroundResource(R.drawable.circle_blue);
            people_tag.setBackgroundResource(R.drawable.circle_gray);
            FragmentTransaction t = manager.beginTransaction();
            t.show(fragments.get(0));
            t.hide(fragments.get(1)).commit();
        } else if (pos == 1) {
            people_tv.setTextColor(colorSel);
            country_tv.setTextColor(colorNor);
            country_tag.setBackgroundResource(R.drawable.circle_gray);
            people_tag.setBackgroundResource(R.drawable.circle_blue);
            FragmentTransaction t = manager.beginTransaction();
            t.show(fragments.get(1));
            t.hide(fragments.get(0)).commit();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null || StringUtil.isEmpty(s.toString())) {
        }
    }
}
