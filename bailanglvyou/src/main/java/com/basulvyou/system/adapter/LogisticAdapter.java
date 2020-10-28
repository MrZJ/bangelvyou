package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 物流信息展示适配器
 */
public class LogisticAdapter extends AbsBaseAdapter<CarpoolingInfoEntity> {

    private List<CarpoolingInfoEntity> mData;
    private LayoutInflater inflater;
    private boolean isList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context ctx;

    public LogisticAdapter(List<CarpoolingInfoEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        ctx = context;
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
//        imageLoader.displayImage(viewEntity.location_img, holder.img_today_hot,
//                AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
        return convertView;
    }

    private class ViewHolder {
        TextView  tv_info_startads, tv_info_endads, tv_info_suggest, tv_info_price, tv_info_dist, tv_info_time;
    }

    public void setData(List<CarpoolingInfoEntity> viewEntity) {
        mData = viewEntity;
    }
}
