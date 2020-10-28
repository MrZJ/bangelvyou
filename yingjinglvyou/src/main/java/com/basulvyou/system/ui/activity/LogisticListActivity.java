package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LogisticFragmentAdapter;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 物流列表信息页面
 */
public class LogisticListActivity extends BaseActivity implements View.OnClickListener{

    private PagerSlidingTabStrip logistic_tabs;
    private ViewPager logistic_pager;
    private View logistic_select,tagOtherView,sendCarpool;
    private PopupWindow pop;
    private View tagPopView;
    private TextView careTag, timeTag, disTag;
    private int tagNomalBg = R.mipmap.sx_grey;
    private int tagSelectBg = R.mipmap.sx_orange;
    private LogisticFragmentAdapter adapter;
    private ShopBundleEntity shopBundleEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic_list);
        initView();
        initListener();
        setAdapter();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("物流");
        setTopRightImg(R.mipmap.carpool_ask, TopClickUtil.TOP_L_RULE);
        logistic_tabs = (PagerSlidingTabStrip) findViewById(R.id.logistic_tabs);
        logistic_pager = (ViewPager) findViewById(R.id.logistic_pager);
        logistic_select = findViewById(R.id.btn_logistic_select);
        sendCarpool = findViewById(R.id.img_logistic_list_sendinfo);

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
        logistic_select.setOnClickListener(this);
        careTag.setOnClickListener(this);
        timeTag.setOnClickListener(this);
        disTag.setOnClickListener(this);
        tagOtherView.setOnClickListener(this);
        sendCarpool.setOnClickListener(this);
    }

    private void setAdapter(){
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.logistic);
        if(location != null){
            for(int i = 0; i < location.length; i++){
                list.add(location[i]);
            }
            adapter = new LogisticFragmentAdapter(getSupportFragmentManager(), list);
           /* if(shopBundleEntity == null){
                shopBundleEntity = new ShopBundleEntity();
            }*/
//            adapter.setShopBundle(shopBundleEntity);
            logistic_pager.setAdapter(adapter);
            logistic_tabs.setViewPager(logistic_pager);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logistic_select:
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
                setSelectedBg(disTag,"5");
                break;
            case R.id.img_logistic_list_sendinfo:
                Intent sendIntent = new Intent(this,SendLogisticInfoActivity.class);
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
            pop.showAsDropDown(logistic_tabs);
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
