package com.yishangshuma.bangelvyou.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.api.OrderApi;
import com.yishangshuma.bangelvyou.entity.OrderEntity;
import com.yishangshuma.bangelvyou.entity.OrderGoodsList;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *订单详情页面
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener{

    private View rl_goods;
    private TextView tv_name, tv_phone, tv_address, tv_num, tv_way, tv_delivery, tv_bill_state
            , tv_bill_user, tv_bill_class, tv_good_price, tv_freight_price, tv_shop_price
            , tv_delete_order, tv_buy;
    private ImageView img_good_1, img_good_2, img_good_3;
    private OrderEntity order;
    private String state = "-10";//订单状态，默认是取消状态
    private String evaluation_state = "0";//订单评论状态，默认是未状态
    private int COMMENT=0;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        order = (OrderEntity) getIntent().getExtras().getSerializable("order");
        initView();
        initListener();
        setData();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        initTopView();
        setLeftBackButton();;
        setTitle("订单详情");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_way = (TextView) findViewById(R.id.tv_way);
        tv_delivery = (TextView) findViewById(R.id.tv_delivery);
        tv_bill_state = (TextView) findViewById(R.id.tv_bill_state);
        tv_bill_user = (TextView) findViewById(R.id.tv_bill_user);
        tv_bill_class = (TextView) findViewById(R.id.tv_bill_class);
        tv_good_price = (TextView) findViewById(R.id.tv_good_price);
        tv_freight_price = (TextView) findViewById(R.id.tv_freight_price);
        tv_shop_price = (TextView) findViewById(R.id.tv_shop_price);
        tv_delete_order = (TextView) findViewById(R.id.tv_delete_order);
        tv_buy = (TextView) findViewById(R.id.tv_buy);

        img_good_1 = (ImageView) findViewById(R.id.img_good_1);
        img_good_2 = (ImageView) findViewById(R.id.img_good_2);
        img_good_3 = (ImageView) findViewById(R.id.img_good_3);

        rl_goods = (View) findViewById(R.id.rl_goods);
    }

    /**
     * 设置tab数据
     */
    private void setData(){
        if(order != null){
            List<OrderGoodsList> order_goods = order.extend_order_goods;
            int j = 0;
            for(int i = 0; i < order_goods.size(); i++){
                j += Integer.parseInt(order_goods.get(i).goods_num);
                if(i == 0){
                    img_good_1.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(order_goods.get(i).goods_image_url, img_good_1,
                            AsynImageUtil.getImageOptions(), null);
                }
                if(i == 1){
                    img_good_2.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(order_goods.get(i).goods_image_url, img_good_2,
                            AsynImageUtil.getImageOptions(), null);
                }
                if(i == 2){
                    img_good_3.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(order_goods.get(i).goods_image_url, img_good_3,
                            AsynImageUtil.getImageOptions(), null);
                }
            }
            tv_name.setText(order.extend_order_common.reciver_name);
            tv_address.setText(Html.fromHtml(order.extend_order_common.reciver_info.address));
            tv_phone.setText(order.extend_order_common.reciver_info.phone);
            tv_num.setText("共" + j + "件");
            tv_way.setText(order.payment_name);
            tv_delivery.setText(order.store_pstype);
            tv_good_price.setText(order.goods_amount);
            tv_freight_price.setText(order.shipping_fee);
            tv_shop_price.setText(order.order_amount);

            Object invoice_info = order.extend_order_common.invoice_info;
            if(invoice_info instanceof JSONObject){
                HashMap<String, String> invoiceMap = JSON.parseObject(invoice_info.toString(),new TypeReference<HashMap<String, String>>(){});
                Iterator iter = invoiceMap.entrySet().iterator();
                List<String> value = new ArrayList<String>();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    value.add(entry.getValue().toString());
                }
                if(value.size() > 2){
                    tv_bill_state.setText(value.get(2));
                }
                if(value.size() > 1){
                    tv_bill_user.setText(value.get(1));
                }
                if(value.size() > 0){
                    tv_bill_class.setText(value.get(0));
                    tv_bill_state.setVisibility(View.GONE);
                }
            }

            state = order.order_state;
            evaluation_state=order.evaluation_state;
            if("-10".equals(state)){
                tv_delete_order.setText("已取消");
                tv_buy.setVisibility(View.GONE);
            } else if("0".equals(state)){
                tv_delete_order.setText("取消订单");
                tv_buy.setText("立即支付");
            } else if("10".equals(state)){
                if ("1".equals(order.pay_type)) {
                    tv_delete_order.setText("待发货");
                    tv_buy.setVisibility(View.GONE);
                }else if ("0".equals(order.pay_type)) {
                    state = "0";
                    tv_delete_order.setText("取消订单");
                    tv_buy.setText("待发货");
                    tv_buy.setTextColor(Color.parseColor("#666666"));
                    tv_buy.setClickable(false);
                    tv_buy.setBackgroundResource(R.mipmap.btn_order_deletebg);
                }
            } else if("20".equals(state)){
                tv_delete_order.setText("确认收货");
                tv_buy.setVisibility(View.GONE);
            } else if("40".equals(state)&&"0".equals(evaluation_state)){
                tv_delete_order.setText("评价晒单");
                tv_buy.setVisibility(View.GONE);
            } else if("40".equals(state)&&"1".equals(evaluation_state)){
                tv_delete_order.setText("已完成");
                tv_buy.setVisibility(View.GONE);
            } else if("90".equals(state)){
                tv_delete_order.setText("已关闭");
                tv_buy.setVisibility(View.GONE);
            } else if("100".equals(state)){
                tv_delete_order.setText("取消订单");
                tv_buy.setText("未成团");
                tv_buy.setTextColor(Color.parseColor("#666666"));
                tv_buy.setClickable(false);
                tv_buy.setBackgroundResource(R.mipmap.btn_order_deletebg);
            }

        }
    }


    public void initListener(){
        super.initListener();
        initSideBarListener();
        tv_delete_order.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        rl_goods.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(order == null){
            Toast.makeText(OrderDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.tv_delete_order://根据订单状态做不同的响应
                action();
                break;
            case R.id.tv_buy://立即支付
			/*Intent intent = new Intent(OrderDetailActivity.this, PayActivity.class);
			intent.putExtra("cart_sum", order.order_amount);
			intent.putExtra("tradeoutid", order.pay_sn);
			String shopName = "神买";
			if(order.extend_order_goods != null && order.extend_order_goods.size() > 0){
				shopName = order.extend_order_goods.get(0).goods_name;
			}
			intent.putExtra("shopName", shopName);*/
                Intent intent = new Intent(OrderDetailActivity.this, PayWebActivity.class);
                intent.putExtra("order_sn", order.order_sn);
                intent.putExtra("pay_type", order.pay_type);
                startActivity(intent);
                OrderDetailActivity.this.finish();
                break;
            case R.id.rl_goods://订单商品列表
                if(!ListUtils.isEmpty(order.extend_order_goods)){
                    Intent orderGoodsintent = new Intent(OrderDetailActivity.this, OrderGoodsListActivity.class);
                    orderGoodsintent.putExtra("orderGoodsList", (Serializable) order.extend_order_goods);
                    orderGoodsintent.putExtra("orderType", order.order_type);
                    startActivity(orderGoodsintent);
                }
                break;
        }
    }

    /**
     * 根据订单状态做不同的响应
     */
    private void action(){
        if("0".equals(state)|| "100".equals(state)){//取消订单
            getDelOrder();
        }else if("20".equals(state)){//确认收货
            getReceiveOrder();
        }else if("40".equals(state)&&"0".equals(evaluation_state)){
            //跳转到评论界面
            if(!ListUtils.isEmpty(order.extend_order_goods)){
                Intent orderCommentIntent = new Intent(OrderDetailActivity.this, OrderCommentActivity.class);
                orderCommentIntent.putExtra("orderGoodsList", (Serializable) order.extend_order_goods);
                orderCommentIntent.putExtra("orderID", order.order_id);
                startActivityForResult(orderCommentIntent, COMMENT);
            }
        }else if("40".equals(state)&&"1".equals(evaluation_state)){
            //跳转到查看评论界面
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (data!=null) {
            String unClick=data.getStringExtra("commentSucc");
            if (unClick.equals("1")) {
                tv_delete_order.setText("已完成");
                tv_delete_order.setClickable(false);
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

    /**
     * 确认收货信息
     */
    private void getReceiveOrder(){
        httpPostRequest(OrderApi.getReceiveOrderUrl(),
                getReceiveRequestParams(), OrderApi.API_GET_RECEIVE_ORDER);
    }

    /**
     * 获取确认收货参数
     *
     * @return
     */
    private HashMap getReceiveRequestParams(){
        HashMap<String,String> params = new HashMap<>();
        params.put("key", configEntity.key);
        params.put("order_id", order.order_id);
        params.put("order_state", "40");
        return params;
    }

    @Override
    public void httpOnResponse(String json, int action) {
        super.httpOnResponse(json, action);
        switch (action){
            case OrderApi.API_GET_CANCEL_ORDER:
                state = "-10";
                tv_delete_order.setText("已取消");
                tv_buy.setVisibility(View.GONE);
                break;
            case OrderApi.API_GET_RECEIVE_ORDER:
                state = "40";
                tv_delete_order.setText("交易成功");
                tv_buy.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

}