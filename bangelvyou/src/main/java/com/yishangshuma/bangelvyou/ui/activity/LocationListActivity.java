package com.yishangshuma.bangelvyou.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.LocationFragmentAdapter;
import com.yishangshuma.bangelvyou.entity.ShopBundleEntity;
import com.yishangshuma.bangelvyou.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 当地列表界面
 * Created by KevinLi on 2016/2/2.
 */
public class LocationListActivity extends BaseActivity implements View.OnClickListener{

    private PagerSlidingTabStrip location_tabs;
    private ViewPager location_pager;
    private View btn_location_select, tagOtherView;
    private PopupWindow pop;
    private View tagPopView;
    private TextView hotTag, newTag, disTag;
    private int tagNomalBg = R.mipmap.sx_grey;
    private int tagSelectBg = R.mipmap.sx_orange;
    private String locationType;
    private ShopBundleEntity shopBundleEntity;
    private LocationFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        if(getIntent().getExtras() != null){
            shopBundleEntity = (ShopBundleEntity) getIntent().getExtras().getSerializable("shopBundle");
        }
        initView();
        initListener();
        setAdapter();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        location_pager = (ViewPager) findViewById(R.id.location_pager);
        location_tabs = (PagerSlidingTabStrip) findViewById(R.id.location_tabs);
        btn_location_select = (View) findViewById(R.id.btn_location_select);
        pop = new PopupWindow(this);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        tagPopView = getLayoutInflater().inflate(R.layout.pop_location_select, null);
        hotTag = (TextView) tagPopView.findViewById(R.id.hot_tag);
        newTag = (TextView) tagPopView.findViewById(R.id.new_tag);
        disTag = (TextView) tagPopView.findViewById(R.id.dis_tag);
        tagOtherView = tagPopView.findViewById(R.id.lin_other);
        initTagBackgroud();
        pop.setContentView(tagPopView);
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        btn_location_select.setOnClickListener(this);
        hotTag.setOnClickListener(this);
        newTag.setOnClickListener(this);
        disTag.setOnClickListener(this);
        tagOtherView.setOnClickListener(this);
    }

    private void setAdapter(){
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.location);
        if(location != null){
            for(int i = 0; i < location.length; i++){
                list.add(location[i]);
            }
            adapter = new LocationFragmentAdapter(getSupportFragmentManager(), list);
            if(shopBundleEntity == null){
                shopBundleEntity = new ShopBundleEntity();
            }
            locationType = getIntent().getExtras().getString("type");
            adapter.setShopBundle(shopBundleEntity);
//          adapter.setLocationType(locationType);
            location_pager.setAdapter(adapter);
            location_tabs.setViewPager(location_pager);
            if("view".equals(locationType)){
                location_pager.setCurrentItem(0);
            } else if("sleep".equals(locationType)){
                location_pager.setCurrentItem(1);
            } else if("food".equals(locationType)){
                location_pager.setCurrentItem(2);
            } else if("live".equals(locationType)){
                location_pager.setCurrentItem(3);
            } else if("news".equals(locationType)){
                location_pager.setCurrentItem(4);
            }else if("fwz".equals(locationType)){
                location_pager.setCurrentItem(5);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_location_select:
                isShowTagPop();
                break;
            case R.id.hot_tag:
                setSelectedBg(hotTag, "2");
                break;
            case R.id.new_tag:
                setSelectedBg(newTag,"3");
                break;
            case R.id.dis_tag:
                setSelectedBg(disTag,"5");
                break;
            case R.id.lin_other:
                isShowTagPop();
                break;
        }
    }

    /**显示或隐藏筛选条件弹出框**/
    private void isShowTagPop(){
        if(pop == null){
            return;
        }
        if(pop.isShowing()){
            pop.dismiss();
        } else{
            if(4 == location_pager.getCurrentItem()){
                disTag.setVisibility(View.GONE);
                hotTag.setText("浏览量");
            }else{
                disTag.setVisibility(View.VISIBLE);
                hotTag.setText("热门");
            }
            pop.showAsDropDown(location_tabs);
        }
    }

    /**初始化标签设置未选中情况下背景和字体颜色*/
    private void initTagBackgroud() {
        hotTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        newTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        disTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        hotTag.setBackgroundResource(tagNomalBg);
        newTag.setBackgroundResource(tagNomalBg);
        disTag.setBackgroundResource(tagNomalBg);
    }

    /**设置选中的情况下背景和字体颜色
     * 和要按要求的关键key
     * */
    private void  setSelectedBg(TextView tagView,String key){
        initTagBackgroud();
        tagView.setTextColor(getResources().getColor(R.color.location_tag_select));
        tagView.setBackgroundResource(tagSelectBg);
        if(shopBundleEntity == null){
            shopBundleEntity = new ShopBundleEntity();
        }
        shopBundleEntity.key = key;
        adapter.setShopBundle(shopBundleEntity);
        adapter.setNeedRe();
        adapter.notifyDataSetChanged();
        isShowTagPop();
    }
}
