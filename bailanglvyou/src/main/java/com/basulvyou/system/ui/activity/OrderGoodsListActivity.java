package com.basulvyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.basulvyou.system.R;
import com.basulvyou.system.adapter.OrderGoodsListAdapter;
import com.basulvyou.system.entity.OrderGoodsList;

import java.util.List;

/**
 * 订单商品列表
 */
public class OrderGoodsListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView order_goods_list;
    private OrderGoodsListAdapter adapter;
    private List<OrderGoodsList> orderGoodsList;
    private String orderType;// 0普通单| 1团购单

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_goods_list);
        orderGoodsList = (List<OrderGoodsList>) getIntent().getExtras().getSerializable("orderGoodsList");
        orderType = getIntent().getStringExtra("orderType");
        initView();
        initListener();
        setAdapter();
    }

    protected void onResume(){
        super.onResume();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();
        setTitle("商品列表");
        order_goods_list = (ListView) findViewById(R.id.order_goods_list);
    }

    /**
     * 设置tab数据
     */
    private void setAdapter(){
        adapter = new OrderGoodsListAdapter(orderGoodsList, this);
        order_goods_list.setAdapter(adapter);
    }

    public void initListener(){
        super.initListener();
        initSideBarListener();
        order_goods_list.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        OrderGoodsList orderGoods = orderGoodsList.get(position);
        Intent intent = null;
        if ("0".equals(orderType)) {
            intent = new Intent(OrderGoodsListActivity.this, GoodsDetailActivity.class);
        }else if("1".equals(orderType)){
           intent = new Intent(OrderGoodsListActivity.this, GroupGoodDetailActivity.class);
        }
        intent.putExtra("goods_id", orderGoods.goods_id);
        intent.putExtra("image_url", orderGoods.goods_image_url);
        intent.putExtra("goods_name", orderGoods.goods_name);
        startActivity(intent);
    }

}


