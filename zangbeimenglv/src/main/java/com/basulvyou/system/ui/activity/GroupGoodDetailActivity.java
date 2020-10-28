package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.TopOneAdapter;
import com.basulvyou.system.api.ShopDetailApi;
import com.basulvyou.system.entity.GroupGoodDetailEntity;
import com.basulvyou.system.entity.GroupGoodEntity;
import com.basulvyou.system.entity.ShopDetailImgEntity;
import com.basulvyou.system.entity.TopInfo;
import com.basulvyou.system.listener.PageChangeListener;
import com.basulvyou.system.listener.ShareClickListener;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.MyDate;
import com.basulvyou.system.util.TopClickUtil;
import com.basulvyou.system.wibget.AutoScrollViewPager;
import com.basulvyou.system.wibget.CountDownTimerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购商品详情页面
 */
public class GroupGoodDetailActivity extends BaseActivity implements View.OnClickListener,TopOneAdapter.OnModelClickListener,ViewPager.OnPageChangeListener {

    private View  goodInfo,goodComment,commentLine;
    private TextView goodGroupPrice, btn_cart, goodPrice,
            goodType, commentedNum, goodEndDate;
    private AutoScrollViewPager pager;
    private TopOneAdapter mAdAdapter;
    private PageChangeListener pageChangeListener;
    private ShareClickListener shareClickListener;
    private View parentView;
    private LinearLayout indicatorLayout;//图片位置指示
    private ImageView[] indicators = null;
    private List<TopInfo> topInfos;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int width;
    private CountDownTimerView countDownTimer;
    private TextView startTime;//距离开团
    private String goods_id, imageUrl, goods_name;
    private GroupGoodDetailEntity detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_group_good_detail,null);
        setContentView(parentView);
        goods_id = getIntent().getExtras().getString("goods_id");
        goods_name = getIntent().getExtras().getString("goods_name");
        imageUrl = getIntent().getExtras().getString("image_url");
        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getResources().getDisplayMetrics();
        width = dm.widthPixels;//宽度
        initView();
        initListener();
        getGoodDetail();
    }

    public void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null !=countDownTimer) {
            countDownTimer.clearTimer();
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("商品详情");
        setTopRightImg(R.mipmap.top_share, TopClickUtil.TOP_SHA);
        btn_cart = (TextView) findViewById(R.id.sale_btn_cart);
        pager = (AutoScrollViewPager) findViewById(R.id.sale_shop_detail_pager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pager.getLayoutParams();
        params.height = width;
        indicatorLayout = (LinearLayout) findViewById(R.id.sale_chose_indicator);
        goodGroupPrice = (TextView) findViewById(R.id.sale_tv_goodGroupPrice);
        countDownTimer = (CountDownTimerView) findViewById(R.id.sale_countdown_timer);
        goodPrice = (TextView) findViewById(R.id.sale_tv_goodPrice);
        goodPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodType = (TextView) findViewById(R.id.sale_goodType);
        goodEndDate = (TextView) findViewById(R.id.sale_enddate);
        goodInfo = (View) findViewById(R.id.sale_rl_goodInfo);
        goodComment = (View) findViewById(R.id.sale_rl_goodcomments);
        commentedNum = (TextView) findViewById(R.id.sale_tv_comments_num);
        commentLine = (View) findViewById(R.id.sale_have_comment_line);
        startTime = (TextView) findViewById(R.id.tv_end_time);
    }

    /**
     * 设置商品数据数据
     */
    private void setData() {
        if (detail!= null) {
            if ("1".equals(detail.can_buy)) {
                btn_cart.setClickable(false);
                btn_cart.setText("即将开团");
                startTime.setText("距离开团:");
            } else {
                btn_cart.setClickable(true);
                btn_cart.setText("立即购买");
                startTime.setText("距离结束:");
            }
            if (null != detail.pd_stock) {
                if ("0".equals(detail.pd_stock)) {
                    btn_cart.setClickable(false);
                    btn_cart.setText("商品已售罄");
                }
            }
            goodGroupPrice.setText("￥"+detail.groupbuy_price);
            int[] date = MyDate.formatDuring(detail.xiangcha_date_s);
            if(date != null && date.length > 0){
                countDownTimer.setDateTextColor(getResources().getColor(R.color.bg_countdown_text));
                countDownTimer.setTipTextColor(getResources().getColor(R.color.bg_countdown_tip));
                if ("1".equals(detail.can_buy)) {
                    countDownTimer.setTime(date[0], date[1], date[2], date[3], true, btn_cart);
                } else {
                    countDownTimer.setTime(date[0], date[1], date[2], date[3], false, btn_cart);
                }
                countDownTimer.start();
            }
            goodPrice.setText("￥"+detail.goods_price);
            goodType.setText(detail.goods_name);
            goodEndDate.setText("截至日期:"+detail.end_time_text);
            goods_name = detail.goods_name;
            commentedNum.setText("晒单评价("+detail.commentCount+")");
            List<ShopDetailImgEntity> imgEntity = detail.groupbuy_image;
            if (!ListUtils.isEmpty(imgEntity)) {
                imageUrl = imgEntity.get(0).comm_image_url;
                topInfos = new ArrayList();
                for (int i = 0; i < imgEntity.size(); i++) {
                    TopInfo topInfo = new TopInfo();
                    topInfo.pciture = imgEntity.get(i).comm_image_url;
                    topInfos.add(topInfo);
                }
                indicators = new ImageView[topInfos.size()]; // 定义指示器数组大小
                initIndicator();
                mAdAdapter = new TopOneAdapter(this,"salesShopDetail");
                mAdAdapter.setData(topInfos);
                mAdAdapter.setInfiniteLoop(true);
                pager.setAdapter(mAdAdapter);
                pager.setAutoScrollDurationFactor(10);
                pager.setInterval(2000);
                pager.startAutoScroll();
                mAdAdapter.setOnModelClickListener(this);
                if(topInfos != null){
                    pager.setCurrentItem(topInfos.size());
                }
            }
        }

    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        btn_cart.setOnClickListener(this);
        pager.setOnPageChangeListener(this);
        goodInfo.setOnClickListener(this);
        goodComment.setOnClickListener(this);
        shareClickListener = new ShareClickListener(this);// 添加分享监听
        setShareNameAndImg();
        rightOne.setOnClickListener(shareClickListener);
        btn_top_sidebar.setOnClickListener(shareClickListener);
    }

    /**
     * 设置分享文字和图片
     */
    private void setShareNameAndImg(){
        if(shareClickListener != null){
            shareClickListener.setShareImgUrl(imageUrl);
            shareClickListener.setContent(goods_name);
            shareClickListener.setGoodsID(goods_id);
        }
    }

   /**
     * 请求商品详情
     * @param
     */
    private void getGoodDetail()
    {
        httpGetRequest(ShopDetailApi.getGroupGoodDetailUrl(goods_id),ShopDetailApi.API_GET_GROUP_GOOD_DETAIL);
    }


    @Override
    public void onClick(View v) {
        if (null == detail) {
            Toast.makeText(GroupGoodDetailActivity.this, "商品已下架!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(GroupGoodDetailActivity.this, LoginActivity.class);
        switch (v.getId()) {
            case R.id.sale_rl_goodInfo://商品描述
                Intent describeIntent = new Intent(GroupGoodDetailActivity.this,GoodsDesribieActivity.class);
                describeIntent.putExtra("comm_id", detail.group_id);
                describeIntent.putExtra("goodType", "group");
                startActivity(describeIntent);
                break;
            case R.id.sale_rl_goodcomments://商品评论
                Intent commentIntent = new Intent(GroupGoodDetailActivity.this,CommentWebActivity.class);
                commentIntent.putExtra("comm_id", detail.group_id);
                commentIntent.putExtra("goodType", "group");
                startActivity(commentIntent);
                break;
            case R.id.sale_btn_cart:// 立即购买
                if (configEntity.isLogin) {
                    Intent groupCartIntent = new Intent(GroupGoodDetailActivity.this, BuyLocationGoodWebActivity.class);
                    groupCartIntent.putExtra("id", detail.group_id);
                    groupCartIntent.putExtra("type", "group");
                    startActivity(groupCartIntent);
                } else {
                    startActivity(intent);
                }
                break;
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

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case ShopDetailApi.API_GET_GROUP_GOOD_DETAIL:
                goodDetailHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理商品详情信息
     * @param json
     */
    private void goodDetailHander(String json){
        GroupGoodEntity shopDetailEntity = JSON.parseObject(json, GroupGoodEntity.class);
        if (null == shopDetailEntity) {
            Toast.makeText(this, "商品已下架", Toast.LENGTH_SHORT).show();
        }else{
            if (null == shopDetailEntity.group_info) {
                Toast.makeText(this, "商品已下架", Toast.LENGTH_SHORT).show();
            }else{
                detail = shopDetailEntity.group_info;
                setData();
            }
        }

    }

    @Override
    public void gotoShop(String key,TopInfo topInfo) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

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

}
