package com.yishangshuma.bangelvyou.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.HomeTodayHotAdapter;
import com.yishangshuma.bangelvyou.adapter.TopOneAdapter;
import com.yishangshuma.bangelvyou.api.HomeApi;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.entity.HomeAdv;
import com.yishangshuma.bangelvyou.entity.HomeEntity;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.ShopList;
import com.yishangshuma.bangelvyou.entity.TopInfo;
import com.yishangshuma.bangelvyou.entity.WeatherList;
import com.yishangshuma.bangelvyou.listener.TopBarClickListener;
import com.yishangshuma.bangelvyou.ui.activity.LocationDetailActivity;
import com.yishangshuma.bangelvyou.ui.activity.LocationListActivity;
import com.yishangshuma.bangelvyou.ui.activity.LoginActivity;
import com.yishangshuma.bangelvyou.ui.activity.PointMallActivity;
import com.yishangshuma.bangelvyou.ui.activity.WeatherWebActivity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.DensityUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.wibget.AutoScrollViewPager;
import com.yishangshuma.bangelvyou.wibget.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 * Created by Administrator on 2016/1/25.
 */
public class HomeFragment extends AbsLoadMoreFragment<ListView, LocationEntity> implements PullToRefreshListView.OnScrollDistanceListener
                                        ,View.OnClickListener, AbsListView.OnScrollListener, ViewPager.OnPageChangeListener
                                        , TopOneAdapter.OnModelClickListener,AdapterView.OnItemClickListener {

    private View mView, topView, top, rl_home_pager;
    private PullToRefreshListView mPullToRefreshListView;
    private ImageView home_adv;
    private int home_advHeight, StatusBarHeight, distance;
    private TextView search;
    private View home_item_1, home_item_2, home_item_3, home_item_4;
    private TextView[] homeButtonTextView = new TextView[4];
    private CircleImageView[] homeButtonImageView = new CircleImageView[4];
    private int[] homeBtnImageViewId = {R.mipmap.home_btn_img1, R.mipmap.home_btn_img2, R.mipmap.home_btn_img3, R.mipmap.home_btn_img4};
    private String homeButtonText[] = {"景点攻略","线下体验","酒店住宿", "餐饮美食"};
    private AutoScrollViewPager homePager;
    private LinearLayout pagerIndicator;
    private ImageView[] indicators = null;
    private HomeEntity homeEntity;
    private List<HomeAdv> home1 = new ArrayList<HomeAdv>();
    private List<HomeAdv> home2 = new ArrayList<HomeAdv>();
    private TopOneAdapter mAdAdapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private View relWeather;
    private TextView weather;

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
        if(null != getActivity()){
            configEntity = ConfigUtil.loadConfig(getActivity());
        }
    }

    private void initView(LayoutInflater inflater){
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.home_list);
        top = (View) mView.findViewById(R.id.top);
        top.getBackground().setAlpha(0);
        search = (TextView) top.findViewById(R.id.home_top_title_search);
        topView = inflater.inflate(R.layout.home_list_header, null);
        relWeather = topView.findViewById(R.id.rel_home_weather);//天气布局
        weather = (TextView) topView.findViewById(R.id.tv_home_weather_show);//显示天气
        homePager = (AutoScrollViewPager) topView.findViewById(R.id.home_pager);
        rl_home_pager = (View) topView.findViewById(R.id.rl_home_pager);
        pagerIndicator = (LinearLayout) topView.findViewById(R.id.home_pager_indicator);
        home_adv = (ImageView) topView.findViewById(R.id.home_adv);
        mPullToRefreshListView.getRefreshableView().addHeaderView(topView);

        home_item_1 = (View) mView.findViewById(R.id.home_item_1);
        homeButtonImageView[0] = (CircleImageView) home_item_1.findViewById(R.id.img_home);
        homeButtonTextView[0] = (TextView) home_item_1.findViewById(R.id.tv_home);

        home_item_2 = (View) mView.findViewById(R.id.home_item_2);
        homeButtonImageView[1] = (CircleImageView) home_item_2.findViewById(R.id.img_home);
        homeButtonTextView[1] = (TextView) home_item_2.findViewById(R.id.tv_home);

        home_item_3 = (View) mView.findViewById(R.id.home_item_3);
        homeButtonImageView[2] = (CircleImageView) home_item_3.findViewById(R.id.img_home);
        homeButtonTextView[2] = (TextView) home_item_3.findViewById(R.id.tv_home);

        home_item_4 = (View) mView.findViewById(R.id.home_item_4);
        homeButtonImageView[3] = (CircleImageView) home_item_4.findViewById(R.id.img_home);
        homeButtonTextView[3] = (TextView) home_item_4.findViewById(R.id.tv_home);

        setHomeBtnImageLayout();
        initViewData();
    }

    /**
     * 初始化指示器图片
     */
    private void initIndicator(){
        for (int i = 0; i < indicators.length; i++) {
            if(getActivity() == null){
                return;
            }
            indicators[i] = new ImageView(getActivity());
            if(i == 0){
                indicators[i].setBackgroundResource(R.mipmap.indicators_right_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
            pagerIndicator.addView(indicators[i]);
            if(i != 0){
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicators[i].getLayoutParams();
                layoutParams.leftMargin = 20;
                indicators[i].setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 设置home2布局位八个图片按钮的布局
     */
    private void setHomeBtnImageLayout(){
        //设置置顶广告图片下图片按钮
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getActivity(), 60), DensityUtil.dip2px(getActivity(), 60));
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //设置置顶广告图片下文字按钮
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        for(int i = 0; i < 4; i++){
            homeButtonImageView[i].setLayoutParams(imageParams);

            homeButtonTextView[i].setLayoutParams(textParams);
            homeButtonTextView[i].setGravity(Gravity.CENTER_HORIZONTAL);
            homeButtonTextView[i].setText(homeButtonText[i]);
        }

    }

    /**
     * 设置界面初始化数据以及缓存数据，在网络不佳的情况下使用缓存数据。
     */
    private void initViewData(){
        //设置缓存本地图片
        if(getActivity() != null){
            setCacheImg(0, CacheImgUtil.home_btn1);
            setCacheImg(1, CacheImgUtil.home_btn2);
            setCacheImg(2, CacheImgUtil.home_btn3);
            setCacheImg(3, CacheImgUtil.home_btn4);
        }
    }

    /**
     * 设置缓存本地图片
     */
    private void setCacheImg(int i, String filePath){
        File imgFile = new File(filePath);
        if (imgFile != null && imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            homeButtonImageView[i].setImageBitmap(bitmap);
        } else {
            homeButtonImageView[i].setBackgroundResource(homeBtnImageViewId[i]);
        }
    }

    private void setAdapter(){
        mAdapter = new HomeTodayHotAdapter(null, getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener(){
        mPullToRefreshListView.setScrollDistanceListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        mPullToRefreshListView.setOnScrollListener(this);
        search.setOnClickListener(new TopBarClickListener(this.getActivity()));
        home_adv.setOnClickListener(this);
        homePager.setOnPageChangeListener(this);
        home_item_1.setOnClickListener(this);
        home_item_2.setOnClickListener(this);
        home_item_3.setOnClickListener(this);
        home_item_4.setOnClickListener(this);
        relWeather.setOnClickListener(this);
    }

    /**
     * 获取首页数据信息
     */
    private void getHome(){
        httpGetRequest(HomeApi.getHomeUrl(), HomeApi.API_GET_HOME);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HomeApi.API_GET_HOME:
                homeHander(json);
                break;
            case LocationApi.API_GET_LOCATION_LIST:
                hotHander(json);
                break;
            case HomeApi.API_GET_HOME_WEATHER:
                weatherHander(json);
                break;
        }
    }

    /**
     * 处理首页信息
     * @param json
     */
    private void homeHander(String json){
        homeEntity = JSON.parseObject(json, HomeEntity.class);
        if(homeEntity != null){
            home1 = homeEntity.home1;
            home2 = homeEntity.home2;
            setData();
        }
    }

    /**
     * 处理天气数据
     */
    private void weatherHander(String json){
        WeatherList weatherList = JSON.parseObject(json, WeatherList.class);
        if(weatherList != null){
            weather.setText(weatherList.weatherinfo.day + " 温度: " + weatherList.weatherinfo.temp1);
        }else{
            relWeather.setVisibility(View.GONE);
        }
    }

    /**
     * 处理本期热门数据
     * @param json
     */
    private void hotHander(String json){
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if(!"".equals(json)){
            ShopList shopList = JSON.parseObject(json, ShopList.class);
            if(shopList != null){
                List<ShopEntity> shop = shopList.goods_list;
                locationList = new ArrayList<>();
                for(int i = 0; i < shop.size(); i++){
                    ShopEntity shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = shopEntity.goods_content;
                    //location.location_date = shopEntity.n_add_date;
                    location.location_id = shopEntity.goods_id;
                    location.location_img = shopEntity.goods_image_url;
                    location.location_title = shopEntity.goods_name;
                    location.location_visit_count = shopEntity.goods_salenum;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_type = shopEntity.cls_type;
                    locationList.add(location);
                }
            }
            appendData(locationList, start);
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
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        if (!ListUtils.isEmpty(home2)) {
            for (int i = 0; i < home2.size(); i++) {
                if (null != home2.get(i).adv_title && !"".equals(home2.get(i).adv_title)) {
                    homeButtonTextView[i].setText(home2.get(i).adv_title);
                    homeButtonTextView[i].setLayoutParams(textParams);
                    homeButtonTextView[i].setGravity(Gravity.CENTER_HORIZONTAL);
                }
                if (null != home2.get(i).adv_pic) {
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
                }
            }
        }
    }

    /**
     * 获取本期热门推荐景点数据
     */
    @Override
    protected void loadData() {
        httpGetRequest(HomeApi.getHomeWeatherUrl(),HomeApi.API_GET_HOME_WEATHER);
        httpGetRequest(LocationApi.getVirtualShopListUrl("4", mPager.rows + "", mPager.getPage() + ""
                , null,null,null, "2", null, null, null), LocationApi.API_GET_LOCATION_LIST);
    }

    /**
     * 设置顶部标题栏随滑动变色，随下拉刷新隐藏显示
     * @param scrollDistance
     */
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
    }

    /**
     * 获取正在刷新时广告滑动的距离
     * @return
     */
    private int getScrollDistance() {
        if(StatusBarHeight == 0){
            StatusBarHeight = getStatusBarHeight();
        }
        int[] location = new int[2];
        home_adv.getLocationOnScreen(location);
        return location[1] - StatusBarHeight;
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_adv:
                //startActivity(new Intent(getActivity(), LocationListActivity.class));
                /*Uri uri = Uri.parse("geo:31.78341,117.21893,合肥徽园");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,uri);
                Intent mapIntent1 = Intent.createChooser(mapIntent, "请选择打开方式");
                startActivity(mapIntent1);*/
//               startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.home_item_1:
                if(home2.size() > 0){
                    divide(home2.get(0).adv_pic_url);
                }
                break;
            case R.id.home_item_2:
                if(home2.size() > 1){
                    divide(home2.get(1).adv_pic_url);
                }
                break;
            case R.id.home_item_3:
                if(home2.size() > 2){
                    divide(home2.get(2).adv_pic_url);
                }
                break;
            case R.id.home_item_4:
                if(home2.size() > 3){
                    divide(home2.get(3).adv_pic_url);
                }
                break;
            case R.id.rel_home_weather:
                Intent weather = new Intent(getActivity(), WeatherWebActivity.class);
                startActivity(weather);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
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
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 更改指示器图片
        int pos = (position) % ListUtils.getSize(home1);
        for (int i = 0; i < indicators.length; i++) {
            if(pos == i){
                indicators[pos].setBackgroundResource(R.mipmap.indicators_right_now);
            } else{
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void gotoShop(String key) {
        divide(key);
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     * @param keyStr
     */
    private void divide(String keyStr){
        if(keyStr != null && !"".equals(keyStr)){
            if (getActivity() != null) {
                if(keyStr.equals("yhq")){
                    if(configEntity.isLogin){
                        Intent couponIntent = new Intent(getActivity(), PointMallActivity.class);
                        startActivity(couponIntent);
                    }else{
                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                    return;
                }
                String listStr = keyStr.substring(0, 4);
                if(listStr.contains("^^")){
                    //列表规则
                    Intent listIntent = new Intent(getActivity(), LocationListActivity.class);
                    if("^^jd".equals(listStr)){//酒店列表
                        listIntent.putExtra("type", "sleep");
                    }else if("^^xx".equals(listStr)){//体验列表
                        listIntent.putExtra("type", "live");
                    }else if("^^ms".equals(listStr)){//美食列表
                        listIntent.putExtra("type", "food");
                    }else if ("^^mp".equals(listStr)){//景点列表
                        listIntent.putExtra("type", "view");
                    }else {
                        return;
                    }
                    startActivity(listIntent);
                }else{
                    String detailStr = keyStr.substring(0, 2);
                    String goodsId = keyStr.substring(2);
                    //详情规则
                    Intent detailIntent = new Intent(getActivity(), LocationDetailActivity.class);
                    if("**".equals(detailStr)){//景点详情
                        detailIntent.putExtra("type", "sleep");
                        detailIntent.putExtra("id", goodsId);
                    } else if("##".equals(detailStr)){//美食详情
                        detailIntent.putExtra("type", "food");
                        detailIntent.putExtra("id", goodsId);
                    } else if("!!".equals(detailStr)){//酒店详情
                        detailIntent.putExtra("type", "sleep");
                        detailIntent.putExtra("id", goodsId);
                    }else if("~~".equals(detailStr)){//体验详情
                        detailIntent.putExtra("type", "live");
                        detailIntent.putExtra("id", goodsId);
                    }else{
                        return;
                    }
                    startActivity(detailIntent);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if( 0 == (position - 1)){
            return;
        }
        if(mAdapter != null && mAdapter.getCount() > (position - 2)){
            LocationEntity location = mAdapter.getItem(position - 2);
            if(location != null){
                Intent intent = new Intent(getActivity(), LocationDetailActivity.class);
                String type = location.location_type;
                if(null != type && !"".equals(type)){
                    String typeStr = type.substring(0,2);
                    if("**".equals(typeStr)){//景点详情
                        intent.putExtra("type", "view");
                    } else if("##".equals(typeStr)){//美食详情
                        intent.putExtra("type", "food");
                    } else if("!!".equals(typeStr)){//酒店详情
                        intent.putExtra("type", "sleep");
                    }else if("~~".equals(typeStr)){//体验详情
                        intent.putExtra("type", "live");
                    }else{
                        return;
                    }
                    intent.putExtra("image_url", location.location_img);
                    intent.putExtra("name", location.location_title);
                    intent.putExtra("id", location.location_id);
                    startActivity(intent);
                }
            }
        }
    }
}
