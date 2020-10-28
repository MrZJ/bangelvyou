package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.OrderEntity;
import com.yishangshuma.bangelvyou.entity.OrderGoodsList;
import com.yishangshuma.bangelvyou.entity.OrderListEntity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.ListUtils;

import java.util.List;

/**
 * 订单信息适配器
 */
public class OrderAdapter extends AbsBaseAdapter<OrderListEntity> {

    private List<OrderListEntity> mData;
    LayoutInflater inflater;
    int width;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public OrderAdapter(List<OrderListEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public OrderListEntity getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_info, null);
            holder = new ViewHolder();
            holder.img_order_info_goodpicture = (ImageView) convertView
                    .findViewById(R.id.img_order_info_goodpic);
            holder.tv_order_info_goodname = (TextView) convertView
                    .findViewById(R.id.tv_order_info_goodname);
            holder.tv_order_info_goodprice = (TextView) convertView
                    .findViewById(R.id.tv_order_info_goodpri);
            holder.tv_order_info_orderprivce = (TextView) convertView
                    .findViewById(R.id.tv_order_info_orderpri);
            holder.btn_order_info = (Button) convertView
                    .findViewById(R.id.btn_order_info);
            holder.btn_order_info.setClickable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderListEntity order = getItem(position);
        List<OrderEntity> order_list = order.order_list;
        if(!ListUtils.isEmpty(order_list)){
            List<OrderGoodsList> extend_order_goods = order_list.get(0).extend_order_goods;
            if(!ListUtils.isEmpty(extend_order_goods)){
                OrderGoodsList orderGoods = extend_order_goods.get(0);
                imageLoader.displayImage(orderGoods.goods_image_url, holder.img_order_info_goodpicture,
                        AsynImageUtil.getImageOptions(), null);
                holder.tv_order_info_goodname.setText(orderGoods.goods_name);
                holder.tv_order_info_goodprice.setText("￥" + orderGoods.goods_price);
            }
            holder.tv_order_info_orderprivce.setText("实付款:￥" + order_list.get(0).order_amount);
            //0-已取消  10-未支付 20-已支付 30-已发货 40-交易成功
            String order_state = order_list.get(0).order_state;
            String evaluation_state=order_list.get(0).evaluation_state;
            if("-10".equals(order_state)){
                holder.btn_order_info.setText("已取消");
                holder.btn_order_info.setBackgroundColor(Color.parseColor("#D3D3D3"));
            } else if("0".equals(order_state)){
                holder.btn_order_info.setText("去支付");
                holder.btn_order_info.setBackgroundResource(R.mipmap.login_bg);
            } else if("10".equals(order_state)){
                holder.btn_order_info.setText("待发货");
                holder.btn_order_info.setBackgroundResource(R.mipmap.login_bg);
            } else if("20".equals(order_state)){
                holder.btn_order_info.setText("待收货");
                holder.btn_order_info.setBackgroundResource(R.mipmap.login_bg);
            } else if("40".equals(order_state)&&"0".equals(evaluation_state)){
                holder.btn_order_info.setText("评价晒单");
                holder.btn_order_info.setBackgroundResource(R.mipmap.login_bg);
            } else if("40".equals(order_state)&&"1".equals(evaluation_state)){
                holder.btn_order_info.setText("已完成");
                holder.btn_order_info.setBackgroundColor(Color.parseColor("#D3D3D3"));
            } else if("90".equals(order_state)){
                holder.btn_order_info.setText("已关闭");
                holder.btn_order_info.setBackgroundColor(Color.parseColor("#D3D3D3"));
            } else if("100".equals(order_state)){
                holder.btn_order_info.setText("未成团");
                holder.btn_order_info.setBackgroundColor(Color.parseColor("#D3D3D3"));
            }
        }
        return convertView;
    }

    private class ViewHolder {

        TextView tv_order_info_goodname, tv_order_info_goodprice, tv_order_info_orderprivce;
        ImageView img_order_info_goodpicture;
        Button btn_order_info;

    }

    public void setData(List<OrderListEntity> rentCar) {
        mData = rentCar;
    }

}


