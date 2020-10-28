package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.BasePagerAdapter;
import com.yishangshuma.bangelvyou.util.ConfigUtil;

import java.util.ArrayList;

/**
 *导航页面
 */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<View> views;
    private Animation viewInAnimshow;
    private Button btn_enter;
    private int height = 1280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        height = dm.heightPixels/4;//高度
        initView();
    }

    // 初始化视图
    private void initView() {
        // 实例化视图控件
        viewPager = (ViewPager) findViewById(R.id.viewpage_guide);
        views = new ArrayList<View>();
        initGuide();
        pagerAdapter = new BasePagerAdapter(views);
        viewPager.setAdapter(pagerAdapter); // 设置适配器
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化页面
     */
    private void initGuide(){
        initGuideOne();
        initGuideTwo();
        initGuideThree();
//        initGuideFour();
//        initGuideFive();
    }

    /**
     * 初始化第一个页面
     */
    private void initGuideOne(){
        View view = View.inflate(this, R.layout.item_guide, null);
        ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
        img_guide.setBackgroundResource(R.mipmap.bg_guide_one);
        views.add(view);
    }

    /**
     * 初始化第二个页面
     */
    private void initGuideTwo(){
        View view = View.inflate(this, R.layout.item_guide, null);
        ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
        img_guide.setBackgroundResource(R.mipmap.bg_guide_two);
        views.add(view);
    }

    /**
     * 初始化第三个页面
     */
    private void initGuideThree(){
        View view = View.inflate(this, R.layout.item_guide, null);
        ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
        img_guide.setBackgroundResource(R.mipmap.bg_guide_three);
        //进入按钮控件
        btn_enter = (Button) view.findViewById(R.id.btn_enter);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn_enter.getLayoutParams();
        layoutParams.topMargin = height * 3 / 14;
        btn_enter.setLayoutParams(layoutParams);
        btn_enter.setOnClickListener(this);
        viewInAnimshow = new TranslateAnimation(0, 0, (layoutParams.height * 2), 0);
        viewInAnimshow.setDuration(1000);
        btn_enter.setVisibility(View.VISIBLE);
        btn_enter.startAnimation(viewInAnimshow);
        views.add(view);
    }

    /**
     * 初始化第四个页面
     */
    private void initGuideFour(){
        View view = View.inflate(this, R.layout.item_guide, null);
        ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
        img_guide.setBackgroundResource(R.mipmap.bg_guide_four);
        views.add(view);
    }

    /**
     * 初始化第五个页面
     */
    private void initGuideFive(){
        View view = View.inflate(this, R.layout.item_guide, null);
        ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
        img_guide.setBackgroundResource(R.mipmap.bg_welcome);
        //进入按钮控件
        btn_enter = (Button) view.findViewById(R.id.btn_enter);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn_enter.getLayoutParams();
        layoutParams.topMargin = height * 3 / 14;
        btn_enter.setLayoutParams(layoutParams);
        btn_enter.setOnClickListener(this);
        viewInAnimshow = new TranslateAnimation(0, 0, (layoutParams.height * 2), 0);
        viewInAnimshow.setDuration(1000);
        btn_enter.setVisibility(View.VISIBLE);
        btn_enter.startAnimation(viewInAnimshow);
        views.add(view);
    }

    //进主页按钮的点击事件
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter) {
            configEntity.isFirst = false;
            ConfigUtil.saveConfig(this, configEntity);
            GuideActivity.this.startActivity(new Intent(GuideActivity.this, MainActivity.class));
            GuideActivity.this.finish();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    // 监听viewpage
    @Override
    public void onPageSelected(int position) {
        if(position == 2){
            btn_enter.clearAnimation();
            btn_enter.startAnimation(viewInAnimshow);
        }
    }


}
