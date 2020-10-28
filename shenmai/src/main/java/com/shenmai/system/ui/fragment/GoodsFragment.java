package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.adapter.GoodsGridAdapter;
import com.shenmai.system.api.GoodsApi;
import com.shenmai.system.entity.GoodsEntity;
import com.shenmai.system.entity.GoodsList;
import com.shenmai.system.entity.HomeEntity;
import com.shenmai.system.ui.activity.GoodsDetailActivity;
import com.shenmai.system.utlis.AsynImageUtil;
import com.shenmai.system.utlis.CacheObjUtils;
import com.shenmai.system.utlis.GetHeightAndWidthUtil;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.StringUtil;
import com.shenmai.system.widget.MyGridView;

import java.util.HashMap;
import java.util.List;

/**
 *商品Grid显示
 */
public class GoodsFragment extends AbsScrollViewLoadMoreFragment<GoodsEntity> implements AdapterView.OnItemClickListener,GoodsGridAdapter.onAddShopListener{

    private View mView;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private MyGridView gridView;
    private TextView[] goodsName = new TextView[3];
    private TextView[] goodsType = new TextView[3];
    private TextView[] goodsPrice = new TextView[3];
    private TextView[] goodsReback = new TextView[3];
    private TextView[] goodsState = new TextView[3];
    private ImageView[] addShop = new ImageView[3];
    private ImageView[] goodsIma = new ImageView[3];
    private ImageView headIma;
    private View[] relView = new View[3];
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout linearGoodsHead;//显示动态广告布局

    public static Fragment getInstance(Bundle bundle) {
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_goods, container, false);
        initView();
        setAdapter();
        initListenter();
        setCacheData();
        mPager.setPage(0);
        loadData();
        return mView;
    }

    /**
     * 设置缓存数据
     */
    private void setCacheData() {
        final long start = System.currentTimeMillis();
        GoodsList goods = null;
        try {
            goods = (GoodsList)  CacheObjUtils.getObj(getActivity(), "GoodsList");
        } catch (Exception e) {
        }
        if (goods != null && !ListUtils.isEmpty(goods.goodsList)) {
            List<GoodsEntity> goodsList = goods.goodsList;
            if (!ListUtils.isEmpty(goods.home1)) {
                setData(goods.home1);
            }
            appendData(goodsList, start,true);
        }
    }

    private void initView(){
        mPullToRefreshScrollView = (PullToRefreshScrollView) mView.findViewById(R.id.goods_grid_refer);
        linearGoodsHead = (LinearLayout) mView.findViewById(R.id.lin_goods_head);
        gridView = (MyGridView) mView.findViewById(R.id.grid_goods_grid);
    }

    private void initListenter(){
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setScrollingWhileRefreshingEnabled(true);
        gridView.setOnItemClickListener(this);
        ((GoodsGridAdapter)mAdapter).setOnAddShopListener(this);
    }

    private void setAdapter(){
        mAdapter = new GoodsGridAdapter(null, getActivity());
        gridView.setAdapter(mAdapter);
    }

    /**
     * 设置热门数据
     */
    private void setData(List<HomeEntity> list){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < list.size(); i++) {
            HomeEntity entity = list.get(i);
            View view = inflater.inflate(R.layout.grid_goods_head,null);
            findHeadItemView(view,entity);
            linearGoodsHead.addView(view);
        }
    }

    /**
     * 找到控件并赋值
     * @param mView
     */
    private void findHeadItemView(View mView,HomeEntity homeEntity){
        headIma = (ImageView) mView.findViewById(R.id.img_grid_head_column_img);
        relView[0] = mView.findViewById(R.id.rel_grid_goods_head_1);
        relView[1] = mView.findViewById(R.id.rel_grid_goods_head_2);
        relView[2] = mView.findViewById(R.id.rel_grid_goods_head_3);
        goodsName[0] = (TextView) mView.findViewById(R.id.tv_grids_head_goodname1);
        goodsName[1] = (TextView) mView.findViewById(R.id.tv_grids_head_goodname2);
        goodsName[2] = (TextView) mView.findViewById(R.id.tv_grids_head_goodname3);
        goodsType[0] = (TextView) mView.findViewById(R.id.tv_grid_head_goodtype1);
        goodsType[1] = (TextView) mView.findViewById(R.id.tv_grid_head_goodtype2);
        goodsType[2] = (TextView) mView.findViewById(R.id.tv_grid_head_goodtype3);
        goodsPrice[0] = (TextView) mView.findViewById(R.id.tv_grid_head_goodprice1);
        goodsPrice[1] = (TextView) mView.findViewById(R.id.tv_grid_head_goodprice2);
        goodsPrice[2] = (TextView) mView.findViewById(R.id.tv_grid_head_goodprice3);
        goodsReback[0] = (TextView) mView.findViewById(R.id.tv_grid_head_goodreback1);
        goodsReback[1] = (TextView) mView.findViewById(R.id.tv_grid_head_goodreback2);
        goodsReback[2] = (TextView) mView.findViewById(R.id.tv_grid_head_goodreback3);
        goodsState[0] = (TextView) mView.findViewById(R.id.tv_grid_head_goodstate1);
        goodsState[1] = (TextView) mView.findViewById(R.id.tv_grid_head_goodstate2);
        goodsState[2] = (TextView) mView.findViewById(R.id.tv_grid_head_goodstate3);
        addShop[0] = (ImageView) mView.findViewById(R.id.img_grid_head_addshop1);
        addShop[1] = (ImageView) mView.findViewById(R.id.img_grid_head_addshop2);
        addShop[2] = (ImageView) mView.findViewById(R.id.img_grid_head_addshop3);
        goodsIma[0] = (ImageView) mView.findViewById(R.id.img_grid_head_goodimg1);
        goodsIma[1] = (ImageView) mView.findViewById(R.id.img_grid_head_goodimg2);
        goodsIma[2] = (ImageView) mView.findViewById(R.id.img_grid_head_goodimg3);
        List<GoodsEntity> list = homeEntity.parList;
        if (GetHeightAndWidthUtil.width != 0) {
            ViewGroup.LayoutParams params = headIma.getLayoutParams();
            params.height = GetHeightAndWidthUtil.width / 3;
        }
        if (!StringUtil.isEmpty(homeEntity.top_img)) {
            imageLoader.displayImage(homeEntity.top_img, headIma, null,null);
        }
        for (int i = 0; i < list.size(); i++) {
            GoodsEntity entity = list.get(i);
            if( i == 0 ){
                goodsInfoSet(entity,i);
            }
            if( i == 1 ){
                goodsInfoSet(entity,i);
            }
            if( i == 2){
                goodsInfoSet(entity,i);
            }
        }
    }

    /**
     * 为控件赋值
     * @param entity
     * @param i
     */
    private void goodsInfoSet(final GoodsEntity entity,int i){
        imageLoader.displayImage(entity.goods_image_url, goodsIma[i], AsynImageUtil.getImageOptions(R.mipmap.goods_image), null);
        goodsName[i].setText(entity.goods_name);
        goodsType[i].setText(entity.sub_name);
        String priceString = "价格:￥" + entity.goods_price;
        int Nstart = priceString.indexOf("￥") + 1;
        SpannableStringBuilder style = new SpannableStringBuilder(priceString);
        style.setSpan(new TextAppearanceSpan(getActivity(), R.style.GoodsReback), Nstart, priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goodsPrice[i].setText(style);
        if(entity.is_has_kc){
            goodsState[i].setText("有货");
        } else {
            goodsState[i].setText("无货");
        }
        if(configEntity.userRole.equals("1")){
            goodsReback[i].setVisibility(View.VISIBLE);
            addShop[i].setVisibility(View.VISIBLE);
            String rebackString = "佣金:￥"+entity.yj_price;
            SpannableStringBuilder rebackStyle = new SpannableStringBuilder(rebackString);
            rebackStyle.setSpan(new TextAppearanceSpan(getActivity(), R.style.GoodsReback), 0, rebackString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsReback[i].setText(rebackStyle);
            addShop[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(entity.is_has_kc){
                        addGoodsToShop(entity.goods_id);
                    }else{
                        Toast.makeText(getActivity(),"该商品无货",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            goodsReback[i].setVisibility(View.GONE);
            addShop[i].setVisibility(View.GONE);
        }
        relView[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                detailIntent.putExtra("id", entity.goods_id);
                startActivity(detailIntent);
            }
        });
    }

    @Override
    protected PullToRefreshScrollView getRefreshView() {
        return mPullToRefreshScrollView;
    }

    @Override
    protected void loadData() {
        if(mPager.getPage() == 0){
            httpGetRequest(GoodsApi.getGoodsListUrl(mPager.getPage() + "", null, null, null, "true"), GoodsApi.API_GET_SHOP_LIST);
        } else {
            httpGetRequest(GoodsApi.getGoodsListUrl(mPager.getPage() + "", null, null, null, null), GoodsApi.API_GET_SHOP_LIST);
        }
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case GoodsApi.API_GET_SHOP_LIST:
                goodsHander(json);
                break;
            case GoodsApi.API_ADD_GOODS_TOSHOP:
                Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void goodsHander(String json){
        final long start = System.currentTimeMillis();
        List<GoodsEntity> goodsList = null;
        if(!StringUtil.isEmpty(json)){
            GoodsList goods = JSON.parseObject(json, GoodsList.class);
            if (goods != null) {
                try {
                    if (mPager.getPage() == 0) {
                        CacheObjUtils.saveObj(getActivity(), "GoodsList", goods);
                    }
                } catch (Exception e) {
                }
                goodsList = goods.goodsList;
                if (!ListUtils.isEmpty(goods.home1)) {
                    linearGoodsHead.removeAllViews();
                    setData(goods.home1);
                }
            }
        }
        if(mPager.getPage() == 0){
            clearData();
        }
        appendData(goodsList, start,false);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(getActivity() == null){
            return;
        }
        if(null != mAdapter.getItem(position)){
            GoodsEntity goodsEntity = mAdapter.getItem(position);
            if(goodsEntity != null){
                Intent detailIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                detailIntent.putExtra("id",goodsEntity.goods_id);
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void addShop(int position) {
        GoodsEntity goodsEntity = mAdapter.getItem(position);
        if(goodsEntity != null){
            if(goodsEntity.is_has_kc){
                addGoodsToShop(goodsEntity.goods_id);
            } else {
                Toast.makeText(getActivity(),"该商品无货",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addGoodsToShop(String goodsId){
        httpPostRequest(GoodsApi.addGoodsToShopUrl(), getRequestParams(goodsId), GoodsApi.API_ADD_GOODS_TOSHOP);
    }

    private HashMap<String, String> getRequestParams(String goodsId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("goods_id", goodsId);
        return params;
    }

}
