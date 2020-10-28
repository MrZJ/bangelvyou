package com.shenmai.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.shenmai.system.R;
import com.shenmai.system.utlis.ConfigUtil;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;

/**
 *导航页面
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private XBanner viewPager;
    private Button btn_login,btn_register;
    private ArrayList<String> views;
    private Animation viewInAnimshow;
    private int height = 1280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        height = dm.heightPixels/4;//高度
        initView();
    }

    // 初始化视图
    private void initView() {
        // 实例化视图控件
        viewPager = (XBanner) findViewById(R.id.viewpage_guide);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        views = new ArrayList<String>();
        views.add("1");
        views.add("2");
        viewPager.setData(views, null);
        viewPager.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, View view, int position) {
                ImageView bannerImage = (ImageView) view;
                if(position == 0){
                    bannerImage.setBackgroundResource(R.mipmap.bg_guide_one);
                } else if(position == 1){
                    bannerImage.setBackgroundResource(R.mipmap.bg_guide_two);
                }
            }
        });
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    //进主页按钮的点击事件
    @Override
    public void onClick(View v) {
        configEntity.isFirst = false;
        ConfigUtil.saveConfig(this, configEntity);
        switch (v.getId()){
            case R.id.btn_login:
                GuideActivity.this.startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                GuideActivity.this.finish();
                break;
            case R.id.btn_register:
                GuideActivity.this.startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                GuideActivity.this.finish();
                break;
        }
    }

}
