package com.shenmai.system.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenmai.system.R;
import com.shenmai.system.adapter.CateGoodsFragmentAdapter;
import com.shenmai.system.api.AdsApi;
import com.shenmai.system.api.CateApi;
import com.shenmai.system.entity.AdsEntity;
import com.shenmai.system.entity.AdsListEntity;
import com.shenmai.system.entity.CateEntity;
import com.shenmai.system.entity.CateListEntity;
import com.shenmai.system.ui.activity.AdvWebActivity;
import com.shenmai.system.utlis.CacheObjUtils;
import com.shenmai.system.utlis.ListUtils;
import com.shenmai.system.utlis.TopClickUtil;
import com.shenmai.system.widget.PagerSlidingTabStrip;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场
 */
public class StoreFragment extends BaseFragment implements XBanner.OnItemClickListener{

    private PagerSlidingTabStrip store_tabs;
    private ViewPager store_pager;
    private View mView;
    private CateGoodsFragmentAdapter adapter;
    private XBanner storeBanner;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<AdsEntity> adsEntityList;//广告数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_store, container, false);
        initView();
        initListener();
        setCacheData();
        loadAdsAndCate();
        return mView;
    }

    private void setCacheData() {
        AdsListEntity adsListEntity = null;
        try {
            adsListEntity = (AdsListEntity) CacheObjUtils.getObj(getActivity(), "adsListEntity");
            if (adsListEntity != null) {
                if(!ListUtils.isEmpty(adsListEntity.home1)){
                    adsEntityList = adsListEntity.home1;
                    storeBanner.setData(adsEntityList, null);
                    storeBanner.setmAdapter(new XBanner.XBannerAdapter() {
                        @Override
                        public void loadBanner(XBanner banner, View view, int position) {
                            imageLoader.displayImage(adsEntityList.get(position).image_path, (ImageView) view, null,null);
                        }
                    });
                }
                if (!ListUtils.isEmpty(adsListEntity.home2)) {
                    loadClass(adsListEntity.home2);
                }
            }
        } catch (Exception e) {

        }
    }

    private void initView() {
        initTopView(mView);
        hideBackBtn();
        if(configEntity.userRole.equals("1")){
            setTitle("分销");
        } else {
            setTitle("市场");
        }
        setTopRightImg(R.mipmap.top_ss, TopClickUtil.TOP_SEARCH);
        storeBanner = (XBanner) mView.findViewById(R.id.store_banner);
        store_pager = (ViewPager) mView.findViewById(R.id.store_pager);
        store_tabs = (PagerSlidingTabStrip) mView.findViewById(R.id.store_tabs);
    }

    private void initListener() {
        initTopListener();
        storeBanner.setOnItemClickListener(this);
    }

    /**
     * 加载分类
     */
    private void loadCate(){
        httpGetRequest(CateApi.getCateUrl(null), CateApi.API_GET_CATE);
    }

    /**
     * 加载广告
     */
    private void loadAdsAndCate(){
        httpGetRequest(AdsApi.getAdsAndCateUrl(), AdsApi.API_GET_ADS_CATE);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action){
            case AdsApi.API_GET_ADS_CATE:
                adsHander(json);
                break;
            case CateApi.API_GET_CATE:
                cateHander(json);
                break;
        }
    }

    private void adsHander(String json){
        AdsListEntity adsListEntity = JSON.parseObject(json,AdsListEntity.class);
        if(adsListEntity != null){
            adsEntityList = adsListEntity.home1;
            if (adsEntityList != null) {
                storeBanner.removeAllViews();
                storeBanner.setData(adsEntityList, null);
                storeBanner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, View view, int position) {
                        imageLoader.displayImage(adsEntityList.get(position).image_path, (ImageView) view, null,null);
                    }
                });
            }
            //分类
            List<CateEntity> cateEntityList = adsListEntity.home2;
            if(!ListUtils.isEmpty(cateEntityList)) {
                try {
                    CacheObjUtils.saveObj(getActivity(),"adsListEntity",adsListEntity);
                    loadClass(cateEntityList);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadClass(List<CateEntity> cateEntityList){
        ArrayList<String> cateTitleList = new ArrayList<>();
        ArrayList<String> cateIdList = new ArrayList<>();
        cateTitleList.add("发现");
        cateIdList.add("");
        cateTitleList.add("全部");
        cateIdList.add("");
        for (int i = 0; i < cateEntityList.size(); i++) {
            cateTitleList.add(cateEntityList.get(i).cls_name);
            cateIdList.add(cateEntityList.get(i).cls_id);
        }
        if(adapter == null){
            adapter = new CateGoodsFragmentAdapter(getFragmentManager(), cateTitleList, cateIdList);
            store_pager.setAdapter(adapter);
            store_tabs.setViewPager(store_pager);
            store_pager.setCurrentItem(0);
        } else {
            adapter.setData(cateTitleList,cateIdList);
        }

    }

    private void cateHander(String json){
        CateListEntity cateListEntity = JSON.parseObject(json,CateListEntity.class);
        if(cateListEntity != null){
            List<CateEntity> cateEntityList = cateListEntity.list;
            if(!ListUtils.isEmpty(cateEntityList)){
                ArrayList<String> cateTitleList = new ArrayList<>();
                ArrayList<String> cateIdList = new ArrayList<>();
                cateTitleList.add("发现");
                cateIdList.add("");
                cateTitleList.add("全部");
                cateIdList.add("");
                for (int i = 0; i < cateEntityList.size(); i++) {
                    cateTitleList.add(cateEntityList.get(i).cls_name);
                    cateIdList.add(cateEntityList.get(i).cls_id);
                }
                adapter = new CateGoodsFragmentAdapter(getFragmentManager(), cateTitleList, cateIdList);
                store_pager.setAdapter(adapter);
                store_tabs.setViewPager(store_pager);
                store_pager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onItemClick(XBanner banner, int position) {
        if(getActivity() == null){
            return;
        }
        if (!ListUtils.isEmpty(adsEntityList) && adsEntityList.get(position) != null) {
            AdsEntity adsEntity = adsEntityList.get(position);
            if (adsEntity != null) {
                Intent advIntent = new Intent(getActivity(), AdvWebActivity.class);
                advIntent.putExtra("title", adsEntity.adv_title);
                advIntent.putExtra("url", adsEntity.link_url);
                advIntent.putExtra("image_path", adsEntity.image_path);
                startActivity(advIntent);
            }
            adsEntity = null;
        }
    }
}
