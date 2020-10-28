package com.basulvyou.system.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.HomeTodayHotAdapter;
import com.basulvyou.system.adapter.HomeTopAdapter;
import com.basulvyou.system.adapter.TopOneAdapter;
import com.basulvyou.system.api.HomeApi;
import com.basulvyou.system.api.ShareTextApi;
import com.basulvyou.system.entity.BDZXEntity;
import com.basulvyou.system.entity.HomeAdv;
import com.basulvyou.system.entity.HomeEntity;
import com.basulvyou.system.entity.LYJDEntity;
import com.basulvyou.system.entity.ShareEntity;
import com.basulvyou.system.entity.ShareList;
import com.basulvyou.system.entity.TopInfo;
import com.basulvyou.system.entity.WeatherList;
import com.basulvyou.system.ui.activity.AlarmRescueActivity;
import com.basulvyou.system.ui.activity.LocationActivity;
import com.basulvyou.system.ui.activity.LocationDetailActivity;
import com.basulvyou.system.ui.activity.LocationViewGridActivity;
import com.basulvyou.system.ui.activity.LoginActivity;
import com.basulvyou.system.ui.activity.LogisticListActivity;
import com.basulvyou.system.ui.activity.MyWebActivity;
import com.basulvyou.system.ui.activity.NearbyActivity;
import com.basulvyou.system.ui.activity.NewsDetailActivity;
import com.basulvyou.system.ui.activity.PointMallActivity;
import com.basulvyou.system.ui.activity.SearchHistoryActivity;
import com.basulvyou.system.ui.activity.SendCarpoolingActivity;
import com.basulvyou.system.ui.activity.ShareDetailActivity;
import com.basulvyou.system.ui.activity.TranslateActivity;
import com.basulvyou.system.ui.activity.VideoActivity;
import com.basulvyou.system.ui.activity.WeatherWebActivity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.wibget.AutoScrollViewPager;
import com.basulvyou.system.wibget.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 * Created by Administrator on 2016/1/25.
 */
public class HomeFragment extends AbsLoadMoreFragment<ListView, ShareEntity> implements
        View.OnClickListener, ViewPager.OnPageChangeListener
        , TopOneAdapter.OnModelClickListener, AdapterView.OnItemClickListener, HomeTodayHotAdapter.OnZanClickListenter, HomeTopAdapter.ListItemClickInterface {

    private View mView, topView, top, rl_home_pager;
    private PullToRefreshListView mPullToRefreshListView;
    private ImageView home_adv;
    private int home_advHeight, StatusBarHeight, distance;
    private TextView search;
    private View lin_home_btn, lin_home_btn1, home_item_1, home_item_2, home_item_3, home_item_4, home_item_5, home_item_6, home_item_7, home_item_8, home_item_9, home_item_10, home_item_11, home_item_12;
    private TextView[] homeButtonTextView = new TextView[12];
    private ImageView[] homeButtonImageView = new ImageView[12];
    private int[] homeBtnImageViewId = {R.mipmap.home_btn_img1, R.mipmap.home_btn_img2, R.mipmap.home_btn_img3, R.mipmap.home_btn_img4, R.mipmap.home_btn_img5, R.mipmap.home_btn_img6, R.mipmap.home_btn_img7, R.mipmap.home_btn_img8, R.mipmap.home_btn_img7, R.mipmap.home_btn_img8, R.mipmap.home_btn_img8, R.mipmap.home_btn_img8, R.mipmap.home_btn_img8, R.mipmap.home_btn_img8, R.mipmap.home_btn_img8};
    /*private String homeButtonText[] = {"美食","住宿","景点", "购物","藏家乐","拼车","物流","援助","文化","团购"};*/
    private String homeButtonText[] = {"景点", "住宿", "美食", "特产", "文化", "农家乐", "拼车", "本地", "救援", "物流", "加油", "公厕"};
    private AutoScrollViewPager homePager;
    private LinearLayout pagerIndicator;
    private ImageView[] indicators = null;
    private HomeEntity homeEntity;
    private List<HomeAdv> home1 = new ArrayList<HomeAdv>();
    private List<HomeAdv> home2 = new ArrayList<HomeAdv>();
    private TopOneAdapter mAdAdapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView weatherImg;
    private TextView weatherCity, weatherWendu;
    private View relweather;
    private View clickView;
    private int position;
    private MyListView lv;
    private HomeTopAdapter homeTopAdapter;
    private View title_layout_fx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(inflater);
        setAdapter();
        initListener();
        getHome();
        loadData();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != getActivity()) {
            configEntity = ConfigUtil.loadConfig(getActivity());
        }
    }

    private void initView(LayoutInflater inflater) {
        initTopView(mView);
        showHomeSearch();
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.home_list);
//        top = (View) mView.findViewById(R.id.top);
//        top.getBackground().setAlpha(0);
//        search = (TextView) top.findViewById(R.id.home_top_title_search);
        topView = inflater.inflate(R.layout.home_list_header, null);
        title_layout_fx = topView.findViewById(R.id.title_layout_fx);
        relweather = topView.findViewById(R.id.rel_home_weather);
        weatherImg = (ImageView) topView.findViewById(R.id.img_home_weather_img);
        weatherCity = (TextView) topView.findViewById(R.id.tv_home_location);
        weatherWendu = (TextView) topView.findViewById(R.id.tv_home_wendu);
        homePager = (AutoScrollViewPager) topView.findViewById(R.id.home_pager);
        rl_home_pager = (View) topView.findViewById(R.id.rl_home_pager);
        pagerIndicator = (LinearLayout) topView.findViewById(R.id.home_pager_indicator);
        home_adv = (ImageView) topView.findViewById(R.id.home_adv);
        lv = (MyListView) topView.findViewById(R.id.home_top_lv);
        mPullToRefreshListView.getRefreshableView().addHeaderView(topView);

        lin_home_btn = mView.findViewById(R.id.lin_home_btn);
        lin_home_btn1 = mView.findViewById(R.id.lin_home_btn_1);

        home_item_1 = (View) mView.findViewById(R.id.home_item_1);
        homeButtonImageView[0] = (ImageView) home_item_1.findViewById(R.id.img_home);
        homeButtonTextView[0] = (TextView) home_item_1.findViewById(R.id.tv_home);

        home_item_2 = (View) mView.findViewById(R.id.home_item_2);
        homeButtonImageView[1] = (ImageView) home_item_2.findViewById(R.id.img_home);
        homeButtonTextView[1] = (TextView) home_item_2.findViewById(R.id.tv_home);

        home_item_3 = (View) mView.findViewById(R.id.home_item_3);
        homeButtonImageView[2] = (ImageView) home_item_3.findViewById(R.id.img_home);
        homeButtonTextView[2] = (TextView) home_item_3.findViewById(R.id.tv_home);

        home_item_4 = (View) mView.findViewById(R.id.home_item_4);
        homeButtonImageView[3] = (ImageView) home_item_4.findViewById(R.id.img_home);
        homeButtonTextView[3] = (TextView) home_item_4.findViewById(R.id.tv_home);

        home_item_5 = (View) mView.findViewById(R.id.home_item_5);
        homeButtonImageView[4] = (ImageView) home_item_5.findViewById(R.id.img_home);
        homeButtonTextView[4] = (TextView) home_item_5.findViewById(R.id.tv_home);

        home_item_6 = (View) mView.findViewById(R.id.home_item_6);
        homeButtonImageView[5] = (ImageView) home_item_6.findViewById(R.id.img_home);
        homeButtonTextView[5] = (TextView) home_item_6.findViewById(R.id.tv_home);

        home_item_7 = (View) mView.findViewById(R.id.home_item_7);
        homeButtonImageView[6] = (ImageView) home_item_7.findViewById(R.id.img_home_1);
        homeButtonTextView[6] = (TextView) home_item_7.findViewById(R.id.tv_home_1);

        home_item_8 = (View) mView.findViewById(R.id.home_item_8);
        homeButtonImageView[7] = (ImageView) home_item_8.findViewById(R.id.img_home_1);
        homeButtonTextView[7] = (TextView) home_item_8.findViewById(R.id.tv_home_1);

        home_item_9 = (View) mView.findViewById(R.id.home_item_9);
        homeButtonImageView[8] = (ImageView) home_item_9.findViewById(R.id.img_home_1);
        homeButtonTextView[8] = (TextView) home_item_9.findViewById(R.id.tv_home_1);

        home_item_10 = (View) mView.findViewById(R.id.home_item_10);
        homeButtonImageView[9] = (ImageView) home_item_10.findViewById(R.id.img_home_1);
        homeButtonTextView[9] = (TextView) home_item_10.findViewById(R.id.tv_home_1);

        home_item_11 = (View) mView.findViewById(R.id.home_item_11);
        homeButtonImageView[10] = (ImageView) home_item_11.findViewById(R.id.img_home_1);
        homeButtonTextView[10] = (TextView) home_item_11.findViewById(R.id.tv_home_1);

        home_item_12 = (View) mView.findViewById(R.id.home_item_12);
        homeButtonImageView[11] = (ImageView) home_item_12.findViewById(R.id.img_home_1);
        homeButtonTextView[11] = (TextView) home_item_12.findViewById(R.id.tv_home_1);

        lin_home_btn.post(new Runnable() {
            @Override
            public void run() {
                int width = lin_home_btn.getMeasuredWidth();
                android.view.ViewGroup.LayoutParams lp = lin_home_btn.getLayoutParams();
                lp.height = width / 2 + width / 50;
                lin_home_btn.setPadding(0, 0, 0, width / 30);
                setHomeBtnImageLayout(width);
            }
        });
        lin_home_btn1.post(new Runnable() {
            @Override
            public void run() {
                int width = lin_home_btn1.getMeasuredWidth();
                android.view.ViewGroup.LayoutParams lp = lin_home_btn1.getLayoutParams();
                lp.height = width / 9 * 2;
                setHomeBtnImageLayout1(width);
            }
        });

        initViewData();
    }

    /**
     * 初始化指示器图片
     */
    private void initIndicator() {
        for (int i = 0; i < indicators.length; i++) {
            if (getActivity() == null) {
                return;
            }
            indicators[i] = new ImageView(getActivity());
            if (i == 0) {
                indicators[i].setBackgroundResource(R.mipmap.indicators_right_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
            pagerIndicator.addView(indicators[i]);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicators[i].getLayoutParams();
                layoutParams.leftMargin = 20;
                indicators[i].setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 设置home2布局位6个图片按钮的布局
     */
    private void setHomeBtnImageLayout(int w) {
        //设置置顶广告图片下图片按钮
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(w / 7, w / 7);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageParams.topMargin = w / 30;
        //设置置顶广告图片下文字按钮
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        for (int i = 0; i < 6; i++) {
            homeButtonImageView[i].setLayoutParams(imageParams);
            homeButtonTextView[i].setLayoutParams(textParams);
        }
    }

    /**
     * 设置home2布局位另6个图片按钮的布局
     */
    private void setHomeBtnImageLayout1(int w) {
        //设置置顶广告图片下图片按钮
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(w / 12, w / 12);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        imageParams.leftMargin = w / 27;
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //设置置顶广告图片下文字按钮
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(w / 4, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        for (int i = 6; i < 12; i++) {
            homeButtonImageView[i].setLayoutParams(imageParams);
            homeButtonTextView[i].setLayoutParams(textParams);
            homeButtonTextView[i].setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    /**
     * 设置界面初始化数据以及缓存数据，在网络不佳的情况下使用缓存数据。
     */
    private void initViewData() {
        //设置缓存本地图片
        if (getActivity() != null) {
            setCacheImg(0, CacheImgUtil.home_btn1);
            setCacheImg(1, CacheImgUtil.home_btn2);
            setCacheImg(2, CacheImgUtil.home_btn3);
            setCacheImg(3, CacheImgUtil.home_btn4);
            setCacheImg(4, CacheImgUtil.home_btn5);
            setCacheImg(5, CacheImgUtil.home_btn6);
            setCacheImg(6, CacheImgUtil.home_btn7);
            setCacheImg(7, CacheImgUtil.home_btn8);
            setCacheImg(8, CacheImgUtil.home_btn9);
            setCacheImg(9, CacheImgUtil.home_btn10);
            setCacheImg(10, CacheImgUtil.home_btn11);
            setCacheImg(11, CacheImgUtil.home_btn12);
        }
    }

    /**
     * 设置缓存本地图片
     */
    private void setCacheImg(int i, String filePath) {
        File imgFile = new File(filePath);
        if (imgFile != null && imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            homeButtonImageView[i].setImageBitmap(bitmap);
        } else {
//            homeButtonImageView[i].setBackgroundResource(homeBtnImageViewId[i]);
        }
    }

    private void setAdapter() {
        mAdapter = new HomeTodayHotAdapter(null, getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
        homeTopAdapter = new HomeTopAdapter(getActivity(), this);
        lv.setAdapter(homeTopAdapter);
    }

    public void initListener() {
        ((HomeTodayHotAdapter) mAdapter).setOnZanClickListenter(this);
        homeSearch.setOnClickListener(this);
//        mPullToRefreshListView.setScrollDistanceListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
//        mPullToRefreshListView.setOnScrollListener(this);
//        search.setOnClickListener(new TopBarClickListener(this.getActivity()));
        home_adv.setOnClickListener(this);
        homePager.setOnPageChangeListener(this);
        home_item_1.setOnClickListener(this);
        home_item_2.setOnClickListener(this);
        home_item_3.setOnClickListener(this);
        home_item_4.setOnClickListener(this);
        home_item_5.setOnClickListener(this);
        home_item_6.setOnClickListener(this);
        home_item_7.setOnClickListener(this);
        home_item_8.setOnClickListener(this);
        home_item_9.setOnClickListener(this);
        home_item_10.setOnClickListener(this);
        home_item_11.setOnClickListener(this);
        home_item_12.setOnClickListener(this);
        relweather.setOnClickListener(this);
    }

    /**
     * 获取首页数据信息和天气数据
     */
    private void getHome() {
        httpGetRequest(HomeApi.getHomeUrl(), HomeApi.API_GET_HOME);
        httpGetRequest(HomeApi.getHomeWeatherUrl(), HomeApi.API_GET_HOME_WEATHER);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        Log.e("json", json);
        switch (action) {
            case HomeApi.API_GET_HOME:
                homeHander(json);
                break;
            case ShareTextApi.API_GET_SHARE_LIST:
                hotHander(json);
                break;
            case HomeApi.API_GET_HOME_WEATHER:
                weatherHander(json);
                break;
            /*case ShareTextApi.API_GET_SHARE_ZAN:
                Toast.makeText(getActivity(),"点赞成功",Toast.LENGTH_SHORT).show();
                if(clickView != null){
                    ShareEntity entity = mAdapter.getItem(position);
                    entity.ok_count = String.valueOf(Integer.parseInt(entity.ok_count) + 1);
                    TextView zanText = (TextView) clickView.findViewById(R.id.tv_today_hot_viewnum);
                    zanText.setText(" "+entity.ok_count);
                }
                break;*/
        }
    }

    /**
     * 处理首页信息
     *
     * @param json
     */
    private void homeHander(String json) {
        homeEntity = JSON.parseObject(json, HomeEntity.class);
        if (homeEntity != null) {
            home1 = homeEntity.home1;
            home2 = homeEntity.home2;
            setData();
        }
    }

    /**
     * 处理天气数据
     */
    private void weatherHander(String json) {
        WeatherList weatherList = JSON.parseObject(json, WeatherList.class);
        if (weatherList != null) {
            if (!StringUtil.isEmpty(weatherList.weatherinfo.temp1)) {
                weatherCity.setText(weatherList.weatherinfo.city);
                weatherWendu.setText(weatherList.weatherinfo.temp1);
                imageLoader.displayImage(weatherList.weatherinfo.img_title1, weatherImg);
            } else {
                relweather.setVisibility(View.GONE);
            }
        } else {
            relweather.setVisibility(View.GONE);
        }
    }

    /**
     * 处理本期热门数据
     *
     * @param json
     */
    private void hotHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            ShareList shareList = JSON.parseObject(json, ShareList.class);
            if (null != shareList) {
                List<ShareEntity> share = shareList.goods_list;
                if (title_layout_fx != null && !ListUtils.isEmpty(share)) {
                    title_layout_fx.setVisibility(View.VISIBLE);
                }
                share.add(new ShareEntity());
                appendData(share, start);
                List<BDZXEntity> bdzxEntities = shareList.bdzx_list;
                List<LYJDEntity> lyjdEntities = shareList.lyjd_list;
                ArrayList<Object> list = new ArrayList();
                list.addAll(bdzxEntities);
                list.addAll(lyjdEntities);
                if (mPager.getPage() == 0) {
                    homeTopAdapter.replaceList(list);
                    homeTopAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void setData() {
        //设置置顶广告图片
        if (!ListUtils.isEmpty(home1)) {
            List<TopInfo> topInfos = new ArrayList<TopInfo>();
            for (HomeAdv homeAdv : home1) {
                TopInfo topInfo = new TopInfo();
                topInfo.Id = homeAdv.adv_id;
                topInfo.title = homeAdv.adv_title;
                topInfo.pciture = homeAdv.adv_pic;
                topInfo.picUrl = homeAdv.adv_pic_url;
                topInfo.adv_video_url = homeAdv.adv_video_url;
                topInfos.add(topInfo);
            }
            indicators = new ImageView[topInfos.size()]; // 定义指示器数组大小
            initIndicator();
            mAdAdapter = new TopOneAdapter(getActivity(), "home");
            mAdAdapter.setData(topInfos);
            mAdAdapter.setInfiniteLoop(true);
            homePager.setAdapter(mAdAdapter);
            homePager.setAutoScrollDurationFactor(10);
            homePager.setInterval(2000);
            homePager.startAutoScroll();
            mAdAdapter.setOnModelClickListener(this);
            if (topInfos != null) {
                homePager.setCurrentItem(topInfos.size());
            }
            home_adv.setVisibility(View.INVISIBLE);
            rl_home_pager.setVisibility(View.VISIBLE);
        }
        //设置置顶广告图片下方八个按钮
        if (!ListUtils.isEmpty(home2)) {
            for (int i = 0; i < home2.size(); i++) {
                if (i > 11) return;
                if (!StringUtil.isEmpty(home2.get(i).adv_title)) {
                    homeButtonTextView[i].setText(home2.get(i).adv_title);
                } else {
                    homeButtonTextView[i].setText(homeButtonText[i]);
                }
                if (!StringUtil.isEmpty(home2.get(i).adv_pic)) {
                    if (i == 0) {
                        imageLoader.displayImage(home2.get(0).adv_pic, homeButtonImageView[0],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn1));
                    }
                    if (i == 1) {
                        imageLoader.displayImage(home2.get(1).adv_pic, homeButtonImageView[1],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn2));
                    }
                    if (i == 2) {
                        imageLoader.displayImage(home2.get(2).adv_pic, homeButtonImageView[2],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn3));
                    }
                    if (i == 3) {
                        imageLoader.displayImage(home2.get(3).adv_pic, homeButtonImageView[3],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn4));
                    }
                    if (i == 4) {
                        imageLoader.displayImage(home2.get(4).adv_pic, homeButtonImageView[4],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn5));
                    }
                    if (i == 5) {
                        imageLoader.displayImage(home2.get(5).adv_pic, homeButtonImageView[5],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn6));
                    }
                    if (i == 6) {
                        imageLoader.displayImage(home2.get(6).adv_pic, homeButtonImageView[6],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn7));
                    }
                    if (i == 7) {
                        imageLoader.displayImage(home2.get(7).adv_pic, homeButtonImageView[7],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn8));
                    }
                    if (i == 8) {
                        imageLoader.displayImage(home2.get(8).adv_pic, homeButtonImageView[8],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn9));
                    }
                    if (i == 9) {
                        imageLoader.displayImage(home2.get(9).adv_pic, homeButtonImageView[9],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn10));
                    }
                    if (i == 10) {
                        imageLoader.displayImage(home2.get(10).adv_pic, homeButtonImageView[10],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn11));
                    }
                    if (i == 11) {
                        imageLoader.displayImage(home2.get(11).adv_pic, homeButtonImageView[11],
                                AsynImageUtil.getImageOptions(),
                                new AsynImageUtil.PromotionNotFirstDisplayListener(CacheImgUtil.home_btn12));
                    }
                }
            }
        }
    }

    /**
     * 获取热门分享数据
     */
    @Override
    protected void loadData() {
        httpGetRequest(ShareTextApi.getHotShareListUrl(mPager.rows + "", mPager.getPage() + ""), ShareTextApi.API_GET_SHARE_LIST);
    }

    /**
     * 设置顶部标题栏随滑动变色，随下拉刷新隐藏显示
     * @param scrollDistance
     *//*
    @Override
    public void onScrollDistance(int scrollDistance) {
        if(scrollDistance >= 0 && home_adv != null && home_adv.getHeight() > 0 ){
            if(mPullToRefreshListView.isRefreshing() && mPullToRefreshListView.getCurrentMode()
                    == PullToRefreshBase.Mode.PULL_FROM_START){
                int distance1 = getScrollDistance();
                if(distance1 > 10){
                    return;
                }
            }
            if(mPullToRefreshListView.isRefreshing() && mPullToRefreshListView.getCurrentMode()
                    == PullToRefreshBase.Mode.PULL_FROM_END){
                int distance2 = getScrollDistance();
                if(distance2 > 10){
                    top.setVisibility(View.GONE);
                    return;
                }
            }
//            Log.e("mode", mPullToRefreshListView.getCurrentMode() + "");
            top.setVisibility(View.VISIBLE);
            //define it for scroll height
            if(home_advHeight == 0){
                home_advHeight = home_adv.getHeight();
            }
            if(distance < home_advHeight * 1.5){//本期热门数据没有占满屏幕时调用
                if(scrollDistance < home_advHeight){
                    int progress = (int)(new Float(scrollDistance)/new Float(home_advHeight) * 230);//255
                    top.getBackground().setAlpha(progress);
                }else{
                    top.getBackground().setAlpha(255 - 25);
                }
            }
        } else {
            top.setVisibility(View.GONE);
        }
    }*/

    /**
     * 获取正在刷新时广告滑动的距离
     *
     * @return
     */
    /*private int getScrollDistance() {
        if(StatusBarHeight == 0){
            StatusBarHeight = getStatusBarHeight();
        }
        int[] location = new int[2];
        home_adv.getLocationOnScreen(location);
        return location[1] - StatusBarHeight;
    }*/
    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search:
                Intent searchIntent = new Intent(getActivity(), SearchHistoryActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.home_adv:
                //startActivity(new Intent(getActivity(), LocationListActivity.class));
                /*Uri uri = Uri.parse("geo:31.78341,117.21893,合肥徽园");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,uri);
                Intent mapIntent1 = Intent.createChooser(mapIntent, "请选择打开方式");
                startActivity(mapIntent1);*/
//               startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.home_item_1:
                if (home2.size() > 0) {
                    divide(home2.get(0).adv_pic_url, home2.get(0).adv_title);
                }
                break;
            case R.id.home_item_2:
                if (home2.size() > 1) {
                    divide(home2.get(1).adv_pic_url, home2.get(1).adv_title);
                }
                break;
            case R.id.home_item_3:
                if (home2.size() > 2) {
                    divide(home2.get(2).adv_pic_url, home2.get(2).adv_title);
                }
                break;
            case R.id.home_item_4:
                if (home2.size() > 3) {
                    divide(home2.get(3).adv_pic_url, home2.get(3).adv_title);
                }
                break;
            case R.id.home_item_5:
                if (home2.size() > 4) {
                    divide(home2.get(4).adv_pic_url, home2.get(4).adv_title);
                }
                break;
            case R.id.home_item_6:
                if (home2.size() > 5) {
                    divide(home2.get(5).adv_pic_url, home2.get(5).adv_title);
                }
                break;
            case R.id.home_item_7:
                if (home2.size() > 6) {
                    divide(home2.get(6).adv_pic_url, home2.get(6).adv_title);
                }
                break;
            case R.id.home_item_8:
                if (home2.size() > 7) {
                    divide(home2.get(7).adv_pic_url, home2.get(7).adv_title);
                }
                break;
            case R.id.home_item_9:
                if (home2.size() > 8) {
                    divide(home2.get(8).adv_pic_url, home2.get(8).adv_title);
                }
                break;
            case R.id.home_item_10:
                if (home2.size() > 9) {
                    divide(home2.get(9).adv_pic_url, home2.get(9).adv_title);
                }
                break;
            case R.id.home_item_11:
                if (home2.size() > 10) {
                    divide(home2.get(10).adv_pic_url, home2.get(10).adv_title);
                }
                break;
            case R.id.home_item_12:
                if (home2.size() > 11) {
                    divide(home2.get(11).adv_pic_url, home2.get(11).adv_title);
                }
                break;
            case R.id.rel_home_weather:
                Intent weather = new Intent(getActivity(), WeatherWebActivity.class);
                startActivity(weather);
                break;
        }
    }

   /* @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }*/

   /* @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(StatusBarHeight == 0){
            StatusBarHeight = getStatusBarHeight();
        }
        int[] location = new int[2];
        home_adv.getLocationOnScreen(location);
        distance = location[1] - StatusBarHeight;
        if(distance <= 1 && home_adv != null && home_adv.getHeight() > 0 ){
            distance = Math.abs(distance);
            top.setVisibility(View.VISIBLE);
            //define it for scroll height
            if(home_advHeight == 0){
                home_advHeight = home_adv.getHeight();
            }
            if(distance < home_advHeight){
                int progress = (int)(new Float(distance)/new Float(home_advHeight) * 230);//255
                top.getBackground().setAlpha(progress);
            }else{
                top.getBackground().setAlpha(255 - 25);
            }
        } else {
            top.setVisibility(View.GONE);
        }
    }*/

    /*public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 更改指示器图片
        int pos = (position) % ListUtils.getSize(home1);
        for (int i = 0; i < indicators.length; i++) {
            if (pos == i) {
                indicators[pos].setBackgroundResource(R.mipmap.indicators_right_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public TopInfo mCurrTopInfo;

    @Override
    public void gotoShop(String key, TopInfo topInfo) {
        mCurrTopInfo = topInfo;
        divide(key, "");
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     *
     * @param keyStr
     */
    private void divide(String keyStr, String title) {
        if (!StringUtil.isEmpty(keyStr)) {
            if (getActivity() != null) {
                if (keyStr.equals("yhq")) {
                    if (configEntity.isLogin) {
                        Intent couponIntent = new Intent(getActivity(), PointMallActivity.class);
                        startActivity(couponIntent);
                    } else {
                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                    return;
                }
                String listStr = null;
                if (keyStr.length() >= 4) {
                    listStr = keyStr.substring(0, 4);
                } else {
                    listStr = keyStr;
                }
                if (listStr.contains("^^")) {
                    //列表规则
                    Intent listIntent = new Intent(getActivity(), LocationActivity.class);
                    if ("^^jd".equals(listStr)) {//酒店列表
                        listIntent.putExtra("index", 0);
                        listIntent.putExtra("type", "sleep");
                    } else if ("^^xx".equals(listStr)) {//体验列表
                        listIntent.putExtra("index", 4);
                        listIntent.putExtra("type", "live");
                    } else if ("^^ms".equals(listStr)) {//美食列表
                        listIntent.putExtra("index", 1);
                        listIntent.putExtra("type", "food");
                    } else if ("^^mp".equals(listStr)) {//景点列表
                        Intent gridIntent = new Intent(getActivity(), LocationViewGridActivity.class);
                        /*listIntent.putExtra("index", 2);
                        listIntent.putExtra("type", "view");*/
                        startActivity(gridIntent);
                        return;
                    } else if ("^^video".equals(keyStr)) {
                        VideoActivity.startActivity(getActivity(), mCurrTopInfo);
                        return;
                    } else if ("^^tc".equals(listStr)) {//当地特产
                        listIntent.putExtra("index", 3);
                        listIntent.putExtra("type", "location");
                    } else if ("^^tg".equals(listStr)) {//团购优惠
                        listIntent.putExtra("index", 5);
                        listIntent.putExtra("type", "group");
                    } else if ("^^wh".equals(listStr)) {//藏东文化
                        listIntent.putExtra("index", 6);
                        listIntent.putExtra("mod_id", "2020002000");
                        listIntent.putExtra("type", "culture");
                    } else if ("^^pc".equals(listStr)) {//拼车
                        if (!configEntity.isLogin) {
                            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            return;
                        } else {
                            Intent sendIntent = new Intent(getActivity(), SendCarpoolingActivity.class);
                            startActivity(sendIntent);
                            return;
                        }
                    } else if ("^^wl".equals(listStr)) {//物流
                        Intent logisticIntent = new Intent(getActivity(), LogisticListActivity.class);
                        startActivity(logisticIntent);
                        return;
                    } else if ("^^jy".equals(listStr) || "^^yz".equals(listStr) || "^^cs".equals(listStr)) {//救援
                        Intent logisticIntent = new Intent(getActivity(), AlarmRescueActivity.class);
                        String index = keyStr.substring(4);
                        logisticIntent.putExtra("index", index);
                        startActivity(logisticIntent);
                        return;
                    } else if ("^^zx".equals(listStr)) {
                        listIntent.putExtra("index", 6);
                        listIntent.putExtra("mod_id", "2020001000");
                        listIntent.putExtra("type", "culture");
                    } else if ("^^fj".equals(listStr)) {
                        Intent logisticIntent = new Intent(getActivity(), NearbyActivity.class);
                        startActivity(logisticIntent);
                        return;
                    } else if ("^^fy".equals(listStr)) {
                        Intent intent = new Intent(getActivity(), TranslateActivity.class);
                        startActivity(intent);
                        return;
                    } else if ("^^fg".equals(listStr)) {
                        String mid_id = keyStr.substring(4);
                        MyWebActivity.startActivity(getActivity(), ConfigUtil.HTTP_FENG_GANG_DETAIL + mid_id, title);
                        return;
                    } else {
                        return;
                    }
                    startActivity(listIntent);
                } else {
                    String detailStr = keyStr.substring(0, 2);
                    String goodsId = keyStr.substring(2);
                    //详情规则
                    Intent detailIntent = new Intent(getActivity(), LocationDetailActivity.class);
                    if ("**".equals(detailStr)) {//景点详情
                        detailIntent.putExtra("type", "sleep");
                        detailIntent.putExtra("goods_id", goodsId);
                    } else if ("##".equals(detailStr)) {//美食详情
                        detailIntent.putExtra("type", "food");
                        detailIntent.putExtra("goods_id", goodsId);
                    } else if ("!!".equals(detailStr)) {//酒店详情
                        detailIntent.putExtra("type", "sleep");
                        detailIntent.putExtra("goods_id", goodsId);
                    } else if ("~~".equals(detailStr)) {//体验详情
                        detailIntent.putExtra("type", "live");
                        detailIntent.putExtra("goods_id", goodsId);
                    } else {
                        return;
                    }
                    startActivity(detailIntent);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (0 == (position - 1)) {
            return;
        }
        if (mAdapter != null && mAdapter.getCount() > (position - 2)) {
            ShareEntity share = mAdapter.getItem(position - 2);
            if (null != share) {
                Intent intent = new Intent(getActivity(), ShareDetailActivity.class);
                intent.putExtra("shareId", share.friend_id);
                intent.putExtra("commentNum", share.comment_count);
                intent.putExtra("shareTitle", share.title);
                startActivity(intent);
            }
        }
    }

    /**
     * 点赞分享
     */
    public void zan(String shareId, View clickView, int position) {
        /*if (configEntity.isLogin) {
            this.clickView = clickView;
            this.position = position;
            httpGetRequest(ShareTextApi.getZanShareUrl(configEntity.key, shareId), ShareTextApi.API_GET_SHARE_ZAN);
        } else {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onListItemClick(Object o) {
        Intent i;
        if (o instanceof LYJDEntity) {
            LYJDEntity entity = (LYJDEntity) o;
            i = new Intent(getActivity(), LocationDetailActivity.class);
            i.putExtra("type", "view");
            i.putExtra("goods_id", entity.getGoods_id() + "");
            startActivity(i);
        } else if (o instanceof BDZXEntity) {
            BDZXEntity entity = (BDZXEntity) o;
            i = new Intent(getActivity(), NewsDetailActivity.class);
            i.putExtra("url", entity.getN_url());
            startActivity(i);
        }
    }
}
