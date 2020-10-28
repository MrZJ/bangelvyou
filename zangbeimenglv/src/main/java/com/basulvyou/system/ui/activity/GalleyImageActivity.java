package com.basulvyou.system.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.ImageViewPagerAdapter;
import com.basulvyou.system.entity.ShareImgEntity;
import com.basulvyou.system.wibget.HackyViewPager;

import java.util.List;

/**
 * 分享预览图片界面
 */
public class GalleyImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    ImageViewPagerAdapter adapter;
    HackyViewPager pager;
    ImageView back_left;
    private int index;
    private TextView imgIndex, imgTotal;
    public List<ShareImgEntity> imgList;//图片集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galley_image);
        imgList = (List<ShareImgEntity>) getIntent().getSerializableExtra("imgList");
        index = getIntent().getIntExtra("index",1);
        initView();
    }

    private void initView(){
        back_left = (ImageView) findViewById(R.id.back_left);
        pager = (HackyViewPager) findViewById(R.id.pager);
        imgIndex = (TextView) findViewById(R.id.img_index);
        imgTotal = (TextView) findViewById(R.id.img_total);
        adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), imgList, GalleyImageActivity.this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);
        imgTotal.setText("/"+imgList.size());
        indiSelected(index);
        pager.setOnPageChangeListener(this);
        back_left.setOnClickListener(this);
    }

    /**
     * 当前图片位置
     * @param position
     */
    private void indiSelected(int position) {
        imgIndex.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indiSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_left:
                finish();
                break;
        }
    }
}
