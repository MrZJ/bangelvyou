package com.shayangfupin.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shayangfupin.system.R;
import com.shayangfupin.system.adapter.NoticeAdapter;
import com.shayangfupin.system.api.HomeApi;
import com.shayangfupin.system.api.NoticeApi;
import com.shayangfupin.system.entity.HomeAdvEntity;
import com.shayangfupin.system.entity.HomeList;
import com.shayangfupin.system.entity.NoticeEntity;
import com.shayangfupin.system.entity.NoticeList;
import com.shayangfupin.system.ui.activity.LoginActivity;
import com.shayangfupin.system.ui.activity.ModuleDetailActivity;
import com.shayangfupin.system.ui.activity.NoticeActivity;
import com.shayangfupin.system.utlis.AsynImageUtil;
import com.shayangfupin.system.utlis.ConfigUtil;
import com.shayangfupin.system.utlis.StringUtil;
import com.shayangfupin.system.utlis.TopClickUtil;
import com.stx.xhb.xbanner.XBanner;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面
 */
public class HomeFragment extends AbsLoadMoreFragment<ListView, NoticeEntity> implements
        View.OnClickListener, AdapterView.OnItemClickListener ,XBanner.OnItemClickListener{

    private View mView, topView;
    private PullToRefreshListView mPullToRefreshListView;
    private View lin_home_btn, lin_home_line,home_item_1, home_item_2, home_item_3, home_item_4;
    private TextView[] homeButtonTextView = new TextView[4];
    private ImageView[] homeButtonImageView = new ImageView[4];
    private int[] homeBtnImageViewId = {R.mipmap.helping_object, R.mipmap.planning_mag, R.mipmap.log_mag, R.mipmap.mail};
    private String homeButtonText[] = {"帮扶对象", "计划管理", "日志管理", "通讯录"};
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
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPerson();
    }

    public void checkPerson(){
        if (null != getActivity()) {
            configEntity = ConfigUtil.loadConfig(getActivity());
            if(configEntity.usertype.equals("2")){
                lin_home_btn.setVisibility(View.GONE);
                lin_home_line.setVisibility(View.GONE);
            }else{
                lin_home_btn.setVisibility(View.VISIBLE);
                lin_home_line.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView(LayoutInflater inflater) {
        initTopView(mView);
        setTitle("首页");
        hideBackBtn();
        setTopRightImg(R.mipmap.top_ss, TopClickUtil.TOP_NOTC_SEA);
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.home_list);
        topView = inflater.inflate(R.layout.home_list_header, null);
        homeBanner = (XBanner) topView.findViewById(R.id.home_banner);
        mPullToRefreshListView.getRefreshableView().addHeaderView(topView);

        lin_home_btn = mView.findViewById(R.id.lin_home_btn);
        lin_home_line = mView.findViewById(R.id.lin_home_line);

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

        setHomeBtnImageLayout();
    }


    /**
     * 设置home2布局位4个图片按钮的布局
     */
    private void setHomeBtnImageLayout() {
        //设置置顶广告图片下图片按钮
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(60), DensityUtil.dip2px(60));
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //设置置顶广告图片下文字按钮
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        for (int i = 0; i < 4; i++) {
            homeButtonImageView[i].setLayoutParams(imageParams);
            homeButtonTextView[i].setLayoutParams(textParams);
            homeButtonTextView[i].setGravity(Gravity.CENTER_HORIZONTAL);
            homeButtonTextView[i].setText(homeButtonText[i]);
            homeButtonImageView[i].setBackgroundResource(homeBtnImageViewId[i]);
        }
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
        home_item_1.setOnClickListener(this);
        home_item_2.setOnClickListener(this);
        home_item_3.setOnClickListener(this);
        home_item_4.setOnClickListener(this);
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
                homeBanner.setData(homeAdvList,tipList);
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
        if(!configEntity.isLogin){
            Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        Intent intent = new Intent(getActivity(), ModuleDetailActivity.class);
        switch (v.getId()) {
            case R.id.home_item_1:
                intent.putExtra("moduleType","helpObj");
                startActivity(intent);
                break;
            case R.id.home_item_2:
                if(configEntity.usertype.equals("1")){
                    Toast.makeText(getActivity(),"您没有管理权限",Toast.LENGTH_SHORT).show();
                } else {
                    if(configEntity.usertype.equals("3")){
                        intent.putExtra("moduleType","planMag3");
                    } else {
                        intent.putExtra("moduleType","planMag");
                    }
                    startActivity(intent);
                }
                break;
            case R.id.home_item_3:
                intent.putExtra("moduleType","logMag");
                startActivity(intent);
                break;
            case R.id.home_item_4:
                intent.putExtra("moduleType","mail");
                startActivity(intent);
                break;
        }
    }

    /**
     * 处理公告信息
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
}
