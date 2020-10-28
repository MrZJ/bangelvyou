package com.shishoureport.system.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.HomeResList;
import com.shishoureport.system.ui.activity.WebActivity;
import com.shishoureport.system.ui.adapter.HomeAdapter;
import com.shishoureport.system.ui.adapter.TopOneAdapter;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.FrescoHelper;
import com.shishoureport.system.utils.HttpAction;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.AutoScrollViewPager;

/**
 * Created by jianzhang.
 * on 2017/5/25.
 * copyright easybiz.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        TopOneAdapter.OnModelClickListener, View.OnClickListener, HomeAdapter.HomeListClick {
    private SwipeRefreshLayout mSrfLayout;
    private ListView mListView;
    private AutoScrollViewPager mViewPager;
    private View mHeaderView;
    private SimpleDraweeView mMarketSdv;
    private TextView mMarketTv;
    private SimpleDraweeView mCountrySdv;
    private TextView mCountryTv;
    private SimpleDraweeView mAboutSdv;
    private TextView mAboutTv;
    private View mNetMarketView, mCountryView, mAboutView;
    private TopOneAdapter mAdAdapter;
    private HomeAdapter mListAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View v) {
        hideRightSide();
        setLeftBackButton(false);
        setTopTitle("首页");
        mSrfLayout = (SwipeRefreshLayout) v.findViewById(R.id.sf_layout);
        mListView = (ListView) v.findViewById(R.id.home_list);
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_header, null);
        mViewPager = (AutoScrollViewPager) mHeaderView.findViewById(R.id.viewpager);
        mNetMarketView = mHeaderView.findViewById(R.id.net_market_layout);
        mMarketSdv = (SimpleDraweeView) mHeaderView.findViewById(R.id.net_market_iv);
        mMarketTv = (TextView) mHeaderView.findViewById(R.id.net_market_tv);
        mCountryView = mHeaderView.findViewById(R.id.country_layout);
        mCountrySdv = (SimpleDraweeView) mHeaderView.findViewById(R.id.country_iv);
        mCountryTv = (TextView) mHeaderView.findViewById(R.id.country_tv);
        mAboutView = mHeaderView.findViewById(R.id.about_us_layout);
        mAboutSdv = (SimpleDraweeView) mHeaderView.findViewById(R.id.about_us_iv);
        mAboutTv = (TextView) mHeaderView.findViewById(R.id.about_us_tv);
        mListView.addHeaderView(mHeaderView);
        mSrfLayout.setOnRefreshListener(this);
//        mSrfLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light,
//                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        mSrfLayout.setRefreshing(true);
        mNetMarketView.setOnClickListener(this);
        mCountryView.setOnClickListener(this);
        mAboutView.setOnClickListener(this);
        loadData();
    }

    public void loadData() {
        httpGetRequest(ConfigUtil.HTTP_HOME, HttpAction.HTTP_HOME);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    protected void httpResponse(String json, int action) {
        mSrfLayout.setRefreshing(false);
        if (!StringUtil.isEmpty(json)) {
            try {
                HomeResList homeRes = JSONObject.parseObject(json, HomeResList.class);
                if (homeRes != null) {
                    setData(homeRes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(HomeResList homeRes) {
        //顶部viewpager设置
        if (!ListUtils.isEmpty(homeRes.home1310)) {
            mAdAdapter = new TopOneAdapter(getActivity(), "home");
            mAdAdapter.setData(homeRes.home1310);
            mAdAdapter.setInfiniteLoop(true);
            mViewPager.setAdapter(mAdAdapter);
            mViewPager.setAutoScrollDurationFactor(10);
            mViewPager.setInterval(2000);
            mViewPager.startAutoScroll();
            mAdAdapter.setOnModelClickListener(this);
            if (homeRes.home1310 != null) {
                mViewPager.setCurrentItem(homeRes.home1310.size());
            }
        }
        //三个按钮的设置
        if (!ListUtils.isEmpty(homeRes.home1320) && homeRes.home1320.size() >= 3) {
            mMarketTv.setText(homeRes.home1320.get(0).adv_title);
            FrescoHelper.loadImage(homeRes.home1320.get(0).adv_pic, mMarketSdv);
            mNetMarketView.setTag(homeRes.home1320.get(0).adv_pic_url);
            mCountryTv.setText(homeRes.home1320.get(1).adv_title);
            FrescoHelper.loadImage(homeRes.home1320.get(1).adv_pic, mCountrySdv);
            mCountryView.setTag(homeRes.home1320.get(1).adv_pic_url);
            mAboutTv.setText(homeRes.home1320.get(2).adv_title);
            FrescoHelper.loadImage(homeRes.home1320.get(2).adv_pic, mAboutSdv);
            mAboutView.setTag(homeRes.home1320.get(2).adv_pic_url);
        }
        //listview设置
        mListAdapter = new HomeAdapter(getActivity(), this);
        mListAdapter.reSetDataAndNotify(homeRes.home1330);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void gotoShop(String key) {
        WebActivity.startActivity(getActivity(), key);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.net_market_layout:
                WebActivity.startActivity(getActivity(), (String) mNetMarketView.getTag());
                break;
            case R.id.country_layout:
                WebActivity.startActivity(getActivity(), (String) mCountryView.getTag());
                break;
            case R.id.about_us_layout:
                WebActivity.startActivity(getActivity(), (String) mAboutView.getTag());
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        mSrfLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void listClick(String url) {
        WebActivity.startActivity(getActivity(), url);
    }
}
