package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.LocationGoodsAdapter;
import com.basulvyou.system.adapter.LocationTodayHotAdapter;
import com.basulvyou.system.adapter.MyPopAdapter;
import com.basulvyou.system.api.LocationApi;
import com.basulvyou.system.api.ShopListApi;
import com.basulvyou.system.entity.ClassList;
import com.basulvyou.system.entity.LocationClassEntity;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.entity.NewsEntity;
import com.basulvyou.system.entity.NewsListEntity;
import com.basulvyou.system.entity.ShopEntityTeChan;
import com.basulvyou.system.entity.ShopListTeChan;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.basulvyou.system.util.TopClickUtil;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 当地分类列表页
 */
public class LocationActivity extends AbsLoadMoreActivity<ListView, LocationEntity> implements AdapterView.OnItemClickListener, View.OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private int index;
    private String locationType;
    private String mod_id;
    private String locationTypeId[] = {"20888", "20894", "20890", "loca", "20892", "20336", "21062"};
    private PopupWindow pop;
    private View tagPop, tagOtherView, top;
    private TextView defaultTag, priceUpTag, priceDownTag;
    private String order = null;
    private String key = null;
    private ListView popClass;
    private int x;
    private List<LocationClassEntity> classList;
    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        index = getIntent().getIntExtra("index", 0);
        locationType = getIntent().getStringExtra("type");
        mod_id = getIntent().getStringExtra("mod_id");
        initView();
        initListener();
        setAdapter();
        creatPop();
        loadData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.location_List);
        top = findViewById(R.id.top);
        if ("sleep".equals(locationType)) {
            setTitle("酒店");
            setTopRightTitle("排序", TopClickUtil.TOP_ORDERBY);
        } else if ("food".equals(locationType)) {
            setTitle("美食");
        } else if ("view".equals(locationType)) {
            setTitle("景点");
        } else if ("location".equals(locationType)) {
            canClick = false;
            setTitle("特产");
            setTopRightTitleAndImg("分类", TopClickUtil.TOP_CATE, R.mipmap.top_ss, TopClickUtil.TOP_SEA);
        } else if ("live".equals(locationType)) {
            setTitle("藏家乐");
        } else if ("group".equals(locationType)) {
            canClick = false;
            setTitle("团购");
        } else if ("culture".equals(locationType)) {
            if ("2020002000".equals(mod_id)) {
                setTitle("文化");
            } else {
                setTitle("资讯");
            }

        } else {
            setTitle("当地");
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        if (canClick) {
            mPullToRefreshListView.setOnItemClickListener(this);
        }
        top_right_title.setOnClickListener(this);
    }

    /**
     * 创建弹窗
     */
    private void creatPop() {
        if (top_right_title.getTag() == null) {
            return;
        }
        initPop();
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        if (((int) top_right_title.getTag()) == TopClickUtil.TOP_ORDERBY) {
            tagPop = this.getLayoutInflater().inflate(R.layout.pop_grogshop_select, null);
            defaultTag = (TextView) tagPop.findViewById(R.id.default_tag);
            priceUpTag = (TextView) tagPop.findViewById(R.id.price_up_tag);
            priceDownTag = (TextView) tagPop.findViewById(R.id.price_down_tag);
            tagOtherView = tagPop.findViewById(R.id.lin_other);
            initTagBackgroud();
            defaultTag.setTextColor(getResources().getColor(R.color.location_tag_select_1));
            pop.setContentView(tagPop);
            defaultTag.setOnClickListener(this);
            priceUpTag.setOnClickListener(this);
            priceDownTag.setOnClickListener(this);
            tagOtherView.setOnClickListener(this);
        } else if (((int) top_right_title.getTag()) == TopClickUtil.TOP_CATE) {
            tagPop = this.getLayoutInflater().inflate(R.layout.pop_techan_select, null);
            popClass = (ListView) tagPop.findViewById(R.id.lv_pop_class);
            tagOtherView = tagPop.findViewById(R.id.lin_other);
            classList = new ArrayList<>();
            pop.setContentView(tagPop);
            popClass.setOnItemClickListener(this);
            tagOtherView.setOnClickListener(this);
            loadDataClass();
        }
    }

    private void setAdapter() {
        if (index == 3) {
            mAdapter = new LocationGoodsAdapter(null, this);
            mPullToRefreshListView.setBackgroundColor(getResources().getColor(R.color.bg_main_color));
        } else {
            mAdapter = new LocationTodayHotAdapter(null, this);
            ((LocationTodayHotAdapter) mAdapter).setIfList(true);
        }
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        if (index == 3) {//特产
            httpGetRequest(ShopListApi.getShopListUrl("1", mPager.rows + "", mPager.getPage() + "", null, null, null, null, null), LocationApi.API_GET_LOCATION_LIST);
        } else if (index == 6) {//文化
            httpGetRequest(LocationApi.getNewsUrl(mPager.rows + "", mPager.getPage() + "", null, null, mod_id), LocationApi.API_GET_LOCATION_LIST);
        } else if (index == 5) {//团购
            ((LocationTodayHotAdapter) mAdapter).setIsGroup(true);
            httpGetRequest(ShopListApi.getGroupShopListUrl("1", mPager.rows + "", mPager.getPage() + "", null, null, null, null), LocationApi.API_GET_LOCATION_LIST);
        } else {//虚拟商品
            if (index == 0) {
                ((LocationTodayHotAdapter) mAdapter).setIsSleep(true);
            }
            httpGetRequest(LocationApi.getVirtualShopListUrl(key, mPager.rows + "", mPager.getPage() + ""
                    , locationTypeId[index], null, null, order, null, null, null), LocationApi.API_GET_LOCATION_LIST);
        }
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case LocationApi.API_GET_LOCATION_LIST:
                locationHander(json);
                break;
            case ShopListApi.API_GET_CLASS_LIST:
                ClassList classLists = JSON.parseObject(json, ClassList.class);
                if (classLists != null) {
                    classList = classLists.list;
                    MyPopAdapter adapter = new MyPopAdapter(this, classList);
                    popClass.setAdapter(adapter);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理列表信息
     *
     * @param json
     */
    private void locationHander(String json) {
        final long start = System.currentTimeMillis();
        List<LocationEntity> locationList = null;
        if (!StringUtil.isEmpty(json)) {
            locationList = getLocationEntities(json, locationList);
        }
        appendData(locationList, start);
    }

    private List<LocationEntity> getLocationEntities(String json, List<LocationEntity> locationList) {
        if (index == 3) {
            ShopListTeChan shop = JSON.parseObject(json, ShopListTeChan.class);
            if (shop != null && !ListUtils.isEmpty(shop.goods_list)) {
                List<ShopEntityTeChan> shopList = shop.goods_list;
                locationList = new ArrayList();
                for (int i = 0; i < shopList.size(); i++) {
                    ShopEntityTeChan shopEntity = shopList.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_price = "￥" + shopEntity.goods_price;
                    location.location_brief = shopEntity.goods_content;
                    location.location_title = shopEntity.goods_name;
                    location.location_id = shopEntity.goods_id;
                    location.location_img_one = shopEntity.goods_image_url;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    locationList.add(location);
                }
            }
        } else if (index == 6) {
            NewsListEntity newsList = JSON.parseObject(json, NewsListEntity.class);
            if (newsList != null && !ListUtils.isEmpty(newsList.goods_list)) {
                List<NewsEntity> news = newsList.goods_list;
                locationList = new ArrayList();
                for (int i = 0; i < news.size(); i++) {
                    NewsEntity newsEntity = news.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_brief = newsEntity.n_brief;
                    location.location_date = newsEntity.n_add_date;
                    location.location_id = newsEntity.n_id;
                    location.location_img_one = newsEntity.n_main_img;
                    location.location_title = newsEntity.n_title;
                    location.location_visit_count = newsEntity.n_visit_count;
                    location.location_url = newsEntity.n_url;
                    locationList.add(location);
                }
            }
        } else if (index == 5) {
            ShopListTeChan shop = JSON.parseObject(json, ShopListTeChan.class);
            if (shop != null && !ListUtils.isEmpty(shop.goods_list)) {
                List<ShopEntityTeChan> shopList = shop.goods_list;
                locationList = new ArrayList();
                for (int i = 0; i < shopList.size(); i++) {
                    ShopEntityTeChan shopEntity = shopList.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_price = "￥" + shopEntity.goods_price;
                    location.location_title = shopEntity.goods_name;
                    location.location_id = shopEntity.group_id;
                    location.location_img_one = shopEntity.groupbuy_image;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_group_price = "￥" + shopEntity.groupbuy_price;
                    location.location_group_rebate = shopEntity.groupbuy_rebate;
                    location.location_end_date = shopEntity.end_time_text;
                    locationList.add(location);
                }
            }
        } else {
            ShopListTeChan shopList = JSON.parseObject(json, ShopListTeChan.class);
            if (shopList != null && !ListUtils.isEmpty(shopList.goods_list)) {
                List<ShopEntityTeChan> shop = shopList.goods_list;
                locationList = new ArrayList();
                for (int i = 0; i < shop.size(); i++) {
                    ShopEntityTeChan shopEntity = shop.get(i);
                    LocationEntity location = new LocationEntity();
                    location.location_price = "￥" + shopEntity.goods_price + "起";
                    location.location_address = shopEntity.entp_addr;
                    location.location_id = shopEntity.goods_id;
                    location.location_img_one = shopEntity.goods_image_url1;
//                    location.location_img = shopEntity.goods_image_url;
                    location.location_title = shopEntity.goods_name;
                    location.location_visit_count = shopEntity.goods_salenum;
                    location.location_collect_count = shopEntity.sccomm;
                    location.location_evaluation_count = shopEntity.evaluation_count;
                    locationList.add(location);
                }
            }
        }
        return locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.lv_pop_class) {
            Intent intent = new Intent(this, LocationClassActivity.class);
            intent.putExtra("entity", classList.get(position));
            startActivity(intent);
            isShowTagPop();
        } else {
            if (mAdapter != null && mAdapter.getCount() > (position - 1)) {
                LocationEntity location = mAdapter.getItem(position - 1);
                if (location != null) {
                    Intent intent = null;
                    if (index == 3) {//普通商品详情
                        intent = new Intent(this, GoodsDetailActivity.class);
                    } else if (index == 6) {//文化详情
                        intent = new Intent(this, NewsDetailActivity.class);
                        intent.putExtra("url", location.location_url);
                    } else if (index == 5) {//团购商品详情
                        intent = new Intent(this, GroupGoodDetailActivity.class);
                    } else {//其他
                        intent = new Intent(this, LocationDetailActivity.class);
                        intent.putExtra("type", locationType);
                    }
                    if (!StringUtil.isEmpty(location.location_img_one)) {
                        intent.putExtra("image_url", location.location_img_one);
                    } else {
                        if (!ListUtils.isEmpty(location.location_img)) {
                            intent.putExtra("image_url", location.location_img.get(0).comm_image_url);
                        }
                    }
                    intent.putExtra("name", location.location_title);
                    intent.putExtra("goods_id", location.location_id);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_right_text:
                if (classList == null && ((int) v.getTag()) == TopClickUtil.TOP_CATE) {
                    loadDataClass();
                }
                isShowTagPop();
                break;
            case R.id.default_tag:
                setSelectedBg(defaultTag, "", "");
                break;
            case R.id.price_up_tag:
                setSelectedBg(priceUpTag, "3", "1");
                break;
            case R.id.price_down_tag:
                setSelectedBg(priceDownTag, "3", "2");
                break;
            case R.id.lin_other:
                isShowTagPop();
                break;
        }
    }

    /**
     * 初始化标签设置未选中情况下背景和字体颜色
     */
    private void initTagBackgroud() {
        defaultTag.setTextColor(getResources().getColor(R.color.location_tag_nomal_1));
        priceUpTag.setTextColor(getResources().getColor(R.color.location_tag_nomal_1));
        priceDownTag.setTextColor(getResources().getColor(R.color.location_tag_nomal_1));
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
            pop.showAsDropDown(top);
        }
    }
    /**
     * 设置选中的情况下背景和字体颜色
     * 和要按要求的关键key
     */
    private void setSelectedBg(TextView tagView, String key, String order) {
        initTagBackgroud();
        tagView.setTextColor(getResources().getColor(R.color.location_tag_select_1));
        if (!StringUtil.isEmpty(key)) {
            this.key = key;
        } else {
            this.key = null;
        }
        if (!StringUtil.isEmpty(order)) {
            this.order = order;
        } else {
            this.order = null;
        }
        clearData();
        loadData();
        isShowTagPop();
    }

    private void initPop() {
        pop = new PopupWindow(this);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
    }

    private void loadDataClass() {
        httpGetRequest(ShopListApi.getClassListUrl(), ShopListApi.API_GET_CLASS_LIST);
    }
}
