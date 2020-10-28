package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.ui.fragment.BaseFragment;
import com.basulvyou.system.ui.fragment.MyEvaluateFragment;

import static com.basulvyou.system.ui.fragment.MyEvaluateFragment.RECEIVED_EVALUATE;
import static com.basulvyou.system.ui.fragment.MyEvaluateFragment.RELEASED_EVALUATE;

/**
 * Created by jianzhang.
 * on 2017/6/8.
 * copyright easybiz.
 * 我的评价界面
 */

public class MyEvaluateActivity extends BaseActivity implements View.OnClickListener {
    private BaseFragment[] fragments;
    private TextView my_evaluate_tv, my_release_tv;
    private View my_evaluate_line, my_release_line;
    private int corPressed;
    private int corNor;
    private int lineNorCol;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluate);
        initTopView();
        setTitle("我的评价");
        corPressed = getResources().getColor(R.color.top_tab_color_pressed);
        corNor = getResources().getColor(R.color.top_tab_color_nor);
        lineNorCol = getResources().getColor(R.color.list_div_color);
        initView();
    }

    private void initView() {
        my_evaluate_tv = (TextView) findViewById(R.id.my_evaluate_tv);
        my_release_tv = (TextView) findViewById(R.id.my_release_tv);
        my_evaluate_line = findViewById(R.id.my_evaluate_line);
        my_release_line = findViewById(R.id.my_release_line);
        btn_top_goback.setOnClickListener(this);
        my_evaluate_tv.setOnClickListener(this);
        my_release_tv.setOnClickListener(this);
        fragments = new BaseFragment[2];
        fragments[0] = new MyEvaluateFragment().getInstance(RELEASED_EVALUATE);
        fragments[1] = new MyEvaluateFragment().getInstance(RECEIVED_EVALUATE);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragments[1]);
        transaction.add(R.id.content, fragments[0]);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_evaluate_tv:
                swichTab(0);
                break;
            case R.id.my_release_tv:
                swichTab(1);
                break;
            case R.id.btn_top_goback:
                finish();
                break;
        }
    }

    private void swichTab(int pos) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragments[0]);
        transaction.hide(fragments[1]);
        if (pos == 0) {
            my_evaluate_tv.setTextColor(corPressed);
            my_evaluate_line.setBackgroundColor(corPressed);
            my_release_tv.setTextColor(corNor);
            my_release_line.setBackgroundColor(lineNorCol);
        } else {
            my_evaluate_tv.setTextColor(corNor);
            my_evaluate_line.setBackgroundColor(lineNorCol);
            my_release_tv.setTextColor(corPressed);
            my_release_line.setBackgroundColor(corPressed);
        }
        transaction.show(fragments[pos]);
        transaction.commit();
    }
}
