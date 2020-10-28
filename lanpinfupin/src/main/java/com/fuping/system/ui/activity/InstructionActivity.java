package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fuping.system.R;
import com.fuping.system.ui.fragment.InstructionFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class InstructionActivity extends BaseActivity implements View.OnClickListener {
    private View replyed_layout, not_reply_layout;
    private List<InstructionFragment> fragments;
    private FragmentManager manager;
    private String id;

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, InstructionActivity.class);
        intent.putExtra("key", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        initTopView();
        initListener();
        setLeftBackButton();
        initSideBarListener();
        setTitle("历史批示");
        initView();
    }

    private void initView() {
        replyed_layout = findViewById(R.id.replyed_layout);
        not_reply_layout = findViewById(R.id.not_reply_layout);
        replyed_layout.setOnClickListener(this);
        not_reply_layout.setOnClickListener(this);
        fragments = new ArrayList<>();
        String id = getIntent().getStringExtra("key");
        fragments.add(InstructionFragment.getInstance(InstructionFragment.NOT_REPLAY, id));
        fragments.add(InstructionFragment.getInstance(InstructionFragment.IS_REPLAY, id));
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, fragments.get(0)).commit();
        manager.beginTransaction().add(R.id.fragment_container, fragments.get(1)).commit();
        showFragment(0);
    }

    @Override
    public void onClick(View v) {
        if (replyed_layout == v) {
            showFragment(1);
        } else if (not_reply_layout == v) {
            showFragment(0);
        }
    }

    private void showFragment(int pos) {
        if (pos == 0) {
            manager.beginTransaction().hide(fragments.get(1)).commit();
            manager.beginTransaction().show(fragments.get(0)).commit();
        } else {
            manager.beginTransaction().hide(fragments.get(0)).commit();
            manager.beginTransaction().show(fragments.get(1)).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                fragments.get(0).onRefreshData();
                fragments.get(1).onRefreshData();
            } catch (Exception e) {
            }
        }
    }
}
