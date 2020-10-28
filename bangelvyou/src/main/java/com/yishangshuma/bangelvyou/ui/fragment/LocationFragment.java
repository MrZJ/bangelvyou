package com.yishangshuma.bangelvyou.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.LocationTodayHotAdapter;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.entity.NewsEntity;
import com.yishangshuma.bangelvyou.entity.NewsListEntity;
import com.yishangshuma.bangelvyou.ui.activity.LocationListActivity;
import com.yishangshuma.bangelvyou.ui.activity.NewsDetailActivity;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地界面
 * Created by Administrator on 2016/1/25.
 */
public class LocationFragment extends AbsLoadMoreFragment<ListView, LocationEntity> implements AdapterView.OnItemClickListener,View.OnClickListener{

    private View mView, advView;
    private PullToRefreshListView mPullToRefreshListView;
    private ImageView[] locationAdvImg = new ImageView[7];
    private TextView[] locationAdvTitle = new TextView[6];
    private TextView[] locationAdvContext = new TextView[6];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location, container, false);
        initView(inflater);
        initListener();
        setAdapter();
        showLoading(getResources().getString(R.string.load_text), true);
        loadData();
        return mView;
    }

    private void initView(LayoutInflater inflater){
        initTopView(mView);
        setLogoShow(R.mipmap.home_logo, TopClickUtil.TOP_IMG);
        setSearchSeat();

        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.location_list);
        advView = inflater.inflate(R.layout.location_list_header, null);

        locationAdvImg[0] = (ImageView) advView.findViewById(R.id.location_food_img);
        locationAdvTitle[0] = (TextView) advView.findViewById(R.id.location_food_title);
        locationAdvContext[0] = (TextView) advView.findViewById(R.id.location_food_context);

        locationAdvImg[1] = (ImageView) advView.findViewById(R.id.location_sleep_img);
        locationAdvTitle[1] = (TextView) advView.findViewById(R.id.location_sleep_title);
        locationAdvContext[1] = (TextView) advView.findViewById(R.id.location_sleep_context);

        locationAdvImg[2] = (ImageView) advView.findViewById(R.id.location_news_img);
        locationAdvTitle[2] = (TextView) advView.findViewById(R.id.location_news_title);
        locationAdvContext[2] = (TextView) advView.findViewById(R.id.location_news_context);

        locationAdvImg[3] = (ImageView) advView.findViewById(R.id.location_view_img);
        locationAdvTitle[3] = (TextView) advView.findViewById(R.id.location_view_title);
        locationAdvContext[3] = (TextView) advView.findViewById(R.id.location_view_context);

        locationAdvImg[4] = (ImageView) advView.findViewById(R.id.location_live_img);
        locationAdvTitle[4] = (TextView) advView.findViewById(R.id.location_live_title);
        locationAdvContext[4] = (TextView) advView.findViewById(R.id.location_live_context);

        locationAdvImg[5] = (ImageView) advView.findViewById(R.id.location_fwz_img);
        locationAdvTitle[5] = (TextView) advView.findViewById(R.id.location_fwz_title);
        locationAdvContext[5] = (TextView) advView.findViewById(R.id.location_fwz_context);

        locationAdvImg[6] = (ImageView) advView.findViewById(R.id.location_dh_img);

        mPullToRefreshListView.getRefreshableView().addHeaderView(advView);
    }

    private void setAdapter(){
        mAdapter = new LocationTodayHotAdapter(null, getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    private void initListener(){
        initSearchListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
        for (int i = 0; i < locationAdvImg.length; i++) {
            locationAdvImg[i].setOnClickListener(this);
        }
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(LocationApi.getNewsUrl(mPager.rows + "",
                mPager.getPage() + "", null, null,null), LocationApi.API_GET_LOCATION_LIST);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case LocationApi.API_GET_LOCATION_LIST:
                newsHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     * @param json
     */
    private void newsHander(String json){
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if(!"".equals(json)){
            NewsListEntity newsList = JSON.parseObject(json, NewsListEntity.class);
            if(newsList != null){
                List<NewsEntity> news = newsList.news;
                locationList = new ArrayList<>();
                for(int i = 0; i < news.size(); i++){
                    NewsEntity newsEntity = news.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = newsEntity.n_brief;
                    location.location_date = newsEntity.n_add_date;
                    location.location_id = newsEntity.n_id;
                    location.location_img = newsEntity.n_main_img;
                    location.location_title = newsEntity.n_title;
                    location.location_visit_count = newsEntity.n_visit_count;
                    locationList.add(location);
                }
                appendData(locationList, start);
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
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("image_url", location.location_img);
                intent.putExtra("name", location.location_title);
                intent.putExtra("id", location.location_id);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (getActivity() == null)
            return;
        Intent detailIntent = new Intent(getActivity(), LocationListActivity.class);
        switch (v.getId()){
            case R.id.location_food_img:
                detailIntent.putExtra("type", "food");
                break;
            case R.id.location_sleep_img:
                detailIntent.putExtra("type", "sleep");
                break;
            case R.id.location_news_img:
                detailIntent.putExtra("type","news");
                break;
            case R.id.location_view_img://景点
                detailIntent.putExtra("type","view");
                break;
            case R.id.location_live_img://体验
                detailIntent.putExtra("type","live");
                break;
            case R.id.location_fwz_img://非物质
                detailIntent.putExtra("type","fwz");
                break;
            case R.id.location_dh_img://导航
                Uri uri = Uri.parse("geo:90.017283,31.398283" +"," + "班戈");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                Intent mapIntent1 = Intent.createChooser(mapIntent, "请选择打开方式");
                startActivity(mapIntent1);
                return;
        }
        startActivity(detailIntent);
    }
}
