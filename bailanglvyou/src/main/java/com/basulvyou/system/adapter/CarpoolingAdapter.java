package com.basulvyou.system.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.CarpoolingInfoEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.wibget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 拼车信息适配器
 */
public class CarpoolingAdapter extends AbsBaseAdapter<CarpoolingInfoEntity> {

    private List<CarpoolingInfoEntity> mData;
    private LayoutInflater inflater;
    private boolean isList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context ctx;
    private int fragmetPos;

    public CarpoolingAdapter(List<CarpoolingInfoEntity> branchInfos, Context context, int fragmetPos) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
        ctx = context;
        this.fragmetPos = fragmetPos;
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
            convertView = inflater.inflate(R.layout.item_carpooling_info, null);
            holder = new ViewHolder();
            holder.img_info_icon = (CircleImageView) convertView.findViewById(R.id.img_item_carpooling_info_icon);
            holder.tv_info_date = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_date);
            holder.tv_info_startads = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_startads);
            holder.tv_info_endads = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_endads);
            holder.tv_info_time = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_time);
            holder.tv_info_seat = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_seat);
            holder.tv_info_price = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_price);
            holder.tv_info_dist = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_dist);
            holder.tv_info_gocar = (TextView) convertView.findViewById(R.id.tv_item_carpooling_info_gocar);
            holder.ratingbar = (RatingBar) convertView.findViewById(R.id.ratingbar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarpoolingInfoEntity viewEntity = getItem(position);
        if (viewEntity != null) {
            holder.tv_info_date.setText(viewEntity.down_date);
            holder.tv_info_startads.setText(viewEntity.pcqd);
            holder.tv_info_endads.setText(viewEntity.pczd);
            holder.tv_info_time.setText(viewEntity.time);
            holder.tv_info_seat.setText(viewEntity.inventory + "个座位");
            holder.tv_info_price.setText("￥" + viewEntity.goods_price);
            holder.tv_info_dist.setText(viewEntity.distance);
            if (fragmetPos == 0) {
                holder.tv_info_gocar.setText("去拼车");
            } else if (fragmetPos == 1) {
                holder.tv_info_gocar.setText("待确定");
            } else {
                holder.tv_info_gocar.setText("去评论");
            }
            try {
                float score = 0;
                float rating = 0;
                score = Float.valueOf(viewEntity.credit);
                rating = score / 20;
                holder.ratingbar.setRating(rating);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (viewEntity.comm_type.equals("101")) {//车主
                imageLoader.displayImage(viewEntity.user_logo, holder.img_info_icon,
                        AsynImageUtil.getImageOptions(R.mipmap.car_owner_icon), null);
            } else {//乘客
                imageLoader.displayImage(viewEntity.user_logo, holder.img_info_icon,
                        AsynImageUtil.getImageOptions(R.mipmap.passenger_icon), null);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_info_date, tv_info_startads, tv_info_endads, tv_info_time, tv_info_seat, tv_info_price, tv_info_dist, tv_info_gocar;
        CircleImageView img_info_icon;
        RatingBar ratingbar;
    }

    public void setData(List<CarpoolingInfoEntity> viewEntity) {
        mData = viewEntity;
    }

}
