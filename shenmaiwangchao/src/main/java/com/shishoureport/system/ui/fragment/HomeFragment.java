package com.shishoureport.system.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.HomeResList;
import com.shishoureport.system.ui.adapter.HomeAdapter;
import com.shishoureport.system.ui.adapter.TopOneAdapter;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.HttpAction;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.AutoScrollViewPager;

/**
 * Created by jianzhang.
 * on 2017/5/25.
 * copyright easybiz.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TopOneAdapter.OnModelClickListener, View.OnClickListener {
    private SwipeRefreshLayout mSrfLayout;
    private ListView mListView;
    private AutoScrollViewPager mViewPager;
    private View mHeaderView;
    private SimpleDraweeView importProIv, getProIv, myFocusIv, helpIv;
    private TextView importProTv, getProTv, myFocusTv, helpTv;
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
        importProIv = (SimpleDraweeView) mHeaderView.findViewById(R.id.import_pro_iv);
        importProTv = (TextView) mHeaderView.findViewById(R.id.import_pro_tv);
        getProIv = (SimpleDraweeView) mHeaderView.findViewById(R.id.get_product_iv);
        getProTv = (TextView) mHeaderView.findViewById(R.id.import_pro_tv);
        myFocusIv = (SimpleDraweeView) mHeaderView.findViewById(R.id.my_focus_iv);
        myFocusTv = (TextView) mHeaderView.findViewById(R.id.my_focus_tv);
        helpIv = (SimpleDraweeView) mHeaderView.findViewById(R.id.help_iv);
        helpTv = (TextView) mHeaderView.findViewById(R.id.help_tv);
        mListView.addHeaderView(mHeaderView);
        mSrfLayout.setOnRefreshListener(this);
//        mSrfLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light,
//                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        mSrfLayout.setRefreshing(true);
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

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        mSrfLayout.setRefreshing(false);
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
//            mMarketTv.setText(homeRes.home1320.get(0).adv_title);
//            FrescoHelper.loadImage(homeRes.home1320.get(0).adv_pic, mMarketSdv);
//            mCountryTv.setText(homeRes.home1320.get(1).adv_title);
//            FrescoHelper.loadImage(homeRes.home1320.get(1).adv_pic, mCountrySdv);
//            mAboutTv.setText(homeRes.home1320.get(2).adv_title);
//            FrescoHelper.loadImage(homeRes.home1320.get(2).adv_pic, mAboutSdv);
        }
        //listview设置
        mListAdapter = new HomeAdapter(getActivity());
        mListAdapter.reSetDataAndNotify(homeRes.home1330);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void gotoShop(String key) {
        // TODO: 2017/5/25
    }

    @Override
    public void onClick(View view) {

    }
}
