package com.basulvyou.system.ui.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationFragmentAdapter;
import com.basulvyou.system.adapter.MyPopAdapter;
import com.basulvyou.system.api.ShopListApi;
import com.basulvyou.system.entity.ClassList;
import com.basulvyou.system.entity.LocationClassEntity;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.ui.activity.LocationClassActivity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地界面
 * Created by Administrator on 2016/1/25.
 */
public class LocationFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {

    private PagerSlidingTabStrip location_tabs;
    private ViewPager location_pager;
    private View mView, tagPop, tagOtherView, emptyView;
    private ImageView loadView;
    private ShopBundleEntity shopBundleEntity;
    private LocationFragmentAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();
    private PopupWindow pop;
    private TextView defaultTag, priceUpTag, priceDownTag, tip_text;
    private ListView popClass;
    private List<LocationClassEntity> classList;
    private String key = "";
    private String order = "";
    private Animation mRotateAnimation;
    public Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location, container, false);
        initView();
        initListener();
        setAdapter();
        return mView;
    }

    private void initView() {
        initTopView(mView);
        hideBackBtn();
        setTitle("当地");
        location_pager = (ViewPager) mView.findViewById(R.id.location_pager);
        location_tabs = (PagerSlidingTabStrip) mView.findViewById(R.id.location_tabs);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void setAdapter() {
        String[] location = getResources().getStringArray(R.array.location);
        if (location != null) {
            for (int i = 0; i < location.length; i++) {
                list.add(location[i]);
            }
            adapter = new LocationFragmentAdapter(getFragmentManager(), list);
            if (shopBundleEntity == null) {
                shopBundleEntity = new ShopBundleEntity();
            }
//          locationType = getIntent().getExtras().getString("type");
            adapter.setShopBundle(shopBundleEntity);
//          adapter.setLocationType(locationType);
            location_pager.setAdapter(adapter);
            location_tabs.setViewPager(location_pager);
            location_pager.setCurrentItem(0);
            location_tabs.setOnPageChangeListener(this);
        }
    }

    private void initListener() {
        initTopListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_right_text:
                if(classList == null && (int)v.getTag() == TopClickUtil.TOP_CATE){
                    loadDataClass();
                }
                isShowTagPop();
                break;
            case R.id.lin_other:
                isShowTagPop();
                break;
            case R.id.default_tag:
                key = "";
                order = "";
                setSelectedBg();
                break;
            case R.id.price_up_tag:
                key = "3";
                order = "1";
                setSelectedBg();
                break;
            case R.id.price_down_tag:
                key = "3";
                order = "2";
                setSelectedBg();
                break;
        }
    }

    /**
     * 显示或隐藏筛选条件弹出框
     **/
    private void isShowTagPop() {
        if (pop == null) {
            return;
        }
        if (pop.isShowing()) {
            pop.dismiss();
        } else {
            pop.showAsDropDown(location_tabs);
        }
    }

    /**
     * 设置选中的情况下背景和字体颜色
     * 和要按要求的关键key
     */
    private void setSelectedBg() {
        if (shopBundleEntity == null) {
            shopBundleEntity = new ShopBundleEntity();
        }
        shopBundleEntity.key = key;
        shopBundleEntity.order = order;
        pop.dismiss();
        adapter.setShopBundle(shopBundleEntity);
        adapter.setNeedRe();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*switch (position) {
            case 0:
                setTopRightTitle("排序", TopClickUtil.TOP_ORDERBY);
                creatPop();
                break;
            case 3:
                setTopRightTitleAndImg("分类", TopClickUtil.TOP_CATE, R.mipmap.top_ss, TopClickUtil.TOP_SEA);
                creatPop();
                break;
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
                hideTopRightTitle();
                break;
        }*/
    }

    private void creatPop() {
        if (pop == null) {
            pop = new PopupWindow(getActivity());
            pop.setFocusable(true);
            pop.setOutsideTouchable(true);
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        }
        if ((int)top_right_title.getTag() == TopClickUtil.TOP_ORDERBY) {
            tagPop = getActivity().getLayoutInflater().inflate(R.layout.pop_grogshop_select, null);
            defaultTag = (TextView) tagPop.findViewById(R.id.default_tag);
            priceUpTag = (TextView) tagPop.findViewById(R.id.price_up_tag);
            priceDownTag = (TextView) tagPop.findViewById(R.id.price_down_tag);
            tagOtherView = tagPop.findViewById(R.id.lin_other);
            if (order.equals("1")) {
                priceUpTag.setTextColor(getResources().getColor(R.color.location_tag_select_1));
            } else if (order.equals("2")) {
                priceDownTag.setTextColor(getResources().getColor(R.color.location_tag_select_1));
            } else {
                defaultTag.setTextColor(getResources().getColor(R.color.location_tag_select_1));
            }
            pop.setContentView(tagPop);
            defaultTag.setOnClickListener(this);
            priceUpTag.setOnClickListener(this);
            priceDownTag.setOnClickListener(this);
            tagOtherView.setOnClickListener(this);
            top_right_title.setOnClickListener(this);
        } else if ((int)top_right_title.getTag() == TopClickUtil.TOP_CATE) {
            tagPop = getActivity().getLayoutInflater().inflate(R.layout.pop_techan_select, null);
            popClass = (ListView) tagPop.findViewById(R.id.lv_pop_class);
            tagOtherView = tagPop.findViewById(R.id.lin_other);
            /**数据加载布局*/
            emptyView = tagPop.findViewById(R.id.empty_view);
            tipTextView = (TextView) tagPop.findViewById(R.id.tip_text);
            loadView = (ImageView) tagPop.findViewById(R.id.loading_img);

            if (null != emptyView) {
                View view = tagPop.findViewById(R.id.loading_layout);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                if (null != params) {
                    DisplayMetrics displayMetrics = getResources()
                            .getDisplayMetrics();

                    int height = displayMetrics.heightPixels;
                    params.topMargin = (int) (height * 0.25);
                    view.setLayoutParams(params);
                }
                mRotateAnimation = AsynImageUtil.mRotateAnimation;

                mRotateAnimation
                        .setInterpolator(AsynImageUtil.ANIMATION_INTERPOLATOR);
                mRotateAnimation
                        .setDuration(AsynImageUtil.ROTATION_ANIMATION_DURATION_SHORT);
                mRotateAnimation.setRepeatCount(Animation.INFINITE);
                mRotateAnimation.setRepeatMode(Animation.RESTART);
            }
            loadDataClass();
            pop.setContentView(tagPop);
            popClass.setOnItemClickListener(this);
            top_right_title.setOnClickListener(this);
            tagOtherView.setOnClickListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.lv_pop_class) {
            Intent intent = new Intent(getActivity(), LocationClassActivity.class);
            intent.putExtra("entity", classList.get(position));
            startActivity(intent);
            isShowTagPop();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 显示加载布局
     */
    public void showLoading() {
        handler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText("正在加载中...");
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
                }
            }
        });

    }

    /**
     * dimiss加载布局
     */
    public void hiddenLoading() {
        handler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

            }
        });
    }

    private void loadDataClass() {
        showLoading();
        httpGetRequest(ShopListApi.getClassListUrl(), ShopListApi.API_GET_CLASS_LIST);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShopListApi.API_GET_CLASS_LIST:
                hiddenLoading();
                ClassList classLists = JSON.parseObject(json, ClassList.class);
                if (classLists != null) {
                    classList = classLists.list;
                    MyPopAdapter adapter = new MyPopAdapter(getActivity(), classList);
                    popClass.setAdapter(adapter);
                }
                break;
            default:
                break;
        }
    }
}
