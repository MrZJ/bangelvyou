package com.basulvyou.system.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basulvyou.system.R;
import com.basulvyou.system.entity.LocationEntity;
import com.basulvyou.system.util.AsynImageUtil;
import com.basulvyou.system.util.ListUtils;
import com.basulvyou.system.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/2/3.
 */
public class LocationTodayHotAdapter extends AbsBaseAdapter<LocationEntity> {

    private List<LocationEntity> mData;
    private LayoutInflater inflater;
    private boolean isList;
    private boolean isGroup = false;
    private boolean isSleep = false;
    private boolean isCulter = false;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context context;

    public LocationTodayHotAdapter(List<LocationEntity> branchInfos, Context context) {
        mData = branchInfos;
        this.context = context;
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
            /*holder.tv_today_hot_view = (TextView) convertView.findViewById(R.id.tv_today_hot_view);
            holder.tv_today_hot_save = (TextView) convertView.findViewById(R.id.tv_today_hot_save);
            holder.tv_today_hot_com = (TextView) convertView.findViewById(R.id.tv_today_hot_com);
            holder.iv_today_hot_view = (ImageView) convertView.findViewById(R.id.iv_today_hot_view);
            holder.iv_today_hot_save = (ImageView) convertView.findViewById(R.id.iv_today_hot_save);
            holder.iv_today_hot_com = (ImageView) convertView.findViewById(R.id.iv_today_hot_com);*/
            holder.tv_today_endDate = (TextView) convertView.findViewById(R.id.tv_today_group_enddate);
            holder.tv_today_zhekou = (TextView) convertView.findViewById(R.id.tv_today_group_zhekou);
            holder.tv_today_sleep_price = (TextView) convertView.findViewById(R.id.tv_today_sleep_price);
//            holder.linView = (LinearLayout) convertView.findViewById(R.id.lin_today_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocationEntity viewEntity = getItem(position);
        if (isList) {
            if (viewEntity == null || "".equals(viewEntity)) {
                holder.divider_today_hot.setVisibility(View.VISIBLE);
                holder.ll_today_hot.setVisibility(View.VISIBLE);
            } else {
                holder.divider_today_hot.setVisibility(View.GONE);
                holder.ll_today_hot.setVisibility(View.GONE);
            }
        } else {
            if (position > 0) {
                holder.divider_today_hot.setVisibility(View.GONE);
                holder.ll_today_hot.setVisibility(View.GONE);
            } else {
                holder.divider_today_hot.setVisibility(View.VISIBLE);
                holder.ll_today_hot.setVisibility(View.VISIBLE);
            }
        }
        holder.tv_today_hot_name.setText(viewEntity.location_title);
        if (!StringUtil.isEmpty(viewEntity.location_img_one)) {
            imageLoader.displayImage(viewEntity.location_img_one, holder.img_today_hot,
                    AsynImageUtil.getImageOptions(R.mipmap.loadfailed), null);
        } else {
            if (!ListUtils.isEmpty(viewEntity.location_img)) {
                imageLoader.displayImage(viewEntity.location_img.get(0).comm_image_url, holder.img_today_hot,
                        AsynImageUtil.getImageOptions(R.mipmap.loadfailed), null);
            }
        }
        if (isGroup) {
            holder.tv_today_zhekou.setVisibility(View.VISIBLE);
            holder.tv_today_hot_des.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_today_endDate.setText("截至日期:" + viewEntity.location_end_date);
            holder.tv_today_hot_date.setText(viewEntity.location_group_price);
            holder.tv_today_zhekou.setText("折扣:" + viewEntity.location_group_rebate);
            holder.tv_today_zhekou.setTextColor(Color.RED);
            holder.tv_today_hot_date.setTextColor(Color.RED);
        } else {
            holder.tv_today_zhekou.setVisibility(View.GONE);
            holder.tv_today_endDate.setText(viewEntity.location_address);
        }
        /*if(isCulter){
            holder.tv_today_hot_name.setTextAppearance(context, R.style.LocationItemTextBlack_18);
        }*/
        if (isSleep) {
            holder.tv_today_hot_des.setVisibility(View.GONE);
            holder.tv_today_sleep_price.setVisibility(View.VISIBLE);
            int Nstart = viewEntity.location_price.indexOf("￥") + 1;
            int Nend = viewEntity.location_price.indexOf("起");
            SpannableStringBuilder style = new SpannableStringBuilder(viewEntity.location_price);
            style.setSpan(new TextAppearanceSpan(context, R.style.LocationItemTextBlack_10), 0, Nstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new TextAppearanceSpan(context, R.style.LocationItemTextGreen_14), Nstart, Nend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new TextAppearanceSpan(context, R.style.LocationItemTextBlack_10), Nend, Nend + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_today_sleep_price.setText(style);
        } else {
            if (isGroup) {
                holder.tv_today_hot_des.setText(viewEntity.location_price);
            } else {
                holder.tv_today_hot_des.setText(viewEntity.location_brief);
            }
        }
//        holder.tv_today_hot_save.setText(viewEntity.location_collect_count);
//        holder.tv_today_hot_view.setText(viewEntity.location_visit_count);
//        holder.tv_today_hot_view.setText(viewEntity.location_visit_count);
        /*if(StringUtil.isEmpty(viewEntity.location_collect_count)){
            holder.tv_today_hot_save.setVisibility(View.GONE);
            holder.iv_today_hot_save.setVisibility(View.GONE);
        } else {
            holder.tv_today_hot_save.setText(viewEntity.location_collect_count);
            holder.tv_today_hot_save.setVisibility(View.VISIBLE);
            holder.iv_today_hot_save.setVisibility(View.VISIBLE);
        }*/
/*        if(StringUtil.isEmpty(viewEntity.location_evaluation_count)){
//            holder.tv_today_hot_com.setVisibility(View.GONE);
//            holder.iv_today_hot_com.setVisibility(View.GONE);
        } else {
//            holder.tv_today_hot_com.setText(viewEntity.location_evaluation_count);
//            holder.tv_today_hot_com.setVisibility(View.VISIBLE);
//            holder.iv_today_hot_com.setVisibility(View.VISIBLE);
        }*/
        return convertView;
    }

    private class ViewHolder {
        View divider_today_hot, ll_today_hot;
        TextView tv_today_hot_name, tv_today_hot_des, tv_today_hot_date, tv_today_hot_view, tv_today_hot_save, tv_today_hot_com, tv_today_endDate, tv_today_zhekou, tv_today_sleep_price;
        ImageView img_today_hot, iv_today_hot_view, iv_today_hot_save, iv_today_hot_com;
        LinearLayout linView;
    }

    public void setData(List<LocationEntity> viewEntity) {
        mData = viewEntity;
    }

    /**
     * 是否是当地列表界面
     *
     * @param isList
     */
    public void setIfList(boolean isList) {
        this.isList = isList;
    }

    /**
     * 是否是团购列表
     *
     * @param isGroup
     */
    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * 是否是酒店列表
     *
     * @param isSleep
     */
    public void setIsSleep(boolean isSleep) {
        this.isSleep = isSleep;
    }

    /**
     * 是否是文化
     *
     * @param isCulter
     */
    public void setIsCulter(boolean isCulter) {
        this.isCulter = isCulter;
    }
}
