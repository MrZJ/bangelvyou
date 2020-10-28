package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 物流信息展示适配器
 */
public class LogisticAdapter extends AbsBaseAdapter<CarpoolingInfoEntity> {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    private List<CarpoolingInfoEntity> mData;
    private LayoutInflater inflater;
    private boolean isList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context ctx;
    private int mType;

    public LogisticAdapter(List<CarpoolingInfoEntity> branchInfos, Context context, int type) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        ctx = context;
        this.mType = type;
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public CarpoolingInfoEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_logistic_info, null);
            holder = new ViewHolder();
            holder.tv_info_startads = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_startads);
            holder.tv_info_endads = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_endads);
            holder.tv_info_suggest = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_suggest);
            holder.tv_info_price = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_price);
            holder.tv_info_dist = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_dist);
            holder.tv_info_time = (TextView) convertView.findViewById(R.id.tv_item_logistic_info_time);
            holder.get_tv = (TextView) convertView.findViewById(R.id.get_tv);
            holder.status_layout = convertView.findViewById(R.id.status_layout);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarpoolingInfoEntity viewEntity = getItem(position);
        holder.tv_info_startads.setText(viewEntity.pcqd);
        holder.tv_info_endads.setText(viewEntity.pczd);
//        if (StringUtil.isEmpty(viewEntity.goods_jdcontent)){//留言
//            holder.tv_info_suggest.setVisibility(View.GONE);
//        } else {
//            holder.tv_info_suggest.setVisibility(View.VISIBLE);
//            holder.tv_info_suggest.setText(viewEntity.goods_jdcontent);
//        }
        holder.tv_info_price.setText("￥" + viewEntity.goods_price);
        holder.tv_info_dist.setText(viewEntity.distance);
        holder.tv_info_time.setText(viewEntity.time);
        if (mType == TYPE_1) {
            holder.status_layout.setVisibility(View.GONE);
            holder.get_tv.setVisibility(View.VISIBLE);
        } else if (mType == TYPE_2) {
            holder.status_layout.setVisibility(View.VISIBLE);
            holder.get_tv.setVisibility(View.GONE);
            holder.time_tv.setText(viewEntity.up_date);
            if (!StringUtil.isEmpty(viewEntity.order_state)) {
                if (viewEntity.order_state.equals("0")) {
                    holder.status_tv.setText("进行中");
                } else if (viewEntity.order_state.equals("-10")) {
                    holder.status_tv.setText("已取消");
                } else if (viewEntity.order_state.equals("90")) {
                    holder.status_tv.setText("已完成");
                } else if (viewEntity.order_state.equals("40")) {
                    holder.status_tv.setText("已确定");
                }
            } else {
                if (!ListUtils.isEmpty(viewEntity.order_list)) {
                    if (viewEntity.order_list.get(0).order_state.equals("0")) {
                        holder.status_tv.setText("进行中");
                    } else if (viewEntity.order_list.get(0).order_state.equals("-10")) {
                        holder.status_tv.setText("已取消");
                    } else if (viewEntity.order_list.get(0).order_state.equals("90")) {
                        holder.status_tv.setText("已完成");
                    } else if (viewEntity.order_list.get(0).order_state.equals("40")) {
                        holder.status_tv.setText("已确定");
                    }
                }
            }
        }
//        imageLoader.displayImage(viewEntity.location_img, holder.img_today_hot,
//                AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_info_startads, tv_info_endads, tv_info_suggest, tv_info_price, tv_info_dist, tv_info_time, get_tv, time_tv, status_tv;
        View status_layout;
    }

    public void setData(List<CarpoolingInfoEntity> viewEntity) {
        mData = viewEntity;
    }
}
