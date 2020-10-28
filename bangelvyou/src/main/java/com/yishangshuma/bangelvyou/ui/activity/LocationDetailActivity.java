package com.yishangshuma.bangelvyou.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.adapter.NearbyAttractionAdapter;
import com.yishangshuma.bangelvyou.adapter.TopOneAdapter;
import com.yishangshuma.bangelvyou.api.FocusApi;
import com.yishangshuma.bangelvyou.api.LocationApi;
import com.yishangshuma.bangelvyou.entity.CommentInfoEntity;
import com.yishangshuma.bangelvyou.entity.GoodsDetailImgEntity;
import com.yishangshuma.bangelvyou.entity.ShopDetailEntity;
import com.yishangshuma.bangelvyou.entity.ShopDetailInfoEntity;
import com.yishangshuma.bangelvyou.entity.ShopEntity;
import com.yishangshuma.bangelvyou.entity.TopInfo;
import com.yishangshuma.bangelvyou.listener.PageChangeListener;
import com.yishangshuma.bangelvyou.listener.ShareClickListener;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.DensityUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;
import com.yishangshuma.bangelvyou.util.StringUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;
import com.yishangshuma.bangelvyou.wibget.AutoScrollViewPager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 当地酒店、景点、美食体验详情页面
 */
public class LocationDetailActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,ViewPager.OnPageChangeListener{

    private TextView guideDes,tvDetailName,timeTitle,timeContent,addressTitle,addressContent
                    ,trafficContent,enrollTitle,commentTitle, tv_location_detail_name, tv_location_detail_des, tv_location_detail_dis, tv_nav;
    private ImageView lookMoreImg,timeLookMore,addressLookMore,trafficLookMore,img_map,imgGoodPic;
    private boolean isLookMore = true, isTimeLookMore = true, isAddLookMore = true, isTraLookMore = true;
    private LinearLayout commentList;
    private GridView nearbyAttraGrid;
    private HashMap<Integer,Boolean> isShowCommentAll;
    private String detailType;//判断详情类别
    private View relMap,lookMore, linTime,writeComment ,rl_nav,relAddress ,relTraffic, nearbyMore;//攻略时间栏
    private Button enrollButton,lookMoreComment;
    private String id;
    private ShopDetailInfoEntity detail;//商品详情实体
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private double mLatitude;
    private double mLongitude;
    private LocationClient mLocationClient;
    private String[] destinationLatLon;
    private ShareClickListener shareClickListener;
    private AutoScrollViewPager pager;
    private TopOneAdapter mAdAdapter;
    private PageChangeListener pageChangeListener;
    private LinearLayout indicatorLayout;//图片位置指示
    private ImageView[] indicators = null;
    private List<TopInfo> topInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        detailType = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(this);
        if(detail != null){
            detail = null;
            commentList.removeAllViews();
        }
        if(mAdAdapter != null){
            indicators = null;
            indicatorLayout.removeAllViews();
            mAdAdapter = null;
            pager.stopAutoScroll();
        }
        showLoading(getResources().getString(R.string.load_text), true);
        loadData();
    }

    private  void initView() {
        initTopView();
        setLeftBackButton();
        setTopRightImg(R.mipmap.top_collect, TopClickUtil.TOP_COL, R.mipmap.top_share);
        pager = (AutoScrollViewPager) findViewById(R.id.viewpager_location_detail_pager);
        indicatorLayout = (LinearLayout) findViewById(R.id.line_location_detail_indicator);
//        imgGoodPic = (ImageView) findViewById(R.id.img_location_detail_goodspic);
        tv_location_detail_name = (TextView) findViewById(R.id.tv_location_detail_name);
        tv_location_detail_dis = (TextView) findViewById(R.id.tv_location_detail_dis);
        tv_location_detail_des = (TextView) findViewById(R.id.tv_location_detail_des);
        guideDes = (TextView) findViewById(R.id.tv_location_guide_des);
        lookMore = findViewById(R.id.rel_location_lookmore_guide_des);
        lookMoreImg = (ImageView) findViewById(R.id.img_location_lookmore_guide_des);
        tvDetailName = (TextView) findViewById(R.id.tv_location_detail_guide_type);
        linTime = findViewById(R.id.lin_guide_time);
        timeTitle = (TextView) findViewById(R.id.tv_guide_time_title);
        timeContent = (TextView) findViewById(R.id.tv_guide_time);
        timeLookMore = (ImageView) findViewById(R.id.img_guide_time_more);
        relAddress = findViewById(R.id.rel_guide_address);
        addressTitle = (TextView) findViewById(R.id.tv_guide_address_title);
        addressContent = (TextView) findViewById(R.id.tv_guide_address);
        addressLookMore  = (ImageView) findViewById(R.id.img_guide_address_more);
        relTraffic = findViewById(R.id.rel_guide_traffic);
        trafficContent = (TextView) findViewById(R.id.tv_guide_traffic);
        trafficLookMore = (ImageView) findViewById(R.id.img_guide_traffic_more);
//        enrollTitle = (TextView) findViewById(R.id.tv_guide_enroll_title);
//        enrollButton = (Button) findViewById(R.id.btn_guide_enroll);
        commentTitle = (TextView) findViewById(R.id.tv_attraction_comment);
        commentList = (LinearLayout) findViewById(R.id.list_attraction_comment);
        lookMoreComment = (Button) findViewById(R.id.btn_attraction_lookmorec);
        nearbyMore = findViewById(R.id.rel_location_detail_nearby);
        nearbyAttraGrid = (GridView) findViewById(R.id.gridview_attraction_nearby);
        nearbyAttraGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        relMap = findViewById(R.id.rel_location_detail_map);//地图布局
        img_map = (ImageView) findViewById(R.id.img_map);
        rl_nav = (View) findViewById(R.id.rl_nav);
        tv_nav = (TextView) findViewById(R.id.tv_nav);//导航提示
        writeComment = findViewById(R.id.lin_comment);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;
        if("view".equals(detailType)){
            setTitle("景点");
            tvDetailName.setText("景点攻略");
            timeTitle.setLayoutParams(params);
            timeTitle.setText("开放时间");
//            enrollButton.setText("点击购买");
        }else if("sleep".equals(detailType)){
            setTitle("酒店");
            tvDetailName.setText("酒店攻略");
            linTime.setVisibility(View.GONE);
            addressTitle.setText("地点");
        }else if("food".equals(detailType)){
            setTitle("美食");
            tvDetailName.setText("美食攻略");
            timeTitle.setText("开放时间");
            timeTitle.setLayoutParams(params);
//            enrollButton.setText("点击购买");
        }else if("live".equals(detailType)){
            setTitle("体验");
            tvDetailName.setText("体验攻略");
        }else{
            setTitle("当地详情");
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        pager.setOnPageChangeListener(this);
        lookMore.setOnClickListener(this);
        linTime.setOnClickListener(this);
        relAddress.setOnClickListener(this);
        relTraffic.setOnClickListener(this);
        img_map.setOnClickListener(this);
//        enrollButton.setOnClickListener(this);
        lookMoreComment.setOnClickListener(this);
        rl_nav.setOnClickListener(this);
        writeComment.setOnClickListener(this);
        nearbyMore.setOnClickListener(this);
        nearbyAttraGrid.setOnItemClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void setData(){
        if(detail == null){
            return;
        }
        shareClickListener = new ShareClickListener(this);
        setShareNameAndImg();
        rightTwo.setOnClickListener(shareClickListener);
        if(null != detail.entpInfo.entp_latlng && !"".equals(detail.entpInfo.entp_latlng)){
            String destinationString = detail.entpInfo.entp_latlng;
            destinationLatLon = destinationString.split(",");
            imageLoader.displayImage(ConfigUtil.HTTP_URL_BAIDU_MAP + "&width=900&height="
                            + DensityUtil.dip2px(this, 100) +
                            "&center="+destinationString+"&zoom=17&scale=1&copyright=1", img_map,
                    AsynImageUtil.getImageOptions(), null);
            initLocation();//查询地理位置距离
        }else{
            relMap.setVisibility(View.GONE);
        }
        List<GoodsDetailImgEntity> imgEntity = detail.comm_image_url;
        if (!ListUtils.isEmpty(imgEntity)) {
//            imageUrl = imgEntity.get(0).comm_image_url;
            topInfos = new ArrayList<TopInfo>();
            for (int i = 0; i < imgEntity.size(); i++) {
                TopInfo topInfo = new TopInfo();
                topInfo.pciture = imgEntity.get(i).comm_image_url;
                topInfos.add(topInfo);
            }
            indicators = new ImageView[topInfos.size()]; // 定义指示器数组大小
            initIndicator();
            mAdAdapter = new TopOneAdapter(this, "");
            mAdAdapter.setData(topInfos);
            mAdAdapter.setInfiniteLoop(true);
            pager.setAdapter(mAdAdapter);
            pager.setAutoScrollDurationFactor(10);
            pager.setInterval(2000);
            pager.startAutoScroll();
            if(topInfos !=null){
                pager.setCurrentItem(topInfos.size());
            }
        }
//        imageLoader.displayImage(detail.comm_image_url, imgGoodPic, AsynImageUtil.getImageOptions(), null);
        tv_location_detail_name.setText(detail.comm_name);
        if(null != detail.goods_content && !"".equals(detail.goods_content)){
            tv_location_detail_des.setText(Html.fromHtml(detail.goods_content));
        }
        guideDes.setText(Html.fromHtml(detail.goods_glcontent));
        timeContent.setText(detail.entpInfo.entp_shop_hours);
        if(null != detail.entpInfo.entp_addr && !"".equals(detail.entpInfo.entp_addr)){
            addressContent.setText(Html.fromHtml(detail.entpInfo.entp_addr));
        }
        if(null != detail.goods_jtcontent && !"".equals(detail.goods_jtcontent)){
            trafficContent.setText(Html.fromHtml(detail.goods_jtcontent));
        }
        String commentNum = "来自"+detail.commentCount+"位游客的点评";
        int Nstart = commentNum.indexOf("自")+1;
        int Nend = commentNum.indexOf("位");
        SpannableStringBuilder style = new SpannableStringBuilder(commentNum);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#F81200")), Nstart, Nend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        commentTitle.setText(style);
        if(null == detail.commentCount || Integer.parseInt(detail.commentCount) < 9){
            lookMoreComment.setVisibility(View.GONE);
        }else{
            String lookMore = "查看全部"+detail.commentCount+"位用户评价";
            lookMoreComment.setText(lookMore);
        }
        if(!ListUtils.isEmpty(detail.commentInfoList)){
            addCommentToView();
        }

        List<ShopEntity> nearbyList = detail.fjcommList;
        if(!ListUtils.isEmpty(nearbyList)){
            NearbyAttractionAdapter adapter = new NearbyAttractionAdapter(nearbyList,this);
            nearbyAttraGrid.setAdapter(adapter);
        }
    }

    private void loadData() {
        httpGetRequest(LocationApi.getVirtualShopDetailUrl(id, ConfigUtil.phoneIMEI), LocationApi.API_GET_LOCATION_DETAIL);
    }

    /**
     * 设置分享文字和图片
     */
    private void setShareNameAndImg(){
        if(shareClickListener != null){
            if(null != detail){
                shareClickListener.setShareImgUrl(detail.comm_image_url.get(0).comm_image_url);
                shareClickListener.setContent(detail.comm_name);
            }
        }
    }

    /**
     * 初始化指示器图片
     */
    private void initIndicator(){
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            if(i == 0){
                indicators[i].setBackgroundResource(R.mipmap.indicators_right_now);
            } else {
                indicators[i].setBackgroundResource(R.mipmap.indicators_default);
            }
            indicatorLayout.addView(indicators[i]);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicators[i].getLayoutParams();
            if(i!= 0){
                layoutParams.leftMargin = 20;
            }
            layoutParams.bottomMargin = 5;
            layoutParams.height = 18;
            layoutParams.width = 14;
            indicators[i].setLayoutParams(layoutParams);
        }
    }

    /**
     * 显示评论
     */
    private void addCommentToView(){
        isShowCommentAll = new HashMap<Integer,Boolean>();
        for(int i = 0; i < detail.commentInfoList.size(); i++){
            CommentInfoEntity commentInfoEntity = detail.commentInfoList.get(i);
            isShowCommentAll.put(i,true);
            View commentItem = LayoutInflater.from(this).inflate(R.layout.item_tourist_comment, null);
            View commentShowAll = commentItem.findViewById(R.id.item_comment_tourist_more);
            final TextView commentContent = (TextView) commentItem.findViewById(R.id.item_comment_tourist_content);
            final TextView tvCommentLookMore = (TextView) commentItem.findViewById(R.id.item_comment_tourist_more_tv);
            final ImageView imgCommentLookMore = (ImageView) commentItem.findViewById(R.id.item_comment_tourist_more_img);
            TextView item_comment_tourist_name = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_name);
            TextView item_comment_tourist_level = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_level);
            TextView item_comment_tourist_date = (TextView) commentItem
                    .findViewById(R.id.item_comment_tourist_date);
            RatingBar item_comment_tourist_rating = (RatingBar) commentItem
                    .findViewById(R.id.item_comment_tourist_rating);
            ImageView item_comment_tourist_icon = (ImageView) commentItem.findViewById(R.id.item_comment_tourist_icon);
            final int finalI = i;
            commentShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentShowOrHideText(finalI,commentContent,tvCommentLookMore,imgCommentLookMore);
                }
            });
            commentContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentShowOrHideText(finalI,commentContent,tvCommentLookMore,imgCommentLookMore);
                }
            });
            imageLoader.displayImage(commentInfoEntity.comment_user_icon, item_comment_tourist_icon,
                    AsynImageUtil.getImageOptions(R.mipmap.tourist_icon), null);
            commentContent.setText(commentInfoEntity.comment_content);
            item_comment_tourist_name.setText(commentInfoEntity.comment_user_name);
            item_comment_tourist_level.setText("Lv"+commentInfoEntity.comment_user_level);
            item_comment_tourist_date.setText(commentInfoEntity.comment_date);
            item_comment_tourist_rating.setRating(StringUtil.stringToInt(commentInfoEntity.comment_level));
            commentList.addView(commentItem);
        }
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case LocationApi.API_GET_LOCATION_DETAIL:
                locationHander(json);
                break;
            case FocusApi.API_GET_ADD_FOCUS://收藏
                addCollectHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理商品详情信息
     * @param json
     */
    private void locationHander(String json){
        if(!StringUtil.isEmpty(json)){
            ShopDetailEntity shopDetailEntity = JSON.parseObject(json, ShopDetailEntity.class);
            if (null != shopDetailEntity) {
                if (null != shopDetailEntity.comm_info) {
                    detail = shopDetailEntity.comm_info;
                    setData();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_location_lookmore_guide_des://隐藏/显示描述
                if (isLookMore){
                    isLookMore = false;
                    lookMoreImg.setBackgroundResource(R.mipmap.look_more_up);
                    guideDes.setEllipsize(null);
                    guideDes.setSingleLine(false);
                }else{
                    isLookMore = true;
                    lookMoreImg.setBackgroundResource(R.mipmap.look_more);
                    guideDes.setEllipsize(TextUtils.TruncateAt.END);
                    guideDes.setMaxLines(5);
                }
                break;
            case R.id.lin_guide_time://隐藏/显示时间
                if(isTimeLookMore){
                    isTimeLookMore = false;
                }else{
                    isTimeLookMore = true;
                }
                showOrHideText(isTimeLookMore,timeContent,timeLookMore);
                break;
            case R.id.rel_guide_address://隐藏/显示地址
                if(isAddLookMore){
                    isAddLookMore = false;
                }else{
                    isAddLookMore = true;
                }
                showOrHideText(isAddLookMore,addressContent,addressLookMore);
                break;
            case R.id.rel_guide_traffic://隐藏/显示交通
                if(isTraLookMore){
                    isTraLookMore = false;
                }else{
                    isTraLookMore = true;
                }
                showOrHideText(isTraLookMore,trafficContent,trafficLookMore);
                break;
            case R.id.btn_guide_enroll://预定
                if(!configEntity.isLogin){
                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(this,LoginActivity.class);
                    startActivity(loginIntent);
                    return;
                }
                if(null != detail ){
                    Intent reserveIntent = new Intent(this,AttractionReserveActivity.class);
                    reserveIntent.putExtra("type",detailType);//类型
                    reserveIntent.putExtra("id",detail.comm_id);//商品id
                    reserveIntent.putExtra("img_url",detail.comm_image_url.get(0).comm_image_url);//商品图片url
                    reserveIntent.putExtra("name",detail.comm_name);//商品名称
                    reserveIntent.putExtra("price",detail.comm_price);//商品套餐价
                    reserveIntent.putExtra("oldprice", detail.comm_old_price);//商品参考价
                    reserveIntent.putExtra("attrs", (Serializable) detail.attr_arrs);//商品子属性
                    reserveIntent.putExtra("content",detail.contentUrl);//套餐内容
                    reserveIntent.putExtra("notice",detail.noticeUrl);//消费需知
                    startActivity(reserveIntent);
                }
                break;
            case R.id.img_map://goto地图界面
                if(null != detail ){
                    if(null != detail.entpInfo && !StringUtil.isEmpty(detail.entpInfo.entp_latlng)){
                        Intent intent = new Intent(LocationDetailActivity.this, MapActivity.class);
                        intent.putExtra("lat", Double.parseDouble(destinationLatLon[1]));
                        intent.putExtra("lng", Double.parseDouble(destinationLatLon[0]));
                        intent.putExtra("name",detail.comm_name);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.rl_nav://导航
                if(null != detail ) {
                    if (null != detail.entpInfo && !StringUtil.isEmpty(detail.entpInfo.entp_latlng)) {
                        Uri uri = Uri.parse("geo:"+destinationLatLon[1]+","+destinationLatLon[0]+","+detail.entpInfo.entp_name);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                        Intent mapIntent1 = Intent.createChooser(mapIntent, "请选择打开方式");
                        startActivity(mapIntent1);
                    }
                }
                break;
            case R.id.lin_comment://写评论
                if(null != detail){
                    if(configEntity.isLogin){
                        if(null != detail.comm_id && !"".equals(detail.comm_id)){
                            Intent commentIntent = new Intent(this,EditCommentActivity.class);
                            commentIntent.putExtra("id",detail.comm_id);
                            startActivity(commentIntent);
                        }
                    }else{
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(this,LoginActivity.class);
                        startActivity(loginIntent);
                    }
                }
                break;
            case R.id.btn_attraction_lookmorec://查看跟多评论
                if(null != detail){
                    Intent moreCommentIntent = new Intent(this,CommentActivity.class);
                    moreCommentIntent.putExtra("type","1");
                    moreCommentIntent.putExtra("id",detail.comm_id);
                    startActivity(moreCommentIntent);
                }
                break;
            case R.id.rel_location_detail_nearby:
                if(null != detail){
                    Intent nearbyMoreIntent = new Intent(this,NearbyMoreActivity.class);
                    nearbyMoreIntent.putExtra("id",detail.comm_id);
                    startActivity(nearbyMoreIntent);
                }
                break;
        }
    }

    /**TextView展开或只显示一行文字*/
    private void showOrHideText(boolean isShow,TextView text,ImageView img){
        if (isShow){
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setMaxLines(1);
            img.setBackgroundResource(R.mipmap.guide_attr_lookmore);
        }else{
            text.setEllipsize(null);
            text.setSingleLine(false);
            img.setBackgroundResource(R.mipmap.guide_attr_lookmore_up);
        }
    }

    /**TextView展开或只显示二行文字*/
    private void commentShowOrHideText(int indexIsShow,TextView text,TextView tvMore,ImageView imgMore){
        if (isShowCommentAll.get(indexIsShow)){
            isShowCommentAll.put(indexIsShow,false);
            text.setEllipsize(null);
            text.setSingleLine(false);
            tvMore.setText("收起");
            imgMore.setBackgroundResource(R.mipmap.comment_lookmore_up);
        }else{
            isShowCommentAll.put(indexIsShow,true);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setMaxLines(2);
            tvMore.setText("全部");
            imgMore.setBackgroundResource(R.mipmap.comment_lookmore);
        }
    }

    /**
     * 收藏
     */
    public void collect(){
        if(configEntity.isLogin){
            addCollect();
        }else{
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    /**
     * 获取添加收藏数据信息
     *
     */
    private void addCollect() {
        httpPostRequest(FocusApi.getAddFocusUrl(), getFocusRequestParams(),
                FocusApi.API_GET_ADD_FOCUS);
    }

    /**
     * 获取添加收藏参数
     *
     */
    private HashMap<String,String> getFocusRequestParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("goods_id", id);
        return params;
    }

    /**
     * 显示收藏完成的信息
     * @param json
     */
    public void addCollectHander(String json){
        String msg = JSON.parseObject(json).getString("msg");
        if(null != msg && !"".equals(msg)){
            Toast.makeText(LocationDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(LocationDetailActivity.this);
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           if(null != detail){
               if(null != detail.fjcommList){
                   Intent nearbyMoreIntent = new Intent(this,NearbyMoreActivity.class);
                   nearbyMoreIntent.putExtra("id",detail.comm_id);
                   startActivity(nearbyMoreIntent);
               }
           }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int pos = (position) % ListUtils.getSize(topInfos);
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

    /**
     * 百度地理坐标监听
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            if(null != arg0){
                mLatitude = arg0.getLatitude();
                mLongitude = arg0.getLongitude();
                setDistance();
                mLocationClient.stop();
            }
        }
    }

    /**
     * 显示当前地理坐标和目的地坐标的直线距离
     */
    private void setDistance(){
            // 目的地地理坐标
            double mLat = Double.parseDouble(destinationLatLon[1]);//纬度
            double mLon = Double.parseDouble(destinationLatLon[0]);//经度
            LatLng pt1 = new LatLng(mLatitude, mLongitude); // 我的当前地理坐标
            LatLng pt2 = new LatLng(mLat, mLon);
            double distance = DistanceUtil.getDistance(pt1, pt2);
            String distanceStr[] = Double.toString(distance).split("\\.");
            String lastDistance;
            if(distanceStr[0].length() > 4){//超过千米字符后缀设置成KM
                DecimalFormat df= new DecimalFormat("######0.00");
                double distanceInt = Double.parseDouble(distanceStr[0])/1000;
                lastDistance = df.format(distanceInt)+"KM";
            }else{
                lastDistance = distanceStr[0]+"M";
            }
            tv_location_detail_dis.setText(lastDistance);
            tv_nav.setText("距离我"+lastDistance);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocationClient && mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }
}
