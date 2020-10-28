package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.CouponAdapter;
import com.basulvyou.system.api.CouponApi;
import com.basulvyou.system.entity.CouponEntity;
import com.basulvyou.system.entity.CouponListEntity;
import com.basulvyou.system.util.ListUtils;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

/**
 * 我的优惠券界面
 */
public class CouponActivity extends AbsLoadMoreActivity<ListView, CouponEntity> implements View.OnClickListener{

    private PullToRefreshListView mPullToRefreshListView;
    private TextView coupon_title,coupon_spec;
    public boolean hasmore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        initView();
        initListener();
        setApadter();
        loadData();
    }

    private void initView(){
        initTopView();
        setTitle("我的优惠券");
        setLeftBackButton();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.coupon_list);
        coupon_title = (TextView) findViewById(R.id.tv_coupon_num);
        coupon_spec = (TextView) findViewById(R.id.tv_coupon_spec);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        coupon_spec.setOnClickListener(this);
    }

    private void setApadter(){
        mAdapter = new CouponAdapter(null, this);
        mPullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    /**获取上拉刷新网络数据*/
    @Override
    protected void loadData() {
        httpPostRequest(CouponApi.getCouponUrl(), getRequestParams(), CouponApi.API_GET_VOUCHER_LIST);
    }

    /**
     * 获取我的代金券参数
     *
     * @return
     */
    private HashMap getRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("select_detail_state", "1");//默认获取未使用的代金券
        params.put("page", mPager.rows + "");
        params.put("curpage", mPager.getPage() + "");
        return params;
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case CouponApi.API_GET_VOUCHER_LIST:
                couponHander(json);
                break;
            default:
                break;
        }
    }

    /**
     * 处理我的代金券信息
     * @param json
     */
    private void couponHander(String json){
        if(hasmore){
            final long start = System.currentTimeMillis();
            List<CouponEntity> orderList = null;
            CouponListEntity voucher = JSON.parseObject(json,
                    CouponListEntity.class);
            if(voucher != null){
                orderList = voucher.voucher_list;
            }
            appendData(orderList, start);
//            hasmore = hasmoreTemp;
            if(!ListUtils.isEmpty(orderList)){
                coupon_title.setText("共" + orderList.size() + "个优惠券");
            }
        } else {
            getRefreshView().post(new Runnable() {

                public void run() {
                    getRefreshView().onRefreshComplete();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_coupon_spec:
                    Intent couponSpecIntent = new Intent(this,CouponSpecActivity.class);
                    startActivity(couponSpecIntent);
                    break;
            }
    }
}
