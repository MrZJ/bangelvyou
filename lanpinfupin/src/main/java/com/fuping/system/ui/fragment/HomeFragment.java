package com.fuping.system.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fuping.system.R;
import com.fuping.system.adapter.HomeTabAdapter;
import com.fuping.system.adapter.NoticeAdapter;
import com.fuping.system.api.HomeApi;
import com.fuping.system.api.NoticeApi;
import com.fuping.system.entity.HomeAdvEntity;
import com.fuping.system.entity.HomeList;
import com.fuping.system.entity.MyIntentEntity;
import com.fuping.system.entity.NoticeEntity;
import com.fuping.system.entity.NoticeList;
import com.fuping.system.ui.activity.LoginActivity;
import com.fuping.system.ui.activity.NoticeActivity;
import com.fuping.system.utils.AsynImageUtil;
import com.fuping.system.utils.ConfigUtil;
import com.fuping.system.utils.Constant;
import com.fuping.system.utils.StringUtil;
import com.fuping.system.utils.TopClickUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页界面
 */
public class HomeFragment extends AbsLoadMoreFragment<ListView, NoticeEntity> implements
        View.OnClickListener, AdapterView.OnItemClickListener, XBanner.OnItemClickListener {

    private View mView, topView;
    private PullToRefreshListView mPullToRefreshListView;
    private GridView gridview;
    private HomeTabAdapter homeTabAdapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private XBanner homeBanner;
    private List<HomeAdvEntity> homeAdvList;//轮播图片集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(inflater);
        setAdapter();
        initListener();
        getHome();
        loadData();
        registerReceiver();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void initView(LayoutInflater inflater) {
        initTopView(mView);
        setTitle("首页");
        hideBackBtn();
        setTopRightImg(R.mipmap.top_ss, TopClickUtil.TOP_NOTC_SEA);
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.home_list);
        topView = inflater.inflate(R.layout.home_list_header, null);
        gridview = (GridView) topView.findViewById(R.id.gridview);
        homeTabAdapter = new HomeTabAdapter(getActivity(), Constant.getPermissionIntetnt(getActivity(),
                configEntity.isLogin, configEntity.usertype));
        gridview.setAdapter(homeTabAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    MyIntentEntity entity = homeTabAdapter.getmData().get(position);
                    getActivity().startActivity(entity.mIntent);
                } catch (Exception e) {

                }
            }
        });
        homeBanner = (XBanner) topView.findViewById(R.id.home_banner);
        mPullToRefreshListView.getRefreshableView().addHeaderView(topView);
    }

    private void setAdapter() {
        mAdapter = new NoticeAdapter(null, getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    public void initListener() {
        initTopListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    /**
     * 获取轮播图数据信息
     */
    private void getHome() {
        httpGetRequest(HomeApi.getHomeUrl(), HomeApi.API_GET_HOME);
    }


    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case HomeApi.API_GET_HOME:
                homeHander(json);
                break;
            case NoticeApi.API_NOTICE_LIST:
                noticeHander(json);
                break;
        }
    }

    /**
     * 处理首页信息
     *
     * @param json
     */
    private void homeHander(String json) {
        if (!StringUtil.isEmpty(json)) {
            HomeList homeList = JSON.parseObject(json, HomeList.class);
            if (null != homeList) {
                homeAdvList = homeList.list;
                List<String> tipList = new ArrayList<>();
                for (int i = 0; i < homeAdvList.size(); i++) {
                    tipList.add(homeAdvList.get(i).title);
                }
                homeBanner.setData(homeAdvList, tipList);
                homeBanner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, View view, int position) {
                        imageLoader.displayImage(homeAdvList.get(position).img_path, (ImageView) view,
                                AsynImageUtil.getImageOptions(), null);
                    }
                });
                homeBanner.setOnItemClickListener(this);
            }
        }

    }

    /**
     * 获取资讯数据
     */
    @Override
    protected void loadData() {
        httpGetRequest(NoticeApi.getNoticeList(mPager.getPage() + "", null, null), NoticeApi.API_NOTICE_LIST);
    }


    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    public void onClick(View v) {
//        if (!configEntity.isLogin) {
//            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            return;
//        }
//        Intent intent = new Intent(getActivity(), ModuleDetailActivity.class);
//        switch (v.getId()) {
//            case home_item_1:
//                intent.putExtra("moduleType", "helpObj");
//                startActivity(intent);
//                break;
//            case home_item_2:
//                if (Constant.getPlanManagePermission(configEntity.usertype)) {
//                    intent.putExtra("moduleType", "planMag");
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), "您没有管理权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case home_item_3:
//                intent.putExtra("moduleType", "logMag");
//                startActivity(intent);
//                break;
//            case home_item_4:
//                intent.putExtra("moduleType", "mail");
//                startActivity(intent);
//                break;
//            case home_item_5:
//                if (Constant.getBangfuPermission(configEntity.usertype)) {
//                    HelpActivity.startActivity(getActivity());
//                } else {
//                    Toast.makeText(getActivity(), "您没有管理权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case home_item_6:
//                if (Constant.getDuChaPermission(configEntity.usertype)) {
//                    DuChaActivity.startActivity(getActivity());
//                } else {
//                    Toast.makeText(getActivity(), "您没有管理权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case home_item_7:
//                if (Constant.getDuChaPermission(configEntity.usertype)) {
//                    InspectionSelectActivity.startActivity(getActivity());
//                } else {
//                    Toast.makeText(getActivity(), "您没有管理权限", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
    }

    /**
     * 处理公告信息
     *
     * @param json
     */
    private void noticeHander(String json) {
        final long start = System.currentTimeMillis();
        if (!StringUtil.isEmpty(json)) {
            NoticeList noticeList = JSON.parseObject(json, NoticeList.class);
            if (null != noticeList) {
                List<NoticeEntity> infos = noticeList.list;
                appendData(infos, start);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (0 == (position - 1)) {
            return;
        }
        if (mAdapter != null && mAdapter.getCount() > (position - 2)) {
            NoticeEntity entity = mAdapter.getItem(position - 2);
            Intent detailIntent = new Intent(getActivity(), NoticeActivity.class);
            detailIntent.putExtra("title", entity.title);
            detailIntent.putExtra("uid", entity.uuid);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onItemClick(XBanner banner, int position) {
        HomeAdvEntity entity = homeAdvList.get(position);
        Intent detailIntent = new Intent(getActivity(), NoticeActivity.class);
        detailIntent.putExtra("title", entity.title);
        detailIntent.putExtra("link_url", entity.link_url);
        startActivity(detailIntent);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LoginActivity.LOGIN_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private void unRegisterReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LoginActivity.LOGIN_ACTION)) {
                configEntity = ConfigUtil.loadConfig(getActivity());
                homeTabAdapter = new HomeTabAdapter(context,
                        Constant.getPermissionIntetnt(context, configEntity.isLogin, configEntity.usertype));
                gridview.setAdapter(homeTabAdapter);
                mPullToRefreshListView.setRefreshing();
            }
        }
    };
}
