package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.fragment.BaseFragment;
import com.shishoureport.system.ui.fragment.WebFragment;

import static com.shishoureport.system.ui.fragment.WebFragment.WEB_URL_KEY;

/**
 * Created by jianzhang.
 * on 2017/6/2.
 * copyright easybiz.
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {
    private BaseFragment mFragment;

    public static void startActivity(Context c, String url) {
        if (c == null) return;
        Intent i = new Intent(c, WebActivity.class);
        i.putExtra(WEB_URL_KEY, url);
        c.startActivity(i);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(WEB_URL_KEY);
        Bundle b = new Bundle();
        mFragment = new WebFragment();
        b.putString(WEB_URL_KEY, url);
        mFragment.setArguments(b);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.search_webview, mFragment).commit();
    }

    @Override
    public void onClick(View view) {
    }
}
