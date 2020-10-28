package com.basulvyou.system.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.basulvyou.system.R;
import com.basulvyou.system.adapter.OrderAdapter;
import com.basulvyou.system.api.OrderApi;
import com.basulvyou.system.entity.OrderEntity;
import com.basulvyou.system.entity.OrderList;
import com.basulvyou.system.entity.OrderListEntity;
import com.basulvyou.system.ui.activity.OrderDetailActivity;
import com.basulvyou.system.ui.activity.PayWebActivity;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.ListUtils;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

/**
 * 订单分类列表显示
 */
public class OrderTypeFragment extends AbsLoadMoreFragment<ListView, OrderListEntity> implements AdapterView.OnItemClickListener,OrderAdapter.CancelOrderListener {

    private PullToRefreshListView mPullToRefreshListView;
    private String key;
    private List<OrderListEntity> orderList = null;
    private OrderEntity order;

    public static Fragment getInstance(Bundle bundle) {
        OrderTypeFragment fragment = new OrderTypeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_type, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        key = bundle.getString("key");
        configEntity = ConfigUtil.loadConfig(getActivity());
        initView(view);
        initListener();
        setAdapter();
        showLoading("正在加载中...", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(configEntity.isLogin){
            loadData();
        }
    }

    private void initListener(){
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
        mPullToRefreshListView.setOnItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPager.setPage(0);
        if(!ListUtils.isEmpty(orderList)){
            clearData();
        }
    }

    private void initView(View view) {
        initTopView(view);
        hideAllTop();
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.order_list);
    }

    @Override
    protected PullToRefreshAdapterViewBase<ListView> getRefreshView() {
        return mPullToRefreshListView;
    }

    @Override
    protected void loadData() {
        httpGetRequest(OrderApi.getMyOrderListUrl(configEntity.key, mPager.rows + "", mPager.getPage() + ""
                , key), OrderApi.API_GET_MY_ORDER_LIST);
    }

    private void setAdapter(){
        mAdapter = new OrderAdapter(null,getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
        ((OrderAdapter) mAdapter).setCancelOrderListener(this);
    }

    @Override
    public void httpResponse(String json, int action){
        super.httpResponse(json, action);
        switch (action)
        {
            case OrderApi.API_GET_MY_ORDER_LIST:
                orderHander(json);
                break;
            case OrderApi.API_GET_CANCEL_ORDER:
                Toast.makeText(getActivity(), "取消订单成功", Toast.LENGTH_SHORT).show();
                mPager.setPage(0);
                if(!ListUtils.isEmpty(orderList)){
                    orderList.clear();
                    orderList = null;
                    clearData();
                }
                loadData();
                break;
            default:
                break;
        }
    }

    /**
     * 处理我的订单信息
     * @param json
     */
    private void orderHander(String json){
        final long start = System.currentTimeMillis();
        OrderList myOrderList = JSON.parseObject(json, OrderList.class);
        if(myOrderList != null){
            orderList = myOrderList.order_group_list;
        }
        appendData(orderList, start);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= 1;
        if(mAdapter != null && mAdapter.getCount() > position){
            OrderListEntity myOrder = mAdapter.getItem(position);
            if(myOrder != null){
                List<OrderEntity> order_list = myOrder.order_list;
                if(!ListUtils.isEmpty(order_list)){
                    OrderEntity order = order_list.get(0);
                    if(getActivity() != null && order != null){
                        if(null != order.order_type && "100".equals(order.order_type)){
                            //虚拟订单在待支付的情况下才去支付页面
                            if(order.order_state.equals("0")){
                                Intent payIntent = new Intent(getActivity(), PayWebActivity.class);
                                payIntent.putExtra("order_sn",order.order_sn);
                                payIntent.putExtra("pay_type",order.pay_type);
                                startActivity(payIntent);
                            }
                            return;
                        }
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    /**
     * 删除订单信息
     */
    private void getDelOrder(){
        httpPostRequest(OrderApi.getCancelOrderUrl(),
                getDelRequestParams(), OrderApi.API_GET_CANCEL_ORDER);
    }

    /**
     * 获取删除订单参数
     *
     * @return
     */
    private HashMap getDelRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("order_id", order.order_id);
        params.put("order_state", "-10");
        return params;
    }

    @Override
    public void cancel(OrderEntity orderEntity) {
        order = orderEntity;
        getDelOrder();
    }
}
