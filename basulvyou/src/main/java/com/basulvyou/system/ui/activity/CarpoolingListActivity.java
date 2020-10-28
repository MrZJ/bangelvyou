package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.CarpoolingFragmentAdapter;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 拼车信息列表页面
 */
public class CarpoolingListActivity extends BaseActivity implements View.OnClickListener{

    private PagerSlidingTabStrip carpooling_tabs;
    private ViewPager carpooling_pager;
    private View carpooling_select,tagOtherView,sendCarpool;
    private PopupWindow pop;
    private View tagPopView;
    private TextView careTag, timeTag, disTag;
    private int tagNomalBg = R.mipmap.sx_grey;
    private int tagSelectBg = R.mipmap.sx_orange;
    private CarpoolingFragmentAdapter adapter;
    private ShopBundleEntity shopBundleEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooling_list);
        initView();
        initListener();
        setAdapter();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTopRightImg(R.mipmap.carpool_ask, TopClickUtil.TOP_RULE);
        setTitle("拼车");
        carpooling_tabs = (PagerSlidingTabStrip) findViewById(R.id.carpooling_tabs);
        carpooling_pager = (ViewPager) findViewById(R.id.carpooling_pager);
        carpooling_select = findViewById(R.id.btn_carpooling_select);
        sendCarpool = findViewById(R.id.img_carpooling_list_sendinfo);

        pop = new PopupWindow(this);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        tagPopView = getLayoutInflater().inflate(R.layout.pop_location_select, null);
        careTag = (TextView) tagPopView.findViewById(R.id.hot_tag);
        careTag.setText("类别");
        timeTag = (TextView) tagPopView.findViewById(R.id.new_tag);
        timeTag.setText("时间");
        disTag = (TextView) tagPopView.findViewById(R.id.dis_tag);
        tagOtherView = tagPopView.findViewById(R.id.lin_other);
        initTagBackgroud();
        pop.setContentView(tagPopView);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        carpooling_select.setOnClickListener(this);
        careTag.setOnClickListener(this);
        timeTag.setOnClickListener(this);
        disTag.setOnClickListener(this);
        tagOtherView.setOnClickListener(this);
        sendCarpool.setOnClickListener(this);
    }

    private void setAdapter(){
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.carpooling);
        if(location != null){
            for(int i = 0; i < location.length; i++){
                list.add(location[i]);
            }
            adapter = new CarpoolingFragmentAdapter(getSupportFragmentManager(), list);
           /* if(shopBundleEntity == null){
                shopBundleEntity = new ShopBundleEntity();
            }*/
//            adapter.setShopBundle(shopBundleEntity);
            carpooling_pager.setAdapter(adapter);
            carpooling_tabs.setViewPager(carpooling_pager);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_carpooling_select:
                isShowTagPop();
                break;
            case R.id.lin_other:
                isShowTagPop();
                break;
            case R.id.hot_tag:
                setSelectedBg(careTag, "2");
                break;
            case R.id.new_tag:
                setSelectedBg(timeTag,"3");
                break;
            case R.id.dis_tag:
                setSelectedBg(disTag, "5");
                break;
            case R.id.img_carpooling_list_sendinfo:
                Intent sendIntent = new Intent(this,SendCarpoolingActivity.class);
                startActivity(sendIntent);
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
            pop.showAsDropDown(carpooling_tabs);
        }
    }

    /**初始化标签设置未选中情况下背景和字体颜色*/
    private void initTagBackgroud() {
        careTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        timeTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        disTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        careTag.setBackgroundResource(tagNomalBg);
        timeTag.setBackgroundResource(tagNomalBg);
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
