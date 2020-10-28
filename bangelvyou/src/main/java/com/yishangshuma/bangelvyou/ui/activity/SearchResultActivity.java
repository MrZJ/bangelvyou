package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.api.ShopListApi;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.entity.NewsEntity;
import com.yishangshuma.bangelvyou.entity.NewsListEntity;
import com.yishangshuma.bangelvyou.entity.ShopBundleEntity;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.ShopList;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.wibget.DashedLine;

import java.util.ArrayList;
import java.util.List;

/**
 * 检索结果页面
 */
public class SearchResultActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout locationLin, goodsLin, newsLin;
    private String keyWord;
    private View newsView,linearNews1,linearNews2,newsMore,locationView,linearLocation1,linearLocation2,locationMore,specialView,specialMore,linearSpecial1,linearSpecial2;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView newsImage1,newsImage2,locationImage1,locationImage2,specialImage1,specialImage2;
    private TextView newsName1,newsDes1,newsDate1,newsView1,newsSave1,newsCom1,newsName2,newsDes2,newsDate2,newsView2,newsSave2,newsCom2;
    private TextView locationName1,locationDes1,locationDate1,locationView1,locationSave1,locationCom1,locationName2,locationDes2,locationDate2,locationView2,locationSave2,locationCom2;
    private TextView specialName1,specialDate1,specialSave1,specialName2,specialDate2,specialSave2;
    private DashedLine locationDashLine,newsDashLine,specialDashLine;
    private String newsId1,newsId2,goodsRule1,goodsRule2,goodsId1,goodsId2;//商品规则
    private View noResult;//无搜索结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        keyWord = getIntent().getExtras().getString("keyWord");
        initView();
        initListener();
        loadData();
    }

    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("搜索结果");
        noResult = findViewById(R.id.rel_search_result_noresult);
        locationLin = (LinearLayout) findViewById(R.id.lin_search_result_location);
        goodsLin = (LinearLayout) findViewById(R.id.lin_search_result_goods);
        newsLin = (LinearLayout) findViewById(R.id.lin_search_result_news);
        //新闻列表
        newsView = getLayoutInflater().inflate(R.layout.item_search_result_news, null);
        newsMore = newsView.findViewById(R.id.result_news_more);
        linearNews1 = newsView.findViewById(R.id.lin_search_result_news1);
        newsImage1 = (ImageView) newsView.findViewById(R.id.result_news_img1);
        newsName1 = (TextView) newsView.findViewById(R.id.result_news_name1);
        newsDes1 = (TextView) newsView.findViewById(R.id.result_news_des1);
        newsDate1 = (TextView) newsView.findViewById(R.id.result_news_date1);
        newsView1 = (TextView) newsView.findViewById(R.id.result_news_view1);
//        newsSave1 = (TextView) newsView.findViewById(R.id.result_news_save1);
//        newsCom1 = (TextView) newsView.findViewById(R.id.result_news_com1);
        linearNews2 = newsView.findViewById(R.id.lin_search_result_news2);
        newsImage2 = (ImageView) newsView.findViewById(R.id.result_news_img2);
        newsName2 = (TextView) newsView.findViewById(R.id.result_news_name2);
        newsDes2 = (TextView) newsView.findViewById(R.id.result_news_des2);
        newsDate2 = (TextView) newsView.findViewById(R.id.result_news_date2);
        newsView2 = (TextView) newsView.findViewById(R.id.result_news_view2);
//        newsSave2 = (TextView) newsView.findViewById(R.id.result_news_save2);
//        newsCom2 = (TextView) newsView.findViewById(R.id.result_news_com2);
        newsDashLine = (DashedLine) newsView.findViewById(R.id.dashlin_search_result_news);

        //当地列表
        locationView = getLayoutInflater().inflate(R.layout.item_search_result_location, null);
        locationMore = locationView.findViewById(R.id.result_location_more);
        linearLocation1 = locationView.findViewById(R.id.lin_search_result_location1);
        locationImage1 = (ImageView) locationView.findViewById(R.id.result_location_img1);
        locationName1 = (TextView) locationView.findViewById(R.id.result_location_name1);
        locationDes1 = (TextView) locationView.findViewById(R.id.result_location_des1);
        locationDate1 = (TextView) locationView.findViewById(R.id.result_location_date1);
        locationView1 = (TextView) locationView.findViewById(R.id.result_location_view1);
//        locationSave1 = (TextView) locationView.findViewById(R.id.result_location_save1);
        locationCom1 = (TextView) locationView.findViewById(R.id.result_location_com1);
        linearLocation2 = locationView.findViewById(R.id.lin_search_result_location2);
        locationImage2 = (ImageView) locationView.findViewById(R.id.result_location_img2);
        locationName2 = (TextView) locationView.findViewById(R.id.result_location_name2);
        locationDes2 = (TextView) locationView.findViewById(R.id.result_location_des2);
        locationDate2 = (TextView) locationView.findViewById(R.id.result_location_date2);
        locationView2 = (TextView) locationView.findViewById(R.id.result_location_view2);
//        locationSave1 = (TextView) locationView.findViewById(R.id.result_location_save1);
        locationCom2 = (TextView) locationView.findViewById(R.id.result_location_com2);
        locationDashLine = (DashedLine) locationView.findViewById(R.id.dashline_search_result_location);

        //特产列表
        specialView = getLayoutInflater().inflate(R.layout.item_search_result_special, null);
        specialMore = specialView.findViewById(R.id.result_special_more);
        linearSpecial1 = specialView.findViewById(R.id.lin_search_result_special1);
        specialImage1 = (ImageView) specialView.findViewById(R.id.result_special_img1);
        specialName1 = (TextView) specialView.findViewById(R.id.result_special_name1);
        specialDate1 = (TextView) specialView.findViewById(R.id.result_special_date1);
        specialSave1 = (TextView) specialView.findViewById(R.id.result_special_save1);
        linearSpecial2 = specialView.findViewById(R.id.lin_search_result_special2);
        specialImage2 = (ImageView) specialView.findViewById(R.id.result_special_img2);
        specialName2 = (TextView) specialView.findViewById(R.id.result_special_name2);
        specialDate2 = (TextView) specialView.findViewById(R.id.result_special_date2);
        specialSave2 = (TextView) specialView.findViewById(R.id.result_special_save1);
        specialDashLine = (DashedLine) specialView.findViewById(R.id.dashline_search_result_special);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        newsMore.setOnClickListener(this);
        locationMore.setOnClickListener(this);
        specialMore.setOnClickListener(this);
        linearNews1.setOnClickListener(this);
        linearNews2.setOnClickListener(this);
        linearLocation1.setOnClickListener(this);
        linearLocation2.setOnClickListener(this);
        linearSpecial1.setOnClickListener(this);
        linearSpecial2.setOnClickListener(this);
    }

    /**
     * 显示新闻数据
     * @param newsList
     */
    private void setNewsData(List<LocationEntity> newsList){
            LocationEntity entity1 = newsList.get(0);
            imageLoader.displayImage(entity1.location_img, newsImage1, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            newsName1.setText(entity1.location_title);
            newsDes1.setText(entity1.location_brief);
            newsDate1.setText(entity1.location_date);
            newsView1.setText(entity1.location_visit_count);
            newsId1 = entity1.location_id;
            if(newsList.size() > 1){
                LocationEntity entity2 = newsList.get(1);
                imageLoader.displayImage(entity2.location_img, newsImage2, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
                newsName2.setText(entity2.location_title);
                newsDes2.setText(entity2.location_brief);
                newsDate2.setText(entity2.location_date);
                newsView2.setText(entity2.location_visit_count);
                newsId2 = entity2.location_id;
            }else{
                linearNews2.setVisibility(View.GONE);
                newsDashLine.setVisibility(View.GONE);
                newsMore.setVisibility(View.GONE);
            }
            newsLin.addView(newsView);
    }

    /**
     * 显示当地数据
     */
    private void setLocationData(List<LocationEntity> locationList){
            LocationEntity entity1 = locationList.get(0);
            imageLoader.displayImage(entity1.location_img, locationImage1, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            locationName1.setText(entity1.location_title);
            locationDes1.setText(entity1.location_brief);
            locationDate1.setText(entity1.location_date);
            locationView1.setText(entity1.location_visit_count);
            locationCom1.setText(entity1.location_evaluation_count);
            goodsRule1 = entity1.location_type;
            if(locationList.size() > 1){
                LocationEntity entity2 = locationList.get(1);
                imageLoader.displayImage(entity1.location_img, locationImage2, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
                locationName2.setText(entity1.location_title);
                locationDes2.setText(entity1.location_brief);
                locationDate2.setText(entity1.location_date);
                locationView2.setText(entity1.location_visit_count);
                locationCom2.setText(entity1.location_evaluation_count);
                goodsRule2 = entity2.location_type;
            }else{
                linearLocation2.setVisibility(View.GONE);
                locationDashLine.setVisibility(View.GONE);
                locationMore.setVisibility(View.GONE);
            }
            locationLin.addView(locationView);
    }

    /**
     * 显示特产商品
     */
    private void setSpecialData( List<ShopEntity> shopList){
        ShopEntity entity1 = shopList.get(0);
        imageLoader.displayImage(entity1.goods_image_url, specialImage1, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
        specialName1.setText(entity1.goods_name);
        specialDate1.setText("￥"+entity1.goods_price);
        specialSave1.setText(entity1.sccomm);
        goodsId1 = entity1.goods_id;
        if(shopList.size() > 1){
            ShopEntity entity2 = shopList.get(1);
            imageLoader.displayImage(entity2.goods_image_url, specialImage2, AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
            specialName2.setText(entity2.goods_name);
            specialDate2.setText("￥"+entity2.goods_price);
            specialSave2.setText(entity2.sccomm);
            goodsId2 = entity2.goods_id;
        }else{
            linearSpecial2.setVisibility(View.GONE);
            specialDashLine.setVisibility(View.GONE);
            specialMore.setVisibility(View.GONE);
        }
        goodsLin.addView(specialView);
    }

    private void loadData(){
        httpGetRequest(LocationApi.getNewsUrl("2", "0", keyWord, null,null), LocationApi.API_GET_NEWS_LIST);
        httpGetRequest(LocationApi.getVirtualShopListUrl("1", "2", "0", null, keyWord, null, "2", null, null, null), LocationApi.API_GET_LOCATION_LIST);
        httpGetRequest(ShopListApi.getShopListUrl("1", "2", "0", null, keyWord, null, null), ShopListApi.API_GET_SHOP_LIST);
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action)
        {
            case LocationApi.API_GET_NEWS_LIST:
                newsHander(json);
                break;
            case LocationApi.API_GET_LOCATION_LIST:
                locationHander(json);
                break;
            case ShopListApi.API_GET_SHOP_LIST:
                shopHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理新闻列表信息
     * @param json
     */
    private void newsHander(String json){
        List<LocationEntity> newsList = null;
        if(!"".equals(json)){
            NewsListEntity newslist = JSON.parseObject(json, NewsListEntity.class);
            if(newslist != null){
                List<NewsEntity> news = newslist.news;
                if(!ListUtils.isEmpty(news)){
                    newsList = new ArrayList<>();
                    for(int i = 0; i < news.size(); i++){
                        NewsEntity newsEntity = news.get(i);
                        LocationEntity location = new LocationEntity();
                        location.location_brief = newsEntity.n_brief;
                        location.location_date = newsEntity.n_add_date;
                        location.location_id = newsEntity.n_id;
                        location.location_img = newsEntity.n_main_img;
                        location.location_title = newsEntity.n_title;
                        location.location_visit_count = newsEntity.n_visit_count;
                        newsList.add(location);
                    }
                    setNewsData(newsList);
                    noResult.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 处理当地信息
     * @param json
     */
    private void locationHander(String json){
        List<LocationEntity> locationList = null;
        if(!"".equals(json)){
            ShopList shopList = JSON.parseObject(json, ShopList.class);
            if(shopList != null){
                List<ShopEntity> shop = shopList.goods_list;
                if(!ListUtils.isEmpty(shop)){
                    locationList = new ArrayList<>();
                    for(int i = 0; i < shop.size(); i++){
                        ShopEntity shopEntity = shop.get(i);
                        LocationEntity location = new LocationEntity();
                        location.location_brief = shopEntity.goods_content;
                        location.location_id = shopEntity.goods_id;
                        location.location_img = shopEntity.goods_image_url;
                        location.location_title = shopEntity.goods_name;
                        location.location_visit_count = shopEntity.goods_salenum;
                        location.location_evaluation_count = shopEntity.evaluation_count;
                        location.location_type = shopEntity.cls_type;
                        locationList.add(location);
                    }
                    setLocationData(locationList);
                    noResult.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 处理商品信息
     * @param json
     */
    private void shopHander(String json) {
        ShopList shop = JSON.parseObject(json, ShopList.class);
        if (shop != null) {
            List<ShopEntity> shopList = null;
            shopList = shop.goods_list;
            if(!ListUtils.isEmpty(shopList)){
                setSpecialData(shopList);
                noResult.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent loactionIntent = new Intent(this,LocationListActivity.class);
        Intent newsIntent = new Intent(this,NewsDetailActivity.class);
        Intent goodDetailIntent = new Intent(this,GoodsDetailActivity.class);
        ShopBundleEntity bundle = new ShopBundleEntity();
        bundle.keyword = keyWord;
        loactionIntent.putExtra("shopBundle",bundle);
        switch (v.getId()){
            case R.id.result_news_more://列表当地新闻
                loactionIntent.putExtra("type","news");
                startActivity(loactionIntent);
                break;
            case R.id.result_location_more://列表当地风景
                loactionIntent.putExtra("type","view");
                startActivity(loactionIntent);
                break;
            case R.id.result_special_more://列表特产
                Intent goodsIntent = new Intent(SearchResultActivity.this,SpecialActivity.class);
                goodsIntent.putExtra("keyword",keyWord);
                startActivity(goodsIntent);
                break;
            case R.id.lin_search_result_news1://新闻详情
                newsIntent.putExtra("id",newsId1);
                startActivity(newsIntent);
                break;
            case R.id.lin_search_result_news2://新闻详情
                newsIntent.putExtra("id",newsId2);
                startActivity(newsIntent);
                break;
            case R.id.lin_search_result_location1://当地商品详情
                divide(goodsRule1);
                break;
            case R.id.lin_search_result_location2://当地商品详情
                divide(goodsRule2);
                break;
            case R.id.lin_search_result_special1://特产商品详情
                goodDetailIntent.putExtra("goods_id",goodsId1);
                startActivity(goodDetailIntent);
                break;
            case R.id.lin_search_result_special2://特产商品详情
                goodDetailIntent.putExtra("goods_id",goodsId2);
                startActivity(goodDetailIntent);
                break;
        }
    }

    /**
     * 检查字串，按规则分离字串，goto不同界面
     * @param keyStr
     */
    private void divide(String keyStr){
        if(keyStr != null && !"".equals(keyStr)){
            String detailStr = keyStr.substring(0, 2);
            String goodsId = keyStr.substring(2);
            //详情规则
            Intent detailIntent = new Intent(this, LocationDetailActivity.class);
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
