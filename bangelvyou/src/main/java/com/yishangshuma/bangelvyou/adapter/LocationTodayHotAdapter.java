package com.yishangshuma.bangelvyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.LocationEntity;
import com.yishangshuma.bangelvyou.util.AsynImageUtil;
import com.yishangshuma.bangelvyou.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class LocationTodayHotAdapter extends AbsBaseAdapter<LocationEntity> {

    private List<LocationEntity> mData;
    private LayoutInflater inflater;
    private boolean isList;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public LocationTodayHotAdapter(List<LocationEntity> branchInfos, Context context) {
        mData = branchInfos;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {

        if (null == mData) {
            return 0;
        }
        return mData.size();
    }

    public LocationEntity getItem(int position) {

        return mData.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_location_today_hot, null);
            holder = new ViewHolder();
            holder.divider_today_hot = (View) convertView.findViewById(R.id.divider_today_hot);
            holder.ll_today_hot = (View) convertView.findViewById(R.id.ll_today_hot);
            holder.img_today_hot = (ImageView) convertView.findViewById(R.id.img_today_hot);
            holder.tv_today_hot_name = (TextView) convertView.findViewById(R.id.tv_today_hot_name);
            holder.tv_today_hot_des = (TextView) convertView.findViewById(R.id.tv_today_hot_des);
            holder.tv_today_hot_date = (TextView) convertView.findViewById(R.id.tv_today_hot_date);
            holder.tv_today_hot_view = (TextView) convertView.findViewById(R.id.tv_today_hot_view);
            holder.tv_today_hot_save = (TextView) convertView.findViewById(R.id.tv_today_hot_save);
            holder.tv_today_hot_com = (TextView) convertView.findViewById(R.id.tv_today_hot_com);
            holder.iv_today_hot_view = (ImageView) convertView.findViewById(R.id.iv_today_hot_view);
            holder.iv_today_hot_save = (ImageView) convertView.findViewById(R.id.iv_today_hot_save);
            holder.iv_today_hot_com = (ImageView) convertView.findViewById(R.id.iv_today_hot_com);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocationEntity viewEntity = getItem(position);
        if(isList){
            if(viewEntity == null || "".equals(viewEntity)){
                holder.divider_today_hot.setVisibility(View.VISIBLE);
                holder.ll_today_hot.setVisibility(View.VISIBLE);
            } else {
                holder.divider_today_hot.setVisibility(View.GONE);
                holder.ll_today_hot.setVisibility(View.GONE);
            }
            holder.tv_today_hot_view.setVisibility(View.GONE);
            holder.iv_today_hot_view.setVisibility(View.GONE);
        }else{
            if(position > 0){
                holder.divider_today_hot.setVisibility(View.GONE);
                holder.ll_today_hot.setVisibility(View.GONE);
            }else{
                holder.divider_today_hot.setVisibility(View.VISIBLE);
                holder.ll_today_hot.setVisibility(View.VISIBLE);
            }
            holder.tv_today_hot_save.setVisibility(View.GONE);
            holder.iv_today_hot_save.setVisibility(View.GONE);
        }
        imageLoader.displayImage(viewEntity.location_img, holder.img_today_hot,
                AsynImageUtil.getImageOptions(R.mipmap.location_view), null);
        holder.tv_today_hot_name.setText(viewEntity.location_title);
        holder.tv_today_hot_des.setText(viewEntity.location_brief);
        holder.tv_today_hot_date.setText(viewEntity.location_date);
//        holder.tv_today_hot_view.setText(viewEntity.location_visit_count);
        holder.tv_today_hot_save.setText(viewEntity.location_collect_count);
        /*if(StringUtil.isEmpty(viewEntity.location_collect_count)){
            holder.tv_today_hot_save.setVisibility(View.GONE);
            holder.iv_today_hot_save.setVisibility(View.GONE);
        } else {
            holder.tv_today_hot_save.setText(viewEntity.location_collect_count);
            holder.tv_today_hot_save.setVisibility(View.VISIBLE);
            holder.iv_today_hot_save.setVisibility(View.VISIBLE);
        }*/
        if(StringUtil.isEmpty(viewEntity.location_evaluation_count)){
            holder.tv_today_hot_com.setVisibility(View.GONE);
            holder.iv_today_hot_com.setVisibility(View.GONE);
        } else {
            holder.tv_today_hot_com.setText(viewEntity.location_evaluation_count);
            holder.tv_today_hot_com.setVisibility(View.VISIBLE);
            holder.iv_today_hot_com.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        View divider_today_hot, ll_today_hot;
        TextView tv_today_hot_name, tv_today_hot_des , tv_today_hot_date, tv_today_hot_view, tv_today_hot_save, tv_today_hot_com;
        ImageView img_today_hot, iv_today_hot_view, iv_today_hot_save, iv_today_hot_com;
    }

    public void setData(List<LocationEntity> viewEntity) {
        mData = viewEntity;
    }

    /**
     * 是否是当地列表界面
     * @param isList
     */
    public void setIfList(boolean isList){
        this.isList = isList;
    }
}
