package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.basulvyou.system.R;
import com.basulvyou.system.UIApplication;
import com.basulvyou.system.adapter.NearbyFragmentAdapter;
import com.basulvyou.system.entity.ShopBundleEntity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.ToastUtil;
import com.basulvyou.system.wibget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/6/6.
 * copyright easybiz.
 * 附近界面
 */

public class NearbyActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final int NEARBY_REQUEST_CODE = 1000;
    private PagerSlidingTabStrip location_tabs;
    private ViewPager location_pager;
    private View tagOtherView;
    private PopupWindow pop;
    private View tagPopView;
    private TextView hotTag, newTag, disTag;
    private int tagNomalBg = R.mipmap.sx_grey;
    private int tagSelectBg = R.mipmap.sx_orange;
    private ShopBundleEntity shopBundleEntity;
    private NearbyFragmentAdapter adapter;
    private LocationClient mLocationClient;
    private String lnglatString;
    private String address;
    private boolean getLocationAgain = false;
    private String detailAddress;
    private ProgressBar progressBar;
    private ArrayList<String> poisName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        if (getIntent().getExtras() != null) {
            shopBundleEntity = (ShopBundleEntity) getIntent().getExtras().getSerializable("shopBundle");
        }
        initTopView();
        initView();
        setTitle("附近");
        initLocation();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTopRightTitleClick(this);
        location_pager = (ViewPager) findViewById(R.id.location_pager);
        location_tabs = (PagerSlidingTabStrip) findViewById(R.id.location_tabs);
        pop = new PopupWindow(this);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_tag_pop));
        tagPopView = getLayoutInflater().inflate(R.layout.pop_location_select, null);
        hotTag = (TextView) tagPopView.findViewById(R.id.hot_tag);
        newTag = (TextView) tagPopView.findViewById(R.id.new_tag);
        disTag = (TextView) tagPopView.findViewById(R.id.dis_tag);
        tagOtherView = tagPopView.findViewById(R.id.lin_other);
        btn_top_goback.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        initTagBackgroud();
        pop.setContentView(tagPopView);
    }

    public void initListener() {
        super.initListener();
        initSideBarListener();
        hotTag.setOnClickListener(this);
        newTag.setOnClickListener(this);
        disTag.setOnClickListener(this);
        tagOtherView.setOnClickListener(this);
    }

    private void setAdapter() {
        ArrayList<String> list = new ArrayList<>();
        String[] location = getResources().getStringArray(R.array.nearby);
        if (location != null) {
            for (int i = 0; i < location.length; i++) {
                list.add(location[i]);
            }
            adapter = new NearbyFragmentAdapter(getSupportFragmentManager(), list);
            if (shopBundleEntity == null) {
                shopBundleEntity = new ShopBundleEntity();
            }
//          locationType = getIntent().getExtras().getString("type");
            adapter.setShopBundle(shopBundleEntity);
            adapter.setNeedRe();
//          adapter.setLocationType(locationType);
            location_pager.setAdapter(adapter);
            location_tabs.setViewPager(location_pager);
            location_pager.setCurrentItem(0);
            location_tabs.setOnPageChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_tag:
                setSelectedBg(hotTag, "2");
                break;
            case R.id.new_tag:
                setSelectedBg(newTag, "3");
                break;
            case R.id.dis_tag:
                setSelectedBg(disTag, "5");
                break;
            case R.id.lin_other:
                isShowTagPop();
                break;
            case R.id.tv_top_right_text:
                Intent intent = new Intent(this, PoiSearchActivity.class);
                startActivityForResult(intent, NEARBY_REQUEST_CODE);
                break;
            case R.id.btn_top_goback:
                finish();
                break;
        }
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
            if (4 == location_pager.getCurrentItem()) {
                disTag.setVisibility(View.GONE);
                hotTag.setText("浏览量");
            } else {
                disTag.setVisibility(View.VISIBLE);
                hotTag.setText("热门");
            }
            pop.showAsDropDown(location_tabs);
        }
    }

    /**
     * 初始化标签设置未选中情况下背景和字体颜色
     */
    private void initTagBackgroud() {
        hotTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        newTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        disTag.setTextColor(getResources().getColor(R.color.location_tag_nomal));
        hotTag.setBackgroundResource(tagNomalBg);
        newTag.setBackgroundResource(tagNomalBg);
        disTag.setBackgroundResource(tagNomalBg);
    }

    /**
     * 设置选中的情况下背景和字体颜色
     * 和要按要求的关键key
     */
    private void setSelectedBg(TextView tagView, String key) {
        initTagBackgroud();
        tagView.setTextColor(getResources().getColor(R.color.location_tag_select));
        tagView.setBackgroundResource(tagSelectBg);
        if (shopBundleEntity == null) {
            shopBundleEntity = new ShopBundleEntity();
        }
        shopBundleEntity.key = key;
        adapter.setShopBundle(shopBundleEntity);
        adapter.notifyDataSetChanged();
        isShowTagPop();
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(NearbyActivity.this);
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 百度地图监听
     *
     * @author Administrator
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            progressBar.setVisibility(View.GONE);
            if (arg0.getLocType() != BDLocation.TypeNetWorkLocation) {
                ToastUtil.showToast("获取位置信息失败", NearbyActivity.this, ToastUtil.DELAY_SHORT);
                getLocationAgain = true;
                setTopRightTitleAdd("获取位置失败", 11);
//                location.setClickable(true);
            } else {
                lnglatString = String.valueOf(arg0.getLongitude()) + "," + String.valueOf(arg0.getLatitude());
                detailAddress = arg0.getAddrStr();//当前位子详细地址
                address = arg0.getProvince() + arg0.getCity();
                final List<Poi> pois = arg0.getPoiList();
                if (!ListUtils.isEmpty(pois)) {
                    setTopRightTitleAdd(pois.get(0).getName(), 11);
                    new Thread() {
                        @Override
                        public void run() {
                            for (Poi poi : pois) {
                                poisName.add(poi.getName());
                            }
                        }
                    }.start();
                }
            }
//            location.setVisibility(View.VISIBLE);
            double longitude = arg0.getLongitude();
            double latitude = arg0.getLatitude();
            UIApplication.getInstance().setLatitude(latitude);
            UIApplication.getInstance().setLongitude(longitude);
            setAdapter();
            mLocationClient.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == NEARBY_REQUEST_CODE) {
                SuggestionResult.SuggestionInfo info = data.getParcelableExtra("suggestioninfo");
                if (info != null) {
                    setTopRightTitleAdd(info.key, 11);
                    if (info.pt != null){
                        UIApplication.getInstance().setLatitude(info.pt.latitude);
                        UIApplication.getInstance().setLongitude(info.pt.longitude);
                    }
//                    if (adapter != null && info.pt != null) {
//                        adapter.setAddr(info.pt.longitude, info.pt.latitude);
//                        for (int i = 0; i < adapter.getFragments().size(); i++) {
//                            adapter.getFragments().get(i).setAddr(info.pt.longitude, info.pt.latitude);
//                        }
//                    }
                }
            }
        }
    }
}
